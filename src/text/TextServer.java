package text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				
				String request = new String(in.readLine());
				
				File f = new File(request);
				
				if(f.exists() && f.getAbsolutePath().startsWith(pathPrefix)) {
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
