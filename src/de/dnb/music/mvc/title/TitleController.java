package de.dnb.music.mvc.title;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class TitleController {

	TitleView view;

	TitleModel model;

	class ExpansionListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setExpansion(view.getExpansion());
			model.refresh();
		}

	}

	class PicaListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setReturnsPica(view.getPica());
			model.refresh();
		}

	}

	class NumberListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setForceTotalCount(view.getNumber());
			model.refresh();
		}

	}

	class KeyNameListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setRegnognizeKeyName(view.getKeyName());
			model.refresh();
		}

	}

	class PopularMusicListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setRegnognizePopularMusic(view.getPopularMusic());
			model.refresh();
		}

	}

	class AnalyzeListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setComposer(view.getComposer());
			model.setTitle(view.getTitel());
			model.refresh();
		}

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

			info +=
				"\n\nKurzanleitung:\n - "
					+ "Der Analysator ist eine Oberfläche "
					+ "zur Demonstration der aktuellen Fähigkeiten der "
					+ "Software zur Analyse von Werktiteln der Musik. Analysiert "
					+ "werden Titel im RAK- oder RSWK-Format. Dabei wird nicht "
					+ "zwischen den verschiedenen RAK-Versionen unterschieden.	"
					+ "\n - Die Vorgehensweise ist so, dass die Software versucht, in "
					+ "einem ersten Schritt Fassungen und Werkteile zu "
					+ "extrahieren. Ist das geschehen, so wird in einem zweiten "
					+ "Schritt versucht, den Rest als Formalsachtitel zu "
					+ "interpretieren. Gelingt das nicht, so wird davon "
					+ "ausgegangen, dass ein Individualsachtitel vorliegt.	"
					+ "\n - Ein einmal analysierter Werktitel wird versuchsweise im "
					+ "RAK- oder RSWK-Format ausgegeben. Dabei wird versucht, "
					+ "RAK-Musik in der Fassung von 2003 zu erzeugen. "
					+ "Programmiert wurde die Software jedoch vor allem dazu, "
					+ "die Werktitel in das GND-Format zu transformieren."
					+ "\n - Der Analysator erkennt in der aktuellen Version Fassungen und "
					+ "Teile von Werken. Auch das GND-Format wird erkannt und "
					+ "als RAK oder RSWK ausgegeben."
					+ "\n\n - In die Felder links oben können Sie Komponisten "
					+ "(nur benötigt zur Überprüfung auf Korrektheit des "
					+ "Werkverzeichnisses) und Werktitel angeben."
					+ "\n - Durch Eingabe von ENTER oder durch Anklicken von "
					+ "\"Analysieren\" (rechts oben) können Sie eine Ausgabe im "
					+ "GND-Format erzeugen."
					+ "\n - Im GND-Ausgabefeld sind Instrumente (382) und "
					+ "Gattungen (380) standardmäßig nicht expandiert, durch "
					+ "Klick auf die Box rechts neben „Expansion“ können Sie "
					+ "dies ändern."
					+ "\n - Das Pica+-Feldformat kann durch Klicken auf die "
					+ "entsprechende Check-Box erzeugt werden."
					+ "\n - Um eine Ausgabe der Gesamtzahl aller Musizierenden "
					+ "zu erzwingen, können Sie die Check-Box \"Gesamtzahl\" "
					+ "anklicken. Diese Gesamtzahl macht allerdings nur bei "
					+ "Kammermusik Sinn. Ist diese Checkbox nicht markiert, so "
					+ "wird eine Gesamtzahl nur dann ausgegeben, wenn alle "
					+ "Instrumente mit Zahlen versehen sind (also ausgegeben für "
					+ "Vl 1 2 Vc 1 2 3; nicht ausgegeben für Vl 1 2 Va)"
					+ "\n - Standardmäßig werden Abkürzungen der U-Musik "
					+ "(kleingeschrieben) nicht erkannt. Denn das führte "
					+ "erfahrungsgemäß zu Kollisionen mit anderen Abkürzungen "
					+ "(h = Horn und h = Tonart). Sollen diese Abkürzungen "
					+ "erkannt werden oder sollen einzelne Tonbuchstaben "
					+ "(z.B. nicht regelgerecht: \"Messe C\") erkannt werden, "
					+ "können Sie auch dies ändern. (Boxen rechts oben)"
					+ "\n - Die Felder RAK und RSWK sind angelegt, um "
					+ "(hoffentlich) die zur Zeit korrekte Version zu erzeugen. "
					+ "Sie können natürlich auch dazu dienen, aus der einen in "
					+ "die andere Form zu übersetzen. Sie werden mit der "
					+ "Einführung der GND eigentlich überflüssig."
					+ "\n - Im Feld \"strukturiert\" sehen Sie eine möglichst "
					+ "weitgehende Analyse der eingegebenen Daten. Alle diese "
					+ "Daten werden zur Gewinnung der 3XX-Felder in der GND "
					+ "herangezogen.";
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
						"   Info zum \"Musiktitel-Analysator\"");
			jDialog.setSize(500, 700);
			jDialog.setLocationRelativeTo(null);
			jDialog.setResizable(true);
			jDialog.setVisible(true);
		}
	}

	public TitleController() {
		super();
		this.model = new TitleModel();
		this.view = new TitleView(model);
		model.addObserver(view);

		view.addAnalyzeListener(new AnalyzeListener());
		view.addExpansionListener(new ExpansionListener());
		view.addKeyNameListener(new KeyNameListener());
		view.addNumberListener(new NumberListener());
		view.addPicaListener(new PicaListener());
		view.addPopularMusicListener(new PopularMusicListener());
		view.addInfoListener(new InfoListener());

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new TitleController();

	}

}
