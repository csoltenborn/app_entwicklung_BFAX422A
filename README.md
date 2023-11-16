# Dokumentation ChatGPT App Erweiterung - Tanja Vetter

## Einleitung

Ich habe die Möglichkeit, eine Nachricht des Chatverlaufs zu teilen, eingebaut. Dafür war es nötig, das User Interface umzugestalten, damit jede Nachricht des Chatverlaufs in einem einzelnen Textfeld angezeigt wird, um eine Nachricht zum Teilen auswählen zu können.

Das Datum und die Uhrzeit, zu der eine Nachricht gesendet wurde, lässt sich jetzt ebenfalls einsehen.

Außerdem ist es nun möglich, Anfragen an ChatGPT per Texteingabe zu stellen.

Als letztes habe ich noch das App Icon geändert, Hintergrund war da allerdings nur eine bessere Unterscheidungsmöglichkeit zwischen der „echten“ ChatGPT App und der selbst implementierten.

## Anforderungen

Ich möchte eine spezifische Nachricht durch langes Klicken auf diese auswählen. Das soll zum Öffnen eines Dialogs namens *Optionen* führen. Als Optionen stehen *Teilen* und *Info* zur Verfügung. *Teilen* öffnet den Standarddialog von Android, um Inhalte in einer anderen App zu teilen, *Info* zeigt in einem Dialog den Wochentag, das Datum und die Uhrzeit, zu der die Nachricht gesendet wurde, an.

Wird der Chatverlauf länger als die Anzeigegröße des Bildschirms, sollte für die bessere Nutzererfahrung immer automatisch zur letzten Nachricht gescrollt werden.

Unten in der App gibt es nun ein Textfeld, in das eine Nachricht eingegeben werden kann. Daneben befindet sich ein Button, um die eingegebene Nachricht abzusenden und ein weiterer Button, mit dem Anfragen weiterhin auch über die Spracheingabe realisiert werden können.

## Umsetzung

### UI des Chatverlaufs

Um eine beliebige Nachrichtenanzahl darstellen zu können und dafür zu sorgen, dass bei vielen Nachrichten auch gescrollt werden kann, habe ich mich dazu entschieden den Chatverlauf mit einer *RecyclerView* darzustellen. Dabei ist jede Nachricht ein Element der *RecyclerView*.

In [*fragment_main.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/fragment_main.xml) habe ich dafür die *TextView* durch eine *RecyclerView* ersetzt.
Die *RecyclerView* rendert so viele Elemente untereinander, wie die übergebene Datenquelle lang ist.

Für jedes Element muss ebenfalls ein Layout festgelegt werden. Dies geschieht in [*chat_item.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/chat_item.xml). Da ein Element nur eine Nachricht anzeigen muss, besteht es auch nur aus einer *TextView*.

Die Datei [*rounded_corners.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/drawable/rounded_corners.xml) stellt ein Rechteck mit abgerundeten Ecken dar. Dies ist der Hintergrund einer Nachricht. Die Farbe wird erst beim Schreiben abhängig vom Autor der Nachricht festgelegt.

