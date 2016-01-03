import javax.swing.JFrame;

public class playWindow extends JFrame{
	private static final long serialVersionUID = -8884164869409464557L;
	public boolean ongoing = true;
	public stagemgr sm = new stagemgr(10);

	public playWindow(String string) {
		super(string);
	}
	
	public void fail(){
		ongoing = false;
	}

}
