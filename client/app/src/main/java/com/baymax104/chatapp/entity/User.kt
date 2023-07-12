package com.baymax104.chatapp.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.baymax104.chatapp.BR

/**
 * User实体
 * @author John
 */
class User() : BaseObservable(), Parcelable {

    @get:Bindable
    var id: Int = -1
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var username: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.username)
        }

    @get:Bindable
    var age: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }

    @get:Bindable
    var gender: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.gender)
        }

    @get:Bindable
    var account: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.account)
        }

    @get:Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        username = parcel.readString() ?: ""
        age = parcel.readInt()
        gender = parcel.readString() ?: ""
        account = parcel.readString() ?: ""
        password = parcel.readString() ?: ""
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(username)
            writeInt(age)
            writeString(gender)
            writeString(account)
            writeString(password)
        }
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', age=$age, gender='$gender', account='$account', password='$password')"
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}