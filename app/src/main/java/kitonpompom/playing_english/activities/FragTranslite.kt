package kitonpompom.playing_english.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.data.DataSelectTranslation
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.FragmentFragTransliteBinding
import kitonpompom.playing_english.dialogs.DialogCollectGame
import kitonpompom.playing_english.dialogs.InterfaceDialogCollectGame
import kitonpompom.playing_english.frag.BaseFragmentAds
import kitonpompom.playing_english.frag.FragmentManagerAds
import kitonpompom.playing_english.utils.ConstResult

class FragTranslite(private val numTask :Int?, private val id : Int?, private val nameLevel: String?, private val lockCollect: Int?, private val lockSnake: Int?): BaseFragmentAds(), ShowTranslationRcAdapter.ListenerShTr,
    InterfaceDialogCollectGame {

    lateinit var binding: FragmentFragTransliteBinding
    lateinit var adapter: ShowTranslationRcAdapter
    val dialogCollectGame = DialogCollectGame(this)

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as AppMainState).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentFragTransliteBinding.inflate(inflater, container, false)
        adView = binding.adView2
        return binding.root
    }

    override fun adsOnClose() {
    }

    override fun adsOnCloseTwo() {
    }

    override fun adsOnCloseUpdateInterAd() {
    }

    companion object {
        fun newInstance(numTask: Int?, id: Int?, nameLevel: String?, lockCollect:Int?, lockSnake:Int?) = FragTranslite( numTask = numTask, id = id, nameLevel = nameLevel, lockCollect = lockCollect, lockSnake = lockSnake)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

        val updateList = ArrayList<DataSelectTranslation>()

        fun initRcView() = with(binding){
           binding.btCloseFrTr.setOnClickListener {
               activity?.supportFragmentManager?.popBackStack()
           }
           binding.btStartFrTr.setOnClickListener{
               dialogCollectGame.showSelectedGame(activity as AppCompatActivity, numTask!!.toInt(), id!!.toInt(), nameLevel.toString(), lockCollect!!,lockSnake!!)
           }
            rcViewTr.layoutManager = LinearLayoutManager(activity)
            adapter = ShowTranslationRcAdapter(this@FragTranslite)
            rcViewTr.adapter = adapter
        }

    private fun observer(){
        mainViewModel.getTaskListByNumberTask(numTask!!).observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })
    }

    override fun dialogStartQuizGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGame.newInstance(ConstResult.INPUT_FROM_TRANSLIT ,ConstResult.LVL, numTask, id, lockCollect!!,lockSnake!!),"FragGame",activity as AppCompatActivity)
    }

    override fun dialogStartCollectGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGameCollectWord.newInstance(ConstResult.INPUT_FROM_TRANSLIT,ConstResult.LVL, numTask,id, lockCollect!!,lockSnake!!),"fragGameCollectWord", activity as AppCompatActivity)
    }

    override fun dialogStartSnakeGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGameSnakeWord.newInstance(ConstResult.INPUT_FROM_TRANSLIT,ConstResult.LVL, numTask,id, lockCollect!!, lockSnake!!),"fragGameSnakeWord", activity as AppCompatActivity)
    }

}
