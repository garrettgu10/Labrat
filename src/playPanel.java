import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class playPanel extends JPanel{
	private static final long serialVersionUID = -5287891680186230119L;
	public static final int u = 15; //unit of size
	Dimension size = new Dimension(95*u,50*u);
	public static final int circleRadius = 10*u;
	public static final int cursorRadius = u;
	long startTime = System.currentTimeMillis();
	String time;
	Point mousePosition;
	public static final Font timerFont = new Font("Courier", Font.PLAIN, 10*u);
	public static final int ARROW_SIZE = u;
	double displayedDrag = 0;
	double displayedAngle = 0;
	public static final double changeSpeed = 10; //higher is slower
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(init.pw.ongoing)
			g2.setColor(Color.RED);
		else
			g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, size.width, size.height);
		g2.setColor(Color.WHITE);
		g2.fillOval(size.width/2-circleRadius, size.height/2-circleRadius, circleRadius*2, circleRadius*2);
		mousePosition = getMousePosition();
		g2.setColor(Color.GRAY);
		g2.fillOval(mousePosition.x-cursorRadius, mousePosition.y-cursorRadius, cursorRadius*2, cursorRadius*2);
		time = getTimeElapsed();
		g2.setFont(timerFont);
		g2.setColor(Color.WHITE);
		g2.drawString(time, size.width/2-g2.getFontMetrics().stringWidth(time)/2, 10*u);
		g2.setColor(Color.DARK_GRAY);
		if(init.skynet.drag != 0)
			drawDrag(g2);
	}
	
	void drawDrag(Graphics2D g2){
		displayedDrag += (init.skynet.drag-displayedDrag)/changeSpeed;
		if(init.skynet.angle-displayedAngle > 180){
			displayedAngle -= (360-init.skynet.angle+displayedAngle)/changeSpeed;
			if(displayedAngle < 0)
				displayedAngle += 360;
		}else if(displayedAngle-init.skynet.angle > 180){
			displayedAngle += (360-displayedAngle+init.skynet.angle)/changeSpeed;
		}else{
			displayedAngle += (init.skynet.angle-displayedAngle)/changeSpeed;
		}
		if(displayedAngle > 360)
			displayedAngle -= 360;
		drawArrow(g2,displayedAngle,displayedDrag);
	}
	
	void drawArrow(Graphics g, double angle, double drag) { 
		//modified from: http://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java
		Graphics2D g2 = (Graphics2D) g.create();
		
		int l = (int) (drag*u*3);
		AffineTransform at = AffineTransform.getTranslateInstance(size.width/2,size.height/2);
		at.concatenate(AffineTransform.getRotateInstance(2*Math.PI-angle/180*Math.PI));
		g2.transform(at);

		g2.drawLine(-l/2, 0, l/2, 0);
		g2.fillPolygon(new int[] {l/2, l/2-ARROW_SIZE, l/2-ARROW_SIZE, l/2},
			new int[] {0, -ARROW_SIZE, ARROW_SIZE, 0}, 4);
	}
	
	public String getTimeElapsed(){
		if(init.pw.ongoing){
			long millis = System.currentTimeMillis()-startTime;
			int second = (int)millis / 1000;
	
			time = String.format("%d:%02d", second, millis%1000/10);
		}
		return time;
	}
	
	public void startTimer(){
		startTime = System.currentTimeMillis();
	}
	
	public Dimension getPreferredSize(){
		return size;
	}
	
	public Point getMousePosition(){
		return new Point(MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x,
				MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y);
	}
}
