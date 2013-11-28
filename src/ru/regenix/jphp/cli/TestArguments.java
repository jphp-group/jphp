package ru.regenix.jphp.cli;

import com.beust.jcommander.Parameter;

import java.util.List;

final public class TestArguments {
    @Parameter
    public List<String> paths;

    @Parameter(names = {"-r", "--recursive"}, description = "to recursively traverse directories specified")
    public boolean recursive;

    @Parameter(names = {"-m", "--modified"})
    public boolean modified;

    @Parameter(names = {"-c", "--coverage"})
    public boolean coverage;
}
