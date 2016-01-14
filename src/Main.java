import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Main {
	Skynet skynet;
	playPanel pp;
	playWindow pw;
	initPanel ip = new initPanel();
	stagemgr sm;
	myPlayer gameMusic;
	public static final int framerate = 50;
	
	Thread t;
	int tempAlpha;
	Timer gTimer = new Timer();
	boolean clockwise;
	String name = "Guest";
	int highScore = 0;
	String username = Networking.getUserName();
	
	private boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) { 
			return false; 
		}
	}
	
	public void main(int music) {
		if(!netIsAvailable()){
			JOptionPane.showMessageDialog(null, "Internet is not available. \nProgress will not be saved.");
			Networking.online=false;
		}
		try {
			if(Networking.userExists(username)){
				name = Networking.getName(username);
				highScore = Networking.getScore(username);
			}else{
				boolean valid = false;
				String response = "Guest";
				while(!valid){
					response = JOptionPane.showInputDialog("Make a new player name!\n"
							+ "Player names should be unique, and\n"
							+ "you won't be able to make a new one later.");
					if(response.equals("") || response == null){
						JOptionPane.showMessageDialog(null, "Please enter a user name.");
						continue;
					}
					if(response.contains("+")){
						JOptionPane.showMessageDialog(null, "Sorry, names cannot contain the + character.");
					}
					if(response.length() > 15 || response.length() < 5){
						JOptionPane.showMessageDialog(null, "Sorry, names must have length 5-15.");
					}
					valid = true;
				}
				Networking.makeNewUser(response,username);
				name = Networking.getName(username);
				highScore = Networking.getScore(username);
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			//whatevs
		}
		int musicyesno = JOptionPane.showConfirmDialog(null,
				"Do you want music and sound effects?\n"
				+ "Please note that enabling music might help you with timing.",
				"Music",JOptionPane.YES_NO_OPTION);
		if(musicyesno==JOptionPane.NO_OPTION){
			myPlayer.play = false;
		}
		pw = new playWindow("Window");
		init();
	}
	Thread refresher;
	
	public void init(){
		pw.getContentPane().setCursor(Cursor.getDefaultCursor());
		pw.setVisible(false);
		pw = new playWindow("Window");
		ip=new initPanel();
		pw.add(ip);
		pw.pack();
		pw.getContentPane().addMouseListener(new initMouseListener());
		pw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pw.pack();
		pw.setVisible(true);
		gameMusic = new myPlayer(Clip.LOOP_CONTINUOUSLY,"Electrodoodle");
		ip.updateBgColor.start();
		pw.revalidate();
		pw.repaint();
		refresher = new Thread(){
			public void run(){
				tempAlpha = 250;
				while(!pw.ongoing){
					if(tempAlpha > 0){
						ip.bgPlayColor = new Color(ip.bgPlayColor.getRed(),
								ip.bgPlayColor.getGreen(),
								ip.bgPlayColor.getBlue(),tempAlpha);
						tempAlpha = ip.bgPlayColor.getAlpha()-12;
						ip.bgPlaySize += 30;
						ip.bgAngle +=0.05;
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
		refresher.start();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!pw.ongoing){
			Thread x = new Thread(){
				public void run(){
					restartInitBg();
				}
			};
			x.start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//t.interrupt();
		}
	}
	Polygon originalPlayButton = ip.generatePlayButton(50);
	Polygon largerPlayButton = ip.generatePlayButton(70);
	
	public void restartInitBg(){
		ip.playButton = largerPlayButton;
		gTimer.schedule(new TimerTask(){
			public void run(){
				ip.playButton = originalPlayButton;
			}
		}, 100);
		ip.bgPlaySize = 50;
		ip.bgAngle = 0;
		tempAlpha = 250;
		ip.bgPlayColor = new Color(ip.bgPlayColor.getRed(),
				ip.bgPlayColor.getGreen(),
				ip.bgPlayColor.getBlue(),tempAlpha);
		ip.bgPlayColor = ip.generateNewRandomColor();
	}
	
	public void begin(){
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
				while(mainmain.m.pw.ongoing){
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
