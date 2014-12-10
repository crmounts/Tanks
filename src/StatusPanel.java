import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* Sets up the users status panel and handles state related
 * to user status */
public class StatusPanel extends JPanel {

	
	// Whether or not game is being played
	public boolean playing = false;
	
	// State Labels
	private JLabel healthLabel;
	private JLabel fuelLabel;
	private JLabel jetFuelLabel;
	
	public StatusPanel() {
		super();
		// Creates the titled border around the status panel
		setBorder(BorderFactory.createTitledBorder("Status"));
		setLayout(new GridLayout(1, 5));
		
		reset();
		
		add(new JLabel(""));
		add(healthLabel);
		add(fuelLabel);
		add(jetFuelLabel);
		
		
	}
	
	// Reset Method
	public void reset() {
		healthLabel = new JLabel("Health: " + Game.gf.tank1.getHealth());
		fuelLabel = new JLabel("Fuel: " + Game.gf.tank1.getFuel());
		jetFuelLabel = new JLabel("Jet Fuel: " + Game.gf.tank1.getJetFuel());
		
		playing = true;
	}
	
	// Update labels method
	public void updateLabels() {
		healthLabel.setText("Health: " + Game.gf.tank1.getHealth());
		fuelLabel.setText("Fuel: " + Game.gf.tank1.getFuel());
		jetFuelLabel.setText("Jet Fuel: " + Game.gf.tank1.getJetFuel());
	}
	
	
}
