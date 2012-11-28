package com.game.owly.anim;

import com.game.owly.R;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public class OwlyAnimation extends View implements
		ValueAnimator.AnimatorUpdateListener {

	private final Context mContext;
	private Drawable owl;
	private AnimatorSet set;
	private Rect mRect;

	public OwlyAnimation(Context context) {
		super(context);
		mContext = context;
	}

	public void addDrawable(Drawable drawable) {
		owl = drawable;
		Rect bounds = new Rect(20, 120, 100, 200);
		owl.setBounds(bounds);
	}

	private void createAnimation() {
		set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext,
				R.animator.owlmainscreen);
		set.setTarget(owl.getBounds());
	}

	public void startAnimation() {
		createAnimation();
		set.start();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {

		int y = (Integer) animation.getAnimatedValue("y");
		mRect = null;
		mRect = new Rect(y, 150, 130, 230);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (owl != null) {
			if (mRect == null) {
				mRect = new Rect(200, 150, 330, 230);
			}
			canvas.translate(mRect.left, mRect.top);
			owl.setBounds(mRect);
			owl.draw(canvas);
			canvas.translate(-mRect.left, -mRect.top);
		}
	}

}
