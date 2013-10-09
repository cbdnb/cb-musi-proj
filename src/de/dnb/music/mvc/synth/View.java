package de.dnb.music.mvc.synth;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import de.dnb.music.additionalInformation.Key;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;

public class View implements Observer {

	private GUI gui;
	private Model model;

	public View(final Model model) {
		this.model = model;
		gui = new GUI();
		gui.setVisible(true);
	}

	// Listener: -------------------------------
	
	public final void addExpansionListener(final ActionListener al) {
		gui.checkBoxExpansion.addActionListener(al);
	}

	public final void addPicaListener(final ActionListener al) {
		gui.checkBoxPicaPlus.addActionListener(al);
	}

	public final void addNumberListener(final ActionListener al) {
		gui.checkBoxTotalCount.addActionListener(al);
	}

	public final void addInfoListener(final ActionListener al) {
		gui.mntmInfo.addActionListener(al);
	}

	public final void addNewListener(final ActionListener al) {
		gui.btnNew.addActionListener(al);
	}

	public final void addGenreListener(final ActionListener al) {
		gui.btnAddGenre.addActionListener(al);
	}

	public final void addInstrumentListener(final ActionListener al) {
		gui.btnAddInstrument.addActionListener(al);
	}

	public final void addOpusListener(final ActionListener al) {
		gui.btnAddOpus.addActionListener(al);
	}

	public final void addIdxListener(final ActionListener al) {
		gui.btnAddIdx.addActionListener(al);
	}

	public final void addKeyListener(final ActionListener al) {
		gui.btnAddKey.addActionListener(al);
	}

	public final void addModusListener(final ActionListener al) {
		gui.btnAddModus.addActionListener(al);
	}

	public final void addYearListener(final ActionListener al) {
		gui.btnAddYear.addActionListener(al);
	}
	
	public final void addQualiListener(final ActionListener al) {
		gui.btnQualifier.addActionListener(al);
	}

	public final void addSerialListener(final ActionListener al) {
		gui.btnSerial.addActionListener(al);
	}

	public final void addFormalListener(final ActionListener al) {
		gui.btnAddFormal.addActionListener(al);
	}

	public final void addIndividualListener(final ActionListener al) {
		gui.btnAddIndiv.addActionListener(al);
	}

	public final void addVersionListener(final ActionListener al) {
		gui.btnVersion.addActionListener(al);
	}
	
	public final void addUndoListener(final ActionListener al) {
		gui.btnUndo.addActionListener(al);
	}
	
	// Combos füllen: --------------------------------------

	public final void addGenre(final Genre genre) {
		gui.comboBoxGenre.addItem(genre);
		gui.comboBoxGenreFormal.addItem(genre);
	}

	public final void addGenres(final Collection<Genre> genres) {
		for (Genre genre : genres) {
			addGenre(genre);
		}
	}

	public final void addInstrument(final Instrument instrument) {
		gui.comboBoxInstru.addItem(instrument);
	}

	public final void addInstruments(final Collection<Instrument> instruments) {
		for (Instrument instrument : instruments) {
			addInstrument(instrument);
		}
	}

	public final void addVersionString(final String version) {
		gui.comboBoxVersion.addItem(version);
	}

	public final void addVersionStrings(final Collection<String> versions) {
		for (String version : versions) {
			addVersionString(version);
		}
	}

	public final void addOpus(final String opus) {
		gui.comboBoxOpus.addItem(opus);
	}

	public final void addOpera(final Collection<String> opera) {
		for (String opus : opera) {
			addOpus(opus);
		}
	}

	public final void addIdx(final String idx) {
		gui.comboBoxIdx.addItem(idx);
	}

	public final void addIndices(final Collection<String> indices) {
		for (String idx : indices) {
			addIdx(idx);
		}
	}

	public final void addSerial(final String ser) {
		gui.comboBoxSerial.addItem(ser);
	}

	public final void addSerials(final Collection<String> serials) {
		for (String ser : serials) {
			addSerial(ser);
		}
	}

	public final void addKeyName(final String key) {
		gui.comboBoxKeyName.addItem(key);
	}

	public final void addKeyNames(final Collection<String> keys) {
		for (String key : keys) {
			addKeyName(key);
		}
	}

	public final void addModeName(final String mode) {
		gui.comboBoxModeName.addItem(mode);
	}

	public final void addModeNames(final Collection<String> modes) {
		for (String mode : modes) {
			addModeName(mode);
		}
	}

	@SuppressWarnings("boxing")
	public final void addModusCount(final int c) {
		gui.comboBoxModusNumber.addItem(c);
	}

	@SuppressWarnings("boxing")
	public final void addInstrumentCount(final int c) {
		gui.comboBoxCount.addItem(c);
	}
	
	// Sichtbarkeit: --------------------------------

	public void enableNewTitle() {
		gui.btnAddFormal.setEnabled(true);
		gui.btnAddIndiv.setEnabled(true);

		gui.btnVersion.setEnabled(false);

		gui.btnAddGenre.setEnabled(false);
		gui.btnAddInstrument.setEnabled(false);
		gui.btnAddOpus.setEnabled(false);
		gui.btnAddIdx.setEnabled(false);
		gui.btnAddKey.setEnabled(false);
		gui.btnAddYear.setEnabled(false);
		gui.btnSerial.setEnabled(false);
		gui.btnAddModus.setEnabled(false);
		gui.btnQualifier.setEnabled(false);

	}

