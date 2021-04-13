# Sortmonitor
Visualization of sorting algorithms

Eine Liste von Sortieralgorithmen soll visualisiert werden. Das Programm könnte eventuell in der Schule eingesetzt werden.
Bei besserer Performance könnte es evtl später auch für andere Algorithmen verwendet werden.

Versionshistorie:
1.1. Elementare Version; ModelViewController-Grundstruktur; Timer als Taktgeber; Arrays als Feld
1.2. Erweiterung um GUI (Speed, Feldgröße); Arrays als Feld; Bug bei Vergrößerung des Feldes
1.3. Stabile Version: Switch auf ArrayList; skalierbar bis 1400; Number of operations

To do: (bitte erweitern!)

- Skala unterlegen
- weitere Algorithmen implementieren
- Messung der Rechenzeit
- Responsive Design (bei Änderung der Fenstergöße z.B)
- Play/Pause-Button
- (langfristig) Textfeld für eigene Algorithmen zur Verfügung stellen (noch keine eigene Idee zur Umsetzung)
- ...


Verbesserungen: (bitte erweitern!)

- Frage nach besserer Ticker-Steuerung: v.a. um auch rekursive Algorithmen implementieren zu können (Thread.sleep führte mit GUI zu Programmabsturz)
	--> Empfehlung: Experimentieren mit ganz eigener Struktur; Neuprogrammierung oder Elementare Version 1.1 mit Austausch des Tickers
- Arrays statt ArrayListen ?? --> bessere Performanz; Problem: bei Änderung der Feldgröße, v.a. nach oben, gibt es mit Arrays Fehler
	--> Empfehlung: Experimentieren mit Version 1.2. - Array löschen und neu initialisieren in init_field;
- Algorithmen verbessern (Workarounds um fehlende Rekursion sind teilweise schlecht; weniger Arbeitsschritte)
- Visualisierung / Anschaulichkeit verbessern, systematisieren
- Steuerung und Menu erscheinen manchmal erst nach dem Maximieren; keine Ahnung warum
- ....


Programmaufbau:

- MVC-Design Pattern: Model enthält Logik, View die Visualisierung, Controller die Steuerung (insbes. durch Nutzer);
  Model kennt die View nicht, meldet aber bestimmte Dinge an Controller;
  View kennt beide: holt sich über Methoden von Model beide Felder und die zu markierenden Indices;
  Controller steuert den Ticker und die Usereingaben
  
 - Steuermethode ist die Methode sort() im Controller. Diese ruft in Intervallen den vom User gewählten Algorithmus (in String algo gespeichert) auf, bis isFinished() = true ist (damit ist es aber nicht wirklich möglich, einen Algorithmus sich selbst aufrufen zu lassen)
 
 - oldfield und aktfield sind aktuell ArrayListen (anfangs Arrays): ursprüngliches und aktuelles Feld, die beide von der View immer wieder gezeichnet werden

- Die Methode init_field(boolean b) im Model ist wichtig. Hier werden alle Felder/Variablen initialisiert, falls der Nutzer Reset wählt oder einen anderen Algorithmus aufruft. Mit true werden beide Felder initialisiert (oldfield neu mit Zufallszahlen belegt, aktfield gelöscht); mit false wird nur aktfield gelöscht

- Der Controller ist ein JFrame,  dem die View (JPanel), also quasi eine Leinwand, geaddet wird. In der neueren Version wird ein LayoutManager verwendet


Vorgehensempfehlung: (gerne Hilfestellung für GitHub erweitern)

- Gescheite IDE installieren (falls noch nicht vorhanden)
- Git/GitHub-Tutorials schauen
- Git in der IDE zum Laufen bringen --> lokale Versionsverwaltung (sowieso sehr zu empfehlen)
- Remote-Zugriff auf GitHub/Bayreuther-Gruppe/Sortmonitor herstellen (wie genau? Bin selber noch am auschecken)
- SortMonitor klonen/pullen

Um Programmstruktur zu verstehen:
- Eigenen Dummy-Algorithmus in der Basisversion (Initial commit) implementieren (z.B Feld in kleiner als und größer als Dummyzahl aufteilen)
- Naiven Algorithmus (Selection oder Bubble) löschen und selbst schreiben
- dann vielleicht aktuelle Version nehmen (arbeitet mit ArrayList)
- Sich an komplexerem Algorithmus probieren
- Evtl., wer mag: Das ganze komplett selbst aufziehen: bin z.B. nicht überzeugt, dass der Ticker die beste Wahl ist; rein theoretisch könnte das auch in Greenfoot gut umzusetzen sein; da gibt es ja schon die act-Methode
