import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.WindowConstants;

public class init {
	static Skynet skynet;
	static playWindow pw = new playWindow("Window");
	static playPanel pp = new playPanel();
	public static final int framerate = 50;
	public static void main(String[] args) {
		pw.sm.incrementer.start();
		pw.addKeyListener(new gKeyListener());
		try {
			skynet = new Skynet();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		pw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pw.add(pp);
		pp.addMouseMotionListener(new gMouseMotionListener());
		pw.pack();
		pw.setVisible(true);
		BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    blankCursorImg, new Point(0, 0), "blank cursor");
		pw.getContentPane().setCursor(blankCursor);
		skynet.mouseMove(pp.size.width/2, pp.size.height/2);
		Thread refresher = new Thread(){
			public void run(){
				while(true){
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
		};
		refresher.start();
	}

}
