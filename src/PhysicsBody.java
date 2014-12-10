import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;


public abstract class PhysicsBody {
	
	public double mass;
	public Point position;
	public Vector velocity;
	public Set<Vector> forces;
	public String version;
	public BufferedImage img;
	public int width;
	public int height;
	public String type;
	public int id;
	
	public void setData(double inMass, Point inP, Vector inV, 
						String inVersion, int inW, int inH) {
		mass = inMass;
		position = inP;
		velocity = inV;
		version = inVersion;
		width = inW;
		height = inH;
		forces = new HashSet<Vector>();
	}
	
	public void addGravity() {
		forces.add(GamePhysics.EARTH_GRAVITY);
	}
	
	public void addForce(Vector v) {
		forces.add(v);
	}
	
	public void removeForce(Vector v) {
		forces.remove(v);
	}
	

	public void accelerate() {
		for (Vector v : forces) {
			Vector.addVect(velocity, v);
		}
	}
	
	public void move() {
		
		int x = (int)(position.getX() + 
				(velocity.getMagnitude() * 
						 velocity.getX()));
		int y = (int)(position.getY() + 
				  (velocity.getMagnitude() * 
					velocity.getY()));
		if (x < -100 || x > 1100 || y < -100) {
			Game.gp.removeBody(this.id);
		}
		position = new Point(x, y);
	}
	
	public void collide(PhysicsBody p) {
		System.out.println("Collision");
	}
	
	public void noCollision() {
		
	}
	
	public boolean collision(PhysicsBody b) {
		return (position.x + width >= b.position.x
				&& position.y + height >= b.position.y
				&& b.position.x + b.width >= position.x 
				&& b.position.y + b.height >= position.y);
	}
	
	public void draw(Graphics g) {
		g.drawImage(img, position.x, position.y, 
			width, height, null);
	}
	
	public void setId(int inId) {
		id = inId;
	}
}


class Tank extends PhysicsBody {
	
	
	// Tank state
	private int fuelConRate;
	private int jetFuelConRate;
	private boolean onGround;
	private Vector trajectory;
	private boolean rRight;
	private boolean rLeft;
	private boolean facingRight;
	private BufferedImage imgLeft;
	private BufferedImage imgRight;
	private int health;
	private int fuel;
	private int jetFuel;
	private int bullets;
	private boolean canShoot = true;
	private int bombs;
	private boolean canBomb = true;
	private int missiles;
	private boolean canMissile = true;
	private boolean canMoveGround = true;
	private boolean canMoveAir = true;
	
	public Tank(Point startLoc, String inVersion) {
		type = "Tank";
		setData(25.0, startLoc, new Vector(0.0, 0.0), inVersion, 50, 50);
		addGravity();
		
		fuelConRate = 0;
		jetFuelConRate = 0;
		onGround = false;
		trajectory = new Vector(0, 50);
		rRight = false;
		rLeft = false;
		facingRight = true;
		
		
		reset();
		
		try {
			if (imgLeft == null) {
				imgLeft = ImageIO.read(new File(version + "Left.png"));
			}
			if (imgRight == null) {
				imgRight = ImageIO.read(new File(version + "Right.png"));
			}
			img = imgRight;
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}

	}
	
	public void reset() {
		health = 100;
		fuel = 100;
		jetFuel = 100;
		bullets = 50;
		bombs = 30;
		missiles = 3;
	}
	
	@Override
	public void move() {
			
		int x = (int)(position.getX() + 
				(velocity.getMagnitude() * 
						 velocity.getX()));
		int y = (int)(position.getY() + 
				  (velocity.getMagnitude() * 
					velocity.getY()));
		if (x < 0) {
			x = 0;
			velocity.setX(0.0);
		} else if (x > 1000 - width) {
			x = 1000 - width;
			velocity.setX(0.0);
		}
		
		if (y < 0) {
			y = 0;
			velocity.setY(0.0);
		}
		position = new Point(x, y);
	}
	
