package processor.memorysystem;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;

public class Cache implements Element {
    CacheLine[] cacheLines;
    int numberOfLines;
    int latency;
    Processor containingProcessor;
	String name;

    public Cache(Processor containingProcessor, String name, int associativity, int numberOfLines, int latency) {
        this.numberOfLines = numberOfLines/associativity;
        this.latency = latency;
		this.name = name;
        cacheLines = new CacheLine[numberOfLines];
        for (int i = 0; i < numberOfLines; i++) {
            cacheLines[i] = new CacheLine(associativity);
        }
        this.containingProcessor = containingProcessor;
    }

    public void incrementTSLA() {
        for (int i = 0; i < numberOfLines; i++) {
            cacheLines[i].incrementTSLA();
        }
    }

    public void cacheRead(int address, Element requestingElement) {
        int tag = address / numberOfLines;
        int index = address % numberOfLines;

        int way = cacheLines[index].getWay(tag);

        if (way != -1) {
            // Add response event to the event queue
			Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime() + latency, this, requestingElement, cacheLines[index].getData(way), address));

			if (Simulator.isDebugMode()) {
				System.out.println("[Debug] ("+name+") Read hit for address: " + address + " tag: " + tag + " index: " + index + " way: " + way + " data: " + cacheLines[index].getData(way));
			}
        } else {
            // Fetch data from memory
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + latency, this, containingProcessor.getMainMemory(), address));
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + latency, requestingElement, containingProcessor.getMainMemory(), address));

			if (Simulator.isDebugMode()) {
				System.out.println("[Debug] ("+name+") Read miss for address: " + address + " tag: " + tag + " index: " + index);
			}
        }
    }

    public void cacheWrite(int address, int data, Element requestingElement) {
        int tag = address / numberOfLines;
        int index = address % numberOfLines;

        int way = cacheLines[index].getWay(tag);

        if (way != -1) {
            cacheLines[index].setData(way, data);
			
			// Add response event to the event queue
			Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime() + latency, this, requestingElement, data, address));
            Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + latency, this, containingProcessor.getMainMemory(), address, data));

			if (Simulator.isDebugMode()) {
				System.out.println("[Debug] ("+name+") Write hit for address: " + address + " tag: " + tag + " index: " + index + " way: " + way + " data: " + data);
			}
        } else {
            // Fetch data from memory
            Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + latency, requestingElement, containingProcessor.getMainMemory(), address, data));
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + latency, this, containingProcessor.getMainMemory(), address));
            
			if (Simulator.isDebugMode()) {
				System.out.println("[Debug] ("+name+") Write miss for address: " + address + " tag: " + tag + " index: " + index);
			}
        }
    }

    public void cacheInsert(int address, int data) {
        int tag = address / numberOfLines;
        int index = address % numberOfLines;

        int way = cacheLines[index].getLRUWay();

        cacheLines[index].setData(way, data);
        cacheLines[index].setTag(way, tag);

		if (Simulator.isDebugMode()) {
			System.out.println("[Debug] ("+name+") Inserting data: " + data + " for address: " + address + " tag: " + tag + " index: " + index + " way: " + way);
		}
    }

    @Override
    public void handleEvent(Event e) {
        // Handle Cache Read
		if (e.getEventType() == EventType.MemoryRead)
		{
			// Get the memory request
			MemoryReadEvent event = (MemoryReadEvent) e;
			
			cacheRead(event.getAddressToReadFrom(), event.getRequestingElement());
		}

        // Handle Cache Write
        if (e.getEventType() == EventType.MemoryWrite)
        {
            // Get the memory request
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            
            cacheWrite(event.getAddressToWriteTo(), event.getValue(), event.getRequestingElement());
        }

        // Handle Memory Response
        if (e.getEventType() == EventType.MemoryResponse)
        {
            // Get the memory request
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            System.out.println("hi got response from cache with address: "+event.getAddress()+" value which i got:"+event.getValue());
            cacheInsert(event.getAddress(), event.getValue());
        }
    }
}
