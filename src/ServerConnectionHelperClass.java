
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class ServerConnectionHelperClass implements Runnable{

	ServerSocket listener = null;
	Socket socket = null;
	int serverPort;
	public ConfigurationObj nodeObj;
	InetAddress addr;
	InetAddress addr1;
	InetAddress addr2;
	
	
	int port;
	ServerSocket serversock;
	NodeObj local;
	
//	public void AcceptClientConnections(){
//		
//		try {
//			while (true) {
//				try {
//					socket = listener.accept();
//				} catch (IOException e1) {
//					System.out.println("Connection Broken");
//					System.exit(1);
//				}
//				new ReceiveMessageClass(socket,nodeObj).start();
//				
////				Scanner reader = null;
////				try {
////					reader = new Scanner(socket.getInputStream());
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				String input=reader.nextLine();
////				int channelID=-1;
////				if(input.startsWith("NODEID:"))
////					channelID=input.charAt(7)-'0';
//				
//			}
//		}
//		finally {
//			try {
//				listener.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
//	private ServerSocket CreateServer(int serverPort, String host) {
//		
//		try {
//			
//			listener = new ServerSocket(serverPort,-1,InetAddress.getByName(nodeObj.localInfor.hostName));
//			System.out.println(serverPort);
//			addr = listener.getInetAddress();
//			
//			System.out.println(addr);
//		}
//		catch(BindException e) {
//			System.out.println("Failed Server Connection on Node" + nodeObj.localInfor.nodeId + " : " + e.getMessage() + ", Port : " + serverPort);
//			System.exit(1);
//		}
//		catch (IOException e) {
//			System.out.println(e.getMessage());
//			System.exit(1);
//		}
//		return listener;
//	}
//	
//	private void ServerSleep(int millisecs) {
//		try {
//			Thread.sleep(millisecs);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public ServerConnectionHelperClass(ConfigurationObj nodeObj) {
//		
//		this.nodeObj = nodeObj; 
//		
//		serverPort = nodeObj.localInfor.port;
//		String host = nodeObj.localInfor.hostName;
//		
//		//ServerSocket listener = null;
//		listener = CreateServer(serverPort, host);
//		
//		ServerSleep(10000);
//	}
//	
	public ServerConnectionHelperClass(int port,ConfigurationObj node) throws IOException{
		this.port=port;
		this.serversock = new ServerSocket(port,-1,InetAddress.getByName(node.localInfor.hostName));
		this.local=node.localInfor;
		this.nodeObj = node;
	}
	
	@Override
	public void run()
	{
			System.out.println("TCP sever starts listening on"+port);
			while(true){
				Socket socket = null;
				try {
						socket = serversock.accept();
				} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
				
				Scanner reader = null;
				try {
					reader = new Scanner(socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				String input=reader.nextLine();
//				int channelID=-1;
//				if(input.startsWith("NODEID:"))
//					channelID=input.charAt(7)-'0';
				
//				TCPChannel tcpChannel = new TCPChannel(channelID);
//				tcpChannel.setSocket(socket);
//				local.addChannel(tcpChannel);
//				
				new ReceiveMessageClass(socket, nodeObj).start();
				
			}
			
	
	}
	
}