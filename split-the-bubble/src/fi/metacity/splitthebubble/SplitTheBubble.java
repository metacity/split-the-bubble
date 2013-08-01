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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SplitTheBubble implements ApplicationListener {
	
	private long mLastBubbleSpawnTime;
	
	private OrthographicCamera mCamera;
	private SpriteBatch mBatch;
	private Vector3 mTouchPos;
	
	private Sprite mBackground;
	private Sprite mCharacter;
	private Array<Bubble> mBubbles;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		
		mCamera = new OrthographicCamera();
		mCamera.setToOrtho(false, 800, 480);
		
		mBatch = new SpriteBatch();
		mTouchPos = new Vector3();
		mBubbles = new Array<Bubble>();
		
		mCharacter = new Sprite(new Texture(Gdx.files.internal("character.png")));
		mCharacter.setPosition(800/2 - mCharacter.getWidth()/2, 0);  // Center horizontally
		
		mBackground = new Sprite(new Texture(Gdx.files.internal("background.png")));
		
		Bubble.loadAssets();
	}

	@Override
	public void dispose() {
		mBatch.dispose();
		mBackground.getTexture().dispose();
		mCharacter.getTexture().dispose();
		Bubble.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		mCamera.update();

		mBatch.setProjectionMatrix(mCamera.combined);
		mBatch.begin();
		mBackground.draw(mBatch);
		mCharacter.draw(mBatch);
		for (Bubble bubble : mBubbles) {
			bubble.draw(mBatch);
		}
		mBatch.end();
		
		Iterator<Bubble> iter = mBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			
			// Apply air resistance to X
			bubble.xSpeed *= 0.999;
			
			// Apply gravity to Y
			bubble.ySpeed -= (4 * Gdx.graphics.getDeltaTime());
			
			// Move the bubble
			final float deltaX = bubble.xSpeed * 200 * Gdx.graphics.getDeltaTime();
			final float deltaY = bubble.ySpeed * 200 * Gdx.graphics.getDeltaTime();
			bubble.setPosition(bubble.getX() + deltaX, bubble.getY() + deltaY);
			
			// Bounce from screen edges
			if (bubble.getX() > 800 - bubble.getWidth()) {
				bubble.setX(800 - bubble.getWidth());
				bubble.flipXDirection();
			} else if (bubble.getX() < 0) {
				bubble.setX(0);
				bubble.flipXDirection();
			}
			if (bubble.getY() > 480 - bubble.getHeight()) {
				bubble.setY(480 - bubble.getHeight());
				bubble.flipYDirection();
				bubble.ySpeed *= 0.98; // And lose some momentum
			} else if (bubble.getY() < 0) {
				bubble.setY(0);
				bubble.flipYDirection();
			}
			
			// Move the character
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				mCharacter.setX(mCharacter.getX() - 200 * Gdx.graphics.getDeltaTime());
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				mCharacter.setX(mCharacter.getX() + 200 * Gdx.graphics.getDeltaTime());
			}
			
			// Prevent character from going off screen
			if (mCharacter.getX() < 0) {
				mCharacter.setX(0);
			} else if (mCharacter.getX() > 800 - mCharacter.getWidth()) {
				mCharacter.setX(800 - mCharacter.getWidth());
			}
			
			
			// Split when touched
			if (Gdx.input.isTouched()) {
				mTouchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				mCamera.unproject(mTouchPos);
				if (mTouchPos.x >= bubble.getX() && mTouchPos.x <= bubble.getX() + bubble.getWidth()
				 && mTouchPos.y >= bubble.getY() && mTouchPos.y <= bubble.getY() + bubble.getHeight()) {
					if (bubble.type == Bubble.Type.BIG || bubble.type == Bubble.Type.MEDIUM) {
						for (int i = 0; i < 2; ++i) {
							Bubble splitBubble = Bubble.newInstance(
									bubble.type == Bubble.Type.BIG ? Bubble.Type.MEDIUM : Bubble.Type.SMALL);
							splitBubble.setPosition(bubble.getX(), bubble.getY());
							mBubbles.add(splitBubble);
						}
					}
					iter.remove();
				}
			}
		}
		
		if (TimeUtils.nanoTime() - mLastBubbleSpawnTime > 5000000000L) {
			spawnBubble(Bubble.Type.BIG, (float)Math.random()*(600), (float)Math.random()*(300));
		}
		
	}
	
	private void spawnBubble(Bubble.Type type, float x, float y) {
		Bubble bubble = Bubble.newInstance(type);
		bubble.setPosition(x, y);
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
