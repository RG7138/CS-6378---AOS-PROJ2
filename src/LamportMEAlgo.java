import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

public class LamportMEAlgo{
	
	Set<Integer> conditionL1 = new HashSet<Integer>();
	int localID;
	int numOfNodes;
	TimeStampWithID localRequestStamp;
	PriorityBlockingQueue <TimeStampWithID> pqueue ;
	//ConfigurationObj obj;
	
	public LamportMEAlgo(int numofnodes,int nodeID,ConfigurationObj obj) {
		synchronized(obj) {
			Comparator<TimeStampWithID> comparator = new TimeStampWithID(0,0);
			this.localID = nodeID; 
			this.numOfNodes = numofnodes;
			pqueue = new PriorityBlockingQueue<TimeStampWithID>(obj.numNodes, comparator);
			
			obj.conditionL1 = conditionL1;
			obj.pqueue = new PriorityBlockingQueue<TimeStampWithID>(obj.numNodes, comparator);
			
			obj.localRequestStamp = new TimeStampWithID(localID, Integer.MAX_VALUE);
			
			obj.lamp = this;
			
			//this.obj = obj;
			
		}
	}
	
	public synchronized void csEnter(ConfigurationObj obj) {
		
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			RequestMsg msg = new RequestMsg();
			msg.nodeID = obj.localInfor.nodeId;
			
			obj.localRequestStamp.timeStamp = ScalarClockHelperClass.getInstance().getValue();
			
			msg.timestamp = obj.localRequestStamp.timeStamp;
			if(!obj.pqueue.contains(msg.nodeID)) {
				obj.pqueue.add(obj.localRequestStamp);
			}
			else {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			System.out.println("\nPriority Queue:");
//			for(var item: obj.pqueue) {
//				System.out.println(item.nodeId + " " + item.timeStamp);
//			}
			
			try {
				new SendMessageThread(obj).sendRequest(msg);
				if(!((obj.pqueue.peek().nodeId == localID) && (obj.conditionL1.size() == numOfNodes-1))) {
					try {
						this.wait();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("hello!");
			
		
	}
	
	public void check(ConfigurationObj obj) {
//		if(msg instanceof RequestMsg) {
//			
//			RequestMsg tmp = (RequestMsg) msg;
//			
//			LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
//			//System.out.println("Request message received from Node:"+tmp.nodeID);
//			
//			LamportMEAlgo.handleRequest(nodeObj,tmp);
//			
//		}
//		else if(msg instanceof ReleaseMsg) {
//			
//			
//			ReleaseMsg tmp = (ReleaseMsg) msg;
//			LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
//			LamportMEAlgo.handleReleaseMsg(nodeObj,tmp);
//			
//		}
//		else if(msg instanceof ReplyMsg) {
//			
//			
//			ReplyMsg tmp = (ReplyMsg) msg;
//			LamportLogicalClockService.getInstance().receiveAction(tmp.LogicalClockVal);
//			LamportMEAlgo.handleReply(nodeObj,tmp);
//		}
		
			//System.out.println("conditionL1 size:"+obj.conditionL1.size());
			if((!obj.pqueue.isEmpty())&& (obj.pqueue.peek().nodeId == obj.localInfor.nodeId) && (obj.conditionL1.size()== obj.numNodes-1)){
					//System.out.println("here!!");
					synchronized(this) {
						this.notifyAll();
					}
				}			
			
	}
	
	public void csLeave(ConfigurationObj obj) throws InterruptedException {
		
		synchronized(obj) {
			
			obj.localRequestStamp.timeStamp = Integer.MAX_VALUE;
			
			obj.conditionL1.clear();
			
			ReleaseMsg msg = new ReleaseMsg();
			msg.nodeID = obj.localInfor.nodeId;
			
			int timestamp = ScalarClockHelperClass.getInstance().getValue();
			
			msg.timestamp = timestamp;
			
			new SendMessageThread(obj).sendRelease(msg);
			
			if(!obj.pqueue.isEmpty()) {
				obj.pqueue.poll();
			}
			
			//printing priority queue at time of leaving CS
//			System.out.println("\nPriority Queue(after leaving CS):");
//			for(var item: obj.pqueue) {
//				System.out.println(item.nodeId + " " + item.timeStamp);
//			}
			
		}
		
	}
	
	public static void handleReleaseMsg(ConfigurationObj obj,ReleaseMsg msg) {
		
		synchronized(obj) {
			
//			ReleaseMsg tmp = msg;
//			
//			int recevidetimestamp = tmp.timestamp;
//			TimeStampWithID receivedTimeStamp = new TimeStampWithID(msg.nodeID, recevidetimestamp);
//			
//			if((!obj.conditionL1.contains(receivedTimeStamp.nodeId))
//					&& (obj.localRequestStamp.compare(receivedTimeStamp, obj.localRequestStamp)==1))
//			{
//				obj.conditionL1.add(receivedTimeStamp.nodeId);
//			}
			
			if(!obj.pqueue.isEmpty()) {
				obj.pqueue.poll();
			}
			
			
			//System.out.println("\nPriority Queue(After Receiving Release Msg):");
//			for(var item: obj.pqueue) {
//				System.out.println(item.nodeId + " " + item.timeStamp);
//			}
			
			//obj.lamp.check(obj);
		
		}
		
	}
	
	public static void handleRequest(ConfigurationObj obj,RequestMsg msg) {
		
		synchronized (obj) {
			
			RequestMsg tmp = msg;
//			
			int recevidetimestamp = tmp.timestamp;
			TimeStampWithID receivedTimeStamp = new TimeStampWithID(msg.nodeID, recevidetimestamp);
			
			if((!obj.conditionL1.contains(receivedTimeStamp.nodeId))
					&& obj.localRequestStamp.compare(receivedTimeStamp, obj.localRequestStamp)==1)
			{
				obj.conditionL1.add(receivedTimeStamp.nodeId);
			}
			
			if(!obj.pqueue.contains(receivedTimeStamp)) {
				obj.pqueue.add(receivedTimeStamp);
			}
			
//			System.out.println("\nPriority Queue:");
//			for(var item: obj.pqueue) {
//				System.out.println(item.nodeId + " " + item.timeStamp);
//			}
			
			ReplyMsg rplymsg = new ReplyMsg();
			rplymsg.nodeID = obj.localInfor.nodeId;
			rplymsg.timestamp = ScalarClockHelperClass.getInstance().getValue();
			
			try {
				new SendMessageThread(obj).sendReply(rplymsg,msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void handleReply(ConfigurationObj obj,ReplyMsg msg) {
			synchronized (obj) {
			
				ReplyMsg tmp = msg;
			
				int recevidetimestamp = tmp.timestamp;
				TimeStampWithID receivedTimeStamp = new TimeStampWithID(msg.nodeID, recevidetimestamp);
			
				if((!obj.conditionL1.contains(receivedTimeStamp.nodeId))
						/*&& obj.localRequestStamp.compare(receivedTimeStamp, obj.localRequestStamp)==1*/)
				{
					obj.conditionL1.add(receivedTimeStamp.nodeId);
				}
				//obj.lamp.check(obj);
			}
			
	}
}