	@Override
	public void collide(PhysicsBody p) {
		switch (p.type) {
		case "Ground": 
			position.y = 500;
			if (velocity.getY() > 0) {
				velocity.setY(0.0);
			}
			double xvel = velocity.getX();
			if (xvel != 0) {
				if (Math.abs(xvel) < 0.05) {
					velocity.setX(0.0);
				} else if (xvel < 0) {
					velocity.setX(velocity.getX() + 0.1);
				} else if (xvel > 0){
					velocity.setX(velocity.getX() - 0.1);
				}
			}
			onGround = true;
			if (jetFuelConRate > 0) {
				fuelConRate = jetFuelConRate;
				jetFuelConRate = 0;
			}
			break;
		case "Explosion" :
			lowerHealth(2);
			break;
		case "Bullet" :
			lowerHealth(3);
			Game.gp.removeBody(p.id);
			break;
		case "Missile" :
			lowerHealth(10);
			Explosion exp = new Explosion(new Point(
					 position.x - 10, position.y - 10));
			int i = Game.gp.addBody(exp);
			exp.setId(i);
			Game.gp.removeBody(p.id);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, position.x, position.y, 
				width, height, null);
		g.drawLine(position.x + width/2, position.y - 5, 
					(int)(position.x + width/2 + trajectory.getX()), 
					 (int)(position.y - 5 - trajectory.getY()));
	}
	
	@Override
	public void accelerate() {
		double x = trajectory.getX();
		if (x > 50) {
			rRight = false;
			x = 49;
		}
		if (x < -50) {
			rLeft = false;
			x = -49;
		}
		if (rRight) {
			x = x + 1.0;
			double y = Math.sqrt(2500 - trajectory.getX() * trajectory.getX());
			if (Double.isNaN(y)) {
				y = 9.9498743710662;
			}
			trajectory.setX(x);
			trajectory.setY(y);
			
			
		} else if (rLeft) {
			x = x - 1.0;
			double y = Math.sqrt(2500 - trajectory.getX() * trajectory.getX());
			if (Double.isNaN(y)) {
				y = 9.9498743710662;
			}
			trajectory.setX(x);
			trajectory.setY(y);
		}
		for (Vector v : forces) {
			Vector.addVect(velocity, v);
		}
	}
	
	
	public void faceRight() {
		img = imgRight;
		facingRight = true;
	}
	
	public void faceLeft() {
		img = imgLeft;
		facingRight = false;
	}
	
	// Fuel getters, incrementer/decrementers, and zeroers
	public int fConRate() {
		return fuelConRate;
	}
	
	public void incrFCR() {
		fuelConRate++;
	}
	
	public void decrFCR() {
		fuelConRate--;
		if (fuelConRate < 0) {
			zeroFCR();
		}
	}
	
	private void zeroFCR() {
		fuelConRate = 0;
	}
	
	
	public int jfConRate() {
		return jetFuelConRate;
	}
	
	public void incrJFCR() {
		jetFuelConRate++;
	}
	
	public void decrJFCR() {
		jetFuelConRate--;
		if (jetFuelConRate < 0) {
			zeroJFCR();
		}
	}
	
	private void zeroJFCR() {
		jetFuelConRate = 0;
	}
	
	public boolean isOnGround() {
		return onGround;
	}
	
	public void takeOff() {
		onGround = false;
		zeroFCR();
	}
	
	public void rotateTRight() {
		rRight = true;
		rLeft = false;
	}
	
	public void rotateTLeft() {
		rRight = false;
		rLeft = true;
	}
	
	public void stopRotate() {
		rRight = false;
		rLeft = false;
	}
	
	public Vector getTrajectory() {
		return new Vector(trajectory.getX(), -trajectory.getY());
	}
	
	public Point getTEndpoint() {
		return new Point((int)(position.x + width/2 + trajectory.getX()) - 10,
							(int)(position.y - 15 - trajectory.getY()));
	}
	
