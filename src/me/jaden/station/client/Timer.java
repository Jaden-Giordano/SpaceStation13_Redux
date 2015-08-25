package me.jaden.station.client;

import java.util.ArrayList;

/**
 * Created by Jaden on 7/21/2015.
 */
public class Timer {

    public static ArrayList<Timer> timers = new ArrayList<Timer>();

    private static class Interval extends Timer {
        protected Interval(float secs, boolean run, Runnable callback) {
            super(secs, run, callback);
        }

        protected void update(double dt) {
            super.update(dt);
            if(complete()) {
                restart();
            }
        }
    }

    private static class Timeout extends Timer {
        protected Timeout(float secs, boolean run, Runnable callback) {
            super(secs, run, callback);
        }

        protected void update(double dt) {
            super.update(dt);
            if(complete()) {
                stop();
            }
        }
    }

    /**
     * Creates a timeout; runnable will be run once milliseconds are up. Returns the timer
     * @return timer
     */
    public static Timer newTimeout(float secs, boolean run, Runnable callback) {
        Timeout t = new Timeout(secs, run, callback);
        timers.add(t);
        return t;
    }

    /**
     * Creates a interval timeout; runnable will be run once milliseconds are up, then it will automatically restart. Returns the timer
     * @return interval
     */
    public static Timer newInterval(float secs, boolean run, Runnable callback) {
        Interval i = new Interval(secs, run, callback);
        timers.add(i);
        return i;
    }

    public static boolean clearTimer(Timer timer) {
        return timers.remove(timer);
    }

    public static void updateAllTimers(double delta) {
        for (Timer timer : timers) {
            timer.update(delta);
        }
    }

    private float length;
    protected double time;
    private boolean running;
    private Runnable callback;

    protected Timer(float secs, boolean run, Runnable callback) {
        this.length = secs;
        this.running = run;
        this.callback = callback;
        time = 0;
    }

    protected void update(double dt) {
        if(running) {
            time += dt;
            if(complete()) {
                callback.run();
            }
        }
    }

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void stop() {
        time = 0;
        pause();
    }

    public void restart() {
        stop();
        start();
    }

    public boolean complete() {
        return time >= length;
    }

}
