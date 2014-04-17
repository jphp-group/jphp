package org.develnext.jphp.gendocs;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import php.runtime.env.Context;
import php.runtime.env.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocGenerator {

    protected List<File> files = new ArrayList<File>();
    protected Environment environment;

    public DocGenerator(File... directories) {
        environment = new Environment(System.out);

        if (directories != null)
            for(File dir : directories) {
                addDirectory(dir, true);
            }
    }

    public void addDirectory(File directory, boolean recursive) {
        File[] list = directory.listFiles();
        if (list != null) {
            for(File file : list) {
                if (file.isDirectory()) {
                    if (recursive) {
                        addDirectory(file, recursive);
                    }
                } else if (file.isFile()) {
                    if (file.getName().endsWith(".php") || file.getPath().endsWith(".inc")) {
                        files.add(file);
                    }
                }
            }
        }
    }

    protected SyntaxAnalyzer parseFile(File file) {
        try {
            Tokenizer tokenizer = new Tokenizer(new Context(file));
            return new SyntaxAnalyzer(environment, tokenizer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            environment.catchUncaught(e);
            try {
                environment.doFinal();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
            return null;
        }
    }

    public void generate(File targetDirectory) {
        if (!targetDirectory.exists())
            if (!targetDirectory.mkdirs())
                throw new IllegalStateException("Cannot create target directory");

        for(File file : files) {
            ApiDocument document = new ApiDocument(parseFile(file));
            document.generate(targetDirectory);
        }
    }

    public static void main(String[] args) throws IOException {
        File root = new File(".").getCanonicalFile();
        DocGenerator generator = new DocGenerator();

        for(File file : root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && new File(pathname, "sdk/").isDirectory();
            }
        })) {
            generator.addDirectory(new File(file, "sdk/"), true);
        }
        generator.generate(new File("./sdk"));
    }
}
