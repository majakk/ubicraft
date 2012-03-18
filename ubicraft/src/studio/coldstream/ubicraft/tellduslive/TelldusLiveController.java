package studio.coldstream.ubicraft.tellduslive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import studio.coldstream.ubicraft.domain.AbstractDevice;
import studio.coldstream.ubicraft.domain.Controller;
import studio.coldstream.ubicraft.domain.Device;
import studio.coldstream.ubicraft.domain.Method;
import studio.coldstream.ubicraft.domain.Response;
import android.util.Log;

import se.akerfeldt.com.google.gson.Gson;
import se.akerfeldt.com.google.gson.JsonParseException;

public class TelldusLiveController implements Controller {

	private static final int SUPPORT = 0;
	private static final int ID = 2;
	private static final String NAME = "Telldus Live!";
	private static final int SUPPORTED_METHODS = Method.TELLSTICK_DIM.intValue() | Method.TELLSTICK_LEARN.intValue() | Method.TELLSTICK_TURNOFF.intValue()
			| Method.TELLSTICK_TURNON.intValue() | Method.TELLSTICK_TOGGLE.intValue();

	private List<TelldusLiveDevice> devices;
	private boolean connected = false;
	
	private static OAuthConsumer consumer = null;
	
	public void setConsumer(OAuthConsumer con){
		consumer = con;
	}

	public TelldusLiveController() {
		devices = new ArrayList<TelldusLiveDevice>();
	}

	public int getSupport() {
		return SUPPORT;
	}

	public Response refresh() {
		connected = false;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://api.telldus.com/json/devices/list?supportedMethods=" + SUPPORTED_METHODS);
		// HttpURLConnection request = (HttpURLConnection) url.openConnection();
		// request.setRequestMethod("GET");

		//OAuthConsumer consumer = TelldusLiveAuthentication.getConsumer();
		//OAuthConsumer consumer = TelldusLiveAuthentication.getConsumer();

		IntermediateList receivedDevices = null;

		try {
			consumer.sign(request);

			// send the request
			// request.connect();
			HttpResponse response = client.execute(request);

			InputStream is = response.getEntity().getContent();
			if (is != null) {
				Writer writer = new StringWriter();

				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
					Log.v("remotestick", "Result: " + writer.toString());
					Gson gson = new Gson();

					/* Check for errors */
					StatusResponse statusResponse = gson.fromJson(writer.toString(), StatusResponse.class);
					if (statusResponse.isError())
						return Response.errorResponse(903, statusResponse.getError());

					/* Else go ahead and read devices */
					receivedDevices = gson.fromJson(writer.toString(), IntermediateList.class);
					if (receivedDevices != null) {
						connected = true;
						for (TelldusLiveDevice device : receivedDevices.asArray()) {
							device.initSupportedMethods();
						}
					} else
						return Response.errorResponse(904, "Unknown response returned from Telldus Live!");

				} catch (UnsupportedEncodingException e) {
					Log.v("remotestick", "UnsupportedEncodingException " + e.getMessage());
					return Response.errorResponse(9, "UnsupportedEncodingException: " + e.getMessage(), e);
				} catch (IOException e) {
					Log.v("remotestick", "IOException " + e.getMessage());
					return Response.errorResponse(7, "IOException: " + e.getMessage(), e);
				} catch (JsonParseException e) {
					Log.v("remotestick", "JsonParseException " + e.getMessage());
					return Response.errorResponse(10, "JsonParseException: " + e.getMessage(), e);
				} finally {
					is.close();
				}
			} else {
				Log.v("remotestick", "No InputStream when reading content from TelldusLive");
				return Response.errorResponse(11, "No InputStream when reading content from TelldusLive");
			}
		} catch (OAuthMessageSignerException e) {
			Log.v("remotestick", "OAuthMessageSignerException " + e.getMessage());
			return Response.errorResponse(12, "OAuthMessageSignerException: " + e.getMessage(), e);
		} catch (OAuthExpectationFailedException e) {
			Log.v("remotestick", "OAuthExpectationFailedException " + e.getMessage());
			return Response.errorResponse(13, "OAuthExpectationFailedException: " + e.getMessage(), e);
		} catch (OAuthCommunicationException e) {
			Log.v("remotestick", "OAuthCommunicationException " + e.getMessage());
			return Response.errorResponse(14, "OAuthCommunicationException: " + e.getMessage(), e);
		} catch (IllegalStateException e) {
			Log.v("remotestick", "IllegalStateException " + e.getMessage());
			return Response.errorResponse(15, "IllegalStateException: " + e.getMessage(), e);
		} catch (IOException e) {
			Log.v("remotestick", "IOException " + e.getMessage());
			return Response.errorResponse(7, "IOException: " + e.getMessage(), e);
		}

		devices.clear();
		devices.addAll(receivedDevices.asList());
		return Response.succededResponse();
	}

	@Override
	public AbstractDevice getDevice(int i) {
		for (TelldusLiveDevice device : devices) {
			Log.v("remotestick", "looping for device " + i + ". Currently at " + device.getId());
			if (device.getId() == i)
				return device;
		}
		return null;
	}

	@Override
	public List<? extends AbstractDevice> getDevices() {
		return devices;
	}

	@Override
	public Device createDevice(String name, String model, String protocol, Map<String, String> parameters) {
		/* Not supported */
		return null;
	}

	@Override
	public boolean delete(Device device) {
		/* Not supported */
		return false;
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}
}
