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
    - Bei jeder 5ten richtig beantworteten Frage wird ein Zitat aus einem dieser Filme als Belohnung angezeigt: Der Herr der Ringe, Star Wars und Harry Potter.  

### Nicht funktionale Anforderungen:
    - Betriebssystem: Android 
    - Robustheit: Die App darf innerhalb der Erweiterung nicht Abstürzen, einfrieren, etc.
    - Performance: Die Erweiterung darf keine langen Ladezeiten haben und soll schnell reagieren können.
    - Verfügbarkeit: Die App sollte immer die möglichkeit bieten, in das Quiz zu wechseln.
    - Nutzerfreundlichkeit: Die Erweiterung sollte Intuitive nutzbar sein.

    
