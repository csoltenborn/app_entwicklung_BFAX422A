# Erweiterung der ChatGPT App

## Einleitung
   Meine Erweiterung für die ChatGPT-App ist ein Quiz in einer separaten Activity. Jedoch ist dies kein normales Quiz mit vorgefertigten Fragen,
   sondern ein Quiz mit Fragen die von ChatGPT automatisch und zufällig generiert werden. Hierbei habe ich mich allerdings auf ja/nein Fragen beschränkt.

   Ein kleines Spiel in die App zu integrieren, stellt eine nette Abwechslung zu den normalen chats mit chatGPT dar. Ohne zu wissen welche Fragen auf einen zukommen,
   kann der Nutzer sein Allgemeinwissen überprüfen. Die Ja/Nein Fragen ermöglichen es dem Nutzer, nicht nur sein Wissen zu prüfen, sondern auch zu erweitern. 
   Dies könnte die Nutzer langfristig bereichern.
   
## Anforderungen

### Funktionale Anforderungen:
    - Über den Button "Chat" in der Activity in der der normale Chat mit ChatGPT zu finden ist, soll man zu dem Quiz gelangen.
    - Über den Button "Quiz" in der Activity in der das Quiz ist, soll man zum Chat gelangen.
    - Es sollen Quizfragen gestellt werden die von ChatGPT zufällig generiert werden. Diese Fragen sind nur mit "Ja" und "Nein" zu beantworten.
    - Über den Button "Nächste Frage" wird eine Frage von chatGPT generiert und angezeigt.
    - Über den Button "Ja" kann man die generierte Frage mit "ja" beantworten.
    - Über den Button "Nein" kann man die generierte Frage mit "nein" beantworten.
    - Die ausgewählte Antwort auf die Quizfrage wird an den ChatGPT geschickt.
    - Ob die Frage richtig oder falsch beantwortet wurde, liefert ChatGPT mit "Richtig" oder "Falsch" zurück.
    - Bei richtig beantworteter Frage wird die Zahl neben dem Text "Richtige Antworten:" um 1 erhöht.
    - Bei falsch beantworteter Frage wird die Zahl neben dem Text "Falsche Antworten:" um 1 erhöht.
    - Bei jeder 5ten richtig beantworteten Frage wird ein Zitat aus einem dieser Film-Franchises als Belohnung angezeigt: Der Herr der Ringe, Star Wars und Harry Potter.  

### Nicht funktionale Anforderungen:
    - Betriebssystem: Android 
    - Robustheit: Die App darf innerhalb der Erweiterung nicht Abstürzen, einfrieren, etc.
    - Performance: Die Erweiterung darf keine langen Ladezeiten haben und soll schnell reagieren können.
    - Verfügbarkeit: Die App sollte immer die möglichkeit bieten, in das Quiz zu wechseln.
    - Nutzerfreundlichkeit: Die Erweiterung sollte Intuitive nutzbar sein.

