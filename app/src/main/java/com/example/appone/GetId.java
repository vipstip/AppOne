package com.example.appone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GetId {
    private static String url = "https://api.github.com/";
    private Context context;
    private static HashMap<String,String> configbanner = new HashMap<>();
    private static HashMap<String,String> configintertitial = new HashMap<>();
    private static HashMap<String,Integer> placement = new HashMap<>();
    private static boolean checkConfigResponse = false;
    public GetId(Context context) {
        this.context = context;
        URL url1 = null;
        try {
            url1 = new URL(url);
            if (isConnected(url1)){
                Log.e("BBBBB","OK");
                new yourDataTask().execute(url);
            } else {
                Log.e("CCCCC","OK");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                new yourDataTask().execute(url);
                            }
                        },
                        5000
                );
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private static class yourDataTask extends AsyncTask<String, Void, String> {

        yourDataTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            final StringBuilder content = new StringBuilder();


            try {
                URL url1 = new URL(strings[0]);
                readContent(url1, content);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return "[{\"placement\":\"banner1\",\"type\":\"banner\",\"defaultType\":\"vast\",\"isActive\":true,\"adInfor\":[{\"isVideo\":true,\"host\":{\"pbHost\":\"Rubicon\",\"pbAccountId\":\"1001\",\"storedAuctionResponse\":\"sample_video_response\"},\"configId\":\"1001-1\",\"adUnitID\":\"/5300653/test_adunit_vast_pavliuchyk\"},{\"isVideo\":false,\"host\":{\"pbHost\":\"Appnexus\",\"pbAccountId\":\"bfa84af2-bd16-4d35-96ad-31c6bb888df0\",\"storedAuctionResponse\":\"\"},\"configId\":\"625c6125-f19e-4d5b-95c5-55501526b2a4\",\"adUnitID\":\"/19968336/PrebidMobileValidator_Banner_All_Sizes\"}]},{\"placement\":\"banner2\",\"type\":\"banner\",\"defaultType\":\"html\",\"isActive\":true,\"adInfor\":[{\"isVideo\":true,\"host\":{\"pbHost\":\"Custom\",\"pbAccountId\":\"vli_banner\",\"storedAuctionResponse\":\"\"},\"configId\":\"tag_video\",\"adUnitID\":\"/21766281334/Video_Discovery\"},{\"isVideo\":false,\"host\":{\"pbHost\":\"Custom\",\"pbAccountId\":\"vli_banner\",\"storedAuctionResponse\":\"\"},\"configId\":\"tag_banner\",\"adUnitID\":\"/19968336/PrebidMobileValidator_Banner_All_Sizes\"}]},{\"placement\":\"full1\",\"type\":\"interstitial\",\"defaultType\":\"vast\",\"isActive\":true,\"adInfor\":[{\"isVideo\":true,\"host\":{\"pbHost\":\"Rubicon\",\"pbAccountId\":\"1001\",\"storedAuctionResponse\":\"sample_video_response\"},\"configId\":\"1001-1\",\"adUnitID\":\"/5300653/test_adunit_vast_pavliuchyk\"},{\"isVideo\":false,\"host\":{\"pbHost\":\"Appnexus\",\"pbAccountId\":\"bfa84af2-bd16-4d35-96ad-31c6bb888df0\",\"storedAuctionResponse\":\"\"},\"configId\":\"625c6125-f19e-4d5b-95c5-55501526b2a4\",\"adUnitID\":\"/19968336/PrebidMobileValidator_Banner_All_Sizes\"}]},{\"placement\":\"full2\",\"type\":\"interstitial\",\"defaultType\":\"vast\",\"isActive\":true,\"adInfor\":[{\"isVideo\":true,\"host\":{\"pbHost\":\"Custom\",\"pbAccountId\":\"vli_video\",\"storedAuctionResponse\":\"\"},\"configId\":\"tag_video\",\"adUnitID\":\"/21766281334/Video_Discovery\"},{\"isVideo\":false,\"host\":{\"pbHost\":\"Custom\",\"pbAccountId\":\"tag_video\",\"storedAuctionResponse\":\"\"},\"configId\":\"tag_video\",\"adUnitID\":\"/19968336/PrebidMobileValidator_Banner_All_Sizes\"}]}]";
        }

        @Override
        protected void onPostExecute(final String response) {
            super.onPostExecute(response);
            getJson(response);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }

    public HashMap<String,String> getConfigbanner(){
        return configbanner;
    }

    public HashMap<String,String> getConfigintertitial(){
        return configintertitial;
    }

    public HashMap<String, Integer> getPlacement() {
        return placement;
    }

    public boolean isCheckConfigResponse() {
        return checkConfigResponse;
    }

    private static void readContent(URL url, StringBuilder content){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                content.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getJson(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject mainConfig = jsonArray.getJSONObject(i);
                    String type = mainConfig.getString("type");
                    placement.put(mainConfig.getString("placement"),i);
                    if (type.equals("banner")){

                        configbanner.put("placement"+i,mainConfig.getString("placement"));
                        configbanner.put("defaultType"+i,mainConfig.getString("defaultType"));
                        configbanner.put("isActive"+i,mainConfig.getString("isActive"));

                        JSONArray adInfor = mainConfig.getJSONArray("adInfor");

                        for (int j = 0; j < adInfor.length(); j++){

                            JSONObject config = adInfor.getJSONObject(j);

                            String isVideo = config.getString("isVideo");

                            JSONObject host = config.getJSONObject("host");

                            if (isVideo.equals("true")){

                                configbanner.put("VDpbHost"+i,host.getString("pbHost"));
                                configbanner.put("VDpbAccountId"+i,host.getString("pbAccountId"));
                                configbanner.put("VDstoredAuctionResponse"+i,host.getString("storedAuctionResponse"));
                                configbanner.put("VDconfigId"+i,config.getString("configId"));
                                configbanner.put("VDadUnitID"+i,config.getString("adUnitID"));

                            }else {

                                configbanner.put("pbHost"+i,host.getString("pbHost"));
                                configbanner.put("pbAccountId"+i,host.getString("pbAccountId"));
                                configbanner.put("storedAuctionResponse"+i,host.getString("storedAuctionResponse"));
                                configbanner.put("configId"+i,config.getString("configId"));
                                configbanner.put("adUnitID"+i,config.getString("adUnitID"));

                            }
                        }
                    } else if (type.equals("interstitial")){

                        configintertitial.put("placement"+i,mainConfig.getString("placement"));
                        configintertitial.put("defaultType"+i,mainConfig.getString("defaultType"));
                        configintertitial.put("isActive"+i,mainConfig.getString("isActive"));

                        JSONArray adInfor = mainConfig.getJSONArray("adInfor");

                        for (int j = 0; j < adInfor.length(); j++){

                            JSONObject config = adInfor.getJSONObject(j);

                            String isVideo = config.getString("isVideo");

                            JSONObject host = config.getJSONObject("host");

                            if (isVideo.equals("true")){

                                configintertitial.put("VDpbHost"+i,host.getString("pbHost"));
                                configintertitial.put("VDpbAccountId"+i,host.getString("pbAccountId"));
                                configintertitial.put("VDstoredAuctionResponse"+i,host.getString("storedAuctionResponse"));
                                configintertitial.put("VDconfigId"+i,config.getString("configId"));
                                configintertitial.put("VDadUnitID"+i,config.getString("adUnitID"));

                            }else {

                                configintertitial.put("pbHost"+i,host.getString("pbHost"));
                                configintertitial.put("pbAccountId"+i,host.getString("pbAccountId"));
                                configintertitial.put("storedAuctionResponse"+i,host.getString("storedAuctionResponse"));
                                configintertitial.put("configId"+i,config.getString("configId"));
                                configintertitial.put("adUnitID"+i,config.getString("adUnitID"));

                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (configbanner != null && configintertitial != null){
                checkConfigResponse = true;
            }

        } catch (JSONException e) {
            Log.e("Err",e+"");
        }
    }

    public boolean isConnected(URL url) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                // Network is available but check if we can get access from the network
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("Connection", "close");
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
