

public class TankAI {
	
	private TankController controller;
	
	public TankAI(TankController inC) {
		controller = inC;
	}

	public void determineMove() {
		if(controller.tank.getDistance(Game.gf.tank1) > 400) {
			if (controller.tank.getXDistance(Game.gf.tank1) < 0) {
				controller.leftMove();
			} else if (controller.tank.getXDistance(Game.gf.tank1) > 0) {
				controller.rightMove();
			}
			if (controller.tank.getYDistance(Game.gf.tank1) < -100) {
				controller.upMove();
			} else {
				controller.upStop();
			}
		} else {
			controller.leftStop();
			controller.rightStop();
			controller.upStop();
			if (controller.tank.getXDistance(Game.gf.tank1) < 0) {
				controller.tank.faceLeft();
			} else if (controller.tank.getXDistance(Game.gf.tank1) > 0) {
				controller.tank.faceRight();
			}
			
		}
	}
	
	public void determineFire() {
		if (Math.abs(controller.tank.getYDistance(Game.gf.tank1)) < 5
				&& Math.abs(controller.tank.getXDistance(Game.gf.tank1))
				< 400) {
			if (controller.tank.canMissile()) {
				controller.missile();
			} else {
				controller.shoot();
			}
		} else if (Math.abs(controller.tank.getYDistance(Game.gf.tank1)) < 20
					&& Math.abs(controller.tank.getXDistance(Game.gf.tank1))
					< 750) {
			controller.shoot();
		}
	}
	
	public void determineBomb() {
		if(Math.abs(controller.tank.getDirToward(Game.gf.tank1) - 
				controller.tank.getTrajectory().getDirection()) >= 20) {
			if (controller.tank.getTEndpoint().getX() < Game.gf.tank1.position.x
					&& controller.tank.getTEndpoint().getY() < Game.gf.tank1.position.y) {
				controller.tank.rotateTRight();
			} else if (controller.tank.getTEndpoint().getX() > Game.gf.tank1.position.x
					&& controller.tank.getTEndpoint().getY() < Game.gf.tank1.position.y) {
				controller.tank.rotateTLeft();
			} else if (controller.tank.getTEndpoint().getX() < Game.gf.tank1.position.x
					&& controller.tank.getTEndpoint().getY() > Game.gf.tank1.position.y) {
				controller.tank.rotateTRight();
			} else if (controller.tank.getTEndpoint().getX() < Game.gf.tank1.position.x
					&& controller.tank.getTEndpoint().getY() < Game.gf.tank1.position.y) {
				controller.tank.rotateTLeft();
			}
		}
		if (Math.abs(controller.tank.getDirToward(Game.gf.tank1) - 
				controller.tank.getTrajectory().getDirection()) < 20) {
			if (Math.abs(controller.tank.getDistance(Game.gf.tank1)) < 300) {
				controller.bomb();
				controller.tank.stopRotate();
			}
		}

	}
}
