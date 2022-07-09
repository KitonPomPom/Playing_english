package kitonpompom.playing_english.activities

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.R
import kitonpompom.playing_english.data.DataModel
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.FragmentFragGameBinding
import kitonpompom.playing_english.dialogs.DialogFragGameEnd
import kitonpompom.playing_english.dialogs.DialogFragGameEndPositive
import kitonpompom.playing_english.dialogs.OneShowAdsInterfase
import kitonpompom.playing_english.entities.TaskListItem
import kitonpompom.playing_english.frag.*
import kitonpompom.playing_english.utils.ConstResult
import kotlin.collections.ArrayList


class FragGame(private val closeResultEnter: String?,private  val resultEnter: String?, private val numTask :Int?, private val id : Int?, private val lockCollect: Int?, private val lockSnake: Int?) : BaseFragmentAds(), FragmentCloseInterface, FragmentReturnInterface, OneShowAdsInterfase, InterAdsClose, StartGameEndDialog {

    lateinit var binding: FragmentFragGameBinding
    var itList: List<TaskListItem> = listOf()
    var newList: ArrayList<String> = ArrayList()
    var arrayAnswer1: ArrayList<String> = ArrayList()
    var arrayAnswertemp: ArrayList<String> = ArrayList()
    lateinit var ed: EditText
    var tempADSbtVis = 0

    var tempAppReview = 0 // для одноразогово показа предложения оценки игры
    private val dataModel: DataModel by activityViewModels()
    var max = 5
    var cikl: Int = 0
    var resultOptions: Int = 0
    var resultInput: Int = 0
    val dialogNeg = DialogFragGameEnd(this,this,this)
    val dialogPos = DialogFragGameEndPositive(this,this, this)

