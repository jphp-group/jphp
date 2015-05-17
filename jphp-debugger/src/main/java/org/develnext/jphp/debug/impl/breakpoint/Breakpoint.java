package org.develnext.jphp.debug.impl.breakpoint;

import org.w3c.dom.Element;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Breakpoint {

    public enum Type { LINE, CALL, RETURN, EXCEPTION, CONDITIONAL, WATCH, UNKNOWN }

    protected int id;
    protected Type type = Type.LINE;

    protected Integer line = null;
    protected String fileName = null;
    protected String function = null;
    protected String exception = null;

    protected boolean temporary = false;

    protected String expression = null;
    protected String state = "enabled";

    protected int hitCount = 0;

    protected static final AtomicInteger counter = new AtomicInteger();

    public Breakpoint() {
        id = counter.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Integer getLine() {
        return line;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFunction() {
        return function;
    }

    public String getException() {
        return exception;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public String getExpression() {
        return expression;
    }

    public String getState() {
        return state;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void hit() {
        hitCount += 1;
    }

    public void output(Element breakpointEl) {
        breakpointEl.setAttribute("id", String.valueOf(getId()));
        breakpointEl.setAttribute("type", getType().name().toLowerCase());
        breakpointEl.setAttribute("state", getState());

        if (getFileName() != null) {
            breakpointEl.setAttribute("filename", getFileName());
        }

        if (getLine() != null) {
            breakpointEl.setAttribute("lineno", String.valueOf(getLine()));
        }

        if (getFunction() != null) {
            breakpointEl.setAttribute("function", getFunction());
        }

        if (getException() != null) {
            breakpointEl.setAttribute("exception", getException());
        }

        breakpointEl.setAttribute("hit_count", String.valueOf(getHitCount()));
    }


    public static Breakpoint build(Map<String, String> args) {
        Breakpoint breakpoint = new Breakpoint();

        switch (args.get("t")) {
            case "line":
                breakpoint.type = Type.LINE;
                break;

            case "call":
                breakpoint.type = Type.CALL;
                break;

            case "return":
                breakpoint.type = Type.RETURN;
                break;

            case "watch":
                breakpoint.type = Type.WATCH;
                break;

            case "conditional":
                breakpoint.type = Type.CONDITIONAL;
                break;

            case "exception":
                breakpoint.type = Type.EXCEPTION;
                break;

            default:
                breakpoint.type = Type.UNKNOWN;
        }

        if (args.containsKey("s")) {
            breakpoint.state = args.get("s");
        }

        if (args.containsKey("f")) {
            breakpoint.fileName = args.get("f");
        }

        if (args.containsKey("n")) {
            breakpoint.line = Integer.parseInt(args.get("n"));
        }

        if (args.containsKey("r")) {
            breakpoint.temporary = Integer.parseInt(args.get("r")) == 1;
        }

        if (args.containsKey("x")) {
            breakpoint.exception = args.get("x");
        }

        if (args.containsKey("m")) {
            breakpoint.function = args.get("m");
        }

        if (args.containsKey("-")) {
            breakpoint.expression = args.get("-");
        }

        return breakpoint;
    }
}
