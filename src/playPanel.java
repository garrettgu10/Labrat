import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class playPanel extends JPanel{
	private static final long serialVersionUID = -5287891680186230119L;
	Color bgColor = new Color(0,255,0);
	Color txtColor = new Color (255,0,255);
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
	public static final Font plainFont = new Font("Calibri", Font.PLAIN, 20);
	public static final String instructions = "Use your mouse to keep your character within the circle. "
			+ "This is harder than it seems at first.";
	public static final int ARROW_SIZE = u;
	double displayedDrag = 0;
	double displayedAngle = 0;
	public static final double changeSpeed = 5; //higher is slower
	int opacity = 0;
	Image character;
	Thread stopTheCheatrz = new Thread() {
		public void run(){
			Point mousePosition;
			while(true){
				try{
					mousePosition = getMousePosition();
					if(Math.pow(mousePosition.x-Main.pp.circleCenter.x,2) + 
							Math.pow(mousePosition.y-Main.pp.circleCenter.y,2) > 
							Math.pow(Main.sm.circleRadius, 2)){
						Main.pw.fail();
					}
				} catch (Exception e){
					//whatevs -- who's Chris?
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public playPanel(){
		super();
		setCharacterIcon("smiley_30x30");
		stopTheCheatrz.start();
	}
	
	void setCharacterIcon(String name){
		URL characterURL = this.getClass().getClassLoader().getResource("resources/"+name+".png");
		try {
			character = ImageIO.read(characterURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	Color getComplementOf(Color c){
		return new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue());
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(Main.pw.ongoing)
			g2.setColor(bgColor);
		else
			g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, size.width, size.height);
		g2.setColor(Color.WHITE);
		g2.fillOval(circleCenter.x-Main.sm.circleRadius, circleCenter.y-Main.sm.circleRadius, 
				Main.sm.circleRadius*2, Main.sm.circleRadius*2);
		mousePosition = getMousePosition();
		g2.setColor(Color.GRAY);
		time = getTimeElapsed();
		g2.setFont(timerFont);
		g2.setColor(Color.GRAY);
		g2.drawString(time, size.width/2-g2.getFontMetrics().stringWidth(time)/2+2, 8*u+2);
		g2.setColor(txtColor);
		g2.drawString(time, size.width/2-g2.getFontMetrics().stringWidth(time)/2, 8*u);
		g2.setFont(plainFont);
		g2.setColor(Color.WHITE);
		g2.drawString(Integer.toString(Main.sm.stagenumber),10,20);
		if(Main.sm.stagenumber == 1){
			g2.drawString(instructions, 
					size.width/2-g2.getFontMetrics().stringWidth(instructions)/2, 
					size.height-20);
		}
		g2.setColor(Color.DARK_GRAY);
		if(Main.skynet.drag != 0 && Main.sm.showDrag)
			drawDrag(g2);
		g2.drawImage(character,mousePosition.x-cursorRadius, mousePosition.y-cursorRadius,null);
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
		try{
			return new Point(
					MouseInfo.getPointerInfo().getLocation().x-
					this.getLocationOnScreen().x,
					MouseInfo.getPointerInfo().getLocation().y-
					this.getLocationOnScreen().y);
		}catch(NullPointerException e){
			Main.pw.fail();
			return new Point(0,0);
			
		}
	}
	public void screenFlash(int initOpacity){
		Thread screenFlash = new Thread(){
			public void run(){
				opacity = initOpacity;
				while(opacity < 12){
					opacity++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(opacity > 0){
					opacity--;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		screenFlash.start();
	}
	
	public void moveCircle(){
		for(int i = 0; i < Main.sm.circleSpeed; i++){
			if(circleUp)
				circleCenter.y--;
			else
				circleCenter.y++;
			
			if(circleRight)
				circleCenter.x++;
			else
				circleCenter.x--;
			
			if(circleCenter.x-Main.sm.circleRadius <= 0)
				circleRight = true;
			else if(circleCenter.x+Main.sm.circleRadius >= size.width)
				circleRight = false;
			
			if(circleCenter.y-Main.sm.circleRadius <= 0)
				circleUp = false;
			else if(circleCenter.y+Main.sm.circleRadius >= size.height)
				circleUp = true;
		}
	}
	
	Color getColorFromNumber(int stagenumber){ // s=stagenumber
		int s = stagenumber % 24;
		if(s < 8)
			return new Color(0,255-s*32,s*32);
		if(s < 16)
			return new Color((s-8)*32,0,255-(s-8)*32);
		
		return new Color(255-(s-16)*32,(s-16)*32,0);
	}
}
