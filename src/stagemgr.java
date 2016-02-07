public class stagemgr {
	int stagenumber = 1;
	double maxDrag = 0; 
	int circleSpeed = 0; //Speed in taxicab geo of circle
	int circleRadius = 15*playPanel.u;
	boolean showDrag = true;
	
	Thread incrementer = new Thread(){
		public void run(){
			//first two increments are special and quick
			while(mainmain.m.pw.ongoing){
				try {
					if(stagenumber == 1)
						Thread.sleep(4900);
					else if(stagenumber == 2)
						Thread.sleep(5500);
					else
						Thread.sleep(11500);
				} catch (InterruptedException e) {
					//whatevs
				}
				mainmain.m.pp.setCharacterIcon("shocked");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// whatevs
				}
				mainmain.m.pp.setCharacterIcon((mainmain.m.pp.snugChar? "snug":"smiley_30x30"));
				changeStage(stagenumber+1);
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
		if(stagenumber >= 8)
			maxDrag = 16;
		else if (stagenumber == 1)
			maxDrag = 0;
		else
			maxDrag = (stagenumber)*2;
		
		if(stagenumber < 4)
			circleRadius = 15*playPanel.u;
		else if(stagenumber < 14)
			circleRadius = (34-stagenumber)*playPanel.u/2;
		else
			circleRadius = 10*playPanel.u;
		
		if(stagenumber < 8)
			circleSpeed = 0;
		else if(stagenumber < 18)
			circleSpeed = (int)Math.ceil((stagenumber-8.0)/2);
		else
			circleSpeed = 5;
		
		if(stagenumber > 15)
			showDrag = false;
		else
			showDrag = true;
		
		if(stagenumber != 1)
			mainmain.m.pp.screenFlash(0);
		else
			mainmain.m.pp.screenFlash(12);
		
		mainmain.m.pp.bgColor = mainmain.m.pp.getColorFromNumber(stagenumber);
		mainmain.m.pp.txtColor = mainmain.m.pp.getComplementOf(mainmain.m.pp.bgColor);
	}

	public double getAngle() {
		return Math.PI/4;
	}
}