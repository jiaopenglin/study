package disruptor.test;

import static org.junit.Assert.*;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;
import org.w3c.dom.events.EventException;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import disruptor.study.StringEvent;
import disruptor.study.StringEventFactory;
import disruptor.study.StringEventHandler;
import disruptor.study.StringEventProduce;
import disruptor.study1.Consumer;
import disruptor.study1.Order;
import disruptor.study1.Produce;

public class DisruptorTest {

	
	@Test
	public void testName() throws Exception {
		
		//创建线程工厂来触发事件处理
		ThreadFactory  threadFactory=Executors.defaultThreadFactory();
		
		
		//创建事件工厂
		EventFactory<StringEvent> eventFactory=new StringEventFactory();
		
		//创建ringbuffer的大小，一定要是2的整数倍
		int ringBufferSize=16;
		
		//创建disruptor
		
		Disruptor<StringEvent> disruptor=new Disruptor<StringEvent>(eventFactory, 
																	ringBufferSize,
																	threadFactory, 
																	ProducerType.SINGLE, //生产者类型  单个或多个
																	new YieldingWaitStrategy());//拒接策略
		
		//设置事件处理类
		disruptor.handleEventsWith(new StringEventHandler());
		//启动消费者
		disruptor.start();
		
		//生产数据，先拿到ringBuffer
		RingBuffer<StringEvent> ringBuffer = disruptor.getRingBuffer();
		//创建消费者
		StringEventProduce produce=new StringEventProduce(ringBuffer);
		
		for (int i = 0; i < 10000; i++) {
			produce.produce(i+"");
		}
		
		//关闭
		disruptor.shutdown();
		
	}
	/**
	 * 使用EventTranslator发布事件
	 * @throws Exception
	 */
	@Test
	public void testName2() throws Exception {
		
		//创建线程工厂来触发事件处理
		ThreadFactory  threadFactory=Executors.defaultThreadFactory();
		
		
		//创建事件工厂
		EventFactory<StringEvent> eventFactory=new StringEventFactory();
		
		//创建ringbuffer的大小，一定要是2的整数倍
		int ringBufferSize=16;
		
		//创建disruptor
		
		Disruptor<StringEvent> disruptor=new Disruptor<StringEvent>(eventFactory, 
				ringBufferSize,
				threadFactory, 
				ProducerType.SINGLE, //生产者类型  单个或多个
				new YieldingWaitStrategy());//拒接策略
		
		//设置事件处理类
		disruptor.handleEventsWith(new StringEventHandler());
		//启动消费者
		disruptor.start();
		
		//生产数据，先拿到ringBuffer
		RingBuffer<StringEvent> ringBuffer = disruptor.getRingBuffer();
		//创建消费者
		StringEventProduce produce=new StringEventProduce(ringBuffer);
		
		for (int i = 0; i < 10000; i++) {
			produce.produce(i+"");
		}
		
		//关闭
		disruptor.shutdown();
		
	}
	/**
	 * 多生产者和多消费者
	 * @throws Exception
	 */
	@Test
	public void testName1() throws Exception {
		
		RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI, 
				new EventFactory<Order>() {

					@Override
					public Order newInstance() {
					
						return new Order();
					}
			
			
				}, 1024, new YieldingWaitStrategy());
		
		
		//创建屏障  
		SequenceBarrier newBarrier = ringBuffer.newBarrier();
		//创建多个消费者
		Consumer[] consumers = new Consumer[8];
		for (int i = 0; i < consumers.length; i++) {
			consumers[i] = new Consumer("consumer"+i);
		}
		
		WorkerPool<Order> workPool = new WorkerPool<Order>(ringBuffer, newBarrier, null, consumers);
		
		ringBuffer.addGatingSequences(workPool.getWorkerSequences());
		
		ExecutorService pool = Executors.newFixedThreadPool(8);
		
		workPool.start(pool);
		
		final CountDownLatch countdown = new CountDownLatch(1);
		
		for (int i = 0; i < 10; i++) {
			final Produce produce = new Produce(ringBuffer);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
				    try {
				    	countdown.await();
					} catch (Exception e) {
						// TODO: handle exception
					}
					for (int j = 0; j < 1000; j++) {
						produce.setData(UUID.randomUUID().toString());
					}
				}
			}).start();;
			
		}
		
		Thread.sleep(2000);
		System.out.println("线程创建完毕，开始生产数据——————————");
		countdown.countDown();
		
		Thread.sleep(5000);
		System.out.println("消费总数："+Consumer.count.get());
	}
	
}
