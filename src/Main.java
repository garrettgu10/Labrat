import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.Clip;
import javax.swing.WindowConstants;

public class Main {
	static Skynet skynet;
	static playPanel pp;
	static playWindow pw;
	static initPanel ip = new initPanel();
	static stagemgr sm;
	static myPlayer gameMusic;
	public static final int framerate = 50;
	
	static Thread t;
	static int tempAlpha;
	static Timer gTimer = new Timer();
	public static void main(String[] args) {
		pw = new playWindow("Window");
		pw.add(ip);
		pw.getContentPane().addMouseListener(new initMouseListener());
		pw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pw.pack();
		pw.setVisible(true);
		gameMusic = new myPlayer(Clip.LOOP_CONTINUOUSLY,"Overriding_Concern");
		t = new Thread(){
			public void run(){
				tempAlpha = 250;
				while(!pw.ongoing){
					if(tempAlpha > 0){
						ip.bgPlayColor = new Color(ip.bgPlayColor.getRed(),
								ip.bgPlayColor.getGreen(),
								ip.bgPlayColor.getBlue(),tempAlpha);
						tempAlpha = ip.bgPlayColor.getAlpha()-12;
						ip.bgPlaySize += 30;
					}
					pw.revalidate();
					pw.repaint();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		try {
			Thread.sleep(450);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!pw.ongoing){
			restartInitBg();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//t.interrupt();
		}
	}
	static Polygon originalPlayButton = ip.generatePlayButton(50);
	static Polygon originalPlayButtonShadow = ip.generatePlayButton(50,5,5);
	static Polygon largerPlayButton = ip.generatePlayButton(70);
	static Polygon largerPlayButtonShadow = ip.generatePlayButton(70,10,10);
	
	public static void restartInitBg(){
		ip.playButton = largerPlayButton;
		ip.playButtonShadow = largerPlayButtonShadow;
		gTimer.schedule(new TimerTask(){
			public void run(){
				ip.playButton = originalPlayButton;
				ip.playButtonShadow = originalPlayButtonShadow;
			}
		}, 100);
		ip.bgPlaySize = 50;
		tempAlpha = 250;
		ip.bgPlayColor = new Color(ip.bgPlayColor.getRed(),
				ip.bgPlayColor.getGreen(),
				ip.bgPlayColor.getBlue(),tempAlpha);
		ip.generateNewbgPlayColor();
	}
	
	public static void begin(){
		pw.ongoing = true;
		gameMusic.stop();
		gameMusic = new myPlayer(Clip.LOOP_CONTINUOUSLY,"Pinball_Spring_160");
		pw.getContentPane().removeAll();
		pp = new playPanel();
		sm = new stagemgr(1);
		pw.add(pp);
		pw.pack();
		pw.revalidate();
		pw.repaint();
		pw.setVisible(true);
		try {
			skynet = new Skynet();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		sm.incrementer.start();
		pw.addKeyListener(new gKeyListener());
		pw.addFocusListener(new gFocusListener());
		pw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    blankCursorImg, new Point(0, 0), "blank cursor");
		pw.getContentPane().setCursor(blankCursor);
		//skynet.mouseMove(pp.circleCenter.x, pp.circleCenter.y);
		pp.addMouseMotionListener(new gMouseMotionListener());
		Thread refresher = new Thread(){
			public void run(){
				while(Main.pw.ongoing){
					pw.revalidate();
					pw.repaint();
					try {
						Thread.sleep(1000/framerate);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				pw.revalidate();
				pw.repaint();
			}
		};
		refresher.start();
	}

}
