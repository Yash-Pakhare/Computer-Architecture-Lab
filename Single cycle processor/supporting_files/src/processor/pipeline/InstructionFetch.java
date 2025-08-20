package processor.pipeline;

import processor.Processor;

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
		if(IF_EnableLatch.isIF_enable())
		{
			if(EX_IF_Latch.getIF_enable()){
				currentPC = EX_IF_Latch.get_BPC();
				EX_IF_Latch.setIF_enable(false);
			}
			else{
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			}
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			String newBinaryInstruction = Integer.toBinaryString(newInstruction);
			newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
			IF_OF_Latch.setInstruction(newBinaryInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			System.out.println("pc "+ currentPC);
			System.out.println("inst "+ newBinaryInstruction);
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);

			// int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			// if(EX_IF_Latch.isIF_enable()){
			// 	currentPC = EX_IF_Latch.get_BPC();
			// 	EX_IF_Latch.setIF_enable(false);
			// }
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// IF_OF_Latch.setInstruction(newInstruction);
			// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);
		}
	}

}
