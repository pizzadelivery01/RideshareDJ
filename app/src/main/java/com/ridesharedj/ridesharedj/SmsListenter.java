package com.ridesharedj.ridesharedj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListenter extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg ="";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent Received: " + intent.getAction());
        if (intent.getAction() == SMS_RECEIVED) {
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {
                    String format = dataBundle.getString("format");
                    message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    msg = message[i].getMessageBody();

                }
            }
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
           // Intent i = new Intent(context, MainActivity.class);
           // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // i.putExtra("smsText", msg);
           // context.startActivity(i);
        }
    }
}
