package sortmon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JButton butt, playButt;
	private JTextField inputFieldsize;
	ImageIcon pause = new ImageIcon(getClass().getResource("/pause.png"));
	ImageIcon play = new ImageIcon(getClass().getResource("/play.png"));
	private int delay = 100;  // Default-Animationsgeschwindigkeit
	private String algo = "";
	private boolean paused;
	
	
	public Controller() {
	// Entwurfsmuster
				m1 = new Model(this);
				v1 = new View(m1,this,windowWidth,windowHeight);		// = JPanel
				v1.setSize(windowWidth-10, windowHeight-110);
				setup_GUI();
	}

   // Steuermethode - ruft im Intervall von delay immer wieder die in String algo festgelegte Methode auf
   public void sort() {
	   if (t == null) {
		   t = new Timer( delay, new ActionListener() {
	   
		   public void actionPerformed( ActionEvent e ) {
			 if (!m1.isFinished() && !isPaused()) { 
				 	switch (getAlgo()) {
				 		case "Dummy Sort": m1.dummy_sort(); break;
				 		case "Selection Sort": m1.selection_sort(); break;			 		
				 		case "Gnome Sort": m1.gnome_sort(); break;			 		
				 		case "Bubble Sort": m1.bubble_sort(); break;
				 		case "Quick Sort": m1.quick_sort(); break;
				 		case "Quick Sort opt": m1.opt_quick_sort(); break;
				 		case "Merge Sort": break;
				 	}
				 }
			repaint();
		   }
		 });
		 t.start(); 
	   }
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
							setAlgo("");
							repaint();
							} 
					});	
					reset.add(init);
					
				JMenu sort = new JMenu("Algorithm");
					JMenuItem dummy = new JMenuItem("DummySort");
					dummy.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Dummy Sort");
							playButt.setIcon(pause);
							sort();
							} 
					});
					JMenuItem bubble = new JMenuItem("BubbleSort");
					bubble.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Bubble Sort");
							sort();
							} 
					});	
					JMenuItem selection = new JMenuItem("SelectionSort");
					selection.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Selection Sort");
							sort();
							} 
					});
					JMenuItem gnome = new JMenuItem("GnomeSort");
					gnome.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Gnome Sort");
							sort();
							} 
					});
					JMenuItem quick = new JMenuItem("QuickSort");
					quick.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Quick Sort");
							sort();
							} 
					});	
					JMenuItem optquick = new JMenuItem("QuickSort (optimiert)");
					optquick.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Quick Sort opt");
							sort();
							} 
					});
					JMenuItem merge = new JMenuItem("MergeSort");
					merge.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Merge Sort");
							sort();
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
					
					sort.add(dummy);
					sort.add(bubble);
					sort.add(gnome);					
					sort.add(selection);					
					sort.add(quick);
					sort.add(optquick);
					sort.add(merge);
			
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
				//elements.add(getSlider(), BorderLayout.CENTER);
				elements.add(getTimeOfCalculation(), BorderLayout.EAST);
					JPanel buttons = new JPanel();
					buttons.add(getPlayButton());
					buttons.add(getSlider());
				elements.add(buttons, BorderLayout.CENTER);
					
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
   
   private JSlider getSlider() {									// Regler der Animationsverzögerung
		speed = new JSlider(0,1000,delay);				// Minimal, Maximal, Default
		speed.setPreferredSize(new Dimension(300,50));	   
		ChangeListener speedChange = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (t != null) { t.setDelay((int)speed.getValue()); }		// falls Timer schon existiert/Algorithmus schon gewählt wurde
				delay = (int)speed.getValue();								// zudem wird die Vorbelegungsvariable delay geändert	
			}	
		};
		speed.addChangeListener(speedChange);
		return speed;
   }
     
   private JLabel getTimeOfCalculation() {
	   timeOfCalculation = new JLabel("Calculation time");
	   timeOfCalculation.setHorizontalAlignment(SwingConstants.CENTER);
	   timeOfCalculation.setVerticalAlignment(SwingConstants.TOP);   
	   timeOfCalculation.setPreferredSize(new Dimension(windowWidth/3-20,50));
	   return timeOfCalculation;
   }
   
   private JLabel getNrOfOperations() {
	   nrOfOperations = new JLabel("Number of operations");
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
		nrOfOperations.setText("<html><body><center>Number of operations<br>" + op + "</center><body></html>");		
	}
	
	public void setCalculationTime(long ct) {
		timeOfCalculation.setText("<html><body><center>Calculation time<br>" + ct + " ms</center><body></html>");
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
		           	v1.calc_barWidth();
		           	repaint();
		           } 
			   }
	       } 
			
			if(e.getSource() == playButt){
				   paused = !paused;
				   if (m1.isFinished()) { m1.init_field(false); }
				   if (paused) { playButt.setIcon(play); }
				   else { playButt.setIcon(pause); }
		       } 
		}
	

}
