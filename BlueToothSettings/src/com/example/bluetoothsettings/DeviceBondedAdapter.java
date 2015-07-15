package com.example.bluetoothsettings;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceBondedAdapter extends BaseAdapter {

	List<BluetoothDevice> mData;

	public void setData(List<BluetoothDevice> data) {
		this.mData = data;
	}

	public List<BluetoothDevice> getData() {
		return this.mData;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int paramInt) {
		return mData == null ? null : mData.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		ViewHolder1 holder;
		if (paramView == null) {
			paramView = LayoutInflater.from(MainApplication.getInstance())
					.inflate(R.layout.blue_item_layout, paramViewGroup, false);
			holder = new ViewHolder1();
			holder.btDeviceIcon = (ImageView)paramView.findViewById(R.id.blue_icon);
			holder.btDeviceName = (TextView)paramView.findViewById(R.id.blue_name);
			paramView.setTag(holder);
		} else {
			holder = (ViewHolder1) paramView.getTag();
		}
		
		String deviceName = mData.get(paramInt).getName();
		holder.btDeviceName.setText(deviceName);
		
		return paramView;
	}

	private  class ViewHolder1 {
		ImageView btDeviceIcon;
		TextView btDeviceName;
	}

}
