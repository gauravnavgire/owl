package com.game.owly;

import java.text.DecimalFormat;
import java.util.Random;
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

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
//import org.andengine.util.color.Color;
import android.graphics.Color;

import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * We make use of the BaseGameActivity class instead of Activity for our
 * application. We implement the IAccelerometerListener because we want to make
 * use of the accelerometer sensor of the phone. We declare the resources and
 * the fixed values such as the size of the game window, which I have set here
 * to the height and width of the screen of my phone, which has a 480×800
 * resolution.
 * 
 * @author gaurav
 * 
 */

public class OwlActivity extends SimpleBaseGameActivity implements
		IAccelerationListener, IOnSceneTouchListener, IOnAreaTouchListener {
	// ======================CODE INSERTED BY SEVENTYNINE
	// APPJACKET===================
	// PLATFORM SDK OBJECT STARTS HERE
	platform.sdk.HandleLayout customViewHeader;
	boolean boolAfterPaused;
	// PLATFORM SDK OBJECT ENDS HERE

	private DisplayMetrics mMetrics;
	private Camera mCamera;
	public Scene mScene;
	private Font mFont, mWonLostFont;
	private Text mEatenText, mEngineUpdateText, mEatsText;
	private static int mCount = 0;
	// Sprite UI
	private static Sprite mPillarOneSprite, mPillarTwoSprite, mWaveSprite,
			mBackgroundSprite;
	private AnimatedSprite mOwlySprite;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mOwlyTextureRegion, mFishTextureRegion,
			mSharkTextureRegion;
	private TextureRegion mPillarTextureRegion, mBackgroundTextureRegion,
			mWaveTextureRegion;

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 860;
	private static final float FISH_VELOCITY = 200.0f;
	private static float mCenterX;
	private float mCenterY;
	private VertexBufferObjectManager mVertexBufferObjectManager;

	// Box2D variables
	/**
	 * A FixtureDef is used with the physics engine. It sets the density of the
	 * object, its elasticity and friction values.This will affect the behaviour
	 * of an object while it’s inside the physics world.
	 */
	private PhysicsWorld mPhysicsWorld;
	private float mGravityX;
	private float mGravityY;
	private static float mDensity = 0.8f;
	private static float mElasticity = 0.3f;
	private static float mFriction = 0.3f;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory
			.createFixtureDef(mDensity, mElasticity, mFriction);

	// Game elements
	private boolean mEaten = false;
	private int atecount = 10;
	private int target = 10;
	private Fish mFish1, mFish2, mFish3, mSharkFish;
	private GameUpdateHandler mGameUpdateHandler;
	private float mSeconds = 0.0f;

	@Override
	public EngineOptions onCreateEngineOptions() {
		mMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		boolean fullScreen = true;
		ScreenOrientation orientation = ScreenOrientation.PORTRAIT_FIXED;
		RatioResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(
				CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions eo = new EngineOptions(fullScreen, orientation,
				resolutionPolicy, mCamera);
		return eo;
	}

	/**
	 * In OnLoadResources we load all of the textures and other resources we
	 * will be using later. For OpenGL ES it is most efficient to use a single,
	 * large texture (1024×1024 on older phones, 2048×2048 on newer) instead of
	 * many small ones, as OpenGL ES can only hold one texture in memory at any
	 * time, making switching between textures an unnecessary burden. We use a
	 * single texture (texture atlas, previously called Texture in AndEngine)
	 * with all of the textures we will use copied onto it. TextureRegions are
	 * used to read sections from this unified texture. In the above code we
	 * create a TextureRegion while adding its contents to the texture atlas. We
	 * then tell the Engine’s texture manager to load the texture atlas resource
	 * into memory.
	 */
	@Override
	protected void onCreateResources() {
		// Textures
		mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 500,
				400);
		BitmapTextureAtlas BitmapTowerTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 30, 250);
		BitmapTextureAtlas BitmapBackgroundTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 1000, CAMERA_HEIGHT * 2,
				TextureOptions.DEFAULT);
		final String assetsBasePath = "gfx/";
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(assetsBasePath);
		mOwlyTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, this, "owl1.png", 0,
						0, 2, 1);
		mFishTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, this,
						"fish_front.png", 0, 65, 4, 1);
		mSharkTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, this,
						"sharksprite.png", 0, 120, 8, 1);
		mPillarTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(BitmapTowerTextureAtlas, this, "tower.png", 0,
						0);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(BitmapBackgroundTextureAtlas, this,
						"background.png", 0, 0);

		mWaveTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(BitmapBackgroundTextureAtlas, this,
						"wave.png", CAMERA_WIDTH + 1, 0);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		mEngine.getTextureManager().loadTexture(BitmapTowerTextureAtlas);
		mEngine.getTextureManager().loadTexture(BitmapBackgroundTextureAtlas);
	}

	/**
	 * OnLoadScene is where the scene is set up. If your games uses levels you
	 * would make this part a lot more complex, but since we are just setting up
	 * a basic example here, we do just that here. We create a new Scene, set
	 * the background to black and create a new PhysicsWorld, which is
	 * essentially a Box2D World [6]. This PhysicsWorld is given a gravity
	 * vector (downwards Y vector is set to ~1 G, or Earth gravity), with allow
	 * sleep set to false.
	 * 
	 * Next a texture we loaded previously from /assets/gfx is now used to
	 * create a new Sprite, essentially a bitmap image. A body is created as
	 * well, which is required for the PhysicsWorld. We make this a Dynamic
	 * Body, meaning that it will be affected by the PhysicsWorld without
	 * requiring external input. We also apply the FixtureDef we previously
	 * created to it.
	 * 
	 * Finally we create three lines which create the boundaries of the screen.
	 * These are set to a white colour, with a width adapting to the screen’s
	 * density. They also get the same FixtureDef. After completing all four
	 * objects this way, we add their faces to the Scene and their Bodies to the
	 * PhysicsWorld, using a PhysicsConnector. The latter is the link between
	 * the visual and physics representations and ensure that the results of the
	 * PhyicsWorld update the Scene.
	 * 
	 * Next we enable the accelerometer and register the PhysicsWorld as being
	 * responsible for handling updates for the Scene.
	 */
	@Override
	protected Scene onCreateScene() {
		mGameUpdateHandler = new GameUpdateHandler();
		mEngine.registerUpdateHandler(mGameUpdateHandler);
		mScene = new Scene();
		Vector2 gravity = new Vector2(0, SensorManager.GRAVITY_EARTH);
		boolean allowSleep = false;
		mPhysicsWorld = new PhysicsWorld(gravity, allowSleep);
		mVertexBufferObjectManager = getVertexBufferObjectManager();
		// Physics body
		final Body owlRunnerBody;
		final Body pillarOneBody;
		final Body pillarTwoBody;

		mBackgroundSprite = new Sprite(0.0f, 0.0f, mBackgroundTextureRegion,
				mVertexBufferObjectManager);
		mBackgroundSprite.setScaleY(mBackgroundSprite.getHeight()
				/ CAMERA_HEIGHT);

		mWaveSprite = new Sprite(0, mCamera.getHeight()
				- mWaveTextureRegion.getHeight(), mWaveTextureRegion,
				mVertexBufferObjectManager);

		mPillarOneSprite = new Sprite(100.0f, 400.0f, mPillarTextureRegion,
				mVertexBufferObjectManager);
		pillarOneBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mPillarOneSprite, BodyType.StaticBody, FIXTURE_DEF);
		pillarOneBody.setUserData(new String("pillar1"));

		mOwlySprite = new AnimatedSprite(mPillarOneSprite.getX(),
				mPillarOneSprite.getY() - mOwlyTextureRegion.getHeight(),
				mOwlyTextureRegion, mVertexBufferObjectManager);
		mOwlySprite.setTag(99);
		long[] eachFrmTime = { 1000, 90 };
		int[] eachFrm = { 0, 1 };

		mOwlySprite.animate(eachFrmTime, eachFrm);
		owlRunnerBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mOwlySprite, BodyType.DynamicBody, FIXTURE_DEF);

		mPillarTwoSprite = new Sprite(300.0f, 400.0f, mPillarTextureRegion,
				mVertexBufferObjectManager);
		pillarTwoBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mPillarTwoSprite, BodyType.StaticBody, FIXTURE_DEF);
		pillarOneBody.setUserData(new String("pillar1"));

		Rectangle rect_top = new Rectangle(0, 0, CAMERA_WIDTH, 0,
				mVertexBufferObjectManager);
		Rectangle rect_bottom = new Rectangle(0, CAMERA_HEIGHT, CAMERA_WIDTH,
				CAMERA_HEIGHT, mVertexBufferObjectManager);
		Rectangle rect_left = new Rectangle(0, 0, 0, CAMERA_HEIGHT,
				mVertexBufferObjectManager);
		Rectangle rect_right = new Rectangle(CAMERA_WIDTH, 0, CAMERA_WIDTH,
				CAMERA_HEIGHT, mVertexBufferObjectManager);
		rect_top.setColor(1, 1, 1);
		rect_left.setColor(1, 1, 1);
		rect_right.setColor(1, 1, 1);
		rect_bottom.setColor(1, 1, 1);

		Body wall_top = PhysicsFactory.createBoxBody(mPhysicsWorld, rect_top,
				BodyType.StaticBody, FIXTURE_DEF);
		Body wall_right = PhysicsFactory.createBoxBody(mPhysicsWorld,
				rect_right, BodyType.StaticBody, FIXTURE_DEF);
		Body wall_left = PhysicsFactory.createBoxBody(mPhysicsWorld, rect_left,
				BodyType.StaticBody, FIXTURE_DEF);
		Body wall_bottom = PhysicsFactory.createBoxBody(mPhysicsWorld,
				rect_bottom, BodyType.StaticBody, FIXTURE_DEF);
		// attache the sprites to the scene
		mScene.attachChild(mBackgroundSprite);
		mScene.attachChild(mOwlySprite);
		mScene.attachChild(mPillarOneSprite);
		mScene.attachChild(mPillarTwoSprite);
		mScene.attachChild(rect_left);
		mScene.attachChild(rect_bottom);
		mScene.attachChild(rect_right);
		mScene.attachChild(rect_top);

		// Create the physics connector.
		// This connector will help in updating the sprite ui positions
		// and rotations by getting the data from the sprite body
		// which will be updated when onUpdate of PhysicsWorld instance is
		// called.
		// onUpdate of PhysicsWolrd will get called when the scene updateHandler
		// is assigned to this Physics world.
		boolean update_position = true;
		boolean update_rotation = true;
		PhysicsConnector owlRunnerConnector = new PhysicsConnector(mOwlySprite,
				owlRunnerBody, update_position, false);
		PhysicsConnector pillarOneConnector = new PhysicsConnector(
				mPillarOneSprite, pillarOneBody, update_position,
				update_rotation);
		PhysicsConnector pillarTwoConnector = new PhysicsConnector(
				mPillarTwoSprite, pillarTwoBody, update_position,
				update_rotation);
		PhysicsConnector lineTopConnector = new PhysicsConnector(rect_top,
				wall_top, update_position, update_rotation);
		PhysicsConnector lineBottomConnector = new PhysicsConnector(
				rect_bottom, wall_bottom, update_position, update_rotation);
		PhysicsConnector lineLeftConnector = new PhysicsConnector(rect_left,
				wall_left, update_position, update_rotation);
		PhysicsConnector lineRightConnector = new PhysicsConnector(rect_right,
				wall_right, update_position, update_rotation);

		mPhysicsWorld.registerPhysicsConnector(owlRunnerConnector);
		mPhysicsWorld.registerPhysicsConnector(pillarOneConnector);
		mPhysicsWorld.registerPhysicsConnector(pillarTwoConnector);
		mPhysicsWorld.registerPhysicsConnector(lineTopConnector);
		mPhysicsWorld.registerPhysicsConnector(lineBottomConnector);
		mPhysicsWorld.registerPhysicsConnector(lineLeftConnector);
		mPhysicsWorld.registerPhysicsConnector(lineRightConnector);

		this.enableAccelerationSensor(this);
		mScene.registerUpdateHandler(mPhysicsWorld);

		mCenterX = (OwlActivity.CAMERA_WIDTH - this.mOwlyTextureRegion
				.getWidth()) / 2;
		mCenterY = (OwlActivity.CAMERA_HEIGHT - this.mOwlyTextureRegion
				.getHeight()) / 2;
		mFish1 = new Fish(mCenterX, OwlActivity.CAMERA_HEIGHT - 170,
				this.mFishTextureRegion, this.getVertexBufferObjectManager());
		mFish1.animate(200);
		mFish1.setTag(101);

		mFish2 = new Fish(mCenterX + 130, OwlActivity.CAMERA_HEIGHT - 140,
				this.mFishTextureRegion, this.getVertexBufferObjectManager());
		mFish2.animate(200);
		mFish2.setTag(102);

		mFish3 = new Fish(40, OwlActivity.CAMERA_HEIGHT - 120,
				this.mFishTextureRegion, this.getVertexBufferObjectManager());
		mFish3.animate(200);
		mFish3.setTag(103);

		mSharkFish = new Fish(mOwlySprite.getX(),
				OwlActivity.CAMERA_HEIGHT - 120, mSharkTextureRegion,
				mVertexBufferObjectManager);
		mSharkFish.animate(200);
		mSharkFish.setTag(104);

		this.mScene.attachChild(mFish1);
		this.mScene.attachChild(mFish2);
		this.mScene.attachChild(mFish3);

		mPhysicsWorld.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {

				Body b1 = contact.getFixtureA().getBody();
				Body b2 = contact.getFixtureB().getBody();
				Object whichBody = b1.getUserData() == null ? b2.getUserData()
						: b1.getUserData();
				Body b = b1.getType() == BodyType.DynamicBody ? b1 : b2;

				if (whichBody != null) {
					String which = (String) whichBody;
					if (which.equals("pillar1")) {

						b.setAngularVelocity(5.0f);
					}

				}

			}

			@Override
			public void beginContact(Contact contact) {
			}
		});
		mScene.attachChild(mWaveSprite);
		loadFont();
		attachFonts();
		return mScene;
	}

	private void attachFonts() {
		// Texts
		mEatsText = new Text(10, 40, mFont, "Fish ate : ",
				mVertexBufferObjectManager);
		mEatenText = new Text(mEatsText.getWidth() + 10, 40, mFont, "10", 100,
				mVertexBufferObjectManager);
		Text update = new Text(10, 65, mFont, " Time : ", 100,
				mVertexBufferObjectManager);
		mEngineUpdateText = new Text(update.getWidth() + 10, 65, mFont, " 0 ",
				100, mVertexBufferObjectManager);
		mScene.attachChild(mEatsText);
		mScene.attachChild(mEatenText);
		mScene.attachChild(update);
		mScene.attachChild(mEngineUpdateText);

	}

	public void detachFonts() {
		mScene.detachChild(mEatsText);
		mScene.detachChild(mEatenText);
		mScene.detachChild(mEngineUpdateText);
	}

	private void showGameOver(int currentScore2) {
		mScene.detachChildren();
		mScene.unregisterUpdateHandler(mPhysicsWorld);
		mEngine.unregisterUpdateHandler(mGameUpdateHandler);
		// Create gameover image and attach it
		mScene.attachChild(mBackgroundSprite);
		mEatsText = new Text(70, 200, mWonLostFont,
				" \t Game Over.\n You ate only " + (target-atecount)
						+ " fish. \n Owly is still hungry.",
				mVertexBufferObjectManager);
		mScene.attachChild(mEatsText);
	}

	private void showGameWon(int currentScore2) {
		mScene.detachChildren();
		mScene.unregisterUpdateHandler(mPhysicsWorld);
		mEngine.unregisterUpdateHandler(mGameUpdateHandler);
		// Create won image and attach it.
		mScene.attachChild(mBackgroundSprite);
		mEatsText = new Text(
				70,
				200,
				mWonLostFont,
				" Congratulations you won! \n You ate all fish.\n Owly's tummy is full.",
				mVertexBufferObjectManager);
		mScene.attachChild(mEatsText);
	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		mGravityX = pAccelerationData.getX();
		mGravityY = pAccelerationData.getY() / 2;
		final Vector2 gravity = Vector2Pool.obtain(mGravityX, mGravityY);
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	private void loadFont() {
		FontFactory.setAssetBasePath("gfx/font/");
		final ITexture wonLostFontTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 400, 600, TextureOptions.BILINEAR);
		// Font
		this.mFont = FontFactory.create(getFontManager(), getTextureManager(),
				100, 100, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20);
		this.mFont.load();

		this.mWonLostFont = FontFactory.createFromAsset(this.getFontManager(),
				wonLostFontTexture, this.getAssets(), "KingdomOfHearts.ttf",
				60, true, Color.RED);
		this.mWonLostFont.load();
	}

	private static class Fish extends AnimatedSprite {
		private final PhysicsHandler mPhysicsHandler;
		int Low = 100;
		int High = (int) OwlActivity.FISH_VELOCITY;
		Random random = new Random();
		float velocity;
		float jump;

		public void resetPosition() {

			switch (this.getTag()) {
			case 101:
				this.setPosition(mCenterX, OwlActivity.CAMERA_HEIGHT - 200);
				break;
			case 102:
				this.setPosition(mCenterX + 130,
						OwlActivity.CAMERA_HEIGHT - 160);
				break;
			case 103:
				this.setPosition(40, OwlActivity.CAMERA_HEIGHT - 100);
				break;

			default:
				break;
			}
		}

		public Fish(final float pX, final float pY,
				final TiledTextureRegion pTextureRegion,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
			velocity = (float) random.nextInt(High - Low) + Low;
			int low = OwlActivity.CAMERA_HEIGHT - 650;
			int high = OwlActivity.CAMERA_HEIGHT - 550;
			jump = (float) random.nextInt(high - low) + low;
			this.mPhysicsHandler.setVelocityY(velocity);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			if (this.mY < jump) {
				this.mPhysicsHandler.setVelocityY(velocity);
				this.setRotation(180.0f);
			} else if (this.mY + (this.getHeight()) > OwlActivity.CAMERA_HEIGHT) {
				this.mPhysicsHandler.setVelocityY(-velocity);
				this.setRotation(0.0f);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		return false;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	// ======================CODE INSERTED BY SEVENTYNINE
	// APPJACKET===================
	// PLATFORM SDK OBJECT STARTS HERE
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!SplashHandleActivity.boolNeedSplashAtEnd) {
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
		SingleT.removeObjects(customViewHeader, null);
	}

	// PLATFORM SDK OBJECT ENDS HERE

	private class GameUpdateHandler implements IUpdateHandler {

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			mSeconds = mSeconds + pSecondsElapsed;
			DecimalFormat format = new DecimalFormat("##.#");
			String formatted = format.format(mSeconds);
			mEngineUpdateText.setText(" " + formatted);

			IShape owl = (IShape) mScene.getChildByTag(99);
			IShape shape = (IShape) mScene.getChildByTag(101);
			IShape shape1 = (IShape) mScene.getChildByTag(102);
			IShape shape2 = (IShape) mScene.getChildByTag(103);
			IShape blackshape = (IShape) mScene.getChildByTag(200);
			if (owl.collidesWith(shape)) {
				mScene.detachChild(101);
			} else if (owl.collidesWith(shape1)) {
				mScene.detachChild(102);
			} else if (owl.collidesWith(shape2)) {
				mScene.detachChild(103);
			} else if (mOwlySprite.collidesWith(mWaveSprite)
					|| mOwlySprite.collidesWith(mSharkFish)) {
				mScene.detachChild(mOwlySprite);
			}

			// if fish is removed i.e eaten. then decrease ate count.
			if (mScene.getChildByTag(101) == null) {
				mEatenText.setText((--atecount) + "");
				mFish1.resetPosition();
				mScene.attachChild(mFish1);
				mScene.detachChild(mWaveSprite);
				mScene.attachChild(mWaveSprite);
			} else if (mScene.getChildByTag(102) == null) {
				mEatenText.setText((--atecount) + "");
				mFish2.resetPosition();
				mScene.attachChild(mFish2);
				mScene.detachChild(mWaveSprite);
				mScene.attachChild(mWaveSprite);
			} else if (mScene.getChildByTag(103) == null) {
				mEatenText.setText((--atecount) + "");
				mFish3.resetPosition();
				mScene.attachChild(mFish3);
				mScene.detachChild(mWaveSprite);
				mScene.attachChild(mWaveSprite);
			}

			int sec = (int) mSeconds;
			if (sec != 0 && sec % 10 == 0) {
				if (mSharkFish != null) {
					if (mSharkFish.getY() > mWaveSprite.getY()) {
						mScene.detachChild(mSharkFish);
					}

					if (!mSharkFish.hasParent()) {
						mSharkFish.setX(mOwlySprite.getX());
						mScene.attachChild(mSharkFish);
					}
					mScene.detachChild(mWaveSprite);
					mScene.attachChild(mWaveSprite);
				}
			}

			// Game over conditions
			if (mScene.getChildByTag(99) == null || sec == 120) {
				showGameOver(atecount);
			}
			// Won conditions
			if (atecount == 0) {
				showGameWon(atecount);
			}
		}
	}
}
