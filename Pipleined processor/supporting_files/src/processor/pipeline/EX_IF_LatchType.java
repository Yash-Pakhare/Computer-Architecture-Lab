package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean IF_enable;
	int bpc;

	public EX_IF_LatchType()
	{
		IF_enable = false;
	}
	
	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean getIF_enable(){
		return IF_enable;
	}

	public void set_BPC(int PC){
		bpc = PC;
	}

	public int get_BPC(){
		return bpc;
	}

}
