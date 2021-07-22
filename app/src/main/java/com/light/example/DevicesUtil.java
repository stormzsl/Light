package com.light.example;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DevicesUtil {


    public static void printSupportAbis(){
        HashMap<String, String> map = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            map.put("supportedABIs", Build.SUPPORTED_ABIS == null ? "" : TextUtils.join(",", Build.SUPPORTED_ABIS));
        } else {
            map.put("supportedABIs", Build.CPU_ABI);
        }

        Log.e("DevicesUtil","Build.VERSION.SDK_INT="+Build.VERSION.SDK_INT);
         Iterator iterator =map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry= (Map.Entry) iterator.next();
            String key= (String) entry.getKey();
            String value= (String) entry.getValue();
            Log.e("DevicesUtil",String.format("key:%s,value:%s",key,value));
        }

    }
}
