package studio.coldstream.ubicraft.domain;

import java.util.List;
import java.util.Map;


public interface Controller {

	public Response refresh();
	
	public AbstractDevice getDevice(int i);
	
	public List<? extends AbstractDevice> getDevices();

	public Device createDevice(String name, String model, String protocol, Map<String, String> parameters);
	
	public boolean delete(Device device);
	
	public int getSupport();

	public int getId();

	public String getName();

	public boolean isConnected();
}