	public Point getFront() {
		if (facingRight) {
			return new Point((int)(position.x + width + 5),
							 (int)(position.y + 5));
		} else {
			return new Point((int)(position.x - 42),
					 (int)(position.y + 5));
		}
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void lowerHealth(int inHealth) {
		health -= inHealth;
		if (health <= 0) {
			health = 0;
			Game.isOver = true;
			Game.isPaused = false;
			Game.isPlaying = false;
			Game.gameControls.start.setText("Restart");
		}
	}
	
	public int getFuel() {
		return fuel;
	}
	
	public void setFuel(int inFuel) {
		fuel = inFuel;
		if (fuel < 0) {
			fuel = 0;
			canMoveGround = false;
			try {
				Robot bot = new Robot();
				bot.keyRelease(KeyEvent.VK_LEFT);
				bot.keyRelease(KeyEvent.VK_RIGHT);
				bot.keyRelease(KeyEvent.VK_UP);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getJetFuel() {
		return jetFuel;
	}
	
	public void setJetFuel(int inFuel) {
		jetFuel = inFuel;
		if (jetFuel < 0) {
			jetFuel = 0;
			canMoveAir = false;
			try {
				Robot bot = new Robot();
				bot.keyRelease(KeyEvent.VK_LEFT);
				bot.keyRelease(KeyEvent.VK_RIGHT);
				bot.keyRelease(KeyEvent.VK_UP);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}
		
	public boolean isMoveAir() {
		return canMoveAir;
	}
	
	public boolean isMoveGround() {
		return canMoveGround;
	}
	
	public int getBullets() {
		return bullets;
	}
	
	public void decrBullets() {
		bullets--;
		if (bullets == 0) {
			canShoot = false;
		}
	}
	
	public boolean canShoot() {
		return canShoot;
	}
	
	public int getBombs() {
		return bombs;
	}
	
	public void decrBombs() {
		bombs--;
		if (bombs == 0) {
			canBomb = false;
		}
	}
	
	public boolean canBomb() {
		return canBomb;
	}
	
	public int getMissiles() {
		return missiles;
	}
	
	public void decrMissiles() {
		missiles--;
		if (missiles == 0) {
			canMissile = false;
		}
	}
	
	public boolean canMissile() {
		return canMissile;
	}
}

class Ground extends PhysicsBody {
	
	public Ground() {
		type = "Ground";
		setData(99999999999999999999.0, new Point(0, 550), 
				new Vector(0.0, 0.0), "Ground.png", 1000, 50);
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(version));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	@Override
	public void accelerate() {
	}
	
	@Override
	public void move() {
	}
	
	@Override
	public void collide(PhysicsBody p) {
	}
}

class Bomb extends PhysicsBody {
	
	public boolean onScreen;
	
	public Bomb(Point startLoc, Vector inVel) {
		type = "Bomb";
		setData(10.0, startLoc, inVel, "Bomb.png", 20, 20);
		addGravity();
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(version));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	@Override
	public void collide(PhysicsBody p) {
		switch (p.type) {
		case "Ground":
			Explosion exp = new Explosion(new Point(
					 position.x - 10, position.y - 10));
			int i = Game.gp.addBody(exp);
			exp.setId(i);
			Game.gp.removeBody(this.id);
			break;
		case "Tank" :
			Explosion exp1 = new Explosion(new Point(
					 position.x - 10, position.y));
			int x = Game.gp.addBody(exp1);
			exp1.setId(x);
			Game.gp.removeBody(this.id);
			break;
		}
	
	}

}

class Explosion extends PhysicsBody {
	
	public int explosionLife;
	
	public Explosion(Point startLoc) {
		type = "Explosion";
		setData(0.0, startLoc, new Vector(0,0), "Explosion.png", 40, 40);
		
		explosionLife = 0;
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(version));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	@Override
	public void move() {
		if (explosionLife++ > 5) {
			Game.gp.removeBody(this.id);
		}
	}
}

class Bullet extends PhysicsBody {
	
	public Bullet(Point startLoc, String in, Vector inVel) {
		type = "Bullet";
		setData(0.0, startLoc, inVel, "Bullet" + in, 20, 20);
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(version));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
}

class Missile extends PhysicsBody {
	
	public Missile(Point startLoc, String in, Vector inVel) {
		type = "Missile";
		setData(0.0, startLoc, inVel, "Missile" + in, 40, 25);
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(version));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
}