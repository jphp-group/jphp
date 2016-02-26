package php.runtime.loader.sourcemap;

import php.runtime.env.Context;

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
    protected Map<Integer, Item> itemsByLine = new LinkedHashMap<>();

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
        itemsByLine.clear();
    }

    public void addLine(int sourceLine, int compiledLine) {
        Item item = new Item(sourceLine, compiledLine);

        itemsByLine.put(compiledLine, item);
    }

    public int getSourceLine(int compiledLine) {
        Item item = itemsByLine.get(compiledLine);

        if (item == null) {
            return -1;
        }

        return item.sourceLine;
    }

    public Map<Integer, Item> getItemsByLine() {
        return itemsByLine;
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
