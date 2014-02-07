package de.dnb.music.mvc.title;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextPane;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class TitleGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextField textFieldComposer;
	JTextField textFieldTitle;
	JTextField textFieldRAK;
	JTextField textFieldRSWK;
	JCheckBox chckbxKeyName;
	JCheckBox chckbxPopularMusic;
	JCheckBox chckbxExpansion;
	JCheckBox chckbxPica;
	JCheckBox chckbxNumberInstruments;
	JButton btnAnalysieren;
	JTextArea textAreaGND;
	JTextArea textAreaStructured;
	private JMenuBar menuBar;
	private JMenu menu;
	JMenuItem mntmInfo;
	private JScrollPane scrollPane_2;
	JTextArea textAreaSetOfRules;
	private JLabel lblMglichInDen;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TitleGUI frame = new TitleGUI();
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
	public TitleGUI() {
		initialize();
	}
	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(TitleGUI.class.getResource("/de/dnb/music/resources/2.png")));
		setTitle("Musiktitelanalysator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1275, 601);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("?");
		menuBar.add(menu);
		
		mntmInfo = new JMenuItem("Info");
		menu.add(mntmInfo);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths =
			new int[] { 0, 0, 0, 0, 0, 291, 90, 93, 0, 0, 0 };
		gbl_contentPane.rowHeights =
			new int[] { 30, 30, 47, 0, 0, 115, 37, 87 };
		gbl_contentPane.columnWeights =
			new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights =
			new double[] { 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);
		
				JLabel lblKomponist = new JLabel("Komponist (fakultativ):");
		GridBagConstraints gbc_lblKomponist = new GridBagConstraints();
		gbc_lblKomponist.anchor = GridBagConstraints.EAST;
		gbc_lblKomponist.insets = new Insets(0, 0, 5, 5);
		gbc_lblKomponist.gridx = 0;
		gbc_lblKomponist.gridy = 0;
		contentPane.add(lblKomponist, gbc_lblKomponist);
		
				textFieldComposer = new JTextField();
		GridBagConstraints gbc_textFieldComposer = new GridBagConstraints();
		gbc_textFieldComposer.gridwidth = 7;
		gbc_textFieldComposer.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldComposer.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldComposer.gridx = 1;
		gbc_textFieldComposer.gridy = 0;
		contentPane.add(textFieldComposer, gbc_textFieldComposer);
		textFieldComposer.setColumns(10);
		
				btnAnalysieren = new JButton("Analysieren");
		
				GridBagConstraints gbc_btnAnalysieren = new GridBagConstraints();
		gbc_btnAnalysieren.insets = new Insets(0, 0, 5, 0);
		gbc_btnAnalysieren.gridx = 9;
		gbc_btnAnalysieren.gridy = 0;
		contentPane.add(btnAnalysieren, gbc_btnAnalysieren);
		
				JLabel lblZuAnalysierenderTitel =
					new JLabel("Zu analysierender Titel:");
		GridBagConstraints gbc_lblZuAnalysierenderTitel =
			new GridBagConstraints();
		gbc_lblZuAnalysierenderTitel.anchor = GridBagConstraints.EAST;
		gbc_lblZuAnalysierenderTitel.insets = new Insets(0, 0, 5, 5);
		gbc_lblZuAnalysierenderTitel.gridx = 0;
		gbc_lblZuAnalysierenderTitel.gridy = 1;
		contentPane.add(lblZuAnalysierenderTitel,
				gbc_lblZuAnalysierenderTitel);
		
				textFieldTitle = new JTextField();
		GridBagConstraints gbc_textFieldTitle = new GridBagConstraints();
		gbc_textFieldTitle.gridwidth = 7;
		gbc_textFieldTitle.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTitle.gridx = 1;
		gbc_textFieldTitle.gridy = 1;
		contentPane.add(textFieldTitle, gbc_textFieldTitle);
		textFieldTitle.setColumns(10);
		
				chckbxKeyName = new JCheckBox("Tonbuchstaben erkennen");
		
				GridBagConstraints gbc_chckbxKeyName = new GridBagConstraints();
		gbc_chckbxKeyName.anchor = GridBagConstraints.WEST;
		gbc_chckbxKeyName.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxKeyName.gridx = 9;
		gbc_chckbxKeyName.gridy = 1;
		contentPane.add(chckbxKeyName, gbc_chckbxKeyName);
		
				JLabel lblNewLabel =
					new JLabel("RAK-M 2003 (vermutlich, zur Kontrolle):");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
				textFieldRAK = new JTextField();
		GridBagConstraints gbc_textFieldRAK = new GridBagConstraints();
		gbc_textFieldRAK.gridwidth = 7;
		gbc_textFieldRAK.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldRAK.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldRAK.gridx = 1;
		gbc_textFieldRAK.gridy = 2;
		contentPane.add(textFieldRAK, gbc_textFieldRAK);
		textFieldRAK.setColumns(10);
		
				chckbxPopularMusic = new JCheckBox("U-Musik erkennen");
		
				GridBagConstraints gbc_chckbxPopularMusic = new GridBagConstraints();
		gbc_chckbxPopularMusic.anchor = GridBagConstraints.WEST;
		gbc_chckbxPopularMusic.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxPopularMusic.gridx = 9;
		gbc_chckbxPopularMusic.gridy = 2;
		contentPane.add(chckbxPopularMusic, gbc_chckbxPopularMusic);
		
				JLabel lblNewLabel_1 =
					new JLabel("RSWK (vermutlich, zur Kontrolle):");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 3;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
				textFieldRSWK = new JTextField();
		textFieldRSWK.setColumns(10);
		GridBagConstraints gbc_textFieldRSWK = new GridBagConstraints();
		gbc_textFieldRSWK.gridwidth = 7;
		gbc_textFieldRSWK.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldRSWK.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldRSWK.gridx = 1;
		gbc_textFieldRSWK.gridy = 3;
		contentPane.add(textFieldRSWK, gbc_textFieldRSWK);
		
				chckbxExpansion = new JCheckBox("Expansion");
		
				GridBagConstraints gbc_chckbxExpansion = new GridBagConstraints();
		gbc_chckbxExpansion.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxExpansion.gridx = 2;
		gbc_chckbxExpansion.gridy = 4;
		contentPane.add(chckbxExpansion, gbc_chckbxExpansion);
		
				chckbxPica = new JCheckBox("Pica+");
		
				GridBagConstraints gbc_chckbxPica = new GridBagConstraints();
		gbc_chckbxPica.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxPica.gridx = 3;
		gbc_chckbxPica.gridy = 4;
		contentPane.add(chckbxPica, gbc_chckbxPica);
		
				chckbxNumberInstruments = new JCheckBox("Gesamtzahl");
		
				GridBagConstraints gbc_chckbxNumberInstruments =
					new GridBagConstraints();
		gbc_chckbxNumberInstruments.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNumberInstruments.gridx = 4;
		gbc_chckbxNumberInstruments.gridy = 4;
		contentPane
				.add(chckbxNumberInstruments, gbc_chckbxNumberInstruments);
		
				JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);
		
				textAreaGND = new JTextArea();
		scrollPane.setViewportView(textAreaGND);
				
						JLabel lblStrukturiert = new JLabel("strukturiert:");
						GridBagConstraints gbc_lblStrukturiert = new GridBagConstraints();
						gbc_lblStrukturiert.insets = new Insets(0, 0, 5, 5);
						gbc_lblStrukturiert.gridx = 6;
						gbc_lblStrukturiert.gridy = 5;
						contentPane.add(lblStrukturiert, gbc_lblStrukturiert);
		
				JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 7;
		gbc_scrollPane_1.gridy = 5;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
				textAreaStructured = new JTextArea();
				textAreaStructured.setTabSize(2);
		scrollPane_1.setViewportView(textAreaStructured);
		
				JLabel lblGnd = new JLabel("GND:");
		lblGnd.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblGnd = new GridBagConstraints();
		gbc_lblGnd.anchor = GridBagConstraints.EAST;
		gbc_lblGnd.insets = new Insets(0, 0, 5, 5);
		gbc_lblGnd.gridx = 0;
		gbc_lblGnd.gridy = 6;
		contentPane.add(lblGnd, gbc_lblGnd);
		
		lblMglichInDen = new JLabel("Entstammt evtl. den Regelwerken:");
		GridBagConstraints gbc_lblMglichInDen = new GridBagConstraints();
		gbc_lblMglichInDen.insets = new Insets(0, 0, 0, 5);
		gbc_lblMglichInDen.gridx = 6;
		gbc_lblMglichInDen.gridy = 7;
		contentPane.add(lblMglichInDen, gbc_lblMglichInDen);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridwidth = 3;
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 7;
		gbc_scrollPane_2.gridy = 7;
		contentPane.add(scrollPane_2, gbc_scrollPane_2);
		
		textAreaSetOfRules = new JTextArea();
		textAreaSetOfRules.setTabSize(2);
		scrollPane_2.setViewportView(textAreaSetOfRules);
	}

	public void actionPerformed(final ActionEvent event) {
		Object source = event.getSource();
		boolean selectet;
		String composer = textFieldComposer.getText();
		String title = textFieldTitle.getText();
		if (source == btnAnalysieren || source == textFieldTitle
			|| source == textFieldComposer) {

			System.out.println(composer);
			System.out.println(title);
		}

		if (source == chckbxExpansion) {
			selectet = chckbxExpansion.isSelected();
			System.out.println(selectet);
		}
		if (source == chckbxKeyName) {
			selectet = chckbxKeyName.isSelected();
			System.out.println(selectet);
		}
		if (source == chckbxNumberInstruments) {
			selectet = chckbxNumberInstruments.isSelected();
			System.out.println(selectet);
		}
		if (source == chckbxPica) {
			selectet = chckbxPica.isSelected();
			System.out.println(selectet);
		}
		if (source == chckbxPopularMusic) {
			selectet = chckbxPopularMusic.isSelected();
			System.out.println(selectet);
		}

	}

}
