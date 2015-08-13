package me.jaden.station;

/**
 * Created by Jaden on 7/15/2015.
 */
public class Logger {

    private String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public void log(String msg) {
        System.out.println(tag+msg);
    }
}
