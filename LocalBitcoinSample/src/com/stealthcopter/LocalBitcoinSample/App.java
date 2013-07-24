package com.stealthcopter.LocalBitcoinSample;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.stealthcopter.localbitcoinslibrary.LocalBitcoinAction;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: mat
 * Date: 24/07/13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {

    // TODO: Replace with your own ClientID and ClientSecret
    static final String CLIENT_ID = "b7465c4ad54f81b371dd";
    static final String CLIENT_SECRET = "5446b879cccd66a94be8491bf82dfcff4ba00832";

    static final boolean TEST_MODE=true;
    private static final String PREFS_ACCESS_TOKEN = "PREFS_ACCESS_TOKEN";

    private LocalBitcoinAction localBitcoinAction;
    private static App instance;

    public App(){
        instance = this;
    }
    public static App get(){
        return (App)instance;
    }

    public LocalBitcoinAction getLocalBitcoinsAction(){
        if (localBitcoinAction==null){
            localBitcoinAction=new LocalBitcoinAction(CLIENT_ID, CLIENT_SECRET);
            if (TEST_MODE) localBitcoinAction.setTestMode(TEST_MODE);
        }
        return localBitcoinAction;
    }

    public AccessToken getAccessToken(){

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = mPrefs.getString(PREFS_ACCESS_TOKEN,null);
        if (json!=null){
            try {
                return new AccessToken(new JSONObject(json));
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    public void saveAccessToken(AccessToken accessToken){
        if (accessToken==null) return;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.edit().putString(PREFS_ACCESS_TOKEN, accessToken.toJSON().toString()).commit();
    }

    public void removeAccessToken() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.edit().remove(PREFS_ACCESS_TOKEN).commit();
    }

}
