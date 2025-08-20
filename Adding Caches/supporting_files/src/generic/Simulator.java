package generic;
import java.io.FileInputStream;
import java.io.IOException;
import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	static boolean debugMode = true;

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		eventQueue = new EventQueue();
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		try(FileInputStream objfile = new FileInputStream(assemblyProgramFile)){
			byte[] byteData = new byte[4];
            int bytesRead = objfile.read(byteData);
            int firstCodeAddress = ((byteData[0] & 0xFF) << 24) |
                	        ((byteData[1] & 0xFF) << 16) |
                    	    ((byteData[2] & 0xFF) << 8) |
                        	(byteData[3] & 0xFF);
			processor.getRegisterFile().setProgramCounter(firstCodeAddress);
			int i=0;
			while(objfile.read(byteData)!=-1){
            	int address = ((byteData[0] & 0xFF) << 24) |
                	        ((byteData[1] & 0xFF) << 16) |
                    	    ((byteData[2] & 0xFF) << 8) |
                        	(byteData[3] & 0xFF);
				processor.getMainMemory().setWord(i,address);
				i++;
			}
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles()+1);
			// Statistics.setNumberOfInstructions(Statistics.getNumberofInstruction() + 1);
			System.out.println("\n\n\n--------------ONE CYCLE completed-----------------\n\n\n");
		}
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}

	public static boolean isDebugMode() {
		return debugMode;
	}

	public static EventQueue getEventQueue()
	{
		return eventQueue;
	}
}
