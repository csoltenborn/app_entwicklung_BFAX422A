# App_entwicklung_BFAX422A 

Meine App-Erweiterung: 

Meine App-Erweiterung inkludiert eine Texteingabe, welche die Spracheingabe ergänzt, indem sie einen String an ChatGPT schickt, statt die nur die Spracheingabe zu nutzen. 
Sie verbessert die App, indem sie dem Nutzer erlaubt eine Alternative zu Spracheingabe zu verwenden, um mit ChatGPT zu kommunizieren. 


Funktionale Anforderung (der Erweiterung): 

-	Funktion 1: Textfeld („id: Textfield“)  Der Benutzer bekommt Zugang zu einem Feld, indem er eine Nachricht an ChatGPT schreiben kann.

![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/45beebf3-8d90-4b3b-8300-59cc5ccda254)

Abb. 1

-	Funktion 2: Knopf („id: send_Button“) Nachdem der Nutzer seine Nachricht im Textfeld 
geschrieben hat, kann er mit dem Button die Nachricht an ChatGPT absenden, um eine Antwort zu erhalten.



![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/8ec94aa7-7e56-49b3-a61c-dfca8b699c20)


Abb. 2

Umsetzung der Erweiterung:

Die Umsetzung erfolgte, indem Zunächst die Komponenten Textfeld und Knopf implementiert werden (mit entsprechenden IDs) in der XML-Datei „main-Fragment“ müssen (siehe Abb. 1-2). 
Daraufhin müssen wir Zugang zum Button als auch Textfeld, bzw. dem Inhalt des Textfeldes in unseren Quellcode bekommen. Dies ermöglichen wir, indem wir zwei Getter-Methoden implementieren und dabei aus der View die IDs der jeweiligen Komponenten verwenden (siehe Abb.3-4). 
Bevor wir sagen können, wie der Button handeln soll, müssen wir zunächst eine Methode schreiben die einen String als Parameter hat (nämlich der Inhalt des Textfelds) und diesen ChatGPT schickt, bzw. dessen Antwort auch auf der TextView darstellt. Solch ein Codeabschnitt haben wir glücklicherweise schon in der Vorlesung geschrieben. Deshalb fehlte nur noch diesen Abschnitt in eine Methode auszulagern (siehe Abb. 5) und wieder zu Verwenden.
Zum Schluss fehlt nur noch das Verhalten des Buttons zu konfigurieren. Dieser soll nämlich beim Betätigen, den Text des Textfeldes an ChatGPT schicken bzw. den String in die zuvor erwähnte Methode packen und diese ausführen (siehe Abb. 6).



![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/4d26720e-e634-4b30-870d-89644c095a86)

 
Abb. 5


![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/cf80648e-b17b-4455-a41a-3c4bc5c28621)


Abb. 3


![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/b21e820d-284f-4153-b59e-67cbbf253d3b)

 
Abb. 4


![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/28c992fc-ffc6-4d50-bbe2-88e857c8b612)

 
Abb. 5


![image](https://github.com/YassineH03/app_entwicklung_BFAX422A/assets/147349679/fcb745b8-b03c-4936-bf0d-d739157a3beb)

 
Abb. 6
	

Komplikationen: 

An der Stelle, an der es am meisten gehackt hat, war nicht die Erstellung der Bausteine (Textfeld, Button), sondern wie die Bausteine mit einer kommunizieren. Denn Zunächst war mein Ansatz den Aufbau wie in der „LaunchSpeechRecognition- Klasse“ aufzubauen und Intents zu verwenden (siehe LaunchTextfield- Klasse), jedoch ist mir nach kurzer Zeit klar geworden- nachdem ich nämlich recherchiert hatte zu was die Bausteine in der Lage waren, dass es ein viel zu umständlicher Ansatz war.


Lesson Learned: 

Je besser man die Elemente versteht, die man verwendet, desto leichter und effektiver kann deren Nutzung sein. 


Fazit:

Ich war in der Lage meine App-Erweiterung im gegeben Zeitrahmen umzusetzen, dabei war die Aufgabe sowohl herausfordernd als auch spaßig.

