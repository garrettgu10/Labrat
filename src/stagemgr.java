public class stagemgr {
	int stagenumber = 1;
	public double getMaxDrag(){
		return 10.0;
	}
	
	public void changeStage(int stagenumber){
		this.stagenumber = stagenumber;
	}

	public double getAngle() {
		return Math.PI/4;
	}
}