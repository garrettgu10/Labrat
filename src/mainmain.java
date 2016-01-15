
public class mainmain {
	static Main m;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		m = new Main();
		m.main(0);
	}
	
	static void interruptThreads(){
		m.sm.incrementer.interrupt();
		m.pw.removeAll();
		
		m.skynet.mouseDragAndMoveCircle.interrupt();
		m.pp.stopTheCheatrz.interrupt();
		m.skynet.dragUpdater.interrupt();
		m.refresher.interrupt();
	}

}
