package com.rambopan.testactivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rambopan.commonlib.CountListener;
import com.rambopan.commonlib.IMyAidl;
import com.rambopan.commonlib.Person;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "Test.MainActivity";
    IMyAidl myAidl;
    private WorkBinder2 mBinder = new WorkBinder2();

    private class WorkBinder2 extends Binder implements CountListener{

        @Override
        public void onGetNumber(int number) throws RemoteException {
            Log.i(TAG, "onGetNumber: ---> number : " + number);

        }

        @Override
        public void onGetPersonInfo(Person person) throws RemoteException {
            Log.i(TAG, "onGetPersonInfo: ---> person : " + person);

        }

        @Override
        public IBinder asBinder() {
            return mBinder;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            myAidl = IMyAidl.Stub.asInterface(service);
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

        //传入一个 Listener
        Button btnStartCount = findViewById(R.id.btn_start_count);
        Button btnStopCount = findViewById(R.id.btn_stop_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myAidl != null){
                    try {
                        myAidl.registerListener(mBinder);
                        myAidl.onStartCount();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.w(TAG, "onClick: registerListener RemoteException" );
                    }
                }
            }
        });
        btnStopCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myAidl != null){
                    try {
                        myAidl.unregisterListener(mBinder);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.w(TAG, "onClick: unregisterListener RemoteException" );
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    public boolean bindService() {
        Intent intent = new Intent("com.rambopan.commonlib.IMyAidl");
        intent.setPackage("com.rambopan.aidlcallback");
        return bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }

    public boolean unbindService(){
        if(mServiceConnection != null){
            unbindService(mServiceConnection);
            mServiceConnection = null;
            return true;
        }else{
            return false;
        }
    }
}