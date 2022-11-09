import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiveMessageClass extends Thread {
	
	Socket socket;
	ConfigurationObj nodeObj;
	
	public ReceiveMessageClass(Socket ClientSocket,ConfigurationObj nodeObj) {
		this.socket = ClientSocket;
		this.nodeObj = nodeObj;
	}
	
	public void MessageListener(ObjectInputStream ois) {
		
		try {
			
			PerformanceCalService.getInstance().addReceiveMessageCount();
			MessageStruc msg;
			msg = (MessageStruc) ois.readObject();
			
			synchronized(nodeObj) {
				
				if(msg instanceof RequestMsg) {
					
					RequestMsg tmp = (RequestMsg) msg;
					
					LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
					VectorClockService.getInstance().receiveAction(tmp.VectorClockVal);
					//System.out.println("Request message received from Node:"+tmp.nodeID);
					
					
					LamportMEAlgo.handleRequest(nodeObj, tmp);
					
				}
				else if(msg instanceof ReleaseMsg) {
					
					
					ReleaseMsg tmp = (ReleaseMsg) msg;
					LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
					VectorClockService.getInstance().receiveAction(tmp.VectorClockVal);
					LamportMEAlgo.handleReleaseMsg(nodeObj,tmp);
					
				}
				else if(msg instanceof ReplyMsg) {
					
					
					ReplyMsg tmp = (ReplyMsg) msg;
					LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
					VectorClockService.getInstance().receiveAction(tmp.VectorClockVal);
					LamportMEAlgo.handleReply(nodeObj,tmp);
				}
				
				//System.out.println("Before checking!!!");
				nodeObj.lamp.check(nodeObj);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(true){
			MessageListener(ois);
		}
	}
}
