import java.io.Serializable;
public class MessageStruc implements Serializable {
	
}

class timestampID extends MessageStruc implements Serializable{
	int nodeID;
	int timestamp;
}

class RequestMsg extends MessageStruc implements Serializable{
	public String msg = "Request";
	public int nodeID;
	public int timestamp;//Lamport Logical Clock Value
	public int LogicalClockVal;
	public String VectorClockVal;
}

class ReleaseMsg extends MessageStruc implements Serializable{
	public String msg = "Release";
	public int nodeID;
	public int timestamp;
	public int LogicalClockVal;
	public String VectorClockVal;
}

class ReplyMsg extends MessageStruc implements Serializable{
	public String msg = "Reply";
	public int nodeID;
	public int timestamp;
	public int LogicalClockVal;
	public String VectorClockVal;
}
