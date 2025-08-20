package processor.pipeline;
import configuration.*;
import processor.*;
import generic.*;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	OF_EX_LatchType OF_EX_Latch; // new
	EX_MA_LatchType EX_MA_Latch; // new
	MA_RW_LatchType MA_RW_Latch; // new
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performIF()
	{
		int currentPC = 0;
		
		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.isIF_busy()){
				System.out.println("HI");
				return;
			}
			if(IF_EnableLatch.isIF_busy_MA()){
				return;
			}
			if(IF_EnableLatch.isIF_busy_EX()){
				return;
			}
			if(EX_IF_Latch.getIF_enable()){
				currentPC = EX_IF_Latch.get_BPC();
				if(IF_OF_Latch.isBranchHazard()){
					IF_OF_Latch.setBranchHazard(false);
					IF_OF_Latch.setInstruction(null);
					System.out.println("inst null");
					Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
				}else{
					containingProcessor.getRegisterFile().setProgramCounter(currentPC);
					EX_IF_Latch.setIF_enable(false);
					// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
					// Memory read latency
					Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), currentPC));
					System.out.println("IF busy");
					IF_EnableLatch.setIF_busy(true);
					IF_OF_Latch.setOF_enable(false);
					IF_EnableLatch.setExpc(true);
					// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

					// String newBinaryInstruction = Integer.toBinaryString(newInstruction);
					// newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
					// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
					// IF_OF_Latch.setInstruction(newBinaryInstruction);
					// System.out.println("inst "+ newInstruction);
				}
			}
			else{
				currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				// Memory read latency
				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), currentPC));
				IF_EnableLatch.setIF_busy(true);
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setExpc(true);
				System.out.println("IF busy");
				// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);

				// String newBinaryInstruction = Integer.toBinaryString(newInstruction);
				// newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
				// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				// IF_OF_Latch.setInstruction(newBinaryInstruction);
				// System.out.println("inst "+ newInstruction);
			}
			
			// IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);
		}
		System.out.println();
	}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.isOF_busy() || IF_OF_Latch.isOF_busy_MA() || IF_OF_Latch.isOF_busy_EX())
		{
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else if(EX_IF_Latch.isIF_enable()){
			System.out.println("hi");
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setOF_enable(false);
		}
		else
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			if(IF_EnableLatch.getExpc()){
				IF_EnableLatch.setExpc(false);
				IF_OF_Latch.setBranch(false);
				OF_EX_Latch.setBranch(false);
				EX_MA_Latch.setBranch(false);
				MA_RW_Latch.setBranch(false);
			}
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);

			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = event.getValue();
			// Memory read latency
			// Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), currentPC));
			// IF_EnableLatch.setIF_busy(true);
			// IF_OF_Latch.setOF_enable(false);
			System.out.println("pc "+ containingProcessor.getRegisterFile().getProgramCounter());
			String newBinaryInstruction = Integer.toBinaryString(newInstruction);
			newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			IF_OF_Latch.setInstruction(newBinaryInstruction);
			System.out.println("inst "+ newInstruction);

		}
	}

}
