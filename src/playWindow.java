import javax.swing.JFrame;

public class playWindow extends JFrame{
	private static final long serialVersionUID = -8884164869409464557L;
	public boolean ongoing = false;
	

	public playWindow(String string) {
		super(string);
	}
	
	public void fail(){
		ongoing = false;
		Main.gameMusic.stop();
	}

}
