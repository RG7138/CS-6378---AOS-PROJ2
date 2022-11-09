TEAM: Akash Biswal (axb200166), Rahul Gauri(rxg200002), Pranay Hemant Sadavarte(phs200001)
Extract the attached .zip file to a folder of choice

All the relevant java packages and Main.java file should be in this folder

To Run:

1. Open a linux terminal inside the extracted directory
2. Run javac Main.java to compile the program.
3. Then run java Main.java <nodeid> <configfilename> to start the lamports' mutual exclusion demonstration part.
	- Lamport's algorithm is not fully implemented, for now all the socket connections are being created
	- We have tested the same locally

To Do:

1. Need to create the launcher to open simulataneous terminals as per #of nodes and cleanup bash script.
2. Testing mechanism for lamports' mutual exclusion
3. Ricart and Agarawla's mutual exclusion protocol
