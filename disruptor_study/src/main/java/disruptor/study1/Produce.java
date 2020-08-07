package disruptor.study1;

import com.lmax.disruptor.RingBuffer;

public class Produce {

	
	private RingBuffer<Order> ringBuffer;

	public Produce(RingBuffer<Order> ringBuffer) {
	
		this.ringBuffer = ringBuffer;
	}
	
	public void setData(String id) {
		
		long next = ringBuffer.next();
		try {
			Order order = ringBuffer.get(next);
			order.setId(id);
		} finally {
			ringBuffer.publish(next);
		}
		
	}
}
