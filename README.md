# Einleitung
Meine Erweiterung ermöglicht einer Anfrage mitzugeben, wie ChatGPT auf die Anfrage antworten soll. Diese Nachricht nenne ich im Folgenden, wie in der [Dokumentation der API](https://platform.openai.com/docs/api-reference/chat/create), System-Nachricht. Sie ermöglicht dem Nutzer Antworten zu erhalten, die besser auf seine Situation zugeschnitten sind. Technisch gesehen wird in dem bereits implementierten Chat-Objekt zu Beginn ein jeden Dialogs eine System-Nachricht mitgegeben, die der App-Nutzer beliebig einstellen kann.

# Verbesserungen
- Wenn man nur auf eine schnelle Antwort aus ist, muss man nicht die manchmal doch sehr langen Antworten durchlesen, sondern bekommt direkt eine knappe Antwort.
- Man kann ChatGPT einen gewissen Kontext mitgeben, der ihm ermöglicht sich besser zurecht zu finden und eine genauere / mehr auf das Problem bezogene Antwort zu geben.
- Eine allgemein schnellere und zufriedenstellendere Bedienung der App, da man nicht mit unnötigen Textwänden konfrontiert wird oder irrelevante Antworten erhält.

# Anforderungen
Mann soll die Möglichkeit haben einem Chat-Objekt, mit dem wir mit der ChatGPT-API kommunizieren, zu Beginn einer Konversation eine System-Nachricht mitgeben zu können. Diese soll beeinflussen, auf welche spezifische Weise oder mit welchen Hintergrundinformationen die Frage beantwortet wird.
Die Nachricht soll daraufhin im Hintergrund abgespeichert werden, sodass man nicht bei jeder Benutzung der App immer die gleiche System-Nachricht eingeben muss.
Der Bequemlichkeit halber sollte man einige Standardeinstellungen per Click auswählen können, für speziellere Fälle sollte man auch einen eigenen String einfügen können.

# Umsetzung
Wenn beim Laden der App noch ein alter Chat zwischengespeichert wurde, wird dieser geladen und damit die davor kreierte System-Nachricht übernommen. (Die Attribute der Message-Klasse sind final, daher können wir sie nicht mehr ändern und müssen die alten Übernehmen).
Wenn kein abgespeicherter Chat gefunden wird, dann wird erst wenn eine Anfrage an ChatGPT geschickt wird ein neues Chat-Objekt kreiert. Diesem wird mit der momentan in den Einstellungen ausgewählten System-Nachricht versehen und anschließend nach anhängen der Anfrage an ChatGPT geschickt. Durch diese spätere Initialisierung des Chat-Objekts kann man vor dem Stellen einer Frage noch die System-Nachricht in den Einstellungen ändern, anstelle sie wie zuvor im onViewCreated schon festzusetzen.

# Änderungen in verscheidenen Artefakten
- root_preferences.xml  
Hier musste ich einige Eintellungen hinzufügen, die das Auswählen verschiedener System-Nachrichten vereinfachen. Ich hätte auch eine einmal eingegebene Einstellung (Ähnlich wie das Chat-Objekt auch schon) immer wieder abspeichern und aufrufen können, allerdings erschienen mir hier die Einstellungen als zentrale Stelle für Veränderungen als passender.
Konkret hinzugefügt habe ich ein Dropdown-Menü, über das man die verschiedenen Modi der System-Nachricht auswählen kann. Damit auch eine flexible bzw. Nutzerdefinierte Eingabe möglich ist, habe ich zudem noch eine Texteingabe hinzugefügt, die nur beim Auswählen von "Custom" im Dropdown-Menü in das Chat-Objekt eingefügt wird.
- arrays.xml  
Hier habe ich die Werte für das Dropdown-Element in den Einstellungen abgespeichert, da diese nicht wie bei der Texteingabe direkt in root_preferences.xml gespeichert werden können. "entry_values" sind die Werte, die der PreferenceManager an die App weitergibt, "entries" sind die Werte, die der Nutzer auswählen kann.

# Probleme
- Teilweise bin ich beim suchen nach einer Lösung für ein bestimmtes Problem sehr tief in Sachen abgetaucht, die ich eigentlich nicht benötigt hätte.
- Ich hatte eine sehr konkrete Vorstellung, wie genau ich einige Sachen umsetzen wollte. Das hat dazu geführt, dass ich oft mit Fehlern und Problemen zu kämpfen hatte, die ich, wenn ich eine leicht andere Herangehensweise gewählt hätte, nicht gehabt hätte.

# Lessons learned
- Android ist ein sehr mächtigs System, das mit wenig Programmierung sehr viel machen kann => nicht versuchen alles selbst zu implementieren.
- Etwas flexibel im Bezug auf die eigenen Anforderungen sein: wenn sie sich nicht genauso umsetzen lässt, frühzeitig nach Alternativen suchen, als ewig die Lösung für ein viel schwereres Problem zu suchen.

# Nicht implementierte Features
Wenn man in dem Dropdown-Menü "Custom" auswählt sollte die Eingabe einer eigenen Eingabe erscheinen und bei Auswahl eines anderen Wertes wieder verschwinden. Das habe ich nicht umsetzen können, da dieses Ausblende-System (so wie ich das verstanden habe) so aufgebaut ist, dass eine Ja / Nein Einstellung wie CheckBoxPreference oder SwitchPreference eine Gruppe an Einstellungen ein- oder ausblenden kann. Da bei mir die Ein- / Ausblendeeinstellung aber ein Dropdown-Men ist, ging das nicht. Ich bin dann auch auf die Möglichkeit gestossen, im Code die Werte und Sichtbarkeiten der verschiedenen Einstellugen ändern zu können, das war dann aber zu komplex und nicht mehr in dem Zeitrahmen möglich.

# Fazit
Alles ist in etwa so, wie ich es mir am Anfang vorgestellt hatte. Zu Beginn wollte ich nur irgendwie die System-Nachricht ändern und, wenn dan noch Zeit ist das in den Einstellungen einstellbar machen. Als mir dann aufgefallen ist, wie viel leichter es ist, in ein bestehendes System Kleinigkeiten einzubauen anstelle so wie im Unterricht immer neue Sachen zu implementieren, hatte ich erst eine genaue Vorstellung, wie genau alles am Ende sein sollte. Das ist auch nun bis auf die Kleinigkeit mit dem Ein- und Ausblenden der einen Einstellung umgesetzt.  
Zeitlich gesehen habe ich grob so lange gebraucht wie erwartet, nur war die Aufteilung umgekehrt. Ich hätte erwartet, dass die Implementierung deutlich komplizierter wäre, sich die Erweiterung dafür aber sehr einfach in die Umgebung von Android einbetten lassen würde. Letztendlich ging dann die Implementierung sehr schnell und ich musste in diesem Sinne nichts neues zur Logik der Anfrage hinzufügen, dafür hatte ich aber einige Probleme mit Abfrage der Einstellungen beim Starten der App und der Interaktion der Einstellungen untereinander.

# Einige Screenshots mit verschiedenen System-Nachrichten

## Übersicht Einstellungen
<img width="325" alt="Screenshot 2023-11-21 at 22 55 27" src="https://github.com/Pandem0n1um/App_Entwicklung/assets/147406701/85fb933f-8ad9-489a-8f60-f0590fbc2963">

## Einstellung Funny - Bitte antworte möglichst lustig und offensichtlich falsch.
<img width="338" alt="Screenshot 2023-11-21 at 22 45 28" src="https://github.com/Pandem0n1um/App_Entwicklung/assets/147406701/48183635-06b3-483f-b1e2-ff66d29153d6">

## Einstellung Precise - Bitte antworte so knapp und präzise wie möglich, dass ich die Antwort schnell und einfach verstehen kann.
<img width="351" alt="Screenshot 2023-11-21 at 22 47 45" src="https://github.com/Pandem0n1um/App_Entwicklung/assets/147406701/aa8b3e15-56ce-4d14-9f81-e2d40950ee00">

## Einstellung Custom - Bitte formuliere deine Antwort als Haiku
<img width="363" alt="Screenshot 2023-11-21 at 22 49 48" src="https://github.com/Pandem0n1um/App_Entwicklung/assets/147406701/e7a3bbff-ba0a-480d-809d-41d03447fabb">

## Keine System-Nachricht
<img width="344" alt="Screenshot 2023-11-21 at 22 52 26" src="https://github.com/Pandem0n1um/App_Entwicklung/assets/147406701/80623e7a-dfbb-40c7-b3c0-ec055820a327">
