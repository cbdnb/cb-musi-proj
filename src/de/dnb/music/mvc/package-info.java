/**
 * MVC mit kleinen Modifikationen:
 * 
 * 	- TitleView ist Brücke zu TitleGUI, um dessen Felder zu verbergen und TitleGUI nicht
 * 		aufzublähen. Daher enthält TitleView diverse Methoden:
 * 
 * 			add...Listener()	Werden im Konstruktor von TitleController aufgerufen.
 * 								Die Listener enthalten die Verarbeitungslogik 
 * 								und werden von TitleView an TitleGUI weitergereicht.
 * 			get...()			Felder und Boxen in TitleGUI abfragen
 * 			set...()			Textfelder mit Werten belegen
 * 
 * 		TitleView implementiert Observer und muss demzufolge update() implementieren.
 * 		
 * 	- TitleModel erbt von Observable.
 * 		TitleView ist bei TitleModel als Observer registriert und wird von 
 * 		Zustandsänderungen via setChanged() und notifyObservers() informiert.
 * 
 * - TitleController enthält die inneren Klassen ...Listener, die bei TitleView
 * 		registriert werden.
 */
package de.dnb.music.mvc;

