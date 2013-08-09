package fi.metacity.splitthebubble;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SplitTheBubble implements ApplicationListener {
	
	private static final int WORLD_WIDTH = 800;
	private static final int WORLD_HEIGHT = 480;
	
	
	private long mLastBubbleSpawnTime;
	
	private final OrthographicCamera mCamera = new OrthographicCamera();
	private SpriteBatch mBatch;
	
	private final Array<Bubble> mBubbles = new Array<Bubble>();
	private Texture mCharacterTexture;
	private Rectangle mCharacter;
	private Sprite mBackground;
	
	private TextureRegion mRopeRegion;
	private Rectangle mRope;
	private boolean mRopeVisible;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Gdx.graphics.setVSync(true);
		
		mCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		mBatch = new SpriteBatch();
		
		mCharacterTexture = new Texture(Gdx.files.internal("character.png"));
		mCharacter = new Rectangle(
				WORLD_WIDTH/2 - mCharacterTexture.getWidth()/2, // Center horizontally
				0,
				mCharacterTexture.getWidth(),
				mCharacterTexture.getHeight()
				);
		
		mBackground = new Sprite(new Texture(Gdx.files.internal("background.png")));
		
		Texture ropeTexture = new Texture(Gdx.files.internal("rope.png"));
		mRopeRegion = new TextureRegion(ropeTexture, ropeTexture.getWidth(), 0);
		mRope = new Rectangle(
				WORLD_WIDTH/2 - ropeTexture.getWidth()/2, // Center horizontally
				0,
				ropeTexture.getWidth(),
				0
				);
		
		Bubble.loadAssets();
		
	}

	@Override
	public void dispose() {
		mBatch.dispose();
		mBackground.getTexture().dispose();
		mCharacterTexture.dispose();
		Bubble.dispose();
	}

	@Override
	public void render() {
		final float deltaTime = Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		mCamera.update();

		mBatch.setProjectionMatrix(mCamera.combined);
		mBatch.begin();
		mBackground.draw(mBatch);
		mBatch.draw(mRopeRegion, mRope.x, mRope.y);
		mBatch.draw(mCharacterTexture, mCharacter.x, mCharacter.y);
		for (Bubble bubble : mBubbles) {
			bubble.draw(mBatch);
		}
		mBatch.end();
		
		
		// Move the character
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.VOLUME_UP)) {
			mCharacter.x -= (400 * deltaTime);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.VOLUME_DOWN)) {
			mCharacter.x += (400 * deltaTime);
		}
		
		// Spawn the rope
		if (!mRopeVisible && (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU) || Gdx.input.isTouched())) {
			mRopeVisible = true;
			mRope.x = mCharacter.x + mCharacter.width/2 - mRope.width/2;
			mRope.height = 0;
			mRopeRegion.setRegionHeight(0);
		}
		
		if (mRopeVisible) {
			mRope.height += 400 * deltaTime;
			mRopeRegion.setRegionHeight((int)mRope.height);
		}
		
		if (mRope.height > WORLD_HEIGHT) {
			clearRope();
		}
		
		// Prevent character from going off screen
		if (mCharacter.x < 0) {
			mCharacter.x = 0;
		} else if (mCharacter.x > WORLD_WIDTH - mCharacter.width) {
			mCharacter.x = WORLD_WIDTH - mCharacter.width;
		}
		
		// Process bubbles
		Iterator<Bubble> iter = mBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			
			// Apply gravity to Y
			bubble.yVelocity -= (4 * deltaTime);
			
			// Move the bubble
			final float deltaX = bubble.xVelocity * 200 * deltaTime;
			final float deltaY = bubble.yVelocity * 200 * deltaTime;
			bubble.x += deltaX;
			bubble.y += deltaY;
			
			// Bounce from screen edges
			if (bubble.x > WORLD_WIDTH - bubble.width) {
				bubble.x = WORLD_WIDTH - bubble.width;
				bubble.flipXDirection();
			} else if (bubble.x < 0) {
				bubble.x = 0;
				bubble.flipXDirection();
			}
			if (bubble.y > WORLD_HEIGHT - bubble.height) {
				bubble.y = WORLD_HEIGHT - bubble.height;
				bubble.flipYDirection();
				bubble.yVelocity *= 0.98; // And lose some momentum
			} else if (bubble.y < 0) {
				bubble.y = 0;
				// Apply ground friction to X
				bubble.xVelocity *= 0.999;
				bubble.flipYDirection();
			}
			
			// Split when overlapping
			if (bubble.overlaps(mRope) && mRopeVisible) {
				if (bubble.type == Bubble.Type.BIG || bubble.type == Bubble.Type.MEDIUM) {
					for (int i = 0; i < 2; ++i) {
						Bubble splitBubble = Bubble.newInstance(
								bubble.type == Bubble.Type.BIG ? Bubble.Type.MEDIUM : Bubble.Type.SMALL);
						splitBubble.x = bubble.x;
						splitBubble.y = bubble.y;
						mBubbles.add(splitBubble);
					}
				}
				clearRope();
				iter.remove();
			}
		}
		
		if (TimeUtils.nanoTime() - mLastBubbleSpawnTime > 5000000000L) {
			spawnBubble(Bubble.Type.BIG, (float)Math.random()*(600), (float)Math.random()*(300));
		}
		
	}
	
	private void clearRope() {
		mRopeVisible = false;
		mRope.height = 0;
		mRopeRegion.setRegionHeight(0);
	}
	
	private void spawnBubble(Bubble.Type type, float x, float y) {
		Bubble bubble = Bubble.newInstance(type);
		bubble.x = x;
		bubble.y = y;
		mBubbles.add(bubble);
		mLastBubbleSpawnTime = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
