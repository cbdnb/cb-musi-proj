package utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class InOut {

	/**
	 * Liest einen String aus der Zwischenablage.
	 * 
	 * @return String oder null, wenn kein String in Zwischenablage.
	 */
	public static String readClipboard() {
		Clipboard systemClipboard;
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferData = systemClipboard.getContents(null);
		if (transferData != null
			&& transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			// Inhalt auslesen
			try {
				return (String) transferData
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

	public static void write2Clipboard(String s) {
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(s), null);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		write2Clipboard("hi");
	}

}
