# Projekt App Entwicklung - Abgabe Fabian Stefer

In dieser ReadMe Datei geht es um die Dokumentation des Projektes "Google Sign". Das Projekt
setzt auf das den bereits vorhandenen Code aus dem Projekt [ChatGPT](https://github.com/csoltenborn/app_entwicklung_BFAX422A)
aus der Vorlesung "App Entwicklung" von Christian Soltenborn auf.

Die Umsetzung des Projektes befindet sich im Branch "projektabgabe_google_sign_in"

## Vorstellung der Erweiterung

Im Folgenden stelle ich vor, welche Erweiterung ich vorgenommen habe. Bei der Erweiterung handelt es
sich um eine Authentifizierung via Google und einer anschließenden Willkommensnachricht
von ChatGPT.

### Was ist "Über Google Anmelden"?

Mit der Authentifizierungsfunktion "Über Google Anmelden" können sich Nutzer bei einem Dienst
authentifizieren und autorisieren. Dies kann über verschiedene Plattformen hinweg erfolgen, zum
Beispiel in einer App oder im Web. Verwendet wird hierbei ein bereits vorhandener Google Account.
Auf dessen Basis kann der Betreiber des Dienstes auch ein eigenes Konto in seinem Dienst erstellen,
da der Authentifizierungsservice Daten des Users, wie zum Beispiel E-Mail und Name weitergibt.
Nachdem sich der User erstmalig angemeldet hat, ist es außerdem möglich den User beim Aufruf des
Dienstes ohne weitere Schritte einzuloggen. Im Hintergrund spricht der Service mit der
[Google Identity Services Authorization API](https://developers.google.com/identity/oauth2/web/guides/overview?hl=de)
. Um die API nutzen zu können, muss der Entwickler vorher die Anwendung über die Google Identity
Console konfigurieren. Mehr Informationen im Kaptiel [Einrichtung in der Google Cloud Console](#einrichtung-in-der-google-cloud-console)
. Im Hintergrund verwendet Google für diesen Service "OAuth" und "OpenId Conntect". Mehr dazu im
Folgenden.

> Quelle: [Google Developers](https://developers.google.com/identity/gsi/web/guides/overview?hl=de)

### OAuth2 und OpenId Connect

Bei Open Id Connect (kurz auch OIDC) handelt es sich um Authentifizierungsprotokoll, welches
die Authentifizierung von Nutzern in einer Anwendung standardisieren und vereinfachen soll.
Der Authentifizierungsserver führt hierbei die Authentifizierung durch. Entwickler einer Anwendung
können den Anmeldeablauf starten und durch weitere Spezifikationen, wie die Verschlüsselung von
Identitätsdaten, die Erkennung von Identitätsanbietern oder die Sitzungsabmeldung erweiter werden.
Die Verantwortung für das Festlegen, Speichern und Verwalten von Passwörtern entfällt hierbei
komplett in der Anwendung und wird auf den Authentifizierungsserver übertragen. Man kann in 
einer Anwendung auch verschiedene Identitätsanbieter verwenden und es ist für jeden möglich, selbst 
zum Identitätsanbieter zu werden.

> Quelle: [OpenID](https://openid.net/developers/how-connect-works/)

Während sich OIDC um die Authentifizierung kümmert, beschäftigt sich OAuth2 mit der Autorisierung.
Es verwendet Zugrifftokens, um Zugriff auf bestimmt Ressourcen, wie zum Beispiel APIs zu verwalten.
Hierzu werden oft JSON Web Token (JWTs) genutzt. In diesen kann man direkt verschiedene Rollen
speichern, welche in der Anwendung in Berechtigungen übersetzt werden können.

> Quelle: [Auth0](https://auth0.com/de/intro-to-iam/what-is-oauth-2)

### Inhalt und Nutzen der Anwendung

Die Erweiterung des Projektes soll nun auch den "Anmelden über Google" Button in das ChatGPT Projekt
bringen. Der Benutzer soll sich mit seinem Google Account in der App anmelden und anschließend soll
ihm eine personalisierte Willkommensnachricht ausgespielt werden.  
Durch die Anmeldung kann ein unerlaubtes Benutzen der App verhindert werden und gleichzeitig
kann man auf einer persönlichen Ebene mit dem Nutzer interagieren. Wir haben einen Überblick, 
wer unsere App benutzt und können perspektivisch sogar gewisse Inhalte mit einer Bezahloption 
bereitstellen oder den Benutzer per Mail über Änderungen an der App informieren.

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

1. Zuerst muss
   die [Google Cloud API Console](https://console.cloud.google.com/welcome?hl=de&project=peppy-linker-244912)
   im Browser aufgerufen werden und man muss sich einloggen. Hat man sich bereits in Chrome mit 
   seinem Google Account eingeloggt und Öffnet diesen, ist man meistens automatisch eingeloggt.

2. Hier erstellet man ein neues Google
   Cloud-Projekt [nach folgender Anleitung](https://cloud.google.com/resource-manager/docs/creating-managing-projects?hl=de)

3. Nachdem man das Projekt erstellt hat, erstellt man unter "APIs und Dienste", "Anmeldedaten",
   "Anmeldedaten erstellen" ein neue OAuth-Client-ID.

  <img alt="Bildschirmfoto 2023-11-15 um 14.56.26.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2014.56.26.png"/>

> Übersicht der Einstellungen, welche man in der Google Cloud Console anpassen muss

4. Als Anwendungstyp wählt man "Android" aus. Den Namen kann man frei wählen. Um nun den
   Paketnamen
   und den SHA-1 Zertifikatsabdruck herauszufinden schaut man in sein App Repository, welches
   man erweitern möchten.

   Den Paketnamen findet man in der build.gradle im Ordner "app" unter "namespace"

   Für den SHA-1 Fingerabdruck öffnet man den Reiter "Gradle" in Android Studio, in dem man "Excute
   Gradle Task" auswählt und folgenden Befehl ausführt:

   `gradle signingReport`

   Im signingReport finden man unter SHA1 unseren SHA-1 Zertifikatsabdruck.

5. Hat man alle Werte richtig eingetragen, klicken man auf "Erstellen". Damit ist man in der
   Cloud Console fertig.

### Erstellung der SignInActivity

Die Aufforderung zur Anmeldung soll erfolgen, wenn der Benutzer die App öffnet. Da es sich bei
der Anmeldung um einen geschlossenen Vorgang handelt, der danach nicht mehr relevant für weitere
Interaktionen des Benutzers mit dem System ist, habe ich mich dazu entschlossen, eine neue Activity
für diesen Vorgang zu erstellen. Dafür habe ich im Activity Wizard eine Empty View Activity
erzeugt.
Aufgrund von Übersichtlichkeit und Erweiterbarkeit habe ich außerdem ein neues Fragment erzeugt,
welches in der Main Activity angezeigt werden soll. Anschließend konnte ich mit dem Design der Ui 
beginnen. Glücklicherweise stell Google bereits einen Anmelden Button bereit, dafür musste ich nur 
folgende Dependencies per Gradlle installieren:

> implementation 'com.google.android.gms:play-services-auth:20.7.0'  
  
Danach konnte ich mit dem UI Design beginnen. Dafür musste ich den Button einfügen und per
Constraint Layout mittig im Fragment platzieren. Für den Fall, dass aus irgendeinem Grund die
Anmeldung bei Google fehlschlägt, habe ich noch eine Error Meldung eingebaut. Diese soll erst 
sichtbar werden, sobald die Google Anmeldung fehlgeschlagen ist. Das Ergebnis sah dann wie folgt 
aus:  
  
<img alt="Bildschirmfoto 2023-11-15 um 15.21.45.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2015.21.45.png"/>

Anschließend konnte ich mit der Implementierung der Logik anfangen. Der Großteil der Logik befindet
sich im SignInFragement. Bei der Implementierung habe ich mich hauptsächlich an der Anleitung orientiert, 
[welche Google hierfür bereitstellt](https://developers.google.com/identity/sign-in/android/start-integrating?hl=de)
. Laut der Anleitung stellt Google bereits eine Klasse **GoogleSignInClient** bereit, welche wir mit dem
**GoogleSignInOptions** konfigurieren können. Diese habe ich nun in der onCreate Methode erzeugt 
und konfiguriert. Damit Google den benutzer nun anmeldet, wenn man auf den Button klickt, musste ich
dem Button noch einen Listener hinzufügen. Der Listener startet einen Intent, in welchem sich
der User einloggen kann. Wenn der User fertig ist soll er in die MainActivity weitergeleitet
werden und dabei soll der Name des Nutzers direkt übergeben werden. Dafür habe ich einen
**ActivityResultLauncher** erstellt. Falls der User erfolgreich eingeloggt wird, erzeugt dieser
einen Intent mit der MainActivity und setzt den Benutzernamen. Falls der Login fehlschlägt, wird
der Error Text in der UI sichtbar. Zum Schluss musste ich in der **AndroidManifest.xml** noch 
einstellen, dass die SignInActivity beim Starten der App gestartet wird. Danach funktionierte der 
Login.

<img alt="Bildschirmfoto 2023-11-15 um 13.46.47.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2013.46.47.png"/>

> Der User konnte sich danach über den Button anmelden, um die MainActity zu gelangen.

### Erstellung einer Begrüßungsnachricht

Da wir nun über die Information verfügen, wie der Benutzer heißt, können wir ihm eine personalisierte
Begrüßungsnachricht per ChatGPT ausspielen. Da der vorherige Code diese Funktionalität noch nicht
bereitstellte, musste ich diesen erweitern. Angefangen habe ich in der Klasse **ChatGPT**. Dort
habe ich die Methode **getGreetingsMessage** erstellt. Diese erzeugt einen leeren Chat und fügt 
einen Befehl für ChatGPT hinzu, eine Begrüßungsnachricht zu erzeugen. Der Wortlaut lautet wie folgt:

> Bitte erstelle mir eine Wilkommensnachricht für %s. Begrüße die Person bitte im Chat.

"%s" wird später durch den Benutzername ersetzt. Anschließend habe ich im **MainFragment** noch 
den benötigten Code hinzugefügt, um die Willkommensnachricht anzuzeigen. Dafür war es notwendig, 
dass das ChatGpt Objekt in der Klasse verfügbar ist. Nachdem ich es in der Klasse erreichbar gemacht 
habe, habe ich die Methode **createExtraGreetingMessage** erstellt. Hier wird der Benutzername aus 
dem Intent geladen, welcher vorher übergeben wurde. Dieser wird an die getGreetingsMessage Methode
in der ChatGpt Klasse übergeben. Die zurückgegebene Willkommensnachricht wird dann in die TextView
eingefügt.   
  
Nachdem ich die Funktionalität implementiert habe, habe ich das Ganze getestet. Ich habe die App
gestartet und mich eingeloggt und anschließend eine Fehlermeldung bekommen. Der Grund lag in einem
falschen ChatGPT Key. Da die Willkommensnachricht in der **onViewCreated** Funktion ausgeführt wird,
gab es keine Chance für den Benutzer, den Key zu ändern, bevor die App crashed. Deswegen habe ich 
eine Textausgabe mit einem vordefinierten Text eingebaut, welche angezeigt wird, falls der API Key 
falsch ist. Zusätzlich habe ich noch eine Textanzeige implementiert, falls überhaupt kein API Key 
hinterlegt ist. Diese sehen wir folgt aus:  
  
Kein Token:
> Hallo %s. Leider hast du noch keinen API Token gesetzt. Bitte setzen den API Token in Settings, um ChatGPT nutzen zu können.  

Falscher Token:
> Hallo %s. Leider hast du einen falschen API Token gesetzt. Bitte setzen den API Token in Settings, um ChatGPT nutzen zu können.  
  
Danach bekam der Benutzer eine Meldung, wenn er keinen oder den falschen API Key gesetzt hat. Falls
der Key richtig war, bekam er eine personalisierte Nachricht von ChatGPT. Damit war dieses Feature
fertig implementiert.  

<img alt="Bildschirmfoto 2023-11-15 um 14.33.21.png" height="600" src="readme_images%2FBildschirmfoto%202023-11-15%20um%2014.33.21.png"/>

> Der User bekommt eine personalisierte Nachricht angezeigt

## Probleme/Lessons Learned

Diesen Abschnitt möchte ich noch über die Probleme und Lessons Learned sprechen, die beim entwickeln
des Projekts entstanden sind.

### Probleme

Das erste aufgetretene Problem war, dass die Google Dokumentation den Zugriff auf auf einen Intent 
mit einer Methode gestalten wollte, welche nicht mehr aktuell (deprecated) war. Laut der 
Dokumentation hätte ich den Login per **startActivityForResult** starten sollen und per 
**onActivityResult** das Ergebnis bekommen sollen. Glücklicherweise gab es im Code schon eine 
Implementierung nach neuen Standards. So konnte ich mich daran orientieren und das Feature auch 
uf einer aktuellen Basis entwickeln.  

Zusätzlich trat das Problem auf, dass die Anwendung sofort abstürzte, wenn ich keinen oder den 
falschen API Key eingegeben hatte. Das Problem konnte ich mit einer Standardnachricht beheben,
welche den Nutzer darauf hinweist, dass er keinen oder den falschen Key angegeben hat. 

### Lessons Learned

Bei der Entwicklung des Features habe ich viel gelernt. Positiv im Gedächtnis ist mir die
einfache Konfiguration in der Google Cloud Admin Konsole geblieben. Zum Anfang des Projekts ging 
ich davon aus, dass ich sehr aufwendig werden würde, den Zugriff einzurichten oder dass es mir 
gar von Google verboten wird, den Login für Testzwecke bereit zu stellen. Diese Befürchtungen haben 
sich nicht bewahrheitet. Außerdem habe ich gelernt, dass die Entwicklung von Android Apps nicht so 
schwierig ist wie anfangs gedacht. Hat man sich einmal in die UI Constraint eingefunden und 
verstanden, wie Intents funktionieren, kann man mit dem Wissen schon so einige Funktionalitäten 
bereitstellen. Zuletzt habe ich auch negative Seiten der App Enticklung kennen gelernt.
So fand ich es teilweise verwirrend, dass Versionen von APIs unterstützt werden müssen, falls die 
App mit den meisten Smartphones kompatibel sein soll. 

## Fazit

Gegen meine Erwartungen habe ich es geschafft das Projekt vollumfänglich umsetzen, da Google
durch mächtige Frameworks und ausführliche Dokumentation es den Entwicklern vereinfacht,
Features in kurzer Zeit im Code bereit zu stellen. Außerdem habe es geschafft, das Featue binnen 
einer Woche bereit zu stellen und gleichzeit eine vollumfängliche Dokumentation in dieser ReadMe 
anzulegen. Das Projekt wurde damit in der vorgebenen Zeit erfolgreich umgesetzt.