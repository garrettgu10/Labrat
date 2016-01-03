import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

public class Skynet extends Robot{
	public double drag = 0;
	public int angle = 0; //degrees
	Thread mouseDrag = new Thread(){
		public void run(){
			while(true){
				mouseMoveWithDrag();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	Thread dragUpdater = new Thread(){
		public void run(){
			while(true){
				if(init.pw.sm.maxDrag!=0)
					drag = Math.random()*(init.pw.sm.maxDrag-1.0)+1.0;
				angle = (int)(Math.random()*360);
				try {
					Thread.sleep(2000);
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
		mouseDrag.start();
	}
	
	public void mouseMove(int x, int y){
		super.mouseMove(x+init.pp.getLocationOnScreen().x, y+init.pp.getLocationOnScreen().y);
	}
	
	public void mouseMoveWithDrag(){
		int dx = (int) (drag*Math.cos((360-angle)/180.0*Math.PI));
		int dy = (int) (drag*Math.sin((360-angle)/180.0*Math.PI));
		super.mouseMove(MouseInfo.getPointerInfo().getLocation().x+dx,MouseInfo.getPointerInfo().getLocation().y+dy);
	}
}
