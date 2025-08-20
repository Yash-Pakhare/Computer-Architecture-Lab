package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions=0;
	static int numberOfCycles=0;
	static int numberOfStalls=0;
	static int numberOfWrongBranch=0;
	static float IPC=0;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			IPC = (float)numberOfInstructions / (float)numberOfCycles;
			writer.println("Instructions per Cycle (IPC) = " + IPC);
			// writer.println("Number of times OF stage is stalled = " + numberOfStalls);
			// writer.println("Number of times instruction on wrong branch path entered = " + numberOfWrongBranch);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfStalls(int numberOfStalls) {
		Statistics.numberOfStalls = numberOfStalls;
	}

	public static void setNumberOfWrongBranch(int numberOfWrongBranch) {
		Statistics.numberOfWrongBranch = numberOfWrongBranch;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void setNumberOfInstructions(int numberOfInst) {
		Statistics.numberOfInstructions = numberOfInst;
	}

	public static int getNumberOfStalls() {
		return numberOfStalls;
	}

	public static int getNumberOfWrongBranch() {
		return numberOfWrongBranch;
	}

	public static int getNumberOfCycles() {
		return numberOfCycles;
	}

	public static int getNumberOfInstructions() {
		return numberOfInstructions;
	}
}
