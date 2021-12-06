import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JFrame window;
	private BufferedImage frame;
	
	public Window(String title) {
		setPreferredSize(new Dimension(ProblemEnvironment.WIDTH, ProblemEnvironment.HEIGHT));
		
		window = new JFrame(title);
		
		frame = new BufferedImage(ProblemEnvironment.WIDTH, ProblemEnvironment.HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setFocusable(true);
		window.requestFocus();
		window.add(this);
		window.pack();
		window.setVisible(true);
		window.addKeyListener(new KBListener());
	}
	
	public void pushImage() {
		paintComponent(this.getGraphics());
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(frame, 0, 0, this);
	}
	
	private void drawEnvironment(Graphics g) {
		int sum = 0;
		int colMult;
		double max = 0;
		
		for(int i = 0; i < ProblemEnvironment.ENVIRONMENT.length; i++) {
			if(ProblemEnvironment.ENVIRONMENT[i][1] > max) {
				max = ProblemEnvironment.ENVIRONMENT[i][1];
			}
		}
		colMult = (int)(255 / max);
		
		for(int i = 0; i < ProblemEnvironment.ENVIRONMENT.length; i++) {
			g.setColor(new Color((int)ProblemEnvironment.ENVIRONMENT[i][1] * colMult, (int)ProblemEnvironment.ENVIRONMENT[i][1] * colMult, (int)ProblemEnvironment.ENVIRONMENT[i][1] * colMult));
			g.fillRect(0, sum, ProblemEnvironment.WIDTH, (int)(ProblemEnvironment.HEIGHT * ProblemEnvironment.ENVIRONMENT[i][0]));
			sum += (int)(ProblemEnvironment.HEIGHT * ProblemEnvironment.ENVIRONMENT[i][0]);
		}
		
		if(ProblemEnvironment.MULTIOBJECTIVE) {
			g.setColor(Color.GREEN);
			for(int i = 0; i < ProblemEnvironment.TARGETS.length; i++) {
				g.fillRect((int)ProblemEnvironment.TARGETS[i].getX(), (int)ProblemEnvironment.TARGETS[i].getY(), (int)ProblemEnvironment.TARGETS[i].getWidth(), (int)ProblemEnvironment.TARGETS[i].getHeight());
			}
		}
	}
	
	public void drawIndividual(Individual ind, int gen, int num) {
		BufferedImage bimg = new BufferedImage(ProblemEnvironment.WIDTH, ProblemEnvironment.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = bimg.getGraphics();
		
		drawEnvironment(g);
		
		int steps = 0;
		boolean goal = false;
		
		double xpos = 0;
		double ypos = 0;
		
		double newX, newY;
		
		double terrainMultiplier;
		
		int sum;
		
		while(steps < ProblemEnvironment.MAX_STEPS && !goal) {
			sum = 0;
			terrainMultiplier = 0;
			for(int i = 0; i < ProblemEnvironment.ENVIRONMENT.length && terrainMultiplier == 0; i++) {
				sum += ProblemEnvironment.ENVIRONMENT[i][0] * ProblemEnvironment.HEIGHT;
				if(i == ProblemEnvironment.ENVIRONMENT.length - 1 || ypos < sum) {
					terrainMultiplier = ProblemEnvironment.ENVIRONMENT[i][1];
				}
			}
			
			newX = xpos + Math.cos(ind.getY(steps)) * terrainMultiplier;
			newY = ypos + Math.sin(ind.getY(steps)) * terrainMultiplier;
			
			if(ypos > ProblemEnvironment.HEIGHT) {
				ypos = ProblemEnvironment.HEIGHT;
			}
			
			if(steps % 2 == 0) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.BLUE);
			}
			
			g.drawLine((int)xpos, (int)ypos, (int)newX, (int)newY);
			
			xpos = newX;
			ypos = newY;
			
			goal = (xpos > ProblemEnvironment.WIDTH);
			
			steps++;
		}
		
		g.setColor(Color.YELLOW);
		
		g.drawString("Gen " + gen + "/" + ProblemEnvironment.MAX_GENS + ", Ind " + num + "/" + ProblemEnvironment.MU + "        f(y) = " + ind.getf_y(), 10, ProblemEnvironment.HEIGHT - 16);
		
		frame = bimg;
	}
	
	public void screenshot() {
		try {
			File outFile = new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS")) + ".png");
			System.out.println(outFile.getPath());
			outFile.createNewFile();
			ImageIO.write(frame, "png", outFile);
		} catch (IOException e) {
			System.out.println("Could not output screenshot. Printing Stack Trace.");
			e.printStackTrace();
		}
	}
}
