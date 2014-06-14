package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.logic.MCQuery;
import com.logic.QueryResponse;

public class Main {

	private JFrame frmMinecraftServerQuery;
	private JButton btnRefresh;
	private JLabel valPlayers;
	private JLabel valMapName;
	private JLabel valGamemode;
	private JLabel valGameVersion;
	private JLabel valHostname;
	private JLabel valPlugins;
	private JLabel valMOTD;
	private JLabel valGameID;
	private JTextField textPort;
	private JTextField textIP;
	private JLabel valErrors;
	private final DefaultListModel<String> model = new DefaultListModel<String>();
	private JList<String> listPlayers = new JList<String>(model);
	private JList<String> listPlugins = new JList<String>(model);
	private Color red = new Color(0xFF, 0x00, 0x00);
	private Color green = new Color(0x5b, 0x7d, 0x34);
	private JLabel imageLabel;
	ImageIcon ii = new ImageIcon(this.getClass().getResource("/com/resources/images/loading2.gif"));

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmMinecraftServerQuery.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		initialize();
		initializeEvents();
	}
	
	private void RefreshData(MCQuery query, QueryResponse response) {
		imageLabel.setVisible(false);

		if (query.errorCheck) {
			imageLabel.setVisible(true);
			valErrors.setForeground(red);
			valErrors.setText(query.errorMessage);
		} else {
			valErrors.setForeground(green);
			valErrors.setText("Success");
			valMOTD.setText(response.getMOTD());
			valGameID.setText(response.getGameId());
			valHostname.setText(response.getHostname());
			valGamemode.setText(response.getGameMode());
			valGameVersion.setText(response.getGameVersion());
			valPlayers.setText(response.getOnlinePlayers() + " of " + response.getMaxPlayers());
			valMapName.setText(response.getMapName());
			
			if (!"No".equalsIgnoreCase(response.getPlugins()))
			{
				DefaultListModel<String> modeloPlugins = new DefaultListModel<String>();
				String pluginString = response.getPlugins();
				String[] plugins = pluginString.split(";");

				for (int i = 0; i < plugins.length; i++) {
					modeloPlugins.addElement(plugins[i].trim());
				}
				
				listPlugins.setModel(modeloPlugins);
			}
			
			if (!response.getPlayerList().isEmpty()) {
				DefaultListModel<String> modeloUsers = new DefaultListModel<String>();
				
				ArrayList<String> users = response.getPlayerList();
				
				for (int i = 0; i < users.size(); i++) {
					modeloUsers.addElement(users.get(i).trim());
				}

				listPlayers.setModel(modeloUsers);
			}
		}
	}
	
	private void initializeEvents() {
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MCQuery query = new MCQuery(textIP.getText(), textPort.getText());
				QueryResponse response = query.fullStat();
				
				RefreshData(query, response);
			}
		});
	}

	private void initialize() {
		frmMinecraftServerQuery = new JFrame();
		frmMinecraftServerQuery.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/resources/images/icon.png")));
		frmMinecraftServerQuery.setTitle("MSS - Minecraft Server Status");
		frmMinecraftServerQuery.setBounds(100, 100, 758, 474);
		frmMinecraftServerQuery.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelMain = new JPanel();
		frmMinecraftServerQuery.getContentPane().add(panelMain, BorderLayout.SOUTH);
				
		JLabel lblIp = new JLabel("IP");
		
		textIP = new JTextField();
		textIP.setColumns(20);
		
		textPort = new JTextField();
		textPort.setHorizontalAlignment(SwingConstants.CENTER);
		textPort.setText("25565");
		textPort.setColumns(5);
		panelMain.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelMain.add(lblIp);
		panelMain.add(textIP);
		
		JLabel lblPort = new JLabel("Port");
		panelMain.add(lblPort);
		panelMain.add(textPort);
		
		btnRefresh = new JButton();
		btnRefresh.setText("Refresh server status");
		panelMain.add(btnRefresh);
		
		JPanel panelInfo = new JPanel();
		frmMinecraftServerQuery.getContentPane().add(panelInfo, BorderLayout.CENTER);
		GridBagLayout gbl_panelInfo = new GridBagLayout();
		gbl_panelInfo.columnWidths = new int[]{90, 291, 196, 0};
		gbl_panelInfo.rowHeights = new int[]{110, 100, 42, 0};
		gbl_panelInfo.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelInfo.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panelInfo.setLayout(gbl_panelInfo);
		
		JPanel panelServerInfo = new JPanel();
		panelServerInfo.setBorder (BorderFactory.createTitledBorder ("Server info"));
		GridBagConstraints gbc_panelServerInfo = new GridBagConstraints();
		gbc_panelServerInfo.fill = GridBagConstraints.BOTH;
		gbc_panelServerInfo.insets = new Insets(0, 0, 5, 5);
		gbc_panelServerInfo.gridwidth = 2;
		gbc_panelServerInfo.gridx = 0;
		gbc_panelServerInfo.gridy = 0;
		panelInfo.add(panelServerInfo, gbc_panelServerInfo);
		
		JLabel lblMOTD = new JLabel("MOTD");
		lblMOTD.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMOTD.setBounds(10, 20, 59, 14);
		
		JLabel lblGameId = new JLabel("Game ID");
		lblGameId.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGameId.setBounds(10, 40, 59, 14);
		
		JLabel lblPlugins = new JLabel("Plugins");
		lblPlugins.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPlugins.setBounds(10, 80, 59, 14);
		
		JLabel lblHostname = new JLabel("Hostname");
		lblHostname.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHostname.setBounds(10, 60, 59, 14);
		
		valPlugins = new JLabel("");
		valPlugins.setBounds(85, 80, 283, 14);
		
		valHostname = new JLabel("");
		valHostname.setBounds(85, 60, 283, 14);
		
		valGameID = new JLabel("");
		valGameID.setBounds(85, 40, 283, 14);
		
		valMOTD = new JLabel("");
		valMOTD.setBounds(85, 20, 283, 14);
		panelServerInfo.setLayout(null);
		panelServerInfo.add(lblGameId);
		panelServerInfo.add(lblMOTD);
		panelServerInfo.add(lblHostname);
		panelServerInfo.add(lblPlugins);
		panelServerInfo.add(valGameID);
		panelServerInfo.add(valMOTD);
		panelServerInfo.add(valPlugins);
		panelServerInfo.add(valHostname);
		
				listPlugins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listPlugins.setBackground(SystemColor.menu);
				listPlugins.setBounds(10, 101, 356, 144);
				panelServerInfo.add(listPlugins, BorderLayout.CENTER);
		
		JPanel panelPlayerInfo = new JPanel();
		panelPlayerInfo.setBorder (BorderFactory.createTitledBorder ("Player info"));
		GridBagConstraints gbc_panelPlayerInfo = new GridBagConstraints();
		gbc_panelPlayerInfo.fill = GridBagConstraints.BOTH;
		gbc_panelPlayerInfo.insets = new Insets(0, 0, 5, 0);
		gbc_panelPlayerInfo.gridheight = 2;
		gbc_panelPlayerInfo.gridx = 2;
		gbc_panelPlayerInfo.gridy = 0;
		panelInfo.add(panelPlayerInfo, gbc_panelPlayerInfo);
		panelPlayerInfo.setLayout(new BorderLayout(0, 0));
		
		listPlayers.setBackground(UIManager.getColor("EditorPane.disabledBackground"));
		listPlayers.setEnabled(false);
		listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelPlayerInfo.add(listPlayers, BorderLayout.CENTER);
		
		JPanel panelGameInfo = new JPanel();
		panelGameInfo.setBorder (BorderFactory.createTitledBorder ("Game info"));
		GridBagConstraints gbc_panelGameInfo = new GridBagConstraints();
		gbc_panelGameInfo.fill = GridBagConstraints.BOTH;
		gbc_panelGameInfo.insets = new Insets(0, 0, 5, 5);
		gbc_panelGameInfo.gridwidth = 2;
		gbc_panelGameInfo.gridx = 0;
		gbc_panelGameInfo.gridy = 1;
		panelInfo.add(panelGameInfo, gbc_panelGameInfo);
		
		JLabel lblMapName = new JLabel("Map name");
		lblMapName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMapName.setBounds(10, 77, 80, 14);
		
		JLabel lblGameVersion = new JLabel("Game version");
		lblGameVersion.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGameVersion.setBounds(10, 36, 80, 14);
		
		JLabel lblGamemode = new JLabel("Gamemode");
		lblGamemode.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGamemode.setBounds(10, 16, 80, 14);
		
		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPlayers.setBounds(10, 57, 80, 14);
		
		valGamemode = new JLabel("");
		valGamemode.setBounds(100, 16, 268, 14);
		
		valGameVersion = new JLabel("");
		valGameVersion.setBounds(100, 36, 268, 14);
		
		valPlayers = new JLabel("");
		valPlayers.setBounds(100, 57, 268, 14);
		
		valMapName = new JLabel("");
		valMapName.setBounds(100, 77, 268, 14);
		panelGameInfo.setLayout(null);
		panelGameInfo.add(lblGameVersion);
		panelGameInfo.add(lblGamemode);
		panelGameInfo.add(lblMapName);
		panelGameInfo.add(lblPlayers);
		panelGameInfo.add(valPlayers);
		panelGameInfo.add(valMapName);
		panelGameInfo.add(valGamemode);
		panelGameInfo.add(valGameVersion);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.anchor = GridBagConstraints.WEST;
		gbc_verticalStrut.fill = GridBagConstraints.VERTICAL;
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 1;
		panelInfo.add(verticalStrut, gbc_verticalStrut);
		
		JPanel panelErrors = new JPanel();
		GridBagConstraints gbc_panelErrors = new GridBagConstraints();
		gbc_panelErrors.anchor = GridBagConstraints.NORTH;
		gbc_panelErrors.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelErrors.gridwidth = 3;
		gbc_panelErrors.gridx = 0;
		gbc_panelErrors.gridy = 2;
		panelInfo.add(panelErrors, gbc_panelErrors);
		panelErrors.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		imageLabel = new JLabel();
		imageLabel.setIcon(ii);
		imageLabel.setVisible(false);
		panelErrors.add(imageLabel, java.awt.BorderLayout.CENTER);
		
		valErrors = new JLabel("");
		panelErrors.add(valErrors);
	}
}
