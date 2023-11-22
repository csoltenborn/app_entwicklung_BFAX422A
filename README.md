# Dokumentation

## Einleitung
Ich habe mehrere Erweiterungen am Code vorgenommen. Diese Änderungen beinhalten die Behebung eines Fehlers, das Hinzufügen von Einstellungen, die Einführung von Farben, die Anzeige von Benutzername und ChatGPT-Name (Assistantname), die Anpassung für leere Spracheingaben, die Umstellung von CharSequence auf String, die Integration von Übersetzungen, der Funktionalität Nachrichten als Text eingeben zu können, sowie die Änderung die TextView scrollbar zu machen. Diese Erweiterungen sollen die App funktionaler, benutzerfreundlicher und für den internationalen Markt zugänglicher machen.

## Anforderungen
Fehlerbehebung: Verhindern eines Absturzes der App
Einstellungen hinzufügen: Erweiterung der Einstellungsseite für Vergabe des Benutzernamens und ChatGPT-Namens
Farben hinzufügen: Visuelle Differenzierung der Nachrichten durch die Einführung von Farben
Absendernamen anzeigen: Anzeigen von Benutzername und ChatGPT-Name im Chat
Internationalisierung: Hinzufügen von Übersetzungen
Anfrage durch Texteingabe: Hinzufügen der Möglichkeit ChatGPT-Anfragen per Texteingabe auszuführen
Benutzerfreundlichkeit: TextView scrollbar machen

