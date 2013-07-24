package com.stealthcopter.localbitcoinslibrary.Objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: mat
 * Date: 11/07/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class AccessToken {
    public String access_token;
    public String refresh_token;
    public String expires_in;

    // JSON Keys
    private static final String JSON_ACCESS_TOKEN="access_token";
    private static final String JSON_EXPIRES_IN="expires_in";
    private static final String JSON_REFRESH_TOKEN = "refresh_token";
    ;
    public AccessToken(JSONObject json) throws JSONException{
            // All fields are required, so we will throw an exception if not found
//            JSONObject data = json.getJSONObject("data");
            access_token = json.getString(JSON_ACCESS_TOKEN);
            refresh_token = json.getString(JSON_REFRESH_TOKEN);
            expires_in = json.getString(JSON_EXPIRES_IN);
    }

}
