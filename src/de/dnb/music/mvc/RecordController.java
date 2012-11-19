package de.dnb.music.mvc;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class RecordController {

	RecordView view;

	RecordModel model;

	private static RecordController rc;

	class ExplicitListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.refresh();
		}

	}

	class AnalyzeListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setOldRecord(view.getOldRecord());
			model.refresh();
		}

	}

	class PicaListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setPicaMode(view.isPicaMode());
			model.refresh();
		}

	}

	class ScriptListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			model.setScriptMode(view.isScriptMode());
			model.refresh();
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
							.getResourceAsStream("/resources/help.txt")));

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
		view.addPicaListener(new PicaListener());
		view.addScriptListener(new ScriptListener());
		view.addErrorListener(new ShowErrorListener());
		view.addInfoListener(new InfoListener());
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HeadlessException 
	 * @throws Exception 
	 */
	public static void main(final String[] args) {
		String userName = System.getProperty("user.name");
		String userDir = System.getProperty("user.dir");
		FileWriter fw;
		try {
			fw = new FileWriter(userDir + "\\log.txt", true);
			fw.append("\n" + userName + "\t" + new Date());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rc = new RecordController();

	}

}
