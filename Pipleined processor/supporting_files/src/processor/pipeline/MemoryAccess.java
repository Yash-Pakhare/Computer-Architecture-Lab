package processor.pipeline;

import processor.Processor;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		System.out.println("MA "+EX_MA_Latch.instruction);
		if(EX_MA_Latch.isMA_enable())
		{
			String binaryInstruction = EX_MA_Latch.getInstruction();
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
						ldresult = containingProcessor.getMainMemory().getWord(aluresult);
						break;
					case store:
						containingProcessor.getMainMemory().setWord(aluresult, containingProcessor.getRegisterFile().getValue(rs1));
						break;
					case end:
						EX_MA_Latch.setMA_enable(false);
						break;
					default:
						break;
				}
				MA_RW_Latch.setRs1(rs1);
				MA_RW_Latch.setRs2(rs2);
				MA_RW_Latch.setRd(rd);
				MA_RW_Latch.setAluResult(aluresult);
				MA_RW_Latch.set_ldresult(ldresult);
				MA_RW_Latch.setrem(rem);
			}
			MA_RW_Latch.setInstruction(binaryInstruction);
			// EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
