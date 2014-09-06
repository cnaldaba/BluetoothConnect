package com.example.bluetoothconnect;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
 // Called when activity is first created
	private BluetoothAdapter btAdapter;
	
	public TextView statusUpdate;
	public Button connect;
	public Button disconnect;
	public ImageView logo;
	
BroadcastReceiver bluetoothState = new BroadcastReceiver(){
	@Override
    public void onReceive(Context context, Intent intent){
    	String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
    	String stateExtra = BluetoothAdapter.EXTRA_STATE;
    	int state = intent.getIntExtra(stateExtra, -1);
    	int previousState = intent.getIntExtra(prevStateExtra, -1);
    	String toastText ="";
    	switch(state){
    		case(BluetoothAdapter.STATE_TURNING_ON):
    		{
    			toastText = "Bluetooth is turning on";
    			Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
    			break;
    		}
    		case(BluetoothAdapter.STATE_ON):
    		{
    			toastText = "Bluetooth on";
    			Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
    			setupUI();
    			break;
    		}
    		case(BluetoothAdapter.STATE_TURNING_OFF):
    		{
    			toastText = "Bluetooth is turning off";
    			Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
    			break;
    		}
    		case(BluetoothAdapter.STATE_OFF):
    		{
    			toastText = "Bluetooth is off";
    			Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
    			setupUI();
    			break;
    		}
    	}
    }
	
	
};
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    public void setupUI(){
    //get references
    final TextView statusUpdate =(TextView) findViewById(R.id.result);
    final Button connect = (Button) findViewById(R.id.connectBtn);
    final Button disconnect = (Button) findViewById(R.id.disconnectBtn);
    final ImageView logo = (ImageView)findViewById(R.id.logo);
    
    //set display view
    
    disconnect.setVisibility(View.GONE);
    logo.setVisibility(View.GONE);
    btAdapter = BluetoothAdapter.getDefaultAdapter();
	if(btAdapter.isEnabled()){ //Check if BT is on
		
		String address = btAdapter.getAddress(); //get hardware address
		String name = btAdapter.getName(); // get name of device
		String statusText = name + ":" + address; 
		statusUpdate.setText(statusText);
		  disconnect.setVisibility(View.VISIBLE);
		    logo.setVisibility(View.VISIBLE);
			connect.setVisibility(View.GONE);
	}
	else {
		statusUpdate.setText("Bluetooth is not on");
		connect.setVisibility(View.VISIBLE);
		
	}
    connect.setOnClickListener(new OnClickListener(){
    	@Override
    	public void onClick(View v){
    	//Turn on Bluetooth
    		
    		String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED; //broadcast action
    		String actionRequestEnable = BluetoothAdapter.ACTION_REQUEST_ENABLE; //pop-up dialog
    		IntentFilter filter = new IntentFilter(actionStateChanged); 
    		registerReceiver(bluetoothState, filter);  //Recieve info back to know if bluetooth is on
    		startActivityForResult(new Intent(actionRequestEnable), 0);
    		
    	}
    });
    
    disconnect.setOnClickListener(new OnClickListener(){
    	@Override 
    	public void onClick(View v){
    		btAdapter.disable();
    		disconnect.setVisibility(View.GONE);
  		    logo.setVisibility(View.GONE);
  			connect.setVisibility(View.VISIBLE);
  			statusUpdate.setText("Bluetooth Off");
    		
    	}
    });
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
