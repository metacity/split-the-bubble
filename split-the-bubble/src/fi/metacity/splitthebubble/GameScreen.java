package fi.metacity.splitthebubble;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	
	static final int WORLD_WIDTH = 800;
	static final int WORLD_HEIGHT = 480;
	
	static final long BUBBLE_SPAWN_INTERVAL_NS = 7500000000L; 
	
	final SplitTheBubble game;
	final OrthographicCamera camera = new OrthographicCamera();
	
	final Array<Bubble> activeBubbles = new Array<Bubble>(false, 16);
	final Pool<Bubble> bubblePool = Pools.get(Bubble.class);
	
	final Texture smallBubbleImage;
	final Texture medBubbleImage;
	final Texture bigBubbleImage;
	
	final Texture characterImage;
	final Rectangle character;
	
	final Texture backgroundImage;
	final Sprite background;
	
	Texture ropeImage;
	TextureRegion ropeRegion;
	Rectangle rope;
	boolean isRopeVisible;
	
	int score = 0;
	long lastBubbleSpawnTime;
	
	public GameScreen(SplitTheBubble game) {
		this.game = game;
		
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		// Initialize assets
		backgroundImage = new Texture(Gdx.files.internal("background.png"));
		background = new Sprite(backgroundImage);
		
		bigBubbleImage = new Texture(Gdx.files.internal("bubble_big.png"));
		medBubbleImage = new Texture(Gdx.files.internal("bubble_med.png"));
		smallBubbleImage = new Texture(Gdx.files.internal("bubble_small.png"));
		
		characterImage = new Texture(Gdx.files.internal("character.png"));
		character = new Rectangle(
				WORLD_WIDTH/2 - characterImage.getWidth()/2, // Center horizontally
				0,
				characterImage.getWidth(),
				characterImage.getHeight()
				);
		
		ropeImage = new Texture(Gdx.files.internal("rope.png"));
		ropeRegion = new TextureRegion(ropeImage, ropeImage.getWidth(), 0);
		rope = new Rectangle(
				WORLD_WIDTH/2 - ropeImage.getWidth()/2, // Center horizontally
				0,
				ropeImage.getWidth(),
				0
				);
	}
	
	public void setLevel(Stage level) {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		background.draw(game.batch);
		game.batch.draw(ropeRegion, rope.x, rope.y);
		game.batch.draw(characterImage, character.x, character.y);
		for (Bubble bubble : activeBubbles) {
			Texture texture;
			if (bubble.type == Bubble.Type.SMALL) {
				texture = smallBubbleImage;
			} else if (bubble.type == Bubble.Type.MEDIUM) {
				texture = medBubbleImage;
			} else {
				texture = bigBubbleImage;
			}
			// Circle defines x and y as the center coordinates, but batch renders the texture as bottom-left coordinates
			game.batch.draw(texture, bubble.x - bubble.radius, bubble.y - bubble.radius);
		}
		game.font.draw(game.batch, "Score: " + score, 20, WORLD_HEIGHT - 20);
		game.batch.end();
		
		
		// Move the character
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.VOLUME_UP)) {
			character.x -= (400 * delta);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.VOLUME_DOWN)) {
			character.x += (400 * delta);
		}
		
		// Spawn the rope
		if (!isRopeVisible && (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU) || Gdx.input.isTouched())) {
			isRopeVisible = true;
			rope.x = character.x + character.width/2 - rope.width/2;
			rope.height = 0;
			ropeRegion.setRegionHeight(0);
		}
		
		if (isRopeVisible) {
			rope.height += 400 * delta;
			ropeRegion.setRegionHeight((int)rope.height);
		}
		
		if (rope.height > WORLD_HEIGHT) {
			clearRope();
		}
		
		// Prevent character from going off screen
		if (character.x < 0) {
			character.x = 0;
		} else if (character.x > WORLD_WIDTH - character.width) {
			character.x = WORLD_WIDTH - character.width;
		}
		
		// Process bubbles
		Iterator<Bubble> iter = activeBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			
			bubble.update(delta);
		
			// Bounce from screen edges
			if (bubble.x > WORLD_WIDTH - bubble.radius) {
				bubble.x = WORLD_WIDTH - bubble.radius;
				bubble.flipXDirection();
			} else if (bubble.x - bubble.radius < 0) {
				bubble.x = bubble.radius;
				bubble.flipXDirection();
			}
			if (bubble.y > WORLD_HEIGHT - bubble.radius) {
				bubble.y = WORLD_HEIGHT - bubble.radius;
				bubble.flipYDirection();
				bubble.velocity.y *= 0.98; // And lose some momentum
			} else if (bubble.y - bubble.radius < 0) {
				bubble.y = bubble.radius;
				// Apply ground friction to X
				bubble.velocity.x *= 0.999;
				bubble.flipYDirection();
			}
			
			// Split when overlapping
			if (isRopeVisible && Intersector.overlapCircleRectangle(bubble, rope)) {
				if (bubble.type == Bubble.Type.BIG || bubble.type == Bubble.Type.MEDIUM) {
					for (int i = 0; i < 2; ++i) { // 2 bubbles per split
						Bubble splitBubble = newBubble(
								bubble.type == Bubble.Type.BIG ? Bubble.Type.MEDIUM : Bubble.Type.SMALL, 
								bubble.x, 
								bubble.y
							);
						activeBubbles.add(splitBubble);
					}
				}
				score++;
				clearRope();
				iter.remove();
				bubblePool.free(bubble);
			}
		}
		
		if (TimeUtils.nanoTime() - lastBubbleSpawnTime > BUBBLE_SPAWN_INTERVAL_NS) {
			Bubble spawnedBubble = newBubble(Bubble.Type.BIG, (float)Math.random()*(600), (float)Math.random()*(300));
			activeBubbles.add(spawnedBubble);
			lastBubbleSpawnTime = TimeUtils.nanoTime();
		}
	}
	
	private void clearRope() {
		isRopeVisible = false;
		rope.height = 0;
		ropeRegion.setRegionHeight(0);
	}
	
	private Bubble newBubble(Bubble.Type type, float x, float y) {
		Bubble bubble = bubblePool.obtain();
		float radius;
		if (type == Bubble.Type.SMALL) {
			radius = smallBubbleImage.getWidth() / 2;
		} else if (type == Bubble.Type.MEDIUM) {
			radius = medBubbleImage.getWidth() / 2;
		} else {
			radius = bigBubbleImage.getWidth() / 2;
		}
		bubble.init(type, radius, x, y);
		return bubble;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		bigBubbleImage.dispose();
		medBubbleImage.dispose();
		smallBubbleImage.dispose();
		characterImage.dispose();
		ropeImage.dispose();
		backgroundImage.dispose();
	}

}
