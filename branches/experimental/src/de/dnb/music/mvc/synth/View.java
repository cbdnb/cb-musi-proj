package de.dnb.music.mvc.synth;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;

public class View implements Observer {

	private GUI theGui;
	private Model theModel;

	public View(final Model model) {
		this.theModel = model;
		theGui = new GUI();
		theGui.setVisible(true);
	}

	// Listener: -------------------------------

	public final void addExpansionListener(final ActionListener al) {
		theGui.checkBoxExpansion.addActionListener(al);
	}

	public final void addPicaListener(final ActionListener al) {
		theGui.checkBoxPicaPlus.addActionListener(al);
	}

	public final void addNumberListener(final ActionListener al) {
		theGui.checkBoxTotalCount.addActionListener(al);
	}

	public final void addInfoListener(final ActionListener al) {
		theGui.mntmInfo.addActionListener(al);
	}

	public final void addNewListener(final ActionListener al) {
		theGui.btnNew.addActionListener(al);
	}

	public final void addGenreListener(final ActionListener al) {
		theGui.btnAddGenre.addActionListener(al);
	}

	public final void addInstrumentListener(final ActionListener al) {
		theGui.btnAddInstrument.addActionListener(al);
	}

	public final void addOpusListener(final ActionListener al) {
		theGui.btnAddOpus.addActionListener(al);
	}

	public final void addIdxListener(final ActionListener al) {
		theGui.btnAddIdx.addActionListener(al);
	}

	public final void addKeyListener(final ActionListener al) {
		theGui.btnAddKey.addActionListener(al);
	}

	public final void addModusListener(final ActionListener al) {
		theGui.btnAddModus.addActionListener(al);
	}

	public final void addYearListener(final ActionListener al) {
		theGui.btnAddYear.addActionListener(al);
	}

	public final void addQualiListener(final ActionListener al) {
		theGui.btnQualifier.addActionListener(al);
	}

	public final void addSerialListener(final ActionListener al) {
		theGui.btnSerial.addActionListener(al);
	}

	public final void addFormalListener(final ActionListener al) {
		theGui.btnAddFormal.addActionListener(al);
	}

	public final void addIndividualListener(final ActionListener al) {
		theGui.btnAddIndiv.addActionListener(al);
	}

	public final void addVersionListener(final ActionListener al) {
		theGui.btnVersion.addActionListener(al);
	}

	public final void addUndoListener(final ActionListener al) {
		theGui.btnUndo.addActionListener(al);
	}

	public final void addComposerListener(final ActionListener al) {
		theGui.comboBoxComposer.addActionListener(al);
	}

	// Combos füllen: --------------------------------------

	public final void addGenre(final Genre genre) {
		theGui.comboBoxGenre.addItem(genre);
		theGui.comboBoxGenreFormal.addItem(genre);
	}

	public final void addGenres(final Collection<Genre> genres) {
		for (Genre genre : genres) {
			addGenre(genre);
		}
	}

	public final void addInstrument(final Instrument instrument) {
		theGui.comboBoxInstru.addItem(instrument);
	}

	public final void addInstruments(final Collection<Instrument> instruments) {
		for (Instrument instrument : instruments) {
			addInstrument(instrument);
		}
	}

	public final void addVersionString(final String version) {
		theGui.comboBoxVersion.addItem(version);
	}

	public final void addVersionStrings(final Collection<String> versions) {
		for (String version : versions) {
			addVersionString(version);
		}
	}

	public final void addComposer(final Composer composer) {
		theGui.comboBoxComposer.addItem(composer);
	}

	public final void addComposers(final Collection<Composer> composers) {
		for (Composer composer : composers) {
			addComposer(composer);
		}
	}

	public final void addOpus(final String opus) {
		theGui.comboBoxOpus.addItem(opus);
	}

	public final void addOpera(final Collection<String> opera) {
		for (String opus : opera) {
			addOpus(opus);
		}
	}

	public final void addIdx(final String idx) {
		theGui.comboBoxIdx.addItem(idx);
	}

	public final void addIndices(final Collection<String> indices) {
		for (String idx : indices) {
			addIdx(idx);
		}
	}

	public final void addSerial(final String ser) {
		theGui.comboBoxSerial.addItem(ser);
	}

	public final void addSerials(final Collection<String> serials) {
		for (String ser : serials) {
			addSerial(ser);
		}
	}

	public final void addKeyName(final String key) {
		theGui.comboBoxKeyName.addItem(key);
	}

	public final void addKeyNames(final Collection<String> keys) {
		for (String key : keys) {
			addKeyName(key);
		}
	}

	public final void addModeName(final String mode) {
		theGui.comboBoxModeName.addItem(mode);
	}

	public final void addModeNames(final Collection<String> modes) {
		for (String mode : modes) {
			addModeName(mode);
		}
	}

	@SuppressWarnings("boxing")
	public final void addModusCount(final int c) {
		theGui.comboBoxModusNumber.addItem(c);
	}

	@SuppressWarnings("boxing")
	public final void addInstrumentCount(final int c) {
		theGui.comboBoxCount.addItem(c);
	}

	// Sichtbarkeit: --------------------------------

	public final void enableNewTitle() {
		theGui.btnAddFormal.setEnabled(true);
		theGui.btnAddIndiv.setEnabled(true);

		theGui.btnVersion.setEnabled(false);

		theGui.btnAddGenre.setEnabled(false);
		theGui.btnAddInstrument.setEnabled(false);
		theGui.btnAddOpus.setEnabled(false);
		theGui.btnAddIdx.setEnabled(false);
		theGui.btnAddKey.setEnabled(false);
		theGui.btnAddYear.setEnabled(false);
		theGui.btnSerial.setEnabled(false);
		theGui.btnAddModus.setEnabled(false);
		theGui.btnQualifier.setEnabled(false);

	}

