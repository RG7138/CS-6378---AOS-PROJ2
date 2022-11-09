
public class PerformanceEvaluaterClass {

	public static void main(String[] args) {
		
		int numNodes = 0;
		
		if(args.length > 0) {
		
		numNodes = Integer.parseInt(args[0]);
		String curDirecotry = System.getProperty("user.dir")+"/";
		PerformanceFileParser.parseFile(curDirecotry+"performance_file_node_", numNodes,1);
		System.out.println("Evaluation Complete");
		}

	}

}
