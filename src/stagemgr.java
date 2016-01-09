public class stagemgr {
	int stagenumber = 1;
	double maxDrag = 0; //20.0 is ungodly hard; 30.0 is practically impossible
	int circleSpeed = 0; //Speed in taxicab geo of circle
	int circleRadius = 15*playPanel.u;
	
	Thread incrementer = new Thread(){
		public void run(){
			//first two increments are special and quick
			while(true){
				try {
					if(stagenumber == 1 || stagenumber == 2)
						Thread.sleep(6000);
					else
						Thread.sleep(12000);
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
		if(stagenumber > 20)
			maxDrag = 20;
		else
			maxDrag = (stagenumber-1);
		
		if(stagenumber < 3)
			circleRadius = 15*playPanel.u;
		else if(stagenumber < 13)
			circleRadius = (35-stagenumber)*playPanel.u/2;
		else
			circleRadius = 10*playPanel.u;
		
		if(stagenumber < 7)
			circleSpeed = 0;
		else if(stagenumber < 17)
			circleSpeed = (int)Math.ceil((stagenumber-10.0)/2);
		else
			circleSpeed = 5;
		
		if(stagenumber != 1)
			Main.pp.screenFlash(0);
		else
			Main.pp.screenFlash(12);
		Main.pp.bgColor = Main.pp.getColorFromStageNumber(stagenumber);
		Main.pp.txtColor = Main.pp.getComplementOf(Main.pp.bgColor);
	}

	public double getAngle() {
		return Math.PI/4;
	}
}