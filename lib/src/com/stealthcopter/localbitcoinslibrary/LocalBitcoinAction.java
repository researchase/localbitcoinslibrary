package com.stealthcopter.localbitcoinslibrary;

import android.util.Log;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;
import com.stealthcopter.localbitcoinslibrary.Objects.Escrow;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Matthew Rollings
 * Date: 18/07/13
 * Time: 12:47
 */
public class LocalBitcoinAction {

    private static final String TAG="com.stealthcopter.localbitcoinslibrary";
    private static final String address_url="https://localbitcoins.com";


    private String clientID;
    private String clientSecret;

    public static final int MODE_READ_WRITE=0;
    public static final int MODE_READ_ONLY=1;

    private AccessToken accessToken=null;

    private static final String JSON_ESCROW_COUNT = "escrow_count";
    private static final String JSON_ESCROW_LIST = "escrow_list";
    private boolean testMode=false;

    /*
     *   Constructor
     *
     *   @param clientID -
     *   @param clientSecret -
     */
    public LocalBitcoinAction(String clientID, String clientSecret){
        if (clientID==null || clientID.equals("")) throw new NullPointerException("clientID not set");
        if (clientSecret==null || clientSecret.equals("")) throw new NullPointerException("clientSecret not set");
        this.clientID=clientID;
        this.clientSecret=clientSecret;
    }

    /*
     *
     */
    public AccessToken connect(AccessToken accessToken) {
        this.accessToken=accessToken;
        return accessToken;
    }


    /*
     *
     */
    public AccessToken connect(String username, String password, int mode){
        if (clientID==null) throw new NullPointerException("clientID not set");
        if (clientSecret==null) throw new NullPointerException("clientSecret not set");

        // Set the mode text
        String modeText=mode==MODE_READ_ONLY?"read":"read+write";

        String parameters =
                "grant_type="+"password"
                +"&client_id="+clientID
                +"&client_secret="+clientSecret
                +"&username="+username
                +"&password="+password
                +"&scope="+modeText;

        try
        {
            String response = getStringResponseFromUrl(address_url+"/oauth2/access_token/", parameters);
            this.accessToken= new AccessToken(new JSONObject(response));

            Log.d(TAG, "Access token:"+response);

            return this.accessToken;

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
        } catch(IOException e) {
            Log.e(TAG, "IOException", e);
        }


        return null;

    }





    public ArrayList<Escrow> getEscrows(){
        if (accessToken==null) throw new NullPointerException("Access Token is null");

        try
        {
            String parameters ="access_token="+accessToken.access_token;
            Log.d(TAG, "params: "+parameters);
            String response = getStringResponseFromUrl("https://localbitcoins.com/api/escrows/", parameters);

            JSONObject json = new JSONObject(response);
            JSONObject innerJson = json.getJSONObject("data");
            int escrow_count = innerJson.getInt(JSON_ESCROW_COUNT);

            Log.d(TAG, "escrow_count "+escrow_count);


            JSONArray jsonArray = new JSONArray();
            if (testMode){
                jsonArray.put(getTestItem());
            }
            else{
                jsonArray=innerJson.getJSONArray("escrow_list");
            }

            Log.d(TAG, "Array example now:  "+jsonArray);

            ArrayList<Escrow> escrows = new ArrayList<Escrow>();
            for (int i=0; i<jsonArray.length(); i++){
                escrows.add(new Escrow(jsonArray.getJSONObject(i)));
            }

            return escrows;

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
        } catch(IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return null;
    }

    public void setTestMode(boolean testMode) {
        this.testMode=testMode;
    }


    /**
     * Interface to callback when an escrow release event errors or is successful
     *
     */
    public interface ReleaseEscrowCallBack {
        public void onError(String error);
        public void onSuccess();
    }


    /**
     * Releases the escrow passed
     *
     * @param escrow - the escrow to be released
     *
     * @param callBackListener - the ReleaseEscrowCallBack to be notified on error or success
     *
     */
    public void releaseEscrowItem(Escrow escrow, ReleaseEscrowCallBack callBackListener){

        try
        {
            String parameters ="access_token="+accessToken.access_token;
            String response = getStringResponseFromUrl(address_url+escrow.release_url, parameters);

            JSONObject json = new JSONObject(response);

            Log.d(TAG, "escrow release responce "+json);

            // TODO: Check responce
            callBackListener.onSuccess();

        } catch (JSONException e) {
            callBackListener.onError("JSONException");
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
            callBackListener.onError("Error #103: MalformedURLException");
        } catch (ProtocolException e) {
            callBackListener.onError("Error #104: ProtocolException");
            Log.e(TAG, "ProtocolException", e);
        } catch(IOException e) {
            callBackListener.onError("Error #105: IOException");
            Log.e(TAG, "IOException", e);
        }

    }



    /*
     *  As there are no test accounts to play with we fake some
     *  test escrows to play with:
     *
     *  @return JSONObject with some test data
     */
    private JSONObject getTestItem(){
        String test="{\n" +
                "  \"data\": {\n" +
                "    \"created_at\": \"2013-06-20T15:23:01.61\",\n" +
                "    \"buyer_username\": \"TEST_ESCROW\",\n" +
                "    \"reference_code\": \"123\",\n" +
                "    \"currency\": \"EUR\",\n" +
                "    \"amount\": \"105.55\",\n" +
                "    \"amount_btc\": \"1.5\",\n" +
                "    \"exchange_rate_updated_at\": \"2013-06-20T15:23:01.61\"\n" +
                "  },\n" +
                "  \"actions\": {\n" +
                "    \"release_url\": \"/api/escrow_release/1/\"\n" +
                "  }\n" +
                "}";
        try {
            return new JSONObject(test);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException",e);
        }
        return null;
    }



    /*
     *  Static function to load a url with set parameters
     *  and return the string response
     *
     *  @param address - the url
     *
     *  @param parameters - string parameters
     *
     *  @return
     */

    private static String getStringResponseFromUrl(String address, String parameters) throws IOException {

        String response = null;
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = new URL(address);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");

        request = new OutputStreamWriter(connection.getOutputStream());
        request.write(parameters);
        request.flush();
        request.close();

        Log.d(TAG, "URL: "+url);
        Log.d(TAG, "Params "+parameters);

        String line = "";
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        // Response from server after login process will be stored in response variable.
        response = sb.toString();
        // You can perform UI operations here
        Log.d(TAG, "Response " + response);
        isr.close();
        reader.close();
        return response;
    }


}
