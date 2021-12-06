import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KBListener extends KeyAdapter {
	
	public static boolean w = false;
	public static boolean a = false;
	public static boolean s = false;
	public static boolean d = false;
	public static boolean ctrl = false;
	public static boolean ek = false;
	
	public static boolean used = false;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
       		case KeyEvent.VK_W:
	       		w = true;
	       		break;
       		case KeyEvent.VK_A:
	       		a = true;
	       		break;
       		case KeyEvent.VK_S:
	       		s = true;
	       		break;
       		case KeyEvent.VK_D:
	       		d = true;
	       		break;
       		case KeyEvent.VK_Q:
	       		System.exit(0);
	       		break;
       		case KeyEvent.VK_CONTROL:
	       		ctrl = true;
	       		break;
       		case KeyEvent.VK_E:
	       		ek = true;
	       		break;
		}
	}
	    
    @Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				w = false;
				used = false;
	    		break;
			case KeyEvent.VK_A:
				a = false;
				used = false;
	    		break;
			case KeyEvent.VK_S:
				s = false;
				used = false;
	    		break;
			case KeyEvent.VK_D:
				d = false;
				used = false;
	    		break;
			case KeyEvent.VK_CONTROL:
				ctrl = false;
	    		break;
			case KeyEvent.VK_E:
	       		ek = false;
	       		used = false;
	       		break;
		}
    }
}