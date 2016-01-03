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
	public static final int cursorRadius = u;
	Point circleCenter = new Point(size.width/2,size.height/2);
	boolean circleUp = true;
	boolean circleRight = true; // direction circle will move
	long startTime = System.currentTimeMillis();
	String time;
	Point mousePosition;
	public static final Font timerFont = new Font("Courier", Font.PLAIN, 10*u);
	public static final int ARROW_SIZE = u;
	double displayedDrag = 0;
	double displayedAngle = 0;
	public static final double changeSpeed = 10; //higher is slower
	int opacity = 0;
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(Main.pw.ongoing)
			g2.setColor(Color.RED);
		else
			g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, size.width, size.height);
		g2.setColor(Color.WHITE);
		g2.fillOval(circleCenter.x-Main.pw.sm.circleRadius, circleCenter.y-Main.pw.sm.circleRadius, 
				Main.pw.sm.circleRadius*2, Main.pw.sm.circleRadius*2);
		mousePosition = getMousePosition();
		g2.setColor(Color.GRAY);
		g2.fillOval(mousePosition.x-cursorRadius, mousePosition.y-cursorRadius, cursorRadius*2, cursorRadius*2);
		time = getTimeElapsed();
		g2.setFont(timerFont);
		g2.setColor(Color.WHITE);
		g2.drawString(time, size.width/2-g2.getFontMetrics().stringWidth(time)/2, 10*u);
		g2.setColor(Color.DARK_GRAY);
		if(Main.skynet.drag != 0)
			drawDrag(g2);
		g2.setColor(new Color(255,255,255,opacity*20));
		g2.fillRect(0, 0, size.width, size.height);
	}
	
	void drawDrag(Graphics2D g2){
		displayedDrag += (Main.skynet.drag-displayedDrag)/changeSpeed;
		if(Main.skynet.angle-displayedAngle > 180){
			displayedAngle -= (360-Main.skynet.angle+displayedAngle)/changeSpeed;
			if(displayedAngle < 0)
				displayedAngle += 360;
		}else if(displayedAngle-Main.skynet.angle > 180){
			displayedAngle += (360-displayedAngle+Main.skynet.angle)/changeSpeed;
		}else{
			displayedAngle += (Main.skynet.angle-displayedAngle)/changeSpeed;
		}
		if(displayedAngle > 360)
			displayedAngle -= 360;
		drawArrow(g2,displayedAngle,displayedDrag);
	}
	
	void drawArrow(Graphics g, double angle, double drag) { 
		//modified from: http://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java
		Graphics2D g2 = (Graphics2D) g.create();
		
		int l = (int) (drag*u);
		AffineTransform at = AffineTransform.getTranslateInstance(circleCenter.x,circleCenter.y);
		at.concatenate(AffineTransform.getRotateInstance(2*Math.PI-angle/180*Math.PI));
		g2.transform(at);

		g2.drawLine(-l/2, 0, l/2, 0);
		g2.fillPolygon(new int[] {l/2, l/2-ARROW_SIZE, l/2-ARROW_SIZE, l/2},
			new int[] {0, -ARROW_SIZE, ARROW_SIZE, 0}, 4);
	}
	
	public String getTimeElapsed(){
		if(Main.pw.ongoing){
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
	public void screenFlash(){
		Thread screenFlash = new Thread(){
			public void run(){
				opacity = 1;
				while(opacity < 12){
					opacity++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				while(opacity > 0){
					opacity--;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		screenFlash.start();
	}
	
	public void moveCircle(){
		for(int i = 0; i < Main.pw.sm.circleSpeed; i++){
			if(circleUp)
				circleCenter.y--;
			else
				circleCenter.y++;
			
			if(circleRight)
				circleCenter.x++;
			else
				circleCenter.x--;
			
			if(circleCenter.x-Main.pw.sm.circleRadius <= 0)
				circleRight = true;
			else if(circleCenter.x+Main.pw.sm.circleRadius >= size.width)
				circleRight = false;
			
			if(circleCenter.y-Main.pw.sm.circleRadius <= 0)
				circleUp = false;
			else if(circleCenter.y+Main.pw.sm.circleRadius >= size.height)
				circleUp = true;
		}
	}
}
