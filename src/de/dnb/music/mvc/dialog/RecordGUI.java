package de.dnb.music.mvc.dialog;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class RecordGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnAnalyse;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	JMenuItem mntmZeigeFehler;
	JMenuItem mntmInfo;
	JButton btnGenre;
	JButton btnInstrument;
	JButton btnComp;
	JComboBox comboBoxGenre;
	JComboBox comboBoxInstru;
	JComboBox comboBoxComp;
	JComboBox comboBoxCount;
	private JLabel label;
	JTextField txtCode;
	private JLabel lblmal;
	JTabbedPane tabbedPane;
	JPanel panelPica;
	JPanel panelAleph;
	JScrollPane scrollPaneOldRecord;
	JTextArea textAreaOldRecord;
	JCheckBox chckbxExpansion;
	JCheckBox chckbxPica;
	JScrollPane scrollPanePica;
	JTextArea textAreaPica;
	JScrollPane scrollPaneAleph;
	JTextArea textAreaAleph;
	JButton btnUndo;
	JLabel label_1;
	JButton btnAddTitle;
	JCheckBox chckbxExplicit;
	private JPanel panelPicaCheck;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				RecordGUI.class.getResource("/resources/Violin_256.png")));
		setTitle("Bearbeite Datensatz");
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
		gbl_contentPane.columnWidths =
			new int[] { 0, 0, 0, 36, 62, 89, 161, 34, 49, 41, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 30, 0, 0 };
		gbl_contentPane.columnWeights =
			new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights =
			new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		btnAnalyse = new JButton("Bearbeiten / Neu ...");

		GridBagConstraints gbc_btnAnalyse = new GridBagConstraints();
		gbc_btnAnalyse.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAnalyse.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnalyse.gridx = 2;
		gbc_btnAnalyse.gridy = 1;
		contentPane.add(btnAnalyse, gbc_btnAnalyse);

		label_1 = new JLabel(" Einfügen:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 4;
		gbc_label_1.gridy = 1;
		contentPane.add(label_1, gbc_label_1);

		btnAddTitle = new JButton("Neuer Werktitel in 1XX oder 4XX ...");
		btnAddTitle.setActionCommand("");
		GridBagConstraints gbc_btnAddTitle = new GridBagConstraints();
		gbc_btnAddTitle.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddTitle.gridx = 5;
		gbc_btnAddTitle.gridy = 1;
		contentPane.add(btnAddTitle, gbc_btnAddTitle);

		btnGenre = new JButton("Gattung");
		GridBagConstraints gbc_btnGenre = new GridBagConstraints();
		gbc_btnGenre.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGenre.insets = new Insets(0, 0, 5, 5);
		gbc_btnGenre.gridx = 5;
		gbc_btnGenre.gridy = 2;
		contentPane.add(btnGenre, gbc_btnGenre);

		comboBoxGenre = new JComboBox();
		GridBagConstraints gbc_comboBoxGenre = new GridBagConstraints();
		gbc_comboBoxGenre.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxGenre.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxGenre.gridx = 6;
		gbc_comboBoxGenre.gridy = 2;
		contentPane.add(comboBoxGenre, gbc_comboBoxGenre);

		btnUndo = new JButton("Letzte Aktion rückgängig");
		GridBagConstraints gbc_btnUndo = new GridBagConstraints();
		gbc_btnUndo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUndo.insets = new Insets(0, 0, 5, 5);
		gbc_btnUndo.gridx = 2;
		gbc_btnUndo.gridy = 3;
		contentPane.add(btnUndo, gbc_btnUndo);

		btnInstrument = new JButton("Instrument");
		GridBagConstraints gbc_btnInstrument = new GridBagConstraints();
		gbc_btnInstrument.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInstrument.insets = new Insets(0, 0, 5, 5);
		gbc_btnInstrument.gridx = 5;
		gbc_btnInstrument.gridy = 3;
		contentPane.add(btnInstrument, gbc_btnInstrument);

		comboBoxInstru = new JComboBox();
		GridBagConstraints gbc_comboBoxInstru = new GridBagConstraints();
		gbc_comboBoxInstru.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxInstru.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxInstru.gridx = 6;
		gbc_comboBoxInstru.gridy = 3;
		contentPane.add(comboBoxInstru, gbc_comboBoxInstru);

		comboBoxCount = new JComboBox();
		GridBagConstraints gbc_comboBoxCount = new GridBagConstraints();
		gbc_comboBoxCount.gridwidth = 2;
		gbc_comboBoxCount.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxCount.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxCount.gridx = 7;
		gbc_comboBoxCount.gridy = 3;
		contentPane.add(comboBoxCount, gbc_comboBoxCount);

		lblmal = new JLabel("-mal");
		lblmal.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblmal = new GridBagConstraints();
		gbc_lblmal.anchor = GridBagConstraints.WEST;
		gbc_lblmal.insets = new Insets(0, 0, 5, 5);
		gbc_lblmal.gridx = 9;
		gbc_lblmal.gridy = 3;
		contentPane.add(lblmal, gbc_lblmal);

		btnComp = new JButton("Komponist");
		GridBagConstraints gbc_btnComp = new GridBagConstraints();
		gbc_btnComp.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnComp.insets = new Insets(0, 0, 5, 5);
		gbc_btnComp.gridx = 5;
		gbc_btnComp.gridy = 4;
		contentPane.add(btnComp, gbc_btnComp);

		comboBoxComp = new JComboBox();
		GridBagConstraints gbc_comboBoxComp = new GridBagConstraints();
		gbc_comboBoxComp.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxComp.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxComp.gridx = 6;
		gbc_comboBoxComp.gridy = 4;
		contentPane.add(comboBoxComp, gbc_comboBoxComp);

		label = new JLabel("$4");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 7;
		gbc_label.gridy = 4;
		contentPane.add(label, gbc_label);

		txtCode = new JTextField();
		txtCode.setText("kom1");
		GridBagConstraints gbc_txtCode = new GridBagConstraints();
		gbc_txtCode.fill = GridBagConstraints.VERTICAL;
		gbc_txtCode.anchor = GridBagConstraints.WEST;
		gbc_txtCode.insets = new Insets(0, 0, 5, 5);
		gbc_txtCode.gridx = 8;
		gbc_txtCode.gridy = 4;
		contentPane.add(txtCode, gbc_txtCode);
		txtCode.setColumns(10);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 9;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 5;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		scrollPaneOldRecord = new JScrollPane();
		tabbedPane.addTab("Alter Datensatz", null, scrollPaneOldRecord, null);

		textAreaOldRecord = new JTextArea();
		scrollPaneOldRecord.setViewportView(textAreaOldRecord);

		panelPica = new JPanel();
		tabbedPane.addTab("Neu Pica", null, panelPica, null);
		panelPica.setLayout(new BorderLayout());
		
		panelPicaCheck = new JPanel();
		panelPica.add(panelPicaCheck, BorderLayout.NORTH);
		panelPicaCheck.setLayout(new BoxLayout(panelPicaCheck, BoxLayout.X_AXIS));

		chckbxExpansion = new JCheckBox("Expansion");
		chckbxExpansion.setSelected(true);
		chckbxExpansion.setBounds(182, 7, 97, 23);
		panelPicaCheck.add(chckbxExpansion);

		chckbxPica = new JCheckBox("Pica+");
		chckbxPica.setBounds(332, 7, 97, 23);
		panelPicaCheck.add(chckbxPica);

		scrollPanePica = new JScrollPane();
		scrollPanePica.setBounds(10, 36, 1054, 238);
		panelPica.add(scrollPanePica);

		textAreaPica = new JTextArea();
		scrollPanePica.setViewportView(textAreaPica);

		panelAleph = new JPanel();
		tabbedPane.addTab("Neu Aleph", null, panelAleph, null);
		panelAleph.setLayout(new BorderLayout());

		scrollPaneAleph = new JScrollPane();
		panelAleph.add(scrollPaneAleph, BorderLayout.CENTER);

		textAreaAleph = new JTextArea();
		textAreaAleph.setTabSize(4);
		scrollPaneAleph.setViewportView(textAreaAleph);
		
		chckbxExplicit = new JCheckBox("Felder und Unterfelder explizit");
		panelAleph.add(chckbxExplicit, BorderLayout.NORTH);
	}
}
