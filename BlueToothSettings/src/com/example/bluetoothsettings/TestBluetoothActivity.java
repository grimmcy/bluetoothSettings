package com.example.bluetoothsettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TestBluetoothActivity extends Activity {

	private LocalBluetoothAdapter mAdapter;
	private LocalBluetoothManager mBluetoothManager;
	private BluetoothEventManager mEventManager;

	private LinearLayout mBondedLayout;
	private LinearLayout mScanedLayout;
	private ListView mBondedList;
	private ListView mScanedList;

	private List<CachedBluetoothDevice> mDevices = new ArrayList<CachedBluetoothDevice>();
	private Handler mHandler = new Handler();

	private BlueAdapter mScanedAdapter;
	private DeviceBondedAdapter mBondedAdapter;
	
	//test HandlerThread
	private Handler mTestHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.bluetooth_settings);
		initWidget();
		initData();
		
		setHanderThread();
		testhandlerThread();
	}

	private void initWidget() {
		mBondedLayout = (LinearLayout) findViewById(R.id.btsetting_bonded_layout);
		mBondedList = (ListView) findViewById(R.id.btsetting_bonded_list);
		mBondedList.setOnItemClickListener(mOnItemClickListener);

		mScanedLayout = (LinearLayout) findViewById(R.id.btsetting_scaned_layout);
		mScanedList = (ListView) findViewById(R.id.btsetting_scaned_list);
		mScanedList.setOnItemClickListener(mOnItemClickListener);
	}

	private void initData() {
		mDevices.clear();

		mBluetoothManager = LocalBluetoothManager.getInstance(getApplication());
		mAdapter = mBluetoothManager.getBluetoothAdapter();

		if (mAdapter.isDiscovering()) {
			mAdapter.cancelDiscovery();
		}

		Set<BluetoothDevice> bondedDevices = mAdapter.getBondedDevices();

		mEventManager = mBluetoothManager.getEventManager();
		mEventManager.registerCallback(mBluetoothCallback);

		mBondedLayout.setVisibility(View.GONE);

		mScanedAdapter = new BlueAdapter();
		mScanedList.setAdapter(mScanedAdapter);

		int state = mAdapter.getBluetoothState();
		if (state == BluetoothAdapter.STATE_ON) {
			mAdapter.startScanning(true);
		}

		initBondedDevice();
	}

	private void initBondedDevice() {
		mBondedAdapter = new DeviceBondedAdapter();
		mBondedList.setAdapter(mBondedAdapter);
		Set<BluetoothDevice> bonderData = mAdapter.getBondedDevices();
		if (bonderData == null || bonderData.size() == 0) {
			mBondedLayout.setVisibility(View.GONE);
		} else {
			mBondedLayout.setVisibility(View.VISIBLE);
			mBondedList.setFocusable(true);
			mBondedAdapter.setData(new ArrayList<BluetoothDevice>(bonderData));
			mBondedAdapter.notifyDataSetChanged();
		}
	}

	// resolve the bluetooth event
	private BluetoothCallback mBluetoothCallback = new BluetoothCallback() {

		@Override
		public void onScanningStateChanged(boolean started) {
			Log.d("xxx", "onScanningStateChanged " + started);
			if (started) {
				setProgressBarIndeterminateVisibility(true);
			} else {
				setProgressBarIndeterminateVisibility(false);
			}
		}

		@Override
		public void onDeviceDeleted(CachedBluetoothDevice cachedDevice) {
		}

		@Override
		public void onDeviceBondStateChanged(
				CachedBluetoothDevice cachedDevice, int bondState) {
			Log.d("xxx","onBondStateChanged : "+bondState);
			if (bondState == BluetoothDevice.BOND_BONDED) {
				List<BluetoothDevice> data = mBondedAdapter.getData();
				if(data == null)data = new ArrayList<BluetoothDevice>();
				data.add(cachedDevice.getDevice());
				mBondedAdapter.notifyDataSetChanged();
				mBondedLayout.setVisibility(View.VISIBLE);
			}else if(bondState == BluetoothDevice.BOND_NONE){
				List<BluetoothDevice> data = mBondedAdapter.getData();
				if(data == null)data = new ArrayList<BluetoothDevice>();
				Iterator<BluetoothDevice> iterator = data.iterator();
				while(iterator.hasNext()){
					BluetoothDevice device = iterator.next();
					if(device.getAddress().equalsIgnoreCase(cachedDevice.getDevice().getAddress())){
						iterator.remove();
					}
				}
				mBondedAdapter.setData(data);
				mBondedAdapter.notifyDataSetChanged();
				if(data == null || data.size() == 0){
					mBondedLayout.setVisibility(View.GONE);
				}
				
				mDevices.add(cachedDevice);
				mScanedAdapter.setData(mDevices);
				mScanedAdapter.notifyDataSetChanged();
				
			}
		}

		@Override
		public void onDeviceAdded(CachedBluetoothDevice cachedDevice) {
			if (cachedDevice != null) {
				if (cachedDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
					return;
				}
				mDevices.add(cachedDevice);
				mScanedAdapter.setData(mDevices);
				mScanedAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onBluetoothStateChanged(int bluetoothState) {
		}
	};

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			switch (paramAdapterView.getId()) {
			case R.id.btsetting_scaned_list:
				List<CachedBluetoothDevice> data = mScanedAdapter.getData();
				if (data != null) {
					CachedBluetoothDevice device = data.get(paramInt);
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						device.connect(true);
					}
				}
				break;
			case R.id.btsetting_bonded_list:
				List<BluetoothDevice> bondedDevices = mBondedAdapter.getData();
				BluetoothDevice device = bondedDevices.get(paramInt);
				ArrayList<CachedBluetoothDevice> copyDevices = (ArrayList<CachedBluetoothDevice>) mBluetoothManager
						.getCachedDeviceManager().getCachedDevicesCopy();
				for(CachedBluetoothDevice tempDevice:copyDevices){
					if(tempDevice.getDevice().getAddress().equalsIgnoreCase(device.getAddress())){
						tempDevice.unpair();
					}
				}
				
				
				break;
			}

		}
	};

	
	//test handlerthread
	private void setHanderThread(){
		HandlerThread handlerThread = new HandlerThread("testHandlerThread");
		handlerThread.start();
		
		mTestHandler = new Handler(handlerThread.getLooper()){
			@Override
			public void handleMessage(Message msg) {
				String time = (String) msg.obj;
				Log.d("xxx","receive the msg"+msg.what+"** send time is : "+time);
			}
		};
	}
	
	private void testhandlerThread(){
		for(int i = 0;i < 10 ;i++){
			Random random = new Random();
			int count = random.nextInt(10);
			MyThread thread = new MyThread(count,mTestHandler);
			thread.start();
			
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	private class MyThread extends Thread{
		private int mCount;
		private Handler mMyHandler;
		public MyThread(int index,Handler handler){
			mCount = index;
			mMyHandler = handler;
		}
		
		@Override
		public void run() {	
			if(mMyHandler != null){
				Message msg = new Message();
				msg.what = mCount;
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				int second = cal.get(Calendar.SECOND);
				int millionSecond = cal.get(Calendar.MILLISECOND);
				
				String time = String.format("%1$d:%2$d:%3$d:%4$d", hour,minute,second,millionSecond);
				
				msg.obj = time;
				mMyHandler.sendMessage(msg);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	

}
