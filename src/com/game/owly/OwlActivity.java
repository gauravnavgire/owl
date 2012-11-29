package com.game.owly;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
		IAccelerationListener {
	private static OwlActivity instance;
	private DisplayMetrics mMetrics;
	private Camera mCamera;
	public Scene mScene;
	private Font mFont;
	private Text mCollionsText, mEngineUpdateText;
	private static int mCount = 0;
	// Sprite UI
	private Sprite mOwlRunnerSprite, mPillarOneSprite, mPillarTwoSprite;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mOwlRunTextureRegion;
	private TextureRegion mPillarTextureRegion, mBackgroundTextureRegion;

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;
	private static final float DEMO_VELOCITY = 100.0f;

	// Box2D variables
	/**
	 * A FixtureDef is used with the physics engine. It sets the density of the
	 * object, its elasticity and friction values.This will affect the behaviour
	 * of an object while it’s inside the physics world.
	 */
	private PhysicsWorld mPhysicsWorld;
	private static float mDensity = 0.5f;
	private static float mElasticity = 0.5f;
	private static float mFriction = 0.0f;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory
			.createFixtureDef(mDensity, mElasticity, mFriction);

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		boolean fullScreen = true;
		ScreenOrientation orientation = ScreenOrientation.PORTRAIT_FIXED;
		RatioResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(
				CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(fullScreen, orientation, resolutionPolicy,
				mCamera);
	}

	public static OwlActivity getInstance() {
		return instance;
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
		mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 64,
				128);
		BitmapTextureAtlas BitmapTowerTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 30, 250);
		BitmapTextureAtlas BitmapBackgroundTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), CAMERA_WIDTH, CAMERA_HEIGHT,
				TextureOptions.DEFAULT);
		final String assetsBasePath = "gfx/";
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(assetsBasePath);
		mOwlRunTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, this, "owl1.png", 0,
						0, 1, 1);
		mPillarTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(BitmapTowerTextureAtlas, this, "tower.png", 0,
						0);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(BitmapBackgroundTextureAtlas, this,
						"background.png", 0, 0);
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
		mEngine.registerUpdateHandler(new FPSLogger());
		mScene = new Scene();
		Background background = new Background(Color.CYAN);
		mScene.setBackground(background);
		Vector2 gravity = new Vector2(0, SensorManager.GRAVITY_EARTH);
		boolean allowSleep = false;
		mPhysicsWorld = new PhysicsWorld(gravity, allowSleep);
		VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();
		// Physics body
		final Body owlRunnerBody;
		final Body pillarOneBody;
		final Body pillarTwoBody;

		final Sprite backgroundSprite = new Sprite(0.0f, 0.0f,
				mBackgroundTextureRegion, vertexBufferObjectManager);
		backgroundSprite.setHeight(mCamera.getHeight());

		mOwlRunnerSprite = new Sprite(0.0f, 0.0f, mOwlRunTextureRegion,
				vertexBufferObjectManager);
		owlRunnerBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mOwlRunnerSprite, BodyType.DynamicBody, FIXTURE_DEF);

		mPillarOneSprite = new Sprite(100.0f, 350.0f, mPillarTextureRegion,
				vertexBufferObjectManager);
		pillarOneBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mPillarOneSprite, BodyType.StaticBody, FIXTURE_DEF);

		mPillarTwoSprite = new Sprite(300.0f, 350.0f, mPillarTextureRegion,
				vertexBufferObjectManager);
		pillarTwoBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				mPillarTwoSprite, BodyType.StaticBody, FIXTURE_DEF);

		Rectangle rect_top = new Rectangle(0, 0, CAMERA_WIDTH, 0,
				vertexBufferObjectManager);
		Rectangle rect_bottom = new Rectangle(0, CAMERA_HEIGHT, CAMERA_WIDTH,
				CAMERA_HEIGHT, vertexBufferObjectManager);
		Rectangle rect_left = new Rectangle(0, 0, 0, CAMERA_HEIGHT,
				vertexBufferObjectManager);
		Rectangle rect_right = new Rectangle(CAMERA_WIDTH, 0, CAMERA_WIDTH,
				CAMERA_HEIGHT, vertexBufferObjectManager);
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
		mScene.attachChild(backgroundSprite);
		mScene.attachChild(mOwlRunnerSprite);
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
		PhysicsConnector owlRunnerConnector = new PhysicsConnector(
				mOwlRunnerSprite, owlRunnerBody, update_position, false);
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

		mOwlRunnerSprite.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				mEngineUpdateText.setText("Engine update : " + pSecondsElapsed
						+ " secs");
				if (mOwlRunnerSprite.collidesWith(mPillarOneSprite)) {
					mCollionsText.setText("" + ++mCount + " with Pillar 1");
				}

				if (mOwlRunnerSprite.collidesWith(mPillarTwoSprite)) {
					mCollionsText.setText("" + ++mCount + " with Pillar 2");
				}
			}
		});

		this.enableAccelerationSensor(this);
		mScene.registerUpdateHandler(mPhysicsWorld);

		loadFont();
		// Texts
		final Text movesText = new Text(10, 740, mFont, "Total collisions : ",
				vertexBufferObjectManager);
		mCollionsText = new Text(150, 740, mFont, "0", 100,
				vertexBufferObjectManager);
		mEngineUpdateText = new Text(10, 760, mFont, "Engine update : ", 100,
				vertexBufferObjectManager);
		mScene.attachChild(movesText);
		mScene.attachChild(mCollionsText);
		mScene.attachChild(mEngineUpdateText);

		final float centerX = (OwlActivity.CAMERA_WIDTH - this.mOwlRunTextureRegion
				.getWidth()) / 2;
		final float centerY = (OwlActivity.CAMERA_HEIGHT - this.mOwlRunTextureRegion
				.getHeight()) / 2;
		
		final Ball ball = new Ball(centerX, centerY, this.mOwlRunTextureRegion,
				this.getVertexBufferObjectManager());
		Body owlAutoRunnerBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
				ball, BodyType.DynamicBody, FIXTURE_DEF);
		PhysicsConnector owlAutoRunnerConnector = new PhysicsConnector(
				ball, owlAutoRunnerBody, update_position, false);
		//mPhysicsWorld.registerPhysicsConnector(owlAutoRunnerConnector);

		mScene.attachChild(ball);

		return mScene;
	}

	private class GameUpdater implements IUpdateHandler {

		@Override
		public void onUpdate(float pSecondsElapsed) {

		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
				pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	private void loadFont() {
		// Font
		this.mFont = FontFactory.create(getFontManager(), getTextureManager(),
				100, 100, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 10);
		this.mFont.load();
	}

	private static class Ball extends AnimatedSprite {
		private final PhysicsHandler mPhysicsHandler;

		public Ball(final float pX, final float pY,
				final TiledTextureRegion pTextureRegion,
				final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
			this.mPhysicsHandler.setVelocity(OwlActivity.DEMO_VELOCITY,
					OwlActivity.DEMO_VELOCITY);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			if (this.mX < 0) {
				this.mPhysicsHandler.setVelocityX(OwlActivity.DEMO_VELOCITY);
			} else if (this.mX + this.getWidth() > OwlActivity.CAMERA_WIDTH) {
				this.mPhysicsHandler.setVelocityX(-OwlActivity.DEMO_VELOCITY);
			}

			if (this.mY < 0) {
				this.mPhysicsHandler.setVelocityY(OwlActivity.DEMO_VELOCITY);
			} else if (this.mY + this.getHeight() > OwlActivity.CAMERA_HEIGHT) {
				this.mPhysicsHandler.setVelocityY(-OwlActivity.DEMO_VELOCITY);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	}
}
