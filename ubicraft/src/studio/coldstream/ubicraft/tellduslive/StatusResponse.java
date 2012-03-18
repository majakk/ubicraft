package studio.coldstream.ubicraft.tellduslive;

public class StatusResponse {

	private String status;
	
	private String error;
	
	public String getStatus() {
		return status;
	}
	
	public String getError() {
		return error;
	}
	
	public boolean isSuceess() {
		return "success".equals(status);
	}
	
	public boolean isError() {
		return error != null;
	}
}
