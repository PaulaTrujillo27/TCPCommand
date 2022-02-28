package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;

public class Server{
	
	private static ServerSocket server;
	private static Socket socket;
	private static BufferedReader breader;
	private static BufferedWriter bwriter; 
	
	public static void main(String[] args) {
		try {
			server = new ServerSocket(5000);
			while(true) {
				socket = server.accept();
				bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String message = breader.readLine();
				options(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void options(String line) throws IOException {
		System.out.println("Mensaje recibido: "+line);
		
		if(line.equalsIgnoreCase("remoteIpconfig")) {
			InetAddress myAdress =InetAddress.getLocalHost();
			bwriter.write(myAdress.getHostAddress()+"\n");
			bwriter.flush();
		}
		if(line.equalsIgnoreCase("interface")) {
			try {
				Enumeration<NetworkInterface> interfaces= NetworkInterface.getNetworkInterfaces();
				while(interfaces.hasMoreElements()) {
					NetworkInterface netN = interfaces.nextElement();
					if(netN.isUp()) {
						bwriter.write(netN.getName()+"\n");
						bwriter.flush();
						break;
					}
				}
			}catch(SocketException e) {
				e.printStackTrace();
			}
		}
		if(line.equalsIgnoreCase("whatTimeIsIt")) {
			Calendar c = Calendar.getInstance();
			String fecha = c.getTime().toString();
			bwriter.write(fecha+"\n");
			bwriter.flush();
		}
		if(line.equalsIgnoreCase("RTT")) {
			line = breader.readLine(); 
			bwriter.write(line+"\n");
			bwriter.flush();
		}
		if(line.equalsIgnoreCase("speed")) {
			line = breader.readLine();
			bwriter.write(line+"\n");
			bwriter.flush();
		}
	}
}