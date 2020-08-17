package com.esprit.sim.SlimFit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.esprit.sim.SlimFit.step.StepService;


/**
 * Created by rami on 16/8/20.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("conf", context.MODE_PRIVATE);
        boolean temp=sharedPreferences.getBoolean("switch_on", false);
        if (!temp)
            return;
        Intent serviceIntent = new Intent(context, StepService.class);
        serviceIntent.putExtra("restart",intent.getAction());
        context.startService(serviceIntent);
    }
}
