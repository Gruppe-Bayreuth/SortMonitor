package sortmon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class View extends JPanel {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model m;
	private Controller c;
	private int windowWidth, windowHeight;
	private int barWidth=5;
	private int space=2;
	private int marginLeft = 40;
	private int marginBottom = 520;
	private int marginBottom2 = 150;
	private int factorHeight = 3;
	private Color old = Color.GRAY;
	private Color akt = Color.LIGHT_GRAY;
	private Color used = Color.YELLOW;
	private Color compared = Color.RED;
	
	
			View(Model m, Controller c, int w, int h) {
					this.windowWidth = w;
					this.windowHeight = h;
					this.m = m;		
					this.c = c;
					calc_barWidth();
			}
			
			public void calc_barWidth() {
				this.space = 2;
				if (m.getSize() > 470) { this.space = 1; }
				if (m.getSize() > 700) { this.space = 0; }
				
				this.barWidth = (int) (windowWidth-marginLeft*2) / m.getSize() - space;
				if (this.barWidth < 1) { this.barWidth = 1; }
			}
			
				public void paintComponent(Graphics g) {
			        Graphics2D g2d = (Graphics2D) g;
			        g2d.setColor(Color.BLACK);
			        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			        
			        
			        // Oldfield
			        g2d.setColor(old);
			        g.drawString("Randomized field", marginLeft, windowHeight-marginBottom-103*factorHeight);
			        ArrayList<Integer> old_field = m.getOldField();
			        int count = 0;
			        for (int f: old_field) {
			        	if (count == m.getCurrentOldIndex()) { g2d.setColor(used); }
			        	else { g2d.setColor(old); }
			        	g2d.fillRect(marginLeft+count*(barWidth+space),windowHeight-marginBottom-f*factorHeight,barWidth,f*factorHeight);
			        	count++;
			        }
			        
			        // Aktfield
			        g2d.setColor(akt);
			        g.drawString(c.getAlgo(), marginLeft, windowHeight-marginBottom2-103*factorHeight);
			        ArrayList<Integer> akt_field = m.getAktField();
			        count = 0;
			        for (int f: akt_field) {
			        	if (count == m.getUsedIndex()) { g2d.setColor(used); } 
			        	else if (count == m.getComparedIndex()) { g2d.setColor(compared); } 
			        	else { g2d.setColor(akt); }        	
			        	g2d.fillRect(marginLeft+count*(barWidth+space),windowHeight-marginBottom2-f*factorHeight,barWidth,f*factorHeight);
			        	count++;
			        }
			        
			}
}
