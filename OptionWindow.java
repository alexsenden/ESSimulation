import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionWindow {
	private static final String[] FIELDS = {"Mu (positive integer)", "Lambda (positive integer)", "Max Gens (positive integer)", "Multiobjective (true/false)", "Target Value"};
	
	public static void openOptionWindow() {
		// Initialize Function Variables
		JPanel optionWindow = new JPanel();
		JTextField[] textFields = new JTextField[FIELDS.length];
		int result;
		
		// Set Vertical Layout
		optionWindow.setLayout(new BoxLayout(optionWindow, BoxLayout.Y_AXIS));
		
		// Add header and spacing
		optionWindow.add(new JLabel("Enter ES Simulation Parameters"));
		optionWindow.add(Box.createVerticalStrut(8));
		
		// Initialize and add fields
		for(int i = 0; i < FIELDS.length; i++) {
			textFields[i] = new JTextField();
			optionWindow.add(new JLabel(FIELDS[i]));
			optionWindow.add(textFields[i]);
		}
		
		// Display window
		result = JOptionPane.showConfirmDialog(null, optionWindow, "ES Simulation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(result == JOptionPane.OK_OPTION) {
			try {
				// Actually set values
				ProblemEnvironment.MU = Integer.parseInt(textFields[0].getText());
				ProblemEnvironment.LAMBDA = Integer.parseInt(textFields[1].getText());
				ProblemEnvironment.MAX_GENS = Integer.parseInt(textFields[2].getText());
				ProblemEnvironment.MULTIOBJECTIVE = Boolean.parseBoolean(textFields[3].getText());
				ProblemEnvironment.TARGET_BONUS = Integer.parseInt(textFields[4].getText());
				
				// Ensure values in range
				if(ProblemEnvironment.MU <= 0 || ProblemEnvironment.LAMBDA <= 0 || ProblemEnvironment.MAX_GENS <= 0) {
					throw new NumberFormatException("Parameters out of range");
				}
			} catch (NumberFormatException e) {
				// If values not in range, try again
				openOptionWindow();
			}
		} else {
			System.exit(0);
		}
	}
}
