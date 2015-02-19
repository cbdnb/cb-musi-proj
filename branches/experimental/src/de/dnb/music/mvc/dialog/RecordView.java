package de.dnb.music.mvc.dialog;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mvc.dialog.RecordController.InfoListener;

public class RecordView implements Observer {

	private RecordGUI gui;
	private RecordModel model;
	private Map<String, Composer> name2Composer =
		new HashMap<String, Composer>();

	public RecordView(final RecordModel model) {
		this.model = model;
		gui = new RecordGUI();
		gui.setVisible(true);
		gui.tabbedPane.setSelectedComponent(gui.panelPica);
	}

	// Listener ----------------------------

	public final void addAnalyzeListener(final ActionListener al) {
		gui.btnAnalyse.addActionListener(al);
	}

	public final void addErrorListener(final ActionListener al) {
		gui.mntmZeigeFehler.addActionListener(al);
	}

	public final void addInfoListener(final InfoListener infoListener) {
		gui.mntmInfo.addActionListener(infoListener);
	}

	public final void addGenreListener(final ActionListener al) {
		gui.btnGenre.addActionListener(al);
	}

	public final void addInstrListener(final ActionListener al) {
		gui.btnInstrument.addActionListener(al);
	}

	public final void addCompListener(final ActionListener al) {
		gui.btnComp.addActionListener(al);
	}

	public final void addNewRecFocusListener(final FocusListener fl) {
		gui.textAreaPica.addFocusListener(fl);
	}

	public final void addNewTitleListener(final ActionListener al) {
		gui.btnAddTitle.addActionListener(al);
	}

	public final void addPicaListener(final ActionListener al) {
		gui.chckbxPica.addActionListener(al);
	}

	public final void addExplicitListener(final ActionListener al) {
		gui.chckbxExplicit.addActionListener(al);
	}

	public final void addExpansionListener(final ActionListener al) {
		gui.chckbxExpansion.addActionListener(al);
	}

	public final void addUndoListener(final ActionListener al) {
		gui.btnUndo.addActionListener(al);
	}

	// Combos füllen: --------------------------------------

	public final void addGenre(final Genre genre) {
		gui.comboBoxGenre.addItem(genre);
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

	public final void addComposer(final Composer composer) {
		name2Composer.put(composer.toString(), composer);
		gui.comboBoxComp.addItem(composer.toString());
	}

	public final void addComposers(final List<Composer> composers) {

		for (Composer composer : composers) {
			addComposer(composer);
		}
	}

	@SuppressWarnings("boxing")
	public final void addCount(final int c) {
		gui.comboBoxCount.addItem(c);
	}

	// getter: -------------------------------

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

	public final Composer getComposer() {
		String name = (String) gui.comboBoxComp.getSelectedItem();
		return name2Composer.get(name);
	}

	public final String getCode() {
		return gui.txtCode.getText();
	}

	public final String getOldRecord() {
		return gui.textAreaOldRecord.getText();
	}

	public final String getNewRecord() {
		return gui.textAreaPica.getText();
	}

	public final RecordGUI getGui() {
		return gui;
	}

	public final boolean expansionWanted() {
		return gui.chckbxExpansion.isSelected();
	}

	public final boolean explicitWanted() {
		return gui.chckbxExplicit.isSelected();
	}

	public final boolean picaWanted() {
		return gui.chckbxPica.isSelected();
	}

	@Override
	public final void update(final Observable obs, final Object message) {
		gui.textAreaPica.setText(model.getPica());
		gui.textAreaAleph.setText(model.getAleph());
	}

}
