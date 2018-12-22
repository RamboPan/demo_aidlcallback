package com.rambopan.aidlcallback;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rambopan.commonlib.IMyAidl;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Server.MainActivity";
    //定义为全局引用，方便后面调用
    IMyAidl myAidl;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            myAidl = IMyAidl.Stub.asInterface(service);
            int sum;
            String string = null;
            try {
                sum = myAidl.onNumberPlus(10,20);
                string = myAidl.onUpperString("abcde");
            } catch (RemoteException e) {
                e.printStackTrace();
                sum = -1;
                string = "Error";
            }
            //试着打印下结果
            Log.i(TAG, "onServiceConnected: sum " + sum );
            Log.i(TAG, "onServiceConnected: String " + string );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    //简历连接
    private boolean bindService(){
        Intent intent = new Intent(this,WorkService.class);
        return bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束时解除连接
        if(mServiceConnection != null){
            unbindService(mServiceConnection);
        }
    }
}
