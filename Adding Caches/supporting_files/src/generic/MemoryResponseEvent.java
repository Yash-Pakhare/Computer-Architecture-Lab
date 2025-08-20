package generic;

public class MemoryResponseEvent extends Event {

	int value;
	int address;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int address) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.address = address;
 	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

}
