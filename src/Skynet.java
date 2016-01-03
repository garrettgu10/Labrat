import java.awt.AWTException;
import java.awt.Robot;

public class Skynet extends Robot{
	public double drag = 1.0;
	public int angle = 0;
	Thread dragUpdater = new Thread(){
		public void run(){
			while(true){
				drag = Math.random()*(playWindow.sm.getMaxDrag()-1.0)+1.0;
				angle = (int)(Math.random()*360);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	public Skynet() throws AWTException {
		super();
		dragUpdater.start();
	}
	
	public void mouseMove(int x, int y){
		super.mouseMove(x+init.pp.getLocationOnScreen().x, y+init.pp.getLocationOnScreen().y);
	}
}
