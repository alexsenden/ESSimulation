public class Main {
	public static void main(String[] args) {
		// Get Parameters
		OptionWindow.openOptionWindow();
		
		// Run Simulation
		Individual[][] population = runSimulation();
		
		// Open Display Window
		Window w = new Window("ES Simulation");
		
		// Run Listeners on Display Window for KB Input
		runListeners(w, population);
	}
	
	private static Individual[][] runSimulation() {
		// Create Population Arrays
		Individual[][] parentPopulation = new Individual[ProblemEnvironment.MAX_GENS + 1][ProblemEnvironment.MU];
		Individual[] offspringPopulation = new Individual[ProblemEnvironment.LAMBDA];
		
		// Set initial gen number
		int g = 0;
		
		// Create and evaluate initial gen
		for(int i = 0; i < ProblemEnvironment.MU; i++) {
			parentPopulation[g][i] = new Individual();
			parentPopulation[g][i].evaluate();
		}
		Utils.quickSort(parentPopulation[g]);
		
		// Create MAX_GENS new generations using crossover and mutation operators, and evaluate each individual
		while(g < ProblemEnvironment.MAX_GENS) {
			g++;
			
			for(int k = 0; k < ProblemEnvironment.LAMBDA; k++) {
				offspringPopulation[k] = Individual.crossover(parentPopulation[g-1][(int)(Math.random() * ProblemEnvironment.MU)], 
																parentPopulation[g-1][(int)(Math.random() * ProblemEnvironment.MU)]);
				offspringPopulation[k].mutate_y();
				offspringPopulation[k].mutate_s();
				offspringPopulation[k].evaluate();
			}
			
			parentPopulation[g] = Individual.selection(parentPopulation[g-1], offspringPopulation);
		}
		
		return parentPopulation;
	}
	
	private static void runListeners(Window w, Individual[][] population) {
		int gen = ProblemEnvironment.MAX_GENS;
		int ind = 0;
		boolean update = true;
		
		while(true) {
			if(KBListener.a && ind > 0 && !KBListener.used) {
				ind--;
				KBListener.used = true;
				update = true;
			} else if(KBListener.d && ind < ProblemEnvironment.MU - 1 && !KBListener.used) {
				ind++;
				KBListener.used = true;
				update = true;
			}
			else if(KBListener.w && gen < ProblemEnvironment.MAX_GENS && !KBListener.used) {
				gen++;
				KBListener.used = true;
				update = true;
			} else if(KBListener.s && gen > 0 && !KBListener.used) {
				gen--;
				KBListener.used = true;
				update = true;
			} else if(KBListener.ek && gen > 0 && !KBListener.used) {
				KBListener.used = true;
				w.screenshot();
			}
			if(update) {
				w.drawIndividual(population[gen][ind], gen, ind + 1);
				w.pushImage();
				update = false;
			}
			if(KBListener.ctrl) {
				KBListener.used = false;
			}
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
