package kitonpompom.playing_english.frag


import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kitonpompom.playing_english.R

abstract class BaseFragmentAds: Fragment(), InterAdsClose {
    var adView: AdView? = null
    var interAd: InterstitialAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAd()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

    private fun initAds(){
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.ad_Inter_ID), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad
                }
            })
    }

    fun showInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() { // действие если закрыть рекламу на крестик
                    adsOnClose()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) { //действие если ошибка
                    adsOnClose()
                }
            }
            interAd?.show(context as Activity)
        }else{
            adsOnClose()
        }
    }

    fun showInterAdTwo(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() { // действие если закрыть рекламу на крестик
                    adsOnCloseTwo()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) { //действие если ошибка
                    adsOnCloseTwo()
                }
            }
            interAd?.show(context as Activity)
        }else{
            adsOnCloseTwo()
        }
    }

    fun showInterAdUpdateInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {// действие если закрыть рекламу на крестик
                    adsOnCloseUpdateInterAd()
                    interAd = null
                    loadInterAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) { //действие если ошибка
                    adsOnCloseUpdateInterAd()
                }
            }
            interAd?.show(context as Activity)
        }else{
            adsOnCloseUpdateInterAd()
        }
    }
}