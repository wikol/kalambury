package pl.uj.edu.tcs.kalambury_maven.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa do logowania, inspirowana Androidowym Log. Klasa wypisuje wiadomości na
 * standartowym wyjściu (errory na err), oraz zapisuje je do pliku. Zapis do
 * pliku można wyłączyć. Wypisywanie na standartowe wyjście też. Plik znajduje się w folderze "./logi", format nazwy
 * pliku to "yyyy-MM-dd_HH-mm-ss".
 * 
 * @author Anna Szybalska
 * 
 */
public class Log {
	// ------ public -----

	/**
	 * Ustawia czy klasa powinna zapisywać logi do pliku. Domyślne ustawienie to
	 * true.
	 * 
	 * @param saveToFile
	 *            true jeśli logi mają być zapisywane, false jeśli nie
	 */
	public static void setSaveToFile(boolean saveToFile) {
		Log.saveToFile = saveToFile;
	}
	
	/**
	 * Ustawia czy klasa powinna wypisywać logi na System.out. Domyślne ustawienie to
	 * true.
	 * 
	 * @param saveToFile
	 *            true jeśli logi mają być wypisywane, false jeśli nie
	 */
	public static void setWriteToOut(boolean write) {
		Log.writeToOut = write;
	}
	/**
	 * Służy do logowania debugu.
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void d(String tag, String message, Throwable throwable) {
		addMessage(TYPE.DEBUG, whoCalledMe(), tag, message, throwable);
	}

	/**
	 * Służy do logowania debugu.
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void d(String tag, String message) {
		addMessage(TYPE.DEBUG, whoCalledMe(), tag, message, null);
	}

	/**
	 * Służy do logowania debugu.
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void d(String tag, Throwable throwable) {
		addMessage(TYPE.DEBUG, whoCalledMe(), tag, null, throwable);
	}

	/**
	 * Służy do logowania debugu. Jako tag ustawiana jest automatycznie nazwa
	 * klasy która wysłała log.
	 * 
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void d(String message) {
		addMessage(TYPE.DEBUG, whoCalledMe(), null, message, null);
	}

	/**
	 * Służy do logowania debugu. Jako tag ustawiana jest automatycznie nazwa
	 * klasy która wysłała log.
	 * 
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void d(Throwable throwable) {
		addMessage(TYPE.DEBUG, whoCalledMe(), null, null, throwable);
	}

	/**
	 * Służy do logowania wiadomości INFO. (np. "Logowanie udane",
	 * "User przeszedł w stan online").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void i(String tag, String message, Throwable throwable) {
		addMessage(TYPE.INFO, whoCalledMe(), tag, message, throwable);
	}

	/**
	 * Służy do logowania wiadomości INFO. (np. "Logowanie udane",
	 * "User przeszedł w stan online").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void i(String tag, String message) {
		addMessage(TYPE.INFO, whoCalledMe(), tag, message, null);
	}

	/**
	 * Służy do logowania wiadomości INFO. (np. "Logowanie udane",
	 * "User przeszedł w stan online").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void i(String tag, Throwable throwable) {
		addMessage(TYPE.INFO, whoCalledMe(), tag, null, throwable);
	}

	/**
	 * Służy do logowania wiadomości INFO. (np. "Logowanie udane",
	 * "User przeszedł w stan online"). Jako tag ustawiana jest automatycznie
	 * nazwa klasy która wysłała log.
	 * 
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void i(String message) {
		addMessage(TYPE.INFO, whoCalledMe(), null, message, null);
	}

	/**
	 * Służy do logowania wiadomości INFO. (np. "Logowanie udane",
	 * "User przeszedł w stan online"). Jako tag ustawiana jest automatycznie
	 * nazwa klasy która wysłała log.
	 * 
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void i(Throwable throwable) {
		addMessage(TYPE.INFO, whoCalledMe(), null, null, throwable);
	}

	/**
	 * Służy do logowania ostrzeżeń. (np. "Nie udało się wysłać eventu",
	 * "Nie udało się odserializować otrzymanych eventów").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void w(String tag, String message, Throwable throwable) {
		addMessage(TYPE.WARN, whoCalledMe(), tag, message, throwable);
	}

	/**
	 * Służy do logowania ostrzeżeń. (np. "Nie udało się wysłać eventu",
	 * "Nie udało się odserializować otrzymanych eventów").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void w(String tag, String message) {
		addMessage(TYPE.WARN, whoCalledMe(), tag, message, null);
	}

	/**
	 * Służy do logowania ostrzeżeń. (np. "Nie udało się wysłać eventu",
	 * "Nie udało się odserializować otrzymanych eventów").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void w(String tag, Throwable throwable) {
		addMessage(TYPE.WARN, whoCalledMe(), tag, null, throwable);
	}

	/**
	 * Służy do logowania ostrzeżeń. (np. "Nie udało się wysłać eventu",
	 * "Nie udało się odserializować otrzymanych eventów"). Jako tag ustawiana
	 * jest automatycznie nazwa klasy która wysłała log.
	 * 
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void w(String message) {
		addMessage(TYPE.WARN, whoCalledMe(), null, message, null);
	}

	/**
	 * Służy do logowania ostrzeżeń. (np. "Nie udało się wysłać eventu",
	 * "Nie można obsłużyć eventu",
	 * "Nie udało się odserializować otrzymanych eventów"). Jako tag ustawiana
	 * jest automatycznie nazwa klasy która wysłała log.
	 * 
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void w(Throwable throwable) {
		addMessage(TYPE.WARN, whoCalledMe(), null, null, throwable);
	}

	/**
	 * Służy do logowania errorów. (np. "Null pointer", "Cast exception").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void e(String tag, String message, Throwable throwable) {
		addMessage(TYPE.ERROR, whoCalledMe(), tag, message, throwable);
	}

	/**
	 * Służy do logowania errorów. (np. "Null pointer", "Cast exception").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void e(String tag, String message) {
		addMessage(TYPE.ERROR, whoCalledMe(), tag, message, null);
	}

	/**
	 * Służy do logowania errorów. (np. "Null pointer", "Cast exception").
	 * 
	 * @param tag
	 *            Do identyfikacji źródła wiadomości, najczęściej nazwa klasy.
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void e(String tag, Throwable throwable) {
		addMessage(TYPE.ERROR, whoCalledMe(), tag, null, throwable);
	}

	/**
	 * Służy do logowania errorów. (np. "Null pointer", "Cast exception"). Jako
	 * tag ustawiana jest automatycznie nazwa klasy która wysłała log.
	 * 
	 * @param message
	 *            Wiadomość do zalogowania.
	 */
	public static void e(String message) {
		addMessage(TYPE.ERROR, whoCalledMe(), null, message, null);
	}

