package com.game.owly;

import android.os.Bundle;
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
import android.app.Activity;
import android.view.Menu;

public class HowToPlayActivity extends Activity {

//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
	platform.sdk.HandleLayout  customViewHeader;
	boolean boolAfterPaused;
//PLATFORM SDK OBJECT ENDS HERE


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_to_play);
//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
ExploringDBForBanners.iRefreshRate = 30000;
BackgroundService.strPublisherID = "388";
HandleLayout.contextAppContext = getApplicationContext();
		customViewHeader = (HandleLayout) findViewById(R.id.custViewHeader);
		customViewHeader.init("Image","header");
		customViewHeader.invalidate();

//PLATFORM SDK OBJECT ENDS HERE

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
