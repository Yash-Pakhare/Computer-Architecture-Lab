package processor.pipeline;

import processor.Processor;
import generic.Simulator;
import generic.Statistics;
import generic.Instruction.OperationType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch; // added IF_EnableLatch
	EX_MA_LatchType EX_MA_Latch; // added EX_MA_Latch
	MA_RW_LatchType MA_RW_Latch; // added MA_RW_Latch

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

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, IF_EnableLatchType iF_EnableLatch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performOF()
	{
		
		if(IF_OF_Latch.isBranch())
			return;
		if(IF_OF_Latch.isOF_busy())
			return;
		// if(IF_OF_Latch.isOF_busy_MA())
		// 	return;
		if(IF_EnableLatch.isIF_busy()){
			OF_EX_Latch.setInstruction(null);
			return;
		}
		if(IF_OF_Latch.isOF_enable())
		{
			String binaryInstruction = IF_OF_Latch.getInstruction();
			System.out.println("OF "+IF_OF_Latch.getInstruction());
			if(binaryInstruction!=null)
			{
				String opCode = binaryInstruction.substring(0, 5);
				OperationType operation = OperationType.values()[Integer.parseInt(opCode, 2)];
				OperationType operation_ex=null;
				OperationType operation_ma=null;
				OperationType operation_rw=null;
				String ex_inst=null;
				if(EX_MA_Latch.getInstruction()!=null){
					ex_inst = EX_MA_Latch.getInstruction();
					String opCode_ex = ex_inst.substring(0,5);
					operation_ex = OperationType.values()[Integer.parseInt(opCode_ex,2)];
					if(Integer.parseInt(opCode_ex,2)>=23){
						ex_inst=null;
					}
				}
				String ma_inst=null;
				if(MA_RW_Latch.getInstruction()!=null){
					ma_inst = MA_RW_Latch.getInstruction();
					String opCode_ma = ma_inst.substring(0,5);
					operation_ma = OperationType.values()[Integer.parseInt(opCode_ma,2)];
					if(Integer.parseInt(opCode_ma,2)>=23){
						ma_inst=null;
					}
				}
				String rw_inst=null;
				if(IF_OF_Latch.getRWInstruction()!=null){
					rw_inst = IF_OF_Latch.getRWInstruction();
					String opCode_rw = rw_inst.substring(0,5);
					operation_rw = OperationType.values()[Integer.parseInt(opCode_rw,2)];
					if(Integer.parseInt(opCode_rw,2)>=23){
						rw_inst=null;
					}
				}
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
						if(ex_inst!=null){
							if(EX_MA_Latch.getRd()==rs1 || EX_MA_Latch.getRd()==rs2){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(ma_inst!=null){
							if(MA_RW_Latch.getRd()==rs1 || MA_RW_Latch.getRd()==rs2){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(rw_inst!=null){
							if(IF_OF_Latch.getRd()==rs1 || IF_OF_Latch.getRd()==rs2){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(operation_ex==OperationType.div || operation_ex==OperationType.divi){
							if(rs1==31 || rs2==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_ma==OperationType.div || operation_ma==OperationType.divi){
							if(rs1==31 || rs2==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_rw==OperationType.div || operation_rw==OperationType.divi){
							if(rs1==31 || rs2==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
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
						if(ex_inst!=null){
							if(EX_MA_Latch.getRd()==rs1 || (EX_MA_Latch.getRd()==rd && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(ma_inst!=null){
							if(MA_RW_Latch.getRd()==rs1 || (MA_RW_Latch.getRd()==rd && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(rw_inst!=null){
							if(IF_OF_Latch.getRd()==rs1 || (IF_OF_Latch.getRd()==rd && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(operation_ex==OperationType.div || operation_ex==OperationType.divi){
							if(rs1==31 || (rd==31 && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_ma==OperationType.div || operation_ma==OperationType.divi){
							if(rs1==31 || (rd==31 && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_rw==OperationType.div || operation_rw==OperationType.divi){
							if(rs1==31 || (rd==31 && operation==OperationType.store)){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
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
						if(ex_inst!=null){
							if(EX_MA_Latch.getRd()==rs1 || EX_MA_Latch.getRd()==rd){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(ma_inst!=null){
							if(MA_RW_Latch.getRd()==rs1 || MA_RW_Latch.getRd()==rd){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(rw_inst!=null){
							if(IF_OF_Latch.getRd()==rs1 || IF_OF_Latch.getRd()==rd){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}
						}
						if(operation_ex==OperationType.div || operation_ex==OperationType.divi){
							if(rs1==31 || rd==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_ma==OperationType.div || operation_ma==OperationType.divi){
							if(rs1==31 || rd==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
						if(operation_rw==OperationType.div || operation_rw==OperationType.divi){
							if(rs1==31 || rd==31){
								binaryInstruction=null;
								IF_EnableLatch.setIF_enable(false);
								OF_EX_Latch.setInstruction(binaryInstruction);
								OF_EX_Latch.setEX_enable(true);
								Statistics.setNumberOfStalls(Statistics.getNumberOfStalls()+1);
								return;
							}							
						}
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
						// IF_OF_Latch.setOF_enable(false);
						IF_EnableLatch.setIF_enable(false);
						OF_EX_Latch.setInstruction(binaryInstruction);
						OF_EX_Latch.setEX_enable(true);
						return;
						// Simulator.setSimulationComplete(true);
					default:
						break;
				}
				// System.out.println("rs1,rs2,imm,rd,instruction"+rs1+" "+rs2+" "+immediate+" "+rd+" "+binaryInstruction);
			}
			IF_EnableLatch.setIF_enable(true);
			// IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setInstruction(binaryInstruction);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
