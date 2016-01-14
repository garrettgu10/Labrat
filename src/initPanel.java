import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

class initMouseListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		if(mainmain.m.ip.playButton.contains(e.getX(),e.getY())){
			Thread t = new Thread(){
				int temp = 255;
				public void run(){
					temp = 255;
					new myPlayer(0,"Coin_Drop");
					for(int i = 0; i < 10; i++){
						temp = mainmain.m.ip.playButtonColor.getGreen();
						temp -= 15;
						try{
							mainmain.m.ip.playButtonColor = new Color(0,temp,0);
						}catch(Exception e){
							break;
						}
						mainmain.m.ip.playButtonShadowOffset -= 0.5;
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mainmain.m.gameMusic.stop();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mainmain.m.begin();
				}
			};
			t.start();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}

public class initPanel extends JPanel{
	private static final long serialVersionUID = -5493285222798401915L;
	Font titleFont = new Font("Courier",Font.PLAIN,100);
	public static final int u = 15; //unit of size
	Dimension size = new Dimension(95*u,50*u);
	public static final double cospi6 = Math.sqrt(3)/2;
	public static final double sinpi6 = 0.5;
	Polygon playButton = new Polygon(new int[] {size.width/2+50,
		(int)(size.width/2-sinpi6*50),
		(int)(size.width/2-sinpi6*50)},
			new int[] {size.height/2,(int) (size.height/2+cospi6*50),(int) (size.height/2-cospi6*50)},3);
	Color playButtonShadowColor = new Color(0,0,0,100);
	double playButtonShadowOffset = 5;
	Color bgPlayColor = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));
	Color bgColor = Color.WHITE;
	int bgPlaySize = 50;
	Color playButtonColor = Color.GREEN;
	double bgAngle = 0;
	Thread updateBgColor = new Thread(){
		public void run(){
			while(true){
				bgColor = generateNewBrightColor();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	String name;
	String score;
	public static final Font plainFont = new Font("Calibri",Font.PLAIN,20);
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		Graphics2D g3 = (Graphics2D)g.create(); // graphics for bgPlayButton
		Graphics2D g4 = (Graphics2D)g.create();
		g2.setColor(bgColor);
		g2.fillRect(0, 0, size.width, size.height);
		g3.setColor(bgPlayColor);
		
		g3.transform(AffineTransform.getRotateInstance(bgAngle, size.width/2,size.height/2));
		g3.fillPolygon(generatePlayButton(bgPlaySize));
		
		g4.setColor(playButtonShadowColor);
		g4.transform(AffineTransform.getTranslateInstance(playButtonShadowOffset, playButtonShadowOffset));
		g4.fillPolygon(playButton);
		g2.setColor(playButtonColor);
		g2.fillPolygon(playButton);
		g2.setFont(titleFont);
		g2.setColor(Color.BLACK);
		g2.drawString("Lab Rat",size.width/2-g2.getFontMetrics().stringWidth("Lab Rat")/2,size.height/4);
		g2.drawPolygon(playButton);
		name = "Name: "+mainmain.m.name;
		score = "High score: "+playPanel.formatTime(mainmain.m.highScore);
		g2.setFont(plainFont);
		g2.drawString(name, size.width/2-g2.getFontMetrics().stringWidth(name)/2, size.height/4*3);
		g2.drawString(score, size.width/2-g2.getFontMetrics().stringWidth(score)/2, size.height/4*3+30);
	}
	Polygon generatePlayButton(int s){
		return generatePlayButton(s,0,0);
	}
	
	Polygon generatePlayButton(int s, int xoffset,int yoffset){ // s=radius of circumscribed circle
		return new Polygon(new int[] {size.width/2+s+xoffset,
				(int)(size.width/2-sinpi6*s+xoffset),
				(int)(size.width/2-sinpi6*s+xoffset)},
					new int[] {size.height/2+yoffset,
							(int) (size.height/2+cospi6*s+yoffset),
							(int) (size.height/2-cospi6*s+yoffset)},3);
	}
	
	public Color generateNewRandomColor(){
		return new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));
	}
	
	public Color generateNewBrightColor(){
		return new Color((int)(100*Math.random()+155),(int)(100*Math.random()+155),(int)(100*Math.random()+155));
	}
	
	public Dimension getPreferredSize(){
		return size;
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
