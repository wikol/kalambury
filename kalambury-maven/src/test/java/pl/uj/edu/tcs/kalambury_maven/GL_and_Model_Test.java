package pl.uj.edu.tcs.kalambury_maven;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.model.ChatMessage;
import pl.uj.edu.tcs.kalambury_maven.model.Point;
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

public class GL_and_Model_Test extends TestCase {

	public class TestServer implements Server {

		@Override
		public void sendEvent(String name, Event event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void broadcastEvent(Event event) {
			// TODO Auto-generated method stub

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
		return new TestSuite(GL_and_Model_Test.class);
	}

	/**
	 * simple one in - one out test
	 */
	public void test01() {
		GameLogic gl = new GameLogic();
		gl.setServer(new TestServer());

		List<String> users = new ArrayList<>();

		gl.reactTo("a", new UsersOnlineEvent("a"));
		users.add("a");
		assertTrue(gl.getModel().getUserRanking().getUsersOnline()
				.equals(users));
		assertTrue(gl.getQueue().peek().equals("a"));

		gl.reactTo("a", new UsersOfflineEvent("a"));
		users.remove(users.get(0));
		assertTrue(gl.getModel().getUserRanking().getUsersOnline()
				.equals(users));
	}
	/**
	 * three in; some failed writting and some good writting; and some guessing
	 */
	public void test02() {
		GameLogic gl = new GameLogic();
		gl.setServer(new TestServer());
		gl.setRiddlesGenerator(new TestRiddlesGenerator());
		gl.setPointsManager(new TestPointsManager());
		
		SimpleModel sm = new SimpleModel();
		List<String> users = new ArrayList<>();

		gl.reactTo("a", new UsersOnlineEvent("a"));
		users.add("a");
		gl.reactTo("b", new UsersOnlineEvent("b"));
		users.add("b");
		for (int i = 0; i < users.size(); i++)
			assertTrue(gl.getModel().getUserRanking().getUsersOnline()
					.contains(users.get(i)));
		assertTrue(gl.getQueue().peek().equals("a"));

		gl.reactTo(null, new NewGameEvent());
		assertTrue(gl.getModel().getDrawingModel().getDrawing().isEmpty());

		gl.reactTo("c", new UsersOnlineEvent("c"));
		users.add("c");
		for (int i = 0; i < users.size(); i++)
			assertTrue(gl.getModel().getUserRanking().getUsersOnline()
					.contains(users.get(i)));

		gl.reactTo("a", new NewMessageWrittenEvent("a", "cokolwiek"));
		assertTrue(gl.getModel().getChatMessagesList()
				.getMessagesList().isEmpty());
		gl.reactTo("b", new NewMessageWrittenEvent("b", "cos innego"));
		
		int counter = 0;
		for (int i = 0; i < gl.getModel().getChatMessagesList()
				.getMessagesList().size(); i++)
			if(!gl.getModel().getChatMessagesList().getMessagesList().get(i).getMessage()
					.equals("cos innego"))
				++counter;
		assertTrue(counter < gl.getModel().getChatMessagesList()
				.getMessagesList().size());
		
		gl.reactTo("c", new NewMessageWrittenEvent("c", "marchew"));
		assertTrue(gl.getQueue().peek().equals("b"));
		
		gl.reactTo("a", new UsersOfflineEvent("a"));
		gl.reactTo("b", new UsersOfflineEvent("b"));
		gl.reactTo("c", new UsersOfflineEvent("c"));
		assertTrue(gl.getQueue().isEmpty());
	}
	/**
	 * three in; failed drawing; one out; good drawing; the drawing one out; good drawing by the last.
	 */
	public void test03() {
		GameLogic gl = new GameLogic();
		gl.setServer(new TestServer());
		gl.setRiddlesGenerator(new TestRiddlesGenerator());
		PointsManager pn = new TestPointsManager();
		gl.setPointsManager(pn);
		
		SimpleModel sm = new SimpleModel();
		List<String> users = new ArrayList<>();

		gl.reactTo("a", new UsersOnlineEvent("a"));
		users.add("a");
		gl.reactTo("b", new UsersOnlineEvent("b"));
		users.add("b");
		gl.reactTo("c", new UsersOnlineEvent("c"));
		users.add("c");
		gl.reactTo(null, new NewGameEvent());
		
		List<Point> l = new LinkedList<>();
		l.add(new Point(0.1f, 0.1f, 1.0f, Color.BLACK));
		l.add(new Point(0.5f, 0.4f, 5.0f, Color.DARK_GRAY));
		
		List<Point> f = new LinkedList<>();
		f.add(new Point(0.2f, 0.2f, 1.0f, Color.BLUE));
		
		String current = gl.getQueue().peek();
		
		gl.reactTo(current, new NewPointsDrawnEvent(l));
		gl.reactTo(((current.equals("a"))?("b"):("a")), new NewPointsDrawnEvent(f));
		
		assertTrue(gl.getModel().getDrawingModel().getDrawing().equals(l));
		
		gl.reactTo(current, new UsersOfflineEvent(current));
		current = gl.getQueue().peek();
		
		l.clear();
		l.add(new Point(0.0f, 0.0f, 0.0f, Color.RED));
		gl.reactTo(current, new NewPointsDrawnEvent(l));
		
		assertTrue(gl.getModel().getDrawingModel().getDrawing().equals(l));
		gl.reactTo(current, new UsersOfflineEvent(current));
		current = gl.getQueue().peek();
		gl.reactTo(current, new UsersOfflineEvent(current));
		
		assertTrue(gl.getModel().getUserRanking().getUsersOnline().isEmpty());
	}

}
