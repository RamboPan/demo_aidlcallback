// IMyAidl.aidl
package com.rambopan.commonlib;

// Declare any non-default types here with import statements
import com.rambopan.commonlib.CountListener;

interface IMyAidl {

    int onNumberPlus(int number1,int number2);

    String onUpperString(String strins);

    //开始计数
    boolean onStartCount();

    //停止计数
    boolean onStopCount();

    //注册回调
    void registerListener(CountListener listener);

    //反注册回调
    void unregisterListener(CountListener listener);
}