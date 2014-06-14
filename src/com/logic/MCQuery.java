package com.logic;

import java.net.*;

/**
 * A class that handles Minecraft Query protocol requests
 * 
 * @author Ryan McCann
 */
public class MCQuery
{
	final static byte HANDSHAKE = 9;
	final static byte STAT = 0;
	
	String serverAddress = "127.0.0.1";
	int queryPort = 25565; // the default minecraft query port
	int localPort = 25566; // the local port we're connected to the server on
	int milisecondsTimeout = 1000;
	
	private DatagramSocket socket = null; //prevent socket already bound exception
	private int token = -1;
	
	public boolean errorCheck = false;
	public String errorMessage = "";
	
	public MCQuery(String address, String port)
	{
		serverAddress = address;
		queryPort = Integer.parseInt(port);
	}
	
	public void setTimeout(int timeout) {
		milisecondsTimeout = timeout;
	}
	
	// used to get a session token
	private void handshake()
	{
		QueryRequest req = new QueryRequest();
		req.type = HANDSHAKE;
		req.sessionID = generateSessionID();
		
		int val = 11 - req.toBytes().length; //should be 11 bytes total
		byte[] input = ByteUtils.padArrayEnd(req.toBytes(), val);
		byte[] result = sendUDP(input);
		
		if (result != null) {
			token = Integer.parseInt(new String(result).trim());
		}
	}

	/**
	 * Use this to get basic status information from the server.
	 * @return a <code>QueryResponse</code> object
	 */
	public QueryResponse basicStat()
	{
		handshake(); //get the session token first

		if (token != -1) {
			QueryRequest req = new QueryRequest(); //create a request
			req.type = STAT;
			req.sessionID = generateSessionID();
			req.setPayload(token);
	
			byte[] send = req.toBytes();		
			byte[] result = sendUDP(send);
			
			QueryResponse res = new QueryResponse(result, false);
			return res;
		}
		
		return null;
	}
	
	/**
	 * Use this to get more information, including players, from the server.
	 * basicStat() calls handshake()
	 * QueryResponse basicResp = this.basicStat();
	 * int numPlayers = basicResp.onlinePlayers;
	 * note: buffer size = base + #players(online) * 16(max username length)
	 * @return a <code>QueryResponse</code> object
	 */
	public QueryResponse fullStat()
	{
		handshake();
		
		if (token != -1) {
			QueryRequest req = new QueryRequest();
			req.type = STAT;
			req.sessionID = generateSessionID();
			req.setPayload(token);
			req.payload = ByteUtils.padArrayEnd(req.payload, 4); //for full stat, pad the payload with 4 null bytes
			
			byte[] send = req.toBytes();
			byte[] result = sendUDP(send);
	
			QueryResponse res = new QueryResponse(result, true);
			return res;
		}

		return null;
	}
	
	private byte[] sendUDP(byte[] input)
	{
		try
		{
			while(socket == null)
			{
				try {
					socket = new DatagramSocket(localPort); //create the socket
				} catch (BindException e) {
					++localPort; // increment if port is already in use
				}
			}
			
			//create a packet from the input data and send it on the socket
			InetAddress address = InetAddress.getByName(serverAddress); //create InetAddress object from the address
			DatagramPacket packet1 = new DatagramPacket(input, input.length, address, queryPort);
			socket.send(packet1);
			
			//receive a response in a new packet
			byte[] out = new byte[1024]; //guess at max size
			DatagramPacket packet = new DatagramPacket(out, out.length);
			socket.setSoTimeout(milisecondsTimeout);
			socket.receive(packet);
			
			return packet.getData();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			errorCheck = true;
			errorMessage = e.getMessage();
		}
		catch (SocketTimeoutException e)
		{
			//e.printStackTrace();
			errorCheck = true;
			errorMessage = "Is the server offline?";
		}
		catch (UnknownHostException e)
		{
			//e.printStackTrace();
			errorCheck = true;
			errorMessage = "Unknown host!";
		}
		catch (Exception e) //any other exceptions that may occur
		{
			e.printStackTrace();
			errorCheck = true;
			errorMessage = e.getMessage();
		}
		
		return null;
	}
	
	/**
	 * Can be anything, so we'll just use 1 for now. Apparently it can be omitted altogether.
	 */
	private int generateSessionID()
	{
		return 1;
	}
	
	@Override
	public void finalize()
	{
		socket.close();
	}
}
