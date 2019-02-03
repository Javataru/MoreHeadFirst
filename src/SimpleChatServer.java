import java.io.*;
import java.net.*;
import java.util.*;
public class SimpleChatServer {
	ArrayList clientOutputStreams;
	public class clientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		public clientHandler (Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		public void Run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					tellEveryone(message);
					
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	

	public static void main(String[] args) {
		new SimpleChatServer().go();

	}
	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				Thread t = new Thread(new clientHandler(clientSocket));
			t.start();
			System.out.println("Recieved a connection");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} 
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
