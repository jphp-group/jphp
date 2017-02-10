package php.runtime.env;

abstract public class PackageLoader {
    abstract public Package load(String name, TraceInfo trace);
}
