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
			} else {
				valPlugins.setText(response.getPlugins());
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
		frmMinecraftServerQuery.setBounds(100, 100, 800, 580);
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
		gbl_panelInfo.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
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
				panelServerInfo.setLayout(new BorderLayout(0, 0));
				
				JPanel panel = new JPanel();
				panelServerInfo.add(panel, BorderLayout.NORTH);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{63, 58, 0};
				gbl_panel.rowHeights = new int[]{14, 0, 0, 0, 10, 0};
				gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				
				JLabel lblGameId = new JLabel("Game ID");
				lblGameId.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lblGameId = new GridBagConstraints();
				gbc_lblGameId.fill = GridBagConstraints.BOTH;
				gbc_lblGameId.insets = new Insets(0, 0, 5, 5);
				gbc_lblGameId.gridx = 0;
				gbc_lblGameId.gridy = 0;
				panel.add(lblGameId, gbc_lblGameId);
				
				valGameID = new JLabel("");
				GridBagConstraints gbc_valGameID = new GridBagConstraints();
				gbc_valGameID.fill = GridBagConstraints.BOTH;
				gbc_valGameID.insets = new Insets(0, 0, 5, 0);
				gbc_valGameID.gridx = 1;
				gbc_valGameID.gridy = 0;
				panel.add(valGameID, gbc_valGameID);
				
				JLabel lblMOTD = new JLabel("MOTD");
				lblMOTD.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lblMOTD = new GridBagConstraints();
				gbc_lblMOTD.fill = GridBagConstraints.BOTH;
				gbc_lblMOTD.insets = new Insets(0, 0, 5, 5);
				gbc_lblMOTD.gridx = 0;
				gbc_lblMOTD.gridy = 1;
				panel.add(lblMOTD, gbc_lblMOTD);
				
				valMOTD = new JLabel("");
				GridBagConstraints gbc_valMOTD = new GridBagConstraints();
				gbc_valMOTD.fill = GridBagConstraints.BOTH;
				gbc_valMOTD.insets = new Insets(0, 0, 5, 0);
				gbc_valMOTD.gridx = 1;
				gbc_valMOTD.gridy = 1;
				panel.add(valMOTD, gbc_valMOTD);
				
				JLabel lblHostname = new JLabel("Hostname");
				lblHostname.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lblHostname = new GridBagConstraints();
				gbc_lblHostname.fill = GridBagConstraints.BOTH;
				gbc_lblHostname.insets = new Insets(0, 0, 5, 5);
				gbc_lblHostname.gridx = 0;
				gbc_lblHostname.gridy = 2;
				panel.add(lblHostname, gbc_lblHostname);
				
				valHostname = new JLabel("");
				GridBagConstraints gbc_valHostname = new GridBagConstraints();
				gbc_valHostname.fill = GridBagConstraints.BOTH;
				gbc_valHostname.insets = new Insets(0, 0, 5, 0);
				gbc_valHostname.gridx = 1;
				gbc_valHostname.gridy = 2;
				panel.add(valHostname, gbc_valHostname);
				
				JLabel lblPlugins = new JLabel("Plugins");
				lblPlugins.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lblPlugins = new GridBagConstraints();
				gbc_lblPlugins.fill = GridBagConstraints.BOTH;
				gbc_lblPlugins.insets = new Insets(0, 0, 5, 5);
				gbc_lblPlugins.gridx = 0;
				gbc_lblPlugins.gridy = 3;
				panel.add(lblPlugins, gbc_lblPlugins);
				
				valPlugins = new JLabel("");
				GridBagConstraints gbc_valPlugins = new GridBagConstraints();
				gbc_valPlugins.insets = new Insets(0, 0, 5, 0);
				gbc_valPlugins.fill = GridBagConstraints.BOTH;
				gbc_valPlugins.gridx = 1;
				gbc_valPlugins.gridy = 3;
				panel.add(valPlugins, gbc_valPlugins);
				
				JPanel panel_1 = new JPanel();
				panelServerInfo.add(panel_1, BorderLayout.CENTER);
						panel_1.setLayout(new BorderLayout(0, 0));
						listPlugins.setVisibleRowCount(16);
						listPlugins.setLayoutOrientation(JList.VERTICAL_WRAP);
						panel_1.add(listPlugins);
						
								listPlugins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								listPlugins.setBackground(UIManager.getColor("Panel.background"));
		
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
		
		listPlayers.setBackground(UIManager.getColor("Panel.background"));
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
		GridBagLayout gbl_panelGameInfo = new GridBagLayout();
		gbl_panelGameInfo.columnWidths = new int[]{80, 73, 0};
		gbl_panelGameInfo.rowHeights = new int[]{14, 14, 14, 14, 0};
		gbl_panelGameInfo.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelGameInfo.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelGameInfo.setLayout(gbl_panelGameInfo);
		
		JLabel lblGamemode = new JLabel("Gamemode");
		lblGamemode.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblGamemode = new GridBagConstraints();
		gbc_lblGamemode.anchor = GridBagConstraints.NORTH;
		gbc_lblGamemode.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGamemode.insets = new Insets(0, 0, 5, 5);
		gbc_lblGamemode.gridx = 0;
		gbc_lblGamemode.gridy = 0;
		panelGameInfo.add(lblGamemode, gbc_lblGamemode);
		
		valGamemode = new JLabel("");
		GridBagConstraints gbc_valGamemode = new GridBagConstraints();
		gbc_valGamemode.fill = GridBagConstraints.BOTH;
		gbc_valGamemode.insets = new Insets(0, 0, 5, 0);
		gbc_valGamemode.gridx = 1;
		gbc_valGamemode.gridy = 0;
		panelGameInfo.add(valGamemode, gbc_valGamemode);
		
		JLabel lblGameVersion = new JLabel("Game version");
		lblGameVersion.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblGameVersion = new GridBagConstraints();
		gbc_lblGameVersion.anchor = GridBagConstraints.NORTH;
		gbc_lblGameVersion.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGameVersion.insets = new Insets(0, 0, 5, 5);
		gbc_lblGameVersion.gridx = 0;
		gbc_lblGameVersion.gridy = 1;
		panelGameInfo.add(lblGameVersion, gbc_lblGameVersion);
		
		valGameVersion = new JLabel("");
		GridBagConstraints gbc_valGameVersion = new GridBagConstraints();
		gbc_valGameVersion.fill = GridBagConstraints.BOTH;
		gbc_valGameVersion.insets = new Insets(0, 0, 5, 0);
		gbc_valGameVersion.gridx = 1;
		gbc_valGameVersion.gridy = 1;
		panelGameInfo.add(valGameVersion, gbc_valGameVersion);
		
		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblPlayers = new GridBagConstraints();
		gbc_lblPlayers.anchor = GridBagConstraints.NORTH;
		gbc_lblPlayers.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPlayers.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayers.gridx = 0;
		gbc_lblPlayers.gridy = 2;
		panelGameInfo.add(lblPlayers, gbc_lblPlayers);
		
		valPlayers = new JLabel("");
		GridBagConstraints gbc_valPlayers = new GridBagConstraints();
		gbc_valPlayers.fill = GridBagConstraints.BOTH;
		gbc_valPlayers.insets = new Insets(0, 0, 5, 0);
		gbc_valPlayers.gridx = 1;
		gbc_valPlayers.gridy = 2;
		panelGameInfo.add(valPlayers, gbc_valPlayers);
		
		JLabel lblMapName = new JLabel("Map name");
		lblMapName.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblMapName = new GridBagConstraints();
		gbc_lblMapName.anchor = GridBagConstraints.NORTH;
		gbc_lblMapName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMapName.insets = new Insets(0, 0, 0, 5);
		gbc_lblMapName.gridx = 0;
		gbc_lblMapName.gridy = 3;
		panelGameInfo.add(lblMapName, gbc_lblMapName);
		
		valMapName = new JLabel("");
		GridBagConstraints gbc_valMapName = new GridBagConstraints();
		gbc_valMapName.fill = GridBagConstraints.BOTH;
		gbc_valMapName.gridx = 1;
		gbc_valMapName.gridy = 3;
		panelGameInfo.add(valMapName, gbc_valMapName);
		
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
