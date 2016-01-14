import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class gKeyListener implements KeyListener{

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F){
			mainmain.m.pw.fail(mainmain.m.pp.getFinalScore());
		}
	}

}
