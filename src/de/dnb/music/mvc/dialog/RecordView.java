package de.dnb.music.mvc.dialog;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mvc.dialog.RecordController.InfoListener;


public class RecordView implements Observer {

	private RecordGUI gui;
	private RecordModel model;

	public RecordView(final RecordModel model) {
		this.model = model;
		gui = new RecordGUI();
		gui.setVisible(true);
	}

	public final void addAnalyzeListener(final ActionListener al) {
		gui.btnAnalysieren.addActionListener(al);
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
		gui.textAreaNew.addFocusListener(fl);
	}
	
	

	public final void addRemoveListener(final ActionListener al) {
		gui.btnExpansionAendern.addActionListener(al);
	}
	
	public final void addTitleListener(final ActionListener al) {
		gui.btnTitle.addActionListener(al);
		gui.textFieldTitle.addActionListener(al);
	}
	
	public final void addFieldNumber(final String number) {
		gui.comboBoxFieldNumber.addItem(number);
	}
	
	public final void addFieldNumbers(final Collection<String> numbers) {
		for (String number : numbers) {
			addFieldNumber(number);
		}
	}
	
	

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
		gui.comboBoxComp.addItem(composer);
	}

	public final void addComposers(final Collection<Composer> composers) {
		for (Composer composer : composers) {
			addComposer(composer);
		}
	}

	public final Genre getGenre() {
		return (Genre) gui.comboBoxGenre.getSelectedItem();
	}

	public final Instrument getInstrument() {
		return (Instrument) gui.comboBoxInstru.getSelectedItem();
	}
	
	public final String getFieldNumber() {
		return (String) gui.comboBoxFieldNumber.getSelectedItem();
	}
	
	public final String getTitleString() {
		return gui.textFieldTitle.getText();
	}

	@SuppressWarnings("boxing")
	public final void addCount(final int c) {
		gui.comboBoxCount.addItem(c);
	}

	@SuppressWarnings("boxing")
	public final int getCount() {
		return (Integer) gui.comboBoxCount.getSelectedItem();
	}

	public final Composer getComposer() {
		return (Composer) gui.comboBoxComp.getSelectedItem();
	}

	public final String getCode() {
		return gui.txtCode.getText();
	}

	@Override
	public final void update(final Observable obs, final Object message) {
		gui.textAreaNew.setText(model.getNewRecordString());
	}

	public final String getOldRecord() {
		return gui.textAreaOld.getText();
	}
	
	public final String getNewRecord() {
		return gui.textAreaNew.getText();
	}

	public final Component getGui() {
		return gui;
	}

}
