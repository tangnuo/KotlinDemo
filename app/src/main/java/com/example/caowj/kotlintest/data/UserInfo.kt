package com.example.caowj.kotlintest.data

import android.os.Parcel
import android.os.Parcelable

/**
 * 数据类<p/>
 *
 * 构造函数<br/>
 *
 * 序列化<br/>
 *
 * http://androidwing.net/index.php/97
 *
 *
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/21 12:10
 */

/**
 * 写法一：有属性的数据类
 */
//data class UserInfo(var name: String, var id: Int)

/**
 * 写法二：构造器是单参或者空参
 */
class UserInfo : Parcelable {

    var name: String? = null
    var id: Int? = 0
    var age: Int = 0
//    val x: Int = 0


    constructor(name: String?, id: Int?, age: Int) {
        this.name = name
        this.id = id
        this.age = age
    }


    /*************************序列化***************************/

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.name)
        dest?.writeInt(this.age)
        dest?.writeInt(this.id!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "UserInfo(name=$name, id=$id, age=$age)"
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<UserInfo> = object : Parcelable.Creator<UserInfo> {
            override fun createFromParcel(source: Parcel): UserInfo {
                return UserInfo(source)
            }

            override fun newArray(size: Int): Array<UserInfo?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readInt())
}