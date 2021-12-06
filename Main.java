import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
	public static void main(String[] args) {
		
		//runRandomSearch();
		
		//runTrials();
		
		///*
		
		if(args.length > 0) {
			try {
				ProblemEnvironment.MU = Integer.parseInt(args[0]);
				ProblemEnvironment.LAMBDA = Integer.parseInt(args[1]);
				ProblemEnvironment.MAX_GENS = Integer.parseInt(args[2]);
				ProblemEnvironment.MULTIOBJECTIVE = Boolean.parseBoolean(args[3]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Running Simulation...");
		
		Individual[][] population = runSimulation();

		System.out.println("Outputting Results...");
		
		int gen = ProblemEnvironment.MAX_GENS;
		int ind = 0;
		boolean update = true;
		
		Window w = new Window("ES Simulation");
		
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
		//*/
	}
	
	public static Individual[][] runSimulation() {
		Individual[][] parentPopulation = new Individual[ProblemEnvironment.MAX_GENS + 1][ProblemEnvironment.MU];
		Individual[] offspringPopulation = new Individual[ProblemEnvironment.LAMBDA];
		
		int g = 0;
		
		for(int i = 0; i < ProblemEnvironment.MU; i++) {
			parentPopulation[g][i] = new Individual();
			parentPopulation[g][i].evaluate();
		}
		Utils.quickSort(parentPopulation[g]);
		
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
	
	public static void runRandomSearch() {
		Individual ind;
		Individual best = new Individual();
		best.evaluate();
		for(int i = 0; i < 500000; i++) {
			ind = new Individual();
			ind.evaluate();
			if(ind.getf_y() > best.getf_y()) {
				best = ind;
			}
		}
		Window w = new Window("ES Simulation");
		w.drawIndividual(best, 0, 0);
		w.pushImage();
	}
	
	public static void runTrials() {
		int maxMu = 800;
		int maxLambda = 800;
		
		Individual[][] result;
		
		File out1 = new File("output.csv");
		
		try {
			out1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintStream o = null;
		try {
			o = new PrintStream(out1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		o.println("mu,lambda,value");
		
		ProblemEnvironment.MAX_GENS = 1000;
		for(ProblemEnvironment.MU = 25; ProblemEnvironment.MU <= maxMu; ProblemEnvironment.MU += 25) {
			for(ProblemEnvironment.LAMBDA = 25; ProblemEnvironment.LAMBDA <= maxLambda; ProblemEnvironment.LAMBDA += 25) {
				System.out.println("Running Simulation with Mu=" + ProblemEnvironment.MU + ", Lambda=" + ProblemEnvironment.LAMBDA);
				result = runSimulation();
				o.println(ProblemEnvironment.MU + "," + ProblemEnvironment.LAMBDA + "," + result[ProblemEnvironment.MAX_GENS][0].getf_y());
			}
		}
	}
}
