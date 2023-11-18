# Dokumentation meiner App-Erweiterung
## Vorwort
Während meines Studiums der Angewandten Informatik an der Fachhochschule der Wirtschaft (FHDW) in Bergisch Gladbach, wurde 
im Rahmen des Moduls "App-Projekt" im 3.Semester unter Aufsicht von Herrn Dr. Christian Soltenborn eine Android-App zu Lernzwecken umgesetzt. 
Diese im Modul zusammen entwickelte App, soll nun im Rahmen eines Projekts um eine selbst gewählte Funktionalität ergänzt werden.
Diese Ergänzug ist Teil der Prüfungsleistung des Moduls.

## Einleitung
### Ist-Zustand
<img align="right" src="https://github.com/PapeMarc/app_entwicklung_BFAX422A/assets/147148804/f097dba3-30cd-4b08-a944-e2b03a0be358" padding="5">
Die vorgegebene App ermöglicht es bereits, über die Spracheingabe von Android Nachrichten aufzunehmen,
diese dann umzuwandeln, korrekt der API von ChatGPT (OpenAI) zu übermitteln, sowie dessen Antwort entgegenzunehmen, darzustellen und vorzulesen.
Die Spracheingabe und die anschließende Kommunikation wird mithilfe der Schaltfläche "Fragen"/"Ask" initiiert.
Weiterhin wurden bereits App-Einstellungen implementiert (oben rechts), über dessen Aufruf der Benutzer seinen API-Schlüssel hinterlegen kann. 
Dies muss er auch tun, da die App sonst keine Verbindung mit der Schnittstelle von GPT herstellen kann.

### Identifizierte Probleme des Ist-Zustands
Folgende Probleme mit der vorgegebenen App konnten ermittelt werden:

1. Der Benutzer bekommt die Antwort seiner Frage immer vorgelesen / es fehlt eine Schaltfläche zum pausieren des Vorgangs.

2. Der Benutzer kann ältere Antworten nicht nachschlagen, da die Textfläche zur Darstellung durch die Bildschirmgröße des Endgeräts begrenzt ist,
   sowie keine Funktionalität zum scrollen aufweist.

3. Es gibt keine Möglichkeit zum löschen seiner bisherigen Nachrichten um den Kontext der Konversation zu ändern.
   - Der Benutzer kann immer nur eine Konversation mit ChatGPT führen.

4. Der Benutzer kann keine neuen Chats erstellen oder solche Abrufen.

5. Der Nachrichtenverlauf der Konversation wird nicht persitiert / die geführte Unterhaltung wird nicht gespeichert und kann daher auch nicht zu einem
   späteren Zeitpunkt eingesehen werden. Startet der Benutzer die App also neu, verliert er alle seine Nachrichten.

6. Fehler werden nicht abgefangen und führen zum Absturz der APP. Der Benutzer erfährt nicht, was geschieht, sondern wird mit der direkten Beendigung der App konfrontiert (schlechtes Nutzererlebnis). 

7. Die App ist begrenzt ästhetisch.

### Soll-Zustand
Nachdem zuvor 7 Probleme klar identifiziert und definiert werden konnten, sind folgende Lösungsansätze für die Punkte 1-7 als meine Erweiterung der App vorgesehen:

1. Die Implementierung einer "Pause/Stop"-Schaltfläche, um das Vorlesen der Antwort abzubrechen.

2. Das Hinzufügen der scrollable-Fähigkeit zur Präsentationsfläche (TextView), um auch ältere Nachrichten nachschlagen zu können.

3. Die Implementierung einer Schaltfläche "Delete", um die bisherige Konversation zu löschen und automatische eine neue leere zu erstellen.

4. Das Ergänzen der App um ein Dropdown-Menü, auf welcher verschiedene Chats präsentiert werden. Angezeigt wird die aktuelle Auswahl auf der Präsentationsfläche.
   - Die verschiedenen Konversationen sollen klar mithilfe des Datums sowie der Uhrzeit identifiziert werden können. 
<img align="right" src="https://github.com/PapeMarc/app_entwicklung_BFAX422A/assets/147148804/69ddb012-eda8-4f0f-8ecd-1e9e616cd6f6" padding="5">

5. Das Anbinden der Room-Datenbank von Android zwecks der Serialisierung und Persistierung der Chats im Drop-Down Menü.

6. Die Implementierung von Try Catch zum Abfangen von Fehlern, sowie das Hinzufügen einer neuen kleinen Präsentationsfläche unter der bisherigen, zur Visualisierung der bisher aufgetretenen Fehler.

7. Um die App ästhetischer zu gestalten, soll ein Hintergrundbild hinter die Präsentationsfläche gelegt, entsprechende Icons im Rahmen der geplanten und bestehenden Schaltflächen eingebaut, Abstände und Mindestgrößen zur Darstellung im Hochformat (Vertikal) implementiert, sowie die Schriftfarbe der geplanten Fehlerbox auf Rot festgelegt werden.

