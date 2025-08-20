package processor.pipeline;

import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;
import generic.Statistics;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch; // added IF_OF_Latch

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch; // added IF_OF_Latch
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isBranch())
			return;
		if(MA_RW_Latch.isRW_busy())
			return;
		if(MA_RW_Latch.isRW_enable())
		{
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
		String binaryInstruction = MA_RW_Latch.getInstruction();
		System.out.println("RW "+MA_RW_Latch.instruction);
			
			if(binaryInstruction!=null)
			{
				int rs1 = MA_RW_Latch.getRs1();
				int rs2 = MA_RW_Latch.getRs2();
				int rd = MA_RW_Latch.getRd();
				int aluresult = MA_RW_Latch.getAluResult();
				int ldresult = MA_RW_Latch.get_ldresult();
				int rem = MA_RW_Latch.getrem();
				OperationType operation = OperationType.values()[Integer.parseInt(binaryInstruction.substring(0,5),2)];
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
					case end:
						MA_RW_Latch.setRW_enable(false);
						Simulator.setSimulationComplete(true);
						break;
					default:
						break;
				}
				IF_OF_Latch.setRs1(rs1);
				IF_OF_Latch.setRs2(rs2);
				IF_OF_Latch.setRd(rd);
				IF_OF_Latch.setAluResult(aluresult);
				IF_OF_Latch.set_ldresult(ldresult);
				IF_OF_Latch.setrem(rem);
			}
			IF_OF_Latch.setRWInstruction(binaryInstruction);
			// MA_RW_Latch.setRW_enable(false);
			// IF_EnableLatch.setIF_enable(true);
			MA_RW_Latch.setInstruction(null);
		}
	}

}
