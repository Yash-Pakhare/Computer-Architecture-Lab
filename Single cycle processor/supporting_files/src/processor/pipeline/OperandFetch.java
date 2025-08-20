package processor.pipeline;

import processor.Processor;
import generic.Simulator;
import generic.Instruction.OperationType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public static String twoComplement(String binaryString) {
    	int index = binaryString.length() - 1;
		while (index >= 0 && binaryString.charAt(index) != '1') {
			index--;
		}
		if (index == -1) {
			return binaryString;
		}
		StringBuilder result = new StringBuilder(binaryString.length());
		for (int i = 0; i < index; i++) {
			result.append(binaryString.charAt(i) == '0' ? '1' : '0');
		}
		result.append(binaryString, index, binaryString.length());
		return result.toString();
  	}

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			String newBinaryInstruction = Integer.toBinaryString(newInstruction);
			newBinaryInstruction = String.format("%32s", newBinaryInstruction).replace(' ', '0');
			String binaryInstruction = IF_OF_Latch.getInstruction();
			String opCode = binaryInstruction.substring(0, 5);
			OperationType operation = OperationType.values()[Integer.parseInt(opCode, 2)];
			OF_EX_Latch.setInstruction(binaryInstruction);
			int immediate=0;
			int rs1=0;
			int rs2=0;
			int rd=0;
			int offset;
			switch(operation){
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor: 
				case slt: 
				case sll: 
				case srl: 
				case sra:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rs2 = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					rd = Integer.parseInt(binaryInstruction.substring(15, 20),2);
					OF_EX_Latch.setRs1(rs1);
					OF_EX_Latch.setRs2(rs2);
					OF_EX_Latch.setRd(rd);
					break;
				case addi:
				case subi:
				case muli:
				case divi:
				case andi:
				case ori:
				case xori:
				case slti:
				case slli:
				case srli:
				case srai:
				case load:
				case store:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					immediate = Integer.parseInt(binaryInstruction.substring(15, 32),2);
					OF_EX_Latch.setRs1(rs1);
					OF_EX_Latch.setRd(rd);
					OF_EX_Latch.setImm(immediate);
					break;
				case beq:
				case bgt:
				case blt:
				case bne:
					rs1 = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					rd = Integer.parseInt(binaryInstruction.substring(10, 15),2);
					if(binaryInstruction.substring(15, 16).equals("1")){
						offset = -1*Integer.parseInt(twoComplement(binaryInstruction.substring(15, 32)),2);
						OF_EX_Latch.setOffset(offset);
						OF_EX_Latch.setRs1(rs1);
						OF_EX_Latch.setRd(rd);
					}else{
						offset = Integer.parseInt(binaryInstruction.substring(15, 32),2);
						OF_EX_Latch.setOffset(offset);
						OF_EX_Latch.setRs1(rs1);
						OF_EX_Latch.setRd(rd);
					}
					break;
				case jmp:
					rd = Integer.parseInt(binaryInstruction.substring(5, 10),2);
					if(binaryInstruction.substring(10, 11).equals("1")){
						offset = -1*Integer.parseInt(twoComplement(binaryInstruction.substring(10, 32)),2);
					}else{
						offset = Integer.parseInt(twoComplement(binaryInstruction.substring(10, 32)),2);
					}
					OF_EX_Latch.setRd(rd);
					OF_EX_Latch.setOffset(offset);
					break;
				case end:
					Simulator.setSimulationComplete(true);
					break;
			}
			System.out.println("rs1,rs2,imm,rd,instruction"+rs1+" "+rs2+" "+immediate+" "+rd+" "+binaryInstruction);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
