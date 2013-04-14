package de.dnb.music.mvc.record;

import java.awt.EventQueue;
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
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	JMenuItem mntmZeigeFehler;
	JMenuItem mntmInfo;
	private JLabel lblEinfgen;
	JButton btnGenre;
	JButton btnInstrument;
	JButton btnComp;
	JComboBox comboBoxGenre;
	JComboBox comboBoxInstru;
	JComboBox comboBoxComp;
	JButton btnExpansionEntfernen;
	JComboBox comboBoxCount;
	private JLabel label;
	JTextField txtCode;
	private JLabel lblmal;
	JButton btnTitle;
	JComboBox comboBoxFieldNumber;
	JTextField textFieldTitle;

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
		gbl_contentPane.columnWidths =
			new int[] { 0, 0, 0, 0, 96, 62, 89, 72, 77, 0, 49, 41, 27, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 32, 0, 0, 30, 0, 0 };
		gbl_contentPane.columnWeights =
			new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0,
				0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		lblAlterDatensatz = new JLabel("Alter Datensatz");
		lblAlterDatensatz.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlterDatensatz.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblAlterDatensatz = new GridBagConstraints();
		gbc_lblAlterDatensatz.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAlterDatensatz.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlterDatensatz.gridx = 2;
		gbc_lblAlterDatensatz.gridy = 0;
		contentPane.add(lblAlterDatensatz, gbc_lblAlterDatensatz);

		lblNeuerDatensatz = new JLabel("Neuer Datensatz");
		lblNeuerDatensatz.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblNeuerDatensatz = new GridBagConstraints();
		gbc_lblNeuerDatensatz.gridwidth = 3;
		gbc_lblNeuerDatensatz.insets = new Insets(0, 0, 5, 5);
		gbc_lblNeuerDatensatz.gridx = 6;
		gbc_lblNeuerDatensatz.gridy = 0;
		contentPane.add(lblNeuerDatensatz, gbc_lblNeuerDatensatz);

		btnExpansionEntfernen = new JButton("Expansionen entfernen");
		GridBagConstraints gbc_btnExpansionEntfernen = new GridBagConstraints();
		gbc_btnExpansionEntfernen.gridwidth = 4;
		gbc_btnExpansionEntfernen.insets = new Insets(0, 0, 5, 0);
		gbc_btnExpansionEntfernen.gridx = 10;
		gbc_btnExpansionEntfernen.gridy = 0;
		contentPane.add(btnExpansionEntfernen, gbc_btnExpansionEntfernen);
		
				lblEinfgen = new JLabel("Einfügen:");
				GridBagConstraints gbc_lblEinfgen = new GridBagConstraints();
				gbc_lblEinfgen.insets = new Insets(0, 0, 5, 5);
				gbc_lblEinfgen.gridx = 5;
				gbc_lblEinfgen.gridy = 1;
				contentPane.add(lblEinfgen, gbc_lblEinfgen);
				
				btnTitle = new JButton("Werktitel");
				GridBagConstraints gbc_btnWerktitel = new GridBagConstraints();
				gbc_btnWerktitel.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnWerktitel.insets = new Insets(0, 0, 5, 5);
				gbc_btnWerktitel.gridx = 6;
				gbc_btnWerktitel.gridy = 1;
				contentPane.add(btnTitle, gbc_btnWerktitel);
				
				comboBoxFieldNumber = new JComboBox();
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.insets = new Insets(0, 0, 5, 5);
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 7;
				gbc_comboBox.gridy = 1;
				contentPane.add(comboBoxFieldNumber, gbc_comboBox);
				
				textFieldTitle = new JTextField();
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.gridwidth = 5;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.BOTH;
				gbc_textField.gridx = 8;
				gbc_textField.gridy = 1;
				contentPane.add(textFieldTitle, gbc_textField);
				textFieldTitle.setColumns(10);
		
				btnGenre = new JButton("Gattung");
				GridBagConstraints gbc_btnGenre = new GridBagConstraints();
				gbc_btnGenre.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnGenre.insets = new Insets(0, 0, 5, 5);
				gbc_btnGenre.gridx = 6;
				gbc_btnGenre.gridy = 2;
				contentPane.add(btnGenre, gbc_btnGenre);

		comboBoxGenre = new JComboBox();
		GridBagConstraints gbc_comboBoxGenre = new GridBagConstraints();
		gbc_comboBoxGenre.gridwidth = 2;
		gbc_comboBoxGenre.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxGenre.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxGenre.gridx = 7;
		gbc_comboBoxGenre.gridy = 2;
		contentPane.add(comboBoxGenre, gbc_comboBoxGenre);

		btnAnalysieren = new JButton("Bearbeiten / Neu ...");

		GridBagConstraints gbc_btnAnalysieren = new GridBagConstraints();
		gbc_btnAnalysieren.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAnalysieren.insets = new Insets(0, 0, 5, 5);
		gbc_btnAnalysieren.gridx = 2;
		gbc_btnAnalysieren.gridy = 3;
		contentPane.add(btnAnalysieren, gbc_btnAnalysieren);
		
				btnInstrument = new JButton("Instrument");
				GridBagConstraints gbc_btnInstrument = new GridBagConstraints();
				gbc_btnInstrument.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnInstrument.insets = new Insets(0, 0, 5, 5);
				gbc_btnInstrument.gridx = 6;
				gbc_btnInstrument.gridy = 3;
				contentPane.add(btnInstrument, gbc_btnInstrument);

		comboBoxInstru = new JComboBox();
		GridBagConstraints gbc_comboBoxInstru = new GridBagConstraints();
		gbc_comboBoxInstru.gridwidth = 2;
		gbc_comboBoxInstru.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxInstru.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxInstru.gridx = 7;
		gbc_comboBoxInstru.gridy = 3;
		contentPane.add(comboBoxInstru, gbc_comboBoxInstru);
		
				comboBoxCount = new JComboBox();
				GridBagConstraints gbc_comboBoxCount = new GridBagConstraints();
				gbc_comboBoxCount.gridwidth = 2;
				gbc_comboBoxCount.insets = new Insets(0, 0, 5, 5);
				gbc_comboBoxCount.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBoxCount.gridx = 9;
				gbc_comboBoxCount.gridy = 3;
				contentPane.add(comboBoxCount, gbc_comboBoxCount);
		
		lblmal = new JLabel("-mal");
		lblmal.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblmal = new GridBagConstraints();
		gbc_lblmal.anchor = GridBagConstraints.WEST;
		gbc_lblmal.insets = new Insets(0, 0, 5, 5);
		gbc_lblmal.gridx = 11;
		gbc_lblmal.gridy = 3;
		contentPane.add(lblmal, gbc_lblmal);
		
				btnComp = new JButton("Komponist");
				GridBagConstraints gbc_btnComp = new GridBagConstraints();
				gbc_btnComp.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnComp.insets = new Insets(0, 0, 5, 5);
				gbc_btnComp.gridx = 6;
				gbc_btnComp.gridy = 4;
				contentPane.add(btnComp, gbc_btnComp);

		comboBoxComp = new JComboBox();
		GridBagConstraints gbc_comboBoxComp = new GridBagConstraints();
		gbc_comboBoxComp.gridwidth = 2;
		gbc_comboBoxComp.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxComp.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxComp.gridx = 7;
		gbc_comboBoxComp.gridy = 4;
		contentPane.add(comboBoxComp, gbc_comboBoxComp);
		
		label = new JLabel("$4");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 9;
		gbc_label.gridy = 4;
		contentPane.add(label, gbc_label);
		
		txtCode = new JTextField();
		txtCode.setText("kom1");
		GridBagConstraints gbc_txtCode = new GridBagConstraints();
		gbc_txtCode.fill = GridBagConstraints.VERTICAL;
		gbc_txtCode.anchor = GridBagConstraints.WEST;
		gbc_txtCode.gridwidth = 2;
		gbc_txtCode.insets = new Insets(0, 0, 5, 5);
		gbc_txtCode.gridx = 10;
		gbc_txtCode.gridy = 4;
		contentPane.add(txtCode, gbc_txtCode);
		txtCode.setColumns(10);

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 14;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 5;
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
		splitPane.setDividerLocation(250);
	}

}