	/**
	 * Służy do logowania errorów. (np. "Null pointer", "Cast exception"). Jako
	 * tag ustawiana jest automatycznie nazwa klasy która wysłała log.
	 * 
	 * @param throwable
	 *            Wyjątek do zalogowania.
	 */
	public static void e(Throwable throwable) {
		addMessage(TYPE.ERROR, whoCalledMe(), null, null, throwable);
	}

	// ------ private --------

	private static boolean saveToFile = true;
	private static boolean writeToOut = true;
	private static Path pathToFile = null;

	private static StackTraceElement whoCalledMe() {
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		return stackTraceElements[3];
	}

	private Log() {
	}

	private static enum TYPE {
		DEBUG, INFO, WARN, ERROR;
	}

	private static void addMessage(TYPE type,
			StackTraceElement stackTraceElement, String tag, String message,
			Throwable throwable) {
		LogElement logElement = new LogElement(type, stackTraceElement, tag,
				message, throwable);

		// na standartowe wyjście
		if (type == TYPE.ERROR) {
			System.err.println(logElement);
		} else if (writeToOut) {
			System.out.println(logElement);
		}

		// do pliku
		if (saveToFile) {
			File f = new File("logs");
			if (!f.isDirectory()) {
				f.mkdir();
			}
			if (pathToFile == null) {
				pathToFile = Paths.get("./logs/" + getFileName());
			}
			try {
				RandomAccessFile raf = new RandomAccessFile(
						pathToFile.toString(), "rw");
				raf.seek(raf.length());
				raf.writeChars(logElement.toString() + "\n");
				raf.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getFileName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String currentDate = (dateFormat.format(date));
		return currentDate;
	}

	private static class LogElement {
		private TYPE type;
		private StackTraceElement stackTraceElement;
		private String tag;
		private String message;
		private Throwable throwable;

		public LogElement(TYPE type, StackTraceElement stackTraceElement,
				String tag, String message, Throwable throwable) {
			super();
			this.type = type;
			this.stackTraceElement = stackTraceElement;
			this.tag = tag;
			this.message = message;
			this.throwable = throwable;
		}

		@Override
		public String toString() {
			if (tag == null) {
				tag = stackTraceElement.getClassName();
			}
			String result = String.format("%s/%s(%dL):", type.toString(), tag,
					stackTraceElement.getLineNumber());
			if (message != null) {
				result += " " + message;
			}
			if (throwable != null) {
				result += " " + throwable.getStackTrace();
			}
			return result;
		}
	}
}