Im Bild rechts sind nun die Stellen rot hervorgehoben, an denen die neuen GUI-Elemente platziert werden sollen. 
Während ganz oben das neue Dropdown Menü implementiert wird, soll direkt darunter die Präsentationsfläche die verschiedenen Nachrichten darstellen. 
Unter dieser wird dann die neue Fehlerbox eingebunden, welche auftretende Programmfehler anzeigt. 
Am unteren Bildschirmrand sind dann die vier Schaltflächen: "Delete","Add","Pause" und "Ask".


## Umsetzung

Bevor ich also angefangen habe meine Erweiterungen in die Vorgabe zu integrieren, habe ich folgenden Ablaufplan festgehalten, um meine Implementierung zeiteffizient umzusetzen:

Implementierung ...
1. ...der Schaltfläche "Delete".
2. ...der Fehlerbox und dazugehöriger Fehlerbehandlung.
3. ...der scrollability der TextView.
4. ...der Datenbank Room zum persistieren von Daten sowie dessen Schnittstelle.
5. ...der eventuellen Anpassungen bestehender Klassen.
6. ...des Dropdown-Menüs inklusive Anbindung an die Datenbank.
7. ...der Schaltflächen "New" und "Pause" sowie dessen Anbindung an die Logik.
8. ...der Abhängigkeiten zwecks der Positionierung der GUI-Elemente (Constraints).
9. ..., bzw. das Testen des Zusammenspiels der Erweiterungen sowie Behandeln evtl. auftretender Fehler.
10. ...eines Hintergrundbildes.
11. ...von verschiedenen Icons zur Aufarbeitung der GUI.

Auch wenn diese Liste noch oft geändert werden sollte, hatte ich vorerst einen Rahmen, an dem ich mich orientieren kann. Welche Faktoren diesen Plan wie umgestellt haben, wird im nächsten Abschnitt behandelt.

Kommen wir nun zur Umsetzung meiner Erweiterungen.

### Umfeld
Zu aller erst habe ich damit begonnen, diesen Zweig / "Branch" entsprechend der Vorgabe in meinem Repository "app_entwicklung_BFAX422A" zu erzeugen.
Anschließend habe ich Android Studio auch auf meinem Heim-PC eingerichtet und mit meinem mobilen Laptop verbunden.
Zudem habe ich mein Smartphone in den Entwicklermodus versetzt, um mit diesem meinen entwicklungsfortschritt live testen zu können, sowie einen Eindruck
zu bekommen, was der APP fehlt / ob meine Erweiterungen so realisierbar sind, wie gewünscht.

### Überarbeitung der grafischen Benutzeroberfläche
Die grafische Benutzeroberfläche meiner Android-Applikation wurde grundlegend Überarbeitet. 
Es wurde ein Hintergrundbild hinzugefügt, es wurden neue Schaltflächen eingebaut sowie mit Icons versehen,
es wurde eine Dropdownliste implementiert zwecks der Darstellung der verschiedenen Chats und es wurde eine 
Fehlerbox hinzugefügt um den Benutzer im Außnahmefall nicht direkt mit einem Absturz der Applikation zu konfrontieren.

### Implementierung der Datenbank
### Implementierung der Fehlerbehandlung

## Probleme während der Entwicklung

Auch wenn der Ablaufplan, welchen ich im letzten Abschnitt präsentiert hab eine solide Struktur zur Entwicklung meiner Erweiterungen bietet, so war ich mehrfach gezwungen ihn umzustellen. Nachfolgend nun die wirkliche Reihenfolge der Arbeitsschritte, die notwendig waren um meine Erweiterungen vollständig zu implementieren:

1. Schaltflächen "Delete" und "New",
2. Dropdown Menü,
3. Fehlerbox und grundlegende Fehlerbehandlung,
4. Room Datenbank und Entität Chat,
5. Datenzugriffsobjekt und Transferobjekt (DAO & DTO),
6. Erweiterte Fehlerbehandlung,
7. Umbau des Datenbankzugriffs / verwendung eines "Background Executer Service",
8. Fehlerbehandlung Spinner,
9. Umbau des Transferobjekts,
10. Fehlersuche und Beheben (rotieren des Geräts und laden des Speicherstandes),
11. Schaltfläche "Pause",
12. Testen aller Neuerungen und finale Säuberung.

Aber warum diese Umstellung? Nach Beginn der Entwicklung und nach dem hinzufügen der Schaltfläche "Delte", 
fand ich es sinnvoll mich (aufgrund bisheriger Erfahrungen mit Dropdown-Listen in Android) erst noch einmal
über Dropdown-Listen zu belesen, bevor ich weiter entwickle. Und das tat ich glücklicherweise früh genug,
denn ich erkannte, dass ich den Umfang, eine Dropdown-Liste zu implementieren, deutlich unterschätzt hatte. 
Daher zog ich die Entwicklung der Dropdown-Liste vor.


## Fazit des Projekts
