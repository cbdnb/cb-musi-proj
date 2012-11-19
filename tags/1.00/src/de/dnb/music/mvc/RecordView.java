package de.dnb.music.mvc;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import de.dnb.music.mvc.RecordController.InfoListener;

public class RecordView implements Observer {
	
	private RecordGUI gui;
	private RecordModel model;
	
	

	public RecordView(final RecordModel model) {
		this.model = model;
		gui = new RecordGUI();
		gui.setVisible(true);
	}
	
	
	public void addAnalyzeListener(ActionListener al) {
		gui.btnAnalysieren.addActionListener(al);
	}
	
	public void addPicaListener(ActionListener al) {
		gui.chckbxPica.addActionListener(al);
	}
	
	public void addScriptListener(ActionListener al) {
		gui.chckbxScript.addActionListener(al);
	}
	
	public void addErrorListener(ActionListener al) {
		gui.mntmZeigeFehler.addActionListener(al);
	}
	
	public void addInfoListener(
			InfoListener infoListener) {
		gui.mntmInfo.addActionListener(infoListener);
		
	}

	@Override
	public void update(final Observable obs, final Object message) {
		gui.textAreaNew.setText(model.getNewRecord());
	}


	public String getOldRecord() {
		return gui.textAreaOld.getText();
	}
	
	public void setNewRecord(String text) {
		gui.textAreaNew.setText(text);
	}
	
	public boolean isPicaMode() {
		return gui.chckbxPica.isSelected();
	}
	
	public boolean isScriptMode() {
		return gui.chckbxScript.isSelected();
	}


	public Component getGui() {
		return gui;
	}


	

}
