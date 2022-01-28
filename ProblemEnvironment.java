import java.awt.geom.Rectangle2D;

public class ProblemEnvironment {
	
	// Exogenous Strategy Parameters
	public static int MAX_GENS = 1000;
	public static int MU = 75;
	public static int LAMBDA = 300;
	public static final int RHO = 2;
	public static final double TAU0 = 1 / Math.sqrt(2 * ProblemEnvironment.MAX_STEPS);
	public static final double TAU = 1 / Math.sqrt(2 * Math.sqrt(ProblemEnvironment.MAX_STEPS));
	public static final double C = 0.5;
	
	// Problem Specification Variables
	public static boolean MULTIOBJECTIVE = false;
	public static int TARGET_BONUS = 5;
	public static final int HEIGHT = 500;
	public static final int WIDTH = 500;
	public static final int MAX_STEPS = (int)(((HEIGHT + WIDTH) / 25) * 1.2);
	public static final int TARGET_SIZE = ((HEIGHT + WIDTH) / 2) / 20;

	
	// 0th index is percentage of the screen the region will use, must sum to 1
	// 1st index is speed in that region
	public static final double[][] ENVIRONMENT = {
		{0.25, 25},
		{0.15, 35},
		{0.15, 30},
		{0.25, 50},
		{0.20, 35}
	};
	
	public static final Rectangle2D[] TARGETS = {
		new Rectangle2D.Double((int)(WIDTH * 0.2), (int)(HEIGHT * 0.2), TARGET_SIZE, TARGET_SIZE),
		new Rectangle2D.Double((int)(WIDTH * 0.4), (int)(HEIGHT * 0.65), TARGET_SIZE, TARGET_SIZE),
		new Rectangle2D.Double((int)(WIDTH * 0.8), (int)(HEIGHT * 0.5), TARGET_SIZE, TARGET_SIZE),
		new Rectangle2D.Double((int)(WIDTH * 0.6), (int)(HEIGHT * 0.15), TARGET_SIZE, TARGET_SIZE)
	};
}
