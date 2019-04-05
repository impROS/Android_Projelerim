package impros.cale.yksgerisayim.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import impros.cale.yksgerisayim.Pojos.SettingsPojo;

public class UI {
    private static SettingsPojo settingsPojo;

    public static void log(String mesaj) {
        Log.d("x3k", mesaj);
    }

    public static void loge(Exception ex) {
        Log.e("x3k", "Hata : " + Log.getStackTraceString(ex));
        ex.printStackTrace();
    }

    public static void loge(String mesaj) {
        Log.e("x3k", mesaj);

    }

    public static SettingsPojo getSettings(Context act) {
        if (settingsPojo == null) {
            initSettings(act);

        }
        return settingsPojo;
    }

    public static SettingsPojo initSettings(Context cn) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cn);
        String settingsJson;
        settingsJson = preferences.getString("settings", null);
        if (settingsJson == null || settingsJson.isEmpty()) {
            UI.setSettings(new SettingsPojo());
        } else {
            UI.setSettings(new Gson().fromJson(settingsJson, SettingsPojo.class));
        }
        int girisSayisi = UI.getSettings(cn).getGirisSayisi();
        UI.getSettings(cn).setGirisSayisi(++girisSayisi);
        UI.log("Giris Sayisi : " + UI.getSettings(cn).getGirisSayisi());
        saveSettings(cn, settingsPojo);
        return settingsPojo;
    }

    public static void setSettings(SettingsPojo settings) {
        UI.settingsPojo = settings;
    }

    public static void saveSettings(Context cn, String settingsJson) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cn);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("settings", settingsJson);
        editor.commit();

        settingsPojo = new Gson().fromJson(settingsJson, SettingsPojo.class);
    }

    public static void saveSettings(Context cn, SettingsPojo settingsPojo) {
        String settingsJson = new Gson().toJson(settingsPojo);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cn);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("settings", settingsJson);
        editor.commit();

        UI.settingsPojo = new Gson().fromJson(settingsJson, SettingsPojo.class);
    }
}
