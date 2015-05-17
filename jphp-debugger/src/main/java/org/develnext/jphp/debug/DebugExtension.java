package org.develnext.jphp.debug;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.DebuggerException;
import org.develnext.jphp.debug.impl.command.*;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.env.handler.ProgramShutdownHandler;
import php.runtime.ext.support.Extension;

import java.io.IOException;

public class DebugExtension extends Extension {
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(final CompileScope scope) {
        if (scope.isDebugMode()) {
            String debugIdeKey = "JPHP_DEBUGGER";

            if (scope.configuration.containsKey("debug.ideKey")) {
                debugIdeKey = scope.configuration.get("debug.ideKey").toString();
            }

            String debugRootPath = "./src/";

            if (scope.configuration.containsKey("debug.rootPath")) {
                debugRootPath = scope.configuration.get("debug.rootPath").toString();
            }

            int debugPort = 9000;
            if (scope.configuration.containsKey("debug.port")) {
                debugPort = scope.configuration.get("debug.port").toInteger();
            }

            String debugHost = "127.0.0.1";
            if (scope.configuration.containsKey("debug.host")) {
                debugHost = scope.configuration.get("debug.host").toString();
            }

            final String finalDebugIdeKey = debugIdeKey;
            final String finalDebugRootPath = debugRootPath;
            final int finalDebugPort = debugPort;

            final DebugTickHandler tickHandler = new DebugTickHandler();
            scope.setTickHandler(tickHandler);

            final String finalDebugHost = debugHost;
            Thread debuggerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Debugger debugger = new Debugger(finalDebugPort, finalDebugHost);

                        scope.registerProgramShutdownHandler(new ProgramShutdownHandler() {
                            @Override
                            public void onShutdown(CompileScope scope, Environment env) {
                                debugger.shutdown();
                            }
                        });

                        if (debugger.isWorking()) {
                            debugger.setIdeKey(finalDebugIdeKey);
                            debugger.setRootPath(finalDebugRootPath);

                            debugger.run();
                        }

                        tickHandler.setDebugger(debugger);
                    } catch (IOException e) {
                        throw new DebuggerException(e);
                    }
                }
            });
            debuggerThread.setName("jphpDebuggerThread");
            debuggerThread.start();
        }
    }

    static {
        Debugger.registerCommand(new StatusCommand());
        Debugger.registerCommand(new FeatureSetCommand());

        Debugger.registerCommand(new RunCommand());
        Debugger.registerCommand(new ContextNamesCommand());
        Debugger.registerCommand(new ContextGetCommand());
        Debugger.registerCommand(new PropertySetCommand());
        Debugger.registerCommand(new PropertyGetCommand());
        Debugger.registerCommand(new PropertyValueCommand());

        Debugger.registerCommand(new StepIntoCommand());
        Debugger.registerCommand(new StepOverCommand());
        Debugger.registerCommand(new StepOutCommand());
        Debugger.registerCommand(new StackGetCommand());

        Debugger.registerCommand(new BreakpointSetCommand());
        Debugger.registerCommand(new BreakpointGetCommand());
        Debugger.registerCommand(new BreakpointRemoveCommand());
        Debugger.registerCommand(new BreakpointListCommand());

        Debugger.registerCommand(new EvalCommand());
    }
}
