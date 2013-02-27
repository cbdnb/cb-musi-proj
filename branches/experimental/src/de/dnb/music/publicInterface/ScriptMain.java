package de.dnb.music.publicInterface;

import utils.InOut;
import utils.StringUtils;
import de.dnb.music.publicInterface.TransformRecord.TransformMode;

public final class ScriptMain {

	private ScriptMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Soll nicht mehr verändert werden, da für den Austausch mit WinIBW
	 * zuständig. Der Austausch geht über die Zwischenablage.
	 * 
	 * Skript:
	 * 
	Sub aaaTest()

	' Schickt den Datensatz zum Transformator und überschreibt mit den neuen Daten
	application.activeWindow.copyTitle
	Application.ActiveWindow.Command "k", False		
		
	set WshShell = CreateObject("Wscript.shell")
	Set oExec = WshShell.Exec("javaw -jar V:\DMA\Anwendungen\dist\TransformRecord.jar")
	msgbox "Daten übernehmen"
	
	inhaltNeu = application.activeWindow.clipboard
	
	if inhaltNeu = "" then 
		Application.ActiveWindow.SimulateIBWKey "FE" 'mit ESCAPE-Taste Korrekturmodus verlassen
		exit sub	
	end if	
	
	application.activeWindow.title.insertText inhaltNeu
	application.activeWindow.title.insertText vbcrlf
	application.activeWindow.title.insertText "------------" & vbcrlf
	application.activeWindow.title.insertText "ALT:" & vbcrlf

	End Sub

	 * @param args In args[0] steht der zu bearbeitende Datensatz.
	 */
	public static void main(final String[] args) {

		String titleStrOld = "";

		if (args == null || args.length != 1) {
			titleStrOld = InOut.readClipboard();
		} else
			titleStrOld = args[0];

		if (titleStrOld == null)
			return;
		//@formatter:on

		String s =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		s = StringUtils.removeExpansion(s);
		String cause = TransformRecord.getRejectionCause();
		if (!"".equals(cause)) {
			s += "\n------------";
			s += cause;
		}
		InOut.write2Clipboard(s);
		System.exit(0);
	}
}
