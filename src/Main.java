import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.WindowConstants;

public class Main {
	static Skynet skynet;
	static playPanel pp;
	static playWindow pw;
	static initPanel ip;
	static stagemgr sm;
	public static final int framerate = 50;
	public static void main(String[] args) {
		pw = new playWindow("Window");
		ip = new initPanel();
		pw.add(ip);
		pw.getContentPane().addMouseListener(new initMouseListener());
		pw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pw.pack();
		pw.setVisible(true);
		while(!pw.ongoing){
			if(ip.bgPlaySize*Math.sin(Math.PI/6) < ip.size.width/2){
				ip.bgPlaySize += 30;
			}else{
				ip.bgPlaySize = 0;
				ip.bgColor = ip.bgPlayColor;
				ip.generateNewbgPlayColor();
			}
			pw.revalidate();
			pw.repaint();
			try {
				Thread.sleep(1000/framerate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void begin(){
		pw.ongoing = true;
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
