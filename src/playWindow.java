import javax.swing.JFrame;

public class playWindow extends JFrame{
	private static final long serialVersionUID = -8884164869409464557L;
	public boolean ongoing = true;
	public static stagemgr sm = new stagemgr();

	public playWindow(String string) {
		super(string);
	}
	
	public void fail(){
		ongoing = false;
	}

}