	public final void enableAll() {
		theGui.btnAddFormal.setEnabled(true);
		theGui.btnAddIndiv.setEnabled(true);

		theGui.btnVersion.setEnabled(true);

		theGui.btnAddGenre.setEnabled(true);
		theGui.btnAddInstrument.setEnabled(true);
		theGui.btnAddOpus.setEnabled(true);
		theGui.btnAddIdx.setEnabled(true);
		theGui.btnAddKey.setEnabled(true);
		theGui.btnAddYear.setEnabled(true);
		theGui.btnSerial.setEnabled(true);
		theGui.btnAddModus.setEnabled(true);
		theGui.btnQualifier.setEnabled(true);
	}

	public final void disableAll() {
		theGui.btnAddFormal.setEnabled(false);
		theGui.btnAddIndiv.setEnabled(false);

		theGui.btnVersion.setEnabled(false);

		theGui.btnAddGenre.setEnabled(false);
		theGui.btnAddInstrument.setEnabled(false);
		theGui.btnAddOpus.setEnabled(false);
		theGui.btnAddIdx.setEnabled(false);
		theGui.btnAddKey.setEnabled(false);
		theGui.btnAddYear.setEnabled(false);
		theGui.btnSerial.setEnabled(false);
		theGui.btnAddModus.setEnabled(false);
		theGui.btnQualifier.setEnabled(false);
	}

	public final void disableAfterVersion() {
		theGui.btnAddFormal.setEnabled(false);
		theGui.btnAddIndiv.setEnabled(false);

		theGui.btnVersion.setEnabled(false);

		theGui.btnAddGenre.setEnabled(true);
		theGui.btnAddInstrument.setEnabled(true);
		theGui.btnAddOpus.setEnabled(true);
		theGui.btnAddIdx.setEnabled(true);
		theGui.btnAddKey.setEnabled(true);
		theGui.btnAddYear.setEnabled(true);
		theGui.btnSerial.setEnabled(true);
		theGui.btnAddModus.setEnabled(true);
		theGui.btnQualifier.setEnabled(false);
	}

	public final void setTip(final String tip) {
		theGui.textPaneTip.setText(tip);
		if (tip.isEmpty())
			theGui.lblTip.setText("");
		else
			theGui.lblTip.setText("Tip:");
	}

	public final void setUndoVisible(final boolean v) {
		theGui.btnUndo.setVisible(v);
	}

	// Getter: -------------------------------

	public final Genre getGenre() {
		return (Genre) theGui.comboBoxGenre.getSelectedItem();
	}

	public final Instrument getInstrument() {
		return (Instrument) theGui.comboBoxInstru.getSelectedItem();
	}

	@SuppressWarnings("boxing")
	public final int getCount() {
		return (Integer) theGui.comboBoxCount.getSelectedItem();
	}

	public final Genre getNewTitleGenre() {
		return (Genre) theGui.comboBoxGenreFormal.getSelectedItem();
	}

	public final String getNewIndivTitle() {
		String title = theGui.textFieldIndivTitle.getText();
		if (title.isEmpty())
			throw new IllegalArgumentException("Bitte "
				+ "Individualtitel eingeben");
		return title;
	}

	public final String getVersionPhrase() {
		return (String) theGui.comboBoxVersion.getSelectedItem();
	}

	public final String getOpus() {
		String number = theGui.textFieldOpus.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("Opuszahl eingeben");
		return ((String) theGui.comboBoxOpus.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getIdx() {
		String number = theGui.textFieldIdx.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("WV-Nummer eingeben");
		return ((String) theGui.comboBoxIdx.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getSerial() {
		String number = theGui.textFieldSerial.getText();
		if (number.isEmpty())
			throw new IllegalArgumentException("Fortlaufende Zählung eingeben");
		return ((String) theGui.comboBoxSerial.getSelectedItem()) + " "
			+ number.trim();
	}

	public final String getKey() {
		String mode = (String) theGui.comboBoxModeName.getSelectedItem();
		String key = (String) theGui.comboBoxKeyName.getSelectedItem();
		if (Key.istMollig(mode))
			key = key.toLowerCase();
		return key + "-" + mode;
	}

	public final String getModus() {
		@SuppressWarnings("boxing")
		String number =
			Integer.toString((Integer) theGui.comboBoxModusNumber
					.getSelectedItem());
		return number + ". Ton";
	}

	public final String getYear() {
		String year = theGui.textFieldYear.getText();
		if (year.isEmpty())
			throw new IllegalArgumentException("Jahreszahl eingeben");
		return year;
	}

	public final String getQualifier() {
		String qualifier = theGui.textFieldQualifier.getText();
		if (qualifier.isEmpty())
			throw new IllegalArgumentException("Zusatz eingeben");
		return qualifier;
	}

	public final Composer getComposer() {
		return (Composer) theGui.comboBoxComposer.getSelectedItem();
	}

	public final boolean expansionWanted() {
		return theGui.checkBoxExpansion.isSelected();
	}

	public final boolean picaWanted() {
		return theGui.checkBoxPicaPlus.isSelected();
	}

	public final boolean numberWanted() {
		return theGui.checkBoxTotalCount.isSelected();
	}

	@Override
	public final void update(final Observable obs, final Object message) {
		theGui.textAreaGND.setText(theModel.getGND());
		theGui.textAreaStruct.setText(theModel.getStructured());
		theGui.textAreaAleph.setText(theModel.getAleph());
	}

	public final Component getTheGui() {
		return theGui;
	}

}
