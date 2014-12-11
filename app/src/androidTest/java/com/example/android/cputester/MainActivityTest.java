package com.example.android.cputester;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivityTest extends ActivityInstrumentationTestCase2 {
    Activity mainActivity;
    TextView textView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        textView = (TextView) mainActivity.findViewById(R.id.textview_id);
    }

    // Test that the initial displayed text is correct.
    @SmallTest
    public void testInitialValues() {
        String text = textView.getText().toString();
        final Resources resources = mainActivity.getResources();
        assertEquals(resources.getString(R.string.compute_explanation_start) +
                " " + String.valueOf(ComputeService.COMPUTATION_SECONDS) + " " +
                resources.getString(R.string.compute_explanation_end), text);
    }

    // Test that the compute service is launched on startup.
    @SmallTest
    public void testServiceStart() {
        assertTrue(isMyServiceRunning(ComputeService.class));
    }

    // Helper function to check if a specific service is currently running.
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mainActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // Test that ComputeService is terminated after some amount of seconds(s).
    @SmallTest
    public void testServiceStop() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(ComputeService.COMPUTATION_SECONDS));
        } catch (InterruptedException e) {
            // Something weird happened.
            e.printStackTrace();
            fail();
        }
        assertFalse(isMyServiceRunning(ComputeService.class));
    }

    // Test that the TextView text is updated with the computation result after it is done.
    @SmallTest
    public void testUpdateText() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(ComputeService.COMPUTATION_SECONDS));
        } catch (InterruptedException e) {
            // Something weird happened.
            e.printStackTrace();
            fail();
        }

        final Resources resources = mainActivity.getResources();
        String text = textView.getText().toString();
        String prefix = resources.getString(R.string.computation_finished_message);
        assertTrue(text.matches(prefix + " 3\\..*[0-9]"));
    }
}