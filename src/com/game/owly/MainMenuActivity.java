package com.game.owly;

import android.app.Activity;
//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
import android.widget.*;
import android.view.*;
import platform.sdk.*;
import android.webkit.WebView;
import android.util.Log;
import android.util.DisplayMetrics;
import java.util.*;
import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
//PLATFORM SDK OBJECT ENDS HERE
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity implements OnClickListener {

//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
	platform.sdk.HandleLayout  customViewHeader;
	boolean boolAfterPaused;
//PLATFORM SDK OBJECT ENDS HERE


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_menu);
//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
ExploringDBForBanners.iRefreshRate = 30000;
BackgroundService.strPublisherID = "388";
HandleLayout.contextAppContext = getApplicationContext();
		customViewHeader = (HandleLayout) findViewById(R.id.custViewHeader);
		customViewHeader.init("Image","header");
		customViewHeader.invalidate();

//PLATFORM SDK OBJECT ENDS HERE


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

//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
	@Override
	public void onDestroy(){
		super.onDestroy();
		SingleT.removeObjects(customViewHeader, null);
}
//PLATFORM SDK OBJECT ENDS HERE

}
