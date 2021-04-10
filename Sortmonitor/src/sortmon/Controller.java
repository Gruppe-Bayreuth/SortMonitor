package sortmon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;


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
	private int delay = 100;  // Default-Animationsgeschwindigkeit
	private String algo = "";
	
	
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
			 if (m1.isFinished() == false) { 
				 	switch (getAlgo()) {
				 		case "Dummy Sort": m1.dummy_sort(); break;
				 		case "Selection Sort": m1.selection_sort(); break;			 		
				 		case "Bubble Sort": m1.bubble_sort(); break;
				 		case "Quick Sort": m1.quick_sort(); break;
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
			this.setTitle("Sort Monitor");
			this.add(v1);
			this.setVisible(true);
			
			// Menu
			JMenuBar menu = new JMenuBar();
				JMenu reset = new JMenu("Reset");
					JMenuItem init = new JMenuItem("Initialize field");	
					init.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {   					
							m1.init_field(true);
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
					JMenuItem quick = new JMenuItem("QuickSort");
					quick.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) { 
							m1.init_field(false);
							setAlgo("Quick Sort");
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
		
					
					sort.add(dummy);
					sort.add(bubble);
					sort.add(selection);					
					sort.add(quick);
					sort.add(merge);
			
					menu.add(reset);
					menu.add(sort);
					
			this.setJMenuBar(menu);
			
   }
   
    // Getter  und Setter  
	public String getAlgo() {
		return algo;
	}
	
	public void setAlgo(String a) {
		algo = a;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
