package com.stealthcopter.LocalBitcoinSample;

import android.app.Application;
import com.stealthcopter.localbitcoinslibrary.LocalBitcoinAction;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;

/**
 * Created with IntelliJ IDEA.
 * User: mat
 * Date: 24/07/13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {

    // TODO: Replace with your own ClientID and ClientSecret
    static final String CLIENT_ID = "";
    static final String CLIENT_SECRET = "";

    static final boolean TEST_MODE=true;

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

}
