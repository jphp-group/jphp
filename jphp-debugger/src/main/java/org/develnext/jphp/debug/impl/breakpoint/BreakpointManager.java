package org.develnext.jphp.debug.impl.breakpoint;

import org.develnext.jphp.debug.impl.Debugger;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BreakpointManager {
    protected final Debugger debugger;
    protected Map<String, Breakpoint> breakpoints;

    public BreakpointManager(Debugger debugger) {
        this.debugger = debugger;
        breakpoints = new LinkedHashMap<>();
    }

    public Breakpoint findFor(Environment env, TraceInfo trace) {
        String fileName = trace.getFileName();

        if (!fileName.startsWith("/") && !fileName.matches("^[A-Za-z](\\\\:)(.+?)")) {
            fileName = debugger.getFileName(fileName);
        }

        int line = trace.getStartLine();

        for (Breakpoint breakpoint : breakpoints.values()) {
            if (breakpoint.line == line + 1 && fileName != null && fileName.equalsIgnoreCase(breakpoint.fileName)) {
                return breakpoint;
            }
        }

        return null;
    }

    public Collection<Breakpoint> all() {
        return breakpoints.values();
    }

    public Breakpoint get(String id) {
        return breakpoints.get(id);
    }

    public Breakpoint remove(String id) {
        return breakpoints.remove(id);
    }

    public void set(Breakpoint breakpoint) {
        breakpoints.put(String.valueOf(breakpoint.getId()), breakpoint);
    }
}
