package com.rambopan.aidlcallback;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.rambopan.commonlib.CountListener;
import com.rambopan.commonlib.IMyAidl;
import com.rambopan.commonlib.Person;

import java.util.Random;

/**
 * Author: RamboPan
 * Date: 2018/12/22
 * Describe:
 */
public class WorkService extends Service {
    private static final String TAG = "Server.WorkService";

    private WorkBinder mWorkBinder;
    //开启线程来递增数字
    private Thread mThread;
    //回调接口
    private CountListener mCountListener;
    //递增数字
    private int mNumber = 0;
    //开始与结束线程
    private boolean isStart = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if(mWorkBinder == null){
            mWorkBinder = new WorkBinder();
        }
    }

    private int addNumber(int a, int b){
        return a + b;
    }

    private String toUpperString(String s){
        if(s == null || s.equals("")){
            return null;
        }else{
            return s.toUpperCase();
        }
    }

    Random mRandom = new Random();

    //开始计数的实现 开启线程，2秒+1
    private boolean startCount(){
        Log.i(TAG, "startCount: ---> ");
        if(!isStart && mThread == null){
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(isStart){
                        mNumber++;
                        Log.d(TAG, "run: ---> i " + mNumber + " ； mCountListener ： " + mCountListener);
                        if(mCountListener != null){
                            try {
                                mCountListener.onGetNumber(mNumber);
                                //随机生成 A - Z 的字母作为名字
                                String name = Character.toString((char)(mRandom.nextInt(26) + 65));
                                //随机生成 0 - 20 的数字作为年龄
                                int age = mRandom.nextInt(20);
                                Person person = new Person(name,age);

                                mCountListener.onGetPersonInfo(person);
                            } catch (RemoteException e) {
                                Log.w(TAG, "deliver number Error  " );
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            isStart = true;
            mThread.start();
            return true;
        }
        return false;
    }


    //停止计数的实现
    private boolean stopCount(){
        if(isStart && mThread != null){
            isStart = false;
            return true;
        }
        return false;
    }

    //注册回调实现
    private void registerListenerImp(CountListener listener){
        Log.i(TAG, "registerListener: listener : " + listener);
        if(listener != null){
            mCountListener = listener;
        }
    }

    //反注册回调实现
    private void unregisterListenerImp(CountListener listener){
        Log.i(TAG, "unregisterListenerImp: listener : " + listener);
        mCountListener = null;
    }

    public class WorkBinder2 extends Binder{

    }

    //包装一层，调用 Service 中的实现。
    public class WorkBinder extends IMyAidl.Stub{
        @Override
        public int onNumberPlus(int number1, int number2) throws RemoteException {
            return addNumber(number1,number2);
        }

        @Override
        public String onUpperString(String strins) throws RemoteException {
            return toUpperString(strins);
        }

        @Override
        public boolean onStartCount() throws RemoteException {
            return startCount();
        }

        @Override
        public boolean onStopCount() throws RemoteException {
            return stopCount();
        }

        @Override
        public void registerListener(CountListener listener) throws RemoteException {
            registerListenerImp(listener);
        }

        @Override
        public void unregisterListener(CountListener listener) throws RemoteException {
            unregisterListenerImp(listener);
        }
    }

    //返回该 Service 实例时重要的方法。
    //如果是通过 startService 方法时，可以忽略此方法。
    @Override
    public IBinder onBind(Intent intent) {
        if(mWorkBinder == null){
            return null;
        } else{
            return mWorkBinder;
        }
    }
}