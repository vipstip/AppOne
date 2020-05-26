/*
 *    Copyright 2018-2019 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.appone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;

import org.prebid.mobile.Host;

import java.net.MalformedURLException;
import java.net.URL;

class Constants {
    private GetId getId;
    private Context context;
    private boolean checkInternet;

    private String url = "https://api.github.com/";

    private TimeoutCountDownTimerGetConfig timeoutCountDownTimerGetConfig;

    private String placement;

    private Integer key;
    private String type;

    private boolean onCompleteConfig = false;


    public Constants() {
    }

    public void getConfigOfPlacement(String placement,Context context){
        this.placement = placement;
        this.context = context;
        onCompleteConfig = false;
        getId = new GetId();
        try{
            if (getId.isCheckConfigResponse()){
                setConfig();
                onCompleteConfig = true;
            }
            else {
                getId.startGetConfig();
                TimeoutCountDownTimerCheckInternet timeoutCountDownTimerCheckInternet = new TimeoutCountDownTimerCheckInternet(30000, 3000);
                timeoutCountDownTimerCheckInternet.start();

                timeoutCountDownTimerGetConfig = new TimeoutCountDownTimerGetConfig(30000,3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @org.jetbrains.annotations.Nullable
    private String getConfigWithKey(String keyword){
        if (type.equals("banner")){
            return getId.getConfigbanner().get(keyword+key);
        } else if (type.equals("interstitial")){
            return getId.getConfigintertitial().get(keyword+key);
        }
        return null;
    }


    public boolean isOnCompleteConfig() {
        return onCompleteConfig;
    }

    //AppNexus
    // Prebid server config ids
    public String PBS_ACCOUNT_ID_APPNEXUS;
    public Host HOST;
    public String PUB_ADUNIT_ID;
    public String DFP_ADUNIT_ID_Prebid;
    public String STORED_AUCTION_RESPONSE_CONFIG;

    public String VD_PBS_ACCOUNT_ID_APPNEXUS;
    public Host VD_HOST;
    public String VD_PUB_ADUNIT_ID;
    public String VD_DFP_ADUNIT_ID_Prebid;
    public String VD_STORED_AUCTION_RESPONSE_CONFIG;

    public String DFP_IN_BANNER_NATIVE_ADUNIT_ID_APPNEXUS;



    class TimeoutCountDownTimerCheckInternet extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeoutCountDownTimerCheckInternet(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            URL url1 = null;
            try {
                url1 = new URL(url);
                if (isConnected()){
                    checkInternet = true;
                    Log.d("Connection","Start get config from Server");
                    onFinish();
                    cancel();
                } else
                {
                    checkInternet = false;
                    Log.d("Connection","No Internet");
                }
            } catch ( MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            if (!checkInternet){
                Log.e("Time Out","Internet no connection");
            }
            else {
                timeoutCountDownTimerGetConfig.start();
            }
        }
    }

    class TimeoutCountDownTimerGetConfig extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeoutCountDownTimerGetConfig(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (getId.isCheckConfigResponse()){
                onFinish();
                cancel();
            } else Log.d("Getting config","Waiting get config from Server");
        }

        @Override
        public void onFinish() {
            if (!getId.isCheckConfigResponse()){
                Log.d("Time out","Haven't got config from Server");
            } else {
                Log.d("Getting config","Success");
                setConfig();
                onCompleteConfig = true;
            }
        }
    }

    private void setConfig(){
        key = getId.getPlacement().get(placement);
        type = getId.getType().get(placement);

        PBS_ACCOUNT_ID_APPNEXUS = getConfigWithKey("pbAccountId");
        DFP_ADUNIT_ID_Prebid = getConfigWithKey("adUnitID");
        PUB_ADUNIT_ID = getConfigWithKey("configId");
        switch (getConfigWithKey("pbHost")) {
            case "Custom":
                HOST = Host.CUSTOM;
                break;
            case "Appnexus":
                HOST = Host.APPNEXUS;
                break;
            case "Rubicon":
                HOST = Host.RUBICON;
                break;
        }
        STORED_AUCTION_RESPONSE_CONFIG = getConfigWithKey("storedAuctionResponse");

        VD_PBS_ACCOUNT_ID_APPNEXUS = getConfigWithKey("VDpbAccountId");
        VD_DFP_ADUNIT_ID_Prebid = getConfigWithKey("VDadUnitID");
        VD_PUB_ADUNIT_ID = getConfigWithKey("VDconfigId");
        switch (getConfigWithKey("VDpbHost")) {
            case "Custom":
                VD_HOST = Host.CUSTOM;
                break;
            case "Appnexus":
                VD_HOST = Host.APPNEXUS;
                break;
            case "Rubicon":
                VD_HOST = Host.RUBICON;
                break;
        }

        VD_STORED_AUCTION_RESPONSE_CONFIG = getConfigWithKey("VDstoredAuctionResponse");
    }

    private boolean isConnected() {
            ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connect != null)
            {
                NetworkInfo[] information = connect.getAllNetworkInfo();
                if (information != null)
                    for (int x = 0; x < information.length; x++)
                        if (information[x].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }
            }
            return false;
    }


}
