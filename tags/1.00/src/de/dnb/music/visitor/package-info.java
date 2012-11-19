/**
 * 
 * Leichte Modifikation des Visitor-Musters gegenüber GoF:
 * - statt visitConcreteElementX(concreteElementX) wird nur 
 * 		visit(concreteElementX) gewählt.
 * 		Das funktioniert, da visit für jede ConcreteElementX-Klasse
 * 		überladen ist. 
 * - Visitor ist nicht Interface, sondern abstrakte Klasse. Daher ist es 
 *  	möglich, in Visitor jede dieser überladenen Methoden visit()
 * 		rudimentär zu imlementieren.
 * - Die Knoten und Blätter der MusicTitle-Struktur imlementieren 
 * 		TitleElement. 
 * - ConcreteElementX.accept(Visitor) entscheidet, in welcher Reihenfolge
 * 		die Kindelemente des Knotens durchlaufen werden. Es gibt keinen
 * 		Iterator.
 * - Nachdem die Kindelemente besucht worden sind, wird leave(concreteElementX)
 * 		aufgerufen. Das müssen aber nur innere Knoten machen.
 * - Die Methode visit() liefert dann, wenn Kindelemente vorliegen, einen
 * 		boolschen Wert zurück, der es accept() erlaubt, zu entscheiden,
 * 		ob die Kindelemente überhaupt besucht werden müssen.
 * 
 */
package de.dnb.music.visitor;

