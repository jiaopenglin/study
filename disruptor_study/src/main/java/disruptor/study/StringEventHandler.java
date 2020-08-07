package disruptor.study;

import com.lmax.disruptor.EventHandler;
/**
 * 事件的处理
 * @author jiao
 *
 */
public class StringEventHandler implements EventHandler<StringEvent>{

	@Override
	public void onEvent(StringEvent arg0, long arg1, boolean arg2) throws Exception {
	
		
		System.out.println("信息："+arg0.getEvent()+"*******sequence:"+arg1+"**********"+arg2);
		
	}

}
