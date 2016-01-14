import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class playWindow extends JFrame{
	private static final long serialVersionUID = -8884164869409464557L;
	public boolean ongoing = false;
	boolean updatedHighScore = false;
	

	public playWindow(String string) {
		super(string);
	}
	
	public void fail(int score){
		if(ongoing){
			ongoing = false;
			mainmain.m.gameMusic.stop();
			updatedHighScore = false;
			if(score > mainmain.m.highScore){
				updatedHighScore=true;
				JOptionPane.showMessageDialog(this, "You beat your high score!\n"
						+ "Your old high score was "+playPanel.formatTime(mainmain.m.highScore)+".\n"
						+ "Your new high score was "+playPanel.formatTime(score)+".");
				try {
					Networking.changeScore(mainmain.m.username, score);
				} catch (NoSuchAlgorithmException | IOException e) {
					e.printStackTrace();
				}
				mainmain.m.highScore = score;
			}
			mainmain.m.pw.revalidate();
			mainmain.m.pw.repaint();
			Thread t = new Thread(){
				public void run(){
					if(!updatedHighScore){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mainmain.interruptThreads();
					mainmain.m.init();
				}
			};
			t.start();
			
		}
		
	}
}
