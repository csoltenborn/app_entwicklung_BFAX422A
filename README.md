# Projekt App Enticklung - Abgabe Fabian Stefer

In dieser ReadMe Datei soll die Dokumentation des Projektes "Google Sign" erfolgen. Das Projekt setzt
auf das den bereits vorhandenen Code aus Projekt [ChatGPT](https://github.com/csoltenborn/app_entwicklung_BFAX422A)
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
Hintergrund verwendet Google für diesen Service "OAuth" und "OpenId Conntect". Mehr dazu im Folgenden.  

>Quelle: [Google Developers](https://developers.google.com/identity/gsi/web/guides/overview?hl=de)  

## OAuth2 und OpenId Connect

Bei Open Id Connect (kurz auch OIDC) handelt es sich um Authentifizierungsprotokoll, welches 
die Authentifizierung von Nutzern in einer Anwendung standartiseren und vereinfachen soll. 
Der Authentifizeriungsserver führt hierbei die Authentifizerung durch. Entwickler einer Anwendung 
können die Anmeldeablauf starten und durch weitere Spezifikationen, wie die Verschlüsselung von
Identitätsdaten, die Erkennung von Identitätsanbietern oder die Sitzungsabmeldung erweiter werden.
Die Verantwortung für das Festlegen, Speichern und Verwalten von Passwörten entfällt hierbei komplett
in der Anwendung und wird auf den Authentifizeriungsserver übertragen. Man kann in einer Anwendung
auch verschiedene Identitäsanbieter verwenden und es ist für jeden möglich, selbst zum Identitäsanbieter
zu werden. 
  
>Quelle: [OpenID](https://openid.net/developers/how-connect-works/)  

Während sich OIDC um die authentifizierung kümmert, beschäftigt sich OAuth2 mit der autorsierung.
Es verwendet Zugrifftokens, um Zugriff auf bestimmt Ressourcen, wie zum Beispiel APIs zu verwalten.
Hierzu werden oft JSON Web Token (JWTs) genutzt. In diesen kann man direkt verschiedene Rollen 
speichern, welche in der Anwendung in Berechtigungen übersetzt werden können.  
  
>Quelle: [Auth0](https://auth0.com/de/intro-to-iam/what-is-oauth-2)  

## Erweiterung der Anwendung

![Bildschirmfoto 2023-11-15 um 13.46.47.png](readme_images%2FBildschirmfoto%202023-11-15%20um%2013.46.47.png)

Die Erweiterung des Projektes soll nun auch den "Anmelden über Google" Button das ChatGPt Projekt 
bringen. Damit sich die App mit Google Identity verbinden kann 

