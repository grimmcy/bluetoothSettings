package com.example.bluetoothsettings;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlueAdapter extends BaseAdapter {

	List<CachedBluetoothDevice> mData;

	public void setData(List<CachedBluetoothDevice> data) {
		this.mData = data;
	}

	public List<CachedBluetoothDevice> getData() {
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
		ViewHolder holder;
		if (paramView == null) {
			paramView = LayoutInflater.from(MainApplication.getInstance())
					.inflate(R.layout.blue_item_layout, paramViewGroup, false);
			holder = new ViewHolder();
			holder.btDeviceIcon = (ImageView)paramView.findViewById(R.id.blue_icon);
			holder.btDeviceName = (TextView)paramView.findViewById(R.id.blue_name);
			paramView.setTag(holder);
		} else {
			holder = (ViewHolder) paramView.getTag();
		}
		
		String deviceName = mData.get(paramInt).getName();
		holder.btDeviceName.setText(deviceName);
		
		return paramView;
	}

	private static class ViewHolder {
		ImageView btDeviceIcon;
		TextView btDeviceName;
	}

}
