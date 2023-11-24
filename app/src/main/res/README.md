# App-Feature

- **Autor:** Lena Kosmetschke
- **Datum:** 22. November 2023
- **Projekt:** Theme-Switcher
- **Erweiterung:** 1.0
- **Version:** 1.0

## Inhaltsverzeichnis

1. [Einleitung](#einleitung)
2. [Anforderungen](#anforderungen)
3. [Umsetzung](#umsetzung)
    1. [Anpassung des Designs in den Einstellungen](#anpassung-des-Designs-in-den-Einstellungen)
    2. [Anzeige und Bearbeitung der Einstellung](#anzeige-und-Bearbeitung-der-Einstellung)
        1. [PrefsActivity.java](#prefsActivity.java)
        2. [ThemeHelper.java](#themeHelper.java)
3. [Problem/ Lessons Learned](#problem/-Lessons-Learned)
4. [Fazit](#fazit)


## 1. Einleitung

Im Rahmen des Moduls App-Entwicklung wurde eine „ChatGPT“-Android-App entwickelt, die es Benutzern ermöglicht mit dem GPT-3 basierten Chatbot „ChatGPT“ zu kommunizieren und Antworten in Echtzeit zu erhalten. Um die App zu erweitern, habe ich eine Theme-Wechsel-Erweiterung eingebaut.
Die Erweiterung konzentriert sich auf die visuelle Anpassung der App. Damit wird die Benutzerfreundlichkeit der App erhöht.

## 2. Anforderungen

Die Erweiterung soll es Benutzern ermöglichen, das Erscheinungsbild nach eigenen Wünschen anzupassen. Zwei verschiedene Themes sollen zur Auswahl stehen: ein helles (lightmode) und ein dunkles (darkmode) Design. Die einzelnen Farben und Farbzusammenstellungen der Themes sollen möglichst benutzerfreundlich sein.
Das Theme soll in den Einstellungen geändert werden können. Nach Auswahl eines Themes soll dieses sofort und ohne Neustart in der App zu sehen sein.

## Umsetzung

Bei der Umsetzung betrachte ich drei verschiedene Schritte, welche durchlaufen müssen, damit das Feature funktioniert:

1.	Eine Möglichkeit als Benutzer Einstellungen zum Bildschirm in der App vorzunehmen.
2.	Eine Funktion, welche den Status der Settings liest, prüft und ausgibt.
3.	Eine Funktion, welche auf Basis der vorherigen Funktion das Theme entsprechend der Benutzereinstellungen ändert

### 1. Anpassung des Designs in den Einstellungen

Damit Benutzer die Themes verwenden können, müssen Sie die Möglichkeit haben im Rahmen der App das Theme einzustellen. Die Einstellungen werden in einer `PreferenceScreen` definiert. Dort habe ich eine neue `PreferenceCategory` für das Feature erstellt mit einem `SwitchPreferenceCompat`. Titel und Beschreibung werden zunächst mit Variablen gesetzt.

```
<PreferenceCategory
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:title="@string/display">

    <SwitchPreferenceCompat
        android:key="themechange"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:defaultValue="false"
        android:title="@string/darkmode"
        android:summary="@string/darkmodeInfo"/>
</PreferenceCategory>

```

Alles Strings die in der App verwendet werden, werden in den entsprechenden `strings.xml`-Dateien mit Werten definiert.

```
<resources>
    <string name="app_name" translatable="false">ChatGpt</string>
    <string name="ask">Fragen</string>
    <string name="settings">Einstellungen</string>
    <string name="display">Bildschirm</string>
    <string name="darkmode">Dunkelmodus</string>
    <string name="darkmodeInfo">Wechsel hier zwischen dunklem Modus und hellem Modus</string>
</resources>
```
![img.png](..%2F..%2F..%2F..%2Fressources%2Fimg.png)

Bei der Farbauswahl orientiere ich mich an zwei verschiedenen Farbschemata. Für jedes Thema definiere ich eine Primäre, Sekundäre und Tertiäre Farbe.


| Lightmode  | Darkmode |
| ---------- |:--------:|
| ![img_1.png](..%2F..%2F..%2F..%2Fressources%2Fimg_1.png)  | ![img_2.png](..%2F..%2F..%2F..%2Fressources%2Fimg_2.png)     |

Die entsprechenden Hex-Codes werden dann in die `Themes.xml` geschrieben.

```
<style name="LightTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:windowBackground">@color/colorBackgroundLight</item>
    <item name="colorPrimary">#A1B5D8</item>
    <item name="colorSecondary">#E4F0D0</item>
    <item name="colorTertiary">#C2D8B9</item>
</style>

<style name="DarkTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:windowBackground">@color/colorBackgroundDark</item>
    <item name="colorPrimary">#8AB0AB</item>
    <item name="colorSecondary">#C0CAAD</item>
    <item name="colorTertiary">#3E505B</item>
</style>

```

### Anzeige und Bearbeitung der Einstellung

#### 2. PrefsActivity.java

Danach habe ich die bereits vorhandene `PrefsActivity`-Klasse um meine Erweiterung erweitert, die für die Anzeige und Bearbeitung von Einstellungen verantwortlich ist.

- Die `SettingsFragment`-Klasse erbt nun von `PreferenceFragmentCompat`, welche ermöglicht, dass Einstellungen in Android-Apps sauber und strukturiert Implementiert werden.

```
public static class SettingsFragment extends PreferenceFragmentCompat {
setPreferencesFromResource(R.xml.root_preferences, rootKey);
}
```

- Man benötig außerdem Referenzen um zum einen auf die Einstellungen der Anwendung zugreifen zu können und zum anderen um den Zustand des Darkmode-Schalters zu repräsentieren.

```
sharedPreferences =    PreferenceManager.getDefaultSharedPreferences(requireContext());
colorSwitch = findPreference("themechange");
```

- Dann benötige ich eine `onCreatePreferences`-Methode die aufgerufen wird, wenn die Settings Ansicht erstellt wird. In dieser werden die Settings aus der `root_preferences.xml`Datei geladen.
```
colorSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { }
```

- `sharedPreferences` wird mit den Standard-Settings der Anwendung initialisiert. `colorSwitch` wird mit dem in der XML-Datei definierten Schalter für den Darkmode initialisiert.
```
public boolean onPreferenceChange(Preference preference, Object newValue) { }
```

-	Sobald der Zustand des Darkmode-Schalters sich ändert, wird die `onPreferenceChange`-Methode aufgerufen.
     Je nachdem ob der Schalter aktiviert (`isChecked = true`) ist oder nicht, wird eine Funktion aufgerufen, die dafür sorgt, dass der Zustand des Darkmode in des gemeinsamen Settings geändert wird.
```
boolean isChecked = (boolean) newValue;

if (isChecked) {
    enableColorChange();
} else {
    disableColorChange();
}

private void enableColorChange() {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean("themechange", true).apply();
}

private void disableColorChange() {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean("themechange", false).apply();
}
```

- Ein Handler sorgt nun dafür, eine Verzögerung von 1 Sekunge zu erzeugen, bevor die nächste Aktion ausgeführt wird.
  In der `run`-Methode des Handlers wird eine neue `MainActivity`gestartet und die aktuellen Aktivitäten im Stapel entfernt

```
Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        requireActivity().finish();
    }
}, 1000);

```

-	Jetzt wird die `checkAndApplyTheme`-Mehtode aus meiner `ThemeHelper`-Klasse aufgerufen, die das aktuelle Design basierend auf den Einstellungen ändert.
```
ThemeHelper.checkAndApplyTheme(sharedPreferences, requireContext());
```


#### 3. ThemeHelper.java

Nun benötigen wir noch eine `ThemeHelper`-Klasse, die eine Methode enthält, welche für die Überprüfung des aktuellen Darkmode-Zustands und die Anwendung des entsprechenenden Themes verantwortlich ist. Dafür schreibe ich eine neue Klasse, damit sowohl die `MainActivity` als auch die `PresActivity `die `checkAndApplyTheme`-Methode verwenden können.

-	Zunächst werden der Methode Parameter übergeben. Bei den Parametern handelt es sich um den `themechange` Schlüssel, welcher der Darkmode-Zustand speichert und dem `context`um auf ressourcen zuzugreifen und die Anwendung des Themes zu steuern.
```
public static void checkAndApplyTheme(SharedPreferences sharedPreferences, Context context) { }
```

- Danach überprüfen wir den Darkmode-Zustand sowohl von unser Anwendung, als auch des Geräts.
```
int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

```

- Nun legen wir das aktuelle Theme anhand der Darkmode-Zustands des Geräts fest. Wenn der Darkmode aktiviert ist, wird das Darkmode-Theme (`R.style.DarkTheme`) ausgwählt, ansonsten das Lightmode-Theme (`R.style.LightTheme`).
```
int currentTheme = isNightMode ? R.style.DarkTheme : R.style.LightTheme;
```


-	Schließlich wird das Theme, basiered darauf ob der Darkmode-Zustand aktiviert oder deaktiviert und das Gerät bereits im Darkmode-Zustand ist oder nicht, festgelegt.
     Außerdem wird die Hintergrundfarbe basierend auf dem Darkmode-zustand ausgewählt.

```
if (isDarkModeEnabled && !isNightMode) {
    ((Activity) context).setTheme(R.style.DarkTheme);
} else if (!isDarkModeEnabled && isNightMode) {
    ((Activity) context).setTheme(R.style.LightTheme);
}

int backgroundColor = isNightMode ? R.color.colorBackgroundDark : R.color.colorBackgroundLight;
((Activity) context).getWindow().setBackgroundDrawableResource(backgroundColor);

```

- Sollte sich das angewendete Theme von dem aktuellen Theme unterscheiden, wird die Aktivität neu erstellt (`recreate()`).

- Damit die Änderungen in der Anwendungen sofort sichtbar sind benötigen wir noch eine zusätzliche Abfrage
```
int appliedTheme = isNightMode ? R.style.DarkTheme : R.style.LightTheme;

if (appliedTheme != currentTheme) {
    ((Activity) context).recreate();
}

```

Insgesamt stellt diese Methode sicher, dass das Darkmode-Theme basierend auf den Einstellungen angewendet wird und die Änderungen sofort sichtbar sind, indem die Aktivität neu erstellt wird.

## Resultat

1. Der Benutzer ändert die Einstellung für den Wechsel des Themes.
2. `onPreferenceChange` wird aufgerufen.
3. `checkAndApplyTheme` wird ausgeführt, um das Theme entsprechend der Benutzereinstellungen zu ändern.

| ![img_3.png](..%2F..%2F..%2F..%2Fressources%2Fimg_3.png)      | ![img_4.png](..%2F..%2F..%2F..%2Fressources%2Fimg_4.png) | ![img_5.png](..%2F..%2F..%2F..%2Fressources%2Fimg_5.png)  | ![img_6.png](..%2F..%2F..%2F..%2Fressources%2Fimg_6.png)  |


## Problem/ Lessons Learned

### Hintergrund

Aufgrund von überlappenden Ansichtselementen in den XML-Dateien, war die gesetzte Hintergrundfarbe sowohl für den Darkmode als auch den Lightmode nicht sichtbar. Wenn Elemente in der Hierarchie über anderen Elementen liegen, können sie den Hintergrund der darunterliegenden Elemente verdecken.
Deswegen muss man die Elemente richtig platzieren. Zusätzlich sollte man, wenn man Hintergrundbilder oder Farben in verschiedenen Ansichtselementen verwendet sicherstellen, dass die Hintergründe transparent sind.


### Zeitverzögerung

Zunächst änderte sich das Theme direkt, nachdem die Einstellung geändert worden ist in der `PrefsActivity`. Das führte zu Flackern und war eine potenzielle Absturzquelle.
Die Einführung einer kurzen Zeitverzögerung (1 Sekunde) vor dem Neustart der `Aktivität`, insbesondere in der `PrefsActivity`, verbesserte das, indem das Theme erst nach einer kurzen Verzögerung angewendet wurde.


## Fazit

Die Erweiterung konnte wie geplant umgesetzt werden und verbessert die App durch die Implementierung eines Theme-Wechsel-Features. Der zeitliche Aufwand entsprach weitgehend meinen Erwartungen. Die flexiblen Theme-Optionen bieten den Nutzern eine personalisierte Erfahrung und machen die App vielseitiger für unterschiedliche Vorlieben und Umgebungen. In Zukunft könnte die Erweiterung um Animationen oder erweiterte Anpassungsoptionen erweitert werden, um die Benutzererfahrung weiter zu verbessern. Insgesamt trägt die Implementierung zur Attraktivität und Individualisierbarkeit der App bei, während der "ChatGPT Key" als zentrales Element des Gesamtprojekts unberührt bleibt.
