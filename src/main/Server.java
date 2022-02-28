package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;

public class Server{
	public static void main(String[] args) {

		try {
			ServerSocket server = new ServerSocket(5000);
			System.out.println("Esperando conexión");
			Socket socket = server.accept();
			System.out.println("Conectado");

			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(isr);

			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bwriter = new BufferedWriter(osw);
			
			while(true) {
				String line = breader.readLine();
				System.out.println("Mensaje recibido: "+line);
				
				if(line.contains("remoteIpconfig")) {
					InetAddress myAdress =InetAddress.getLocalHost();
					bwriter.write(myAdress.getHostAddress()+"\n");
					bwriter.flush();
				}
				if(line.contains("interface")) {
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
				if(line.contains("whatTimeIsIt")) {
					Calendar c = Calendar.getInstance();
					String fecha = c.getTime().toString();
					bwriter.write(fecha+"\n");
					bwriter.flush();
				}
				if(line.contains("RTT")) {
					line = breader.readLine(); 
					System.out.println(line);
					bwriter.write(line+"\n");
					bwriter.flush();
				}
				if(line.contains("speed")) {
					line = breader.readLine();
					System.out.println(line);
					bwriter.write(line+"\n");
					bwriter.flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}