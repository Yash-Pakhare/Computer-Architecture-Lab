package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	boolean EX_busy;
	boolean EX_busy_MA;
	boolean branch;
	boolean branch_problem;
	int rs1;
	int rs2;
	int rd;
	int immediate;
	int branchTarget;
	String instruction;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setInstruction(String Instruction){
		instruction = Instruction;
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

	public int getOffset(){
		return branchTarget;
	}

	public void setOffset(int brnchT){
		branchTarget = brnchT;
	}

	public boolean isBranch() {
		return branch;
	}

	public void setBranch(boolean b) {
		branch = b;
	}

	public boolean isBranchPorblem() {
		return branch_problem;
	}

	public void setBranchProblem(boolean b) {
		branch_problem = b;
	}	

	public boolean isEX_busy() {
		return EX_busy;
	}

	public void setEX_busy(boolean busy) {
		EX_busy = busy;
	}

	public boolean isEX_busy_MA() {
		return EX_busy_MA;
	}

	public void setEX_busy_MA(boolean busy) {
		EX_busy_MA = busy;
	}

}
