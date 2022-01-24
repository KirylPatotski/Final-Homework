package com.omisoft.myapplication.mvvm.fakes

import com.omisoft.myapplication.mvvm.data.storage.UserStorage

class FakeUserStorage : UserStorage {
    override fun saveToken(token: String) {}
}