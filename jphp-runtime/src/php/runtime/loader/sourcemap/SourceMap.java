package php.runtime.loader.sourcemap;

import java.util.LinkedHashMap;
import java.util.Map;

public class SourceMap {
    public static class Item {
        public final int sourceLine;
        public final int compiledLine;

        public Item(int sourceLine, int compiledLine) {
            this.sourceLine = sourceLine;
            this.compiledLine = compiledLine;
        }
    }

    protected final String moduleName;
    protected Map<Integer, Item> itemsByCompiled = new LinkedHashMap<>();
    protected Map<Integer, Item> itemsBySource = new LinkedHashMap<>();

    public SourceMap(String moduleName) {
        if (moduleName == null) {
            throw new NullPointerException("moduleName is null");
        }

        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void clear() {
        itemsByCompiled.clear();
        itemsBySource.clear();
    }

    public void addLine(int sourceLine, int compiledLine) {
        Item item = new Item(sourceLine, compiledLine);

        itemsByCompiled.put(compiledLine, item);
        itemsBySource.put(sourceLine, item);
    }

    public int getCompiledLine(int sourceLine) {
        Item item = itemsBySource.get(sourceLine);

        if (item == null) {
            return -1;
        }

        return item.compiledLine;
    }

    public int getSourceLine(int compiledLine) {
        Item item = itemsByCompiled.get(compiledLine);

        if (item == null) {
            return -1;
        }

        return item.sourceLine;
    }

    public Map<Integer, Item> getItemsByCompiled() {
        return itemsByCompiled;
    }

    public Map<Integer, Item> getItemsBySource() {
        return itemsBySource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceMap sourceMap = (SourceMap) o;

        return moduleName.equals(sourceMap.moduleName);

    }

    @Override
    public int hashCode() {
        return moduleName.hashCode();
    }
}
