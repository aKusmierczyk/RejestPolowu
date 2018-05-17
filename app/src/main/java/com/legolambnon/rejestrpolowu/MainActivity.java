package com.legolambnon.rejestrpolowu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ads

        MobileAds.initialize(this, "ca-app-pub-7452133748145052~6952649373");

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        //real ad
        mInterstitialAd.setAdUnitId("ca-app-pub-7452133748145052/6671649599");

        //testad
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //endtestad

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //end ads

        Button btnGoTo = (Button)(findViewById(R.id.btnGoTo));
        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed(){
                            Intent intent = new Intent (MainActivity.this , MenuActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent (MainActivity.this , MenuActivity.class);
                    startActivity(intent);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }
        });
    }
}
