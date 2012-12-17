package com.game.owly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity implements OnClickListener {

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

			break;
		case R.id.about_btn:

			break;

		default:
			break;
		}

	}
}