    val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as AppMainState).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFragGameBinding.inflate(layoutInflater)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        printAnswer()
        initBack()

        tempADSbtVis = 0 // для однаразового вкл рекламы

        val prBar = binding.progressBar

        dataModel.questionforFragGame.observe(activity as LifecycleOwner,{
            binding.tvZadanie.text = it
        })

        dataModel.scoreForFragGame.observe(activity as LifecycleOwner,{
            ObjectAnimator.ofInt(prBar, "progress", it)
                .setDuration(15)
                .start()
        })

        binding.btOtvet1.setOnClickListener {
            var textButton = binding.btOtvet1.text.toString()
            clickAnswer(textButton)
        }

        binding.btOtvet2.setOnClickListener {
            var textButton = binding.btOtvet2.text.toString()
            clickAnswer(textButton)
        }

        binding.btOtvet3.setOnClickListener{
            var textButton = binding.btOtvet3.text.toString()
            clickAnswer(textButton)
        }
        binding.btOtvet4.setOnClickListener{
            var textButton = binding.btOtvet4.text.toString()
            clickAnswer(textButton)
        }

        binding.btClose.setOnClickListener{
            dialogPos.showExitQuestion(activity as AppCompatActivity)
        }

        binding.btShowAnswer.setOnClickListener{
            closeKeyboard(ed)
            binding.cvEnterAnswer.visibility = View.GONE //прячем кнопку ввода
            binding.cvAnswerOptions.visibility = View.VISIBLE //показываем варианты ответов
        }

        binding.btContinueAnswer.setOnClickListener {
            if (cikl == max - 1) {
                if(resultEnter == ConstResult.LVL) {
                    mainViewModel.updateSelectdedSuccessById(id!!, resultOptions)
                    mainViewModel.updateTypedSuccessById(id!!, resultInput)
                    if (resultInput + resultOptions == max) {
                        mainViewModel.updateLockById(id!! + 1, 1)
                        mainViewModel.updateLockQuestionByNumTask(numTask!! + 1, 1)
                    }
                }
                dialogPos.showEndPositiveDialog(activity as AppCompatActivity, itList ,newList!!, resultInput, resultOptions, lockCollect!!, lockSnake!!) //открываем и отправляем в диалог результаты
                if(id == 5 && resultEnter == ConstResult.LVL && lockCollect == 0 && tempAppReview == 0){
                    tempAppReview = 1
                    inAppReview()
                }
                if(id == 20 && resultEnter == ConstResult.LVL && lockCollect == 0 && tempAppReview == 0){
                    tempAppReview = 1
                    inAppReview()
                }
                if(id == 50 && resultEnter == ConstResult.LVL && lockCollect == 0 && tempAppReview == 0){
                    tempAppReview = 1
                    inAppReview()
                }

            } else {
                if (newList != null) {
                    cikl++
                    binding.tvHu.text = cikl.toString()
                    binding.tvVopros.text = newList[cikl]
                    val indVw = newList.indexOf(binding.tvVopros.text.toString())
                    arrayAnswer1.clear()
                    arrayAnswer1.add(itList.get(indVw).rightAnswer)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption1)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption2)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption3)
                    arrayAnswertemp = copyArray(arrayAnswer1)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextRus)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextEng)
                    shufleArray(arrayAnswer1)
                    dataModel.scoreForFragGame.value = cikl
                }
                binding.cvRight.visibility = View.GONE
                binding.cvEnterAnswer.visibility = View.VISIBLE
                binding.edEnterAnswer.setText("")
            }
        }
    }

    companion object {
        fun newInstance(closeResultEnter:String?,resultEnter: String?, numTask: Int?, id: Int?, lockCollect: Int?, lockSnake:Int? ) = FragGame(closeResultEnter = closeResultEnter,resultEnter = resultEnter, numTask = numTask, id = id, lockCollect = lockCollect, lockSnake = lockSnake)
    }

    fun clickAnswer(textButton:String){ // действие по нажатию на вариант ответа из предоставленых вариантов
        if (textButton == arrayAnswertemp[0]) {
            if (newList != null) {
                resultOptions++
                dataModel.scoreForFragGame.value = cikl
                binding.tvRight.text = arrayAnswer1[0]
                binding.tvOptionTextRus.text = arrayAnswertemp[4]
                binding.tvOptionTextEng.text = arrayAnswertemp[5]
                binding.cvRight.visibility = View.VISIBLE
                binding.cvEnterAnswer.visibility = View.GONE
                binding.edEnterAnswer.setText("")
                binding.cvAnswerOptions.visibility = View.GONE
            }
        }else{
            dialogNeg.showEndDialog(activity as AppCompatActivity, itList, newList!!, tempADSbtVis, arrayAnswertemp[0])
        }
    }

    override fun onFragClose() {
        showInterAd() //открытие рекламы
    }

    override fun returnGame(newList: ArrayList<String>?, itListForReturn: List<TaskListItem>) {
        cikl = 0
        tempADSbtVis = 0
        resultOptions = 0
        resultInput = 0
        binding.tvHu.text = cikl.toString()
        newList?.clear()
        itList = itListForReturn
        itList = shuffleArrayAnswerWord(itList)
        for (i in itList.indices) {
            newList?.add(itList.get(i).word)
        }
        dataModel.scoreForFragGame.value = cikl
        if (newList != null) {
            binding.tvVopros.text = newList[cikl]
            val indVw = newList.indexOf(binding.tvVopros.text.toString())
            arrayAnswer1.clear()
            arrayAnswer1.add(itList.get(indVw).rightAnswer)
            arrayAnswer1.add(itList.get(indVw).suggestedOption1)
            arrayAnswer1.add(itList.get(indVw).suggestedOption2)
            arrayAnswer1.add(itList.get(indVw).suggestedOption3)
            arrayAnswertemp = copyArray(arrayAnswer1)
            arrayAnswertemp.add(itList.get(indVw).optionInTheTextRus)
            arrayAnswertemp.add(itList.get(indVw).optionInTheTextEng)
            shufleArray(arrayAnswer1)
            dataModel.scoreForFragGame.value = cikl
            binding.cvRight.visibility = View.GONE
            binding.cvEnterAnswer.visibility = View.VISIBLE
            binding.edEnterAnswer.setText("")
            binding.cvAnswerOptions.visibility = View.GONE
        }
    }

    private fun closeKeyboard(view: View){ // функция скрытия клавиатуры
        val imm  = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

    private fun printAnswer(){ // функциия ввода ответа
        binding.cvEnterAnswer.visibility = View.VISIBLE
        ed =  binding.edEnterAnswer
        ed.addTextChangedListener { char ->
            if(char!=null) {
                if (arrayAnswertemp[0].lowercase().startsWith(char.toString().lowercase())) {
                    ed.setTextColor(ContextCompat.getColor(activity as Activity ,
                        R.color.grean_star_gradient
                    ))
                    if (arrayAnswertemp[0].lowercase() == (char.toString().lowercase())){
                        binding.cvEnterAnswer.visibility = View.GONE
                        closeKeyboard(ed)
                        binding.tvRight.text = arrayAnswer1[0]
                        binding.cvRight.visibility = View.VISIBLE
                        binding.tvOptionTextRus.text = arrayAnswertemp[4]
                        binding.tvOptionTextEng.text = arrayAnswertemp[5]
                        resultInput++
                        dataModel.resultInputForDialog.value = resultInput
                    }
                }else{
                    ed.setTextColor(ContextCompat.getColor(activity as Activity , R.color.red))
                }
            }
        }
    }

    private fun observer() {
        if(resultEnter == ConstResult.LVL) {
            mainViewModel.getTaskListByNumberTask(numTask!!).observe(viewLifecycleOwner) {
                max = it.size
                binding.progressBar.max = max
                if (newList.isEmpty()) {
                    itList = it
                    itList = shuffleArrayAnswerWord(itList)
                    for (i in itList.indices) {
                        newList.add(itList.get(i).word)
                    }
                    binding.tvVopros.text = newList[cikl]
                    val indVw = newList.indexOf(binding.tvVopros.text.toString())
                    //Log.d("MyLog", "indVw : $indVw")
                    arrayAnswer1.clear()
                    arrayAnswer1.add(itList.get(indVw).rightAnswer)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption1)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption2)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption3)
                    arrayAnswertemp = copyArray(arrayAnswer1)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextRus)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextEng)
                    shufleArray(arrayAnswer1)
                    dataModel.scoreForFragGame.value = cikl
                }
            }
        }else{
            mainViewModel.getTaskListByLockRequest(1).observe(viewLifecycleOwner) {
                binding.idImBrain.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.linHuisHu.visibility = View.VISIBLE
                max = it.size
                binding.tvHuisHu.text = max.toString()
                binding.tvHu.text = cikl.toString()
                binding.progressBar.max = max
                if (newList.isEmpty()) {
                    itList = it
                    itList = shuffleArrayAnswerWord(itList)
                    for (i in itList.indices) {
                        newList.add(itList.get(i).word)
                    }
                    binding.tvVopros.text = newList[cikl]
                    val indVw = newList.indexOf(binding.tvVopros.text.toString())
                    arrayAnswer1.clear()
                    arrayAnswer1.add(itList.get(indVw).rightAnswer)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption1)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption2)
                    arrayAnswer1.add(itList.get(indVw).suggestedOption3)
                    arrayAnswertemp = copyArray(arrayAnswer1)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextRus)
                    arrayAnswertemp.add(itList.get(indVw).optionInTheTextEng)
                    shufleArray(arrayAnswer1)
                    dataModel.scoreForFragGame.value = cikl
                }
            }
        }
    }

    fun shufleArray(list: ArrayList<String>){ // перемешиваем массив с вариантами ответов
        val shufle = list.shuffled()
        binding.btOtvet1.text = shufle[0]
        binding.btOtvet2.text = shufle[1]
        binding.btOtvet3.text = shufle[2]
        binding.btOtvet4.text = shufle[3]
    }

    fun shuffleArrayAnswerWord(list: List<TaskListItem>):List<TaskListItem>{
        val shuffleTemp: List<TaskListItem> = list.shuffled() as List<TaskListItem>
        return shuffleTemp
    }


    fun copyArray(list:ArrayList<String>): ArrayList<String>{
       return list.clone() as ArrayList<String>
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
            if(resultEnter == ConstResult.LVL) {
                if (id != null) {
                    mainViewModel.updateTypedSuccessById(
                        id,
                        resultInput
                    ) // отправляем в БД по закрытию количество введенных
                    mainViewModel.updateSelectdedSuccessById(
                        id,
                        resultOptions
                    ) // отправляем в БД по закрытию количество угаданых
                }
                if(closeResultEnter == ConstResult.INPUT_FRAG_LVL) {
                    activity?.supportFragmentManager?.popBackStack()
                }else{
                    activity?.supportFragmentManager?.popBackStack("fragLvl",0)
                }
            }else{
                activity?.supportFragmentManager?.beginTransaction()?.remove(this@FragGame)
                   ?.commit() // функция закрытия/сварачивания фрагмента
            }
    }

    fun initBack(){ //инициализируем и переназначаем кнопку телефона "назад"
            val callback = requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        //activity?.supportFragmentManager?.popBackStack("fragLvl", 0)
                        dialogPos.showExitQuestion(activity as AppCompatActivity)
                    }
                })
    }

    override fun adsOnCloseUpdateInterAd() { //функция отвечает за загрузку рекламы один раз
        if(resultEnter != ConstResult.ARCADE) {
            tempADSbtVis = 1
        }
    }

    override fun updateLockGame() {
        if(resultEnter == ConstResult.LVL) {
            mainViewModel.updateLockCollectById(id!!, 1)
        }
    }

    override fun OneShowInterAds() { //функция отвечает за загрузку рекламы один раз
        showInterAdUpdateInterAd()
    }

    override fun startGameCollectEndDialog() {
        FragmentManagerAds.setFragment(FragGameCollectWord.newInstance(ConstResult.INPUT_FROM_TRANSLIT,ConstResult.LVL, numTask,id, lockCollect!!,lockSnake!!),
            "fragGameCollectWord", activity as AppCompatActivity)
    }

    override fun startGameSnakeEndDialog() {
        FragmentManagerAds.setFragment(FragGameSnakeWord.newInstance(ConstResult.INPUT_FROM_TRANSLIT,ConstResult.LVL, numTask,id, lockCollect!!, lockSnake!!),
            "fragGameSnakeWord", activity as AppCompatActivity)
    }

    fun inAppReview() {
        val reviewManager = ReviewManagerFactory.create(activity as AppCompatActivity)
        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = request.result
                val flow = reviewManager.launchReviewFlow(activity as AppCompatActivity, reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            } else {
                Toast.makeText(activity as AppCompatActivity, "Error", Toast.LENGTH_LONG).show()
                // There was some problem, continue regardless of the result.
            }
        }
    }
}