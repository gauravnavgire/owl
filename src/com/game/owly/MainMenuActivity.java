package com.game.owly;

import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAgent;
import com.game.owly.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuActivity extends Activity implements OnClickListener, FlurryAdListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        findViewById(R.id.play_btn).setOnClickListener(this);
        findViewById(R.id.howtoplay_btn).setOnClickListener(this);
        findViewById(R.id.about_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.play_btn:
            Intent gameIntent = new Intent(getApplicationContext(),
                    OwlActivity.class);
            startActivity(gameIntent);
            break;
        case R.id.howtoplay_btn:
            startActivity(new Intent(getApplicationContext(),
                    HowToPlayActivity.class));
            break;
        case R.id.about_btn:
            Builder dialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_message);
            dialog.show();
            break;

        default:
            break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void spaceDidFailToReceiveAd(String arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void spaceDidReceiveAd(String arg0) {
        // TODO Auto-generated method stub
        
    }
}
