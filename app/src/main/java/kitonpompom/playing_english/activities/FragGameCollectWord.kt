package kitonpompom.playing_english.activities

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.data.DataModel
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.FragmentFragGameCollectWordBinding
import kitonpompom.playing_english.dialogs.DialogFragGameEndPositive
import kitonpompom.playing_english.entities.TaskListItem
import kitonpompom.playing_english.frag.*
import kitonpompom.playing_english.utils.ConstResult


class FragGameCollectWord(private val closeResultEnter: String?, private  val resultEnter: String?, private val numTask:Int?, private val id:Int?, private val lockCollect: Int?, private val lockSnake: Int?) : BaseFragmentAds(), LetterTwoAdapterCollectWord.LetterTwoInterface, FragmentCloseInterface, FragmentReturnInterface, StartGameEndDialog{

    private lateinit var binding: FragmentFragGameCollectWordBinding
    val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as AppMainState).database)
    }
    val dialogEndPositive = DialogFragGameEndPositive(this,this, this)
    private val dataModel: DataModel by activityViewModels()
    private var cikl:Int = 0
    private var maxSizeWord:Int = 3
    private var cont:Int = 0

    var newList: ArrayList<String> = ArrayList()
    var itList: List<TaskListItem> = listOf()
    var answer: String = ""
    var indVw: Int = 0
    var max: Int = 0
    var stopUpdateOnResum: Int = 0
    var tempOptionRus: String = ""
    var tempOptionEng: String = ""
    val arrayAlphabet = arrayOf("a","b","c","d","e","f","g","h","i","j","k",
        "l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")
    var arrayLetterAnswerNull: ArrayList<String> = ArrayList()
    var arrayLetterAnswer: ArrayList<String> = ArrayList()
    var arrayLetterAnswerAbc: ArrayList<String> = ArrayList()
    var arrayLetterAnswerPos: ArrayList<Int> = ArrayList()
    var arrayLetterAnswerAbcShuffle: ArrayList<String> = ArrayList()
    private var adapterOne: LetterOneAdapterCollectWord? = null
    private var adapterTwo: LetterTwoAdapterCollectWord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFragGameCollectWordBinding.inflate(inflater, container, false)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prBar = binding.progressBar

        initRcView()
        observer()
        initBack()

        binding.btContinueAnswerCollectWord.setOnClickListener(){
            enterContinue()
        }

        binding.btClose.setOnClickListener(){
        dialogEndPositive.showExitQuestion(activity as AppCompatActivity)
        }

        dataModel.scoreForFragGame.observe(activity as LifecycleOwner,{
            ObjectAnimator.ofInt(prBar, "progress", it)
                .setDuration(15)
                .start()
        })
    }

    private fun observer() {
            if (resultEnter == ConstResult.LVL) {
                mainViewModel.getTaskListByNumberTask(numTask!!).observe(viewLifecycleOwner) {
                    if (stopUpdateOnResum == 0) {
                        stopUpdateOnResum = 1
                        max = it.size
                        binding.progressBar.max = max
                        dataModel.scoreForFragGame.value = cikl
                        itList = it
                        itList = shuffleArrayAnswerWord(itList)
                        if (newList.isEmpty()) { // проверка что newList пустой
                            for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                                newList.add(itList.get(i).word)
                            }
                        }
                        binding.tvVopros2.text =
                            newList[cikl] //передемв текствью задания русское слово с 0 позиции
                        indVw =
                            newList.indexOf(binding.tvVopros2.text.toString()) //получаем индекс позиции русского слова в newList
                        answer = itList.get(indVw).rightAnswer // получаем ответ
                        tempOptionRus = itList.get(indVw).optionInTheTextRus
                        tempOptionEng = itList.get(indVw).optionInTheTextEng
                        //Log.d("MyLog", "answer : $answer")
                        arrayLetterAnswerNull.clear()
                        arrayLetterAnswerAbc.clear()
                        arrayLetterAnswerAbcShuffle.clear()
                        arrayLetterAnswerPos.clear()
                        arrayLetterAnswer.clear()
                        for (char in answer) { // разбиваем по буквам ответ и записываем в массив
                            arrayLetterAnswerNull.add("")
                            arrayLetterAnswer.add(char.toString())
                            arrayLetterAnswerAbc.add(char.toString())
                        }
                        for (i in 1..3) {
                            arrayLetterAnswerAbc.add(arrayAlphabet.random().toString())
                        }
                        arrayLetterAnswerAbcShuffle =
                            shuffleArrayLetterAnswerAbc(arrayLetterAnswerAbc)
                        maxSizeWord = arrayLetterAnswerAbc.size
                        adapterOne?.updateAdapter(arrayLetterAnswerNull)
                        adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
                    }
                }
            } else {
                mainViewModel.getTaskListByLockRequest(1).observe(viewLifecycleOwner) {
                    if (stopUpdateOnResum == 0) {
                        stopUpdateOnResum = 1
                        binding.idImBrain.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.linHuisHu.visibility = View.VISIBLE
                        max = it.size
                        binding.progressBar.max = max
                        dataModel.scoreForFragGame.value = cikl
                        binding.tvHuisHu.text = max.toString()
                        binding.tvHu.text = cikl.toString()
                        binding.progressBar.max = max
                        itList = it
                        itList = shuffleArrayAnswerWord(itList)
                        if (newList.isEmpty()) { // проверка что newList пустой
                            for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                                newList.add(itList.get(i).word)
                            }
                        }
                        binding.tvVopros2.text =
                            newList[cikl] //передемв текствью задания русское слово с 0 позиции
                        indVw =
                            newList.indexOf(binding.tvVopros2.text.toString()) //получаем индекс позиции русского слова в newList
                        answer = itList.get(indVw).rightAnswer // получаем ответ
                        tempOptionRus = itList.get(indVw).optionInTheTextRus
                        tempOptionEng = itList.get(indVw).optionInTheTextEng
                        arrayLetterAnswerNull.clear()
                        arrayLetterAnswerAbc.clear()
                        arrayLetterAnswerAbcShuffle.clear()
                        arrayLetterAnswerPos.clear()
                        arrayLetterAnswer.clear()
                        for (char in answer) { // разбиваем по буквам ответ и записываем в массив
                            arrayLetterAnswerNull.add("")
                            arrayLetterAnswer.add(char.toString())
                            arrayLetterAnswerAbc.add(char.toString())
                        }
                        for (i in 1..3) {
                            arrayLetterAnswerAbc.add(arrayAlphabet.random().toString())
                        }
                        arrayLetterAnswerAbcShuffle =
                            shuffleArrayLetterAnswerAbc(arrayLetterAnswerAbc)
                        maxSizeWord = arrayLetterAnswerAbc.size
                        adapterOne?.updateAdapter(arrayLetterAnswerNull)
                        adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
                    }
                }
            }
    }

    fun shuffleArrayLetterAnswerAbc(list: ArrayList<String>):ArrayList<String>{
        val shuffleTemp: ArrayList<String> = list.shuffled() as ArrayList<String>
        return shuffleTemp
    }

    fun shuffleArrayAnswerWord(list: List<TaskListItem>):List<TaskListItem>{
        val shuffleTemp: List<TaskListItem> = list.shuffled() as List<TaskListItem>
        return shuffleTemp
    }

    private fun initRcView(){
        binding.rcLetterNull.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        adapterOne = LetterOneAdapterCollectWord(this)
        binding.rcLetterNull.adapter = adapterOne
        binding.rcLetterAbc.layoutManager = StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL)
        adapterTwo = LetterTwoAdapterCollectWord(this)
        binding.rcLetterAbc.adapter = adapterTwo
    }


    override fun adsOnClose() {
        if(resultEnter == ConstResult.LVL) {
            if(closeResultEnter == ConstResult.INPUT_FRAG_LVL) {
                activity?.supportFragmentManager?.popBackStack()
            }else{
                activity?.supportFragmentManager?.popBackStack("fragLvl",0)
            }
        }else{
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun adsOnCloseTwo() {
        TODO("Not yet implemented")
    }

    override fun adsOnCloseUpdateInterAd() {
        TODO("Not yet implemented")
    }

    override fun updateLockGame() {
        mainViewModel.updateLockSnakeById(id!!,1)
    }

    companion object {
        @JvmStatic
        fun newInstance(closeResultEnter:String?, resultEnter: String?, numTask: Int?,id: Int?, lockCollect: Int?, lockSnake:Int? ) = FragGameCollectWord(closeResultEnter = closeResultEnter, resultEnter = resultEnter ,numTask = numTask, id = id, lockCollect = lockCollect, lockSnake = lockSnake)
    }

    override fun onClickItemViewLetterTwo(pos: Int, letter: String) {
        if (cont < arrayLetterAnswerNull.size) {
            arrayLetterAnswerNull[cont] = letter
            adapterOne?.updateAdapter(arrayLetterAnswerNull)
            if(arrayLetterAnswerNull == arrayLetterAnswer){
                binding.rcLetterAbc.visibility = View.GONE
                binding.tvOptionTextRus.text = tempOptionRus
                binding.tvOptionTextEng.text = tempOptionEng
                binding.cvRight.visibility = View.VISIBLE
                cikl++
            }
            arrayLetterAnswerAbcShuffle[pos] = ""
            adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
            cont++
            arrayLetterAnswerPos.add(pos)
        }
    }

    override fun onClickItemViewLetterOne(letterOne: String) {
        if(binding.cvRight.visibility == View.GONE) {
            if (cont > 0) {
                arrayLetterAnswerAbcShuffle[arrayLetterAnswerPos[cont - 1]] =
                    arrayLetterAnswerNull[cont - 1]
                arrayLetterAnswerPos.removeAt(cont - 1)
                adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
                arrayLetterAnswerNull[cont - 1] = ""
                adapterOne?.updateAdapter(arrayLetterAnswerNull)
                cont--
            }
        }
    }

    fun enterContinue(){
        if(cikl == max){ // Тут игра заканчивается
            dialogEndPositive.showEndCollectGamePositiveDialog(activity as AppCompatActivity, itList ,newList, lockSnake!!)
        }else {
            dataModel.scoreForFragGame.value = cikl
            cont = 0
            binding.tvHu.text = cikl.toString()
            binding.cvRight.visibility = View.GONE
            binding.tvVopros2.text =
                newList[cikl] //передаем в текствью задания русское слово с 0 позиции
            indVw =
                newList.indexOf(binding.tvVopros2.text.toString()) //получаем индекс позиции русского слова в newList
            answer = itList.get(indVw).rightAnswer // получаем ответ
            tempOptionRus = itList.get(indVw).optionInTheTextRus //получаем контекст в рус варианте
            tempOptionEng = itList.get(indVw).optionInTheTextEng //получаем контекст в анг варианте
            arrayLetterAnswerNull.clear()
            arrayLetterAnswerAbc.clear()
            arrayLetterAnswerAbcShuffle.clear()
            arrayLetterAnswerPos.clear()
            arrayLetterAnswer.clear()
            binding.rcLetterNull.adapter = adapterOne
            for (char in answer) { // разбиваем по буквам ответ и записываем в массив
                arrayLetterAnswerNull.add("")
                arrayLetterAnswer.add(char.toString())
                arrayLetterAnswerAbc.add(char.toString())
            }
            for (i in 1..3) {
                arrayLetterAnswerAbc.add(arrayAlphabet.random().toString())
            }
            arrayLetterAnswerAbcShuffle = shuffleArrayLetterAnswerAbc(arrayLetterAnswerAbc)
            maxSizeWord = arrayLetterAnswerAbc.size
            adapterOne?.updateAdapter(arrayLetterAnswerNull)
            adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
            binding.rcLetterAbc.visibility = View.VISIBLE
        }
    }

    fun initBack(){ //инициализируем и переназначаем кнопку телефона "назад"
        val callback = requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //activity?.supportFragmentManager?.popBackStack("fragLvl", 0)
                    dialogEndPositive.showExitQuestion(activity as AppCompatActivity)
                }
            })
    }

    override fun onFragClose() {
        showInterAd()
    }

    override fun returnGame(newList: ArrayList<String>?, itListForReturn: List<TaskListItem>) {
        cikl = 0
        dataModel.scoreForFragGame.value = cikl
        cont = 0
        binding.tvHu.text = cikl.toString()
        dataModel.scoreForFragGame.value = cikl
        newList?.clear()
        itList = itListForReturn
        itList = shuffleArrayAnswerWord(itList)
            for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                newList?.add(itList.get(i).word)
            }
        binding.cvRight.visibility = View.GONE
        binding.tvVopros2.text =  newList!![cikl] //передаем в текствью задания русское слово с 0 позиции
        indVw =
            newList.indexOf(binding.tvVopros2.text.toString()) //получаем индекс позиции русского слова в newList
        answer = itList.get(indVw).rightAnswer // получаем ответ
        tempOptionRus = itList.get(indVw).optionInTheTextRus //получаем контекст в рус варианте
        tempOptionEng = itList.get(indVw).optionInTheTextEng //получаем контекст в анг варианте
        arrayLetterAnswerNull.clear()
        arrayLetterAnswerAbc.clear()
        arrayLetterAnswerAbcShuffle.clear()
        arrayLetterAnswerPos.clear()
        arrayLetterAnswer.clear()
        for (char in answer) { // разбиваем по буквам ответ и записываем в массив
            arrayLetterAnswerNull.add("")
            arrayLetterAnswer.add(char.toString())
            arrayLetterAnswerAbc.add(char.toString())
        }
        for (i in 1..3) {
            arrayLetterAnswerAbc.add(arrayAlphabet.random().toString())
        }
        arrayLetterAnswerAbcShuffle = shuffleArrayLetterAnswerAbc(arrayLetterAnswerAbc)
        maxSizeWord = arrayLetterAnswerAbc.size
        adapterOne?.updateAdapter(arrayLetterAnswerNull)
        adapterTwo?.updateAdapter(arrayLetterAnswerAbcShuffle)
        binding.rcLetterAbc.visibility = View.VISIBLE
    }

    override fun startGameCollectEndDialog() {
    }

    override fun startGameSnakeEndDialog() {
        FragmentManagerAds.setFragment(FragGameSnakeWord.newInstance(ConstResult.INPUT_FROM_TRANSLIT,ConstResult.LVL, numTask,id, lockCollect!!, lockSnake!!),
            "fragGameSnakeWord", activity as AppCompatActivity)
    }
}