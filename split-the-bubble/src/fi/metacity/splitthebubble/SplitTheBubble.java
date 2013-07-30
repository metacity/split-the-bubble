package fi.metacity.splitthebubble;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
	private Array<Bubble> mBubbles;
	private Sprite mBackground;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		
		mCamera = new OrthographicCamera();
		mCamera.setToOrtho(false, 800, 480);
		mBatch = new SpriteBatch();
		mTouchPos = new Vector3();
		mBubbles = new Array<Bubble>();
		mBackground = new Sprite(new Texture(Gdx.files.internal("background.png")));
		
		Bubble.loadAssets();
	}

	@Override
	public void dispose() {
		mBatch.dispose();
		mBackground.getTexture().dispose();
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
		for (Bubble bubble : mBubbles) {
			bubble.draw(mBatch);
		}
		mBatch.end();
		
		Iterator<Bubble> iter = mBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			final float deltaX = bubble.xDirection * 200 * Gdx.graphics.getDeltaTime();
			final float deltaY = bubble.yDirection * 200 * Gdx.graphics.getDeltaTime();
			bubble.setPosition(bubble.getX() + deltaX, bubble.getY() + deltaY);
			if (bubble.getX() > 800 - bubble.getWidth() || bubble.getX() < 0) {
				bubble.flipXDirection();
			}
			if (bubble.getY() > 480 - bubble.getHeight() || bubble.getY() < 0) {
				bubble.flipYDirection();
			}
			
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
			spawnBubble(Bubble.Type.BIG, (float)Math.random()*(800-130), (float)Math.random()*(480-130));
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
