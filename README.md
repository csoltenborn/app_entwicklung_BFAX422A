# Projekt App Enticklung - Abgabe Fabian Stefer

In dieser ReadMe Datei soll die Dokumentation des Projektes "Google Sign" erfolgen. Das Projekt
setzt
auf das den bereits vorhandenen Code aus
Projekt [ChatGPT](https://github.com/csoltenborn/app_entwicklung_BFAX422A)
aus der Vorlesung "App Entwicklung" von Christian Soltenborn auf.

Die Umsezung des Projektes befindet sich im Branch "projektabgabe_google_sign_in"

## Vorstellung der Erweiterung

Im folgenden stelle ich vor, welche Erweiterung ich vorgenommen habe. Bei der Erweiterung handelt es
sich um eine Authentifizierung via Google.

### Was ist "Über Google Anmelden"?

Mit der Authentifizierungsfunktion "Über Google Anmelden" können sich Nutzer bei einem Dienst
authentifizeren und authorisieren. Dies kann über verschiedene Platformen hinweg erfolgen, zum
Beipiel in einer App oder im Web. Verwendet wird hierbei ein bereits vorhandener Google Account.
Auf dessen Basis kann der Betreiber des Dienstes auch ein eigenes Konto in seinem Dienst erstellen,
da der Authentifizierungsservice Daten des Users, wie zum Beispiel E-Mail und Name weiter gibt.
Nachdem sich der User erstmalig angemeldet hat, ist es außerdem möglich den User beim Aufruf des
Dienstes ohne weitere Schritte einzuloggen. Im Hintergrund spricht der Service mit der
[Google Identity Services Authorization API](https://developers.google.com/identity/oauth2/web/guides/overview?hl=de)
. Um die API nutzen zu können, muss der Entwickler vorher die Anwendung über die Google Identity
Console konfigurieren. Mehr Informationen dazu erfolgen im Kaptiel "KAPITEL EINGÜGEN". Im
Hintergrund verwendet Google für diesen Service "OAuth" und "OpenId Conntect". Mehr dazu im
Folgenden.

> Quelle: [Google Developers](https://developers.google.com/identity/gsi/web/guides/overview?hl=de)

### OAuth2 und OpenId Connect

Bei Open Id Connect (kurz auch OIDC) handelt es sich um Authentifizierungsprotokoll, welches
die Authentifizierung von Nutzern in einer Anwendung standartiseren und vereinfachen soll.
Der Authentifizeriungsserver führt hierbei die Authentifizerung durch. Entwickler einer Anwendung
können die Anmeldeablauf starten und durch weitere Spezifikationen, wie die Verschlüsselung von
Identitätsdaten, die Erkennung von Identitätsanbietern oder die Sitzungsabmeldung erweiter werden.
Die Verantwortung für das Festlegen, Speichern und Verwalten von Passwörten entfällt hierbei
komplett
in der Anwendung und wird auf den Authentifizeriungsserver übertragen. Man kann in einer Anwendung
auch verschiedene Identitäsanbieter verwenden und es ist für jeden möglich, selbst zum
Identitäsanbieter
zu werden.

> Quelle: [OpenID](https://openid.net/developers/how-connect-works/)

Während sich OIDC um die authentifizierung kümmert, beschäftigt sich OAuth2 mit der autorsierung.
Es verwendet Zugrifftokens, um Zugriff auf bestimmt Ressourcen, wie zum Beispiel APIs zu verwalten.
Hierzu werden oft JSON Web Token (JWTs) genutzt. In diesen kann man direkt verschiedene Rollen
speichern, welche in der Anwendung in Berechtigungen übersetzt werden können.

> Quelle: [Auth0](https://auth0.com/de/intro-to-iam/what-is-oauth-2)

### Inhalt und Nutzen der Anwendung

Die Erweiterung des Projektes soll nun auch den "Anmelden über Google" Button in das ChatGPT Projekt
bringen. Der Benutzer soll sich mit seinem Google Account in der App anmelden und anschließend soll
ihm
eine personalisierte Willkommensnachricht ausgespielt werden.  
Durch die Anmeldung können wir ein unerlaubtes Benutzen der App verhindern und gleichzeitig auf
einer perönlichen Ebene mit dem Nutzer interagieren. Wir haben einen Überblick, wer unsere App
benutzt und können perspektivisch sogar gewissse Inhalte mit einer Bezahloption bereit stellen oder
den
Benutzer per Mail über Änderungen an der App informieren.

<div style="display: flex; justify-content: space-between;">
<img alt="Bildschirmfoto 2023-11-15 um 13.46.47.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2013.46.47.png"/>
<img alt="Bildschirmfoto 2023-11-15 um 14.33.21.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2014.33.21.png"/>
</div>

> Der Benutzer meldet sich in der App an und bekommt im Anschluss eine von ChatGPT personalisierte
> Willkommensnachricht angezeigt

## Implementierung

### Einrichtung in der Google Cloud Console

Damit sich die App mit Google Identity verbinden kann, sind noch einige Einstellung in der
Google Identity Console nötig:

1. Zuerst muss man
   die [Google Cloud API Console](https://console.cloud.google.com/welcome?hl=de&project=peppy-linker-244912)
   im Browser aufrufen und sich einloggen. Hat man sich bereits in Chrome mit seinem Google Account
   eingeloggt und Öffnet diesen, ist man meistens automatisch eingeloggt.

2. Hier erstellen wir ein neues Google
   Cloud-Projekt [nach folgender Anleitung](https://cloud.google.com/resource-manager/docs/creating-managing-projects?hl=de)

3. Nachdem wir das Projekt ausgewählt erstellen wir unter "APIs und Dienste", "Anmeldedaten",
   "Anmeldedaten erstellen" ein neue OAuth-Client-ID.

  <img alt="Bildschirmfoto 2023-11-15 um 14.56.26.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2014.56.26.png"/>

> Übersicht der Einstellungen, welche wir in der Google Cloud Console anpassen müssen

4. Als Anwendungstyp wählen wir "Android" aus. Den Namen können wir frei wählen. Um nun den
   Paketnamen
   und den SHA-1 Zertifikatsabdruck herauszufinden schauen wir in unser App Repository, welches
   wir erweitern möchten.

   Den Paketnamen finden wir in der build.gradle im Ordner "app" unter "namespace"

   Für den SHA-1 Fingerabdruck öffnen wir den Reiter "Gradle" in Android Studio, in dem wir "Excute
   Gradle Task" auswählen und folgenden Befehl ausführen:

   `gradle signingReport`

   Im signingReport finden wir unter SHA1 unseren SHA-1 Zertifikatsabdruck.

5. Haben wir alle Werte richtig eingetragen, klicken wir auf "Erstellen" und sind damit in der
   Cloud Console fertig.

### Erstellung der SignInActivity

Die Aufforderung zur Anmeldung soll erfolgen, wenn der Benutzer die App öffnet. Da es sich bei
der Anmeldung um einen geschlossen Vorgang handelt, der danach nicht mehr relevant für weitere
Interaktionen des Benutzers mit dem System ist, habe ich mich dazu entschlossen, eine neue Activity
für diesen Vorgang zu erstellen. Dafür habe ich im Activity Wizard einen Empty View Activity
erzeugt.
Aufgrund von Übersichtichtlichkeit und Erweiterbarkeit habe ich außerdem ein neues Fragment erzeugt,
welches in der Main Activity angezeigt werden soll. Nachdem ich beide Objekte erzeugt habe, konnte
ich mit dem Design der Ui beginnen. Glücklicherweise stell Google bereits einen Anmelden Button
bereit dafür musst ich nur folgende Dependencies per Gradlle installieren:

> implementation 'com.google.android.gms:play-services-auth:20.7.0'  
  
Danach konnte ich mit dem UI Design beginnen. Dafür musste ich nur Button einfügen und per
Constraint Layout mittig im Fragment platzieren. Für den Fall, dass aus irgendeinem Grund die
Anmeldung bei
Google fehlschlägt, habe ich noch eine Error Meldung eingebaut. Diese soll erst sichtbar werden,
sobald die Google Anmeldung fehlgeschlagen ist. Das Ergebnis sah dann wiefolgt aus:  
  
<img alt="Bildschirmfoto 2023-11-15 um 15.21.45.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2015.21.45.png"/>

Anschließend konnte ich mit der Implementierung der Logik anfangen. Der Großteil der Logik befindet
sich im SignInFragement. Bei der Implementierung habe ich mich haputsächlich an der Anleitung orientiert, 
[welche Google hierfür bereit stellt](https://developers.google.com/identity/sign-in/android/start-integrating?hl=de)
. Laut der Anleitung stellt Google bereits eine Klasse **GoogleSignInClient** bereit, welche wir mit dem
**GoogleSignInOptions** konfigurieren können. Diese habe ich nun in der onCreate Methode erzeugt 
und konfiguriert. Damit Google den benutzer nun anmeldet, wenn man auf den Button klickt, musste ich
dem Button noch einen Listener hinzufügen. Der Listener startet einen Intent, in welchem sich
der User einloggen kann. Wenn der User fertig ist soll er in die MainActivity weiter geleitet
werden und dabei soll der Name des Nutzer direkt übergeben werden. Dafür habe ich einen
**ActivityResultLauncher** erstellt. Falls der User erfolgreich eingeloggt wird, erzeugt dieser
ein Intent mit der MainActivity und setzt den Benutzernamen. Falls der Login fehlschlägt, wird
der Error Text in der UI sichtbar. Zum Schluss musste ich in der **AndroidManifest.xml** noch 
einstellen, dass die SignInActivity beim starten der App gestartet wird. Danach funkionierte der 
Login.

<img alt="Bildschirmfoto 2023-11-15 um 13.46.47.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2013.46.47.png"/>

> Der User konnte sich danach über den Button anmelden um die MainActity zu gelangen.

### Erstellung einer Begrüßungsnachricht

Da wir nun über die Information verfügen, wie der Benutzer heißt, können wir ihm eine personalisierte
Begrüßungsnachricht per ChatGPT ausspielen. Da der vorherige Code diese Funktionalität noch nicht
bereit stellte, musste ich diesen erweitern. Angfangen habe ich der Klasse **ChatGPT**. Dort
habe ich die Methode **getGreetingsMessage** erstellt. Diese erzeugt einen leeren Chat und fügt 
einen Befehl für ChatGPT hinzu, eine Begrüßungsnachricht zu erzeugen. Der Wortlaut lautet wie folgt:

> Bitte erstelle mir eine Wilkommensnachricht für %s. Begrüße die Person bitte im Chat.

"%s" wird später durch den Benutzername ersetzt. Anschließend habe ich im **MainFragment** noch 
den benötigten Code hinzugefügt, um die Willkommensnachricht anzuzeigen. Dafür war es notwendig, das 
ChatGpt Objekt in der Klasse verüfbar ist. Nachdem ich es in der Klasse erreichbar gemacht habe,
habe ich die Methode **createExtraGreetingMessage** erstellt. Hier wird der Benutzername aus dem
Intent geladen, welcher vorher übergeben wurde. Dieser wird an die getGreetingsMessage Methode in
der ChatGpt Klasse übergeben. Die zurückgegebene Willkommensnachricht wird dann in die TextView
eingefügt.   
  
Nachdem ich die Funktionalität implementiert habe, habe ich das ganze getestet. Ich habe die App
gestartet und mich eingeloggt und anschließend eine Fehlermeldung bekommen. Der Grund lag in einem
falschen ChatGPT key. Da die Willkommensnachricht in der **onViewCreated** Funktion ausgeführt wird,
gab es keine Chance für den Benutzer, die Key zu ändern, bevor die App crashed. Deswegen eine 
Textausgabe mit einem vordefinierten Text eingebaut, welche angezeigt wird, falls der API Key falsch
ist. Zusätzlich habe ich noch eine Text anzeige implementiert, falls überhaupt kein API Key hinterlegt
ist. Diese sehen wir folgt aus:  
  
Kein Token:
> Hallo %s. Leider hast du noch keinen API Token gesetzt. Bitte setzen den API Token in Settings, um ChatGPT nutzen zu können.  

Falscher Token:
> Hallo %s. Leider hast du einen falschen API Token gesetzt. Bitte setzen den API Token in Settings, um ChatGPT nutzen zu können.  
  
Danach bekam der Benutzer eine Meldung, wenn er keinen oder den falschen API Key gesetzt hat. Falls
der Key richtg war, bekam er eine peronsalisierte Nachricht von ChatGPT. Damit war dieses Feature
fertig implementiert.

## Probleme/Lessons Learned

Diesen Abschnitt möchte ich noch über die Probleme und Lessons Leraned sprechen, die beim entwickeln
des Projekts entstanden sind.

### Probleme

Das erste Problem, was aufgetreten ist, war, dass die Google Dokumentation 
mir einen Zugriff auf den Intent angezeigt hat, der deprecated war. Laut Google hätte ich den
Login per **startActivityForResult** starten sollen und per **onActivityResult** das Ergebnis
bekommen sollen. Zum Glück gab es im Code schon eine Implementierung nach neuen Standards. 
So konnte ich mich daran orientieren und das Feature auch mit der aktuellen Methode entwickeln.  

Zusätzlich trat das Problem auf, dass die Anwedung sofort abstürtzte, wenn ich keinen oder den 
falschen API Key eingegeben hatte. Die Problem konnte ich elegant mit einer Standardnachricht beheben,
welche den Nutzer darauf hinweist, dass er keinen oder den falschen Key angegeben hat. 

### Lessons Learned

Bei der Entwicklung des Features habe ich viel gelernt. Das erste was mir positv im Gedächtnis geblieben
ist, ist, wie einfach die Konfiguration in der Google Cloud Admin Konsole war. Zum Anfang des Projekts 
dachte ich, dass ich sehr aufwendig werden würde, den Zugriff einzurichten oder dass es mir gar
von Google verboten wird, den Login für Testzwecke bereit zu stellen. Diese Befürchtungen haben sich
nicht bewahrheitet. Außerdem habe ich gelernt, dass die Entwicklung von Android Apps gar nicht so
schwierig ist wie anfangs gedacht. Hat man sich einmal in die UI Constraint eingefunden und verstanden,
wie Intents funktionieren, kann man mit dem Wissen schon so einige Funktionalitäten bereit stellen. 
Zuletzt habe ich auch negative Seiten der App Enticklung kennen gelernt. So finde ich es teilweise
verwirrend, dass Versionen von APIs unterstützt werden. Soll die App mit den meisten Smartphones
kompatibel sein, muss man stets veraltete API Versionen unterstützen. 

## Fazit

Ich habe es geschafft, das Projekt wie geplant in vorgegeben Zeit umzusetzen. Auch vom zeitlichen
Aufwand, habe ich es geschafft, das Projekt im mir selbst gesteckten Zeitrahmen umzusetzen. Das
Projekt konnte ich an einem Tag fertig stellen. 