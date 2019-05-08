package org.develnext.jphp.core.tester;

import static java.util.stream.Collectors.toMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.memory.StringMemory;

public class Test {
    private static final Set<String> sectionNames;
    private static final Pattern SECTION_NAME_PATTERN = Pattern.compile("--([A-Z]+)--");

    static {
        Set<String> names = new HashSet<>(30);
        names.add("TEST");
        names.add("DESCRIPTION");
        names.add("CREDITS");
        names.add("SKIPIF");
        names.add("REQUEST");
        names.add("POST");
        names.add("POST_RAW");
        names.add("PUT");
        names.add("GZIP_POST");
        names.add("DEFLATE_POST");
        names.add("GET");
        names.add("COOKIE");
        names.add("STDIN");
        names.add("INI");
        names.add("ARGS");
        names.add("ENV");
        names.add("FILE");
        names.add("FILEEOF");
        names.add("FILE_EXTERNAL");
        names.add("REDIRECT_TEST");
        names.add("HEADERS");
        names.add("CGI");
        names.add("XFAIL");
        names.add("EXPECT_HEADERS");
        names.add("EXPECT");
        names.add("EXPECTF");
        names.add("EXPECTREGEX");
        names.add("EXPECTEXTERNAL");
        names.add("EXPECTREGEX_EXTERNAL");
        names.add("CLEAN");

        sectionNames = Collections.unmodifiableSet(names);
    }

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

    public Test(File file) {
        _sections = new LinkedHashMap<>();
        _sectionLines = new HashMap<>();

        try (Scanner reader = new Scanner(new BufferedInputStream(new FileInputStream(file)), "UTF-8")) {
            String line;
            List<String> content = new ArrayList<>();
            String section = null;
            int i = 0;
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                if (line == null)
                    line = "";

                if (isSectionName(line)) {
                    if (section != null) {
                        _sections.put(section, StringUtils.join(content, '\n'));
                    }

                    section = line.substring(2, line.length() - 2);
                    _sectionLines.put(section, i);
                    content = new ArrayList<>();
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

    private static boolean isSectionName(String line) {
        Matcher matcher = SECTION_NAME_PATTERN.matcher(line);
        return matcher.matches() && sectionNames.contains(matcher.group(1));
    }

    public boolean run(PrintStream output) {
        return true;
    }

    public Map<String, String> getSections() {
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

    public String getIni() {
        return ini;
    }

    public Map<String, Memory> getIniEntries() {
        return Optional.ofNullable(ini)
                .filter(StringUtils::isNotBlank)
                .map(s -> s.split("\\r?\\n"))
                .map(Stream::of)
                .map(stringStream -> stringStream.map(s -> s.split("=", 2)))
                .map(stream -> stream.collect(toMap((String[] o) -> o[0], (String[] o) -> StringMemory.valueOf(o[1]), (k1, k2) -> k2)))
                .map(Collections::unmodifiableMap)
                .orElseGet(Collections::emptyMap);
    }

    public int getSectionLine(String section) {
        return _sectionLines.get(section);
    }
}