Außerdem habe ich das Farbschema basierend auf [Googles Material Design 3](https://m3.material.io/) angepasst und Primär- und Sekundärfarben festgelegt.

### RecyclerView

Um die *RecyclerView* füllen zu können, wird ein [*Adapter*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/model/ChatAdapter.java) benötigt. Diesem muss man eine Liste der anzuzeigenden Elemente übergeben. In diesem Fall ist dies die Liste der Chatnachrichten. Innerhalb des Adapters gibt es noch einen *ViewHolder* für jede einzelne Nachricht. In diesem wird die Nachricht an das Textfeld gebunden. Dabei wird noch überprüft von welchem Autor die Nachricht kommt und der Hintergrund entsprechend gefärbt.

Um die Optionen (*Teilen* und *Info*) aufzurufen, wird ein OnLongClickListener gesetzt. Wenn eine Nachricht lange gedrückt wird, öffnet sich ein Dialog. Je nachdem welche der beiden Optionen angeklickt wird, wird eine weitere Funktion aufgerufen.

Zum Teilen wird ein *Intent* der Kategorie *ACTION_SEND* gestartet, was den standardmäßigen Teilen Dialog von Android öffnet.

Zum Anzeigen der Info (also Datum und Uhrzeit) wird das Datum nach dem deutschen Standard formatiert und als Nachricht in einem *AlertDialog* angezeigt.

Im [*MainFragment*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/MainFragment.java) wird in *onViewCreated* ein Adapter Objekt erstellt und der *RecyclerView* zugewiesen. Jedes Mal, wenn eine Nachricht hinzugefügt wird, also nachdem der Nutzer eine Anfrage gestellt hat oder ChatGPT geantwortet hat, wird der Adapter mit *chatAdapter.notifyItemInserted* darüber informiert, dass ein Element eingefügt wurde, damit dies entsprechend der *RecyclerView* hinzugefügt werden kann. Außerdem wird mit *recyclerView.scrollToPosition* bewirkt, dass immer ans untere Ende gescrollt wird.

### Texteingabe

Für die Texteingabe habe ich unten in [*fragment_main.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/fragment_main.xml) ein *TextInputLayout* hinzugefügt, dass eine *TextInputEditText* View enthält. Um die Nachricht abzusenden, gibt es daneben einen *FloatingActionButton* mit einem Senden Icon. Für die Spracheingabe gibt es einen weiteren *FloatingActionButton* mit einem Mikrofon Icon. Die Icons sind wieder aus dem [Material Design 3](https://fonts.google.com/icons).
Für beide Buttons habe ich im [*MainFragment*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/MainFragment.java) *OnClickListener* implementiert.

Da die Anfrage an ChatGPT unabhängig von der Eingabeform gestellt wird, habe ich diese (bereits in der Vorlesung implementierte) Logik in eine eigene Funktion namens *askChatGPT* ausgelagert. Zusätzlich wird wie bereits erläutert der Adapter über eine neue Nachricht informiert und ans Ende der Liste gescrollt.

Wird der Button mit dem Mikrofon geklickt, passiert genau das gleiche wie vorher. Klickt der Nutzer hingegen auf den Senden Button, wird natürlich auch zunächst die Anfrage an ChatGPT geschickt. Außerdem wird die Tastatur mit der Funktion *hideKeyboard* versteckt und der Text aus dem Eingabefeld wird entfernt.

## Probleme

Jede Nachricht ist ein Element der RecyclerView. Das sorgt dafür, dass das Textfeld vom Design her immer identisch ist. Allerdings ist es in Chatverläufen üblich, dass Nachrichten rechts bzw. links orientiert sind, je nachdem, wer die Nachricht gesendet hat. Diese Umsetzung war für mich nicht möglich, allerdings habe ich mir folgende Alternative überlegt: je nachdem von wem, die Nachricht stammt, erhält das Textfeld einen andersfarbigen Hintergrund. So lässt sich trotzdem noch identifizieren, wer die Nachricht gesendet hat.

## Fazit

Ich konnte bis auf das beschriebene Problem alles wie geplant umsetzen. Auch für das Problem habe ich eine meiner Meinung nach gleichwertige Lösung gefunden, so dass die App alle geplanten Funktionalitäten erhalten hat.

Der zeitliche Aufwand für die eigentliche *Teilen* Funktion war deutlich geringer als erwartet, da Android schon viel dafür bereitstellt. Da ich für das Teilen einer einzelnen Nachricht allerdings die Anzeigelogik des Chatverlaufs umbauen musste, war dieser Teil doch umfangreich.

Die *Info* Funktion war sehr simpel, da ich bereits den „Optionen“ Dialog erstellt hatte und das Datum im Hintergrund bereits immer gespeichert wird.

Eine Texteingabe zusätzlich zur Spracheingabe hinzuzufügen war ebenfalls nicht sehr kompliziert.
