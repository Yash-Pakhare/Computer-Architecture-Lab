package processor.pipeline;

import processor.Processor;
import generic.Statistics;
import generic.Instruction.OperationType;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch; // added IF_OF_Latch

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch; // added IF_OF_Latch
	}
	
	public void performEX()
	{
		System.out.println("EX "+OF_EX_Latch.instruction);
		// System.out.println("Before EX "+IF_OF_Latch.instruction);
		if(OF_EX_Latch.isEX_enable()){
			String binaryInstruction = OF_EX_Latch.getInstruction();
			if(binaryInstruction!=null)
			{
				String opCode = binaryInstruction.substring(0,5);
				OperationType operation = OperationType.values()[Integer.parseInt(opCode,2)];
				int PC = containingProcessor.getRegisterFile().getProgramCounter();
				PC-=2; // Since this stage is 2 cycles behind current PC
				int rs1 = OF_EX_Latch.getRs1();
				int rs2 = OF_EX_Latch.getRs2();
				int rd = OF_EX_Latch.getRd();
				int immediate = OF_EX_Latch.getImm();
				int Offset = OF_EX_Latch.getOffset();
				int result = 0;
				int x=0;
				int y=0;
				int rem= 0;
				switch(operation){
					case add:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x + y;
						break;
					case sub:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x - y;
						break;
					case mul:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x * y;
						break;
					case div:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x / y;
						rem = x % y;
						break;
					case and:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x & y;
						break;
					case or:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x | y;
						break;
					case xor:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x ^ y;
						break;
					case slt:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						if(x>y){
							result = 0;
						}
						else{
							result = 1;
						}
						break;
					case sll:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x << y;
						break;
					case srl:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x >> y;
						break;
					case sra:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x >>> y;
						break;
					case addi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x + y;
						break;
					case subi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x - y;
						break;
					case muli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x * y;
						break;
					case divi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x / y;
						rem = x % y;
						break;
					case andi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x & y;
						break;
					case ori:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x | y;
						break;
					case xori:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x ^ y;
						break;
					case slti:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						if(x>y){
							result = 0;
						}
						else{
							result=1;
						}
						break;
					case slli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x << y;
						break;
					case srli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x >> y;
						break;
					case srai:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x >>> y;
						break;
					case load:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x + y;
						break;
					case store:
						x = containingProcessor.getRegisterFile().getValue(rd);
						y = immediate;
						result = x + y;
						break;
					case beq:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rd);
						if(x==y){
							EX_IF_Latch.setIF_enable(true);
							int bpc = Offset+PC;
							EX_IF_Latch.set_BPC(bpc);
							IF_OF_Latch.setInstruction(null);
							IF_OF_Latch.setBranchHazard(true);
							Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						}
						break;
					case bne:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rd);
						if(x!=y){
							EX_IF_Latch.setIF_enable(true);
							int bpc = Offset+PC;
							EX_IF_Latch.set_BPC(bpc);
							IF_OF_Latch.setInstruction(null);
							IF_OF_Latch.setBranchHazard(true);
							Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						}
						break;
					case blt:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rd);
						if(x<y){
							EX_IF_Latch.setIF_enable(true);
							int bpc = Offset+PC;
							EX_IF_Latch.set_BPC(bpc);
							IF_OF_Latch.setInstruction(null);
							IF_OF_Latch.setBranchHazard(true);
							Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						}
						break;
					case bgt:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rd);
						if(x>y){
							EX_IF_Latch.setIF_enable(true);
							int bpc = Offset+PC;
							EX_IF_Latch.set_BPC(bpc);
							IF_OF_Latch.setInstruction(null);
							IF_OF_Latch.setBranchHazard(true);
							Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						}
						break;
					case jmp:
						EX_IF_Latch.setIF_enable(true);
						int bpc = Offset + PC;
						EX_IF_Latch.set_BPC(bpc);
						IF_OF_Latch.setInstruction(null);
						IF_OF_Latch.setBranchHazard(true);
						Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						break;
					case end:
						OF_EX_Latch.setEX_enable(false);
						break;
					default:
						break;
				}
				EX_MA_Latch.setAluResult(result);
				EX_MA_Latch.setImm(immediate);
				EX_MA_Latch.setRs1(rs1);
				EX_MA_Latch.setRs2(rs2);
				EX_MA_Latch.setRd(rd);
				EX_MA_Latch.setrem(rem);
			}
			EX_MA_Latch.setInstruction(binaryInstruction);
			// OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			// System.out.println("After EX "+IF_OF_Latch.instruction);
		}
	}

}
