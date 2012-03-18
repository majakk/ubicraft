package studio.coldstream.ubicraft;

import java.util.LinkedList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import studio.coldstream.ubicraft.domain.AbstractDevice;
import studio.coldstream.ubicraft.domain.Controller;
import studio.coldstream.ubicraft.tellduslive.TelldusLiveAuthentication;
import studio.coldstream.ubicraft.tellduslive.TelldusLiveController;
import studio.coldstream.ubicraft.domain.Response;
import studio.coldstream.ubicraft.NFCForegroundUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	static String TAG = "UBICRAFT_MAIN";
	
	public static final int ACTIVITY_CREATE = 4;
	
	protected static final int DEFAULT = 0x8000;
	protected static final int ADD = DEFAULT + 1;
	protected static final int LIST = DEFAULT + 2;
	protected static final int CRAFT = DEFAULT + 3;	
	protected static final int STATUS = DEFAULT + 4;
	
	protected static final String[] iconName = {"Add Device", "Device List", "Craft Device", "Status"};
	protected static final int[] iconImage = {R.drawable.add, R.drawable.list, R.drawable.craft, R.drawable.status};
	
	GridView grid_main;
	
	NFCForegroundUtil nfcForegroundUtil = null;

	private TextView info;
	
	List<String> listan;
	
	private Controller controller;
	
	//@SuppressWarnings("unused")
	//private List<Device> devices;
	
	private static OAuthConsumer consumer = null;
	
	private TelldusLiveController ctrlTelldusLive;
	
	boolean teststate;
	
	private SensorManager mSensorManager;

	private ShakeEventListener mSensorListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    	    
	    //This snippet authenticates the device! (or program or something else...)
	    /*Intent i = new Intent(this, TelldusLiveAuthentication.class);
		startActivity(i);
		TelldusLiveAuthentication.beginAuthorization(MainActivity.this);	  */ 
	    
	    //controller = ControllerFactory.getController(getApplicationContext());
	    	    
	    //List<? extends AbstractDevice> devices = controller.getDevices();
		consumer = TelldusLiveAuthentication.getConsumer(getApplicationContext());
		ctrlTelldusLive = new TelldusLiveController();
		
		ctrlTelldusLive.setConsumer(consumer);
		ctrlTelldusLive.refresh();
		
		List<? extends AbstractDevice> devices = ctrlTelldusLive.getDevices();
	    		
		/*lastRefresh = new RefreshController();
		lastRefresh.execute(this);*/
	    
		//ctrlTelldusLive.getDevice(1).turnOn();
		//devices.clear();
		//devices = ctrlTelldusLive.getDevices();
		
	    //devices = (List<Device>) ctrlTelldusLive.getDevices();
	    //final Response response = devices.get(1).turnOn();
	    //Log.v(TAG, response.getAdditionalInformation());
	    Log.v(TAG, Integer.toString(devices.size()));
	    
	    
	    //Log.v(TAG, Boolean.toString(pd.turnOn().isOk()));
	    
	   

	    nfcForegroundUtil = new NFCForegroundUtil(this);
	    listan = new LinkedList<String>();
	    
	    teststate = false;
	    
	    mSensorListener = new ShakeEventListener();
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener,
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_UI);


	    mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

	      public void onShake() {
	        Toast.makeText(getBaseContext(), "Shake!", Toast.LENGTH_SHORT).show();
	        
	        ctrlTelldusLive.setConsumer(consumer);
		    ctrlTelldusLive.refresh();
		    
		    List<? extends AbstractDevice> devices = ctrlTelldusLive.getDevices();
		    
		    //devices.get(0).setControl(Method.TELLSTICK_TOGGLE);
		    Response res;
		    if(!teststate){
		    	res = devices.get(0).turnOn();
		    	teststate = true;
		    }
		    else{
		    	res = devices.get(0).turnOff();
		    	teststate = false;
		    }
	      }
	    });

	    
	    showMenuView();

	}
	
	public class ImageViewHolder{
		ImageView image1;
		TextView text1;
	}
	
	public class ImageAdapter extends BaseAdapter{
        Context mContext;
        public ImageAdapter(Context c){
            mContext = c;
        }
        @Override
        public int getCount() {
            return ACTIVITY_CREATE;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder v;
            if(convertView==null){
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.icon, null);
                
                v = new ImageViewHolder();
                v.text1 = (TextView)convertView.findViewById(R.id.icon_text);
                v.text1.setText(iconName[position]);
                v.image1 = (ImageView)convertView.findViewById(R.id.icon_image);
                v.image1.setImageResource(iconImage[position]);
                convertView.setTag(v);
            }
            else
            {
            	v = (ImageViewHolder) convertView.getTag();
            }
            return convertView;
        }
		@Override
		public Object getItem(int position) {	
			return null;
		}
		@Override
		public long getItemId(int position) {	
			return 0;
		}
    }
	
	private void showMenuView(){
		setContentView(R.layout.main);
		
		info = (TextView)findViewById(R.id.title);
		
		grid_main = (GridView)findViewById(R.id.GridView1);
        grid_main.setAdapter(new ImageAdapter(this));
        grid_main.setOnItemClickListener(new OnItemClickListener() 
        {
            @SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, 
            View v, int position, long id) 
            {                
    			Message m1 = new Message();
    			switch(position){
    			case 0:    				
    	        	m1.what = ADD;                            
    	        	messageHandler.sendMessage(m1);
    				break;
    			case 1:    				
    	        	m1.what = LIST;                            
    	        	messageHandler.sendMessage(m1);
    				break;
    			case 2:    				
    	        	m1.what = CRAFT;                            
    	        	messageHandler.sendMessage(m1);
    				break;
    			case 3:    				
    	        	m1.what = STATUS;                            
    	        	messageHandler.sendMessage(m1);
    				break;    		
    			default:
    				;
    			}    			
                /*Toast.makeText(getBaseContext(), 
                        "pic" + (position + 1) + " selected", 
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        
	}
	
	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			//handle messages
			case ADD:
				//showHofView();	
				Intent mainIntent1 = new Intent(MainActivity.this, AddUbiNodeActivity.class);
			    MainActivity.this.startActivityForResult(mainIntent1, -1);
				break;
			case CRAFT:
				//showGlobalsRssView();
				Intent mainIntent2 = new Intent(MainActivity.this, CraftUbiDeviceActivity.class);
			    MainActivity.this.startActivityForResult(mainIntent2, -1);
				break;
			case LIST:
				//showNewsRssView();
				Intent mainIntent3 = new Intent(MainActivity.this, UbiDevicesListActivity.class);
			    MainActivity.this.startActivityForResult(mainIntent3, -1);
				break;
			case STATUS:
				//showItemsView();
				break;
			
			default:
				//break;
			}
		}
	};
	

	public void onPause() {
	    super.onPause();
	    nfcForegroundUtil.disableForeground();
	}   

	public void onResume() {
	    super.onResume();
	    nfcForegroundUtil.enableForeground();

	    if (!nfcForegroundUtil.getNfc().isEnabled())
	    {
	        Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	    }
	    
	    if(controller != null)
			setTitle("UbiCraft - " + controller.getName());
	    
	    mSensorManager.registerListener(mSensorListener,
	            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	            SensorManager.SENSOR_DELAY_UI);

	}
	
	@Override
	protected void onStop() {
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onStop();
	}

		
	public Controller getController() {
		return controller;
	}

	public void onNewIntent(Intent intent) {
	    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	    String resultat = "Debug Data:\n";
	    /*StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < tag.getId().length; i++){
	    	sb.append(new Integer(tag.getId()[i]) + " ");
	    }*/
	    listan.add(bytesToHex(tag.getId()));
	    for(int i = 0; i < listan.size(); i++){
	    	resultat += listan.get(i) + "\n";
	    }
	    info.setText(resultat);    
	    
	    ctrlTelldusLive.setConsumer(consumer);
	    ctrlTelldusLive.refresh();
	    
	    List<? extends AbstractDevice> devices = ctrlTelldusLive.getDevices();
	    
	    //devices.get(0).setControl(Method.TELLSTICK_TOGGLE);
	    Response res;
	    if(!teststate){
	    	res = devices.get(0).turnOn();
	    	teststate = true;
	    }
	    else{
	    	res = devices.get(0).turnOff();
	    	teststate = false;
	    }
	    
	    
	    //devices = (List<Device>) ctrlTelldusLive.getDevices();
	    Log.v(TAG, Integer.toString(devices.size()));
	    Log.v(TAG, Integer.toString(res.getResponseCode()));
	    Log.v(TAG, devices.get(0).toString());
	    
	    
	}
	
    /**
     *  Convenience method to convert a byte array to a hex string.
     *
     * @param  data  the byte[] to convert
     * @return String the converted byte[]
     */

    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]).toUpperCase());
            buf.append(" "); //the space between 
        }
        return (buf.toString());
    }

    /**
     *  method to convert a byte to a hex string.
     *
     * @param  data  the byte to convert
     * @return String the converted byte
     */
    public static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }
    
    /**
     *  Convenience method to convert an int to a hex char.
     *
     * @param  i  the int to convert
     * @return char the converted char
     */
    public static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('a' + (i - 10));
        }
    }
    
    
    
}