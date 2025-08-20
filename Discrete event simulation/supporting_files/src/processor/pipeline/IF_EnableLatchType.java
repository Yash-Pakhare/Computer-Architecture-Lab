package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean IF_busy;
	boolean IF_busy_MA;
	boolean IF_busy_EX;
	boolean ex_pc;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean isIF_busy() {
		return IF_busy;
	}

	public void setIF_busy(boolean iF_busy) {
		IF_busy  = iF_busy;
	}

	public boolean isIF_busy_MA() {
		return IF_busy_MA;
	}

	public void setIF_busy_MA(boolean iF_busy) {
		IF_busy_MA  = iF_busy;
	}

	public boolean isIF_busy_EX() {
		return IF_busy_EX;
	}

	public void setIF_busy_EX(boolean iF_busy) {
		IF_busy_EX  = iF_busy;
	}

	public boolean getExpc() {
		return ex_pc;
	}

	public void setExpc(boolean expc) {
		ex_pc = expc;
	}

}
