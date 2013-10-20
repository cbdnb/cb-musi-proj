package de.dnb.music.mvc.dialog;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import utils.GNDTitleUtils;

import de.dnb.gnd.parser.line.Line;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;

public class RecordController {

	RecordView view;

	RecordModel model;

	private static RecordController rc;

	class AnalyzeListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setOldRecord(view.getOldRecord());
		}
	}

	class ExpansionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setExpansion(view.expansionWanted());
		}
	}

	class ComposerListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.addComposer(view.getComposer(), view.getCode());
		}
	}

	class InstrumentListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			Instrument ins = view.getInstrument();
			ins.setCount(view.getCount());
			Line line = GNDTitleUtils.makeLine(ins);
			model.add(line);

		}

	}

	class GenreListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			Genre genre = view.getGenre();
			model.add(GNDTitleUtils.makeLine(genre));

		}

	}

	class ShowErrorListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			String stackTrace = model.getStackTrace();
			JTextArea ar = new JTextArea(stackTrace);
			JScrollPane scrollpane = new JScrollPane(ar);
			JOptionPane jop =
				new JOptionPane(scrollpane, JOptionPane.PLAIN_MESSAGE);
			JDialog dialog = jop.createDialog(view.getGui(), "Fehler");
			dialog.setSize(500, 500);
			dialog.setResizable(true);
			dialog.setVisible(true);
		}
	}

	class InsertNewTitleListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			new DialogController(view.getGui());
		}

	}

	class NewRecFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			// uninteressant			
		}

		@Override
		public void focusLost(FocusEvent e) {
			// Feld auslesen und in Record verwandeln
			String newRecStr = view.getNewRecord();
			model.analyzeNewRecordString(newRecStr);
		}

	}
	
	class UndoListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.undo();
		}
	}

	/**
	 * @return the help text
	 */
	public static final String getHelp() {

		/*
		 * Die Manifest-Datei der eigenen jar-Datei ist immer aktuell. 
		 * Daher Zugriff auf deren URL:
		 */
		BufferedReader in =
			new BufferedReader(new InputStreamReader(
					RecordController.class
							.getResourceAsStream("/resources/help2.txt")));

		try {
			String help = "";
			String line = in.readLine();
			while (line != null) {
				help += "\n" + line;
				line = in.readLine();
			}
			in.close();
			return help;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler beim Helpfile", JOptionPane.OK_CANCEL_OPTION);
		}

		return null;
	}

	class InfoListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			SimpleDateFormat formatter =
				new SimpleDateFormat("d. M. yyyy 'um' H:mm 'Uhr'");
			Date date = model.getCreationDate();
			String dateStr = formatter.format(date);
			String info =
				"Version 2.00"
					+ "\nErstellt in und für die Deutsche Nationalbibliothek"
					+ "\nAutor: Christian Baumann\n" + "Erstellungsdatum: "
					+ dateStr;
			String help = getHelp();
			info += "\n" + help;
			JTextArea ar = new JTextArea(info);
			ar.setEditable(false);
			ar.setLineWrap(true);
			ar.setWrapStyleWord(true);
			ar.setBackground(UIManager.getColor("Label.background"));
			JScrollPane scrollpane = new JScrollPane(ar);
			JOptionPane jOpPane =
				new JOptionPane(scrollpane, JOptionPane.PLAIN_MESSAGE);
			JDialog jDialog =
				jOpPane.createDialog(view.getGui(),
						"   Info zu \"Bearbeite Titel\"");
			jDialog.setSize(700, 700);
			jDialog.setLocationRelativeTo(null);
			jDialog.setResizable(true);
			jDialog.setVisible(true);
		}

	}

	public RecordController() {
		super();
		this.model = new RecordModel();
		this.view = new RecordView(model);
		model.addObserver(view);
		view.addAnalyzeListener(new AnalyzeListener());
		view.addCompListener(new ComposerListener());
		view.addInstrListener(new InstrumentListener());
		view.addGenreListener(new GenreListener());
		view.addExpansionListener(new ExpansionListener());

		view.addComposers(ThematicIndexDB.getAllComposers());
		view.addGenres(GenreDB.getAllGenres());
		view.addInstruments(InstrumentDB.getAllInstruments());
		for (int i = 1; i < 10; i++)
			view.addCount(i);

		view.addErrorListener(new ShowErrorListener());
		view.addInfoListener(new InfoListener());
		view.addNewTitleListener(new InsertNewTitleListener());
		view.addNewRecFocusListener(new NewRecFocusListener());
		view.addUndoListener(new UndoListener());
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HeadlessException 
	 * @throws Exception 
	 */
	public static void main(final String[] args) {

		rc = new RecordController();

	}

}
