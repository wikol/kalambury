package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * Ranking użytkowników
 * 
 * @author Anna Dymek
 */

public class Ranking extends JPanel {
	private static final long serialVersionUID = 2317946112772863729L;
	private JTable table;
	private JLabel whoIsDrawing, whoIsDrawingInfo;
	private GridBagConstraints tableConstraints = new GridBagConstraints(0, 1,
			2, 3, CENTER_ALIGNMENT, 4, GridBagConstraints.CENTER,
			UNDEFINED_CONDITION, getInsets(), 0, 0),
			whoIsDrawingConstraints = new GridBagConstraints(0, 0, 1, 1,
					GridBagConstraints.HORIZONTAL, 1,
					GridBagConstraints.CENTER, UNDEFINED_CONDITION,
					getInsets(), 0, 0),
			whoIsDrawingInfoConstraints = new GridBagConstraints(1, 0, 1, 1,
					GridBagConstraints.HORIZONTAL, 1,
					GridBagConstraints.CENTER, UNDEFINED_CONDITION,
					getInsets(), 0, 0);

	/**
	 * Pomocnicza funkcja przerabiająca mapę na posotrowaną listę
	 * 
	 * @param users
	 *            - mapa z nazw użytkowników w punkty
	 * @return posortowana po liczbie punktów lista par nazwa użytkownika -
	 *         punkty
	 */
	private ArrayList<Entry<String, Integer>> sortedList(
			Map<String, Integer> users) {
		ArrayList<Entry<String, Integer>> list = new ArrayList<>();
		for (Entry<String, Integer> e : users.entrySet())
			list.add(e);
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> e1,
					Entry<String, Integer> e2) {
				if (e1.getValue().equals(e2.getValue()))
					return e1.getKey().compareTo(e2.getKey());
				return e2.getValue().compareTo(e1.getValue());
			}
		});
		return list;
	}

	/**
	 * Tworzenie tabelki
	 * 
	 * @param list
	 *            - lista, z której tworzę tabelkę posortowaną po liczbie
	 *            punktów
	 * @return tabelka posortowana po liczbie punktów
	 */
	private JTable createTable(ArrayList<Entry<String, Integer>> list) {
		String[] columnNames = { "No.", "Username", "Points" };
		Object[][] data = new Object[list.size() + 1][3];

		for (int i = 0; i < 3; i++)
			data[0][i] = String.format("<html><a><b>%s</b></a></html>",
					columnNames[i]);

		for (int i = 0; i < list.size(); i++) {
			data[i + 1][0] = i + 1;
			data[i + 1][1] = list.get(i).getKey();
			data[i + 1][2] = list.get(i).getValue();
		}
		JTable newTable = new JTable(data, columnNames);

		newTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		newTable.getColumnModel().getColumn(1).setPreferredWidth(120);
		newTable.getColumnModel().getColumn(2).setPreferredWidth(60);
		newTable.setCellEditor(new TableCellEditor() {
			@Override
			public boolean stopCellEditing() {
				return false;
			}

			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				return false;
			}

			@Override
			public void removeCellEditorListener(CellEditorListener l) {
			}

			@Override
			public boolean isCellEditable(EventObject anEvent) {
				return false;
			}

			@Override
			public Object getCellEditorValue() {
				return null;
			}

			@Override
			public void cancelCellEditing() {
			}

			@Override
			public void addCellEditorListener(CellEditorListener l) {
			}

			@Override
			public Component getTableCellEditorComponent(JTable table,
					Object value, boolean isSelected, int row, int column) {
				return null;
			}
		});
		newTable.setBackground(new Color(0xCCCCFF));
		newTable.setVisible(true);
		newTable.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		return newTable;
	}

	/**
	 * Wyświetlanie, kto aktualnie rysuje
	 * 
	 * @param nowDrawingName
	 *            - nazwa aktualnie rysującego użytkownika
	 * @return - miejsce z informacją kto aktualnie rysuje
	 */
	private JLabel setWhoIsDrawingInfo(String nowDrawingName) {
		String formattedName = String.format("<html><a><b>%s</b></a></html>",
				nowDrawingName);
		return new JLabel(formattedName);
	}

	/**
	 * Wyświetla ładnie posortowany ranking
	 * 
	 * @param users
	 *            - mapa z nazw graczy w liczbę ich punktów
	 * @param nowDrawingName
	 *            - użytkownik, który teraz rysuje
	 */
	public void displayRanking(Map<String, Integer> users, String nowDrawingName) {
		ArrayList<Entry<String, Integer>> list = sortedList(users);

		if (this.getComponents().length != 0) {
			for (Component c : this.getComponents())
				c.setVisible(false);
			this.removeAll();
		}
		this.setLayout(new GridBagLayout());

		table = createTable(list);
		whoIsDrawing = new JLabel("Now drawing: ");
		whoIsDrawingInfo = setWhoIsDrawingInfo(nowDrawingName);

		tableConstraints = new GridBagConstraints(0, 1, 2, 3, CENTER_ALIGNMENT,
				4, GridBagConstraints.CENTER, UNDEFINED_CONDITION, getInsets(),
				0, 0);
		whoIsDrawingConstraints = new GridBagConstraints(0, 0, 1, 1,
				GridBagConstraints.HORIZONTAL, 1, GridBagConstraints.CENTER,
				UNDEFINED_CONDITION, getInsets(), 0, 0);
		whoIsDrawingInfoConstraints = new GridBagConstraints(1, 0, 1, 1,
				GridBagConstraints.HORIZONTAL, 1, GridBagConstraints.CENTER,
				UNDEFINED_CONDITION, getInsets(), 0, 0);
		this.add(table, tableConstraints);
		this.add(whoIsDrawing, whoIsDrawingConstraints);
		this.add(whoIsDrawingInfo, whoIsDrawingInfoConstraints);
		this.revalidate();
	}

	/**
	 * Konstruktor klasy wyświetlającej ranking
	 * 
	 * @param users
	 *            - mapa z nazw graczy w liczbę ich punktów
	 * @param nowDrawing
	 * 			  - imię gracza rysującego albo nic
	 */
	public Ranking(Map<String, Integer> users, String... nowDrawing) {
		super();
		displayRanking(users, (nowDrawing.length == 1)?(nowDrawing[0]):("?"));
	}

	public static void main(String... args) {
		Map<String, Integer> users = new HashMap<String, Integer>();
		users.put("Michał", 1000);
		users.put("Anna", 4);
		users.put("Kasia", 1000);
		users.put("Cziter", 100000);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 200);
		Ranking r = new Ranking(users);
		frame.getContentPane().add(r);
		frame.setVisible(true);

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		users.put("prum", 15);
		users.put("Lama", 1000);
		r.displayRanking(users, "?");
		users.put("lelele", 15);
		users.put("nuda", 1000);
		r.displayRanking(users, "?");
	}
}
