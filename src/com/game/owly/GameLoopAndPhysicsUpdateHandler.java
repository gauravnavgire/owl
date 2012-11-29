package com.game.owly;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public class GameLoopAndPhysicsUpdateHandler extends PhysicsWorld {

	public GameLoopAndPhysicsUpdateHandler(Vector2 pGravity, boolean pAllowSleep) {
		super(pGravity, pAllowSleep);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		
	}

	
}
