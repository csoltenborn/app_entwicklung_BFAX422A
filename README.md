# app_entwicklung_BFAX422A
# Einleitung:
Das Feature, welches ich für die ChatGPT App programmiert habe, eröffnet die Möglichkeit mit ChatGPT, durch eine App, mit Text zu kommunizieren. Diese Funktion ist praktisch, wenn man beispielsweise in einer Umgebung ist, in der man leise sein muss oder nicht reden darf.

# Anforderungen:
Die Erweiterung soll ein Textfield hinzufügen, damit man mit dem Keyboard eine Anfrage an ChatGPT schreiben kann. 

# Umsetzung:
Für die Umsetzung meines Features musste ich ein Textfield, einen Button und eine TextView in der Fragment XML Datei hinzufügen. Das Textfeld wird benutzt, um dort die Frage an ChatGPT hereinzuschreiben. Der Button wird verwendet, damit die Frage, die man in das Textfeld schreibt, an ChatGPT weitergeleiten wird. Die TextView dient dazu das Textfeld zu beschreiben, damit der Benutzer weiß, wofür das Textfeld ist. 
Des Weiteren habe ich die Namen dieser drei Layouts in die Strings.xml Datei geschrieben, sodass die Sprache sich automatisch nach der Systemsprache des Handys anpasst (Englisch, Deutsch).
Dies habe ich im Code umgesetzt mit zwei neuen Methoden namens getSubmitButton() und getTextText(). Diese beiden Methoden geben die ID von dem Textfield und dem Button zurück. Des Weiteren habe ich in der onViewCreated() Methode einen onClickListener für den neuen Butten eingefügt. Im onClickListener wird die Klasse „Message“ aufgerufen und ein neues Objekt mit den Attributen Autor und die eingegebene Frage erzeugt. Anschließend wird der API-Token aus den Settings geholt und ein neues Objekt der Klasse „ChatGPT“ mit diesem Token erzeugt, damit die Frage an ChatGPT weitergeleitet wird. Zu guter Letzt wird die Antwort von ChatGPT in der TextView ausgegeben.

# Probleme/Lessons learned:
Am Anfang hatte ich versucht die Texteingabe mit TextInputLayout umzusetzen, aber dies hatte nicht funktioniert, weswegen ich den Plain Text verwendet habe.
Ich habe gelernt, wie man neue Components in einer Android App hinzufügt und richtig anordnet (Constraints mit anderen Components verbinden) und was die Bibliothek von Android alles zu bieten hat.

# Fazit:
Ich konnte bei diesem Feature alles umsetzten wie geplant. Auch der Zeitaufwand von 7 Stunden war vorhersehbar.

