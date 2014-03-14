package org.develnext.jphp.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import java.util.List;

final public class Arguments {
    @Parameter
    public List<String> parameters = Lists.newArrayList();

    @Parameter(names = "-a", description = "Run interactively")
    public boolean runInteractively;

    @Parameter(names = "-c", description = "<path>|<file>   Look for php.ini file in this directory")
    public String phpIniPath;

    @Parameter(names = "-n", description = "No php.ini file will be used")
    public boolean noPhpIni;

    @Parameter(names = "-d", description = "foo[=bar]    Define INI entry foo with value 'bar'")
    public List<String> phpIniDirectives = Lists.newArrayList();

    @Parameter(names = "-e", description = "Generate extended information for debugger/profiler")
    public boolean extendedInformation;

    @Parameter(names = "-f", description = "<file>  Parse and execute <file>")
    public String file;

    @Parameter(names = "-F", description = "<file>  Parse and execute <file> for every input line", echoInput = true)
    public String inputFile;

    @Parameter(names = "-l", description = "PHP information")
    public boolean phpInformation;

    @Parameter(names = "-r", description = "<code>  Run PHP <code> without using script tags <?..?>")
    public String code;

    @Parameter(names = "-R", description = "<code>  Run PHP <code> for every input line", echoInput = true)
    public String inputCode;

    @Parameter(names = "-v", description = "Version number")
    public boolean showVersion;

    @Parameter(names = "-h", description = "Show help")
    public boolean showHelp;

    @Parameter(names = "-stat", description = "Show stat about execution")
    public boolean showStat;


    @Parameter(names = "--rf", description = "<name>      Show information about function <name>")
    public String showFunction;

    @Parameter(names = "--rc", description = "<name>      Show information about class <name>")
    public String showClass;

    @Parameter(names = "--re", description = "Show information about extension <name>")
    public String showExtension;
}
