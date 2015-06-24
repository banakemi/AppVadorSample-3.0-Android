package com.appvador.appvadordev;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.appvador.ads.AdListener;
import com.appvador.ads.AdManager;
import com.appvador.ads.ErrorCode;


public class MainActivity extends Activity {
    AdManager ad;
    AdManager adInterstitial;
    Button btnShow;
    Button btnShowInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAd();
        initInterstitialAd();

        // show
        btnShow = (Button)findViewById(R.id.button2);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ad.isReady()) {
                    Log.d("DEBUG", "ad is ready.");
                    FrameLayout adLayout = (FrameLayout) findViewById(R.id.adlayout);
                    ad.showAd(adLayout);
                } else {
                    Log.d("DEBUG", "ad is not ready.");
                }
            }
        });

        // show interstitial
        final Activity self = this;
        btnShowInterstitial = (Button)findViewById(R.id.button);
        btnShowInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adInterstitial.isReady()) {
                    adInterstitial.showInterstitial(self);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ad.destroy();
        super.onDestroy();
    }

    private void initAd() {
        // init
        ad = new AdManager(this, "be799a9af42fd94b851539335d3713ab");
        ad.setPreloadType(AdManager.PreloadType.ALL);
        ad.new Random().toSt)setBackgroundColor(Color.RED);
        ad.setAdListener(new AdListener() {
            @Override
            public void onReadyToPlayAd() {
                Log.d("DEBUG", "onReadyToPlayAd");
            }

            @Override
            public void onPlayingAd() {
                Log.d("DEBUG", "onPlayingAd");
            }

            @Override
            public void onFailedToPlayAd(ErrorCode errorCode) {
                Log.d("DEBUG", "onFailedToPlayAd");
            }

            @Override
            public void onCompletionAd() {
                Log.d("DEBUG", "onCompletionAd");
            }

            @Override
            public void onClickAd() {
                Log.d("DEBUG", "onClickAd");
            }

            @Override
            public void onUnmuteAd() {
                Log.d("DEBUG", "onUnmuteAd");
            }

            @Override
            public void onMuteAd() {
                Log.d("DEBUG", "onMuteAd");
            }

            @Override
            public void onCloseAd() {
                Log.d("DEBUG", "onCloseAd");
            }
        });
        ad.load();
    }

    private void initInterstitialAd() {
        // init interstitial
        adInterstitial = new AdManager(this, "be799a9af42fd94b851539335d3713ab");
        adInterstitial.setPreloadType(AdManager.PreloadType.ALL);
        adInterstitial.setAdListener(new AdListener() {
            @Override
            public void onReadyToPlayAd() {
                Log.d("DEBUG", "onReadyToPlayAd");
                btnShowInterstitial.setEnabled(true);
            }

            @Override
            public void onPlayingAd() {
                Log.d("DEBUG", "onPlayingAd");
            }

            @Override
            public void onFailedToPlayAd(ErrorCode errorCode) {
                Log.d("DEBUG", "onFailedToPlayAd");
            }

            @Override
            public void onCompletionAd() {
                Log.d("DEBUG", "onCompletionAd");
            }

            @Override
            public void onClickAd() {
                Log.d("DEBUG", "onClickAd");
            }

            @Override
            public void onUnmuteAd() {
                Log.d("DEBUG", "onUnmuteAd");
            }

            @Override
            public void onMuteAd() {
                Log.d("DEBUG", "onMuteAd");
            }

            @Override
            public void onCloseAd() {
                Log.d("DEBUG", "onCloseAd");
            }
        });
        adInterstitial.load();
    }
}
