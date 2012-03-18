package studio.coldstream.ubicraft;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CraftUbiDeviceActivity extends Activity{
static String TAG = "UBICRAFT_CRAFT";
	
	public static final int CRAFT_CREATE = 9;
	
	GridView grid_craft;
	Button cb;
	TextView info;
	View activeView;
	
	List<String> listan;
	
	NFCForegroundUtil nfcForegroundUtil = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    nfcForegroundUtil = new NFCForegroundUtil(this);
	    listan = new LinkedList<String>();
	    
	    showCraftView();
	}
	
	public class ImageViewHolder{
		ImageView image1;
		//TextView text1;
	}
	
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
	}
	
	public class ImageAdapter extends BaseAdapter{
        Context mContext;
        private int[] iconImage = {0,0,0,0,0,0,0,0,0};
        public ImageAdapter(Context c){
            mContext = c;
        }
        
        
        public void setImage(int position, int id){
        	iconImage[position] = id;
        	return;
        }
        
        @Override
        public int getCount() {
            return CRAFT_CREATE;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder v;
            if(convertView==null){
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.craft_icon, null);
                
                v = new ImageViewHolder();
                //v.text1 = (TextView)convertView.findViewById(R.id.icon_text);
                //v.text1.setText(iconName[position]);
               
                v.image1 = (ImageView)convertView.findViewById(R.id.icon_image);
                //v.image1.setMinimumHeight(grid_craft.getWidth() / 3);
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
	
	private void showCraftView(){
		setContentView(R.layout.craft);
		
		grid_craft = (GridView)findViewById(R.id.craftGridView);
		grid_craft.setAdapter(new ImageAdapter(this));
		grid_craft.setSelector(new ColorDrawable(Color.RED));
		grid_craft.setOnItemClickListener(new OnItemClickListener() 		
        {        
			public void onItemClick(AdapterView<?> parent, 
            View v, int position, long id) 
            {                
				//ImageView iv = ((ImageViewHolder) v.getTag()).image1;
		        //iv.setImageResource(R.drawable.status);
				activeView = v;
    			  			
                Toast.makeText(getBaseContext(), 
                        "onClick -> Square " + (position + 1) + " selected", 
                        Toast.LENGTH_SHORT).show();
            }
        });
		
		grid_craft.setOnItemLongClickListener(new OnItemLongClickListener() 		
        {			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, 
		    View v, int position, long id) 
		    {                
				ImageView iv = ((ImageViewHolder) v.getTag()).image1;
		        iv.setImageResource(R.drawable.shakeevent);		    			
		    			  			
		        Toast.makeText(getBaseContext(), 
                        "onLongClick -> Square " + (position + 1) + " selected", 
                        Toast.LENGTH_SHORT).show();
		        return true;
		    }
        });
        
		cb = (Button) findViewById(R.id.craftButton);
		cb.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		info = (TextView) findViewById(R.id.craftInfo);
		
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
	    
	    if(activeView != null){
	    	ImageView iv = ((ImageViewHolder) activeView.getTag()).image1;
	    	iv.setImageResource(R.drawable.onoffswitch);
	    }
	    
	    /*ctrlTelldusLive.setConsumer(consumer);
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
	    }*/
	    
	    
	    //devices = (List<Device>) ctrlTelldusLive.getDevices();
	    /*Log.v(TAG, Integer.toString(devices.size()));
	    Log.v(TAG, Integer.toString(res.getResponseCode()));
	    Log.v(TAG, devices.get(0).toString());*/
	    
	    
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
            buf.append(" ");
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
