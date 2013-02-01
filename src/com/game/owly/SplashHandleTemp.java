package com.game.owly;
import platform.sdk.*;
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
import android.content.Intent;
import android.os.Bundle;
//import android.os.StrictMode;

public class SplashHandleTemp extends Activity {

//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
	platform.sdk.HandleLayout  customViewHeader;
	platform.sdk.HandleLayout  customViewFooter;
	boolean boolAfterPaused;
//PLATFORM SDK OBJECT ENDS HERE

	
	SplashHandleActivity splashHandle;
	
int iSetTimeOut = 4000;
int iKeepingSplashVisibleTimeOut = 10000;
int iDisplaySkipButtonOnVideoAfterPeriod = 3000;
	
String strButtonSkipText = "Skip";
String strButtonSkipSplashEndText = "Skip";
	
	// Means, whether the publisher wants to display Splash At -- End or not
boolean boolNeedSplashAtEnd = true;
	// Means now, it showing splash now at -- End.
	
boolean boolCrossSplashStart = true;
boolean boolCrossSplashEnd = true;
boolean boolOrientationLandscape = false;
boolean boolLaunchSplashNotClickable = true;
boolean boolExitSplashNotClickable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
SplashHandleActivity.strBannerTypeStart = "Interstitial";
SplashHandleActivity.strBannerTypeEnd = "Interstitial";
SplashHandleActivity.iTimeStart = 4000;
        
        SplashHandleActivity.boolNeedSplashAtEnd = boolNeedSplashAtEnd;
		SplashHandleActivity.boolCrossSplashStart = boolCrossSplashStart;
		SplashHandleActivity.boolCrossSplashEnd = boolCrossSplashEnd;
		SplashHandleActivity.boolOrientationLandscape = boolOrientationLandscape;
		SplashHandleActivity.iSetTimeOut = iSetTimeOut;
		SplashHandleActivity.iKeepingSplashVisibleTimeOut = iKeepingSplashVisibleTimeOut;
		SplashHandleActivity.iDisplaySkipButtonOnVideoAfterPeriod = iDisplaySkipButtonOnVideoAfterPeriod;
		SplashHandleActivity.strButtonSkipText = strButtonSkipText;
		SplashHandleActivity.strButtonSkipSplashEndText = strButtonSkipSplashEndText;
		
		SplashHandleActivity.boolLaunchSplashNotClickable = boolLaunchSplashNotClickable;
		SplashHandleActivity.boolExitSplashNotClickable = boolExitSplashNotClickable;
        
        HandleLayout.contextAppContext = getApplicationContext();
BackgroundService.strPublisherID = "388";
        
        
        Intent intent = new Intent();
        intent.setClass(this, SplashHandleActivity.class);
        startActivity(intent);
        finish();// For boolSplashEnd
   }

//======================CODE INSERTED BY SEVENTYNINE APPJACKET===================
//PLATFORM SDK OBJECT STARTS HERE
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(!SplashHandleActivity.boolNeedSplashAtEnd){
			ExploringDBForBanners.boolKeepRunning = false;
			BackgroundService.boolKeepRunning = false;
			BackgroundService.boolStarted = false;
			ExploringDBForBanners.boolStarted = false;
			ExploringDBForBanners.iThreadWaitingTime = 0;
			ImagePathUrlTable.closeDB();
			TimeTable.closeDB();
			platform.sdk.SingleT.iCheck = 0;
		}
		HandleLayout.boolKeepRunningForUpdatingUI = false;
		platform.sdk.HandleLayout.vContexts.removeAllElements();
SingleT.removeObjects(customViewHeader, customViewFooter);
}
//PLATFORM SDK OBJECT ENDS HERE

}