	public void enableAll() {
		gui.btnAddFormal.setEnabled(true);
		gui.btnAddIndiv.setEnabled(true);

		gui.btnVersion.setEnabled(true);

		gui.btnAddGenre.setEnabled(true);
		gui.btnAddInstrument.setEnabled(true);
		gui.btnAddOpus.setEnabled(true);
		gui.btnAddIdx.setEnabled(true);
		gui.btnAddKey.setEnabled(true);
		gui.btnAddYear.setEnabled(true);
		gui.btnSerial.setEnabled(true);
		gui.btnAddModus.setEnabled(true);
		gui.btnQualifier.setEnabled(true);
	}

	public void disableAll() {
		gui.btnAddFormal.setEnabled(false);
		gui.btnAddIndiv.setEnabled(false);

		gui.btnVersion.setEnabled(false);

		gui.btnAddGenre.setEnabled(false);
		gui.btnAddInstrument.setEnabled(false);
		gui.btnAddOpus.setEnabled(false);
		gui.btnAddIdx.setEnabled(false);
		gui.btnAddKey.setEnabled(false);
		gui.btnAddYear.setEnabled(false);
		gui.btnSerial.setEnabled(false);
		gui.btnAddModus.setEnabled(false);
		gui.btnQualifier.setEnabled(false);
	}

	public void disableAfterVersion() {
		gui.btnAddFormal.setEnabled(false);
		gui.btnAddIndiv.setEnabled(false);

		gui.btnVersion.setEnabled(false);

		gui.btnAddGenre.setEnabled(true);
		gui.btnAddInstrument.setEnabled(true);
		gui.btnAddOpus.setEnabled(true);
		gui.btnAddIdx.setEnabled(true);
		gui.btnAddKey.setEnabled(true);
		gui.btnAddYear.setEnabled(true);
		gui.btnSerial.setEnabled(true);
		gui.btnAddModus.setEnabled(true);
		gui.btnQualifier.setEnabled(false);
	}

	public void setTip(String tip) {
		gui.textPaneTip.setText(tip);
		if (tip.isEmpty())
			gui.lblTip.setText("");
		else
			gui.lblTip.setText("Tip:");
	}

	public void setUndoVisible(boolean v) {
		gui.btnUndo.setVisible(v);
	}

	// Getter: -------------------------------

	public final Genre getGenre() {
		return (Genre) gui.comboBoxGenre.getSelectedItem();
	}

	public final Instrument getInstrument() {
		return (Instrument) gui.comboBoxInstru.getSelectedItem();
	}

	@SuppressWarnings("boxing")
	public final int getCount() {
		return (Integer) gui.comboBoxCount.getSelectedItem();
	}

	public final Genre getNewTitleGenre() {
		return (Genre) gui.comboBoxGenreFormal.getSelectedItem();
	}

	public final String getNewIndivTitle() {
		String title = gui.textFieldIndivTitle.getText();
		if (title.isEmpty())
			throw new IllegalArgumentException("Bitte Individualtitel eingeben");
		return title;
	}

	public final String getVersionPhrase() {
		return (String) gui.comboBoxVersion.getSelectedItem();
	}

	public final String getOpus() {
		String number = gui.textFieldOpus.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("Opuszahl eingeben");
		return ((String) gui.comboBoxOpus.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getIdx() {
		String number = gui.textFieldIdx.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("WV-Nummer eingeben");
		return ((String) gui.comboBoxIdx.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getSerial() {
		String number = gui.textFieldSerial.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("Fortlaufende Zählung eingeben");
		return ((String) gui.comboBoxSerial.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getKey() {
		String mode = (String) gui.comboBoxModeName.getSelectedItem();
		String key = (String) gui.comboBoxKeyName.getSelectedItem();
		if (Key.istMollig(mode))
			key = key.toLowerCase();
		return key + "-" + mode;
	}

	public final String getModus() {
		@SuppressWarnings("boxing")
		String number =
			Integer.toString((Integer) gui.comboBoxModusNumber
					.getSelectedItem());
		return number + ". Ton";
	}

	public final String getYear() {
		String year = gui.textFieldYear.getText();
		if (year.isEmpty())
			throw new IllegalArgumentException("Jahreszahl eingeben");
		return year;
	}
	
	public final String getQualifier() {
		String qualifier = gui.textFieldQualifier.getText();
		if (qualifier.isEmpty())
			throw new IllegalArgumentException("Zusatz eingeben");
		return qualifier;
	}

	public final boolean expansionWanted() {
		return gui.checkBoxExpansion.isSelected();
	}

	public final boolean picaWanted() {
		return gui.checkBoxPicaPlus.isSelected();
	}

	public final boolean numberWanted() {
		return gui.checkBoxTotalCount.isSelected();
	}

	@Override
	public final void update(final Observable obs, final Object message) {
		gui.textAreaGND.setText(model.getGND());
		gui.textAreaStruct.setText(model.getStructured());
		gui.textAreaAleph.setText(model.getAleph());
	}

	public final Component getGui() {
		return gui;
	}

}