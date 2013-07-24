package com.stealthcopter.localbitcoinslibrary.Objects;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: mat
 * Date: 11/07/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class Escrow{
    // Data
    public String buyer_username;
    public String reference_code;
    public String created_at;
    public String currency;
    public String amount;
    public String amount_btc;
    public String exchange_rate_updated_at;

    //Actions
    public String release_url;

    public Escrow(JSONObject json) throws JSONException{
            // All fields are required, so we will throw an exception if not found
            JSONObject data = json.getJSONObject("data");

            buyer_username = data.getString("buyer_username");
            created_at = data.getString("created_at");
            reference_code = data.getString("reference_code");
            currency = data.getString("currency");
            amount = data.getString("amount");
            amount_btc = data.getString("amount_btc");
            exchange_rate_updated_at = data.getString("exchange_rate_updated_at");

            JSONObject actions = json.getJSONObject("actions");
            release_url = actions.getString("release_url");
    }

}
