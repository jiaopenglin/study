package disruptor.study;

import com.lmax.disruptor.RingBuffer;


public class StringEventProduce {

	public   RingBuffer<StringEvent>  ring_buffer;
	
	
	public StringEventProduce(RingBuffer<StringEvent> ringbuffer) {
		super();
		ring_buffer=ringbuffer;
	}


	public  void  produce(String data) {
	//获取放数据的序号
		long next = ring_buffer.next();
		
		try {
			
			//取出对应序号的数据对象
			StringEvent stringEvent = ring_buffer.get(next);
			//设置数据
			stringEvent.setEvent(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//发布
			ring_buffer.publish(next);
			System.out.println("生产数据"+next);
			
		}
		
	}
	
	
}
