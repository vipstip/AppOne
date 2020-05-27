package com.example.appone;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import org.prebid.mobile.AdUnit;
import org.prebid.mobile.Host;
import org.prebid.mobile.InterstitialAdUnit;
import org.prebid.mobile.OnCompleteListener;
import org.prebid.mobile.PrebidMobile;
import org.prebid.mobile.ResultCode;
import org.prebid.mobile.VideoInterstitialAdUnit;

public class Interstitial {
    private int refreshCount = 0;

    private ResultCode resultCode;
    private AdUnit adUnit;

    private AdSize adsize;
    private Context context;
    private int autoRefresh = 30000;

    private String adUnitID;
    private String placement = "";
    private AdListeners adListeners;
    private TypeAd typeAd = TypeAd.VIDEO;

    private PublisherInterstitialAd amInterstitial;
    private PublisherAdRequest request;

    private Constants constants;

    private int CountFailedLoad = 0;
    private CheckOnComplete checkOnComplete;

    public void loadAd() {
        stopAutoRefresh();
        CountFailedLoad = 0;
        if (constants.isOnCompleteConfig()){
            setUpConfigAndLoad();
        } else {
            checkOnComplete.start();
        }
    }
    private void setUpConfigAndLoad(){
        SetupPB();
        setAdUnit();
        setupAMInterstitial();
        loadInterstitial();
    }

    private void SetupPB(){
        Host.CUSTOM.setHostUrl("https://pb-server.vliplatform.com/openrtb2/auction");
        if (typeAd == TypeAd.VIDEO){
            PrebidMobile.setPrebidServerHost(constants.VD_HOST);
            PrebidMobile.setPrebidServerAccountId(constants.VD_PBS_ACCOUNT_ID_APPNEXUS);
//            PrebidMobile.setStoredAuctionResponse(constants.VD_STORED_AUCTION_RESPONSE_CONFIG);
            Log.e("constants.VD_HOST",constants.VD_HOST + " ");
            Log.e("constants.VD_PBS_ACC",constants.VD_PBS_ACCOUNT_ID_APPNEXUS + " ");
            Log.e("constants.VD_STORED",constants.VD_STORED_AUCTION_RESPONSE_CONFIG + " ");
        } else  {
            PrebidMobile.setPrebidServerHost(constants.HOST);
            PrebidMobile.setPrebidServerAccountId(constants.PBS_ACCOUNT_ID_APPNEXUS);
            PrebidMobile.setStoredAuctionResponse(constants.STORED_AUCTION_RESPONSE_CONFIG);
        }
    }

    public void setTypeAd(TypeAd typeAd) {
        this.typeAd = typeAd;
    }

    public void setMillisAutoRefres(int autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    public void setSize(int with, int height){
        this.adsize = new AdSize(with,height);
    }

    public void setAdUnit(String adUnitID){
        this.adUnitID = adUnitID;
    }

    public void setPlacement(String placement){
        if (this.placement.equals(placement)){
            Log.d("Placement"," Duplicate placement!");
        } else {
            this.placement = placement;
            constants.getConfigOfPlacement(placement,context);
        }
    }

    public void setAdUnit(){
        if (adsize != null && typeAd != TypeAd.VIDEO)
        {
            this.adUnit = new InterstitialAdUnit(constants.VD_PUB_ADUNIT_ID,adsize.getWidth(),adsize.getHeight());
            Log.e("constants.VD_DFP_ADUNIT",constants.VD_DFP_ADUNIT_ID_Prebid + " ");
        }
        else if (typeAd == TypeAd.VIDEO){
            this.adUnit = new VideoInterstitialAdUnit(constants.VD_PUB_ADUNIT_ID);
            Log.e("constants.VD_DFP_ADUNIT",constants.VD_DFP_ADUNIT_ID_Prebid + " ");
        }
        else this.adUnit = new InterstitialAdUnit(constants.PUB_ADUNIT_ID);
    }

    public Interstitial(Context context){
        this.context = context;
        checkOnComplete = new CheckOnComplete(30000,3000);
        constants = new Constants();
    }

    private void setupAMInterstitial() {
        this.amInterstitial = new PublisherInterstitialAd(context);
        if (typeAd == TypeAd.VIDEO){
            amInterstitial.setAdUnitId(constants.VD_DFP_ADUNIT_ID_Prebid);
        } else  amInterstitial.setAdUnitId(constants.DFP_ADUNIT_ID_Prebid);

        amInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                CountFailedLoad = 0;
                amInterstitial.show();
                adUnit.stopAutoRefresh();
                Log.e("Loaded", " OK ");
                adListeners.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adListeners.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListeners.onAdClosed();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adListeners.onAdImpression();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                adListeners.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                adListeners.onAdOpened();
            }


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adUnit.stopAutoRefresh();
                if (CountFailedLoad < 3){
                    loadInterstitial();
                    CountFailedLoad++;
                }
                if (typeAd == TypeAd.VIDEO) {
                    typeAd = TypeAd.BANNER;
                    loadAd();
                }
                Log.e("MyTag", "ok" + CountFailedLoad);
                adListeners.onAdFailedToLoad(i);
            }
        });
    }
    private void loadInterstitial() {
        adUnit.setAutoRefreshPeriodMillis(autoRefresh);
        PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();
        request = builder.build();
        adUnit.fetchDemand(request, new OnCompleteListener() {
            @Override
            public void onComplete(ResultCode resultCode) {
                Interstitial.this.resultCode = resultCode;
                amInterstitial.loadAd(request);
                refreshCount++;
            }
        });
    }

    public void setAdlistenners(final AdListeners adlistenners){
        this.adListeners = adlistenners;
    }

    public void stopAutoRefresh() {
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
        }
    }

    public void startAutoRefresh(){
        loadInterstitial();
    }

    public void desTroy(){
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
            adUnit = null;
        }
    }

    class CheckOnComplete extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CheckOnComplete(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (constants.isOnCompleteConfig()){
                onFinish();
                cancel();
            }
        }

        @Override
        public void onFinish() {
            if (constants.isOnCompleteConfig()){
                setUpConfigAndLoad();
            } else
                Log.e("Err","Fetch config false!");
        }
    }

}
