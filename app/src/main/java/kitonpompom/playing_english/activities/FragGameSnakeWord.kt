package kitonpompom.playing_english.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.R
import kitonpompom.playing_english.data.DataModel
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.FragmentFragGameSnakeWordBinding
import kitonpompom.playing_english.dialogs.DialogFragGameEndPositive
import kitonpompom.playing_english.entities.TaskListItem
import kitonpompom.playing_english.frag.BaseFragmentAds
import kitonpompom.playing_english.frag.FragmentCloseInterface
import kitonpompom.playing_english.frag.FragmentReturnInterface
import kitonpompom.playing_english.frag.StartGameEndDialog
import kitonpompom.playing_english.utils.ConstResult
import kotlin.random.Random


class FragGameSnakeWord(private val closeResultEnter: String?, private  val resultEnter: String?, private val numTask:Int?, private val id:Int?, private val lockCollect: Int?, private val lockSnake: Int?) : BaseFragmentAds(),
    FragmentCloseInterface, FragmentReturnInterface, SnakeAdapterWord.SnakeInterface, StartGameEndDialog{

    private lateinit var binding: FragmentFragGameSnakeWordBinding
    val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as AppMainState).database)
    }
    val dialogEndPositive = DialogFragGameEndPositive(this,this,this)
    private val dataModel: DataModel by activityViewModels()
    private var cikl:Int = 0
    private var maxSizeWord:Int = 3
    private var cont:Int = 0
    private var posCordinat:Int = 0

    var newList: ArrayList<String> = ArrayList()
    var itList: List<TaskListItem> = listOf()
    var answer: String = ""
    var TestAnswer: String = "АБВГДЕЁЖЗИКЛМН"
    var indVw: Int = 0
    var max: Int = 0
    var stopUpdateOnResum: Int = 0
    var tempOptionRus: String = ""
    var tempOptionEng: String = ""
    val arrayAlphabet = arrayOf("a","b","c","d","e","f","g","h","i","j","k",
        "l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")
    var arrayLetterAnswerNull: ArrayList<String> = ArrayList()
    var arrayLetterAnswer: ArrayList<String> = ArrayList()
    var arrayRightAnswer: ArrayList<String> = ArrayList()
    var arrayLetterAnswerAbc: ArrayList<String> = ArrayList()
    lateinit var arraySnakePos: Array<String>
    var arrayLetterRightAnswerSnake: ArrayList<String> = ArrayList()
    var arrayLetterAnswerPos: ArrayList<Int> = ArrayList()
    var arrayPosStop: ArrayList<Int> = ArrayList()
    var arrayTempPosGreen: ArrayList<Int> = ArrayList()
    var arrayTempPosGreenTwo: ArrayList<Int> = ArrayList()
    var arrayPosForVisibility: ArrayList<Int> = ArrayList()
    var arrayPosUsed: ArrayList<Int> = ArrayList()
    var arrayLetterAnswerAbcShuffle: ArrayList<String> = ArrayList()
    private var adapterSnake: SnakeAdapterWord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFragGameSnakeWordBinding.inflate(inflater, container, false)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prBar = binding.progressBar

        initRcView()
        observer()
        initBack()

        binding.btContinueAnswerCollectWord?.setOnClickListener(){
            enterContinue()
        }

        binding.btClose?.setOnClickListener(){
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
                if(stopUpdateOnResum == 0) {
                    stopUpdateOnResum = 1
                    max = it.size
                    binding.progressBar?.max = max
                    dataModel.scoreForFragGame.value = cikl
                    itList = it
                    itList = shuffleArrayAnswerWord(itList)
                    if (newList.isEmpty()) { // проверка что newList пустой
                        for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                            newList.add(itList.get(i).word)
                        }
                    }
                    binding.tvVopros2?.text =
                        newList[cikl] //передемв текствью задания русское слово с 0 позиции
                    indVw =
                        newList.indexOf(binding.tvVopros2?.text.toString()) //получаем индекс позиции русского слова в newList
                    answer = itList.get(indVw).rightAnswer // получаем ответ
                    tempOptionRus = itList.get(indVw).optionInTheTextRus
                    tempOptionEng = itList.get(indVw).optionInTheTextEng
                    arrayLetterAnswerNull.clear()
                    arrayLetterAnswerAbc.clear()
                    arrayLetterAnswerAbcShuffle.clear()
                    arrayLetterAnswerPos.clear()
                    arrayLetterAnswer.clear()
                    arrayPosForVisibility.clear()
                    arrayTempPosGreen()
                    maxSizeWord = arrayLetterAnswerAbc.size
                    adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
                    snakeAddLetterArray()
                }
            }
        }else{
            mainViewModel.getTaskListByLockRequest(1).observe(viewLifecycleOwner) {
                if(stopUpdateOnResum == 0) {
                    stopUpdateOnResum = 1
                    binding.idImBrain.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.linHuisHu?.visibility = View.VISIBLE
                    max = it.size
                    binding.tvHuisHu.text = max.toString()
                    binding.tvHu.text = cikl.toString()
                    binding.progressBar.max = max
                    binding.progressBar?.max = max
                    dataModel.scoreForFragGame.value = cikl
                    itList = it
                    itList = shuffleArrayAnswerWord(itList)
                    if (newList.isEmpty()) { // проверка что newList пустой
                        for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                            newList.add(itList.get(i).word)
                        }
                    }
                    binding.tvVopros2?.text =
                        newList[cikl] //передемв текствью задания русское слово с 0 позиции
                    indVw =
                        newList.indexOf(binding.tvVopros2?.text.toString()) //получаем индекс позиции русского слова в newList
                    answer = itList.get(indVw).rightAnswer // получаем ответ
                    tempOptionRus = itList.get(indVw).optionInTheTextRus
                    tempOptionEng = itList.get(indVw).optionInTheTextEng
                    //Log.d("MyLog", "answer : $answer")
                    arrayLetterAnswerNull.clear()
                    arrayLetterAnswerAbc.clear()
                    arrayLetterAnswerAbcShuffle.clear()
                    arrayLetterAnswerPos.clear()
                    arrayLetterAnswer.clear()
                    arrayPosForVisibility.clear()
                    arrayTempPosGreen()
                    maxSizeWord = arrayLetterAnswerAbc.size
                    adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
                    snakeAddLetterArray()
                }
            }
        }
    }

    fun enterContinue(){
        if(cikl == max){
            dialogEndPositive.showEndSnakeGamePositiveDialog(activity as AppCompatActivity, itList, newList, lockSnake!!)
        }else {
            dataModel.scoreForFragGame.value = cikl
            cont = 0
            binding.tvHu.text = cikl.toString()
            binding.cvRight?.visibility = View.GONE
            binding.tvVopros2?.text =
                newList[cikl] //передемв текствью задания русское слово с 0 позиции
            indVw =
                newList.indexOf(binding.tvVopros2?.text.toString()) //получаем индекс позиции русского слова в newList
            answer = itList.get(indVw).rightAnswer // получаем ответ
            tempOptionRus = itList.get(indVw).optionInTheTextRus //получаем контекст в рус варианте
            tempOptionEng = itList.get(indVw).optionInTheTextEng //получаем контекст в анг варианте
            arrayLetterAnswerNull.clear()
            arrayLetterAnswerAbc.clear()
            arrayLetterAnswerAbcShuffle.clear()
            arrayLetterAnswerPos.clear()
            arrayLetterAnswer.clear()
            arrayTempPosGreen()
            adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
            snakeAddLetterArray()
            binding.rcLetterSnake?.visibility = View.VISIBLE
        }
    }

    fun snakeAddLetterArray(){ // функция заполнения массива для rc View данными
        arrayLetterAnswerNull.clear()

        for(char in 0..24){ // наполняем массив цифрами
            arrayAlphabet.shuffle()
            arrayLetterAnswerNull.add(arrayAlphabet[char])
            arrayPosForVisibility.add(0) //паралельный массив для адаптера (для изменения цвета фона cardview)
        }
        adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
        var cikl_snake = 0
        //Log.d("MyLog", "cikl : $cikl")
        arrayPosUsed.clear()
        arraySnakePos = resources.getStringArray(R.array.array_snake) //получаем массив с позициями направлений
        var startIndexPos = Random.nextInt(arraySnakePos.size) //получаем позицию для старта рисования
        var index = startIndexPos
        arrayPosUsed.add(index) // запалняем массив использованой позицией
        arrayRightAnswer.clear()
        for(i in answer){
            arrayRightAnswer.add(i.toString())
        }
        for (i in answer) {
            arrayLetterAnswerNull[index] = i.toString()
            var arraySnakeString = arraySnakePos[index] //по стартовой позиции получаем стрингу с путями для этой позиции
            val stringArrayWay = arraySnakeString.split(",") //получаем разбитый массив с путями типа стринг
            var randomWayPos = randomWayPosFun(stringArrayWay)
            if(cikl_snake < TestAnswer.length-1) {
                index = randomWayPos
                arrayPosUsed.add(index)
                cikl_snake++
            }
        }
        for(pos in arrayPosUsed) {
        }
        adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
    }



    fun randomWayPosFun(list:List<String>): Int {
        var randomPos = 0
        var tempList: ArrayList<Int> = ArrayList()
        var tempListAdd: ArrayList<Int> = ArrayList()
        for(i in list){ // записываем в templist элементы из пришедшего листа и преобразовываем их в Int
            tempList.add(i.toInt())
            tempListAdd.add(i.toInt())
        }
        for(i in tempList){ // проверяем каждый элемент на совпадение с уже занятыми позициями и если есть совпадения удалем элемент
            if(arrayPosUsed.contains(i)) {
                tempListAdd.remove(i)
            }
        }
        if(tempListAdd.size == 0){
            snakeAddLetterArray()
        }else if(tempListAdd.size == 1){
            randomPos = tempListAdd[0]
        }else{
            randomPos = tempListAdd[Random.nextInt(tempListAdd.size)]
        }
        return randomPos
    }

    fun shuffleArrayLetterAnswerAbc(list: ArrayList<String>):ArrayList<String>{
        val shuffleTemp: ArrayList<String> = list.shuffled() as ArrayList<String>
        return shuffleTemp
    }

    fun arrayTempPosGreen(){ // заполнить массив для старта свободными позициями
        for(i in 0..24){
            arrayTempPosGreen.add(i)
        }
    }

    fun addArrayPuts (positionClick: Int ){ //функция для получения позиций куда можно ходить когда уже ведем пальцем
        var arrayTempStringSnake = arraySnakePos[positionClick]
        var tempStringArrayWay = arrayTempStringSnake.split(",")
        arrayTempPosGreen.clear()
        arrayTempPosGreenTwo.clear()
        for(i in tempStringArrayWay){
            arrayTempPosGreen.add(i.toInt())
            arrayTempPosGreenTwo.add(i.toInt())
        }
        arrayPosStop.add(positionClick)

        for (i in arrayTempPosGreenTwo) { // проверяем каждый элемент на совпадение с уже занятыми позициями и если есть совпадения удалем элемент
            if (arrayPosStop.contains(i)) {
                arrayTempPosGreen.remove(i)
            }
        }
        adapterSnake?.updateItem(posCordinat)
    }

   @SuppressLint("ClickableViewAccessibility")
    private fun initRcView() {
        binding.rcLetterSnake?.layoutManager =
            StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL)
        adapterSnake = SnakeAdapterWord(this)
        binding.rcLetterSnake?.adapter = adapterSnake
        binding.rcLetterSnake?.setOnTouchListener { v, event ->
            val maxSizeView = v.width
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if ((event.x > 0 && event.x < (maxSizeView.toFloat()) / 5) && (event.y > 0
                                && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 0
                        if (!arrayPosStop.contains(0) && arrayTempPosGreen.contains(0)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 1
                        if (!arrayPosStop.contains(1) && arrayTempPosGreen.contains(1)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 2
                        if (!arrayPosStop.contains(2) && arrayTempPosGreen.contains(2)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 3
                        if (!arrayPosStop.contains(3) && arrayTempPosGreen.contains(3)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 4
                        if (!arrayPosStop.contains(4) && arrayTempPosGreen.contains(4)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > 0 && event.x < (maxSizeView.toFloat()) / 5)
                                && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5)
                    ) {
                        posCordinat = 5
                        if (!arrayPosStop.contains(5) && arrayTempPosGreen.contains(5)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 6
                        if (!arrayPosStop.contains(6) && arrayTempPosGreen.contains(6)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 7
                        if (!arrayPosStop.contains(7) && arrayTempPosGreen.contains(7)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 8
                        if (!arrayPosStop.contains(8) && arrayTempPosGreen.contains(8)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 9
                        if (!arrayPosStop.contains(9) && arrayTempPosGreen.contains(9)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 10
                        if (!arrayPosStop.contains(10) && arrayTempPosGreen.contains(10)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 11
                        if (!arrayPosStop.contains(11) && arrayTempPosGreen.contains(11)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 12
                        if (!arrayPosStop.contains(12) && arrayTempPosGreen.contains(12)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 13
                        if (!arrayPosStop.contains(13) && arrayTempPosGreen.contains(13)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 14
                        if (!arrayPosStop.contains(14) && arrayTempPosGreen.contains(14)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 1.66) && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 15
                        if (!arrayPosStop.contains(15) && arrayTempPosGreen.contains(15)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 16
                        if (!arrayPosStop.contains(16) && arrayTempPosGreen.contains(16)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 17
                        if (!arrayPosStop.contains(17) && arrayTempPosGreen.contains(17)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 18
                        if (!arrayPosStop.contains(18) && arrayTempPosGreen.contains(18)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 19
                        if (!arrayPosStop.contains(19) && arrayTempPosGreen.contains(19)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 20
                        if (!arrayPosStop.contains(20) &&  arrayTempPosGreen.contains(20)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 21
                        if (!arrayPosStop.contains(21) && arrayTempPosGreen.contains(21)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 22
                        if (!arrayPosStop.contains(22) && arrayTempPosGreen.contains(22)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 23
                        if (!arrayPosStop.contains(23) && arrayTempPosGreen.contains(23)) {
                            addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 24
                        if (!arrayPosStop.contains(24) && arrayTempPosGreen.contains(24)) {
                            addArrayPuts(posCordinat)
                        }
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if ((event.x > 0 && event.x < (maxSizeView.toFloat()) / 5) && (event.y > 0
                                && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 0
                        if (!arrayPosStop.contains(0) && arrayTempPosGreen.contains(0)) {
                               addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 1
                        if (!arrayPosStop.contains(1) && arrayTempPosGreen.contains(1)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 2
                        if (!arrayPosStop.contains(2) && arrayTempPosGreen.contains(2)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 3
                        if (!arrayPosStop.contains(3) && arrayTempPosGreen.contains(3)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > 0 && event.y < (maxSizeView.toFloat()) / 5)
                    ) {
                        posCordinat = 4
                        if (!arrayPosStop.contains(4) && arrayTempPosGreen.contains(4)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > 0 && event.x < (maxSizeView.toFloat()) / 5)
                                && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5)
                    ) {
                        posCordinat = 5
                        if (!arrayPosStop.contains(5) && arrayTempPosGreen.contains(5)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 6
                        if (!arrayPosStop.contains(6) && arrayTempPosGreen.contains(6)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 7
                        if (!arrayPosStop.contains(7) && arrayTempPosGreen.contains(7)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 8
                        if (!arrayPosStop.contains(8) && arrayTempPosGreen.contains(8)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 5) && event.y < (maxSizeView.toFloat()) / 2.5
                    ) {
                        posCordinat = 9
                        if (!arrayPosStop.contains(9) && arrayTempPosGreen.contains(9)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 10
                        if (!arrayPosStop.contains(10) && arrayTempPosGreen.contains(10)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 11
                        if (!arrayPosStop.contains(11) && arrayTempPosGreen.contains(11)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 12
                        if (!arrayPosStop.contains(12) && arrayTempPosGreen.contains(12)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 13
                        if (!arrayPosStop.contains(13) && arrayTempPosGreen.contains(13)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 2.5) && event.y < (maxSizeView.toFloat()) / 1.66
                    ) {
                        posCordinat = 14
                        if (!arrayPosStop.contains(14) && arrayTempPosGreen.contains(14)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 1.66) && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 15
                        if (!arrayPosStop.contains(15) && arrayTempPosGreen.contains(15)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 16
                        if (!arrayPosStop.contains(16) && arrayTempPosGreen.contains(16)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 17
                        if (!arrayPosStop.contains(17) && arrayTempPosGreen.contains(17)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 18
                        if (!arrayPosStop.contains(18) && arrayTempPosGreen.contains(18)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && event.y < (maxSizeView.toFloat()) / 1.25
                    ) {
                        posCordinat = 19
                        if (!arrayPosStop.contains(19) && arrayTempPosGreen.contains(19)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if ((event.x > 0 && event.x < (maxSizeView.toFloat() / 5))
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 20
                        if (!arrayPosStop.contains(20) &&  arrayTempPosGreen.contains(20)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 5) && event.x < (maxSizeView.toFloat()) / 2.5)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 21
                        if (!arrayPosStop.contains(21) && arrayTempPosGreen.contains(21)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 2.5) && event.x < (maxSizeView.toFloat()) / 1.66)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 22
                        if (!arrayPosStop.contains(22) && arrayTempPosGreen.contains(22)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.66) && event.x < (maxSizeView.toFloat()) / 1.25)
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 23
                        if (!arrayPosStop.contains(23) && arrayTempPosGreen.contains(23)) {
                                addArrayPuts(posCordinat)
                        }
                    } else if (((event.x > (maxSizeView.toFloat()) / 1.25) && event.x < (maxSizeView.toFloat()))
                        && (event.y > (maxSizeView.toFloat()) / 1.25) && event.y < (maxSizeView.toFloat())
                    ) {
                        posCordinat = 24
                        if (!arrayPosStop.contains(24) && arrayTempPosGreen.contains(24)) {
                                addArrayPuts(posCordinat)
                        }
                    }
                }

                MotionEvent.ACTION_UP -> { // действие при отпускании пальца
                    for(i in arrayLetterRightAnswerSnake) {
                    }
                    for(i in arrayRightAnswer) {
                    }
                    if (arrayLetterRightAnswerSnake == arrayRightAnswer) {
                        binding.tvRightCollectWord.text = answer
                        binding.tvOptionTextRus.text = tempOptionRus
                        binding.tvOptionTextEng.text = tempOptionEng
                        binding.rcLetterSnake?.visibility = View.GONE
                        binding.cvRight.visibility = View.VISIBLE
                        cikl++
                        arrayPosStop.clear()
                        adapterSnake?.updateAllArrayPosVis(arrayPosForVisibility)
                        arrayLetterRightAnswerSnake.clear()
                        arrayRightAnswer.clear()
                        arrayTempPosGreen()
                    } else {
                        for (char in 0..24) { // наполняем массив цифрами
                            arrayPosForVisibility.add(0)
                        }
                        arrayPosStop.clear()
                        adapterSnake?.updateAllArrayPosVis(arrayPosForVisibility)
                        arrayLetterRightAnswerSnake.clear()
                        arrayTempPosGreen()
                        arrayTempPosGreenTwo.clear()
                    }
                }
            }
            return@setOnTouchListener false
        }
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
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance(closeResultEnter:String?, resultEnter: String?, numTask: Int?,id: Int?, lockCollect:Int?, lockSnake: Int?) = FragGameSnakeWord(closeResultEnter = closeResultEnter, resultEnter = resultEnter, numTask = numTask, id = id, lockCollect = lockCollect, lockSnake = lockSnake)
    }



    override fun addLetterTextSnakeItem(letter: String) {
        arrayLetterRightAnswerSnake.add(letter)
    }

    fun shuffleArrayAnswerWord(list: List<TaskListItem>):List<TaskListItem>{
        val shuffleTemp: List<TaskListItem> = list.shuffled() as List<TaskListItem>
        return shuffleTemp
    }

    override fun onFragClose() {
        showInterAd()
    }

    override fun returnGame(newList: ArrayList<String>?, itListForReturn: List<TaskListItem>) {
        cikl = 0
        dataModel.scoreForFragGame.value = cikl
        cont = 0
        binding.tvHu.text = cikl.toString()
        newList?.clear()
        itList = itListForReturn
        itList = shuffleArrayAnswerWord(itList)
            for (i in itList.indices) { // перебираем таблицу и записываем в newList русские слова
                newList?.add(itList.get(i).word)
            }
        binding.cvRight?.visibility = View.GONE
        binding.tvVopros2?.text =
            newList!![cikl] //передемв текствью задания русское слово с 0 позиции
        indVw =
            newList.indexOf(binding.tvVopros2?.text.toString()) //получаем индекс позиции русского слова в newList
        answer = itList.get(indVw).rightAnswer // получаем ответ
        tempOptionRus = itList.get(indVw).optionInTheTextRus //получаем контекст в рус варианте
        tempOptionEng = itList.get(indVw).optionInTheTextEng //получаем контекст в анг варианте
        arrayLetterAnswerNull.clear()
        arrayLetterAnswerAbc.clear()
        arrayLetterAnswerAbcShuffle.clear()
        arrayLetterAnswerPos.clear()
        arrayLetterAnswer.clear()
        arrayTempPosGreen()
        adapterSnake?.updateAdapter(arrayLetterAnswerNull, arrayPosForVisibility)
        snakeAddLetterArray()
        binding.rcLetterSnake?.visibility = View.VISIBLE
    }

    fun initBack(){ //инициализируем и переназначаем кнопку телефона "назад"
        val callback = requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dialogEndPositive.showExitQuestion(activity as AppCompatActivity)
                }
            })
    }

    override fun startGameCollectEndDialog() {
        TODO("Not yet implemented")
    }

    override fun startGameSnakeEndDialog() {
        TODO("Not yet implemented")
    }

}