import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ClientConnectionHelperClass{
	
	int destChannelID;
	private Socket socket = null;
	ConfigurationObj obj;
	private InetAddress addressResolution(String hostName) {
		
		InetAddress address = null;
		
		try {
			address = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return address;
	}
		
	private Socket connection(InetAddress address,int port,String hostName) {
		
		int trynum = 0;
		Socket client = null;
		while(client == null) {
			trynum++;
			try {
				client = new Socket(address,port);		
				System.out.println("Client Connection Achieved(address,port):"+address.getHostAddress()+"("+ hostName +")"+" "+port);
			} catch (IOException e) {
				System.out.println("Waiting for client connection with:"+address.getHostAddress()+"("+ hostName +")"+" "+port);
				//e.printStackTrace();
				//System.exit(1);
			}
			
			long seconds_to_wait = (long) Math.min(60, Math.pow(2, trynum));
			try {
				Thread.sleep(seconds_to_wait*20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return client;
	}
		
	private ObjectOutputStream ooswriting(Socket client) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
		return oos;
	}
	
	//Each node acts as a client to all its neighboring nodes
	public ClientConnectionHelperClass(ConfigurationObj mapObject) {
		for(Integer item : mapObject.neighbors.keySet()){
			
				String hostName = mapObject.neighbors.get(item).hostName;
				int port = mapObject.neighbors.get(item).port;
				InetAddress address = null;
				
				address = addressResolution(hostName);
				

				Socket client = null;
				
				client = connection(address,port,hostName);
				//Send client request to all neighboring nodes
				mapObject.neighbors.get(item).ipAddress = client;

				ObjectOutputStream oos = null;
				
				oos = ooswriting(client);
				
				mapObject.neighbors.get(item).oos = oos;	
			
		}
	}
}
	
