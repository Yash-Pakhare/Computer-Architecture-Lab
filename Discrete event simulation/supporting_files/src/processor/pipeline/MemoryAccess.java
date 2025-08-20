package processor.pipeline;

import processor.Processor;
import generic.Instruction.OperationType;
import generic.*;
import processor.*;
import configuration.Configuration;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;

	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, OF_EX_LatchType oF_EX_Latch, IF_OF_LatchType iF_OF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performMA()
	{
		
		if(EX_MA_Latch.isBranch())
			return;
		
		if(EX_MA_Latch.isMA_problem()){
			EX_MA_Latch.setInstruction(null);
			EX_MA_Latch.setMA_problem(false);
			System.out.println("NULL setting in MA_problem");
		}
		if(EX_MA_Latch.isMA_busy()){
			System.out.println("MA busy");
			return;
		}
		if(EX_MA_Latch.isMA_enable())
		{
			String binaryInstruction = EX_MA_Latch.getInstruction();
			System.out.println("MA "+EX_MA_Latch.instruction);
			if(binaryInstruction!=null)
			{
				int rd = EX_MA_Latch.getRd();
				int rs1 = EX_MA_Latch.getRs1();
				int rs2 = EX_MA_Latch.getRs2();
				int rem = EX_MA_Latch.getrem();
				int aluresult = EX_MA_Latch.getAluResult();
				int ldresult = 0;
				OperationType operation = OperationType.values()[Integer.parseInt(binaryInstruction.substring(0,5),2)];
				switch (operation) {
					case load:
						Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), aluresult));
						MA_RW_Latch.setRW_enable(false);
						EX_MA_Latch.setMA_busy(true);
						OF_EX_Latch.setEX_busy_MA(true);
						IF_OF_Latch.setOF_busy_MA(true);
						MA_RW_Latch.setRW_busy(true);
						System.out.println("LOAD DETECTED");
						// EX_MA_Latch.setInstruction(null);
						// ldresult = containingProcessor.getMainMemory().getWord(aluresult);
						// Memory read latency
						break;
					case store:
						Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), aluresult, containingProcessor.getRegisterFile().getValue(rs1)));
						MA_RW_Latch.setRW_enable(false);
						EX_MA_Latch.setMA_busy(true);
						OF_EX_Latch.setEX_busy_MA(true);
						IF_OF_Latch.setOF_busy_MA(true);
						MA_RW_Latch.setRW_busy(true);
						System.out.println("STORE DETECTED");
						// EX_MA_Latch.setInstruction(null);
						// containingProcessor.getMainMemory().setWord(aluresult, containingProcessor.getRegisterFile().getValue(rs1));
						// Memory read latency
						break;
					case end:
						MA_RW_Latch.setRs1(rs1);
						MA_RW_Latch.setRs2(rs2);
						MA_RW_Latch.setRd(rd);
						MA_RW_Latch.setAluResult(aluresult);
						MA_RW_Latch.set_ldresult(ldresult);
						MA_RW_Latch.setrem(rem);
						EX_MA_Latch.setMA_enable(false);
						MA_RW_Latch.setRW_enable(true);
						MA_RW_Latch.setInstruction(binaryInstruction);
						System.out.println("END DETECTED");
						break;
					default:
						MA_RW_Latch.setRs1(rs1);
						MA_RW_Latch.setRs2(rs2);
						MA_RW_Latch.setRd(rd);
						MA_RW_Latch.setAluResult(aluresult);
						MA_RW_Latch.set_ldresult(ldresult);
						MA_RW_Latch.setrem(rem);
						MA_RW_Latch.setRW_enable(true);
						MA_RW_Latch.setInstruction(binaryInstruction);
						System.out.println("DEFAULT DETECTED");
						break;
				}
				// MA_RW_Latch.setRs1(rs1);
				// MA_RW_Latch.setRs2(rs2);
				// MA_RW_Latch.setRd(rd);
				// MA_RW_Latch.setAluResult(aluresult);
				// MA_RW_Latch.set_ldresult(ldresult);
				// MA_RW_Latch.setrem(rem);
			}
			else{
				MA_RW_Latch.setInstruction(null);
				MA_RW_Latch.setRW_enable(true);
				System.out.println("NULL DETECTED");
			}
			// MA_RW_Latch.setInstruction(binaryInstruction);
			// EX_MA_Latch.setMA_enable(false);
			// MA_RW_Latch.setRW_enable(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			String binaryInstruction = EX_MA_Latch.getInstruction();
			int rd = EX_MA_Latch.getRd();
			int rs1 = EX_MA_Latch.getRs1();
			int rs2 = EX_MA_Latch.getRs2();
			int rem = EX_MA_Latch.getrem();
			int aluresult = EX_MA_Latch.getAluResult();
			int ldresult = event.getValue();
			MA_RW_Latch.setRs1(rs1);
			MA_RW_Latch.setRs2(rs2);
			MA_RW_Latch.setRd(rd);
			MA_RW_Latch.setAluResult(aluresult);
			MA_RW_Latch.set_ldresult(ldresult);
			MA_RW_Latch.setrem(rem);
			System.out.println("\nhi in ma eventhandler\n"+binaryInstruction);
			MA_RW_Latch.setInstruction(binaryInstruction);
			// System.out.println("inst "+ newInstruction);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_busy(false);
			OF_EX_Latch.setEX_busy_MA(false);
			IF_OF_Latch.setOF_busy_MA(false);
			MA_RW_Latch.setRW_busy(false);
			// EX_MA_Latch.setInstruction(null);
			EX_MA_Latch.setMA_problem(true);

	}

}
