package studio.coldstream.ubicraft.tellduslive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import studio.coldstream.ubicraft.domain.AbstractDevice;
import studio.coldstream.ubicraft.domain.Method;
import studio.coldstream.ubicraft.domain.Response;
import android.util.Log;

import se.akerfeldt.com.google.gson.Gson;

public class TelldusLiveDevice extends AbstractDevice {

	private int id;

	private int methods;

	private int state;

	private int client;

	private String clientName;

	private int online;

	private boolean methodsInit = false;

	public void initSupportedMethods() {
		if (!methodsInit) {
			if ((methods & Method.TELLSTICK_BELL.intValue()) == Method.TELLSTICK_BELL.intValue())
				supportedMethods.add(Method.TELLSTICK_BELL);
			if ((methods & Method.TELLSTICK_DIM.intValue()) == Method.TELLSTICK_DIM.intValue())
				supportedMethods.add(Method.TELLSTICK_DIM);
			if ((methods & Method.TELLSTICK_LEARN.intValue()) == Method.TELLSTICK_LEARN.intValue())
				supportedMethods.add(Method.TELLSTICK_LEARN);
			if ((methods & Method.TELLSTICK_TOGGLE.intValue()) == Method.TELLSTICK_TOGGLE.intValue())
				supportedMethods.add(Method.TELLSTICK_TOGGLE);
			if ((methods & Method.TELLSTICK_TURNOFF.intValue()) == Method.TELLSTICK_TURNOFF.intValue())
				supportedMethods.add(Method.TELLSTICK_TURNOFF);
			if ((methods & Method.TELLSTICK_TURNON.intValue()) == Method.TELLSTICK_TURNON.intValue())
				supportedMethods.add(Method.TELLSTICK_TURNON);
		}
		Log.v("remotestick", "Supported methods int = " + methods);
		Log.v("remotestick", "Supported Methods init'ed to " + supportedMethods);
	}

	@Override
	public Response dim(int level) {
		return invokeMethod(Method.TELLSTICK_DIM, level);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Integer getLastValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLearnable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPhysicalDevice() {
		return true;
	}

	@Override
	public Response learn() {
		return invokeMethod(Method.TELLSTICK_LEARN, 0);
	}

	@Override
	public Response turnOff() {
		return invokeMethod(Method.TELLSTICK_TURNOFF, null);
	}

	@Override
	public Response turnOn() {
		return invokeMethod(Method.TELLSTICK_TURNON, null);
	}
	
	@Override
	public Response toggle() {
		return invokeMethod(Method.TELLSTICK_TOGGLE, null);
	}

	private Response invokeMethod(Method method, Integer value) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://api.telldus.com/json/devices/command?id=" + id + "&method=" + method.intValue() + (value != null ? "&value=" + value : ""));
		Log.v("remotestick", "Invoking method " + method + " for " + id + " with value " + value);
		try {
			TelldusLiveAuthentication.getConsumer().sign(request);
			HttpResponse response = client.execute(request);
			Log.v("remotestick", "HTTP Response return " + response.getStatusLine());
			if(response.getStatusLine().getStatusCode() != 200)
				return Response.errorResponse(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());

			InputStream content = response.getEntity().getContent();
			char[] buffer = new char[1024];
			Writer writer = new StringWriter();
			Reader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
			Log.v("remotestick", writer.toString());
			Gson gson = new Gson();
			StatusResponse statusResponse = gson.fromJson(writer.toString(), StatusResponse.class);
			if(statusResponse.isSuceess())
				return Response.succededResponse();
			else if (statusResponse.isError())
				return Response.errorResponse(902, statusResponse.getError());
			else
				return Response.errorResponse(904, "Unknown response returned from Telldus Live!");
		} catch (OAuthMessageSignerException e) {
			Log.v("remotestick", "OAuthMessageSignerException " + e.getMessage());
			return Response.errorResponse(12, "OAuthMessageSignerException: " + e.getMessage(), e);
		} catch (OAuthExpectationFailedException e) {
			Log.v("remotestick", "OAuthExpectationFailedException " + e.getMessage());
			return Response.errorResponse(13, "OAuthExpectationFailedException: " + e.getMessage(), e);
		} catch (OAuthCommunicationException e) {
			Log.v("remotestick", "OAuthCommunicationException " + e.getMessage());
			return Response.errorResponse(14, "OAuthCommunicationException: " + e.getMessage(), e);
		} catch (ClientProtocolException e) {
			Log.v("remotestick", "ClientProtocolException " + e.getMessage());
			return Response.errorResponse(16, "ClientProtocolException: " + e.getMessage(), e);
		} catch (IOException e) {
			Log.v("remotestick", "IOException " + e.getMessage());
			return Response.errorResponse(17, "IOException: " + e.getMessage(), e);
		}

	}
	
	public boolean isOnline() {
		return online == 1;
	}

	public String getClientName() {
		return clientName;
	}

	public int getClient() {
		return client;
	}

	public int getState() {
		return state;
	}
}
