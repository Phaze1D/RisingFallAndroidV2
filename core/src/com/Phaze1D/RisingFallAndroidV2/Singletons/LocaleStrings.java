package com.Phaze1D.RisingFallAndroidV2.Singletons;

import java.util.Hashtable;

/**
 * Created by davidvillarreal on 9/4/14.
 * Rising Fall Android Version
 */
public class LocaleStrings {
    private static LocaleStrings ourInstance;
    private Hashtable<String,String>localeString;

    private LocaleStrings(Hashtable<String, String> localeStrings) {
        this.localeString = localeStrings;
    }

    public static LocaleStrings getInstance(Hashtable<String, String> localeStrings) {
        if (ourInstance == null){
            ourInstance = new LocaleStrings(localeStrings);
        }

        return ourInstance;
    }

    public static LocaleStrings getOurInstance(){
        return ourInstance;
    }


    public Hashtable<String, String> getLocaleString() {
        return localeString;
    }

    public String getValue(String key){
       String value = localeString.get(key);
        if (value == null){
            return key;
        }else {
            return value;
        }
    }
}
