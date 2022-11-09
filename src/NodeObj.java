import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NodeObj {
	public int nodeId;
	public String hostName;
	public int port;
	public Socket ipAddress;
	public ObjectOutputStream oos; //created in clientconnectionheleprclass
}
