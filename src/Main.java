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
import javax.swing.JFrame;
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
	
	public void drawBoard(){
		new myPlayer(0,"Coin_Drop");
		JFrame jf = new JFrame("High Score Board");
		jf.setLocationRelativeTo (null);
		jf.add(new boardPanel());
		jf.pack();
		jf.repaint();
		jf.setVisible(true);
	}
	
	private boolean netIsAvailable() {
		try {
			final URL url = new URL("http://"+secrets.host);
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) { 
			return false; 
		}
	}
	
	public void displayCredits(){
		new myPlayer(0,"Coin_Drop");
		JOptionPane.showMessageDialog(null, "Coded by -- Garrett Gu\n\n"
				+ "Other works used:\n"
				+ "\tEmoji provided free by http://emojione.com\n"
				+ "\t\t\tBy Attribution 4.0\n"
				+ "\t\"Pinball Spring 160\" Kevin MacLeod (incompetech.com)\n"
				+ "\t\t\tBy Attribution 3.0\n"
				+ "\t\"Electrodoodle\" Kevin MacLeod (incompetech.com)\n"
				+ "\t\t\tBy Attribution 3.0\n\n"
				+ "http://creativecommons.org/licenses/by/4.0/\n"
				+ "http://creativecommons.org/licenses/by/3.0/","Credits",JOptionPane.INFORMATION_MESSAGE);
		new myPlayer(0,"Coin_Drop");
	}
	
	public void displayDisclaimer(){
		int response = JOptionPane.showConfirmDialog(null, "By using this software, you agree not to:\n"
				+ "\tConnect extra peripherals to the computer to gain an advantage over others.\n"
				+ "\tAttempt to hack the high score board.\n"
				+ "\tUse a modified version of this software to gain an advantage over others.\n"
				+ "\tDestroy the world in thermonuclear war.\n\n"
				+ "This software is provided AS-IS.\n\n"
				+ "Do you agree to the above terms?", "Licence Agreement", 
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION){
			System.exit(1);
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
				displayDisclaimer();
				boolean valid = false;
				String response = "Guest";
				while(!valid){
					response = JOptionPane.showInputDialog("Make a new player name!\n"
							+ "Player names should be unique and of length 5-15.\n"
							+ "Please note that you won't be able to change it later.\n"
							+ "Accounts with offensive/profane names will be removed.");
					if(response == null || response.equals("")){
						JOptionPane.showMessageDialog(null, "Please enter a player name.");
						continue;
					}
					if(response.contains("+")){
						JOptionPane.showMessageDialog(null, "Sorry, names cannot contain the + character. Long story.");
						continue;
					}
					if(response.length() > 15 || response.length() < 5){
						JOptionPane.showMessageDialog(null, "Sorry, names must have length 5-15.");
						continue;
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
		new myPlayer(0,"Coin_Drop");
		pw = new playWindow("Window");
		init();
	}
	Thread refresher;
	
	public void init(){
		pw.getContentPane().setCursor(Cursor.getDefaultCursor());
		pw.setVisible(false);
		pw = new playWindow("Lab Rat");
		ip=new initPanel();
		pw.add(ip);
		pw.pack();
		initMouseListener iml = new initMouseListener();
		pw.getContentPane().addMouseListener(iml);
		pw.getContentPane().addMouseMotionListener(iml);
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
		try {
			Networking.updateGamesCounter();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
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
