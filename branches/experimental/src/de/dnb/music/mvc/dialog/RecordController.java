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

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.GNDTitleUtils;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.utils.Misc;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;

public class RecordController {

	RecordView view;

	RecordModel recordModel;

	private static RecordController rc;

	class AnalyzeListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			String old = view.getOldRecord();
			if (!old.trim().isEmpty())
				recordModel.setOldRecord(old);
			else
				recordModel.reset();
		}
	}

	class ExpansionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			recordModel.setExpansion(view.expansionWanted());
		}
	}

	class ComposerListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			recordModel.addComposer(view.getComposer(), view.getCode());
		}
	}

	class InstrumentListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			Instrument ins = view.getInstrument();
			ins.setCount(view.getCount());
			Line line = GNDTitleUtils.makeLine(ins);
			recordModel.add(line);

		}

	}

	class GenreListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			Genre genre = view.getGenre();
			recordModel.add(GNDTitleUtils.makeLine(genre));

		}

	}

	class ShowErrorListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			String stackTrace = recordModel.getStackTrace();
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
			new DialogController(view.getGui(), recordModel);
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
			recordModel.analyzeNewRecordString(newRecStr);
		}

	}

	class UndoListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			recordModel.undo();
		}
	}

	class PicaListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			recordModel.setReturnsPica(view.picaWanted());
		}
	}

	class ExplicitListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			recordModel.setReturnsExplicit(view.explicitWanted());
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
			Misc.showInfo(view.getGui(), "2.00", "/resources/help2.html");
		}

	}

	public RecordController() {
		super();
		this.recordModel = new RecordModel();
		this.view = new RecordView(recordModel);
		recordModel.addObserver(view);
		view.addAnalyzeListener(new AnalyzeListener());
		view.addCompListener(new ComposerListener());
		view.addInstrListener(new InstrumentListener());
		view.addGenreListener(new GenreListener());
		view.addExpansionListener(new ExpansionListener());
		view.addPicaListener(new PicaListener());
		view.addExplicitListener(new ExplicitListener());

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
