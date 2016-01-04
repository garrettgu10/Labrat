import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class myPlayer{
	Clip clip;
	static boolean play = true;
	public myPlayer(int loops, String name){
		if(play){
			try {
				URL url = this.getClass().getClassLoader().getResource("resources/"+name+".wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
				clip.loop(loops);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void stop(){
		try{
			clip.stop();
		}catch(NullPointerException e){
			//blah
		}
	}
	
	void start(int loops){
		try{
			clip.start();
			clip.loop(loops);
		}catch(NullPointerException e){
			//blah
		}
	}
}
