package kitonpompom.playing_english.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.FragmentFragLvlBinding
import kitonpompom.playing_english.dialogs.DialogCollectGame
import kitonpompom.playing_english.dialogs.InterfaceDialogCollectGame
import kitonpompom.playing_english.frag.BaseFragmentAds
import kitonpompom.playing_english.frag.FragmentManagerAds
import kitonpompom.playing_english.utils.ConstResult


class FragLvl() : BaseFragmentAds(), LevelFragRcAdapter.Listener, InterfaceDialogCollectGame {

    private lateinit var binding: FragmentFragLvlBinding
    private var adapter: LevelFragRcAdapter? = null
    val dialogCollectGame = DialogCollectGame(this)

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as AppMainState).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragLvlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun adsOnClose() {
        TODO("Not yet implemented")
    }

    override fun adsOnCloseTwo() {
        TODO("Not yet implemented")
    }

    override fun adsOnCloseUpdateInterAd() {
        TODO("Not yet implemented")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()

        binding.btCloseFragLvl.setOnClickListener(){
            //activity?.supportFragmentManager?.beginTransaction()?.remove(this@FragLvl)
              //  ?.commit() // функция закрытия/сварачивания фрагмента
            closePopBackStack()
        }
    }

    private fun initRcView() = with(binding){//инициализирует адаптер
            idRcLvlFrag.layoutManager = LinearLayoutManager(activity)
            adapter = LevelFragRcAdapter(this@FragLvl)
            idRcLvlFrag.adapter = adapter
    }

    private fun observer(){
        mainViewModel.allLevelList.observe(viewLifecycleOwner,{
            adapter?.submitList(it)
        })
    }

    companion object{
        @JvmStatic
        fun newInstance() = FragLvl()
    }

    override fun openFragGame(numTask: Int, id: Int, nameLevel:String, lockCollect: Int, lockSnake: Int) {
        dialogCollectGame.showSelectedGame(activity as AppCompatActivity, numTask, id, nameLevel, lockCollect,lockSnake)
    }

    override fun openFragTranslite(numTask: Int, id: Int, nameLevel: String, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragTranslite.newInstance(numTask, id, nameLevel, lockCollect, lockSnake),"fragTranslite",activity as AppCompatActivity)
    }

    fun closePopBackStack (){ // функция для выхода на предыдущий фрагмент сахраненный в стэке
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun dialogStartQuizGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGame.newInstance(ConstResult.INPUT_FRAG_LVL ,ConstResult.LVL, numTask, id, lockCollect, lockSnake),"FragGame",activity as AppCompatActivity)
    }

    override fun dialogStartCollectGame(numTask:Int, id:Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGameCollectWord.newInstance(ConstResult.INPUT_FRAG_LVL, ConstResult.LVL, numTask, id, lockCollect, lockSnake),"fragGameCollectWord", activity as AppCompatActivity)
    }

    override fun dialogStartSnakeGame(numTask:Int, id:Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(FragGameSnakeWord.newInstance(ConstResult.INPUT_FRAG_LVL, ConstResult.LVL, numTask,id, lockCollect,lockSnake),"fragGameSnakeWord", activity as AppCompatActivity)
    }

}