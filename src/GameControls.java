import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;





public class GameControls extends JPanel {
	
	public JButton start;

	public GameControls() {
		
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (start.getText().equals("Start")) {
					Game.isPlaying = true;
					Game.isPaused = false;
					start.setText("Pause");
				} else if (start.getText().equals("Pause")) {
					Game.isPlaying = false;
					Game.isPaused = true;
					start.setText("Start");
				} else if (start.getText().equals("Restart")) {
					Game.gp.reset();
					Game.gf.tank1.reset();
					Game.gf.tank2.reset();
					Game.gf.tank1 = new Tank(new Point(25, 500), "TankBlue", true);
					Game.gf.tank2 = new Tank(new Point(900, 500), "TankRed", false);
					Game.gf.tank2.faceLeft();
					Game.tc = new TankController(Game.gf.tank1);
					Game.tc2 = new TankController(Game.gf.tank2);
					Game.tai = new TankAI(Game.tc2);
					Game.gf.ground = new Ground();
					Game.gp.addBody(Game.gf.tank1);
					Game.gp.addBody(Game.gf.tank2);
					Game.gp.addBody(Game.gf.ground);
					Game.isOver = false;
					Game.isPaused = false;
					Game.isPlaying = true;
					start.setText("Pause");
				}
			}
		});
		
		add(start);
	}
}
