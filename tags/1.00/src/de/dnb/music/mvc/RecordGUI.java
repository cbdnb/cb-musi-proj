package de.dnb.music.mvc;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class RecordGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnAnalysieren;
	JTextArea textAreaOld;
	JTextArea textAreaNew;
	private JLabel lblAlterDatensatz;
	private JLabel lblNeuerDatensatz;
	JCheckBox chckbxPica;
	JCheckBox chckbxScript;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	JMenuItem mntmZeigeFehler;
	JMenuItem mntmInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordGUI frame = new RecordGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RecordGUI() {
		initialize();
	}

	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RecordGUI.class.getResource("/resources/Violin_256.png")));
		setTitle("Bearbeite Titel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1170, 516);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("?");
		menuBar.add(mnNewMenu);
		
		mntmInfo = new JMenuItem("Info");
		mnNewMenu.add(mntmInfo);
		
		mntmZeigeFehler = new JMenuItem("Zeige Fehler");
		mnNewMenu.add(mntmZeigeFehler);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 626, 93, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 30, 0, 0 };
		gbl_contentPane.columnWeights =
			new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		btnAnalysieren = new JButton("Bearbeiten ...");

		GridBagConstraints gbc_btnAnalysieren = new GridBagConstraints();
		gbc_btnAnalysieren.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnalysieren.gridx = 0;
		gbc_btnAnalysieren.gridy = 0;
		contentPane.add(btnAnalysieren, gbc_btnAnalysieren);

		chckbxScript = new JCheckBox("Script");
		GridBagConstraints gbc_chckbxScript = new GridBagConstraints();
		gbc_chckbxScript.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxScript.gridx = 2;
		gbc_chckbxScript.gridy = 0;
		contentPane.add(chckbxScript, gbc_chckbxScript);

		lblAlterDatensatz = new JLabel("        Alter Datensatz");
		GridBagConstraints gbc_lblAlterDatensatz = new GridBagConstraints();
		gbc_lblAlterDatensatz.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAlterDatensatz.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlterDatensatz.gridx = 0;
		gbc_lblAlterDatensatz.gridy = 1;
		contentPane.add(lblAlterDatensatz, gbc_lblAlterDatensatz);

		lblNeuerDatensatz = new JLabel("Neuer Datensatz");
		GridBagConstraints gbc_lblNeuerDatensatz = new GridBagConstraints();
		gbc_lblNeuerDatensatz.insets = new Insets(0, 0, 5, 5);
		gbc_lblNeuerDatensatz.gridx = 1;
		gbc_lblNeuerDatensatz.gridy = 1;
		contentPane.add(lblNeuerDatensatz, gbc_lblNeuerDatensatz);

		chckbxPica = new JCheckBox("Pica+");
		GridBagConstraints gbc_chckbxPica = new GridBagConstraints();
		gbc_chckbxPica.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxPica.gridx = 2;
		gbc_chckbxPica.gridy = 1;
		contentPane.add(chckbxPica, gbc_chckbxPica);

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 3;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 2;
		contentPane.add(splitPane, gbc_splitPane);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		scrollPane_1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		textAreaNew = new JTextArea();
		textAreaNew.setTabSize(2);
		scrollPane_1.setViewportView(textAreaNew);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		splitPane.setLeftComponent(scrollPane);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		textAreaOld = new JTextArea();
		scrollPane.setViewportView(textAreaOld);
		splitPane.setDividerLocation(550);
	}

}
