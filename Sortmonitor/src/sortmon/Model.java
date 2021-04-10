package sortmon;
import java.util.ArrayList;
import java.util.Random;

public class Model {
	
	Controller c;
	private int size = 200;											// Größe des Arrays / Zahl der Balken
	private ArrayList<Integer> oldfield = new ArrayList<Integer>();
	private ArrayList<Integer> aktfield = new ArrayList<Integer>();
	private int range = 100;										// verwendete Zahlenwerte: 1 - range
	
	private int currentOldIndex, usedIndex, comparedIndex;			// farblich markierte Indices
	private boolean ready;
	
	private int nOps;												// number Of operations
	private int caT;												// calculation Time
	int counter =0;
	
	//Standard-SortierVariablen
	private int step = 0;
	private int step2 = 0;
	
	// SelectionSort-Vars
	private ArrayList<Integer> unsorted = new ArrayList<Integer>();
	private ArrayList<Integer> sorted = new ArrayList<Integer>();
	
	// Quicksort-Vars
	int pivot, left, right;
	private ArrayList<Integer> inplace = new ArrayList<Integer>();
	private ArrayList<Integer> leftArr = new ArrayList<Integer>();
	private ArrayList<Integer> rightArr = new ArrayList<Integer>();
	
	// Konstruktor
	public Model(Controller c) {
		this.c = c;
		init_field(true);	
	}

	// Sortieralgorithmen
	
	// Jeder Algorithmus wird vom Controller so lange aufgerufen, bis ready = true gesetzt wird
	// Die Variablen int step und int step2 sind für die Algos reserviert.
	// int size ist die Größe des Arrays (default: 199)
	// Mit setCurrentOldIndex() und setUsedIndex() bzw setComparedIndex() werden die aktuell bearbeiteten Schritte des Original- (old) und des aktuellen (new) Arrays farbig (default: gelb) markiert
	
	// DummySort - einfache Umkehrung
	
	public void dummy_sort() {
		if (step < size) {
			aktfield.add(oldfield.get(size-1-step));
			numberOfOpsInc(1);
			setCurrentOldIndex(size-1-step);
			setUsedIndex(step);
			setComparedIndex(-1);					
			ready = false;
			step++;
		}				
		else {
			ready = true;
			step = 0;
		}
	}
	
	// SelectionSort
	
		public void selection_sort() {
			if (step < size) {
				if (step == 0) { sorted.clear(); unsorted.clear(); unsorted.addAll(oldfield); }  // Komplettkopie nach unsorted beim ersten Aufruf
				// Minimum in unsorted finden
					int min = range;  int indexOfMin = -1;							// Wert auf Max setzen
					for (int i=0; i<unsorted.size(); i++) {
						if (unsorted.get(i) <= min) { min = unsorted.get(i); indexOfMin = i; }
						numberOfOpsInc(1);
					}
					
				// Werte tauschen/entfernen
				if (indexOfMin != -1) {
					sorted.add(unsorted.get(indexOfMin));
					unsorted.remove(indexOfMin);
					numberOfOpsInc(1);
				}
				
				// Aktfield befüllen
				aktfield.clear();
				// erst sorted
				aktfield.addAll(sorted);
				// dann unsorted
				aktfield.addAll(unsorted);
				numberOfOpsInc(2);
				
				// Markierungen
				setCurrentOldIndex(step);
				setUsedIndex(sorted.size()-1);
				
				// Minimum wurde ja schon aus unsorted entfernt; zur Veranschaulichung wird jetzt nochmal im neuen unsorted das Minimum gesucht - etwas Fake
				min = range;  indexOfMin = -1;							
				for (int i=0; i<unsorted.size(); i++) {
					if (unsorted.get(i) < min) { min = unsorted.get(i); indexOfMin = i; }
				}
				setComparedIndex(indexOfMin+sorted.size());
				
				ready = false;
				step++;
			}		
			
			else {
				setCurrentOldIndex(size-1);
				setUsedIndex(size-1);
				setComparedIndex(size-1);
				ready = true;
				step = 0;
			}
		}
		
	
	// BubbleSort
	
	public void bubble_sort() {
		if (step2 < size-1) {     // step2: n-ter Index des "äußeren" / Originalarrays	
			if (step < size-1-step2) {   // step: einzelner Wert wird bis nach rechts durchgeschoben / "inneres" Array
					if (step == 0 && step2 == 0) { aktfield.clear(); aktfield.addAll(oldfield); }			// Komplettkopie des Arrays	zu Beginn			
				
					if (aktfield.get(step) > aktfield.get(step+1)) { 
						int tmp = aktfield.get(step+1); aktfield.set(step+1,aktfield.get(step)); aktfield.set(step,tmp);  // größeren Wert nach rechts schieben
						setUsedIndex(step);
						setComparedIndex(step+1);
						}  
					else { 
						setUsedIndex(step+1);
						setComparedIndex(step);
					}
					setCurrentOldIndex(step2);
					
					numberOfOpsInc(1);
											
					ready = false;
					step++;
					
				}		
				
			else {
					setUsedIndex(step+1);		// nächster Durchlauf
					step2++;
					step = 0;
				}
		}
		
		else {									// Ende
			ready = true;
			setCurrentOldIndex(size-1);
			setUsedIndex(size-1);
			setComparedIndex(size-1);
			step = 0;
			step2 = 0;
			}
		
	}
	
