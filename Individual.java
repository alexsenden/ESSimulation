import java.util.Random;

public class Individual implements Comparable<Individual> {
	
	// Domain of y
	private static final double Y_MAX = Math.PI / 2 - 0.001;
	private static final double Y_MIN = -Math.PI / 2 + 0.001;
	
	private static final Random rng = new Random(System.currentTimeMillis());
	
	// Endogenous Strategy Parameters
	private double[] y;
	private double[] s;
	
	// Objective Function Value
	private double f_y;
	
	// Generate Random Individual
	public Individual() {
		y = new double[ProblemEnvironment.MAX_STEPS];
		s = new double[ProblemEnvironment.MAX_STEPS];
		
		for(int i = 0; i < y.length; i++) {
			y[i] = (Math.random() * (Y_MAX - Y_MIN)) + Y_MIN;
		}
		
		for(int i = 0; i < s.length; i++) {
			s[i] = (Math.random() * 0.8) + 0.8;
		}
		
		f_y = Double.MIN_VALUE;
	}
	
	// Generate Individual With Specified Values
	public Individual(double[] y, double[] s) {
		this.y = y;
		this.s = s;
		
		f_y = Double.MIN_VALUE;
	}
	
	// Rho=2 Crossover Operator (BLX-0.5)
	public static Individual crossover(Individual parent1, Individual parent2) {
		double maxParent, minParent, maxOffspring, minOffspring;
		
		double[] y = new double[ProblemEnvironment.MAX_STEPS];
		double[] s = new double[ProblemEnvironment.MAX_STEPS];
		
		for(int i = 0; i < y.length; i++) {
			maxParent = Math.max(parent1.y[i], parent2.y[i]);
			minParent = Math.min(parent1.y[i], parent2.y[i]);
			
			maxOffspring = maxParent + (ProblemEnvironment.C * (maxParent - minParent));
			minOffspring = minParent - (ProblemEnvironment.C * (maxParent - minParent));
			
			y[i] = (Math.random() * (maxOffspring - minOffspring)) + minOffspring;
			
			if(y[i] > Y_MAX) {
				y[i] = Y_MAX;
			}
			if(y[i] < Y_MIN) {
				y[i] = Y_MIN;
			}
		}
		
		for(int i = 0; i < s.length; i++) {
			maxParent = Math.max(parent1.s[i], parent2.s[i]);
			minParent = Math.min(parent1.s[i], parent2.s[i]);
			
			maxOffspring = maxParent + (ProblemEnvironment.C * (maxParent - minParent));
			minOffspring = minParent - (ProblemEnvironment.C * (maxParent - minParent));
			
			s[i] = (Math.random() * (maxOffspring - minOffspring)) + minOffspring;
			
			if(s[i] <= 0) {
				s[i] = 0.001;
			}
		}
		
		return new Individual(y, s);
	}
	
	// y Mutation Operator (Normal Mutation)
	public void mutate_y() {
		for(int i = 0; i < y.length; i++) {
			y[i] = (s[i] * rng.nextGaussian()) + y[i];
			
			if(y[i] > Y_MAX) {
				y[i] = Y_MAX;
			}
			if(y[i] < Y_MIN) {
				y[i] = Y_MIN;
			}
		}
	}
	
	// s Mutation Operator (Lognormal Mutation)
	public void mutate_s() {
		double overallScalar = Math.exp(ProblemEnvironment.TAU0 * rng.nextGaussian());
		
		for(int i = 0; i < s.length; i++) {
			s[i] = overallScalar * s[i] * Math.exp(ProblemEnvironment.TAU * rng.nextGaussian());
		}
	}
	
	// Selection Operator
	public static Individual[] selection(Individual[] parents, Individual[] offspring) {
		Individual[] result = new Individual[ProblemEnvironment.MU];
		Individual[] mixed = new Individual[parents.length + offspring.length];
		
		for(int i = 0; i < parents.length; i++) {
			mixed[i] = parents[i];
		}
		for(int i = 0; i < offspring.length; i++) {
			mixed[i + parents.length] = offspring[i];
		}
		
		Utils.quickSort(mixed);
		
		for(int i = 0; i < ProblemEnvironment.MU; i++) {
			result[i] = mixed[i];
		}
		
		return result;
	}

	// Objective Function
	public void evaluate() {
		int steps = 0;
		boolean goal = false;
		
		double xpos = 0;
		double ypos = 0;
		
		double newX, newY;
		
		double terrainMultiplier;
		
		int sum;
		
		boolean[] targets = new boolean[ProblemEnvironment.TARGETS.length];
		int targetBonus = 0;
		
		while(steps < ProblemEnvironment.MAX_STEPS && !goal) {
			sum = 0;
			terrainMultiplier = 0;
			for(int i = 0; i < ProblemEnvironment.ENVIRONMENT.length && terrainMultiplier == 0; i++) {
				sum += ProblemEnvironment.ENVIRONMENT[i][0] * ProblemEnvironment.HEIGHT;
				if(i == ProblemEnvironment.ENVIRONMENT.length - 1 || ypos < sum) {
					terrainMultiplier = ProblemEnvironment.ENVIRONMENT[i][1];
				}
			}
			
			newX = xpos + Math.cos(y[steps]) * terrainMultiplier;
			newY = ypos + Math.sin(y[steps]) * terrainMultiplier;
			
			if(newY > ProblemEnvironment.HEIGHT) {
				newY = ProblemEnvironment.HEIGHT;
			} else if(newY < 0) {
				newY = 0;
			}
			
			if(ProblemEnvironment.MULTIOBJECTIVE) {
				for(int i = 0; i < targets.length; i++) {
					if(ProblemEnvironment.TARGETS[i].intersectsLine(xpos, ypos, newX, newY) && !targets[i]) {
						targets[i] = true;
						targetBonus += ProblemEnvironment.TARGET_BONUS;
					}
				}
			}
			
			xpos = newX;
			ypos = newY;
			
			goal = (xpos > ProblemEnvironment.WIDTH);
			
			steps++;
		}
		
		f_y = ProblemEnvironment.MAX_STEPS - steps + (xpos % ProblemEnvironment.WIDTH) / ProblemEnvironment.WIDTH + targetBonus;
	}
	
	// Mist Methods
	public double getY(int index) {
		return y[index];
	}
	
	public double getf_y() {
		return f_y;
	}
	
	@Override
	public int compareTo(Individual o) {
		return (int)Math.signum(this.f_y - o.f_y);
	}
}
