package fi.metacity.splitthebubble;

import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class World {
	static final int WORLD_WIDTH = 800;
	static final int WORLD_HEIGHT = 480;
	static final float GRAVITY = 3;
	static final long BUBBLE_SPAWN_INTERVAL_NS = 12500000000L; 
	
	final Bob bob;
	final Array<Bubble> activeBubbles;
	final Rope rope;
	
	int score = 0;
	long lastBubbleSpawnTime;
	
	public World() {
		bob = new Bob(World.WORLD_WIDTH / 2, 0);
		rope = new Rope(bob);
		activeBubbles = new Array<Bubble>(false, 16);
	}
	
	public void update(float delta) {
		bob.update(delta);
		rope.update(delta);
		
		// Process bubbles
		Iterator<Bubble> iter = activeBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			bubble.update(delta);
			// Split when overlapping
			if (rope.isVisible && Intersector.overlapCircleRectangle(bubble, rope)) {
				for (int i = 0; i < 2; ++i) { // 2 bubbles per split
					Bubble splitBubble = bubble.getSplitBubble(bubble.x, bubble.y);
					if (splitBubble != null) {
						activeBubbles.add(splitBubble);
					}
				}
				score++;
				rope.clear();
				iter.remove();
				bubble.free();
			}
		}
		
		if (TimeUtils.nanoTime() - lastBubbleSpawnTime > BUBBLE_SPAWN_INTERVAL_NS) {
			Bubble spawnedBubble = BubbleBig.newInstance((float)Math.random() * 600, (float)Math.random() * 100 + 200);
			activeBubbles.add(spawnedBubble);
			lastBubbleSpawnTime = TimeUtils.nanoTime();
		}
	}
}
