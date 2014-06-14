package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private Color red = new Color(0xFF, 0x00, 0x00);
	private Color green = new Color(0x5b, 0x7d, 0x34);
	private JLabel imageLabel;

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
	
	private void initializeEvents() {
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageLabel.setVisible(false);
				MCQuery query = new MCQuery(textIP.getText(), textPort.getText());
				QueryResponse response = query.fullStat();

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
					valPlugins.setText(response.getPlugins());
					valGamemode.setText(response.getGameMode());
					valGameVersion.setText(response.getGameVersion());
					valPlayers.setText(response.getOnlinePlayers() + " of " + response.getMaxPlayers());
					valMapName.setText(response.getMapName());
					
					DefaultListModel<String> modelo = new DefaultListModel<String>();
					
					for(String playerName: response.getPlayerList()){
						modelo.addElement(playerName);
					}
					
					listPlayers.setModel(modelo);
				}
			}
		});
	}

	private void initialize() {
		frmMinecraftServerQuery = new JFrame();
		frmMinecraftServerQuery.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/resources/images/icon.png")));
		frmMinecraftServerQuery.setResizable(false);
		frmMinecraftServerQuery.setTitle("MSS - Minecraft Server Status");
		frmMinecraftServerQuery.setBounds(100, 100, 450, 333);
		frmMinecraftServerQuery.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelMain = new JPanel();
		frmMinecraftServerQuery.getContentPane().add(panelMain, BorderLayout.SOUTH);
				
		JLabel lblIp = new JLabel("IP");
		
		textIP = new JTextField();
		textIP.setColumns(10);
		
		textPort = new JTextField();
		textPort.setText("25565");
		textPort.setColumns(10);
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
		panelInfo.setLayout(null);
		
		JPanel panelServerInfo = new JPanel();
		panelServerInfo.setBounds(10, 11, 230, 110);
		panelServerInfo.setBorder (BorderFactory.createTitledBorder ("Server info"));
		panelInfo.add(panelServerInfo);
		
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
		valPlugins.setBounds(85, 80, 135, 14);
		
		valHostname = new JLabel("");
		valHostname.setBounds(85, 60, 135, 14);
		
		valGameID = new JLabel("");
		valGameID.setBounds(85, 40, 135, 14);
		
		valMOTD = new JLabel("");
		valMOTD.setBounds(85, 20, 135, 14);
		panelServerInfo.setLayout(null);
		panelServerInfo.add(lblGameId);
		panelServerInfo.add(lblMOTD);
		panelServerInfo.add(lblHostname);
		panelServerInfo.add(lblPlugins);
		panelServerInfo.add(valGameID);
		panelServerInfo.add(valMOTD);
		panelServerInfo.add(valPlugins);
		panelServerInfo.add(valHostname);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBounds(80, 11, 10, 110);
		panelInfo.add(verticalStrut_1);
		
		JPanel panelGameInfo = new JPanel();
		panelGameInfo.setBounds(10, 128, 230, 100);
		panelGameInfo.setBorder (BorderFactory.createTitledBorder ("Game info"));
		panelInfo.add(panelGameInfo);
		
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
		valGamemode.setBounds(100, 16, 120, 14);
		
		valGameVersion = new JLabel("");
		valGameVersion.setBounds(100, 36, 120, 14);
		
		valPlayers = new JLabel("");
		valPlayers.setBounds(100, 57, 120, 14);
		
		valMapName = new JLabel("");
		valMapName.setBounds(100, 77, 120, 14);
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
		verticalStrut.setBounds(97, 132, 10, 96);
		panelInfo.add(verticalStrut);
		
		JPanel panelPlayerInfo = new JPanel();
		panelPlayerInfo.setBounds(248, 11, 186, 217);
		panelPlayerInfo.setBorder (BorderFactory.createTitledBorder ("Player info"));
		panelInfo.add(panelPlayerInfo);
		panelPlayerInfo.setLayout(new BorderLayout(0, 0));

		listPlayers.setBackground(UIManager.getColor("EditorPane.disabledBackground"));
		listPlayers.setEnabled(false);
		listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelPlayerInfo.add(listPlayers, BorderLayout.CENTER);
		
		JPanel panelErrors = new JPanel();
		panelErrors.setBounds(0, 230, 444, 42);
		panelInfo.add(panelErrors);
		panelErrors.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		imageLabel = new JLabel();
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/com/resources/images/loading2.gif"));
		imageLabel.setIcon(ii);
		imageLabel.setVisible(false);
		panelErrors.add(imageLabel, java.awt.BorderLayout.CENTER);
		
		valErrors = new JLabel("");
		panelErrors.add(valErrors);
	}
}
