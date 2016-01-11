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
		if(Math.pow(mouseX-Main.pp.circleCenter.x,2) + 
				Math.pow(mouseY-Main.pp.circleCenter.y,2) > 
				Math.pow(Main.sm.circleRadius, 2)){
			Main.pw.fail();
		}
	}
}
