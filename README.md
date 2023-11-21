 
# Dokumentation ChatGPT App Erweiterung - Tanja Vetter

## Einleitung
Ich habe die Möglichkeit, eine Nachricht des Chatverlaufs zu teilen, eingebaut. Dafür war es nötig, das User Interface umzugestalten, damit jede Nachricht des Chatverlaufs in einem einzelnen Textfeld angezeigt wird, um eine Nachricht zum Teilen auswählen zu können.

Das Datum und die Uhrzeit, zu der eine Nachricht gesendet wurde, lässt sich jetzt ebenfalls einsehen.

Außerdem ist es nun möglich, Anfragen an ChatGPT per Texteingabe zu stellen.

Als letztes habe ich noch das App Icon geändert, Hintergrund war dabei allerdings nur eine bessere Unterscheidungsmöglichkeit zwischen der „echten“ ChatGPT App und der selbst implementierten.

## Anforderungen
Ich möchte eine spezifische Nachricht durch langes Klicken auf diese auswählen. Das soll zum Öffnen eines Dialogs namens *Optionen* führen. Als Optionen stehen *Teilen* und *Info* zur Verfügung. *Teilen* öffnet den Standarddialog von Android, um Inhalte in einer anderen App zu teilen, *Info* zeigt in einem Dialog den Wochentag, das Datum und die Uhrzeit, zu der die Nachricht gesendet wurde, an.

Wird der Chatverlauf länger als die Anzeigegröße des Bildschirms, soll für die bessere Nutzererfahrung immer automatisch zur letzten Nachricht gescrollt werden.

Unten in der App gibt es nun ein Textfeld, in das eine Nachricht eingegeben werden kann. Daneben befindet sich ein Button, um die eingegebene Nachricht abzusenden und ein weiterer Button, mit dem Anfragen weiterhin auch über die Spracheingabe realisiert werden können.

## Umsetzung

### UI des Chatverlaufs
Um eine beliebige Nachrichtenanzahl darstellen zu können und dafür zu sorgen, dass bei vielen Nachrichten auch gescrollt werden kann, habe ich mich dazu entschieden, den Chatverlauf mit einer *RecyclerView* darzustellen. Dabei ist jede Nachricht ein Element der *RecyclerView*.

In [*fragment_main.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/fragment_main.xml) habe ich dafür die *TextView* durch eine *RecyclerView* ersetzt.

Die *RecyclerView* rendert so viele Elemente untereinander, wie die übergebene Datenquelle lang ist.

Für jedes Element muss ebenfalls ein Layout festgelegt werden. Dies geschieht in [*chat_item.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/chat_item.xml). Da ein Element nur eine Nachricht anzeigen muss, besteht es auch nur aus einer *TextView*.

Die Datei [*rounded_corners.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/drawable/rounded_corners.xml) stellt ein Rechteck mit abgerundeten Ecken dar. Dies ist der Hintergrund einer Nachricht. Die Farbe wird erst beim Schreiben abhängig vom Autor der Nachricht festgelegt.

Außerdem habe ich das Farbschema basierend auf [Googles Material Design 3](https://m3.material.io/) angepasst und Primär- und Sekundärfarben festgelegt.

### RecyclerView
Um die *RecyclerView* füllen zu können, wird ein [*Adapter*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/model/ChatAdapter.java) benötigt. Diesem muss man eine Liste der anzuzeigenden Elemente übergeben. In diesem Fall ist dies die Liste der Chatnachrichten. Innerhalb des Adapters gibt es noch einen *ViewHolder*. Da der Adapter (im Hintergrund) über die Liste der Nachrichten iteriert, wird für jede Nachricht ein Objekt der Klasse ViewHolder erstellt. Um die tatsächliche Nachricht in der Benutzeroberfläche sichtbar zu machen, wird diese mithilfe der Funktion *bind* der ViewHolder Klasse als Text der *TextView* gesetzt. Dabei wird noch überprüft, von welchem Autor die Nachricht kommt und der Hintergrund entsprechend gefärbt. Stammt die Nachricht vom *User*, erhält der Hintergrund die Primärfarbe, in den beiden anderen Fällen (*Assistant* oder *System*) die Sekundärfarbe.

Um die Optionen (*Teilen* und *Info*) aufzurufen, wird ein *OnLongClickListener* für jede Nachricht im Konstruktor des *ViewHolders* gesetzt. Wenn eine Nachricht lange gedrückt wird, öffnet sich ein *AlertDialog* mit den entsprechenden beiden Optionen als Elemente. Je nachdem welche der beiden Optionen angeklickt wird, wird eine weitere Funktion aufgerufen, zum Teilen *shareMessage* und zum Anzeigen der Info (also Datum und Uhrzeit) *showInfoDialog*.

In *shareMessage* wird ein *Intent* der Kategorie *ACTION_SEND* mit dem textuellen Inhalt der Nachricht als Extra gestartet, was den standardmäßigen Teilen-Dialog von Android öffnet.

In *showInfoDialog* wird das Datum in einen Instant umgewandelt und mithilfe der Klasse *DateTimeFormatter* in das Format "Wochentag Tag.Monat.Jahr Stunden:Minuten:Sekunden" mit Deutschland als Zeitzone formatiert und als Nachricht in einem *AlertDialog* angezeigt.

Im [*MainFragment*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/MainFragment.java) wird in *onViewCreated* ein Adapter Objekt erstellt und der *RecyclerView* zugewiesen.

Jedes Mal, wenn eine Nachricht hinzugefügt wird, also nachdem der Nutzer eine Anfrage gestellt hat oder ChatGPT geantwortet hat, wird der Adapter mit *chatAdapter.notifyItemInserted* darüber informiert, dass ein Element eingefügt wurde, damit dies entsprechend der *RecyclerView* hinzugefügt werden kann. Außerdem wird mit *recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1)* bewirkt, dass immer ans untere Ende des Chatverlaufs gescrollt wird.

