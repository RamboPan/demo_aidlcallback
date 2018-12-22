// CountListener.aidl
package com.rambopan.commonlib;

// Declare any non-default types here with import statements
import com.rambopan.commonlib.Person;

interface CountListener {

    //计数的接口
    void onGetNumber(int number);

    //获取每个人的信息
    void onGetPersonInfo(in Person person);
}
