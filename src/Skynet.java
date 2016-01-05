import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

public class Skynet extends Robot{
	public double drag = 0;
	public int angle = 0; //degrees
	Thread mouseDrag = new Thread(){
		public void run(){
			while(Main.pw.ongoing){
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
	Thread moveCircle = new Thread(){
		public void run(){
			while(Main.pw.ongoing){
				if(Main.sm.circleSpeed != 0)
					Main.pp.moveCircle();
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
			while(Main.pw.ongoing){
				try{
					if(Main.sm.maxDrag!=0)
						drag = Math.random()*(Main.sm.maxDrag-1.0)+1.0;
					angle = (int)(Math.random()*360);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}catch(NullPointerException e){
					
				}
			}
		}
	};
	public Skynet() throws AWTException {
		super();
		dragUpdater.start();
		mouseDrag.start();
		moveCircle.start();
	}
	
	public void mouseMoveWithRespectToPanel(int x, int y){
		super.mouseMove(x+Main.pp.getLocationOnScreen().x, y+Main.pp.getLocationOnScreen().y);
	}
	
	public void mouseMoveWithDrag(){
		int dx = (int) (2*drag*Math.cos((360-angle)/180.0*Math.PI));
		int dy = (int) (2*drag*Math.sin((360-angle)/180.0*Math.PI));
		try{
			super.mouseMove(MouseInfo.getPointerInfo().getLocation().x+dx,MouseInfo.getPointerInfo().getLocation().y+dy);
		}catch(Exception e){
			
		}
	}
}
