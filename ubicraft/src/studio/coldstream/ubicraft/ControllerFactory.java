package studio.coldstream.ubicraft;

//import java.net.URI;
//import java.net.URISyntaxException;

import studio.coldstream.ubicraft.domain.Controller;
//import studio.coldstream.ubicraft.remotestickserver.RemoteStickController;
import studio.coldstream.ubicraft.tellduslive.TelldusLiveAuthentication;
import studio.coldstream.ubicraft.tellduslive.TelldusLiveController;
import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;

public class ControllerFactory {

	private static TelldusLiveController ctrlTelldusLive;
	//private static RemoteStickController ctrlRemotestick;

	public static Controller getController(Context context) {
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		//Log.v("remotestick", "RemoteStick Service: " + prefs.getBoolean(RemoteStickPreferenceActivity.SERVICE_REMOTESTICK, false));
		//Log.v("remotestick", "Telldus Live! Service: " + prefs.getBoolean(RemoteStickPreferenceActivity.SERVICE_TELLDUSLIVE, false));
		//boolean remotestick = prefs.getBoolean(RemoteStickPreferenceActivity.SERVICE_REMOTESTICK, false);
		//boolean tellduslive = prefs.getBoolean(RemoteStickPreferenceActivity.SERVICE_TELLDUSLIVE, false);
		boolean tellduslive = true;

		/*if (remotestick) {
			if (ctrlRemotestick == null) {

				int connectTimeout = RemoteStickController.DEFAULT_CONNECT_TIMEOUT;
				int socketTimeout = RemoteStickController.DEFAULT_SOCKET_TIMEOUT;
				try {
					connectTimeout = Integer.parseInt(prefs.getString("connectTimeout", RemoteStickController.DEFAULT_CONNECT_TIMEOUT_STR));
				} catch (Exception e) {
				}
				try {
					socketTimeout = Integer.parseInt(prefs.getString("socketTimeout", RemoteStickController.DEFAULT_SOCKET_TIMEOUT_STR));
				} catch (Exception e) {
				}

				String host = prefs.getString("host", null);
				Integer port = null;
				try {
					port = Integer.parseInt(prefs.getString("port", null));
				} catch (NumberFormatException nfe) {
					port = null;
				}
				String username = prefs.getString("username", null);
				String password = prefs.getString("password", null);
				if (host == null || port == null || (password != null && username == null))
					return null;

				String basePath = prefs.getString("basePath", "");
				
				try {
					URI uri = new URI("http", null, host, port, basePath, null, null);
					ctrlRemotestick = new RemoteStickController(uri, username, password, connectTimeout, socketTimeout);
					prefs.registerOnSharedPreferenceChangeListener(ctrlRemotestick);
				} catch (URISyntaxException use) {
					Log.e("remotestick", "URISyntaxException when creating uri", use);
					return null;
				}
			}
			return ctrlRemotestick;*/
		if (tellduslive) {
			if (ctrlTelldusLive == null) {
				TelldusLiveAuthentication.getConsumer(context);
				ctrlTelldusLive = new TelldusLiveController();
			}
			return ctrlTelldusLive;
		} else
			return null;
	}
}
