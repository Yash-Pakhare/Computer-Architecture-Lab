package processor.pipeline;

import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			int rs1 = MA_RW_Latch.getRs1();
			int rs2 = MA_RW_Latch.getRs2();
			int rd = MA_RW_Latch.getRd();
			String instruction = MA_RW_Latch.getInstruction();
			int aluresult = MA_RW_Latch.getAluResult();
			int ldresult = MA_RW_Latch.get_ldresult();
			int rem = MA_RW_Latch.getrem();
			OperationType operation = OperationType.values()[Integer.parseInt(instruction.substring(0,5),2)];
			switch (operation) {
				case add:
				case sub:
				case mul:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					containingProcessor.getRegisterFile().setValue(rd, aluresult);
					break;
				
				case addi:
				case subi:
				case muli:
				case andi:
				case ori:
				case xori:
				case slti:
				case slli:
				case srli:
				case srai:
					containingProcessor.getRegisterFile().setValue(rd, aluresult);
					break;
				case load:
					containingProcessor.getRegisterFile().setValue(rd, ldresult);
					System.out.println("load");
					break;
				case div:
				case divi:
					containingProcessor.getRegisterFile().setValue(rd, aluresult);
					containingProcessor.getRegisterFile().setValue(31, rem);
					break;
				default:
					break;
			}
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
