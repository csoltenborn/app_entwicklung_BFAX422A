# App-Feature

Projekt: App-Entwiklung
Erweiterung: Theme-Wchsel-Feature

Autor: Lena Kosmetschke
Datum: 22.11.2023



# Einleitung

Im Rahmen des Moduls  _App-Entwicklung_  wurde eine „ChatGPT“-Android-App entwickelt. Die App ermöglicht es Benutzern mit dem GPT-3 basierten Chatbot „ChatGPT“ zu kommunizieren und Antworten in Echtzeit zu erhalten. Um die App zu erweitern, wurde ein Theme-Wechsel-Feature eingebaut.

Die Erweiterung konzentriert sich auf die visuelle Anpassung der App. Damit wird die Benutzerfreundlichkeit der App erhöht.

# Anforderungen

Die Anforderung der Erweiterung ist es, Benutzern die Möglichkeit zu geben, das Erscheinungsbild nach eigenen Wünschen anzupassen. Die einzelnen Farben und Zusammenstellungen der Themes soll möglichst benutzerfreundlich sein. Dabei sollen zwei verschiedene Themes angeboten werden: ein dunkleres und eine helleres Theme. Das Theme soll in den Einstellungen geändert werden können. Nach der Auswahl eines Themes, soll dieses sofort und ohne Neustart der App zu sehen sein.

# Umsetzung

1. Der Benutzer ändert die Einstellung für den Wechsel des Themes.

2. onPreferenceChange wird aufgerufen.

3. **checkAndApplyTheme** wird ausgeführt, um das Theme entsprechend der Benutzereinstellungen zu ändern.

## Anpassung des Designs in den Einstellungen

Damit Benutzer die Themes verwenden können, müssen Sie die Möglichkeit haben im Rahmen der App das Theme zu wechseln. Die Einstellungen werden in einer `PreferenceScreen` definiert. Dort habe ich eine neue  `PreferenceCategory`  für das Feature erstellt und ein  `SwitchPreferenceCompat`. Titel und Beschreibung werden zunächst mit Variablen gesetzt.

    <PreferenceCategory android:layout_width="wrap_content" android:layout_height="wrap_content" android:title="@string/display">  

  <SwitchPreferenceCompat android:key="themechange" android:layout_width="wrap_content" android:layout_height="wrap_content" android:defaultValue="false" android:title="@string/darkmode" android:summary="@string/darkmodeInfo"/>  
</PreferenceCategory>

Anschließend wurden die Strings, das heißt  _display_,  _darkmode_  und  _darkmodeinfo_, die verwendet werden, in der `string.xml` entsprechend für die Sprachen Englisch und Deutsch definiert.

![img.png](ressources%2Fimg.png)

Für jedes Theme habe ich eine Primäre, Sekundäre und tertiäre Farbe definiert.

|Darkmode| ![img_2.png](ressources%2Fimg_1.png)  |
|--|--|
|Lightmode| ![img_1.png](ressources%2Fimg_2.png) |

        <style name="LightTheme" parent="Theme.AppCompat.Light.DarkActionBar">  <item name="android:windowBackground">@color/colorBackgroundLight</item>  
      <item name="colorPrimary">#A1B5D8</item>  
      <item name="colorSecondary">#E4F0D0</item>  
      <item name="colorTertiary">#C2D8B9</item>  
    </style>  
      
    <style name="DarkTheme" parent="Theme.AppCompat.Light.DarkActionBar">  <item name="android:windowBackground">@color/colorBackgroundDark</item>  
      <item name="colorPrimary">#8AB0AB</item>  
      <item name="colorSecondary">#C0CAAD</item>  
      <item name="colorTertiary">#3E505B</item>  
    </style>

## Anzeige und Bearbeitung der Einstellung

Danach habe ich die bereits vorhandene PrefsActivity erweitert, die für die Anzeige und Bearbeitung von Einstellungen verantwortlich ist. Die bereits geschrieben  `root_preferences.xml`  wird dann von der  `onCreatePreferences`-Methode verarbeitet. Initialisieren Sie den Schalter (colorSwitch) und legen Sie den `OnPreferenceChangeListener` fest. Wenn der Benutzer den Schalter drückt, wird die Methode `onPreferenceChange` aufgerufen. Diese Methode ermittelt den aktuellen Status des Schalters und ruft entsprechend `enableColorChange` oder `disableColorChange` auf. Darüber hinaus kommt es bei der Verwendung von Handlern zu einer Verzögerung von 1 Sekunde, bevor die Logik fortgesetzt wird. Während dieser Verzögerungszeit wird eine neue `MainActivity` gestartet, um die Änderungen anzuwenden.

Die Methoden `enableColorChange` und `disableColorChange` legen den Dunkelmodusstatus in gemeinsamen Einstellungen fest. Nach dem Ändern des Dunkelmodus wird die Methode `ThemeHelper.checkAndApplyTheme` aufgerufen, um das aktuelle Design anzuwenden.

