import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class gMouseMotionListener implements MouseMotionListener{
	int radius = playPanel.circleRadius;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(Math.pow(mouseX-init.pp.circleCenter.x,2) + Math.pow(mouseY-init.pp.circleCenter.y,2) > Math.pow(radius, 2)){
			init.pw.fail();
		}
	}

}
