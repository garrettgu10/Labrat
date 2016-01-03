import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class gMouseMotionListener implements MouseMotionListener{
	int centerX = init.pp.getPreferredSize().width/2;
	int centerY = init.pp.getPreferredSize().height/2;
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
		if(Math.pow(mouseX-centerX,2) + Math.pow(mouseY-centerY,2) > Math.pow(radius, 2)){
			init.pw.fail();
		}else{
			init.pw.ongoing = true;
		}
	}

}
