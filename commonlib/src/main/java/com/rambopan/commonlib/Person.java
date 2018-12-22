package com.rambopan.commonlib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: RamboPan
 * Date: 2018/12/22
 * Describe:
 */
public class Person implements Parcelable {

    private String mName;
    private int mAge;

    protected Person(Parcel in) {
        mName = in.readString();
        mAge = in.readInt();
    }

    //新增构造函数。
    public Person(String name,int age){
        mName = name;
        mAge = age;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mAge);
    }

    @Override
    public String toString() {
        return " mName : " + mName +
                " mAge : " + mAge;
    }
}