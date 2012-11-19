package de.dnb.music.publicInterface;

import utils.TitleUtils;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

/**
 * Gibt zu einer 130 eine 430-Fassung mit ausschliesslich Deskriptionszeichen 
 * und mit Kommentar "$vR:Ansetzung nach RAK-Musik". 
 * Das ist für die Portalanzeige.
 * 
 * Zugehöriges Skript:
 * 
	Sub f430Portal()
	
	application.activeWindow.command "k", false
	
	inhalt130 = Application.ActiveWindow.Title.FindTag("130", 0, false, false)	
		
	
	'  " durch \" ersetzen, um Text mit Anfuehrungszeichen 
	'korrekt zu uebergeben:
	inhalt130 = Replace(inhalt130 , CStr(Chr(34)),"\"&Chr(34)) 		
	' in Anführungszeichen einschliessen, um an Applikation zu uebergeben
	inhalt130 = Chr(34) & inhalt130 & Chr(34) 
		
	set WshShell = CreateObject("Wscript.shell")
    	Set oExec = WshShell.Exec("javaw -jar V:\DMA\Anwendungen\dist\_
    	RAK_Portal.jar  " & inhalt130)
	set rein = oExec.StdOut
	inhalt430 = rein.readall	
	
	found430 = application.activeWindow.title.find("$vR:Ansetzung nach _
	RAK-Musik", false, false, false)
	if found430 then
		application.activeWindow.title.startOfField
		application.activeWindow.title.endOfField true
	else
		application.activeWindow.title.insertText vbcrlf
		application.activeWindow.title.lineUp
	end if
	
	application.activeWindow.title.insertText inhalt430 
	

	End Sub
 * 
 * 
 * @author baumann
 *
 */
public final class Transform2RAK {

	private Transform2RAK() {
	}

	/**
	 * Führt die Transformation durch.
	 * 
	 * @param args 130-Feld mit Tag
	 */
	public static void main(final String[] args) {

		if (args == null || args.length != 1)
			throw new IllegalArgumentException();

		final String arg0 = args[0];
		if (arg0 == null)
			throw new IllegalArgumentException();

		String inhalt130 = arg0;
		inhalt130 = inhalt130.replace("130 ", "").trim();
		if (inhalt130.length() == 0)
			throw new IllegalArgumentException();

		inhalt130 = inhalt130.replace("$g", "$p");
		//		System.err.println(inhalt430);

		MusicTitle mt = ParseMusicTitle.parse(null, inhalt130);
		String comment = "R:Ansetzung nach RAK-Musik";
		mt.setComment(null);

		String rak = TitleUtils.getRAK(mt);

		System.out.println("430 " + rak + "$v" + comment);

	}

}
