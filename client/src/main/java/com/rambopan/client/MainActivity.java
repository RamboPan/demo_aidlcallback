package com.rambopan.client;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rambopan.commonlib.ConnectAssist;
import com.rambopan.commonlib.CountListener;
import com.rambopan.commonlib.IMyAidl;
import com.rambopan.commonlib.Person;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "Client.MainActivity";
    IMyAidl myAidl;

    private CountClientStub mCountClientStub;

    //新建一个继承 CountListener.Stub的类作为传入的对象。
    private class CountClientStub extends CountListener.Stub{
        @Override
        public void onGetNumber(int number) throws RemoteException {
            Log.i(TAG, "onGetNumber: number ---> " + number);
        }

        @Override
        public void onGetPersonInfo(Person person) throws RemoteException {
            Log.i(TAG, "onGetPersonInfo: ---> Person : " + person);
        }
    }

//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.i(TAG, "onServiceConnected: ");
//            myAidl = IMyAidl.Stub.asInterface(service);
//            int sum;
//            String string = null;
//            try {
//                sum = myAidl.onNumberPlus(40,47);
//                string = myAidl.onUpperString("client");
//            } catch (RemoteException e) {
//                e.printStackTrace();
//                sum = -1;
//                string = "Error";
//            }
//            Log.i(TAG, "onServiceConnected: sum " + sum );
//            Log.i(TAG, "onServiceConnected: String " + string );
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.i(TAG, "onServiceDisconnected: " );
//        }
//    };
    ConnectAssist connectAssist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bindService();

        connectAssist = ConnectAssist.getInstance(this);
        connectAssist.bindService();
        //如果在此处使用可能获得为 null 。
        myAidl = connectAssist.getMyAidl();
        Log.i(TAG, "ConnectAssist: ---> myAidl : " + myAidl);
        //传入一个 Listener
        if(mCountClientStub == null) {
            mCountClientStub = new CountClientStub();
        }
        Button btnStartCount = findViewById(R.id.btn_start_count);
        Button btnStopCount = findViewById(R.id.btn_stop_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //尝试在使用之前判断一次，如果为 null 就取一次
                if(myAidl == null){
                    myAidl = connectAssist.getMyAidl();
                }
                if(myAidl != null){
                    try {
                        myAidl.registerListener(mCountClientStub);
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
                //尝试在使用之前判断一次，如果为 null 就取一次
                if(myAidl == null){
                    myAidl = connectAssist.getMyAidl();
                }
                if(myAidl != null){
                    try {
                        myAidl.onStopCount();
                        myAidl.unregisterListener(mCountClientStub);
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
        //结束时解除连接
//        if(mServiceConnection != null){
//            unbindService(mServiceConnection);
//        }
        connectAssist.unbindService();
    }

//    private void bindService() {
//        Intent intent = new Intent("com.rambopan.commonlib.IMyAidl");
//        intent.setPackage("com.rambopan.aidlcallback");
//        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
//    }
}





