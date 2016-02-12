import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

public class Skynet extends Robot{
	public double drag = 0;
	public int angle = 0; //degrees
	Thread mouseDragAndMoveCircle = new Thread(){
		public void run(){
			while(mainmain.m.pw.ongoing){
				mouseMoveWithDrag();
				if(mainmain.m.sm.circleSpeed != 0)
					mainmain.m.pp.moveCircle();
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
			try {
				Thread.sleep(1450);
			} catch (InterruptedException e1) {
				// whatevs
			}
			while(mainmain.m.pw.ongoing){
				try{
					updateDrag();
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						//whatevs
					}
				}catch(NullPointerException e){
					
				}
			}
		}
	};
	
	public void updateDrag() throws NullPointerException{
		if(mainmain.m.sm.maxDrag!=0)
			drag = Math.random()*(mainmain.m.sm.maxDrag-1.0)+1.0;
		angle = (int)(Math.random()*360);
	}
	
	public Skynet() throws AWTException {
		super();
		dragUpdater.start();
		mouseDragAndMoveCircle.start();
	}
	
	public void mouseMoveWithRespectToPanel(int x, int y){
		super.mouseMove(x+mainmain.m.pp.getLocationOnScreen().x, y+mainmain.m.pp.getLocationOnScreen().y);
	}
	
	public void mouseMoveWithDrag(){
		int dx = (int) Math.ceil(3*drag*Math.cos((360-angle)/180.0*Math.PI)/2);
		int dy = (int) Math.ceil(3*drag*Math.sin((360-angle)/180.0*Math.PI)/2);
		try{
			super.mouseMove(MouseInfo.getPointerInfo().getLocation().x+dx,MouseInfo.getPointerInfo().getLocation().y+dy);
		}catch(Exception e){
			
		}
	}
}
