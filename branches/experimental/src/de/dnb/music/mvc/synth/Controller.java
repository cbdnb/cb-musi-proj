package de.dnb.music.mvc.synth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;
import de.dnb.music.version.VersionDB;

public class Controller {

	private GUI gui;

	private Model model;

	private View view;

	class NewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			model.reset();
			model.refresh();
		}
	}

	class NewFormalListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FormalTitle title = new FormalTitle();
			title.addGenre(view.getNewTitleGenre());
			model.addElement(title);
			model.refresh();
		}
	}

	class NewIndividualListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				IndividualTitle title = new IndividualTitle(
						view.getNewIndivTitle());
				model.addElement(title);
				model.refresh();
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, ex.getMessage(),
						"Falsche Eingabe", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}

	class NewGenreListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			model.addElement(view.getGenre());
			model.refresh();
		}
	}

	class NewInstrumentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Instrument instrument = view.getInstrument().clone();
				instrument.setCount(view.getCount());
				model.addElement(instrument);
				model.refresh();
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
				OpusNumber opusNumber = ParseAdditionalInformation
						.matchOpus(opus);
				model.addElement(opusNumber);
				model.refresh();
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
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
				ThematicIndexNumber idxNumber = ParseAdditionalInformation
						.matchThematicIndex(null, idx);
				model.addElement(idxNumber);
				model.refresh();
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
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
				SerialNumber serNumber = ParseAdditionalInformation
						.matchSerialNumber(ser, true);
				model.addElement(serNumber);
				model.refresh();
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
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
				DateOfComposition year = ParseAdditionalInformation
						.matchDate(y);
				model.addElement(year);
				model.refresh();
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
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
			model.addElement(key);
			model.refresh();
		}
	}

	class ModusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String k = view.getModus();
			Key key = ParseAdditionalInformation.matchKey(k);
			model.addElement(key);
			model.refresh();
		}
	}

	class VersionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String v = view.getVersionPhrase();
			Version version;
			if (v.isEmpty())
				version = new Version();
			else
				version = ParseVersion.parse(null, v);
			System.err.println(version.getRakPhrase());
			model.addElement(version);
			model.refresh();
		}
	}

	class ExpansionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setExpansion(view.expansionWanted());
			model.refresh();
		}
	}

	class PicaListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setReturnsPica(view.picaWanted());
			model.refresh();
		}
	}

	class NumberListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setForceTotalCount(view.numberWanted());
			model.refresh();
		}
	}

	class InfoListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"d. M. yyyy 'um' H:mm 'Uhr'");
			Date date = model.getCreationDate();
			String dateStr = formatter.format(date);
			String info = "Version 2.00"
					+ "\nErstellt in und für die Deutsche Nationalbibliothek"
					+ "\nAutor: Christian Baumann\n" + "Erstellungsdatum: "
					+ dateStr;

			info += "\n\nKurzanleitung:\n - ";
			JTextArea ar = new JTextArea(info);
			ar.setEditable(false);
			ar.setLineWrap(true);
			ar.setWrapStyleWord(true);
			ar.setBackground(UIManager.getColor("Label.background"));
			JScrollPane scrollpane = new JScrollPane(ar);
			JOptionPane jOpPane = new JOptionPane(scrollpane,
					JOptionPane.PLAIN_MESSAGE);
			JDialog jDialog = jOpPane.createDialog(view.getGui(),
					"   Info zum \"Musiktitel-Analysator\"");
			jDialog.setSize(500, 700);
			jDialog.setLocationRelativeTo(null);
			jDialog.setResizable(true);
			jDialog.setVisible(true);
		}
	}

	public Controller() {
		super();
		this.model = new Model();
		this.view = new View(model);
		model.addObserver(view);

		view.addNewListener(new NewListener());

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

		view.addExpansionListener(new ExpansionListener());
		view.addNumberListener(new NumberListener());
		view.addPicaListener(new PicaListener());
		view.addInfoListener(new InfoListener());

		view.addVersionString("");
		view.addVersionStrings(VersionDB.getAllVersionPhrases());

		view.addGenres(GenreDB.getAllGenres());
		view.addInstruments(InstrumentDB.getAllInstruments());
		for (int i = 1; i < 10; i++)
			view.addInstrumentCount(i);

		view.addIndices(ThematicIndexDB.getThematicIndices());
		view.addOpera(OpusNumber.getOperaPhrases());
		view.addSerial("");
		view.addSerials(SerialNumber.getSerialNumberPhrases());
		view.addKeyNames(Key.getKeyNames());
		view.addModeNames(Key.getModeNames());
		for (int i = 1; i <= 13; i++)
			view.addModusCount(i);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new Controller();

	}

}
