package processor.pipeline;

import processor.Processor;
import generic.Statistics;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		int currentPC = 0;
		System.out.println("pc "+ containingProcessor.getRegisterFile().getProgramCounter());
		if(IF_EnableLatch.isIF_enable())
		{
			if(EX_IF_Latch.getIF_enable()){
				currentPC = EX_IF_Latch.get_BPC();
				if(IF_OF_Latch.isBranchHazard()){
					IF_OF_Latch.setBranchHazard(false);
					IF_OF_Latch.setInstruction(null);
					System.out.println("inst null");
					Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
				}else{
					EX_IF_Latch.setIF_enable(false);
					int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
					String newBinaryInstruction = Integer.toBinaryString(newInstruction);
					newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
					containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
					IF_OF_Latch.setInstruction(newBinaryInstruction);
					System.out.println("inst "+ newInstruction);
				}
			}
			else{
				currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				String newBinaryInstruction = Integer.toBinaryString(newInstruction);
				newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				IF_OF_Latch.setInstruction(newBinaryInstruction);
				System.out.println("inst "+ newInstruction);
			}
			
			// IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
		System.out.println();
	}

}
