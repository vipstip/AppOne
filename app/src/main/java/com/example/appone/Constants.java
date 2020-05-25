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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

class Constants {
    private GetId getId;
    private Context context;
    private boolean checkInternet;

    private String url = "https://api.github.com/";

    private TimeoutCountDownTimerGetConfig timeoutCountDownTimerGetConfig;

    private static HashMap<String,String> configintertitial = new HashMap<>();
    private static HashMap<String,Integer> placement = new HashMap<>();

    public Constants(Context context) {
        getId = new GetId(context);
        this.context = context;
        TimeoutCountDownTimerCheckInternet timeoutCountDownTimerCheckInternet = new TimeoutCountDownTimerCheckInternet(3000, 500);
//        timeoutCountDownTimerCheckInternet.start();

        placement = getId.getPlacement();
        configintertitial = getId.getConfigintertitial();
        Integer key;
        key = placement.get("full1");

        Log.e("Keyword",key+" ");
        Log.e("Keyword",configintertitial.toString()+" ");
        timeoutCountDownTimerGetConfig = new TimeoutCountDownTimerGetConfig(30000,5000);
        try {
            URL abc = new URL(url);
            if (isConnected(abc)){
                Log.e("isConnected","OK");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    //AppNexus
    // Prebid server config ids
//    private static final String PBS_ACCOUNT_ID_APPNEXUS = "e8df28e7-78ff-452d-b3af-ff4df83df832";
    private static final String PBS_ACCOUNT_ID_APPNEXUS = "bfa84af2-bd16-4d35-96ad-31c6bb888df0";
    // DFP ad unit ids
//    private static final String DFP_ADUNIT_ID_Prebid = "/307492156/Prebid_Display";
    private static final String DFP_ADUNIT_ID_Prebid = "/5300653/test_adunit_vast_pavliuchyk";
//    private static final String DFP_ADUNIT_ID_Prebid = "/21766281334/Video_Discovery";
    private static final String DFP_IN_BANNER_NATIVE_ADUNIT_ID_APPNEXUS = "/19968336/Wei_Prebid_Native_Test";

    static String PBS_ACCOUNT_ID = PBS_ACCOUNT_ID_APPNEXUS;
    // DFP ad unit ids
    static String DFP_ADUNIT_ID = DFP_ADUNIT_ID_Prebid;
    static String DFP_BANNER_NATIVE_ADUNIT_ID = DFP_IN_BANNER_NATIVE_ADUNIT_ID_APPNEXUS;

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
                if (isConnected(url1)){
                    checkInternet = true;
                    Log.e("Connection","Ok");
                    onFinish();
                    cancel();
                } else
                {
                    checkInternet = false;
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
            } else Log.d("Getting config","Waiting get config from Server");
        }

        @Override
        public void onFinish() {
            if (!getId.isCheckConfigResponse()){
                Log.d("Time out","Haven't got config from Server");
            } else {
                placement = getId.getPlacement();
                configintertitial = getId.getConfigintertitial();
                Integer key;
                key = placement.get("full1");

                Log.e("Keyword",key+" ");
                Log.e("Keyword",configintertitial.toString()+" ");
            }
        }
    }

    public boolean isConnected(URL url) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                // Network is available but check if we can get access from the network
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("Connection", "start get config");
                urlc.setConnectTimeout(3000); // Timeout 2 seconds.
                urlc.connect();

                if (urlc.getResponseCode() == 200) // Successful response.
                {
                    return true;
                } else {
                    Log.d("NO INTERNET", "NO INTERNET");
                    return false;
                }
            } else {
                Log.d("NO INTERNET Connection", "NO INTERNET Connection");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
