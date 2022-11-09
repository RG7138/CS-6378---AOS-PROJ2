import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Experimental Evaluation: Compare the performance of the two different mutual exclusion
algorithms with respect to message complexity, response time and system throughput using exper-
iments for various values of system parameters
 * 
 *
 */
public class PerformanceCalService {
	
	private long[] sendMessageCount=null;
	private long[] receiveMessageCount=null;
	private long[] enterCSTimes=null;
	private long[] responseTime=null;
	private long receiveTotal=0;
	private long sendTotal=0;
	public int count=0;
	public int interval;
	public RandomAccessFile pmFile;
	public int nodeid;
	public String curDirectory="";
	public int RunNo;
	private static PerformanceCalService instance = new PerformanceCalService();
	public static PerformanceCalService getInstance(){
		return instance;
	}
	
	public void setDir(String curdir){
		curDirectory = curdir;
	}
	
	public void init(int interval,int nodeid,int RunNo){
		this.RunNo = RunNo;
		this.sendMessageCount=new long[interval];
		this.receiveMessageCount= new long[interval];
		this.enterCSTimes=new long[interval];
		this.responseTime=new long[interval];
		this.interval=interval;
		this.nodeid=nodeid;
		receiveTotal=0;
		sendTotal=0;
		count=0;
		String fileName= curDirectory+ "performance_file_node_"+Integer.toString(nodeid)+"_"+RunNo;
		try {
			pmFile = new RandomAccessFile(fileName,"rw");
			pmFile.setLength(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public synchronized void updateCSTime(long requestT,long grantT){
		this.sendMessageCount[count]=this.sendTotal;
		this.receiveMessageCount[count]=this.receiveTotal;
		this.enterCSTimes[count]=count+1;
		this.responseTime[count]=grantT-requestT;
		this.toFile();
		count++;	
	}	


	public synchronized void addSendMessageCount() {
		sendTotal++;
	}


	public synchronized void addReceiveMessageCount() {
		receiveTotal++;
	}
	
	public void toFile(){
	
		StringBuilder res = new StringBuilder();
			int i=count;
			res.append(this.enterCSTimes[i]);
			res.append(',');
			res.append(this.receiveMessageCount[i]);
			res.append(',');
			res.append(this.sendMessageCount[i]);
			res.append(',');
			res.append(this.responseTime[i]);
			res.append(';');
			res.append(System.getProperty("line.separator"));
			
			try {
				pmFile.writeBytes(res.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res.setLength(0);	
		
	}
	
}

