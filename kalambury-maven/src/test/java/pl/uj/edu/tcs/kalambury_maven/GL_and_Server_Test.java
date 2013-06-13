package pl.uj.edu.tcs.kalambury_maven;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.GL_and_Model_Test.TestServer;
import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.RiddleEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;
import pl.uj.edu.tcs.kalambury_maven.model.UserRanking;
import pl.uj.edu.tcs.kalambury_maven.server.GameLogic;
import pl.uj.edu.tcs.kalambury_maven.server.GameState;
import pl.uj.edu.tcs.kalambury_maven.server.PointsManager;
import pl.uj.edu.tcs.kalambury_maven.server.RiddlesGenerator;
import pl.uj.edu.tcs.kalambury_maven.server.Server;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GL_and_Server_Test extends TestCase {

	public class TestServer implements Server {
		
		public List<String> Events = new LinkedList<>();
		
		@Override
		public void sendEvent(String name, Event event) {
			Events.add(name);
			Events.add(event.getClass().toString());
		}

		@Override
		public void broadcastEvent(Event event) {
			Events.add("broadcasted");
			Events.add(event.getClass().toString());
		}
	}

	public class TestRiddlesGenerator implements RiddlesGenerator {

		@Override
		public String getCurrentRiddle() {
			// TODO Auto-generated method stub
			return "marchew";
		}

		@Override
		public String nextRiddle() {
			// TODO Auto-generated method stub
			return "marchew";
		}
		
	}
	
	public class TestPointsManager implements PointsManager {
		@Override
		public void updateRankingAfterTimeOver(GameState gs) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void updateRankingAfterGuessing(String guesser, GameState gs) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setRanking(UserRanking rank) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static Test suite() {
		return new TestSuite(GL_and_Server_Test.class);
	}

	/**
	 * simple one in - one out test
	 */
	public void test01() {
		GameLogic gl = new GameLogic();
		TestServer TS = new TestServer(); 
		gl.setServer(TS);

		List<String> events = new LinkedList<>();
		String bdcs = "broadcasted";
		
		gl.reactTo("a", new UsersOnlineEvent("a"));
		events.add("a");
		events.add(ResetUserRankingEvent.class.toString());
		events.add("a");
		events.add(NewPointsDrawnEvent.class.toString());
		events.add("a");
		events.add(NextRoundStartsEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOnlineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo("a", new UsersOfflineEvent("a"));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOfflineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
	}
	
	/**
	 * three in; some failed writting and some good writting - simple, nearly guessed; and some guessing
	 */
	public void test02() {
		GameLogic gl = new GameLogic();
		TestServer TS = new TestServer(); 
		gl.setServer(TS);
		gl.setRiddlesGenerator(new TestRiddlesGenerator());
		gl.setPointsManager(new TestPointsManager());
		
		List<String> events = new LinkedList<>();
		String bdcs = "broadcasted";
		
		gl.reactTo("a", new UsersOnlineEvent("a"));
		events.add("a");
		events.add(ResetUserRankingEvent.class.toString());
		events.add("a");
		events.add(NewPointsDrawnEvent.class.toString());
		events.add("a");
		events.add(NextRoundStartsEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOnlineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo("b", new UsersOnlineEvent("b"));
		events.add("b");
		events.add(ResetUserRankingEvent.class.toString());
		events.add("b");
		events.add(NewPointsDrawnEvent.class.toString());
		events.add("b");
		events.add(NextRoundStartsEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOnlineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(null, new NewGameEvent());		
		events.add(bdcs);
		events.add(ClearScreenEvent.class.toString());
		events.add(bdcs);
		events.add(NextRoundStartsEvent.class.toString());
		events.add(gl.getQueue().peek());
		events.add(RiddleEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo("c", new UsersOnlineEvent("c"));
		events.add("c");
		events.add(ResetUserRankingEvent.class.toString());
		events.add("c");
		events.add(NewPointsDrawnEvent.class.toString());
		events.add("c");
		events.add(NextRoundStartsEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOnlineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(gl.getQueue().peek(), new NewMessageWrittenEvent(gl.getQueue().peek(), "cokolwiek"));
		events.add(gl.getQueue().peek());
		events.add(MessageSendEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		String different = (gl.getQueue().peek().equals("a"))?("b"):("a");
		gl.reactTo(different, new NewMessageWrittenEvent(different, "cos innego"));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(different, new NewMessageWrittenEvent(different, "marchewka"));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		events.add(different);
		events.add(MessageSendEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(different, new NewMessageWrittenEvent(different, "marchew"));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		events.add(bdcs);
		events.add(WordGuessedEvent.class.toString());
		events.add(bdcs);
		events.add(ResetUserRankingEvent.class.toString());
		events.add(bdcs);
		events.add(ClearScreenEvent.class.toString());
		events.add(bdcs);
		events.add(NextRoundStartsEvent.class.toString());
		events.add(gl.getQueue().peek());
		events.add(RiddleEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		different = (gl.getQueue().peek().equals("a"))?("b"):("a");
		gl.reactTo(different, new UsersOfflineEvent(different));
		events.add(bdcs);
		events.add(UsersOfflineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(gl.getQueue().peek(), new UsersOfflineEvent(gl.getQueue().peek()));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		events.add(bdcs);
		events.add(ClearScreenEvent.class.toString());
		events.add(bdcs);
		events.add(NextRoundStartsEvent.class.toString());
		events.add(gl.getQueue().peek());
		events.add(RiddleEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOfflineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
		gl.reactTo(gl.getQueue().peek(), new UsersOfflineEvent(gl.getQueue().peek()));
		events.add(bdcs);
		events.add(MessageSendEvent.class.toString());
		events.add(bdcs);
		events.add(UsersOfflineEvent.class.toString());
		assertTrue(TS.Events.equals(events));
		
	}
}
