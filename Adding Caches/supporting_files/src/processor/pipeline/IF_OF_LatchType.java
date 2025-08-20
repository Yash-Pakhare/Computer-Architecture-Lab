package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	boolean OF_busy;
	boolean OF_busy_MA;
	boolean OF_busy_EX;
	boolean branch;
	String instruction;
	boolean branch_hazard=false;
	String rw_inst;
	int ldResult = 0;
	int aluResult;
	int rs1;
	int rs2;
	int rd;
	int immediate;
	int remainder;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public boolean isBranchHazard(){
		return branch_hazard;
	}

	public void setBranchHazard(boolean isHazard){
		branch_hazard = isHazard;
	}

	public String getRWInstruction(){
		return rw_inst;
	}

	public void setRWInstruction(String inst){
		rw_inst = inst;
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

	public boolean isOF_busy() {
		return OF_busy;
	}

	public void setOF_busy(boolean oF_busy) {
		OF_busy = oF_busy;
	}

	public boolean isOF_busy_MA() {
		return OF_busy_MA;
	}

	public void setOF_busy_MA(boolean oF_busy) {
		OF_busy_MA = oF_busy;
	}

	public boolean isOF_busy_EX() {
		return OF_busy_EX;
	}

	public void setOF_busy_EX(boolean oF_busy) {
		OF_busy_EX = oF_busy;
	}

	public boolean isBranch() {
		return branch;
	}

	public void setBranch(boolean b) {
		branch = b;
	}

}
