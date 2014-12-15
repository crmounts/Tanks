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
	public static TankController tc;
	public static TankController tc2;
	public static TankAI tai;
	
	public Game() {
		super("Tanks");
		
		
		
		// TODO - Create classes extending JPanel where appropriate
		// Main game field
		gf = new GameField();
		add(gf, BorderLayout.CENTER);
		
		tc = new TankController(gf.tank1);
		tc2 = new TankController(gf.tank2);
		tai = new TankAI(tc2);
		
		
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
				tai.determineMove();
			}
		};
		TimerTask fuelDec = new TimerTask() {
			public void run() {
				if (Game.isPlaying) {
					gf.tank1.setFuel(gf.tank1.getFuel() - gf.tank1.fConRate());
					gf.tank1.setJetFuel(gf.tank1.getJetFuel() - gf.tank1.jfConRate());
					gf.tank2.setFuel(gf.tank2.getFuel() - gf.tank2.fConRate());
					gf.tank2.setJetFuel(gf.tank2.getJetFuel() - gf.tank2.jfConRate());
					tai.determineBomb();
				}
			}
		};
		TimerTask ai = new TimerTask() {
			public void run() {
				if (Game.isPlaying) {
					tai.determineFire();
				}
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(gp, 0, 100);
		Timer timer2 = new Timer();
		timer2.scheduleAtFixedRate(updateScreen, 0, 5);
		Timer timer3 = new Timer();
		timer3.scheduleAtFixedRate(fuelDec, 0, 1000);
		Timer timer4 = new Timer();
		timer4.scheduleAtFixedRate(ai, 0, 400);
		
		
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
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (Game.isPlaying) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						tc.leftMove();
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						tc.rightMove();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						tc.upMove();
					} else if (e.getKeyChar() == 'w') {
						tc.missile();
					} else if (e.getKeyChar() == 's') {
						tc.shoot();
					} else if (e.getKeyChar() == 'a') {
						gf.tank1.rotateTLeft();
					} else if (e.getKeyChar() == 'd') {
						gf.tank1.rotateTRight();
					} else if (e.getKeyChar() == 'f') {
						tc.bomb();
					}
	            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
	            	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	            		tc.leftStop();
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						tc.rightStop();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						tc.upStop();
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
	}
}
