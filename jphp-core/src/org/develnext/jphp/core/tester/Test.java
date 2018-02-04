package org.develnext.jphp.core.tester;

import php.runtime.common.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Test {
    protected String test;
    protected String description;
    protected String credits;
    protected String skipIf;
    protected String request;

    protected String post, put, post_raw, gzip_post, deflate_post, get;
    protected String cookie;
    protected String stdin;
    protected String ini;
    protected String args;
    protected String env;

    protected String file, fileEof, file_external, redirectTest;
    protected String headers;
    protected String cgi;

    protected String xfail;
    protected String expectHeaders;
    protected String expect, expectF, expectRegex, expectExternal, expectRegex_external;
    protected String clean;

    private Map<String, String> _sections;
    private Map<String, Integer> _sectionLines;

    public Test(File file){
        try {
            _sections = new LinkedHashMap<String, String>();
            _sectionLines = new HashMap<String, Integer>();

            Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
            String line;
            List<String> content = new ArrayList<String>();
            String section = null;
            int i = 0;
            while (reader.hasNextLine()){
                line = reader.nextLine();
                if (line == null)
                    line = "";

                if (line.startsWith("--") && line.endsWith("--") && line.length() > 2
                        && Character.isLetter(line.charAt(2))){
                    if (section != null)
                        _sections.put(section, StringUtils.join(content, '\n'));

                    section = line.substring(2, line.length() - 2);
                    _sectionLines.put(section, i);
                    content = new ArrayList<String>();
                } else if (section != null) {
                    content.add(line);
                }
                i++;
            }

            if (section != null)
                _sections.put(section, StringUtils.join(content, '\n'));


            this.test = _sections.get("TEST");
            this.description = _sections.get("DESCRIPTION");
            this.credits = _sections.get("CREDITS");
            this.skipIf = _sections.get("SKIPIF");
            this.request = _sections.get("REQUEST");

            this.post = _sections.get("POST");
            this.post_raw = _sections.get("POST_RAW");
            this.put = _sections.get("PUT");
            this.gzip_post = _sections.get("GZIP_POST");
            this.deflate_post = _sections.get("DEFLATE_POST");
            this.get = _sections.get("GET");

            this.cookie = _sections.get("COOKIE");
            this.stdin = _sections.get("STDIN");
            this.ini = _sections.get("INI");
            this.args = _sections.get("ARGS");
            this.env = _sections.get("ENV");

            this.file = _sections.get("FILE");
            this.fileEof = _sections.get("FILEEOF");
            this.file_external = _sections.get("FILE_EXTERNAL");
            this.redirectTest = _sections.get("REDIRECT_TEST");

            this.headers = _sections.get("HEADERS");
            this.cgi = _sections.get("CGI");
            this.xfail = _sections.get("XFAIL");

            this.expectHeaders = _sections.get("EXPECT_HEADERS");
            this.expect = _sections.get("EXPECT");
            this.expectF = _sections.get("EXPECTF");
            this.expectRegex = _sections.get("EXPECTREGEX");
            this.expectExternal = _sections.get("EXPECTEXTERNAL");
            this.expectRegex_external = _sections.get("EXPECTREGEX_EXTERNAL");

            this.clean = _sections.get("CLEAN");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean run(PrintStream output){
        return true;
    }

    public Map<String, String> getSections(){
        return _sections;
    }

    public String getExpect() {
        return expect;
    }

    public String getExpectF() {
        return expectF;
    }

    public String getTest() {
        return test;
    }

    public String getFile() {
        return file;
    }

    public String getCookie() {
        return cookie;
    }

    public int getSectionLine(String section){
        return _sectionLines.get(section);
    }
}
