package kitonpompom.playing_english.data

import androidx.lifecycle.*

open class DataModel(): ViewModel() {


    val questionforFragGame: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val scoreForFragGame: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val resultInputForDialog: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}