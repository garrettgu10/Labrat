public class stagemgr {
	int stagenumber = 1;
	double maxDrag = 0; //20.0 is ungodly hard; 30.0 is practically impossible
	int circleSpeed = 0; //Speed in taxicab geo of circle
	
	Thread incrementer = new Thread(){
		public void run(){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			changeStage(++stagenumber);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			changeStage(++stagenumber);
			//first two increments are special and quick
			while(true){
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				changeStage(++stagenumber);
			}
		}
	};
	
	public stagemgr(int stagenumber){
		changeStage(stagenumber);
	}
	
	public stagemgr(){
		this(1);
	}
	
	public void changeStage(int stagenumber){
		this.stagenumber = stagenumber;
		if(stagenumber > 10){
			maxDrag = 20;
		}else{
			maxDrag = (stagenumber-1)*2;
		}
		
		if(stagenumber < 5){
			Main.pp.circleRadius = 15*playPanel.u;
		}else if(stagenumber < 15){
			Main.pp.circleRadius = (35-stagenumber)*playPanel.u/2;
		}else{
			Main.pp.circleRadius = 10*playPanel.u;
		}
		
		if(stagenumber < 10){
			circleSpeed = 0;
		}else if(stagenumber < 20){
			circleSpeed = (stagenumber-10)/2;
		}else{
			circleSpeed = 5;
		}
		
		
		if(stagenumber != 1)
			Main.pp.screenFlash();
	}

	public double getAngle() {
		return Math.PI/4;
	}
}