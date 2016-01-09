import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class gFocusListener implements FocusListener{

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		Main.pw.fail();
	}

}
