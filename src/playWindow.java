import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
				if(mainmain.m.highScore != 0){
					JOptionPane.showMessageDialog(this, "Congrats! You beat your high score!\n"
							+ "Your old high score was "+playPanel.formatTime(mainmain.m.highScore)+".\n"
							+ "Your new high score is "+playPanel.formatTime(score)+".");
				}else{
					JOptionPane.showMessageDialog(this, "Your score is "+playPanel.formatTime(score)+".");
				}
				Thread t = new Thread(){
					public void run(){
						try {
							Networking.changeScore(mainmain.m.username, score);
						} catch (NoSuchAlgorithmException | IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
				mainmain.m.highScore = score;
			}else{
				JOptionPane.showMessageDialog(this, "Your score is "+playPanel.formatTime(score)+".\n"
						+ "You did not beat your high score.");
			}
			mainmain.m.pw.revalidate();
			mainmain.m.pw.repaint();
			Thread t = new Thread(){
				public void run(){
					mainmain.interruptThreads();
					mainmain.m.init();
				}
			};
			t.start();
			
		}
		
	}
}
