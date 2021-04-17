package text;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TextServer implements Runnable {

	public TextServer() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			File dir = new File("root.txt");
			String pathPrefix = dir.getParentFile().getAbsolutePath();
			
			byte[] data = new byte[1024];
			
			ServerSocket socket = new ServerSocket(3000);
			
			while(true) {
				Socket s = socket.accept();
				/*
				
				String request = new String(data);
				
				File f = new File(request);
				
				if(f.exists() && f.getAbsolutePath().startsWith(pathPrefix)) {
					
				}*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
