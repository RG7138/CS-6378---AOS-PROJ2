import java.io.ObjectOutputStream;

public class SendMessageThread extends Thread{
	
	ConfigurationObj obj;
	
	public SendMessageThread(ConfigurationObj nodeObj) {
		this.obj = nodeObj;
	}
	
	void sendRequest(MessageStruc msg) throws InterruptedException{
		
		synchronized(obj) {
			
			for(Integer keyset : this.obj.neighbors.keySet()) {
				
				try {
					PerformanceCalService.getInstance().addSendMessageCount();
					LamportLogicalClockService.getInstance().sendAction();
					VectorClockService.getInstance().sendAction();
					ObjectOutputStream oos = this.obj.neighbors.get(keyset).oos;
					
					//if(oos !=null ) {
						
						RequestMsg tmp = (RequestMsg) msg;
					
						tmp.LogicalClockVal = LamportLogicalClockService.getInstance().getValue();
						
						tmp.VectorClockVal = VectorClockService.getInstance().toString();
						
						msg = tmp;
					
						//System.out.println("Request Message sent to:"+ keyset);
						oos.writeObject(msg);
						oos.flush();
					//}
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	void sendRelease(MessageStruc msg) throws InterruptedException{
		
		synchronized(obj) {
			
			for(Integer keyset : this.obj.neighbors.keySet()) {
				
				try {
					LamportLogicalClockService.getInstance().sendAction();
					VectorClockService.getInstance().sendAction();
					ObjectOutputStream oos = this.obj.neighbors.get(keyset).oos;
					
					ReleaseMsg tmp = (ReleaseMsg) msg;
					
					tmp.LogicalClockVal = LamportLogicalClockService.getInstance().getValue();
					tmp.VectorClockVal = VectorClockService.getInstance().toString();
					
					msg = tmp;
					
					//System.out.println("Release Message sent to:"+ keyset);
					//if(oos!=null) {
						oos.writeObject(msg);
						oos.flush();
					//}
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
void sendReply(ReplyMsg msg,RequestMsg rmsg) throws InterruptedException{
		
		synchronized(obj) {
			
			
				
				try {
					LamportLogicalClockService.getInstance().sendAction();
					VectorClockService.getInstance().sendAction();
					ObjectOutputStream oos = this.obj.neighbors.get(rmsg.nodeID).oos;
					
					//ReplyMsg tmp = msg;
					
					msg.LogicalClockVal = LamportLogicalClockService.getInstance().getValue();
					msg.VectorClockVal = VectorClockService.getInstance().toString();
					
					//System.out.println("Reply Message sent to:"+ rmsg.nodeID);
					//if(oos!=null) {
					oos.writeObject((MessageStruc) msg);
					oos.flush();
					//}
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
				
			
			
		}
		
	}
	
}
