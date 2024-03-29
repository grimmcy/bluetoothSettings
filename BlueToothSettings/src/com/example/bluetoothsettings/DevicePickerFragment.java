/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 

package com.example.bluetoothsettings;

import static android.os.UserManager.DISALLOW_CONFIG_BLUETOOTH;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDevicePicker;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.bluetoothsettings.R;

*//**
 * BluetoothSettings is the Settings screen for Bluetooth configuration and
 * connection management.
 *//*
@SuppressLint("NewApi")
public final class DevicePickerFragment extends DeviceListPreferenceFragment {
    private static final int MENU_ID_REFRESH = Menu.FIRST;

    public DevicePickerFragment() {
        super(null  Not tied to any user restrictions. );
    }

    private boolean mNeedAuth;
    private String mLaunchPackage;
    private String mLaunchClass;
    private boolean mStartScanOnResume;

    @Override
    void addPreferencesForActivity() {
        addPreferencesFromResource(R.xml.device_picker);

        Intent intent = getActivity().getIntent();
        mNeedAuth = intent.getBooleanExtra(BluetoothDevicePicker.EXTRA_NEED_AUTH, false);
        setFilter(intent.getIntExtra(BluetoothDevicePicker.EXTRA_FILTER_TYPE,
                BluetoothDevicePicker.FILTER_TYPE_ALL));
        mLaunchPackage = intent.getStringExtra(BluetoothDevicePicker.EXTRA_LAUNCH_PACKAGE);
        mLaunchClass = intent.getStringExtra(BluetoothDevicePicker.EXTRA_LAUNCH_CLASS);
    }

    @Override
    void initDevicePreference(BluetoothDevicePreference preference) {
        preference.setWidgetLayoutResource(R.layout.preference_empty_list);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_ID_REFRESH, 0, R.string.bluetooth_search_for_devices)
                .setEnabled(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_REFRESH:
                mLocalAdapter.startScanning(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.device_picker));
        UserManager um = (UserManager) MainApplication.getInstance().getSystemService(Context.USER_SERVICE);
        mStartScanOnResume = !um.hasUserRestriction(DISALLOW_CONFIG_BLUETOOTH)
                && (savedInstanceState == null);  // don't start scan after rotation
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        addCachedDevices();
        if (mStartScanOnResume) {
            mLocalAdapter.startScanning(true);
            mStartScanOnResume = false;
        }
    }

    @Override
    void onDevicePreferenceClick(BluetoothDevicePreference btPreference) {
        mLocalAdapter.stopScanning();
        LocalBluetoothPreferences.persistSelectedDeviceInPicker(
                getActivity(), mSelectedDevice.getAddress());
        if ((btPreference.getCachedDevice().getBondState() ==
                BluetoothDevice.BOND_BONDED) || !mNeedAuth) {
            sendDevicePickedIntent(mSelectedDevice);
//            finish();
            getActivity().onBackPressed();
        } else {
            super.onDevicePreferenceClick(btPreference);
        }
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedDevice,
            int bondState) {
        if (bondState == BluetoothDevice.BOND_BONDED) {
            BluetoothDevice device = cachedDevice.getDevice();
            if (device.equals(mSelectedDevice)) {
                sendDevicePickedIntent(device);
//                finish();
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onBluetoothStateChanged(int bluetoothState) {
        super.onBluetoothStateChanged(bluetoothState);

        if (bluetoothState == BluetoothAdapter.STATE_ON) {
            mLocalAdapter.startScanning(false);
        }
    }

    private void sendDevicePickedIntent(BluetoothDevice device) {
        Intent intent = new Intent(BluetoothDevicePicker.ACTION_DEVICE_SELECTED);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        if (mLaunchPackage != null && mLaunchClass != null) {
            intent.setClassName(mLaunchPackage, mLaunchClass);
        }
        getActivity().sendBroadcast(intent);
    }
}
*/