import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* Sets up the users status panel and handles state related
 * to user status */
public class OpponentStatus extends JPanel {

	
	// Whether or not game is being played
	public boolean playing = false;
	
	// State Labels
	private JLabel healthLabel;
	private JLabel fuelLabel;
	private JLabel jetFuelLabel;
	
	public OpponentStatus() {
		super();
		// Creates the titled border around the status panel
		setBorder(BorderFactory.createTitledBorder("Opp Status"));
		setLayout(new GridLayout(10, 1));
		
		reset();
		
		add(healthLabel);
		add(fuelLabel);
		add(jetFuelLabel);
		
		
	}
	
	// Reset Method
	public void reset() {
		healthLabel = new JLabel("Health: " + Game.gf.tank2.getHealth());
		fuelLabel = new JLabel("Fuel: " + Game.gf.tank2.getFuel());
		jetFuelLabel = new JLabel("Jet Fuel: " + Game.gf.tank2.getJetFuel());
		
		playing = true;
	}
	
	// Update labels method
	public void updateLabels() {
		healthLabel.setText("Health: " + Game.gf.tank2.getHealth());
		fuelLabel.setText("Fuel: " + Game.gf.tank2.getFuel());
		jetFuelLabel.setText("Jet Fuel: " + Game.gf.tank2.getJetFuel());
	}
	
	
}
