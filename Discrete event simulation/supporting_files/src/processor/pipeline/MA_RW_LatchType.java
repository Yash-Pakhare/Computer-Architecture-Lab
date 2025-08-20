package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	boolean RW_busy;
	boolean branch;
	String instruction;
	int ldResult = 0;
	int aluResult;
	int rs1;
	int rs2;
	int rd;
	int immediate;
	int remainder;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setInstruction(String Instruction){
		instruction = Instruction;
	}

	public void set_ldresult(int Ldresult){
		ldResult = Ldresult;
	}
	public int get_ldresult(){
		return ldResult;
	}

	public int getAluResult(){
		return aluResult;
	}

	public void setAluResult(int alu){
		aluResult = alu;
	}
	public int getRs1(){
		return rs1;
	}

	public void setRs1(int Rs1){
		rs1 = Rs1;
	}

	public int getRs2(){
		return rs2;
	}

	public void setRs2(int Rs2){
		rs2 = Rs2;
	}

	public int getRd(){
		return rd;
	}

	public void setRd(int Rd){
		rd = Rd;
	}

	public int getImm(){
		return immediate;
	}

	public void setImm(int imm){
		immediate = imm;
	}

	public int getrem(){
		return remainder;
	}

	public void setrem(int rem){
		remainder = rem;
	}

	public boolean isBranch() {
		return branch;
	}

	public void setBranch(boolean b) {
		branch = b;
	}

	public boolean isRW_busy() {
		return RW_busy;
	}

	public void setRW_busy(boolean b) {
		RW_busy = b;
	}

}
