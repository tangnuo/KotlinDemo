package com.example.caowj.kotlintest.java;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/22 17:46
 */
public class UserInfo2 implements Parcelable {

    public static final Parcelable.Creator<UserInfo2> CREATOR = new Parcelable.Creator<UserInfo2>() {
        @Override
        public UserInfo2 createFromParcel(Parcel source) {
            return new UserInfo2(source);
        }

        @Override
        public UserInfo2[] newArray(int size) {
            return new UserInfo2[size];
        }
    };
    String name;
    int age;

    public UserInfo2() {
    }

    protected UserInfo2(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
    }
}
