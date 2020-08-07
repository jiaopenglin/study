package disruptor.study1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

public class Consumer implements WorkHandler<Order>{

	private  String consumerId;
	
	public static AtomicInteger count=new AtomicInteger(0);
	
	private Random  random=new Random();

	public Consumer(String consumerId) {
		
		this.consumerId=consumerId;
		
	}

	@Override
	public void onEvent(Order event) throws Exception {
	
		count.incrementAndGet();
		System.out.println("消费者:"+consumerId+"****订单id:"+event.getId());
		
	}

	public int getCount() {
		return count.get();
	}


	
	
	
	
	
}
