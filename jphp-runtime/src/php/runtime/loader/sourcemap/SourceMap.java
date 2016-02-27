package php.runtime.loader.sourcemap;

import php.runtime.memory.ArrayMemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SourceMap {
    public static class Item {
        public final int sourceLine;
        public int compiledLine;

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

    public void insertLines(int[][] inserts, int allCountLines) {
        List<Item> lines = new ArrayList<>();

        if (itemsByCompiled != null && !itemsByCompiled.isEmpty()) {
            boolean addedCompiled = false;

            for (int i = 0; i < allCountLines; i++) {
                Item item = itemsByCompiled.get(i + 1);
                if (item != null) {
                    addedCompiled = true;
                    lines.add(new Item(item.sourceLine, item.compiledLine));
                } else {
                    if (addedCompiled) {
                        lines.add(new Item(-1, -1));
                    } else {
                        lines.add(new Item(1, 1));
                    }
                }
            }
        } else {
            for (int i = 0; i < allCountLines; i++) {
                lines.add(new Item(i + 1, i + 1));
            }
        }

        for (int[] insert : inserts) {
            int line = insert[0];
            int count = insert[1];

            for (int i = 0; i < count; i++) {
                lines.add(line, new Item(-1, -1));
            }

            for (int i = line + count; i < lines.size(); i++) {
                Item item = lines.get(i);

                item.compiledLine += count;
            }
        }

        for (Item line : lines) {
            if (line.sourceLine > -1 && line.sourceLine != line.compiledLine) {
                addLine(line.sourceLine, line.compiledLine);
            }
        }
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
