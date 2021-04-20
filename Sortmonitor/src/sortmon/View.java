package sortmon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class View extends JPanel {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model m;
	private Controller c;
	private int windowWidth, windowHeight;
	private float range;
	private float barWidth=5;
	private int space=2;
	private int marginLeft = 40;
	private int marginBottom = 520;
	private int marginBottom2 = 150;
	private float factorHeight = 3;
	private int nrOfScaleRows;
	private int mod = 1;
	private Color old = Color.GRAY;
	private Color akt = Color.LIGHT_GRAY;
	private Color used = Color.YELLOW;
	private Color compared = Color.RED;
	private Color scale = new Color(40,40,40);
	private Color headerCol1 = new Color(154,205,50);
	private Color headerCol2 = new Color(99,184,255);	
	private Font headers = new Font("Verdana", Font.PLAIN, 18);
	
	
			View(Model m, Controller c, int w, int h, int ns) {
					this.windowWidth = w;
					this.windowHeight = h;
					this.nrOfScaleRows = ns;
					this.m = m;		
					this.c = c;
					calc_View();
			}
			
			public void calc_View() {
				this.range = m.getRange();
				this.space = 2;
				if (m.getSize() > 470) { this.space = 1; }
				if (m.getSize() > 700) { this.space = 0; }
				
				this.barWidth = (int) (windowWidth-marginLeft*2) / m.getSize() - space;
				if (this.barWidth < 1) { this.barWidth = 1; }
				if (m.getSize() < 40) { barWidth--; }
				if (m.getSize() < 30) { barWidth--; }
				if (m.getSize() < 20) { barWidth--; }
				
				// Balkenhöhe
				this.factorHeight = 300 / this.range;
				
				// Skala
				if (nrOfScaleRows > this.range) { nrOfScaleRows = (int) range; }
		        if (nrOfScaleRows > 30 && nrOfScaleRows <= 50) { mod=2; } 
	        	else if (nrOfScaleRows > 50 && nrOfScaleRows <= 70) { mod = 3; }
	        	else if (nrOfScaleRows > 70 && nrOfScaleRows < 100) { mod = 4; }
	        	else if (nrOfScaleRows == 100) { mod = 5; }
	        	else { mod=1; }
			}
			
				public void paintComponent(Graphics g) {
			        Graphics2D g2d = (Graphics2D) g;
			        
			        // Hintergrund
			        g2d.setColor(Color.BLACK);
			        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			       		        	
			        g2d.setColor(scale);
			        for (int i = 0; i<=nrOfScaleRows; i++) {
			        	int ypos = windowHeight-marginBottom-((int)(range*factorHeight/nrOfScaleRows*i));
			        	g.drawLine(20, ypos , windowWidth-50, ypos);
			        	if (i%mod==0) { g.drawString(""+(int)(i*range/nrOfScaleRows), windowWidth-45, ypos+4); }			        
			        }
			        for (int i = 0; i<=nrOfScaleRows; i++) {
			        	int ypos = windowHeight-marginBottom2-((int)(range*factorHeight/nrOfScaleRows*i));
			        	g.drawLine(20, ypos , windowWidth-50, ypos);
			        	if (i%mod==0) { g.drawString(""+(int)(i*range/nrOfScaleRows), windowWidth-45, ypos+4); }
			        }
			        
			        // Beschriftung
			        g2d.setColor(headerCol1);
			        g.setFont(headers);
			        g.drawString("RANDOMIZED FIELD", windowWidth/2-128, (int) (windowHeight-marginBottom-(range*factorHeight+20)));
			        g2d.setColor(headerCol2);
				    g.drawString(c.getAlgo().toUpperCase(), windowWidth/2-100, (int) (windowHeight-marginBottom2-(range*factorHeight+20)));
			        			        
			        // Oldfield
			        g2d.setColor(old);
			        int[] old_field = m.getOldField();
			        int count = 0;
			        for (int f: old_field) {
			        	if (count == m.getCurrentOldIndex()) { g2d.setColor(used); }
			        	else { g2d.setColor(old); }
			        	g2d.fillRect(marginLeft+count*((int)barWidth+space), (int) (windowHeight-marginBottom-Math.floor(f*factorHeight)),(int)barWidth,(int) (f*factorHeight));
			        	count++;
			        }
			        
			        // Aktfield
			        g2d.setColor(akt);
			        int[] akt_field = m.getAktField();
			        count = 0;
			        for (int f: akt_field) {
			        	if (count == m.getUsedIndex()) { g2d.setColor(used); } 
			        	else if (count == m.getComparedIndex()) { g2d.setColor(compared); } 
			        	else { g2d.setColor(akt); }        	
			        	g2d.fillRect(marginLeft+count*((int)barWidth+space),(int) (windowHeight-marginBottom2-Math.floor(f*factorHeight)),(int)barWidth,(int) (f*factorHeight));
			        	count++;
			        }
			        
			}
				
		// Getter/Setter
		public int getScaleRows() {
			return nrOfScaleRows;
		}
		
		public void setScaleRows(int sr) {
			nrOfScaleRows = sr;
		}
				
}
