package com.example.android.cputester;

import android.app.IntentService;
import android.content.Intent;

import com.example.android.cputester.MainActivity.ComputeReceiver;

import java.util.Calendar;

/**
 * Simple IntentService to perform long running
 * computation not on the UI thread.
 */
public class ComputeService extends IntentService {
    public static final String PI_ID = "pi_id";
    // Amount of computation time in seconds
    public static final int COMPUTATION_SECONDS = 5;

    public ComputeService() {
        super("ComputeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, COMPUTATION_SECONDS);

        double pi = 3.0;
        int modifier = 1;
        long denominator = 2;
        // Run up computation as much as possible for a minute by computing Pi using the Nilakantha
        // series.
        while (endTime.after(Calendar.getInstance())) {
            pi += modifier * (4.0 / (denominator * (denominator + 1) * (denominator + 2)));
            modifier *= -1;
            denominator += 2;
        }

        // Notify the receiver that the computation has finished.
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(PI_ID, String.format("%.12f", pi));
        broadcastIntent.setAction(ComputeReceiver.ACTION_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(broadcastIntent);

        // Stop the service when the request is complete.
        stopSelf();
    }
}
