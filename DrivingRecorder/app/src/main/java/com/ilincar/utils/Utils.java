package com.ilincar.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zoufeng on 3/14/16.
 */
public class Utils {

    public static final String LCM_SWITCH_PATH = "/sys/class/misc/car_gpio_ctrl/lcm_switch";
//1 35  0 655  car_gpio_ctrl
    public static void lcmSwitchTo35(){
        File f = new File(LCM_SWITCH_PATH);
        try {
            FileOutputStream os = new FileOutputStream(f);
            os.write('1');
            os.flush();
            os.close();
            os = null;
        } catch (Exception e) {
        //    Log.d("zoufeng","lcmSwitchTo35 Exception e = " + e.toString());
            e.printStackTrace();
        }
    }

    public static void lcmSwitchTo655(){
        File f = new File(LCM_SWITCH_PATH);
        try {
            FileOutputStream os = new FileOutputStream(f);
            os.write('0');
            os.flush();
            os.close();
            os = null;
        } catch (Exception e) {
        //    Log.d("zoufeng","lcmSwitchTo655 Exception e = " + e.toString());
            e.printStackTrace();
        }
    }
}
