package ru.netology.nmedia

import android.os.Parcel
import android.os.Parcelable

class EditPostResult(
    val newContent: String?,
    val newVideoUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(newContent)
        parcel.writeString(newVideoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EditPostResult> {
        override fun createFromParcel(parcel: Parcel): EditPostResult {
            return EditPostResult(parcel)
        }

        override fun newArray(size: Int): Array<EditPostResult?> {
            return arrayOfNulls(size)
        }
    }
}