import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;


public class Game extends JFrame implements Runnable {
	
	public static GamePhysics gp = new GamePhysics();
	public static GameField gf;
	public static WeaponsPanel weaponsPanel;
	public static OpponentStatus oppStatusPanel;
	public static StatusPanel statusPanel;
	public static GameControls gameControls;
	public static boolean isPlaying = false;
	public static boolean isPaused = false;
	public static boolean isOver = false;
	
	public Game() {
		super("Tanks");
		
		// TODO - Create classes extending JPanel where appropriate
		// Main game field
		gf = new GameField();
		add(gf, BorderLayout.CENTER);
		
		// Display available weapons and selected weapon
		weaponsPanel = new WeaponsPanel();
		add(weaponsPanel, BorderLayout.WEST);
		
		// Indicate player health, fuel etc.
		statusPanel = new StatusPanel();
		add(statusPanel, BorderLayout.NORTH);
		
		
		// Indicate opponent health
		oppStatusPanel = new OpponentStatus();
		add(oppStatusPanel, BorderLayout.EAST);
		
		// Game controls, end game, etc.
		gameControls = new GameControls();
		add(gameControls, BorderLayout.SOUTH);
		
		// Lots of timers
		TimerTask updateScreen = new TimerTask() {
			public void run() {
				Game.gf.repaint();
				statusPanel.updateLabels();
				oppStatusPanel.updateLabels();
				weaponsPanel.updateLabels();
			}
		};
		TimerTask fuelDec = new TimerTask() {
			public void run() {
				gf.tank1.setFuel(gf.tank1.getFuel() - gf.tank1.fConRate());
				gf.tank1.setJetFuel(gf.tank1.getJetFuel() - gf.tank1.jfConRate());
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(gp, 0, 100);
		Timer timer2 = new Timer();
		timer2.scheduleAtFixedRate(updateScreen, 0, 5);
		Timer timer3 = new Timer();
		timer3.scheduleAtFixedRate(fuelDec, 0, 1000);
		
		
		// Allows all components of the frame to respond to key events
		KeyboardFocusManager manager = 
				KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new Dispatcher());
		
		
		// Put the frame on the screen
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void run() {
		
	}
	
	

	/* Main method for starting game */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
	
	private class Dispatcher implements KeyEventDispatcher {
		private boolean lf = false;
		private boolean rf = false;
		private boolean uf = false;
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (Game.isPlaying) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						gf.tank1.faceLeft();
						if ((gf.tank1.isOnGround() && gf.tank1.isMoveGround()) ||
								(!gf.tank1.isOnGround() && gf.tank1.isMoveAir())) {
							gf.tank1.addForce(GamePhysics.L_ACC);
							if (!lf) {
								fCUP();
								lf = true;
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						gf.tank1.faceRight();
						if ((gf.tank1.isOnGround() && gf.tank1.isMoveGround()) ||
								(!gf.tank1.isOnGround() && gf.tank1.isMoveAir())) {
							gf.tank1.addForce(GamePhysics.R_ACC);
							if (!rf) {
								fCUP();
								rf = true;
							}
						}	
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (gf.tank1.isMoveAir()) {
							gf.tank1.addForce(GamePhysics.U_ACC);
							if (!uf) {
								gf.tank1.incrJFCR();
								uf = true;
							}
							gf.tank1.takeOff();
						}
						
					} else if (e.getKeyChar() == 'w') {
						if (Game.gf.tank1.canMissile()) {
							Missile m;
							if (Game.gf.tank1.isFacingRight()) {
								m = new Missile(gf.tank1.getFront(), "Right.png",
										new Vector(6,0));
							} else {
								m = new Missile(gf.tank1.getFront(), "Left.png",
										new Vector(-6, 0));
							}
							int i = Game.gp.addBody(m);
							m.setId(i);
							Game.gf.tank1.decrMissiles();
						}
						
					} else if (e.getKeyChar() == 's') {
						if (Game.gf.tank1.canShoot()) {
							Bullet b;
							if (Game.gf.tank1.isFacingRight()) {
								b = new Bullet(gf.tank1.getFront(), "Right.png",
										new Vector(4,0));
							} else {
								b = new Bullet(gf.tank1.getFront(), "Left.png",
										new Vector(-4, 0));
							}
							int i = Game.gp.addBody(b);
							b.setId(i);
							Game.gf.tank1.decrBullets();
						}
						
					} else if (e.getKeyChar() == 'a') {
						gf.tank1.rotateTLeft();
					} else if (e.getKeyChar() == 'd') {
						gf.tank1.rotateTRight();
					} else if (e.getKeyChar() == 'f') {
						if (Game.gf.tank1.canBomb()) {
							Bomb b = new Bomb(gf.tank1.getTEndpoint(), 
									gf.tank1.getTrajectory().
									unitVect().scale(3));
							int i = Game.gp.addBody(b);
							b.setId(i);
							Game.gf.tank1.decrBombs();
						}
					}
	            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
	            	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	            		gf.tank1.removeForce(GamePhysics.L_ACC);
	            		fCDOWN();
	            		lf = false;
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						gf.tank1.removeForce(GamePhysics.R_ACC);
						fCDOWN();
						rf = false;
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						gf.tank1.removeForce(GamePhysics.U_ACC);
						fCDOWN();
						uf = false;
					} else if (e.getKeyChar() == 'a') {
						gf.tank1.stopRotate();
					} else if (e.getKeyChar() == 'd') {
						gf.tank1.stopRotate();
					} else if (e.getKeyChar() == 'f') {
						
					}
	            } else if (e.getID() == KeyEvent.KEY_TYPED) {
	            }
			}
            return false;
		}
		
		private void fCUP() {
			if (gf.tank1.isOnGround()) {
				gf.tank1.incrFCR();
			} else {
				gf.tank1.incrJFCR();
			}
		}
		
		private void fCDOWN() {
			if (gf.tank1.isOnGround()) {
				gf.tank1.decrFCR();
			} else {
				gf.tank1.decrJFCR();
			}
		}
	}
}
