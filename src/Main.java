import java.io.IOException;
import java.util.Vector;

public class Main {
	
	public static TimeInterval timeArray[][] = null;
	public static TimeInterval sorted[] = null;
	public static double MeanSyncDelay = 0;
	public static double MeanExecTime = 0;
	
	static boolean testCorrect(TimeInterval wholeData[]){
		int len = wholeData.length;
		int i = 1;
		if(MyVector.compare(wholeData[0].enterCSTimeStamp, wholeData[0].leaveCSTimeStamp)!=-1) {
			System.out.println(0);
			
			return false;
		}
		while(i<len){
			if(MyVector.compare(wholeData[i].enterCSTimeStamp, wholeData[i].leaveCSTimeStamp) != -1 ) {
				System.out.println(i);
				return false;
			}
			if(MyVector.compare(wholeData[i].enterCSTimeStamp,wholeData[i-1].leaveCSTimeStamp) != 1) {
				System.out.println(i);
				return false;
			}
			i++;
		}
		
		return true;
	}
	
	static void calSystemThroughput() {
		
		long SyncDelay = 0;
		long ExecTime = 0;
		int i=0;
		
		while(i<sorted.length-1) {
			
			SyncDelay += sorted[i+1].enterCSSystemTime - sorted[i].leaveCSSystemTime;
			ExecTime += sorted[i].leaveCSSystemTime - sorted[i].enterCSSystemTime;
			i++;
		}
		
		MeanExecTime = ExecTime / ((double) (sorted.length-1));
		
		System.out.println("Mean Exec Time:"+MeanExecTime);
		
		MeanSyncDelay = SyncDelay / ((double) (sorted.length-1));
		
		System.out.println("Mean Sync Delay:"+MeanSyncDelay);
		
	}
	
	static TimeInterval[] megerSortK(TimeInterval array[][], int start, int end){
		int mid = (start+end)/2;
		if(start >= end) return null;
		if(start+1==end) return array[start];
		TimeInterval[] l1 = megerSortK(array, start, mid);
		TimeInterval[] l2 = megerSortK(array, mid, end);
		if(l1==null) return l2;
		if(l2==null) return l1;
		TimeInterval newArray[] = new TimeInterval[l1.length+l2.length];
		int l1i = 0;
		int l2i = 0;
		int ln = 0;
		while(l1i < l1.length && l2i < l2.length){
			newArray[ln] = new TimeInterval();
			if(MyVector.compare(l2[l2i].enterCSTimeStamp, l1[l1i].enterCSTimeStamp) == 1 ){
				newArray[ln].copy(l1[l1i]);
				l1i++;
			}else{
				newArray[ln].copy(l2[l2i]);
				l2i++;
			}
			ln++;
		}
		while(l1i < l1.length ){
			newArray[ln] = new TimeInterval();
			newArray[ln].copy(l1[l1i]);
			l1i++;
			ln++;
		}
		while(l2i < l2.length ){
			newArray[ln] = new TimeInterval();
			newArray[ln].copy(l2[l2i]);
			l2i++;
			ln++;
		}
		return newArray;
	}
	
	public static void VerifyResult(String file,ConfigurationObj obj) {
		
		timeArray = new TimeInterval[obj.numNodes][];
		int vc[] = new int[obj.numNodes];
		MyVector enterCSTimeStamp;
		long enterCSSystemTime;
		MyVector leaveCSTimeStamp;
		long leaveCSSystemTime;		
		for(int i = 0; i < obj.numNodes; i++){
			String name = file+i;
			Vector<String> lines = DataReader.readLines(name);
			int size = lines.size();
			timeArray[i] = new TimeInterval[size];			 
			for(int j = 0; j < size; j++){
				String line = lines.get(j);
				String datas[] = line.split(" ");
				int idx = 0;				
				for(int vck = 0; vck < obj.numNodes; vck++){
					vc[vck] = Integer.parseInt(datas[idx++]);
				}
				enterCSTimeStamp = MyVector.copy(vc);
				enterCSSystemTime = Long.parseLong(datas[idx++]);
				for(int vck = 0; vck < obj.numNodes; vck++){
					vc[vck] = Integer.parseInt(datas[idx++]);
				}
				leaveCSTimeStamp = MyVector.copy(vc);
				leaveCSSystemTime = Long.parseLong(datas[idx++]);				
				timeArray[i][j] = new TimeInterval(enterCSTimeStamp,enterCSSystemTime ,
						leaveCSTimeStamp, leaveCSSystemTime, Integer.parseInt(datas[idx++]));								
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int count = 1;
		int nodeId = 0;
		String configfile = "config.txt";
		if(args.length > 0){
			nodeId = Integer.parseInt(args[0]);
		}
		if(args.length > 1){
			configfile = args[1];
		}
		
		ConfigurationObj nodeobj = new ConfigurationObj();
		nodeobj = ParseConfigFile.parseFile(args[1],nodeId);
		
		//Create a server socket 
		//ServerConnectionHelperClass server = new ServerConnectionHelperClass(nodeobj);
		
		ServerConnectionHelperClass server = null;
		try {
			server = new ServerConnectionHelperClass(nodeId+1, nodeobj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread serv = new Thread(server);
		
		serv.start();
		
		
		VectorClockService.getInstance().init(nodeobj.numNodes, nodeobj.localInfor.nodeId);
		LamportLogicalClockService.getInstance().refresh();
		
		AlgoSuperClass algObj = new AlgoSuperClass(nodeobj, "lamport");
		
		//Create channels and keep it till the end
		new ClientConnectionHelperClass(nodeobj);
		
//		try {
////			if(nodeId == 0) {
////				Thread.sleep(11000);
////			}
////			else {
////				Thread.sleep(10000);
////			}
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		VectorClockService.getInstance().refresh();
		
		if(nodeId == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int i = 0;
		while(i<count) {
			
			System.out.println("For Run No:"+(i+1));
			
			PerformanceCalService.getInstance().setDir(System.getProperty("user.dir")+"/");
			PerformanceCalService.getInstance().init(nodeobj.numRequest,nodeobj.localInfor.nodeId,i+1);
			
			if(i!=0) {
				LamportLogicalClockService.getInstance().refresh();
				VectorClockService.getInstance().refresh();
				algObj = new AlgoSuperClass(nodeobj, "lamport");
			}
			
			algObj.run();
			PerformanceFileParser.parseFile(PerformanceCalService.getInstance().curDirectory+"performance_file_node_", nodeobj.numNodes,i+1);
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			String file = System.getProperty("user.dir")+"/TimeInterval";
			
			VerifyResult(file,nodeobj);
			sorted = megerSortK(timeArray,0,timeArray.length);
			
			if(testCorrect(sorted)) {
				System.out.println("\nResult Verified Successfully!!!");
				calSystemThroughput();
				System.out.println("------------------------Summary------------------------");
				
				System.out.println("Message Complexity:" + PerformanceFileParser.result[0]);
				System.out.println("Response Time:"+PerformanceFileParser.result[1]);
				System.out.println("System Throughput per request:" + String.valueOf(1000/(MeanExecTime+MeanSyncDelay)));
			}
			else {
				System.out.println("\nResult Verification Failed!!!");
			}
			
			i++;
		}
		
	}
	
	
	
}
