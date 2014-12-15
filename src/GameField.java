import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;




public class GameField extends JPanel {
	
	Ground ground;
	
	public Tank tank1;
	public Tank tank2;
	public BufferedImage win;
	public BufferedImage lose;
	public BufferedImage inst;
	
	public GameField() {
		super();
		setBackground(Color.WHITE);
		tank1 = new Tank(new Point(25, 500), "TankBlue", true);
		tank2 = new Tank(new Point(900, 500), "TankRed", false);
		tank2.faceLeft();
		ground = new Ground();
		Game.gp.addBody(tank1);
		Game.gp.addBody(tank2);
		Game.gp.addBody(ground);
	}
	
	// Size
	public Dimension getPreferredSize() {
		return new Dimension(1000,600);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (Game.isPlaying) {
			super.paintComponent(g);
			ground.draw(g);
			Iterator it = Game.gp.getBodies();
			while (it.hasNext()) {
				Map.Entry<Integer, PhysicsBody> pairs = 
						(Map.Entry<Integer, PhysicsBody>)it.next();
				pairs.getValue().draw(g);
				
			}
		} else if (Game.isPaused) {
			super.paintComponent(g);
			ground.draw(g);
			Iterator it = Game.gp.getBodies();
			while (it.hasNext()) {
				Map.Entry<Integer, PhysicsBody> pairs = 
						(Map.Entry<Integer, PhysicsBody>)it.next();
				pairs.getValue().draw(g);
				
			}
			g.drawString("Paused", 460, 290);
		} else if (Game.isOver) {
			super.paintComponent(g);
			try {
				if (win == null) {
					win = ImageIO.read(new File("YouWin.png"));
				}
				if (lose == null) {
					lose = ImageIO.read(new File("YouLose.jpg"));
				}
			} catch (IOException e) {
				System.out.println("Internal Error:" + e.getMessage());
			}
			if (Game.gf.tank1.getHealth() > 0) {
				g.drawImage(win, 250, 200, 500, 200, null);
			} else {
				g.drawImage(lose, 250, 200, 500, 200, null);
			}
		} else {
			super.paintComponent(g);
			try {
				if (inst == null) {
					inst = ImageIO.read(new File("GameInstructions.png"));
				}
			} catch (IOException e) {
				System.out.println("Internal Error:" + e.getMessage());
			}
			g.drawImage(inst, 0, 0, 1000, 600, null);
		}
	}
}