### Texteingabe
Für die Texteingabe habe ich unten in [*fragment_main.xml*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/res/layout/fragment_main.xml) ein *TextInputLayout* hinzugefügt, das eine *TextInputEditText* View enthält. Dieses hat den Style *OutlinedBox* von Material Design 3. Wenn noch nicht in das Feld geklickt wurde, wird der Hinweistext *Nachricht eingeben...* angezeigt. Außerdem erweitert sich das Textfeld bei einer mehrzeiligen Nachricht aufgrund des Attributs *android:inputType="textMultiLine"*. Um die Nachricht abzusenden, gibt es daneben einen *FloatingActionButton* mit einem Senden Icon. Für die Spracheingabe gibt es einen weiteren *FloatingActionButton* mit einem Mikrofon Icon. Die Icons sind wieder aus dem [Material Design 3](https://fonts.google.com/icons).

Für beide Buttons habe ich im [*MainFragment*](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/blob/app_extension/app/src/main/java/de/fhdw/app_entwicklung/chatgpt/MainFragment.java) *OnClickListener* implementiert.

Da die Anfrage an ChatGPT unabhängig von der Eingabeform gestellt wird, habe ich diese (bereits in der Vorlesung implementierte) Logik in eine eigene Funktion namens *askChatGPT* ausgelagert. Zusätzlich wird, wie bereits erläutert, der Adapter sowohl nach dem Empfangen der Nachricht des Benutzers als auch nach der Antwort von ChatGPT über eine neue Nachricht informiert und ans Ende der Liste gescrollt. Dies muss allerdings explizit auf dem UI-Thread geschehen (*requireActivity().runOnUiThread*).

Wird der Button mit dem Mikrofon geklickt, passiert genau das gleiche wie bereits in der Vorlesung implementiert, nur das diese Logik (mit der kleinen Ergänzung für die *RecyclerView*) in die Funktion *askChatGPT* ausgelagert wurde.

Klickt der Nutzer hingegen auf den Senden Button, wird natürlich auch zunächst *askChatGPT* aufgerufen und als Nachricht hier der Text, der in das Eingabefeld eingegeben wurde (*getQuestion().getText().toString()*), abgesendet. Außerdem wird die Tastatur mit der Funktion *hideKeyboard* versteckt und der Text aus dem Eingabefeld wird entfernt, indem der Text auf *""* gesetzt wird.

![UI](https://github.com/tanjavetter04/app_entwicklung_BFAX422A/assets/126447698/b74da733-e0a9-4abb-ba89-066e1db74c00)

## Probleme
Jede Nachricht ist ein Element der *RecyclerView*. Das sorgt dafür, dass das Textfeld vom Design her immer identisch ist. Allerdings ist es in Chatverläufen üblich, dass Nachrichten rechts bzw. links orientiert sind, je nachdem, wer die Nachricht gesendet hat. Diese Umsetzung war für mich nicht möglich, allerdings habe ich mir folgende Alternative überlegt: Je nachdem, von wem die Nachricht stammt, erhält das Textfeld einen andersfarbigen Hintergrund. So lässt sich trotzdem noch identifizieren, wer die Nachricht gesendet hat.

## Fazit
Ich konnte bis auf das beschriebene Problem alles wie geplant umsetzen. Auch für das Problem habe ich eine meiner Meinung nach gleichwertige Lösung gefunden, so dass die App alle geplanten Funktionalitäten erhalten hat.

Der zeitliche Aufwand für die eigentliche *Teilen* Funktion war deutlich geringer als erwartet, da Android schon viel dafür bereitstellt. Da ich für das Teilen einer einzelnen Nachricht allerdings die Anzeigelogik des Chatverlaufs umbauen musste, war dieser Teil doch umfangreich.

Die *Info* Funktion war sehr simpel, da ich bereits den *Optionen* Dialog erstellt hatte und das Datum im Hintergrund schon immer gespeichert wird.

Eine Texteingabe zusätzlich zur Spracheingabe hinzuzufügen war ebenfalls nicht sehr kompliziert.
