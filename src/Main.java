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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class Main {
	Skynet skynet;
	playPanel pp;
	playWindow pw;
	initPanel ip = new initPanel();
	stagemgr sm;
	myPlayer gameMusic;
	public static final int framerate = 60;
	
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
		jf.setResizable(false);
		jf.add(new boardPanel());
		jf.pack();
		jf.setLocationRelativeTo(null);
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
		JOptionPane.showMessageDialog(null, "Coded by -- Garrett Gu\n"
				+ "\tCode under CC Attribution 4.0\n\n"
				+ "Other works used:\n"
				+ "\tEmoji provided free by http://emojione.com\n"
				+ "\t\t\tBy Attribution 4.0\n"
				+ "\t\"Pinball Spring 160\" Kevin MacLeod (incompetech.com)\n"
				+ "\t\t\tBy Attribution 3.0\n"
				+ "\t\"Electrodoodle\" Kevin MacLeod (incompetech.com)\n"
				+ "\t\t\tBy Attribution 3.0\n"
				+ "\t\"Game Over 02\" Notchfilter (freesound.org)\n"
				+ "\t\t\tBy Attribution 3.0\n\n"
				+ "http://creativecommons.org/licenses/by/4.0/\n"
				+ "http://creativecommons.org/licenses/by/3.0/","Credits",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void displayDisclaimer(){
		int response = JOptionPane.showConfirmDialog(null, 
				"By using this software with the stock server, you agree not to:\n"
				+ "\tConnect extra peripherals to the computer to gain an advantage over others.\n"
				+ "\tAttempt to modify the high score board with unauthorized software.\n"
				+ "\tUse a modified version of this software to gain an advantage over others.\n"
				+ "\tUse other software in conjunction with this software to gain an advantage over others.\n"
				+ "\tAttempt to find flaws in the software in order to gain an advantage over others.\n"
				+ "\tDestroy the world in thermonuclear war.\n\n"
				+ "Accounts violating this agreement will be removed.\n"
				+ "This software is provided AS-IS, with no warranty of any kind.\n\n"
				+ "DO YOU AGREE TO THE ABOVE TERMS?", "Licence Agreement", 
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION){
			System.exit(1);
		}
	}
	
	String elicitName(boolean crucial){
		boolean valid = false;
		String response = "Guest";
		while(!valid){
			response = JOptionPane.showInputDialog(null,"Make a new player name!\n"
					+ "Player names should be unique and of length 5-20.\n"
					+ "Names containing profanity/offensive language will be removed.","Name",JOptionPane.QUESTION_MESSAGE);
			if(response == null || response.equals("")){
				if(crucial){
					System.exit(0);
				}else{
					response = name;
				}
			}
			if(response.contains("+")){
				JOptionPane.showMessageDialog(null, "Sorry, names cannot contain the + character. Long story.",
						"Error",JOptionPane.ERROR_MESSAGE);
				continue;
			}
			if(response.length() > 20 || response.length() < 5){
				JOptionPane.showMessageDialog(null, "Sorry, names must have length 5-20.",
						"Error",JOptionPane.ERROR_MESSAGE);
				continue;
			}
			valid = true;
		}
		return response;
	}
	
	void updateName(){
		new myPlayer(0,"Coin_Drop");
		String newName = elicitName(false);
		Thread t = new Thread(){
			public void run(){
				try {
					Networking.updateName(newName, username);
				} catch (NoSuchAlgorithmException | IOException e) {
					//whatevs
				}
			}
		};
		t.start();
		System.out.println(newName);
		name=newName;
	}
	
	public void main(int music) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			//whatevs
		}
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
				String response = elicitName(true);
				Networking.makeNewUser(response,username);
				name = Networking.getName(username);
				highScore = Networking.getScore(username);
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			//whatevs
		}
		if(highScore < 0){
			JOptionPane.showMessageDialog(null, "You have been banned.\n"
					+ "Contact Garrett Gu at garrettgu777@gmail.com if you think this is an error.");
			System.exit(1);
		}
		int musicyesno = JOptionPane.showConfirmDialog(null,
				"Do you want music and sound effects?\n"
				+ "Please note that enabling music might help you with timing.\n"
				+ "Headphones are recommended.",
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
		pw.setResizable(false);
		URL iconURL = this.getClass().getClassLoader().getResource("resources/smiley.png");
		ImageIcon icon = new ImageIcon(iconURL);
		pw.setIconImage(icon.getImage());
		ip=new initPanel();
		pw.add(ip);
		pw.pack();
		pw.setLocationRelativeTo(null);
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
					ip.bgPlayColor = new Color(ip.bgPlayColor.getRed(),
							ip.bgPlayColor.getGreen(),
							ip.bgPlayColor.getBlue(),tempAlpha);
					tempAlpha -= 12;
					if(tempAlpha < 0){
						tempAlpha = 0;
					}
					ip.bgPlaySize += 30;
					ip.bgAngle +=0.05;
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
		Thread t = new Thread(){
			public void run(){
				try {
					Networking.updateGamesCounter();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		};
		t.start();
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
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");
		pw.getContentPane().setCursor(blankCursor);
		skynet.mouseMoveWithRespectToPanel(pp.circleCenter.x, pp.circleCenter.y);
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
