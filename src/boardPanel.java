import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class boardPanel extends JPanel{
	private static final long serialVersionUID = 4733116427693970127L;
	ArrayList<String> names;
	ArrayList<Integer> scores;
	Dimension size = new Dimension(400,400);
	
	public boardPanel(){
		try{
			names = Networking.getTopScorers();
			scores = Networking.getTopScores();
		}catch(Exception e){
			names = new ArrayList<String>();
			names.add("Guest");
			scores = new ArrayList<Integer>();
			scores.add(0);
		}
		size.setSize(400,60+30*names.size());
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(225,225,255));
		g2.fillRect(0, 0, size.width,size.height);
		g2.setFont(new Font("Tahoma",Font.PLAIN,20));
		g2.setColor(Color.BLACK);
		g2.drawString("Names",43,30);
		g2.drawString("Scores", size.width-g2.getFontMetrics().stringWidth("Scores")-20, 30);
		g2.drawLine(0, 42, size.width, 42);
		String time;
		for(int i = 0; i < names.size();i++){
			g2.drawString(Integer.toString(i+1)+". "+names.get(i), 20, 70+30*i);
			time = playPanel.formatTime(scores.get(i));
			g2.drawString(time, size.width-
					g2.getFontMetrics().stringWidth(time)-20, 70+30*i);
		}
	}
	
	public Dimension getPreferredSize(){
		return size;
	}
}
