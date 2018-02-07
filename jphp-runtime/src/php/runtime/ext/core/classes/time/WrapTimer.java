package php.runtime.ext.core.classes.time;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Name("php\\time\\Timer")
public class WrapTimer extends BaseWrapper<TimerTask> {
    private static Timer timer = new Timer("php\\time\\Timer");

    public WrapTimer(Environment env, TimerTask wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapTimer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    protected void __construct() {
    }

    @Signature
    public void cancel() {
        getWrappedObject().cancel();
    }

    @Signature
    public void run() {
        getWrappedObject().run();
    }

    @Signature
    public long scheduledTime() {
        return getWrappedObject().scheduledExecutionTime();
    }

    @Signature
    public static long parsePeriod(String period) {
        if (period == null || period.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid period - '" + period + "', is empty");
        }

        period = period.trim();

        String[] split = period.split(" ");

        long value = 0;
        Set<Character> types = new HashSet<>();

        for (String one : split) {
            if (one.trim().isEmpty()) {
                continue;
            }

            one = one.trim();
            char t = one.charAt(one.length() - 1);
            int k = 1;

            switch (Character.toLowerCase(t)) {
                case 's': k = 1000; break;
                case 'm': k = 1000 * 60; break;
                case 'h': k = 1000 * 60 * 60; break;
                case 'd': k = 1000 * 60 * 60 * 24; break;
                default:
                    if (!Character.isDigit(t)) {
                        throw new IllegalArgumentException("Invalid period - '" + period + "', at '" + one + "'");
                    }
            }

            String num = one;

            if (Character.isDigit(t)) {

            } else {
                if (!types.add(t)) {
                    throw new IllegalArgumentException("Invalid period - '" + period + "', at '" + one + "' that duplicates '" + t + "'");
                }
                
                num = one.substring(0, one.length() - 1);
            }

            try {
                double v = Double.parseDouble(num);
                value += Math.round(v * k);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid period - " + period + ", at '" + one + "', " + e.getMessage());
            }
        }

        return value;
    }

    @Signature
    public static TimerTask after(Environment env, String period, Invoker invoker) {
        return setTimeout(env, invoker, parsePeriod(period));
    }

    @Signature
    public static TimerTask every(Environment env, String period, Invoker invoker) {
        return setInterval(env, invoker, parsePeriod(period));
    }

    @Signature
    public static TimerTask trigger(Environment env, Invoker trigger, Invoker invoker) {
        return trigger(env, trigger, invoker, "16");
    }

    @Signature
    public static TimerTask trigger(Environment env, Invoker trigger, Invoker invoker, String period) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (trigger.callAny(this).toBoolean()) {
                        this.cancel();
                        invoker.callAny(this);
                    }
                } catch (Exception e) {
                    env.catchUncaught(e);
                }
            }
        };

        long millis = parsePeriod(period);
        timer.schedule(task, millis, millis);

        return task;
    }

    @Signature
    public static TimerTask setTimeout(Environment env, final Invoker invoker, long millis) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    invoker.callAny(this);
                } catch (Exception e) {
                    env.catchUncaught(e);
                }
            }
        };

        timer.schedule(task, millis);
        return task;
    }

    @Signature
    public static TimerTask setInterval(Environment env, final Invoker invoker, long millis) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    invoker.callAny(this);
                } catch (Exception e) {
                    env.catchUncaught(e);
                }
            }
        };
        
        timer.schedule(task, millis, millis);
        return task;
    }

    @Signature
    public static void cancelAll() {
        timer.cancel();
        timer.purge();

        timer = new Timer("php\\time\\Timer");
    }

    public static void shutdownAll() {
        timer.cancel();
        timer.purge();
    }
}
