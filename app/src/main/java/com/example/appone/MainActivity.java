package com.example.appone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnHit;
    TextView txtJson,txtJson2;
    ProgressDialog pd;
    Button button,button2;
    String id1,id2;
    FrameLayout adView,adView2;
    private int refreshCount = 0;

    Banner banner;
    Interstitial interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btnAd);
        button2 = (Button) findViewById(R.id.btnAd2);
        adView = (FrameLayout)findViewById(R.id.adView);
        adView2 = (FrameLayout)findViewById(R.id.adView2);
        banner = new Banner(this);
        banner.setSize(AdSizes.MEDIUM_RECTANGLE);
        banner.setPlacement("banner1");
//
        interstitial = new Interstitial(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            banner.loadAd(adView);
                interstitial.setPlacement("full2");
                interstitial.loadAd();
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner.loadAd(adView2);
//                try {
//                    Integer key = getId.getPlacement().get("full1");
//                    Log.e("key",key + " ");
//
//                    keyword = getId.getConfigintertitial();
//
//                    Log.e("Key word",keyword.get("placement"+key) + " + " +keyword.get("defaultType"+key) + " + " +keyword.get("isActive"+key) + " + " +
//                            keyword.get("VDpbHost"+key) + " + " +keyword.get("VDpbAccountId"+key) + " + " +keyword.get("VDstoredAuctionResponse"+key) + " + " +keyword.get("VDconfigId"+key) + " + " +keyword.get("VDadUnitID"+key) + " + " +
//                            keyword.get("pbHost"+key) + " + " +keyword.get("pbAccountId"+key) + " + " +keyword.get("storedAuctionResponse"+key) + " + " +keyword.get("configId"+key) + " + " +keyword.get("adUnitID"+key) );
////                Log.e("KW",config.getJson()+ " ");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                // adUnitID3=/19968336/PrebidMobileValidator_Banner_All_Sizes,
                // adUnitID2=/19968336/PrebidMobileValidator_Banner_All_Sizes
            }
        });
    }


}
