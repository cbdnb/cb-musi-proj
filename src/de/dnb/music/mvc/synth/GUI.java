package de.dnb.music.mvc.synth;

import java.awt.EventQueue;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnNew;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	JMenuItem mntmInfo;
	JButton btnAddGenre;
	JButton btnAddInstrument;
	JComboBox comboBoxGenre;
	JComboBox comboBoxInstru;
	JComboBox comboBoxCount;
	private JLabel lblmal;
	JButton btnAddIndiv;
	JComboBox comboBoxVersion;
	private JLabel lblFassungsphraseAuswhlen;
	JButton btnAddOpus;
	private JTabbedPane tabbedPaneNewTitle;
	private JTabbedPane tabbedPaneAdditional;
	private JPanel panelOpus;
	private JPanel panelThematic;
	JComboBox comboBoxOpus;
	JTextField textFieldOpus;
	JComboBox comboBoxIdx;
	JTextField textFieldIdx;
	JTextField textFieldYear;
	private JPanel panelFormal;
	private JPanel panelIndiv;
	JTextField textFieldIndivTitle;
	private JScrollPane scrollPane;
	JTextArea textAreaGND;
	private JScrollPane scrollPane_1;
	JTextArea textAreaStruct;
	private JLabel lblGnd;
	private JLabel lblStrukturiert;
	JButton btnAddFormal;
	JButton btnAddIdx;
	JButton btnAddKey;
	JButton btnAddYear;
	JButton btnVersion;
	JCheckBox checkBoxExpansion;
	JCheckBox checkBoxPicaPlus;
	JCheckBox checkBoxTotalCount;
	JComboBox comboBoxGenreFormal;
	JPanel panelSerial;
	JComboBox comboBoxSerial;
	JTextField textFieldSerial;
	JButton btnSerial;
	JComboBox comboBoxModeName;
	JComboBox comboBoxKeyName;
	JPanel panelModus;
	JButton btnAddModus;
	JComboBox comboBoxModusNumber;
	JLabel label;
	JLabel lblTon;
	JLabel lblTip;
	JTextArea textPaneTip;

	/**
	 * Create the frame.
	 */
	public GUI() {
		setResizable(false);
		initialize();
	}

	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				GUI.class.getResource("/resources/Violin_256.png")));
		setTitle("Baue Werktitel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1170, 721);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnNewMenu = new JMenu("?");
		menuBar.add(mnNewMenu);

		mntmInfo = new JMenuItem("Info");
		mnNewMenu.add(mntmInfo);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		btnNew = new JButton("Neu ...");
		btnNew.setBounds(36, 37, 69, 26);

		tabbedPaneNewTitle = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneNewTitle.setBounds(205, 5, 773, 69);

		panelFormal = new JPanel();
		tabbedPaneNewTitle.addTab("Formalsachtitel (Form wählen)", null,
				panelFormal, null);
		panelFormal.setLayout(null);

		comboBoxGenreFormal = new JComboBox();
		comboBoxGenreFormal.setBounds(10, 11, 332, 22);
		panelFormal.add(comboBoxGenreFormal);

		btnAddFormal = new JButton("Neues Werk / Neuen Werkteil hinzufügen");
		btnAddFormal.setBounds(368, 10, 305, 26);
		panelFormal.add(btnAddFormal);

		panelIndiv = new JPanel();
		tabbedPaneNewTitle.addTab("Individualsachtitel eingeben", null,
				panelIndiv, null);
		panelIndiv.setLayout(null);

		textFieldIndivTitle = new JTextField();
		textFieldIndivTitle.setBounds(10, 11, 332, 22);
		textFieldIndivTitle.setColumns(10);
		panelIndiv.add(textFieldIndivTitle);

		btnAddIndiv = new JButton("Neues Werk / Neuen Werkteil hinzufügen");
		btnAddIndiv.setBounds(368, 10, 305, 26);
		panelIndiv.add(btnAddIndiv);

		btnAddGenre = new JButton("Gattung hinzufügen");
		btnAddGenre.setEnabled(false);
		btnAddGenre.setBounds(205, 165, 167, 23);

		comboBoxGenre = new JComboBox();
		comboBoxGenre.setBounds(377, 166, 321, 22);

		btnAddInstrument = new JButton("Instrument hinzufügen");
		btnAddInstrument.setEnabled(false);
		btnAddInstrument.setBounds(205, 194, 167, 23);

		comboBoxInstru = new JComboBox();
		comboBoxInstru.setBounds(377, 195, 228, 22);

		comboBoxCount = new JComboBox();
		comboBoxCount.setBounds(615, 195, 62, 22);

		lblmal = new JLabel("-mal");
		lblmal.setBounds(682, 198, 53, 14);
		lblmal.setHorizontalAlignment(SwingConstants.LEFT);

		tabbedPaneAdditional = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneAdditional.setBounds(205, 228, 493, 83);

		panelOpus = new JPanel();
		tabbedPaneAdditional.addTab("Opus", null, panelOpus, null);

		comboBoxOpus = new JComboBox();
		comboBoxOpus.setBounds(10, 11, 113, 22);

		textFieldOpus = new JTextField();
		textFieldOpus.setBounds(129, 11, 86, 20);
		textFieldOpus.setColumns(10);
		panelOpus.setLayout(null);
		panelOpus.add(comboBoxOpus);
		panelOpus.add(textFieldOpus);

		btnAddOpus = new JButton("hinzufügen");
		btnAddOpus.setEnabled(false);
		btnAddOpus.setBounds(230, 11, 120, 20);
		panelOpus.add(btnAddOpus);

		panelThematic = new JPanel();
		tabbedPaneAdditional.addTab("Werkverzeichnis", null, panelThematic,
				null);

		comboBoxIdx = new JComboBox();
		comboBoxIdx.setBounds(10, 11, 113, 22);

		textFieldIdx = new JTextField();
		textFieldIdx.setBounds(129, 11, 86, 20);
		textFieldIdx.setColumns(10);
		panelThematic.setLayout(null);
		panelThematic.add(comboBoxIdx);
		panelThematic.add(textFieldIdx);

		btnAddIdx = new JButton("hinzufügen");
		btnAddIdx.setEnabled(false);
		btnAddIdx.setBounds(230, 11, 120, 20);
		panelThematic.add(btnAddIdx);
		
		panelSerial = new JPanel();
		panelSerial.setLayout(null);
		tabbedPaneAdditional.addTab("Fortlaufende Zählung", null, panelSerial, null);
		
		comboBoxSerial = new JComboBox();
		comboBoxSerial.setBounds(10, 11, 113, 22);
		panelSerial.add(comboBoxSerial);
		
		textFieldSerial = new JTextField();
		textFieldSerial.setColumns(10);
		textFieldSerial.setBounds(129, 11, 86, 20);
		panelSerial.add(textFieldSerial);
		
		btnSerial = new JButton("hinzufügen");
		btnSerial.setEnabled(false);
		btnSerial.setBounds(230, 11, 120, 20);
		panelSerial.add(btnSerial);

		JPanel panelKey = new JPanel();
		tabbedPaneAdditional.addTab("Tonart", null, panelKey, null);
		panelKey.setLayout(null);

		btnAddKey = new JButton("hinzufügen");
		btnAddKey.setEnabled(false);
		btnAddKey.setBounds(231, 11, 120, 20);
		panelKey.add(btnAddKey);
		
		comboBoxModeName = new JComboBox();
		comboBoxModeName.setBounds(98, 11, 97, 20);
		panelKey.add(comboBoxModeName);
		
		comboBoxKeyName = new JComboBox();
		comboBoxKeyName.setBounds(10, 11, 68, 20);
		panelKey.add(comboBoxKeyName);
		
		label = new JLabel("-");
		label.setBounds(88, 11, 27, 20);
		panelKey.add(label);
		
		panelModus = new JPanel();
		panelModus.setLayout(null);
		tabbedPaneAdditional.addTab("Modus", null, panelModus, null);
		
		btnAddModus = new JButton("hinzufügen");
		btnAddModus.setEnabled(false);
		btnAddModus.setBounds(231, 11, 120, 20);
		panelModus.add(btnAddModus);
		
		comboBoxModusNumber = new JComboBox();
		comboBoxModusNumber.setBounds(61, 11, 46, 20);
		panelModus.add(comboBoxModusNumber);
		
		lblTon = new JLabel(". Ton");
		lblTon.setBounds(117, 11, 46, 20);
		panelModus.add(lblTon);

		JPanel panelYear = new JPanel();
		tabbedPaneAdditional.addTab("Jahr", null, panelYear, null);
		panelYear.setLayout(null);

		textFieldYear = new JTextField();
		textFieldYear.setBounds(10, 11, 191, 20);
		textFieldYear.setColumns(10);
		panelYear.add(textFieldYear);

		btnAddYear = new JButton("hinzufügen");
		btnAddYear.setEnabled(false);
		btnAddYear.setBounds(231, 11, 120, 20);
		panelYear.add(btnAddYear);

		lblFassungsphraseAuswhlen = new JLabel("evtl. Fassungsphrase auswählen:");
		lblFassungsphraseAuswhlen.setBounds(377, 85, 213, 14);

		comboBoxVersion = new JComboBox();
		comboBoxVersion.setBounds(377, 107, 321, 22);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 360, 708, 300);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(754, 360, 400, 300);

		textAreaGND = new JTextArea();
		textAreaGND.setEditable(false);
		scrollPane.setViewportView(textAreaGND);
		contentPane.setLayout(null);
		contentPane.add(btnAddGenre);
		contentPane.add(comboBoxGenre);
		contentPane.add(btnAddInstrument);
		contentPane.add(comboBoxInstru);
		contentPane.add(comboBoxCount);
		contentPane.add(lblmal);
		contentPane.add(tabbedPaneAdditional);
		contentPane.add(lblFassungsphraseAuswhlen);
		contentPane.add(btnNew);
		contentPane.add(tabbedPaneNewTitle);
		contentPane.add(scrollPane);
		contentPane.add(scrollPane_1);

		textAreaStruct = new JTextArea();
		textAreaStruct.setEditable(false);
		textAreaStruct.setTabSize(2);
		scrollPane_1.setViewportView(textAreaStruct);
		contentPane.add(comboBoxVersion);

		lblGnd = new JLabel("GND:");
		lblGnd.setBounds(36, 335, 46, 14);
		contentPane.add(lblGnd);

		lblStrukturiert = new JLabel("Strukturiert:");
		lblStrukturiert.setBounds(755, 335, 90, 14);
		contentPane.add(lblStrukturiert);

		btnVersion = new JButton("Fassung hinzufügen");
		btnVersion.setEnabled(false);
		btnVersion.setBounds(205, 107, 167, 23);
		contentPane.add(btnVersion);

		checkBoxExpansion = new JCheckBox("Expansion");
		checkBoxExpansion.setBounds(107, 330, 105, 23);
		contentPane.add(checkBoxExpansion);

		checkBoxPicaPlus = new JCheckBox("Pica+");
		checkBoxPicaPlus.setBounds(236, 330, 90, 23);
		contentPane.add(checkBoxPicaPlus);

		checkBoxTotalCount = new JCheckBox("Gesamtzahl");
		checkBoxTotalCount.setBounds(331, 330, 134, 23);
		contentPane.add(checkBoxTotalCount);
		
		lblTip = new JLabel("Tip:");
		lblTip.setBounds(754, 111, 46, 14);
		contentPane.add(lblTip);
		
		textPaneTip = new JTextArea();
		textPaneTip.setTabSize(3);
		textPaneTip.setEditable(false);
		textPaneTip.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPaneTip.setForeground(SystemColor.menuText);
		textPaneTip.setBackground(SystemColor.control);
		textPaneTip.setBounds(754, 127, 368, 184);
		contentPane.add(textPaneTip);
	}
}
