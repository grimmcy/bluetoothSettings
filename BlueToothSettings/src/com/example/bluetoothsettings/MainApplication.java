package com.example.bluetoothsettings;

import android.app.Application;

public class MainApplication extends Application{
    
    private static MainApplication sInstance;
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sInstance = this;
    }
    
    public static MainApplication getInstance(){
        return sInstance;
    }

}
