package de.dnb.music.mvc.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import utils.GNDConstants;
import utils.GNDTitleUtils;

import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.gnd.utils.Misc;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;
import de.dnb.music.version.VersionDB;

public class DialogController {

	private DialogModel dialogModel;

	private DialogView view;

	private RecordModel recordModel;

	class NewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setTip("Bitte zunächst Individual- "
				+ "oder Formalsachtitel eingeben");
			dialogModel.reset();
		}
	}

	class NewFormalListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FormalTitle title = new FormalTitle();
			title.addGenre(view.getNewTitleGenre());
			view.setTip("");
			dialogModel.addElement(title);
		}
	}

	class NewIndividualListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				IndividualTitle title =
					new IndividualTitle(view.getNewIndivTitle());
				view.setTip("");
				dialogModel.addElement(title);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class NewGenreListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialogModel.addElement(view.getGenre());
		}
	}

	class NewInstrumentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Instrument instrument = view.getInstrument().clone();
				instrument.setCount(view.getCount());
				dialogModel.addElement(instrument);
			} catch (CloneNotSupportedException e1) {
				// nix
			}
		}
	}

	class OpusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String opus = view.getOpus();
				OpusNumber opusNumber =
					ParseAdditionalInformation.matchOpus(opus);
				dialogModel.addElement(opusNumber);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class IdxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String idx = view.getIdx();
				ThematicIndexNumber idxNumber =
					ParseAdditionalInformation.matchThematicIndex(null, idx);
				dialogModel.addElement(idxNumber);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class SerialListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String ser = view.getSerial();
				SerialNumber serNumber =
					ParseAdditionalInformation.matchSerialNumber(ser, true);
				dialogModel.addElement(serNumber);
			} catch (IllegalArgumentException ex) {
				//				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class YearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String y = view.getYear();
				DateOfComposition year =
					ParseAdditionalInformation.matchDate(y);
				dialogModel.addElement(year);
			} catch (IllegalArgumentException ex) {
				//				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class QualifListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String q = view.getQualifier();
				Qualifier qualif = new Qualifier(q);
				dialogModel.addElement(qualif);
			} catch (IllegalArgumentException ex) {
				//				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class KeyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String k = view.getKey();
			Key key = ParseAdditionalInformation.matchKey(k);
			dialogModel.addElement(key);
		}
	}

	class ModusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String k = view.getModus();
			Key key = ParseAdditionalInformation.matchKey(k);
			dialogModel.addElement(key);
		}
	}

	class VersionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String v = view.getVersionPhrase();
			Version version;
			String message = "";
			if (v.isEmpty()) {
				version = new Version();
				message =
					"Sie müssen jetzt noch entweder"
						+ "\n\ta) eine WV-Nummer oder eine Opusnummer\toder"
						+ "\n\tb) einen Gattungsbegriff "
						+ "\n\t     und evtl. Besetzung, "
						+ "Tonart, Jahr oder Zählung" + "\neingeben";
			} else {

				version = ParseVersion.parse(null, v);
				if (version == null
					|| (version.getFallgruppeParagraphM511() == 'c' && version
							.getUntergruppe() == 5)) {
					version = new Version(v);
					message =
						"Keine weiteren Eingaben mehr sinnvoll, "
							+ "\nda eine eigene Fassungsphrase erzeugt wurde";
				} else {
					message =
						"Sie können zusätzlich noch"
							+ "\n\ta) eine Jahreszahl und/oder"
							+ "\n\tb) eine Besetzung" + "\neingeben";
				}
			}
			boolean success = dialogModel.addElement(version);
			if (success)
				view.setTip(message);
		}
	}

	class UndoListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			dialogModel.undo();
		}
	}

	class InfoListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			Misc.showInfo(this, "1.00", "/resources/helpSynth.txt");
		}
	}

	class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			view.getTheGui().dispose();
		}
	}

	class InsertTitleListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			MusicTitle musicTitle = dialogModel.getTitle();
			if (musicTitle != null) {
				String number = view.getFieldNumber();
				Tag tag = GNDConstants.TAG_130;
				if (number.charAt(0) == '4')
					tag = GNDConstants.TAG_430;
				Collection<Line> lines =
					GNDTitleUtils.getLines(tag, musicTitle);
				for (Line line : lines) {
					recordModel.add(line);
				}				
			}
			view.getTheGui().dispose();
		}

	}

	public DialogController(Frame parent, RecordModel recordModel) {
		super();

		this.dialogModel = new DialogModel();
		this.view = new DialogView(dialogModel, parent);
		this.recordModel = recordModel;

		dialogModel.addObserver(view);

		view.enableAll();
		view.setUndoVisible(true);
		view.addUndoListener(new UndoListener());

		view.addFormalListener(new NewFormalListener());
		view.addIndividualListener(new NewIndividualListener());

		view.addVersionListener(new VersionListener());

		view.addGenreListener(new NewGenreListener());
		view.addInstrumentListener(new NewInstrumentListener());

		view.addOpusListener(new OpusListener());
		view.addIdxListener(new IdxListener());
		view.addSerialListener(new SerialListener());
		view.addYearListener(new YearListener());
		view.addKeyListener(new KeyListener());
		view.addModusListener(new ModusListener());
		view.addQualiListener(new QualifListener());

		view.addInfoListener(new InfoListener());

		view.addVersionString("");
		view.addVersionStrings(VersionDB.getAllVersionPhrases());

		view.addGenres(GenreDB.getAllRAKGenres());
		view.addInstruments(InstrumentDB.getAllInstruments());

		for (int i = 1; i < 10; i++) {
			view.addInstrumentCount(i);
		}

		view.addIndices(ThematicIndexDB.getThematicIndices());
		view.addOpera(OpusNumber.getOperaPhrases());
		view.addSerial("");
		view.addSerials(SerialNumber.getSerialNumberPhrases());
		view.addKeyNames(Key.getKeyNames());
		view.addModeNames(Key.getModeNames());
		for (int i = 1; i <= 13; i++)
			view.addModusCount(i);
		view.addFieldNumber("1XX");
		view.addFieldNumber("4XX");
		view.addCancelListener(new CancelListener());
		view.addInsertListener(new InsertTitleListener());

		view.setTip("Bitte zunächst Individual- oder Formalsachtitel eingeben");
		view.setModality();
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1170, 721);
		frame.setVisible(true);
		new DialogController(frame, null);

	}

}
