package studio.coldstream.ubicraft.tellduslive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import studio.coldstream.ubicraft.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class TelldusLiveAuthentication extends Activity {

	private static final String CONSUMER_KEY = "3AWRECERU6ETHAPUJAXAVE6ATRAPENAC";

	private static final String CONSUMER_SECRET = "JUM8HUHUDREWRUSWAVAGUZ9B8BRACHU2";

	private static OAuthProvider provider = null;

	private static OAuthConsumer consumer = null;

	public void onResume() {
		super.onResume();

		final Uri uri = this.getIntent().getData();

		if (uri != null) {
			OAuthConsumer c = getConsumer(this);
			OAuthProvider p = getProvider();

			final String access_token = uri.getQueryParameter("oauth_token");
			Log.v("tellremote", "oauth-token: " + access_token);
			try {
				p.retrieveAccessToken(c, access_token);
			} catch (Exception e) {
				Log.v("tellremote", e.getMessage(), e);
				return;
			}
			Log.v("tellremote", "Provider: " + provider);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor ed = prefs.edit();
			ed.putString("tellduslive.token", c.getToken());
			ed.putString("tellduslive.tokensecret", c.getTokenSecret());
			ed.commit();
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("tellduslive.authorized", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// create an HTTP request to a protected resource
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet("http://api.telldus.com/json/devices/list");
//				HttpURLConnection request = (HttpURLConnection) url.openConnection();
//				request.setRequestMethod("GET");
				     
				// sign the request
				consumer.sign(request);
			
				// send the request
//				request.connect();
				HttpResponse response = client.execute(request);

				String resp;
				InputStream is = response.getEntity().getContent();
				if (is != null) {
		            Writer writer = new StringWriter();

		            char[] buffer = new char[1024];
		            try {
		                Reader reader = new BufferedReader(
		                        new InputStreamReader(is, "UTF-8"));
		                int n;
		                while ((n = reader.read(buffer)) != -1) {
		                    writer.write(buffer, 0, n);
		                }
		            } finally {
		                is.close();
		            }
		            resp = writer.toString();
		        } else {        
		            resp = "";
		        }
				Log.v("tellremote", "Response: " + resp);
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.startActivity(intent);
		}
	}

	public static OAuthConsumer getConsumer(Context context) {
		if (consumer == null) {
			consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

			SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			String token = mPrefs.getString("tellduslive.token", null);
			String tokensecret = mPrefs.getString("tellduslive.tokensecret", null);
			if (token != null && tokensecret != null) {
				Log.v("remotestick", "Setting token and token secret");
				consumer.setTokenWithSecret(token, tokensecret);
			}
		}
		return consumer;
	}
	
	public static OAuthConsumer getConsumer() {
		return consumer;
	}

	public static OAuthProvider getProvider() {
		if (provider == null) {
			provider = new CommonsHttpOAuthProvider("http://api.telldus.com/oauth/requestToken", "http://api.telldus.com/oauth/accessToken",
					"http://api.telldus.com/oauth/authorize");
		}
		return provider;
	}

	public static void beginAuthorization(Context context) {
		try {
			String url = getProvider().retrieveRequestToken(getConsumer(context), "remotestick-tellduslive:///");
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (Exception e) {
			Log.v("remotestick", e.getMessage(), e);
		}
	}

}