	// QuickSort
	
			public void quick_sort() {
				leftArr.clear();
				rightArr.clear();
				left = -1; right = oldfield.size(); // Voreinstellung: gesamtes Array
				// Range finden aus Elementen, die nicht inplace sind, also noch nie Pivot waren
				for (int i=0; i<inplace.size(); i++) {
					if (inplace.get(i) == 0 && left == -1) { left = i; right=i; } 				// erstes Vorkommen einer 0
					if (inplace.get(i) == 0 && left != -1) { right++; } 						// von der ersten 0 an weitere 0er
					if (inplace.get(i) != 0 && left != -1 && left != right) { i= inplace.size() + 100; } // erste Nicht-0 nach einer 0 --> Schleifenabbruch
				}					
				
				//System.out.println("Left: " + left + " - Right: " + right);
				
				if (left != -1) {
					if (aktfield.size() == 0) { aktfield = new ArrayList<Integer>(oldfield); }	 // Komplettkopie bei allererstem Durchlauf
					pivot = aktfield.get(left);					// immer erstes Element der gefundenen freien Range als Pivot
						
					// Linke und rechte Bereiche plus Position des Pivots finden
					int count = 0;
					for (int i=left; i < right; i++) {
						if (aktfield.get(i) < pivot) { leftArr.add(aktfield.get(i)); count++; }
						else { rightArr.add(aktfield.get(i)); }
						numberOfOpsInc(1);
					}
					
					// Akt-Array neubefüllen
					// Left
					for (int i=0; i<leftArr.size(); i++) {
						aktfield.set(left+i, leftArr.get(i));
						numberOfOpsInc(1);
						
					}
					// Pivot
						aktfield.set(left+count, pivot);
						inplace.set(left+count, pivot);			// Pivot ins Hilfsarray eintragen --> hat seine feste Position
						numberOfOpsInc(1);
						
					// Right
					for (int i=0; i<rightArr.size(); i++) {
							aktfield.set(left+count+i, rightArr.get(i));
							numberOfOpsInc(1);
							
						}
					
					// Markieren
					setCurrentOldIndex(left);
					setComparedIndex(left);
					setUsedIndex(left+count);	
				}
				else { 
					setCurrentOldIndex(size-1);  // Endmarkierung händisch setzen
					setUsedIndex(size-1);
					setComparedIndex(size-1);
					ready = true; }
			}
		
	// Array
	
	public void init_field(boolean both) { 		// wenn true, werden beide Arrays initialisiert; ansonsten nur das Aktfield
		if (both) {
			oldfield.clear();
			Random fill = new Random();
			for (int i=0; i<getSize(); i++) { oldfield.add(1 + fill.nextInt(range)); }
			//print_field();
		}
		aktfield.clear();
		inplace.clear();
		for (int i=0; i<getSize();i++) { inplace.add(0); }			
		setCurrentOldIndex(-1);
		setUsedIndex(-1);
		setComparedIndex(-1);
		step = 0;
		step2 = 0;
		nOps = 0;
		c.setAlgo("");	
		ready = false;	
	}
	
	public void print_field() {
		String s = "";
		for (int i: oldfield) {
			s += i + " ";
		}
		System.out.println(s+"\n_____________________________________________________________________________________");
	}
	
	// Kommunikation mit anderen Klassen
	public void numberOfOpsInc(int i) {
		nOps += i;
		c.setNrOfOperations(nOps);				// an Controller Bedienfeld schicken
	}
	
	// Getter/Setter
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int s) {
		this.size = s;
	}
	
	public ArrayList<Integer> getOldField() {
		return oldfield;
	}
	
	public ArrayList<Integer> getAktField() {
		return aktfield;
	}
	
	public int getCurrentOldIndex() {
		return currentOldIndex;
	}
	
	public void setCurrentOldIndex(int ci) {
		this.currentOldIndex = ci;
	}
	
	public int getUsedIndex() {
		return usedIndex;
	}
	
	public void setUsedIndex(int ci) {
		this.usedIndex = ci;
	}
	
	public int getComparedIndex() {
		return comparedIndex;
	}
	
	public void setComparedIndex(int ci) {
		this.comparedIndex = ci;
	}
	
	public boolean isFinished() {
		return ready;
	}
}
