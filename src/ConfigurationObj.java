import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

public class ConfigurationObj {
	
	public int numNodes = 0;
	public int Delay = 0;
	public int CSexecutionTime = 0;
	public int numRequest = 0;	
	public NodeObj localInfor = null;	
	public HashMap<Integer,NodeObj> neighbors = new HashMap<Integer,NodeObj>();
	public PriorityBlockingQueue <TimeStampWithID> pqueue ;
	public Set<Integer> conditionL1 = new HashSet<Integer>();
	public TimeStampWithID localRequestStamp;
	public LamportMEAlgo lamp;
	public String vary;
}
