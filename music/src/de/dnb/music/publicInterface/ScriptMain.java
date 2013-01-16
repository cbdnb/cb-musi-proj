package de.dnb.music.publicInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import de.dnb.music.publicInterface.TransformRecord.TransformMode;

public final class ScriptMain {

	private ScriptMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Soll nicht mehr verändert werden, da für den Austausch mit WinIBW
	 * zuständig.
	 * 
	 * Skript:
	 * 
	Sub aaaTest()

	application.activeWindow.copyTitle
	Application.ActiveWindow.Command "k", False	
	application.activeWindow.title.selectAll		

	volltext = application.activeWindow.clipboard	
	
	'  " durch \" ersetzen, um Text mit Anfuehrungszeichen korrekt zu _
	uebergeben:
	volltext = Replace(volltext , CStr(Chr(34)),"\"&Chr(34)) 		
	' in Anführungszeichen einschliessen, um an Applikation zu _
	uebergeben								
	volltext = Chr(34) & volltext & Chr(34) 
	'msgbox volltext
		
	set WshShell = CreateObject("Wscript.shell")
		Set oExec = WshShell.Exec("javaw -jar _
		V:\DMA\Anwendungen\dist\TransformRecord.jar " & volltext)
	set rein = oExec.StdOut
	inhaltNeu = rein.readall
	'msgbox inhaltNeu 
	
	if inhaltNeu = "" then 
		Application.ActiveWindow.SimulateIBWKey "FE" 'mit ESCAPE-Taste _
		Korrekturmodus verlassen
		exit sub	
	end if
	
	application.activeWindow.title.insertText inhaltNeu
	
	msgbox "Alter Datensatz:" & vbcrlf & volltext

	End Sub

	 * @param args In args[0] steht der zu bearbeitende Datensatz.
	 */
	public static void main(final String[] args) {

		String titleStrOld = "";

		if (args == null || args.length != 1) {

			try {
				BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in));

				String read;

				while ((read = reader.readLine()) != null) {
					titleStrOld += read + "\n";
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else
			titleStrOld = args[0];

		//@formatter:on
		TransformRecord.setUnmodifiables(null);

		System.out.println(TransformRecord.transform(titleStrOld,
				TransformMode.MACHINE));

	}
}
