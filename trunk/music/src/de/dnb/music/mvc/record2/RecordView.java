package de.dnb.music.mvc.record2;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mvc.record2.RecordController.InfoListener;

public class RecordView implements Observer {

	private RecordGUI gui;
	private RecordModel model;

	public RecordView(final RecordModel model) {
		this.model = model;
		gui = new RecordGUI();
		gui.setVisible(true);
	}

	public void addAnalyzeListener(final ActionListener al) {
		gui.btnAnalysieren.addActionListener(al);
	}


	public void addErrorListener(final ActionListener al) {
		gui.mntmZeigeFehler.addActionListener(al);
	}

	public void addInfoListener(final InfoListener infoListener) {
		gui.mntmInfo.addActionListener(infoListener);
	}

	public void addGenreListener(final ActionListener al) {
		gui.btnGenre.addActionListener(al);
	}

	public void addInstrListener(final ActionListener al) {
		gui.btnInstrument.addActionListener(al);
	}

	public void addCompListener(final ActionListener al) {
		gui.btnComp.addActionListener(al);
	}
	
	public void addRemoveListener(final ActionListener al) {
		gui.btnExpansionEntfernen.addActionListener(al);
	}

	public void addGenre(final Genre genre) {
		gui.comboBoxGenre.addItem(genre);
	}
	
	public final void addGenres(final Collection<Genre> genres) {
		for (Genre genre : genres) {
			addGenre(genre);
		}
	}

	public void addInstrument(final Instrument instrument) {
		gui.comboBoxInstru.addItem(instrument);
	}

	public final void addInstruments(final Collection<Instrument> instruments) {
		for (Instrument instrument : instruments) {
			addInstrument(instrument);
		}
	}

	public void addComposer(final Composer composer) {
		gui.comboBoxComp.addItem(composer);
	}

	public final void addComposers(final Collection<Composer> composers) {
		for (Composer composer : composers) {
			addComposer(composer);
		}
	}

	public Genre getGenre() {
		return (Genre) gui.comboBoxGenre.getSelectedItem();
	}

	public Instrument getInstrument() {
		return (Instrument) gui.comboBoxInstru.getSelectedItem();
	}
	
	public void addCount(int c) {
		gui.comboBoxCount.addItem(c);
	}
	
	public int getCount() {
		return (Integer) gui.comboBoxCount.getSelectedItem();
	}

	public Composer getComposer() {
		return (Composer) gui.comboBoxComp.getSelectedItem();
	}
	
	public String getCode() {
		return gui.txtCode.getText();
	}

	@Override
	public void update(final Observable obs, final Object message) {
		gui.textAreaNew.setText(model.getNewRecord());
	}

	public String getOldRecord() {
		return gui.textAreaOld.getText();
	}
	
	public String getNewRecord() {
		return gui.textAreaNew.getText();
	}

	public final void setNewRecord(String text) {
		gui.textAreaNew.setText(text);
	}

	public Component getGui() {
		return gui;
	}

}
