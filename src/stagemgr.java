public class stagemgr {
	int stagenumber = 1;
	double maxDrag = 0; //20.0 is ungodly hard; 30.0 is practically impossible
	Thread incrementer = new Thread(){
		public void run(){
			while(true){
				try {
					Thread.sleep(10000);
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
		if(stagenumber <= 6){
			maxDrag = (stagenumber-1)*4;
		}else{
			maxDrag = 20;
		}
		if(stagenumber >=5 && stagenumber <= 10){
			Main.pp.circleRadius = (20-stagenumber)*Main.pp.u;
		}
		if(stagenumber != 1)
			Main.pp.screenFlash();
	}

	public double getAngle() {
		return Math.PI/4;
	}
}