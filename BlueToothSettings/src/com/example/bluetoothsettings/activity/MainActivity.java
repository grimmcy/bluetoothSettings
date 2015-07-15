package com.example.bluetoothsettings.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.bluetoothsettings.LocalBluetoothAdapter;
import com.example.bluetoothsettings.LocalBluetoothManager;
import com.example.bluetoothsettings.R;

public class MainActivity extends Activity {
	
	LocalBluetoothAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		LocalBluetoothManager manager = LocalBluetoothManager
				.getInstance(getApplicationContext());
		LocalBluetoothAdapter adapter = manager.getBluetoothAdapter();
		mAdapter = adapter;
		int state = adapter.getBluetoothState();
		Log.d("xxx", "bluetooth state is : " + state);
		
		
		
		//adapter.setBluetoothEnabled(false);
		//new Thread(test).start();

	}

	private Runnable test = new Runnable() {
		public void run() {
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d("xxx","after set bluetooth state is : "+mAdapter.getBluetoothState());
		};
	};
}
