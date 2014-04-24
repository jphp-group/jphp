package org.develnext.jphp.genapi;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.genapi.description.ClassDescription;
import org.develnext.jphp.genapi.template.BaseTemplate;
import php.runtime.common.collections.map.HashedMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ApiDocument {
    protected Collection<ClassStmtToken> classes;
    protected Collection<ConstStmtToken> constants;
    protected Collection<FunctionStmtToken> functions;
    protected final BaseTemplate template;

    public ApiDocument(SyntaxAnalyzer analyzer, BaseTemplate template) {
        classes = analyzer.getClasses();
        constants = analyzer.getConstants();
        functions = analyzer.getFunctions();
        this.template = template;
    }

    public Map<String, ClassDescription> getClasses() {
        Map<String, ClassDescription> list = new HashedMap<String, ClassDescription>();
        for(ClassStmtToken el : classes) {
            list.put(el.getFulledName().toLowerCase(), new ClassDescription(el));
        }

        return list;
    }

    public void generate(File targetDirectory, String language, Map<String, ClassDescription> classMap) {
        for(ClassStmtToken el : classes) {
            ClassDescription cls = classMap.get(el.getFulledName().toLowerCase());
            if (cls == null)
                throw new IllegalStateException("Cannot find class " + el.getFulledName() + " in classMap");

            System.out.println("[" +language+ "] gen class: " + cls.getName());

            String result = template.printClass(cls);
            File file = new File(targetDirectory, cls.getName().replace('\\', '/') + ".rst");
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IllegalStateException("Cannot create the '" + file.getParent() + "' directory");
                }
            }

            try {
                FileWriter fileWriter = new FileWriter(file, false);
                try {
                    fileWriter.write(result);
                } finally {
                    fileWriter.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        template.onEnd(targetDirectory);
    }
}
