package com.homework.app.mvvw.ui.music.favorite

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.homework.app.mvvw.AppPreferences

class SaveStringImpl(): ViewModel(), LifecycleEventObserver {

    private var preferences: AppPreferences? = null

//    val arraylist = mutableListOf(str)

    fun SaveStringInPreferences(str: String) {
        var a = GetStringImpl()
        var abc = a.getStr().toString()
        var finalstr = abc +  "\n" + str
        preferences?.saveFavorite(finalstr)
    }


//
//    fun getAdapterIndex(int: Int): Int{
//        num = int
//        return int
//
//    }


//    fun SaveString(arraylist: Collection<String>) {
//        preferences?.saveFavorite(arraylist)
//    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }


}

class GetStringImpl(): ViewModel(), LifecycleEventObserver {

    private var preferences: AppPreferences? = null

    fun getStr(): String? {
        var str = preferences?.getFavorite()
        return str.toString()

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }


//    fun setAdapterIndex(int: Int){
//        Resources.getSystem().getString(R.string.favorite_index)
//
//    }
//
//    fun getAdapterIndex(): Int{
//        return num
//    }

}
