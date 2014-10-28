package com.game.owly;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.game.owly.util.Constants;

public class HowToPlayActivity extends Activity implements FlurryAdListener {
    FrameLayout adLayout;
    String adSpaceName = "Takeover";
    Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);
        adLayout = new FrameLayout(this);
        mContext = this;

        Button fetchAd = (Button) findViewById(R.id.fetch);
        fetchAd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // fetch and prepare ad for this ad space. won’t render one yet
                if (!FlurryAds.isAdReady(adSpaceName))
                    FlurryAds.fetchAd(mContext, adSpaceName, adLayout,
                            FlurryAdSize.FULLSCREEN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
        // get callbacks for ad events
        FlurryAds.setAdListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove the adSpace and the listener
        FlurryAds.removeAd(this, adSpaceName, adLayout);
        FlurryAgent.onEndSession(this);
    }

    @Override
    public void onAdClicked(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAdClosed(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAdOpened(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplicationExit(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRenderFailed(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRendered(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVideoCompleted(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean shouldDisplayAd(String arg0, FlurryAdType arg1) {
        // verify this method returns true to display the fetched ad
        return true;
    }

    @Override
    public void spaceDidFailToReceiveAd(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spaceDidReceiveAd(String adSpace) {
        // called when the ad has been prepared, ad can be displayed:
        FlurryAds.displayAd(this, adSpace, adLayout);
        // instead of displaying the ad here, you can check
        // FlurryAds.isAdReady(adSpace)
        // and display the ad when transitioning out of the activity.

    }
}
