package studio.coldstream.ubicraft.tellduslive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntermediateList {

	private TelldusLiveDevice[] device;
	
	public TelldusLiveDevice[] asArray() {
		return device;
	}
	
	public List<TelldusLiveDevice> asList() {
		if(device != null)
			return Arrays.asList(device);
		else
			return new ArrayList<TelldusLiveDevice>();
	}
}
