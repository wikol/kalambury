package pl.uj.edu.tcs.kalambury_maven;

import java.util.ArrayList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.StartDrawingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Point;
import pl.uj.edu.tcs.kalambury_maven.server.GameLogic;
import pl.uj.edu.tcs.kalambury_maven.server.Server;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GameLogicTest extends TestCase {

	private List<Event> packOfLogs = new ArrayList<>();
	private List<String> packOfNames = new ArrayList<>();
	
	private GameLogic testGameLogic = new GameLogic();
	private TestServer testServer = new TestServer();

	class TestServer implements Server {

		@Override
		public void sendEvent(String name, Event event) {
			packOfLogs.add(event);
			packOfNames.add(name);
		}

		@Override
		public void broadcastEvent(Event event) {
			packOfLogs.add(event);
			packOfNames.add("everyone");
		}

	}
	public GameLogicTest(String testName) {
		super(testName);
	}
	public static Test suite() {
		return new TestSuite(GameLogicTest.class);
	}

	public void UsersOnlineEventTest(String testname) {
//		System.out.println("----> UsersOnlineEvent-test");
		
		assertTrue(packOfLogs.size() == packOfNames.size() && packOfLogs.size() >= 4);
		
		assertEquals(ResetUserRankingEvent.class, packOfLogs.get(0).getClass());
		assertEquals(
				testGameLogic.getModel().getUserRanking().getFullRanking(),
				((ResetUserRankingEvent) packOfLogs.get(0)).getUsersPoints());
		assertEquals(testname, packOfNames.get(0));

		assertEquals(NewPointsDrawnEvent.class, packOfLogs.get(1).getClass());
		assertEquals(testGameLogic.getModel().getDrawingModel().getDrawing(),
				((NewPointsDrawnEvent) packOfLogs.get(1)).getPoints());
		assertEquals(testname, packOfNames.get(1));

		assertEquals(NextRoundStartsEvent.class, packOfLogs.get(2).getClass());
		assertEquals(testGameLogic.getQueue().peek(),
				((NextRoundStartsEvent) packOfLogs.get(2)).getDrawingUser());
		assertEquals(testname, packOfNames.get(2));

		assertEquals(UsersOnlineEvent.class, packOfLogs.get(3).getClass());
		assertEquals(testname, ((UsersOnlineEvent) packOfLogs.get(3)).getUser());
		assertEquals("everyone", packOfNames.get(3));

		if (packOfLogs.size() > 4) {
			for (int i = 0; i < 4; i++) {
				packOfLogs.remove(0);
				packOfNames.remove(0);
			}
			startNextRoundTest();
		}
		packOfLogs.clear();
		packOfNames.clear();
	}
	public void UsersOfflineEventTest(String testname) {
//		System.out.println("----> UsersOfflineEvent-test");
		assertTrue(packOfLogs.size() == packOfNames.size());
		
		if (packOfLogs.size() > 1) {
			assertEquals("everyone", packOfNames.get(0));
			assertEquals(MessageSendEvent.class, packOfLogs.get(0).getClass());

			packOfLogs.remove(0);
			packOfNames.remove(0);

			if (!testGameLogic.getQueue().isEmpty())	//possibly TODO after changes in GameLogic
				startNextRoundTest();
			assertEquals("everyone", packOfNames.get(packOfNames.size() - 1));
			assertEquals(UsersOfflineEvent.class,
					packOfLogs.get(packOfNames.size() - 1).getClass());

		} else {
			assertTrue(packOfLogs.size() == 1);
			
			assertEquals("everyone", packOfNames.get(0));
			assertEquals(UsersOfflineEvent.class, packOfLogs.get(0).getClass());
		}
		packOfLogs.clear();
		packOfNames.clear();
	}
	public void startNextRoundTest() {
//		System.out.println("----> startNextRound-test");
		assertTrue(packOfLogs.size() >= 3);
		
		assertEquals(ClearScreenEvent.class, packOfLogs.get(0).getClass());
		assertEquals("everyone", packOfNames.get(0));

		assertEquals(NextRoundStartsEvent.class, packOfLogs.get(1).getClass());
		assertEquals("everyone", packOfNames.get(1));
		assertEquals(testGameLogic.getQueue().peek(),
				((NextRoundStartsEvent) packOfLogs.get(1)).getDrawingUser());
		
		assertEquals(NewWordIsNeededEvent.class, packOfLogs.get(2).getClass());
		assertEquals(testGameLogic.getQueue().peek(), packOfNames.get(2));
		
	}
	public void NewWordForGuessingEventTest(String testname) {
		if(testGameLogic.getQueue().peek() == testname) {
			assertTrue(packOfLogs.size() == packOfNames.size() && packOfLogs.size() == 1);
			
			assertEquals(StartDrawingEvent.class, packOfLogs.get(0).getClass());
			assertEquals(testname, packOfNames.get(0));
		}
		packOfLogs.clear();
		packOfNames.clear();
	}
	public void ClearScreenEventTest(String testname) {
		assertTrue(packOfLogs.size() == packOfNames.size());
		if(packOfLogs.size() >= 1) {
			assertTrue(packOfLogs.size() == 1);
			
			assertEquals(ClearScreenEvent.class, packOfLogs.get(0).getClass());
			assertEquals("everyone", packOfNames.get(0));
			
			packOfLogs.clear();
			packOfNames.clear();
		}
	}
	public void NewPointsDrawnEventTest(String testname) {
		assertTrue(packOfLogs.size() == packOfNames.size());
		
		if(!(testGameLogic.getQueue().isEmpty() || !testGameLogic.isSomeoneDrawing())) {
			if(testname.equals(testGameLogic.getQueue().peek())) {
				assertTrue(packOfLogs.size() == 1);
				
				assertEquals(NewPointsDrawnEvent.class, packOfLogs.get(0).getClass());
				assertEquals("everyone", packOfNames.get(0));
			}	
		}
		packOfLogs.clear();
		packOfNames.clear();
	}
	public void NewMessageWrittenEventTest(String testname) {
		assertTrue(packOfLogs.size() == packOfNames.size());
		
		if(!testGameLogic.getQueue().isEmpty() && testname.equals(testGameLogic.getQueue().peek())) {
			assertTrue(packOfLogs.size() == 1);
			
			assertEquals(NewMessageWrittenEvent.class, packOfLogs.get(0).getClass());
			assertEquals(testname, packOfNames.get(0));
		}
		else {
			assertEquals(MessageSendEvent.class, packOfLogs.get(0).getClass());
			assertEquals("everyone", packOfNames.get(0));
			assertEquals(testname, ((MessageSendEvent)packOfLogs.get(0)).getUser());
			
			String givenWord = ((MessageSendEvent)packOfLogs.get(0)).getMessage().trim().toLowerCase();
			String trueWord = testGameLogic.getNowBeingDrawnWord().trim().toLowerCase();
			if(testGameLogic.isSomeoneDrawing() && givenWord.equals(trueWord)) {
				
				assertEquals(WordGuessedEvent.class, packOfLogs.get(1).getClass());
				assertEquals(testname, packOfNames.get(1));
				assertEquals(testGameLogic.getNowBeingDrawnWord(), ((WordGuessedEvent) packOfLogs.get(1)).getWord());
				
				packOfLogs.remove(0); packOfLogs.remove(0);
				packOfNames.remove(0); packOfNames.remove(0);
				
				startNextRoundTest();
			}
			else if(packOfLogs.size() == 4) { //był event z areSimilar()
				assertEquals(MessageSendEvent.class, packOfLogs.get(3));
				assertEquals(testname, packOfNames.get(3));
			}
		}
		packOfLogs.clear();
		packOfNames.clear();
	}
	
	/**
	 * simple one in - one out test.
	 */
	public void test00_one_in_one_out() {
		testGameLogic.setServer(testServer);

		testGameLogic.reactTo("test1", new UsersOnlineEvent("test1"));
		UsersOnlineEventTest("test1");
		testGameLogic.reactTo("test1", new UsersOfflineEvent("test1"));
		UsersOfflineEventTest("test1");

	}
	
	/**
	 * some in - some out test.
	 */
	public void test01_some_in_some_out() {
		testGameLogic.setServer(testServer);
		
		testGameLogic.reactTo("test1", new UsersOnlineEvent("test1"));
		UsersOnlineEventTest("test1");
		testGameLogic.reactTo("test2", new UsersOnlineEvent("test2"));
		UsersOnlineEventTest("test2");
		
		testGameLogic.reactTo("test1", new UsersOfflineEvent("test1"));
		UsersOfflineEventTest("test1");
		
		testGameLogic.reactTo("test1", new UsersOnlineEvent("test1"));
		UsersOnlineEventTest("test1");
		
		testGameLogic.reactTo("test2", new UsersOfflineEvent("test2"));
		UsersOfflineEventTest("test2");
		
		testGameLogic.reactTo("test3", new UsersOnlineEvent("test3"));
		UsersOnlineEventTest("test3");
		testGameLogic.reactTo("test4", new UsersOnlineEvent("test4"));
		UsersOnlineEventTest("test4");
		
		testGameLogic.reactTo("test1", new UsersOfflineEvent("test1"));
		UsersOfflineEventTest("test1");
		testGameLogic.reactTo("test4", new UsersOfflineEvent("test4"));
		UsersOfflineEventTest("test4");

	}

	/**
	 * some in - some drawing - some in -
	 * 		- some drawing - some out - some drawing 
	 * 		- everybody out test.
	 */
	public void test02_simple_drawing() {
		testGameLogic.setServer(testServer);
		
		//logowanie dwóch użytkowników
		testGameLogic.reactTo("test1", new UsersOnlineEvent("test1"));
		UsersOnlineEventTest("test1");
		testGameLogic.reactTo("test2", new UsersOnlineEvent("test2"));
		UsersOnlineEventTest("test2");
		//pierwszy podaje słowo do zgadnięcia, po czym oboje próbują rysować
		testGameLogic.reactTo("test1", new NewWordForGuessingEvent("kot"));
		NewWordForGuessingEventTest("test1");
		testGameLogic.reactTo("test1", new NewPointsDrawnEvent(new ArrayList<Point>()));
		NewPointsDrawnEventTest("test1");
		testGameLogic.reactTo("test2", new NewPointsDrawnEvent(new ArrayList<Point>()));
		NewPointsDrawnEventTest("test2");
		//pojawia się nowy użytkownik i próbuje podać hasło do rysowania
		testGameLogic.reactTo("test3", new UsersOnlineEvent("test3"));
		UsersOnlineEventTest("test3");
		testGameLogic.reactTo("test3", new NewWordForGuessingEvent("kot"));
		NewWordForGuessingEventTest("test3");
		//znika użytkownik drugi; użytkownik pierwszy nadal rysuje
		testGameLogic.reactTo("test2", new UsersOfflineEvent("test2"));
		UsersOfflineEventTest("test2");
		testGameLogic.reactTo("test1", new NewPointsDrawnEvent(new ArrayList<Point>()));
		NewPointsDrawnEventTest("test1");
		//znika użytkownik pierwszy (teraz rysuje trójka, ale najpierw musi podać hasło)
		testGameLogic.reactTo("test1", new UsersOfflineEvent("test1"));
		UsersOfflineEventTest("test1");
		testGameLogic.reactTo("test3", new NewPointsDrawnEvent(new ArrayList<Point>()));
		NewPointsDrawnEventTest("test3");
		testGameLogic.reactTo("test3", new NewWordForGuessingEvent("kot"));
		NewWordForGuessingEventTest("test3");
		testGameLogic.reactTo("test3", new NewPointsDrawnEvent(new ArrayList<Point>()));
		NewPointsDrawnEventTest("test3");
		//trójka wychodzi
		testGameLogic.reactTo("test3", new UsersOfflineEvent("test3"));
		UsersOfflineEventTest("test3");
	}

}