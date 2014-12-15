

public class TankController {
	
	private boolean lf = false;
	private boolean rf = false;
	private boolean uf = false;
	public Tank tank;
	
	public TankController (Tank inTank) {
		tank = inTank;
	}
	
	public void bomb() {
		Vector vect = tank.getTrajectory().
				unitVect().scale(3);
		vect.setX(vect.getX() + tank.xvel());
		if (tank.canBomb()) {
			Bomb b = new Bomb(tank.getTEndpoint(), 
					vect);
			int i = Game.gp.addBody(b);
			b.setId(i);
			tank.decrBombs();
		}
	}
	
	public void shoot() {
		if (tank.canShoot()) {
			Bullet b;
			if (tank.isFacingRight()) {
				b = new Bullet(tank.getFront(), "Right.png",
						new Vector(4,0));
			} else {
				b = new Bullet(tank.getFront(), "Left.png",
						new Vector(-4, 0));
			}
			int i = Game.gp.addBody(b);
			b.setId(i);
			tank.decrBullets();
		}
	}
	
	public void missile() {
		if (tank.canMissile()) {
			Missile m;
			if (tank.isFacingRight()) {
				m = new Missile(tank.getFront(), "Right.png",
						new Vector(6,0));
			} else {
				m = new Missile(tank.getFront(), "Left.png",
						new Vector(-6, 0));
			}
			int i = Game.gp.addBody(m);
			m.setId(i);
			tank.decrMissiles();
		}
	}
	
	public void leftMove() {
		tank.faceLeft();
		if ((tank.isOnGround() && tank.isMoveGround()) ||
				(!tank.isOnGround() && tank.isMoveAir())) {
			tank.addForce(GamePhysics.L_ACC);
			if (!lf) {
				fCUP();
				lf = true;
			}
		}
	}
	
	public void rightMove() {
		tank.faceRight();
		if ((tank.isOnGround() && tank.isMoveGround()) ||
				(!tank.isOnGround() && tank.isMoveAir())) {
			tank.addForce(GamePhysics.R_ACC);
			if (!rf) {
				fCUP();
				rf = true;
			}
		}
	}
	
	public void upMove() {
		if (tank.isMoveAir()) {
			tank.addForce(GamePhysics.U_ACC);
			if (!uf) {
				tank.incrJFCR();
				uf = true;
			}
			tank.takeOff();
		}
	}
		
	public void leftStop() {
		if (lf) {
			tank.removeForce(GamePhysics.L_ACC);
			fCDOWN();
			lf = false;
		}
	}
		
	public void rightStop() {
		if (rf) {
			tank.removeForce(GamePhysics.R_ACC);
			fCDOWN();
			rf = false;
		}
	}
	
	public void upStop() {
		if (uf) {
			tank.removeForce(GamePhysics.U_ACC);
			fCDOWN();
			uf = false;
		}
	}


	private void fCUP() {
		if (tank.isOnGround()) {
			tank.incrFCR();
		} else {
			tank.incrJFCR();
		}
	}
	
	private void fCDOWN() {
		if (tank.isOnGround()) {
			tank.decrFCR();
		} else {
			tank.decrJFCR();
		}
	}
}
