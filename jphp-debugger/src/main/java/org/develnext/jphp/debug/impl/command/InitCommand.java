package org.develnext.jphp.debug.impl.command;

import org.develnext.jphp.debug.impl.Debugger;
import org.develnext.jphp.debug.impl.command.support.CommandArguments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import php.runtime.Information;

public class InitCommand extends AbstractCommand {
    protected String appId;
    protected String ideKey = "JPHP_DEBUGGER";
    protected String language = "PHP";
    protected String protocolVersion = "1.0";
    protected String fileUri;

    public InitCommand(String appId, String fileUri) {
        this.appId = appId;
        this.fileUri = fileUri;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void run(Debugger context, CommandArguments args, Document document) {
        Element init = document.createElement("init");

        init.setAttribute("xmlns", "urn:debugger_protocol_v1");
        init.setAttribute("fileuri", this.fileUri);
        init.setAttribute("language", this.language);
        init.setAttribute("protocol_version", this.protocolVersion);
        init.setAttribute("appid", this.appId);
        init.setAttribute("idekey", this.ideKey);

        Element engine = document.createElement("engine");
        engine.setAttribute("version", Information.CORE_VERSION);
        engine.setTextContent("JPHP Debugger");
        init.appendChild(engine);

        Element author = document.createElement("author");
        author.setTextContent("JPHP Group");
        init.appendChild(author);

        Element url = document.createElement("url");
        url.setTextContent("http://j-php.net");
        init.appendChild(url);

        Element copyright = document.createElement("copyright");
        copyright.setTextContent("Copyright (c) 2015 by JPHP Group");
        init.appendChild(copyright);

        document.appendChild(init);
    }

    public String getAppId() {
        return appId;
    }

    public String getIdeKey() {
        return ideKey;
    }

    public void setIdeKey(String ideKey) {
        this.ideKey = ideKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
