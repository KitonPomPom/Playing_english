package kitonpompom.playing_english.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kitonpompom.playing_english.AppMainState
import kitonpompom.playing_english.R
import kitonpompom.playing_english.data.DataModel
import kitonpompom.playing_english.data.MainViewModel
import kitonpompom.playing_english.databinding.ActivityMainBinding
import kitonpompom.playing_english.dialogs.DialogCollectGame
import kitonpompom.playing_english.dialogs.InterfaceDialogCollectGame
import kitonpompom.playing_english.frag.FragmentCloseInterface
import kitonpompom.playing_english.frag.FragmentManagerAds
import kitonpompom.playing_english.utils.ConstResult
import kitonpompom.playing_english.utils.Qustions_helper


class MainActivity : AppCompatActivity(), FragmentCloseInterface, InterfaceDialogCollectGame {

    lateinit var rootElement: ActivityMainBinding
    var testList: ArrayList<String> = ArrayList()

    var pref: SharedPreferences? = null
    var tempStart: Int = 0
    var openWord: Int = 0
    var openWordForDialog = "Открыто слов: "
    private val dataModel: DataModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels() {
        MainViewModel.MainViewModelFactory((this.applicationContext as AppMainState).database)

    }

    private var consentInformation: ConsentInformation? = null
    private var consentForm: ConsentForm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        setContentView(rootElement.root)



        // Установите тег для несовершеннолетнего согласия. false означает, что пользователи не являются несовершеннолетними.
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val consentInformationTemp = consentInformation
        //Smartcast на "Информацию о согласии!" невозможен, потому что "Информация о согласии"
        // является изменяемым свойством, которое могло быть изменено к этому времени
        consentInformationTemp?.requestConsentInfoUpdate(
            this,
            params,
            {
               // Toast.makeText(this, "Проверяем доступна ли форма", Toast.LENGTH_LONG).show()
                // Состояние информации о согласии было обновлено.
                // Теперь вы готовы проверить, доступна ли форма.
                if (consentInformationTemp.isConsentFormAvailable) {
                    loadForm();
                }
            },
            {
                // Обработайте ошибку.
                //Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
            })




        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        (application as AppMainState).showAdIfAvailable(this) {
        }

        val dialogCollectGame = DialogCollectGame(this)

        observer()
        pref = getSharedPreferences("SAVESTART", Context.MODE_PRIVATE)
        tempStart = pref?.getInt("start", 0)!!
        if (tempStart == 0) {
            tempStart = 1
            supportFragmentManager.beginTransaction()
                .replace(R.id.id_frl_zad, FragStartInf.newInstance()).commit()
            Qustions_helper.getAllInfLev(this, mainViewModel)
            saveDataStart(tempStart)
        }

        rootElement.btOpenLvlFrag.setOnClickListener() {
            startFragLvl()
        }

        rootElement.btArcade.setOnClickListener() {
            openWordForDialog = "Играть со всеми открытыми словами"
            dialogCollectGame.showSelectedGame(this, openWord, 0, openWordForDialog, 2, 2)
        }

    }

    fun startFragLvl() {
        FragmentManagerAds.setFragment(FragLvl.newInstance(), "fragLvl", this)
        val zadLevel1 = "Переведите слово"
        dataModel.questionforFragGame.value = zadLevel1
    }

    fun saveDataStart(res: Int) {
        val editor = pref?.edit()
        editor?.putInt("start", res)
        editor?.apply()
    }


    override fun onFragClose() {
    }

    fun observer() {
        mainViewModel.allLevelList.observe(this) {
            var testlock: ArrayList<String> = ArrayList()
            for (i in it.indices) {
                testlock.add(it.get(i).lock.toString())
            }
        }

        mainViewModel.getTaskListByLockRequest(1).observe(this) {
            openWord = it.size
            rootElement.tvResultOpenWord.text = "Открыто слов: $openWord"
            for (i in it.indices) {
                testList.add(it.get(i).word)
            }
        }
    }

    override fun updateLockGame() {
        TODO("Not yet implemented")
    }

    override fun dialogStartQuizGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(
            FragGame.newInstance(
                ConstResult.INPUT_FRAG_LVL,
                ConstResult.ARCADE,
                numTask,
                id,
                lockCollect,
                lockSnake
            ), "arcadeGame", this
        )
    }

    override fun dialogStartCollectGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(
            FragGameCollectWord.newInstance(
                ConstResult.INPUT_FRAG_LVL,
                ConstResult.ARCADE,
                numTask,
                id,
                lockCollect,
                lockSnake
            ), "fragGameCollectWord", this
        )
    }

    override fun dialogStartSnakeGame(numTask: Int, id: Int, lockCollect: Int, lockSnake: Int) {
        FragmentManagerAds.setFragment(
            FragGameSnakeWord.newInstance(
                ConstResult.INPUT_FRAG_LVL,
                ConstResult.ARCADE,
                numTask,
                id,
                lockCollect,
                lockSnake
            ), "fragGameSnakeWord", this
        )
    }

    /*fun loadForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm -> this.consentForm = consentForm }
        ) {
            // Handle the error
            Toast.makeText(this, "Форма не загрузилась", Toast.LENGTH_LONG).show()
        }
    }*/

    fun loadForm() { // Загрузка формы для согласия на рекламу GDPR для EC
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                this@MainActivity.consentForm = consentForm
                if (consentInformation!!.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(
                        this@MainActivity
                    ) { // Handle dismissal by reloading form.
                        loadForm()
                    }
                }
            }
        ) {
            /// Handle Error.
        }
    }

}