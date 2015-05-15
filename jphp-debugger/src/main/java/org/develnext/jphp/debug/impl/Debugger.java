package org.develnext.jphp.debug.impl;

import org.develnext.jphp.debug.impl.breakpoint.Breakpoint;
import org.develnext.jphp.debug.impl.breakpoint.BreakpointManager;
import org.develnext.jphp.debug.impl.command.AbstractCommand;
import org.develnext.jphp.debug.impl.command.InitCommand;
import org.develnext.jphp.debug.impl.command.UnimplementedCommand;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Debugger {
    public enum Status { STARTING, STOPPING, STOPPED, BREAK }
    public enum Step { RUN, OVER, INTO, FORCE_INTO, OUT, DROP_FRAME,  }

    protected Status status = Status.STARTING;

    protected Socket socket;
    protected final Thread loopThread;

    protected boolean shutdown = false;
    protected boolean init = false;
    protected boolean working = true;
    protected Step waitStep = Step.INTO;

    protected static DocumentBuilderFactory xmlBuilderFactory = DocumentBuilderFactory.newInstance();
    protected static DocumentBuilder xmlBuilder;

    protected static TransformerFactory transformerFactory = TransformerFactory.newInstance();
    protected static Transformer transformer;

    protected String ideKey;
    protected String rootPath;

    private CommandArguments currentArguments = null;
    private AbstractCommand currentCommand = null;

    private DebugTick registeredTick = null;

    public final IdeFeatures ideFeatures;
    public final BreakpointManager breakpointManager;

    static {
        try {
            xmlBuilder = xmlBuilderFactory.newDocumentBuilder();
            transformer = transformerFactory.newTransformer();

            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            throw new DebuggerException(e);
        }
    }

    public Debugger(int port) throws IOException {
        this(port, "127.0.0.1");
    }

    public Debugger(int port, String hostname) throws IOException {
        this.breakpointManager = new BreakpointManager(this);
        this.ideFeatures = new IdeFeatures();

        log("waiting for IDE, " + hostname + ":" + port + " ...");

        while (true) {
            try {
                if (shutdown) {
                    log("the program shutdown.");
                    break;
                }

                this.socket = new Socket(hostname, port);

                log("IDE has been connected.");
                log("-----", false);

                break;
            } catch (ConnectException e) {
                // nop...
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    throw new DebuggerException(e1);
                }
            }
        }

        this.loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in   = socket.getInputStream();

                    StringBuilder sb = new StringBuilder();

                    while (true) {
                        if (socket.isClosed()) {
                            return;
                        }

                        if (!socket.isConnected()) {
                            return;
                        }

                        if (shutdown) {
                            return;
                        }

                        int ch = in.read();

                        if (ch > -1) {
                            if (ch == '\0') {
                                Debugger.this.onRequest(sb.toString());
                                sb = new StringBuilder();
                            } else {
                                sb.append((char)ch);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (socket.isClosed()) {
                        return;
                    }

                    throw new DebuggerException(e);
                }
            }
        });
    }

    public void log(String message) {
        log(message, true);
    }

    public void log(String message, boolean prefix) {
        System.out.println((prefix ? "~ Debugger: " : "") + message);
    }

    public Status getStatus() {
        return status;
    }

    public String getIdeKey() {
        return ideKey;
    }

    public void setIdeKey(String ideKey) {
        this.ideKey = ideKey;
    }

    public void setRootPath(String rootPath) {
        try {
            this.rootPath = new File(rootPath).getCanonicalPath().replace('\\', '/');
        } catch (IOException e) {
            throw new DebuggerException(e);
        }
    }

    public String getFileName(String fileName) {
        return "file://" + rootPath + "/" + fileName;
    }

    public boolean isWorking() {
        return working;
    }

    public Step getWaitStep() {
        return waitStep;
    }

    public void shutdown() {
        this.shutdown = true;
        this.working  = false;
    }

    public DebugTick runTicks() {
        return waitTick(Step.RUN);
    }

    public DebugTick waitTick(Step step) {
        working = false;
        waitStep = step;

        DebugTick oldTick = registeredTick;

        while (registeredTick == oldTick) {
            if (shutdown) {
                break;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new DebuggerException(e);
            }
        }

        return registeredTick;
    }

    public void registerBreak(Breakpoint breakpoint, Environment env, TraceInfo trace, ArrayMemory locals) {
        status = Status.BREAK;

        registeredTick = new DebugTick(breakpoint, env, trace, locals);

        this.working = true;
    }

    public void registerEnd(Environment env) {
        registeredTick = new DebugTick(null, env, null, null);

        this.working = true;
    }

    public DebugTick getRegisteredTick() {
        return registeredTick;
    }

    public void run() {
        InitCommand initCommand = new InitCommand("JPHP_APP", "file://" + rootPath);
        initCommand.setIdeKey(ideKey);

        write(null, initCommand);
        loopThread.start();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new DebuggerException(e);
        }
    }

    protected void onRequest(String input) {
        String[] args = StringUtils.split(input, " ", 200);

        if (args.length > 0) {
            String command = args[0].trim();

            String name = null;
            CommandArguments result = new CommandArguments();

            for (int i = 1; i < args.length; i++) {
                if (i % 2 == 0) {
                    if (name != null) {
                        String value = args[i];

                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 2);
                        }

                        result.put(name, value);
                    }
                } else {
                    name = args[i];

                    if (name.startsWith("-")) {
                        name = name.substring(1);
                    } else {
                        name = null;
                    }
                }
            }

            AbstractCommand cmd = commands.get(command);

            if (cmd == null) {
                cmd = new UnimplementedCommand(command);
            }

            currentArguments = result;
            currentCommand = cmd;

            write(result, cmd);

            if (cmd.afterExecutionContinueNeeded()) {
                runTicks();
            }
        }
    }

    protected void write(CommandArguments args, AbstractCommand command) {
        try {
            Document document = xmlBuilder.newDocument();
            command.run(this, args, document);

            StringWriter xmlWriter = new StringWriter();
            StreamResult xmlResult = new StreamResult(xmlWriter);

            transformer.transform(new DOMSource(document), xmlResult);

            byte[] xmlBytes = xmlWriter.toString().getBytes();

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            out.write(String.valueOf(xmlBytes.length).getBytes());
            out.write("\0".getBytes());
                out.write(xmlBytes);
            out.write("\0".getBytes());

            out.flush();
        } catch (IOException | TransformerException e) {
            throw new DebuggerException(e);
        }
    }

    protected final static Map<String, AbstractCommand> commands = new HashMap<>();

    public static void registerCommand(AbstractCommand command) {
        commands.put(command.getName(), command);
    }
}