## Umsetzung
Ich habe mein Projekt damit begonnen einen sauberen Fork aufzusetzen, um die Umsetzung reibungslos gestalten zu können. Nachdem ich mich ein wenig in der Basisversion umgeguckt hatte, fiel mir auf, dass die Anwendung abstürzt, wenn man das Spracheingabefenster leer wegdrückt. Also baute ich an der entsprechenden Stelle einen „null check“ ein. Als nächstes suchte ich mir einen Einstiegspunkt, mit dem ich mein Projekt beginnen konnte. Einer der Anforderungen meines Projekts war es, deutlich erkennbar zu machen, von wem die Chatnachrichten stammen. Um dies zu bewerkstelligen wollte ich vor den Nachrichten den Namen des Absenders platzieren, sowie Farben hinzufügen. Da ich es für langweilig und unpersönlich hielt vor jede Nachricht des Nutzers einfach nur „User“ zu schreiben, fügte ich zunächst Einstellungen hinzu, über die der Nutzer sich und ChatGPT einen Namen geben kann. Anschließend definierte ich in der „PrefsFacade“-Klasse die entsprechenden „get“-Methoden, über welche später die Namen abgerufen werden können. Um farbigen Text hinzufügen zu können fügte ich dem MainFragment die Methode appendColoredText hinzu. Die Methode bekommt die TextView, den hinzuzufügenden Text, und eine Farbe übergeben. Zunächst wird die Länge des Inhalts der TextView abgefragt und in „start“ gespeichert. Anschleißend wird der neue Text hinzugefügt, die Länge erneut abgefragt und in „end“ gespeichert. Zwischen diesem Start- und Endpunkt, wo sich der neue Text befindet, wird die Textfarbe auf den übergebenen Wert gesetzt. Anschließend, so dachte ich, musste ich nur die Aufrufe der Standard append-Methode durch einen Aufruf von appendColoredText ersetzen und den Namen durch einen Aufruf der „get“-Methode, mithilfe des „+“-Operators, mit der Chat-Nachricht verbinden. Beim Aufruf meiner „appendColoredText“-Methode wurde mir eine Fehlermeldung angezeigt. Die Rückgabe der „toString“-Methode sei kein String. Als ich spontan „.toString“ an den Aufruf hing, verschwand die Fehlermeldung. Den Befehl „toString(userMessage).toString“  konnte ich so natürlich nicht stehen lassen. Ich stellte fest, dass die toString-Methode eine CharSequence zurückgab. Zunächst änderte ich den Eingabetyp der „appendColoredText“-Methode zu CharSequence. Dies behob den Fehler. Allerdings hielt ich String für den Standarddatentypen für Text, war es gewöhnt mit String zu arbeiten und mir viel auch kein anderer Grund ein keinen String zu benutzen. Also änderte ich alle Nutzungen von CharSequence zu String. Nachdem dieser Teil erfolgreich abgeschlossen war, entschied ich mich dazu den zunächst hardgecodeten Text der Einstellungen in Strings auszulagern und Übersetzungen hinzuzufügen. Während ich mich schonmal in den „XML“-Dateien befand, fügte ich direkt das Eingabefeld und den Button hinzu, welche ebenfalls Anforderung meines Projekts waren. Nachdem das Layout horizontal getestet war, wollte ich nachsehen, ob das Layout auch vertikal funktioniert. Das Layout sah zwar großartig aus, allerdings war die Farbe des Textes so wie der Name des Absenders verschwunden. Mir viel siedend heiß ein, dass wir gelernt hatten, dass die Activity beim Drehen des Bildschirms zerstört und neu aufgebaut wird, und dass wir zu diesem Zweck Parcelable in das Projekt eingebaut hatten. Ich erweiterte die „Message“-Klasse um die Strings „name“ und „color“. Im MainFragment gab ich beim Erstellen einer Message die entsprechenden Werte mit und passte die „updateTextView“-Methode so an, dass sie auch die „appendColoredText“-Methode nutzte. Im gleichen Atemzug änderte ich auch die toString erneut ab, um direkt dort den Namen des Absenders hinzuzufügen, anstatt diesen jedes Mal manuell anzuhängen. Der Text wurde nun auch nach dem Drehen des Displays korrekt angezeigt. Nachdem das Problem behoben war, machte ich mich daran die Übersetzungen fertigzustellen. Anschließend fügte ich zwei neue „get“-Methoden hinzu, um einen einfachen Zugriff auf das Eingabefeld, sowie den neuen Button zu gewährleisten. Um keinen doppelten Code zu erzeugen, verschob ich den Inhalt von LaunchSpeechRecognition in eine eigene Methode Namens „askChatGPT“. Diese rief ich nun bei einem Klick auf den Spracheingabeknopf oder Textknopf auf und übergab den entsprechenden Inhalt. Im Fall des Textknopfes wird vorher das Eingabefeld geleert. Abschließend fügte ich der TextView noch eine Scrollingmethod hinzu und entfernte überflüssige Kommentare.
Während des Schreibens der Dokumentation fiel mir auf, dass es sinnvoll wäre den Aufruf „Color.parseColor“ in die Methode appendColoredText zu verlagern, anstatt ihn jedes Mal vor dem Aufruf auszuführen. Also tat ich dies umgehend.

## Probleme

### Crash bei leerem Spracheingabe-Text
Wenn die Spracheingabe nach dem Öffnen ohne Eingabe wieder geschlossen wurde, stürzte die Anwendung ab. Ich behob den Fehler, indem ich einen „null check“ hinzufügte, bevor die Eingabe genutzt wird.

### Verlust der Farbe bei drehen des Displays
Wenn das Display gedreht wurde, verschwanden die Farbe des Textes, sowie User- und ChatGPT-Name. Dies lag daran, dass ich vergessen hatte beides über die Parcelable-Klassen zu speichern und in updateTextView wiederherzustellen.

## Fazit
Insgesamt verlief das Projekt weitgehend nach Plan, und alle Anforderungen konnten erfolgreich erreicht werden. Trotzdem blieb ich nicht von Fehlern verschont, insbesondere von einem einzigen Fehler: dem Versäumnis, die neuen Änderungen in Parcelable zu implementieren. Dies führte zu zusätzlichem Aufwand und erforderte nachträgliche Anpassungen. Dennoch blieb der zeitliche Aufwand im Rahmen der ursprünglichen Erwartungen, und die Herausforderungen, die sich durch das anfängliche Fehlen der „Parcelable“-Integration ergaben, konnten erfolgreich bewältigt werden.
