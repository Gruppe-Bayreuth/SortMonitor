package sortmon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Model {
	
	Controller c;
	private int size, range;											// Gr��e des Arrays / Range der Zahlen
	private int[] oldfield;
	private int[] aktfield;
	
	private int currentOldIndex, usedIndex, comparedIndex;			// farblich markierte Indices
	private boolean ready;
	
	private int nOps;												// number Of operations
	//Standard-SortierVariablen
	private int step = 0;
	private int step2 = 0;
	
	// SelectionSort-Vars
	private ArrayList<Integer> unsorted = new ArrayList<Integer>();
	private ArrayList<Integer> sorted = new ArrayList<Integer>();
	
	// Gnomesort
	private int lookat;
	
	// Quicksort-Vars
	int pivot, left, right;
	private int[] inplace;
	private ArrayList<Integer> leftArr = new ArrayList<Integer>();
	private ArrayList<Integer> rightArr = new ArrayList<Integer>();
	
	// opt Quick
	private ArrayList<Integer> ranges = new ArrayList<Integer>();
	private ArrayList<Integer> pivots = new ArrayList<Integer>();
	
	// Merge Sort
	private ArrayList<ArrayList<Integer>> merges = new ArrayList<ArrayList<Integer>>();
	
	// Radix	
	private ArrayList[] distribution = new ArrayList[11];	
	private int part, counter, puff;
	private boolean done;
	
	// Konstruktor
	public Model(Controller c, int s, int r) {
		this.c = c;
		this.size = s;
		this.range = r;
		this.oldfield = new int[s];
		this.aktfield = new int[s];
		this.inplace = new int[s];
		init_field(true);	
	}

	// Sortieralgorithmen
	
	// Jeder Algorithmus wird vom Controller so lange aufgerufen, bis ready = true gesetzt wird
	// Die Variablen int step und int step2 sind f�r die Algos reserviert.
	// int size ist die Gr��e des Arrays (default: 199)
	// Mit setCurrentOldIndex() und setUsedIndex() bzw setComparedIndex() werden die aktuell bearbeiteten Schritte des Original- (old) und des aktuellen (new) Arrays farbig (default: gelb) markiert
	
	// DummySort - einfache Umkehrung
	
	public void dummy_sort() {
		if (step < size) {
			aktfield[step] = oldfield[size-1-step];
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
	
	// Insertion Sort 
	public void insertion_sort() {
		if (step == 0) { copy_field(); }
		else if (step<getSize()) {
			int tmp = aktfield[step];
			int j = step;
			while(j>0 && tmp < aktfield[j-1]) {
				aktfield[j] = aktfield[j-1];
				j--;
				numberOfOpsInc(2);
			}	
			aktfield[j] = tmp;
			numberOfOpsInc(1);			
			setCurrentOldIndex(step+1);
			setUsedIndex(step+1);
			setComparedIndex(j);
		}
		else {
			ready = true;
			setCurrentOldIndex(size-1);
			setUsedIndex(size-1);
			setComparedIndex(size-1);
		}
		step++;
	}
	
	// SelectionSort
	
		public void selection_sort() {
			if (step < size) {
				if (step == 0) { sorted.clear(); unsorted.clear(); for (int i: oldfield) {	unsorted.add(i); } }  // Komplettkopie nach unsorted beim ersten Aufruf
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
				
				// Aktfield bef�llen
				// erst sorted
				int count = 0;
				for (int i: sorted) { 
					aktfield[count] = i; 
					//System.out.println("Count Sorted  " + count + ": " + i); 
					count++; }
				// dann unsorted
				for (int i: unsorted) { 
					aktfield[count] = i; 
					//System.out.println("Count Unsorted  " + count + ": " + i); 
					count++; }
				numberOfOpsInc(2);
				
				// Markierungen
				setCurrentOldIndex(step);
				setUsedIndex(sorted.size()-1);
				
				// Minimum wurde ja schon aus unsorted entfernt; zur Anzeige wird jetzt nochmal im neuen unsorted das Minimum gesucht - etwas fake
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
		// Gnomesort
		
		public void gnome_sort() {
			int used, compared;
			if (step == 0) { copy_field(); lookat = 0; }			// Komplettkopie des Arrays	zu Beginn
			if (lookat < size-1) {
				if (aktfield[lookat] <= aktfield[lookat+1]) { lookat++; numberOfOpsInc(1); compared = lookat; used = lookat+1; }
				else { 
					int tmp=aktfield[lookat]; aktfield[lookat] = aktfield[lookat+1]; aktfield[lookat+1] = tmp; compared = lookat+1; used = lookat;// felder tauschen
					numberOfOpsInc(2);			
					if (lookat > 0) { lookat--; } else { lookat++; }
					} 
				setCurrentOldIndex(lookat);
				setUsedIndex(used);
				setComparedIndex(compared);
				step++;
			}
			else {
				ready=true; step=0;
				setCurrentOldIndex(size-1);
				setUsedIndex(size-1);
				setComparedIndex(size-1);
			}
		}
	
	// BubbleSort
	
	public void bubble_sort() {
		if (step2 < size-1) {     // step2: n-ter Index des "�u�eren" / Originalarrays
				
			if (step < size-1-step2) {   // step: einzelner Wert wird bis nach rechts durchgeschoben / "inneres" Array
					if (step == 0 && step2 == 0) { copy_field(); }			// Komplettkopie des Arrays	zu Beginn			
				
					if (aktfield[step] > aktfield[step+1]) { 
						int tmp = aktfield[step+1]; aktfield[step+1] = aktfield[step]; aktfield[step] = tmp;  // gr��eren Wert nach rechts schieben
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
					setUsedIndex(step+1);		// n�chster Durchlauf
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
	// akz Quick
	public void opt_quick_sort() {	
		leftArr.clear();
		rightArr.clear();
		pivots.clear();
		// im Array ranges werden die noch nicht geteilten Spannen des Feldes gesammelt: immer paarweise und FIFO
		if (aktfield[0] == 0) { 	// allererster Durchlauf
			copy_field();
			ranges.add(0); 			// Vorbelegung: ganzes Feld
			ranges.add(size); 						
			} 
		// Solange es noch Spannen gibt, die nicht komplett ins Aktfield �bertragen wurden
		if (!ranges.isEmpty()) {
			left = ranges.get(0); right = ranges.get(1);  	// gilt f�r alle Durchl�ufe
			//System.out.println("Left: " + left + " - Right: " + right);
			
			// Feld am Pivot aufteilen
			pivot = aktfield[left];			// erstes Element der Spanne
			
			pivots.add(pivot);
			numberOfOpsInc(1);
			
			for (int i = left+1; i < right; i++) {
				if (aktfield[i] < pivot) { leftArr.add(aktfield[i]); }
				else if (aktfield[i] == pivot) { pivots.add(aktfield[i]); }
				else { rightArr.add(aktfield[i]); }
				numberOfOpsInc(1);				
			}
			
			// // aktfield neu bef�llen
			// links	
			for (int i = 0; i < leftArr.size(); i++) {
				aktfield[left+i] = leftArr.get(i);	
				//System.out.println("Linke Pos "+ (left+i) + ": " + leftArr.get(i));
				numberOfOpsInc(1);
				
			}
			// Pivots	
			for (int i = 0; i < pivots.size(); i++) {
				aktfield[left+leftArr.size()+i] = pivots.get(i);	
				//System.out.println("Pivotpos "+ (left+leftArr.size()) + ": " + pivots.get(i));
				numberOfOpsInc(1);
				
			}
			// rechts
			for (int i = 0; i < rightArr.size(); i++) {
				aktfield[left+leftArr.size()+pivots.size()+i] = rightArr.get(i);
				//System.out.println("Rechte Pos: "+ (left+leftArr.size()+pivots.size()+i) +": " + rightArr.get(i));
				numberOfOpsInc(1);
				
			}
			
			//System.out.println("Pivot war: " + pivot + " - an Pos: " + pivotpos);
			// Erste Spanne l�schen
			ranges.remove(0);
			ranges.remove(0);
			// Neue Spannen eintragen
			// LeftArr
			if (leftArr.size()>1) {
				ranges.add(left); // left
				ranges.add(left+leftArr.size()); // right
				numberOfOpsInc(1);
				
			}
			// RightArr
			if (rightArr.size()>1) {
				ranges.add(left+leftArr.size()+pivots.size()); // left
				ranges.add(left+leftArr.size()+pivots.size()+rightArr.size()); // right
				numberOfOpsInc(1);
				
			}
			
			// Markieren
			setCurrentOldIndex(left);
			setComparedIndex(left+leftArr.size()+pivots.size());
			setUsedIndex(left+rightArr.size());
		}
		// wenn Ranges abgearbeitet sind
		else {
			setCurrentOldIndex(size-1);
			setUsedIndex(size-1);
			setComparedIndex(size-1);
			ready = true;
		}
	}
		
	// QuickSort
	
		public void quick_sort() {
			leftArr.clear();
			rightArr.clear();
			left = -1; right = oldfield.length; // Voreinstellung: gesamtes Array
			// Range finden aus Elementen, die nicht inplace sind, also noch nie Pivot waren
			for (int i=0; i<inplace.length; i++) {
				if (inplace[i] == 0 && left == -1) { left = i; right=i; } 
				if (inplace[i] == 0 && left != -1) { right++; } 
				if (inplace[i] != 0 && left != -1 && left != right) { i= inplace.length + 1000; } // Schleifenabbruch
			}					
			
			//System.out.println("Left: " + left + " - Right: " + right);
			
			if (left != -1) {
				if (aktfield[0] == 0) { copy_field(); }	 // Komplettkopie bei allererstem Durchlauf
				pivot = aktfield[left];					// immer erstes Element der gefundenen freien Range als Pivot
					
				// Linke und rechte Bereiche plus Position des Pivots finden
				int count = 0;
				for (int i=left; i < right; i++) {
					if (aktfield[i] < pivot) { leftArr.add(aktfield[i]); count++; }
					else { rightArr.add(aktfield[i]); }
					numberOfOpsInc(1);
				}
				
				// Akt-Array neubef�llen
				// Left
				for (int i=0; i<leftArr.size(); i++) {
					aktfield[left+i] = leftArr.get(i);
					numberOfOpsInc(1);
					
				}
				// Pivot
					aktfield[left+count] = pivot;
					inplace[left+count] = pivot;			// Pivot ins Hilfsarray eintragen --> hat seine feste Position
					numberOfOpsInc(1);
					
				// Right
				for (int i=0; i<rightArr.size(); i++) {
						aktfield[left+count+i] = rightArr.get(i);
						numberOfOpsInc(1);
						
					}
				
				// Markieren
				setCurrentOldIndex(left);
				setComparedIndex(left);
				setUsedIndex(left+count);	
			}
			else { 
				setCurrentOldIndex(size-1);  // Endmarkierung h�ndisch setzen
				setUsedIndex(size-1);
				setComparedIndex(size-1);
				ready = true; 
				}
		}
		
	// Mergesort
	public void merge_sort() {
		// Erster Schritt: aufteilen in Einzelarrays
		if (step == 0) { 
				for (int i: oldfield) {
					ArrayList<Integer> first = new ArrayList<Integer>();
					first.add(i);
					merges.add(first);
					numberOfOpsInc(1);
				}
			}
		
		// Merge-Phase
		ArrayList<ArrayList<Integer>> tmp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < merges.size()-1; i+=2) {
			tmp.add(merge_it(merges.get(i), merges.get(i+1)));
		}
		if (merges.size()%2==1) { tmp.add(merges.get(merges.size()-1)); } // bei ungerader Feldgr��e wird letztes Element unver�ndert eingef�gt
		
		setCurrentOldIndex((int)Math.pow(2, step));  // Endmarkierung h�ndisch setzen
		setComparedIndex((int)Math.pow(2, step));
		
		
		// Kopieren nach Aktfield
		int count = 0;
		for (ArrayList<Integer> ar: tmp) {
			for (int i: ar) {
				aktfield[count] = i;
				count++;
			}
		}
		
		// N�chster Merge-Schritt
		merges=tmp;
		step++;
		int log = (int)(Math.log(size) / Math.log(2));		// logarithmische Laufzeit
		if (step > log) { 
			setCurrentOldIndex(size-1);  // Endmarkierung h�ndisch setzen
			setUsedIndex(size-1);
			setComparedIndex(size-1);
			ready = true; }
	}
	
	private ArrayList<Integer> merge_it(ArrayList<Integer> one, ArrayList<Integer> two) {
		ArrayList<Integer> merged = new ArrayList<Integer>();
		// vergleichen
		int i=0; int ii = 0;
		while (i < one.size() && ii < two.size()) {
			if (one.get(i) < two.get(ii)) { merged.add(one.get(i)); i++; }
			else { merged.add(two.get(ii)); ii++; }
			numberOfOpsInc(1);
		}
		
		if (i < one.size()) {
			for (int ti = i; ti < one.size(); ti++) {
				merged.add(one.get(ti));
				numberOfOpsInc(1);
			}
		}
		
		if (ii < two.size()) {
			for (int ti = ii; ti < two.size(); ti++) {
				merged.add(two.get(ti));
				numberOfOpsInc(1);
			}
		}
		return merged;
	}
		
	// Heap-Sort
	public void heap_sort() {
		if (step == 0) { copy_field(); counter = 0; }
		else if (step==1) { 
			buildHeap();
			//print_field();		
			//System.out.println("Heap: " + checkHeap()); 
			}
		else {
			counter++;
			// Swap
			int tmp = aktfield[aktfield.length-counter];
			//System.out.println("Tausche letztes Element: " + tmp + " mit erstem: " + aktfield[0]);
			aktfield[aktfield.length-counter] = aktfield[0];
			aktfield[0] = tmp;
			// Markieren
			setCurrentOldIndex(step);  
			setUsedIndex(aktfield.length-counter);
			setComparedIndex(0);
			numberOfOpsInc(2);
			// Versickern
			sinkdown(0);
		}
		step++;
		if (counter >= aktfield.length-1) { 
			// Schlechter Hard-Bugfix: die letzten 3-4 Elemente waren immer nicht sortiert
			ArrayList<Integer> order = new ArrayList<Integer>();
				for (int i=0; i<4; i++) { order.add(aktfield[i]); }
			Collections.sort(order);
			for (int i= 0; i<4; i++) { aktfield[i] = order.get(i); }
			order.clear();
			// Bugfix-Ende
			
			setCurrentOldIndex(size-1);  // Endmarkierung h�ndisch setzen
			setUsedIndex(size-1);
			ready = true; 
			}
	}
	
	public void sinkdown(int i) {
		int lk = i*2+1; int rk = i*2+2;
		int which;
		if (aktfield[lk] > aktfield[rk] ) { which = lk; } else { which = rk; }
		numberOfOpsInc(1);
		
		if (aktfield[i] < aktfield[which]) {
			//System.out.println("Versickern: " + aktfield[i] + " kleiner als " + aktfield[which]);			
			int tmp = aktfield[i];
			aktfield[i] = aktfield[which];
			aktfield[which] = tmp;
			numberOfOpsInc(2);
			if (which*2+1 < aktfield.length-1-counter) { sinkdown(which); }		// wenn getauschtes Element Kinder hat, nochmal rekursiv
			//print_field();		
			
		}		
	}
	
	public boolean checkHeap() {
		boolean erg = true;
		for (int i=0; i<= aktfield.length/2-1; i++) {							
			if (aktfield[i] < aktfield[i*2+1]) { erg = false; }
			if (i*2+2 < aktfield.length) { 
				if(aktfield[i] < aktfield[i*2+2]) { erg = false; }		
			}		
			}
		return erg;
	}
	
	public void buildHeap() {
		for (int i = aktfield.length/2-1; i>=0; i--) {
			//System.out.println("Feld " + i + ": " + aktfield[i]);
			heapify(i);
		}
	}
	
	public void heapify(int i) {
		int lk = i*2+1; int rk = i*2+2;
		int which;
		if (rk > aktfield.length-1 || aktfield[lk] > aktfield[rk] ) { which = lk; } else { which = rk; }
		numberOfOpsInc(1);
		
		if (aktfield[which] > aktfield[i]) {
			//System.out.println("Tausche " + aktfield[which] + " mit " + aktfield[i]);			
			int tmp = aktfield[i];
			aktfield[i] = aktfield[which];
			aktfield[which] = tmp;
			numberOfOpsInc(2);
			if (which <= aktfield.length/2-1) { heapify(which); }		// wenn getauschtes Element Kinder hat, nochmal rekursiv
			heapify(i);													// zudem nochmal mit neuem parent
		}
	}
	
	// Radix-Sort
	public void radix_sort() {
		int loc=0;
		if (step == 0 && !done) { 													// nur am Anfang
			copy_field();
			for (int i = 0; i<10; i++) { distribution[i] = new ArrayList<Integer>(); } 	// Zehn ArrayLists in Array Distribution
		}
			
		// ArrayLists (distribution) bef�llen je nach Ziffer
		if (!done) {
					for (int i = 0; i < size; i++) {						
						loc = (aktfield[i]/(int) Math.pow(10,step))%10;						// liefert die step-te Ziffer von rechts
						distribution[loc].add(aktfield[i]);									// der jeweiligen Arraylist anh�ngen
						numberOfOpsInc(1);
					}
					done = true;
			}
		
		// �bertrag in aktfield
			for (int el=0; el < distribution[part].size(); el++) {
					if ((int) distribution[part].get(el)!=0) { 
						aktfield[counter] = (int) distribution[part].get(el);
						counter++;
						numberOfOpsInc(1);
					}			
				}
		//Markieren
		setCurrentOldIndex(counter);
		int used = 0;
		used = counter%size + puff*size/10;
		if (counter == size) { puff++; }
		setUsedIndex(used);
		int compared = used + distribution[part].size();
		setComparedIndex(compared);	
		
		//print_field();
		
		// Verlauf
		if (step>=2 && part > 8) { 		// Komplett durch
			part = 0; 
			for (int i=0; i<10; i++) { distribution[i].clear(); }
			done = false; counter = 0; step=0; ready = true;
			setCurrentOldIndex(size-1);
			setComparedIndex(size-1);
			setUsedIndex(size-1);
			} 
		else { 
			if (part > 8) {														// wenn alle Teile einer Distribution eingetragen wurden
				for (int i=0; i<10; i++) { distribution[i].clear(); }			//Verteilungslisten wieder l�schen f�r die
				step++; done = false; part = 0;	counter = 0;					// n�chste Ziffer
				}
			else { part++; }
			}					
	}
		
	// Array
	
	public void init_field(boolean both) { 		// wenn true, werden beide Arrays initialisiert; ansonsten nur das Aktfield
		Random fill = new Random();
		for (int i=0; i<this.size; i++) {
			if (both) { this.oldfield[i] = 1 + fill.nextInt(this.range); }
			this.aktfield[i] = 0;
			this.inplace[i] = 0;
		}
		ranges.clear();
		merges.clear();
		
		setCurrentOldIndex(-1);
		setUsedIndex(-1);
		setComparedIndex(-1);
		step = 0;
		step2 = 0;
		lookat = 0;
		nOps = 0;
		part = 0; 
		done = false; counter = 0;
		ready = false;
	}
	
	private void copy_field() {
		int count = 0;
		for (int i: oldfield) {
			aktfield[count] = i;
			count++;
		}
	}
	
	public void print_field() {
		String s = "";
		for (int i: aktfield) {
			s += i + " ";
		}
		System.out.println(s+"\n_____________________________________________________________________________________");
	}
	
	public void resize_field(int newsize) {
		setSize(newsize);
		int[] newfield1 = new int[newsize];
		int[] newfield2 = new int[newsize];
		int[] newfield3 = new int[newsize];	
		
		this.oldfield = newfield1;
		this.aktfield = newfield2;
		this.inplace = newfield3;
		
		init_field(true);		
	}
	
	// Austausch/Kommunikation
	public void numberOfOpsInc(int i) {
		nOps += i;
		c.setNrOfOperations(nOps);				// an Controller Bedienfeld schicken
	}
	
	// Getter/Setter
	public int getRange() {
		return this.range;
	}
	
	public void setRange(int r) {
		this.range = r;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int s) {
		this.size = s;
	}
	
	public int[] getOldField() {
		return oldfield;
	}
	
	public int[] getAktField() {
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
