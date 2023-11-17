# Dokumentation meiner App-Erweiterung
## Vorwort
Während meines Studiums der Angewandten Informatik an der Fachhochschule der Wirtschaft (FHDW) in Bergisch Gladbach, wurde 
im Rahmen des Moduls "App-Projekt" unter Aufsicht von Herrn Dr. Christian Soltenborn eine Android-App zu Lernzwecken umgesetzt. 
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

2. Der Benutzer kann ältere Antworten nicht nachschlagen, da die Textfläche zur Darstellung durch die Bidlschirmgröße des Endgeräts begrenzt ist,
   sowie keine Funktionalität zum scrollen aufweist.

3. Es gibt keine Möglichkeit zum löschen seiner bisherigen Nachrichten um den Kontext der Konversation zu ändern.
   - Der Benutzer kann immer nur eine Konversation mit ChatGPT führen.

4. Der Benutzer kann keine neuen Chats erstellen oder solche Abrufen.

5. Der Nachrichtenverlauf der Konversation wird nicht persitiert / die geführte Unterhaltung wird nicht gespeichert und kann daher auch nicht zu einem
   späteren Zeitpunkt eingesehen werden. Startet der Benutzer die App also neu, verliert er alle seine Nachrichten.

6. Fehler werden nicht abgefangen und führen zum Absturz der APP. Der Benutzer erfährt nicht, was geschieht, sondern wird mit der direkten Beendigung der App konfrontiert (schlechtes Nutzererlebnis). 

7. Die App ist begränzt Ästetisch.

### Soll-Zustand
Nachdem zuvor 7 Probleme klar identifiziert und definiert werden konnten, sind folgende Lösungsansätze für die Punkte 1-7 als meine Erweiterung der App vorgesehen:

1. Die Implementierung einer "Pause/Stop"-Schaltfläche, um das Vorlesen der Antwort abzubrechen.
2. Das Hinzufügen der scrollable-Fähigkeit zur Präsentationsfläche (TextView), um auch ältere Nachrichten nachschlagen zu können.
3. Die Implementierung einer Schaltfläche "Delete", um die bisherige Konversation zu löschen und automatische eine neue leere zu erstellen.
4. Das Ergänzen der App um ein Dropdown-Menü, auf welcher verschiedene Chats präsentiert werden. Angezeigt wird die aktuelle Auswahl auf der Präsentationsfläche.
   - Die verschiedenen Konversationen sollen klar mithilfe des Datums sowie der Uhrzeit identifiziert werden können.
5. Das Anbinden der Room-Datenbank von Android zwecks der serialisierung und persistierung der Chats im Drop-Down Menü.
6. Die Implementierung von Try Catch zum Abfangen von Fehlern, sowie das Hinzufügen einer neuen kleinen Präsentationsfläche unter der bisherigen, zur Visualisierung der bisher aufgetretenen Fehler.
7. Um die App ästetischer zu gestalten, soll ein Hintergrundbild hinter die Präsentationsfläche gelegt, entsprechende Icons im Rahmen der geplanten und bestehenden Schaltflächen eingebaut, Abstände und Mindestgrößen zur Darstellung im Hochformat (Vertikal) implementiert, sowie die Schriftfarbe der geplanten Fehlerbox auf Rot festgelegt werden.


## Umsetzung

## Probleme während der Entwicklung

## Fazit des Projekts
