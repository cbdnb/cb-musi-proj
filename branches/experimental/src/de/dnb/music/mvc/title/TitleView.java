package de.dnb.music.mvc.title;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class TitleView implements Observer {
	
	private TitleGUI gui;
	private TitleModel model;
	
	

	public TitleView(final TitleModel model) {
		this.model = model;
		gui = new TitleGUI();
		gui.setVisible(true);
	}
	
	public final String getComposer() {
		return gui.textFieldComposer.getText();
	}
	
	public final String getTitel() {
		return gui.textFieldTitle.getText();
	}
	
	public final boolean getExpansion() {
		return gui.chckbxExpansion.isSelected();
	}
	
	public final boolean getPica() {
		return gui.chckbxPica.isSelected();
	}
	
	public final boolean getNumber() {
		return gui.chckbxNumberInstruments.isSelected();
	}
	
	public final boolean getKeyName() {
		return gui.chckbxKeyName.isSelected();
	}
	
	public final boolean getPopularMusic() {
		return gui.chckbxPopularMusic.isSelected();
	}
	
	public final void addExpansionListener(final ActionListener al) {
		gui.chckbxExpansion.addActionListener(al);
	}
	
	public final void addPicaListener(final ActionListener al) {
		gui.chckbxPica.addActionListener(al);
	}
	
	public final void addNumberListener(final ActionListener al) {
		gui.chckbxNumberInstruments.addActionListener(al);
	}
	
	public final void addKeyNameListener(final ActionListener al) {
		gui.chckbxKeyName.addActionListener(al);
	}
	
	public final void addPopularMusicListener(final ActionListener al) {
		gui.chckbxPopularMusic.addActionListener(al);
	}
	
	public final void addAnalyzeListener(final ActionListener al) {
		gui.textFieldComposer.addActionListener(al);
		gui.textFieldTitle.addActionListener(al);
		gui.btnAnalysieren.addActionListener(al);
	}
	
	public final void addInfoListener(
			final ActionListener infoListener) {
		gui.mntmInfo.addActionListener(infoListener);		
	}

	@Override
	public final void update(final Observable obs, final Object message) {
		gui.textFieldRAK.setText(model.getRAK());
		gui.textFieldRSWK.setText(model.getRSWK());
		gui.textAreaGND.setText(model.getGND());
		gui.textAreaStructured.setText(model.getStructured());
		gui.textAreaSetOfRules.setText(model.getSetOfRules());
	}

	public final Component getGui() {
		return gui;
	}

}
