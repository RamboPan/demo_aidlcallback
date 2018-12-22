package com.rambopan.commonlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Author: RamboPan
 * Date: 2018/12/22
 * Describe:
 */
public class ConnectAssist {
    private static final String TAG = "ConnectAssist";
    private static ConnectAssist connectAssist;
    private IMyAidl myAidl;
    private Context mContext;

    private ConnectAssist(Context context){
        mContext = context;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            myAidl = IMyAidl.Stub.asInterface(service);
            Log.i(TAG, "onServiceConnected: ---> myAidl : " + myAidl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " );
        }
    };

    public static synchronized ConnectAssist getInstance(Context context){
        if(connectAssist == null){
            synchronized (ConnectAssist.class){
                if(connectAssist == null){
                    connectAssist = new ConnectAssist(context.getApplicationContext());
                }
            }
        }
        return connectAssist;
    }

    public boolean bindService() {
        Intent intent = new Intent("com.rambopan.commonlib.IMyAidl");
        intent.setPackage("com.rambopan.aidlcallback");
        return mContext.bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }

    public boolean unbindService(){
        if(mServiceConnection != null){
            mContext.unbindService(mServiceConnection);
            mServiceConnection = null;
            return true;
        }else{
            return false;
        }
    }

    //获取 AIDL 接口
    public IMyAidl getMyAidl(){
        return myAidl;
    }

    //释放
    public void release(){
        if(mServiceConnection != null){
            unbindService();
        }
        mContext = null;
        connectAssist = null;
    }
}
