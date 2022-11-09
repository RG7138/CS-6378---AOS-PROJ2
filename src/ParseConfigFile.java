import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
public class ParseConfigFile {
	
	public static ConfigurationObj parseFile(String name,int localNodeId) {
		
		int Counter = 0;
		int nextConfigInfoPart = 0;
		
		String filepath = System.getProperty("user.dir") + "/" + name;
		
		String line = null;
		
		ConfigurationObj mapFile = new ConfigurationObj();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			
			while((line = br.readLine()) != null) {
				
				// Ignore comments that is anything after #
				if(line.length() == 0 || line.startsWith("#"))
					continue;
				
				String[] ParsedValues;
				
				if(line.contains("#")){
					//spliting line based on # (sharp symbol)
					String[] CommentLine = line.split("#.*$"); 
					
					//Ignore text after # symbol and splitting on space deliminator
					ParsedValues = CommentLine[0].split("\\s+");
				}
				else {
					ParsedValues = line.split("\\s+");
				}
				
				//Initiate configurations
				if((nextConfigInfoPart == 0) && (ParsedValues.length == 4)){
					mapFile.numNodes = Integer.parseInt(ParsedValues[0]);
					mapFile.Delay = Integer.parseInt(ParsedValues[1]);
					mapFile.CSexecutionTime = Integer.parseInt(ParsedValues[2]);
					mapFile.numRequest = Integer.parseInt(ParsedValues[3]);
					nextConfigInfoPart++;
				}
				else if(nextConfigInfoPart == 1 && Counter<mapFile.numNodes) {
					NodeObj node  = new NodeObj();
					
					node.nodeId = Integer.parseInt(ParsedValues[0]);
					
					if(ParsedValues[1].contains("utdallas.edu") || ParsedValues[1].contains("127.0.0.1")) {
						node.hostName = ParsedValues[1];
					}
					else {
						node.hostName = ParsedValues[1]+".utdallas.edu";
					}
					
					node.port = Integer.parseInt(ParsedValues[2]);
					
					if(node.nodeId != localNodeId) {
						mapFile.neighbors.put(node.nodeId,node);
					}
					else {
						mapFile.localInfor = node;
					}
					Counter++;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return mapFile;
	}
	
}