Insgesamt steuert dieses Snippet das Aussehen und die Funktionalität des Dark Mode-Schalters in den App-Einstellungen und stellt sicher, dass die Änderungen effektiv umgesetzt werden.



     colorSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { @Override public boolean onPreferenceChange(Preference preference, Object newValue) { boolean isChecked = (boolean) newValue;  
      
      if (isChecked) {  
      enableColorChange(); } else {  
      disableColorChange(); }  
        
      Handler handler = new Handler(); handler.postDelayed(new Runnable() { @Override public void run() {  
      Intent intent = new Intent(getActivity(), MainActivity.class); intent.setFlags(Intent._FLAG_ACTIVITY_CLEAR_TASK_ | Intent._FLAG_ACTIVITY_CLEAR_TOP_); startActivity(intent); requireActivity().finish(); }  
      }, 1000); ThemeHelper._checkAndApplyTheme_(sharedPreferences, requireContext());  
      
      return true; } private void enableColorChange() {  
      SharedPreferences.Editor editor = sharedPreferences.edit(); editor.putBoolean("themechange", true).apply(); } private void disableColorChange() {  
      SharedPreferences.Editor editor = sharedPreferences.edit(); editor.putBoolean("themechange", false).apply(); }  
    });

## ThemeHelper-Klasse

Die  `ThemeHelper`-Klasse wurde geschaffen, um den Code für das Themenmanagement zu organisieren und wiederzuverwenden.

In der  `ThemeHelper`-Klasse wurde die Methode  `checkAndApplyTheme`  implementiert. Diese prüft die Benutzereinstellungen für das Theme und wendet es auf die Anwendung an.

Ein Schalter `(SwitchPreferenceCompat)` wurde in den App-Einstellungen `(XML:  root_preferences.xml)` integriert, um dem Benutzer das Wechseln zwischen dunklem und hellem Thema zu ermöglichen. Die Klasse wird in der Methode  `onPreferenceChange` der  `SettingsFragment` aufgerufen.

        public static void checkAndApplyTheme(SharedPreferences sharedPreferences, Context context) { boolean isDarkModeEnabled = sharedPreferences.getBoolean("themechange", false);  
      
      int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration._UI_MODE_NIGHT_MASK_;  
      boolean isNightMode = currentNightMode == Configuration._UI_MODE_NIGHT_YES_;  
      
      int currentTheme = isNightMode ? R.style._DarkTheme_ : R.style._LightTheme_;  
      
      if (isDarkModeEnabled && !isNightMode) {  
      ((Activity) context).setTheme(R.style._DarkTheme_); } else if (!isDarkModeEnabled && isNightMode) {  
      ((Activity) context).setTheme(R.style._LightTheme_); } int backgroundColor = isNightMode ? R.color._colorBackgroundDark_ : R.color._colorBackgroundLight_; ((Activity) context).getWindow().setBackgroundDrawableResource(backgroundColor);  
      
      int appliedTheme = isNightMode ? R.style._DarkTheme_ : R.style._LightTheme_;  
      
      if (appliedTheme != currentTheme) {  
      ((Activity) context).recreate(); }  
    }

## Resultat

|Darkmode| ![img_5.png](ressources%2Fimg_5.png) | ![img_6.png](ressources%2Fimg_6.png) |
|--|--|--|--|
| Lightmode | ![img_3.png](ressources%2Fimg_3.png) | ![img_4.png](ressources%2Fimg_4.png) |


## Problem/ Lessons Learned

###  Hintergrund
Aufgrund von überlappenden Ansichtselementen in den XML-Dateien, war die gesetzte Hintergrundfarbe sowohl für den Darkmode als auch den Lightmode nicht sichtbar. Wenn Elemente in der Hierarchie über anderen Elementen liegen, können sie den Hintergrund der darunterliegenden Elemente verdecken.
Deswegen muss man die Elemente richtig platzieren. Zusätzlich sollte man, wenn man Hintergrundbilder oder Farben in verschiedenen Ansichtselementen verwendet sicherstellen, dass die Hintergründe transparent sind.
###  Zeitverzögerung
Zunächst änderte sich das Theme direkt, nachdem die Einstellung geändert worden ist in der `PrefsActivity`. Das führte zu Flackern und war eine potenzielle Absturzquelle.
Die Einführung einer kurzen Zeitverzögerung (1 Sekunde) vor dem Neustart der Aktivität, insbesondere in der `PrefsActivity, verbesserte das, indem das Theme erst nach einer kurzen Verzögerung angewendet wurde.

## Fazit
Die Erweiterung konnte wie geplant umgesetzt werden und verbessert die App durch die Implementierung eines Theme-Wechsel-Features. Der zeitliche Aufwand entsprach weitgehend meinen Erwartungen. Die flexiblen Theme-Optionen bieten den Nutzern eine personalisierte Erfahrung und machen die App vielseitiger für unterschiedliche Vorlieben und Umgebungen. In Zukunft könnte die Erweiterung um Animationen oder erweiterte Anpassungsoptionen erweitert werden, um die Benutzererfahrung weiter zu verbessern. Insgesamt trägt die Implementierung zur Attraktivität und Individualisierbarkeit der App bei, während der "ChatGPT Key" als zentrales Element des Gesamtprojekts unberührt bleibt.
