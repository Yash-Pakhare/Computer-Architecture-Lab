package processor.pipeline;

import processor.Processor;
import generic.Statistics;
import generic.Instruction.OperationType;
import configuration.Configuration;
import generic.*;
import processor.*;

public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch; // added IF_OF_Latch
	MA_RW_LatchType MA_RW_Latch; // added MA_RW_Latch
	IF_EnableLatchType IF_EnableLatch; // added IF_EnableLatch 

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch; // added IF_OF_Latch
		this.MA_RW_Latch = mA_RW_Latch; // added MA_RW_Latch
		this.IF_EnableLatch = iF_EnableLatch; // added IF_EnableLatch
	}
	
	public void performEX()
	{
		// System.out.println("Before EX "+IF_OF_Latch.instruction);
		if(OF_EX_Latch.isBranch())
			return;
		if(OF_EX_Latch.isEX_busy_MA())
			return;
		if(OF_EX_Latch.isEX_busy())
			return;
		if(OF_EX_Latch.isEX_enable()){
			System.out.println("EX "+OF_EX_Latch.instruction);
			if(EX_MA_Latch.getInstruction()!=null){
				if(
					(OperationType.values()[Integer.parseInt((EX_MA_Latch.getInstruction().substring(0,5)),2)] == OperationType.jmp ||
					OperationType.values()[Integer.parseInt((EX_MA_Latch.getInstruction().substring(0,5)),2)] == OperationType.bgt ||
					OperationType.values()[Integer.parseInt((EX_MA_Latch.getInstruction().substring(0,5)),2)] == OperationType.beq ||
					OperationType.values()[Integer.parseInt((EX_MA_Latch.getInstruction().substring(0,5)),2)] == OperationType.blt ||
					OperationType.values()[Integer.parseInt((EX_MA_Latch.getInstruction().substring(0,5)),2)] == OperationType.bne) &&
					(OF_EX_Latch.isBranchPorblem())
				) {
					OF_EX_Latch.setInstruction(null);
					OF_EX_Latch.setBranchProblem(false);
				}
			}
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
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case sub:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x - y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case mul:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x * y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.multiplier_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case div:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x / y;
						rem = x % y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.divider_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case and:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x & y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case or:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x | y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case xor:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x ^ y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case sll:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x << y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case srl:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x >> y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case sra:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = containingProcessor.getRegisterFile().getValue(rs2);
						result = x >>> y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case addi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x + y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case subi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x - y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case muli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x * y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.multiplier_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case divi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x / y;
						rem = x % y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.divider_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case andi:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x & y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case ori:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x | y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case xori:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x ^ y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case slli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x << y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case srli:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x >> y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case srai:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x >>> y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case load:
						x = containingProcessor.getRegisterFile().getValue(rs1);
						y = immediate;
						result = x + y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case store:
						x = containingProcessor.getRegisterFile().getValue(rd);
						y = immediate;
						result = x + y;
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
							IF_OF_Latch.setBranch(true);
							OF_EX_Latch.setBranch(true);
							EX_MA_Latch.setBranch(true);
							MA_RW_Latch.setBranch(true);
							OF_EX_Latch.setBranchProblem(true);
						}
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
							IF_OF_Latch.setBranch(true);
							OF_EX_Latch.setBranch(true);
							EX_MA_Latch.setBranch(true);
							MA_RW_Latch.setBranch(true);
							OF_EX_Latch.setBranchProblem(true);
						}
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
							IF_OF_Latch.setBranch(true);
							OF_EX_Latch.setBranch(true);
							EX_MA_Latch.setBranch(true);
							MA_RW_Latch.setBranch(true);
							OF_EX_Latch.setBranchProblem(true);
						}
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
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
							IF_OF_Latch.setBranch(true);
							OF_EX_Latch.setBranch(true);
							EX_MA_Latch.setBranch(true);
							MA_RW_Latch.setBranch(true);
							OF_EX_Latch.setBranchProblem(true);
						}
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case jmp:
						EX_IF_Latch.setIF_enable(true);
						int bpc = Offset + PC;
						EX_IF_Latch.set_BPC(bpc);
						IF_OF_Latch.setInstruction(null);
						IF_OF_Latch.setBranchHazard(true);
						Statistics.setNumberOfWrongBranch(Statistics.getNumberOfWrongBranch()+1);
						IF_OF_Latch.setBranch(true);
						OF_EX_Latch.setBranch(true);
						EX_MA_Latch.setBranch(true);
						MA_RW_Latch.setBranch(true);
						OF_EX_Latch.setBranchProblem(true);
						Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime()+Configuration.ALU_latency, this, this));
						OF_EX_Latch.setEX_busy(true);
						IF_OF_Latch.setOF_busy_EX(true);
						IF_EnableLatch.setIF_busy_EX(true);
						break;
					case end:
						OF_EX_Latch.setEX_enable(false);
						EX_MA_Latch.setMA_enable(true);	
						Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions()+1);
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
			// EX_MA_Latch.setMA_enable(true);
			// System.out.println("After EX "+IF_OF_Latch.instruction);
		}
		
	}

	@Override
	public void handleEvent(Event e) {
		if(EX_MA_Latch.isMA_busy()){
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else{
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_busy(false);
			IF_OF_Latch.setOF_busy_EX(false);
			IF_EnableLatch.setIF_busy_EX(false);
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions()+1);
		}
	}

}
