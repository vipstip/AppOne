package com.example.appone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btnHit;
    TextView txtJson,txtJson2;
    ProgressDialog pd;
    Button button,button2;
    String id1,id2;
    private int refreshCount = 0;

    HashMap<String,String> keyword;

    String url = "https://api.github.com/";
    GetId getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtJson = (TextView) findViewById(R.id.textView);
        txtJson2 = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.btnAd);
        button2 = (Button) findViewById(R.id.btnAd2);
        getId = new GetId(this);
//        configBanner = new ConfigBanner();

        keyword = new HashMap<>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                constants = new Constants(MainActivity.this);
//                keyword = constants.getResponse();
                try {
                    Log.e("Err Man2",keyword.get("placement") + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer key = getId.getPlacement().get("full1");
                    Log.e("key",key + " ");

                    keyword = getId.getConfigintertitial();

                    Log.e("Key word",keyword.get("placement"+key) + " + " +keyword.get("defaultType"+key) + " + " +keyword.get("isActive"+key) + " + " +
                            keyword.get("VDpbHost"+key) + " + " +keyword.get("VDpbAccountId"+key) + " + " +keyword.get("VDstoredAuctionResponse"+key) + " + " +keyword.get("VDconfigId"+key) + " + " +keyword.get("VDadUnitID"+key) + " + " +
                            keyword.get("pbHost"+key) + " + " +keyword.get("pbAccountId"+key) + " + " +keyword.get("storedAuctionResponse"+key) + " + " +keyword.get("configId"+key) + " + " +keyword.get("adUnitID"+key) );
//                Log.e("KW",config.getJson()+ " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }


}
