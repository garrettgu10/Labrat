import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class gMouseMotionListener implements MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(Math.pow(mouseX-mainmain.m.pp.circleCenter.x,2) + 
				Math.pow(mouseY-mainmain.m.pp.circleCenter.y,2) > 
				Math.pow(mainmain.m.sm.circleRadius, 2)){
			mainmain.m.pw.fail(mainmain.m.pp.getFinalScore());
		}
	}
}
