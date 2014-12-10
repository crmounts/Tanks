
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class GamePhysics extends TimerTask {
	
	// Constants
	public static final Vector EARTH_GRAVITY = new Vector(0.0, 0.1);
	public static final double WIND_RESISTANCE = 0.075;
	public static final Vector R_ACC = new Vector(0.15, 0.0);
	public static final Vector L_ACC = new Vector(-0.15, 0.0);
	public static final Vector U_ACC = new Vector(0.0, -0.2);
	public int counter = 0;
	
	/* Stores all of the physics bodies in a set
	 * so that it is easy to iterate through and 
	 * accelerate all of the bodies */
	private ConcurrentHashMap<Integer, PhysicsBody> bodies;
	
	
	public GamePhysics() {
		bodies = new ConcurrentHashMap<Integer, PhysicsBody>();
	}
	
	public void run() {
		if (Game.isPlaying) {
			boolean noCollision = true;
			Iterator it = Game.gp.getBodies();
			while (it.hasNext()) {
				Map.Entry<Integer, PhysicsBody> pairs = 
						(Map.Entry<Integer, PhysicsBody>)it.next();
				pairs.getValue().accelerate();
				pairs.getValue().move();
				Iterator inner = Game.gp.getBodies();
				while (inner.hasNext()) {
					Map.Entry<Integer, PhysicsBody> pairsInner = 
							(Map.Entry<Integer, PhysicsBody>)inner.next();
					if (pairs.getValue() != pairsInner.getValue() ) {
						if (pairs.getValue().collision(
								pairsInner.getValue())) {
							pairs.getValue().collide(pairsInner.getValue());
							noCollision = false;
						}
					}
				}
				if (noCollision) {
					pairs.getValue().noCollision();
				}
			}	
		}
	}
	
	public int addBody(PhysicsBody pb) {
		bodies.put(counter++, pb);
		return counter - 1;
	}
	
	public void removeBody(int in) {
		bodies.remove(in);
	}
	
	public Iterator getBodies() {
		return bodies.entrySet().iterator();
	}
	
	public void reset() {
		bodies.clear();
	}
}

