import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;



public class WeaponsPanel extends JPanel {
	
	
	// State labels
	private JLabel bulletsLabel;
	private JLabel bombsLabel;
	private JLabel missilesLabel;
	

	// TODO
	public WeaponsPanel() {
		// Creates the titled border around the status panel
		setBorder(BorderFactory.createTitledBorder("Weapons"));
		setLayout(new GridLayout(10, 1));
		
		bulletsLabel = new JLabel("Bullets: " + Game.gf.tank1.getBullets());
		bombsLabel = new JLabel("Bombs: " + Game.gf.tank1.getBombs());
		missilesLabel = new JLabel("Missiles: " + Game.gf.tank1.getMissiles());
		
		add(bulletsLabel);
		add(bombsLabel);
		add(missilesLabel);

	}
	
	// Size
	public Dimension getPreferredSize() {
		return new Dimension(100,600);
	}
	
	
	// Update labels method
	public void updateLabels() {
		bulletsLabel.setText("Bullets: " + Game.gf.tank1.getBullets());
		bombsLabel.setText("Bombs: " + Game.gf.tank1.getBombs());
		missilesLabel.setText("Missiles: " + Game.gf.tank1.getMissiles());
	}
}
