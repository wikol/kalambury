package pl.uj.edu.tcs.kalambury_maven;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.GL_and_Model_Test.TestServer;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
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

	
}
