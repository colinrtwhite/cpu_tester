package com.example.android.cputester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // Launch the ComputeService
        Intent computeIntent = new Intent(this, ComputeService.class);
        startService(computeIntent);

        // Instantiate the intent filter and the receiver
        IntentFilter filter = new IntentFilter(ComputeReceiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        ComputeReceiver receiver = new ComputeReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final float TEXT_SIZE = 40;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textview_id);
            textView.setTextSize(TEXT_SIZE);
            textView.setText(getResources().getString(R.string.compute_explanation_start) +
                    " " + String.valueOf(ComputeService.COMPUTATION_SECONDS) + " " +
                    getResources().getString(R.string.compute_explanation_end));
            return rootView;
        }
    }

    /**
     * Simple BroadcastReceiver that is notified when the background ComputeService
     * finishes and updates the TextView.
     */
    public class ComputeReceiver extends BroadcastReceiver {
        public static final String ACTION_RESPONSE =
                "com.example.android.intent.action.COMPUTATION_FINISHED";

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView textView = (TextView) findViewById(R.id.textview_id);
            textView.setText(getResources().getString(R.string.computation_finished_message) + " " +
                intent.getStringExtra(ComputeService.PI_ID));
        }
    }
}
