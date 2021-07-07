package com.salwa.salwa.homepage.ui.account.admin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminListModel(
    var name: String? = null,
    var dp: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var status: String? = null,
    var uid: String? = null,
) : Parcelable