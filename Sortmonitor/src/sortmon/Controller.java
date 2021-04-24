package sortmon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int windowWidth = 1500;
	private int windowHeight = 900;
	private Model m1;
	private View v1;
	private Timer t;
	private JSlider speed;
	private JLabel nrOfOperations;
	private JLabel timeOfCalculation;
	private JFrame prefWindow;
	private JButton butt, playButt, scaleButt, rangeButt;
	private JTextField inputFieldsize;
	ImageIcon pause = new ImageIcon(getClass().getResource("/pause.png"));
	ImageIcon play = new ImageIcon(getClass().getResource("/play.png"));
	private int delay;  // Default-Animationsgeschwindigkeit
	private String algo = "";
	private boolean paused;
	private long calcTimeStart, calcTimeEnd, calculationTime;
	private Preferences prefs;
	
	public Controller() {
				// Voreinstellungen laden
				prefs = Preferences.userRoot().node(this.getClass().getName());
				int size = prefs.getInt("size", 200);	// Fieldsize
				int range = prefs.getInt("range", 100);	// Range of numbers: von 1....range
				int scalerows = prefs.getInt("scalerows", 20); // Scalerows
				this.delay = prefs.getInt("delay", 100); // Geschwindigkeit
				
				// Entwurfsmuster
				m1 = new Model(this, size, range);
				v1 = new View(m1,this,windowWidth,windowHeight,scalerows);		// = JPanel
				v1.setSize(windowWidth-10, windowHeight-110);
				setup_GUI();
	}

   // Steuermethode - ruft im Intervall von delay immer wieder die in String algo festgelegte Methode auf
   public void sort() {
	   if (t == null) {
		   t = new Timer( delay, new ActionListener() {
	   
		   public void actionPerformed( ActionEvent e ) {
			 if (!m1.isFinished() && !isPaused()) {
				    playButt.setIcon(pause);
				 	calctimeStart();
			 		switch (getAlgo()) {
				 		case "Dummy Sort": m1.dummy_sort(); break;
				 		case "Selection Sort": m1.selection_sort(); break;
				 		case "Insertion Sort": m1.insertion_sort(); break;			 						 		
				 		case "Gnome Sort": m1.gnome_sort(); break;			 		
				 		case "Bubble Sort": m1.bubble_sort(); break;
				 		case "Quick Sort": m1.quick_sort(); break;
				 		case "Quick Sort opt": m1.opt_quick_sort(); break;
				 		case "Merge Sort": m1.merge_sort(); break;
				 		case "Radix Sort": m1.radix_sort(); break;
				 	}
			 		calctimeEnd();
				 }
			 else { playButt.setIcon(play); }
		 		
			repaint();
		   }
		 });
		 t.start(); 
	   }
   }
   
   // Start des Algorithmus
   private void startAlgo(String which) {
	   	m1.init_field(false);
	   	resetTime();
		setAlgo(which);
		sort();
	}
   
   // Zeitmessung
   private void calctimeStart() {
	   calcTimeStart = System.currentTimeMillis();
   }
   
   private void calctimeEnd() {
	   calcTimeEnd = System.currentTimeMillis();
	   calculationTime += calcTimeEnd-calcTimeStart;
	   setCalculationTime(calculationTime);
	 }
   
   public void resetTime() {
	   calcTimeStart = 0;
	   calcTimeEnd = 0;
	   calculationTime = 0;
   }
   
   // GUI
   public void setup_GUI() {
	   		// Window
			this.setSize(windowWidth, windowHeight);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.setTitle("Sort Monitor");
			
			// Menu
			JMenuBar menu = new JMenuBar();
				JMenu reset = new JMenu("Reset");
					JMenuItem init = new JMenuItem("Initialize field");	
					init.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {   					
							m1.init_field(true);
							setNrOfOperations(0);
							resetTime();
							setAlgo("");
							repaint();
							} 
					});	
					reset.add(init);
					
				
					
				JMenu sort = new JMenu("Algorithm");
					JMenuItem dummy = new JMenuItem("DummySort");
					dummy.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Dummy Sort");
							} 
					});
					JMenuItem bubble = new JMenuItem("BubbleSort");
					bubble.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Bubble Sort");
							} 
					});	
					JMenuItem selection = new JMenuItem("SelectionSort");
					selection.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Selection Sort");					
							} 
					});
					JMenuItem insertion = new JMenuItem("InsertionSort");
					insertion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Insertion Sort");					
							} 
					});
					JMenuItem gnome = new JMenuItem("GnomeSort");
					gnome.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Gnome Sort");
							} 
					});
					JMenuItem quick = new JMenuItem("QuickSort");
					quick.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Quick Sort");
							} 
					});	
					JMenuItem optquick = new JMenuItem("QuickSort (optimiert)");
					optquick.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Quick Sort opt");
							} 
					});
					JMenuItem merge = new JMenuItem("MergeSort");
					merge.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Merge Sort");
							} 
					});
					JMenuItem radix = new JMenuItem("RadixSort");
					radix.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							startAlgo("Radix Sort");
							} 
					});
				JMenu preferences = new JMenu("Preferences");
					JMenuItem fieldsize = new JMenuItem("Fieldsize");
					fieldsize.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFrame prefWindow = getPrefWindow();
							prefWindow.setVisible(true);
							} 
					});	
					preferences.add(fieldsize);
					
					JMenuItem scaleRows = new JMenuItem("Scalerows");
					scaleRows.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFrame scaleRowWindow = getscaleRowWindow();
							scaleRowWindow.setVisible(true);
							} 
					});	
					
					JMenuItem range = new JMenuItem("Range of numbers");
					range.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFrame rangeWindow = getRangeWindow();
							rangeWindow.setVisible(true);
							} 
					});	
					
					preferences.add(fieldsize);
					preferences.add(scaleRows);
					preferences.add(range);
					
					sort.add(dummy);
					sort.add(bubble);
					sort.add(gnome);					
					sort.add(selection);
					sort.add(insertion);					
					sort.add(quick);
					sort.add(optquick);
					sort.add(merge);
					sort.add(radix);
			
					menu.add(reset);
					menu.add(sort);
					menu.add(preferences);
					
			this.setJMenuBar(menu);
			
			// Layout
			this.setLayout(new BorderLayout());
			this.add(v1, BorderLayout.CENTER);
				JPanel elements = new JPanel();					// Untere Bedienleiste
				elements.setSize(windowWidth-40, 50);
				elements.setLayout(new BorderLayout());			
				elements.add(getNrOfOperations(), BorderLayout.WEST);
				elements.add(getTimeOfCalculation(), BorderLayout.EAST);
					JPanel buttons = new JPanel();
					buttons.add(getPlayButton());
					buttons.add(getSlider());
				elements.add(buttons, BorderLayout.CENTER);
				elements.setVisible(true);
					
			this.add(elements, BorderLayout.SOUTH); 
   }
   
   // Elements
   private JButton getPlayButton() {
	   playButt = new JButton();
	   playButt.setMaximumSize(new Dimension(50,20));
	   playButt.setFocusable(false);
	   playButt.setIcon(play);
	   playButt.addActionListener(this);		
	   return playButt;
   }
            
   private JFrame getPrefWindow() {
	   	prefWindow = new JFrame("Preferences");
	   	prefWindow.setSize(300,150);
	   	prefWindow.setLocationRelativeTo(this);
		JPanel pan = new JPanel();
		JLabel fs = new JLabel("Fieldsize (max. 1400)");
		pan.add(fs);
		inputFieldsize = new JTextField(""+m1.getSize(),4);
		pan.add(inputFieldsize);
		butt = new JButton("OK");
		butt.addActionListener(this);
		pan.add(butt);
		prefWindow.add(pan);
		return prefWindow;
   }
   
   private JFrame getscaleRowWindow() {
	   	prefWindow = new JFrame("Preferences");
	   	prefWindow.setSize(300,150);
	   	prefWindow.setLocationRelativeTo(this);
		JPanel pan = new JPanel();
		JLabel fs = new JLabel("Scalerows (max. 100)");
		pan.add(fs);
		inputFieldsize = new JTextField(""+v1.getScaleRows(),4);
		pan.add(inputFieldsize);
		scaleButt = new JButton("OK");
		scaleButt.addActionListener(this);
		pan.add(scaleButt);
		prefWindow.add(pan);
		return prefWindow;
  }
   
   private JFrame getRangeWindow() {
	   	prefWindow = new JFrame("Preferences");
	   	prefWindow.setSize(300,150);
	   	prefWindow.setLocationRelativeTo(this);
		JPanel pan = new JPanel();
		JLabel fs = new JLabel("Range of numbers (max. 1000)");
		pan.add(fs);
		inputFieldsize = new JTextField(""+m1.getRange(),4);
		pan.add(inputFieldsize);
		rangeButt = new JButton("OK");
		rangeButt.addActionListener(this);
		pan.add(rangeButt);
		prefWindow.add(pan);
		return prefWindow;
 }
   
   private JSlider getSlider() {									// Regler der Animationsverzögerung
		speed = new JSlider(0,1000,delay);				// Minimal, Maximal, Default
		speed.setPreferredSize(new Dimension(300,50));	   
		ChangeListener speedChange = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (t != null) { t.setDelay((int)speed.getValue()); }		// falls Timer schon existiert/Algorithmus schon gewählt wurde
				delay = (int)speed.getValue();								// zudem wird die Vorbelegungsvariable delay geändert	
				prefs.putInt("delay", delay);
			}	
		};
		speed.addChangeListener(speedChange);
		return speed;
   }
     
   private JLabel getTimeOfCalculation() {
	   timeOfCalculation = new JLabel();
	   timeOfCalculation.setHorizontalAlignment(SwingConstants.CENTER);
	   timeOfCalculation.setVerticalAlignment(SwingConstants.TOP);   
	   timeOfCalculation.setPreferredSize(new Dimension(windowWidth/3-20,50));
	   return timeOfCalculation;
   }
   
   private JLabel getNrOfOperations() {
	   nrOfOperations = new JLabel();
	   nrOfOperations.setHorizontalAlignment(SwingConstants.CENTER);
	   nrOfOperations.setVerticalAlignment(SwingConstants.TOP);
	   nrOfOperations.setPreferredSize(new Dimension(windowWidth/3-20,50));
	   return nrOfOperations;
   }
   
	public boolean isParsable(String inp) {
		try {
	        Integer.parseInt(inp);
	        return true;
	    } catch (final NumberFormatException e) {
	        return false;
	    }	
	}
   
    // Getter  und Setter  
	public String getAlgo() {
		return algo;
	}
	
	public void setAlgo(String a) {
		algo = a;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setNrOfOperations(int op) {
		nrOfOperations.setText("<html><body><br><center>Number of operations<br>" + op + "</center><body></html>");		
	}
	
	public void setCalculationTime(long ct) {
		timeOfCalculation.setText("<html><body><br><center>Calculation time<br>" + ct + " ms</center><body></html>");
	}
	
	   // ActionListener
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == butt){
			String inp = inputFieldsize.getText();
			   if (isParsable(inp)) {
		           int i = Integer.parseInt(inp);
		           if (i<3 || i>1400) { inputFieldsize.setText(""+m1.getSize()); }
		           else { 
		           	prefWindow.dispose();
		           	m1.resize_field(i);
		           	v1.calc_View();
		           	prefs.putInt("size", i);
		           	repaint();
		           } 
			   }
	       } 
			
			if(e.getSource() == scaleButt){
				String inp = inputFieldsize.getText();
				   if (isParsable(inp)) {
			           int i = Integer.parseInt(inp);
			           if (i<1 || i>100) { inputFieldsize.setText(""+v1.getScaleRows()); }
			           else { 
			           	prefWindow.dispose();
			           	v1.setScaleRows(i);
			           	v1.calc_View();
			           	prefs.putInt("scalerows", i);			           
			           	repaint();
			           } 
				   }
		       } 
			if(e.getSource() == rangeButt){
				String inp = inputFieldsize.getText();
				   if (isParsable(inp)) {
			           int i = Integer.parseInt(inp);
			           if (i<3 || i>1000) { inputFieldsize.setText(""+m1.getRange()); }
			           else { 
			           	prefWindow.dispose();
			           	m1.setRange(i);
			           	m1.init_field(true);
			           	v1.calc_View();
			           	prefs.putInt("range", i);			           
			           	repaint();
			           } 
				   }
		       } 
			
			if(e.getSource() == playButt){
				   paused = !paused;
				   if (m1.isFinished()) { m1.init_field(false); resetTime(); }
				   } 
		}
	

}
