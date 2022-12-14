import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class AlgoSuperClass {
	
	public int RequestDelay;
	public int csexecutiontime;
	
	public ConfigurationObj obj;
	
	public LamportMEAlgo lamp=null;
	
	public String algoUsed = null;
	
	public AlgoSuperClass(ConfigurationObj nodeobj,String algo) {
		
		synchronized(nodeobj) {
		RequestDelay = nodeobj.Delay;
		csexecutiontime = nodeobj.CSexecutionTime;
		this.obj = nodeobj;
		
		try {
			if(algo.equals("lamport")) {
				this.lamp = new LamportMEAlgo(nodeobj.numNodes,nodeobj.localInfor.nodeId,nodeobj);
				this.algoUsed = "lamport";
			}
			else {
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		}
	}
	
	public void writetofile(String filename,TimeInterval t) {
		
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(filename, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			file.seek(file.length());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			file.writeBytes(t.toString());
			file.writeBytes("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		for(int i=0;i<obj.numRequest;++i) {
			
			int randDelay = RequestDelay;
			int csexect = csexecutiontime;
			
			if(obj.vary.equals("e")) {
				csexect = nextcsExecutionTimer();
			}
			else if(obj.vary.equals("r")) {
				randDelay = nextInterRequestDelay();
			}
			
			try {
				Thread.sleep(randDelay);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			if(this.algoUsed.equals("lamport")) {
				long requestCSTime = System.currentTimeMillis();
				lamp.csEnter(obj);
				
				long grantedCSTime = System.currentTimeMillis();
				
				//System.out.println(grantedCSTime);
				if(i%10 == 0) {
					System.out.println("Critical Section Exec. No.: "+(i+1)+" with execution time "+csexect+"and interrequest delay"+randDelay);
				}
				
				MyVector enterCSTimeStamp = MyVector.copy(VectorClockHelperClass.getInstance().toString());
				long enteredCSSystemTime = System.currentTimeMillis();
				
				try {
					VectorClockHelperClass.getInstance().tick();
					Thread.sleep(csexect);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				SummaryCalHelperClass.getInstance().updateCSTime(requestCSTime, grantedCSTime);
				MyVector leaveCSTimeStamp = MyVector.copy(VectorClockHelperClass.getInstance().toString());			
				long leaveCSSystemTime = System.currentTimeMillis();
				//System.out.println(leaveCSSystemTime);
				TimeInterval curTimeInterval =  new TimeInterval(enterCSTimeStamp,enteredCSSystemTime,leaveCSTimeStamp, leaveCSSystemTime, obj.localInfor.nodeId);
				
				String filename = System.getProperty("user.dir")+"/TimeInterval"+obj.localInfor.nodeId;
				writetofile(filename,curTimeInterval);
				//System.out.println("Written to file:"+curTimeInterval.toString());
				
				VectorClockHelperClass.getInstance().tick();
				try {
					lamp.csLeave(obj);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("Application leaves CS!");
				
			}
			
		}
		
//		while(!obj.pqueue.isEmpty()) {
//			
//		}
	}
	
	public int nextInterRequestDelay(){
		
		Random rand  = new Random();
		
		int[] vary = {0,2,4,6,8,10}; 
		
		int x = rand.nextInt(6);
		
		return vary[x];
		
		//return RequestDelay;
	}
	
	public int nextcsExecutionTimer(){
		
		Random rand  = new Random();
		
		int[] vary = {0,2,4,6,8,10}; 
		
		int x = rand.nextInt(6);
		
		return vary[x];
		
		//return csexecutiontime;
	}
	
}
