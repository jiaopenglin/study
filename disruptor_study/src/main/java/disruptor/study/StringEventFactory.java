package disruptor.study;

import com.lmax.disruptor.EventFactory;

public class StringEventFactory implements  EventFactory<StringEvent>{

	@Override
	public StringEvent newInstance() {
		// TODO Auto-generated method stub
		return new StringEvent();
	}

}
