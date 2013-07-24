package com.stealthcopter.LocalBitcoinSample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.stealthcopter.localbitcoinslibrary.LocalBitcoinAction;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;
import com.stealthcopter.localbitcoinslibrary.Objects.Escrow;

import java.util.ArrayList;

public class EscrowActivity extends Activity {

    private ListView listView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escrow);
        listView = (ListView) findViewById(R.id.escrowListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEscrows();
    }

    private void loadEscrows(){
        // We load escrows in a background thread as it contains a network operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Escrow> escrows = App.get().getLocalBitcoinsAction().getEscrows();
                // Adapter needs to be set on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new EscrowAdapter(EscrowActivity.this, escrows));
                    }
                });
            }
        }).start();
    }


    public class EscrowAdapter extends BaseAdapter {

        ArrayList<Escrow> escrows;
        Context context;

        public EscrowAdapter(Context context, ArrayList<Escrow> escrows) {
            this.escrows=escrows;
            this.context=context;
        }

        @Override
        public int getCount() {
            return escrows.size();
        }

        @Override
        public Object getItem(int position) {
            return escrows.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        // create a new ButtonView for each item referenced by the Adapter
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Escrow escrow = escrows.get(position);

            if (convertView==null){
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_escrow, null);
            }

            ((TextView)convertView.findViewById(R.id.escrowReference)).setText("Ref: "+escrow.reference_code);
            ((TextView)convertView.findViewById(R.id.escrowBuyerName)).setText("Username: "+escrow.buyer_username);
            ((TextView)convertView.findViewById(R.id.escrowCreationDate)).setText("Created: "+escrow.created_at);

            ((TextView)convertView.findViewById(R.id.escrowCurrencyAmount)).setText("Currency: "+escrow.currency+" "+escrow.amount);
            ((TextView)convertView.findViewById(R.id.escrowBTCAmount)).setText("BTC: "+escrow.amount_btc);
            ((TextView)convertView.findViewById(R.id.escrowExchangeDate)).setText("Exchange Date: "+escrow.exchange_rate_updated_at);

            final Button confirm = (Button)convertView.findViewById(R.id.escrowConfirm);
            final View itemSpinnerView = convertView.findViewById(R.id.itemSpinnerView);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirm.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            App.get().getLocalBitcoinsAction().releaseEscrowItem(escrow, new LocalBitcoinAction.ReleaseEscrowCallBack() {
                                @Override
                                public void onError(String error) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            itemSpinnerView.setVisibility(View.GONE);
                                            confirm.setVisibility(View.VISIBLE);
                                            Toast.makeText(EscrowActivity.this, "Error releasing escrow", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onSuccess() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            confirm.setVisibility(View.GONE);
                                            itemSpinnerView.setVisibility(View.GONE);
                                            // TODO: Show happy face or tick etc..
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }
            });

            return convertView;
        }
    }


}