## Umsetzung
   
   ### Konzept
       Zunächst habe ich mich mit der Konzeption der Oberfläche beschäftigt, welche Elemente müssen eingebaut werden und in welcher Anordnung?.
       Mir ging es vor allem darum, eine Intuitive Nutzeroberfläche zu schaffen.

       Mein erstes Konzept sah folgendermaßen aus: 
       (Bild wird noch eingefügt)
       ![image]([https://github.com/FinnEhrl/app_entwicklung_FinnEhrlich/blob/QuizErweiterung/Erstes_Konzept.jpg?raw=true])


       Da ich allerdings beim Entwickeln mit der Positionierung des "Chat" buttons nicht ganz zufrieden war habe ich ihn nach ganz oben in die Mitte verschoben.
       
   ### Implementierung des UI

       Für die Erweiterung habe ich eine Activity und ein Fragment hinzugefügt.
       Das UI habe ich nach meinem Konzept gebaut (bis auf die Umpositionierung des "chat" buttons).
       Dafür brauchte ich jediglich den in Android Studio bereitgestellten Designer. 
       Für statische Texte habe ich noch übersetzungen für deutsch und englisch eingebaut, die sich nach der Systemsprache anpassen.
       Dazu habe ich noch den button "quiz" in die Chatoberfläche eingefügt.
       
   ### Implementierung der Logik

       Vorab habe ich für jedes UI Element, welches ich noch in der Logik verwendet habe, eine "get" Methode geschrieben, um auf diese einfach zugreifen zu können.
       Der API-Token wird in der onViewCreated Methode direkt gesetzt.
       
       Als erstes habe ich mich damit befasst, ein wechsel zwischen der Chat- und Quizoberflächse zu ermöglichen.
       Dazu habe ich für die beiden buttons ("Quiz" und "Chat") einen actionListener gebaut, der zu der jeweils der anderen Activity wechselt.

       Dann habe ich mich der Fragestellung von ChatGPT gewidmet, für die ich zunächst eine Methode erstellt habe die ChatGPT nach einer Quizfrage, fragt.
       Bei der Anfrage habe ich noch die Art der Quizfrage und andere kleinigkeiten spezifiziert, damit ein Reibungsloser fragefluss entsteht.
       Ich habe eine konstante Variable mit eingebaut, die überprüft ob es sich um die erste gestellte Frage handelt oder nicht.
       Dies hat den Grund, das die erste gestellte Anfrage länger ist, und das unnötige responsetime kosten haben könnte.
       Die gestellte Quizfrage von Chatgpt wird dann in der TextView über dem Button "Nächste Frage" angezeigt.
       Diese Methode habe ich dann noch an den actionlistener des buttons "Nächste Frage" gehangen.

       Über die Buttons "Ja" und "Nein", wird die jeweilige Antwort auf die Quizfrage an eine seperate Methode übergeben, die auch über die actionlistener aufgerufen wird.
       Nun wird die Antwort an ChatGPT weitergeben und die Lösung mit "Richtig" oder "Falsch" in die Textview unterhalb des buttons "Nächste Frage" angezeigt.
       In dieser Methode werden noch die konstanten "rightanswers" und "wrong answers" je nachdem ob die Antwort auf die Quizfrage richtig oder falsch war, inkrementiert.
       Die Zahl die inkrementiert wurde ersetzt dann die aktuelle Zahl neben den Textviews "Richtige Antworten:" oder "Falsche Antworten:".

       Darüber hinaus wird danach die aktuelle anzahl der richtigen Antworten überprüft, wenn diese geteilt durch 5, den Rest 0 ergibt, wird ein Zitat aus einem der in den Anforderungen
       erwähnten Film-Franchises in der TextView unterhalb von dem Button "Nächste Frage" angezeigt.
       
       
      
   ## Probleme/Lessons Learned

      Anfängliche Probleme hatte ich beim erstellen des UIs. Bei Android gibt es verschiedenste wizards um zum Beispiel Settingactivies zu erstellen.
      Da hatte ich etwas ausgewählt das nicht für meinen Fall passend war. Deshalb habe ich mir erstmal informiert, welche vorgefertigten Strukturen es gibt.

      Da ich im Bereich der Mobile-Entwicklung noch nicht so viele Erfahrungen sammeln konnte, verlief der Entwicklungsprozess im Allgemeinen nicht komplett reibungslos.
      Je weiter ich in meinem Entwicklungsprozess war, desto besser lief es.

      Beim Entwickeln der Belohung, hatte ich anfänglich geplant ein Popup zu bauen, in dem das Zitat angezeigt wird.
      Leider hat es mit dem Popup nicht so geklappt wie ich es mir gewünscht hatte und habe mich von daher dazu entschieden,
      das Zitat in der TextView unter dem button "Nächste Frage" anzeigen zu lassen

   ## Fazit

      Entegen meiner Vorstellung im Bezug auf den Zeitaufwand der von mir geplanten Erweiterung, habe ich festgestellt das dieser nicht so hoch war.
      Die geplanten Features konnte ich Umsetzen wie ich es geplant hatte und konnte zudem noch eine Kleinigkeit im Rahmen einer Belohnung einbauen.
      
      
      
      


