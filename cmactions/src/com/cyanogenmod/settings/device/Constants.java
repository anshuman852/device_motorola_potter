/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.cyanogenmod.internal.util.FileUtils;

public class Constants {

    private static final String TAG = "CMActions";

    // Swap keys
    public static final String FP_HOME_KEY = "fp_home";

    // Swap nodes
    public static final String FP_HOME_NODE = "/sys/homebutton/enable";

    // List of keys
    public static final String FP_KEYS = "fp_keys";
    public static final String FP_KEY_HOLD = "fp_key_hold";
    public static final String FP_KEY_LEFT = "fp_key_left";
    public static final String FP_KEY_RIGHT = "fp_key_right";

    // Keys nodes
    public static final String FP_KEYS_NODE = "/sys/homebutton/key";
    public static final String FP_KEY_HOLD_NODE = "/sys/homebutton/key_hold";
    public static final String FP_KEY_LEFT_NODE = "/sys/homebutton/key_left";
    public static final String FP_KEY_RIGHT_NODE = "/sys/homebutton/key_right";

    // Holds <preference_key> -> <proc_node> mapping
    public static final Map<String, String> sBooleanNodePreferenceMap = new HashMap<>();

    // Holds <preference_key> -> <default_values> mapping
    public static final Map<String, Object> sNodeDefaultMap = new HashMap<>();

    public static final String[] sButtonPrefKeys = {
        FP_HOME_KEY,
        FP_KEYS,
        FP_KEY_HOLD,
        FP_KEY_RIGHT,
        FP_KEY_LEFT,
    };

    static {
        sBooleanNodePreferenceMap.put(FP_HOME_KEY, FP_HOME_NODE);
        sBooleanNodePreferenceMap.put(FP_KEYS, FP_KEYS_NODE);
        sBooleanNodePreferenceMap.put(FP_KEY_HOLD, FP_KEY_HOLD_NODE);
        sBooleanNodePreferenceMap.put(FP_KEY_LEFT, FP_KEY_LEFT_NODE);
        sBooleanNodePreferenceMap.put(FP_KEY_RIGHT, FP_KEY_RIGHT_NODE);
        sNodeDefaultMap.put(FP_HOME_KEY, false);
        sNodeDefaultMap.put(FP_KEYS, "0");
        sNodeDefaultMap.put(FP_KEY_HOLD, "0");
        sNodeDefaultMap.put(FP_KEY_LEFT, "0");
        sNodeDefaultMap.put(FP_KEY_RIGHT, "0");
    }

    public static boolean isPreferenceEnabled(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, (Boolean) sNodeDefaultMap.get(key));
    }

    public static String GetPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, (String) sNodeDefaultMap.get(key));
    }

    public static void writePreference(Context context, String pref) {

        String value = "1";
        Log.e(TAG, "Write Pref: " + pref);
        if (!pref.equals(FP_KEYS) && !pref.equals(FP_KEY_HOLD) && !pref.equals(FP_KEY_LEFT) && !pref.equals(FP_KEY_RIGHT))
            value = isPreferenceEnabled(context, pref) ? "1" : "0";
        else
            value = GetPreference(context, pref);

        String node = sBooleanNodePreferenceMap.get(pref);
            Log.e(TAG, "Write " + value + " to node " + node);

        if (!FileUtils.writeLine(node, value)) {
            Log.w(TAG, "Write " + value + " to node " + node +
                "failed while restoring saved preference values");
        }
    }
}
