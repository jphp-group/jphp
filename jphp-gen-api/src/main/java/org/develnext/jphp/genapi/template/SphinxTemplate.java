package org.develnext.jphp.genapi.template;

import org.develnext.jphp.genapi.description.ArgumentDescription;
import org.develnext.jphp.genapi.description.ClassDescription;
import org.develnext.jphp.genapi.description.FunctionDescription;
import org.develnext.jphp.genapi.description.MethodDescription;
import php.runtime.common.StringUtils;

import java.io.*;
import java.util.*;

public class SphinxTemplate extends BaseTemplate {

    protected Map<String, List<ClassDescription>> classes = new LinkedHashMap<String, List<ClassDescription>>();

    @Override
    protected void print(ClassDescription description) {
        String[] sections = StringUtils.split(description.getName(), "\\");

        String namespace = sections.length == 1
                ? ""
                : StringUtils.join(Arrays.copyOf(sections, sections.length - 1), "\\");

        List<ClassDescription> list = classes.get(namespace);
        if (list == null) {
            classes.put(namespace, list = new ArrayList<ClassDescription>());
        }
        list.add(description);

        sb.append(description.getName().replaceAll("\\\\", "\\\\\\\\"));
        sb.append("\n")
                .append(StringUtils.repeat('-', description.getName().length()))
                .append("\n\n");

        sb.append(".. php:class:: ")
                .append(description.getShortName());

        String extend = description.getExtends();
        String[] implement = description.getImplements();

        sb.append("\n");
        if (extend != null || implement != null || description.isAbstract() || description.isFinal()) {
            sb.append("\n");
            if (description.isAbstract()) {
                sb.append("\t**abstract** class\n");
            }

            if (description.isFinal()) {
                sb.append("\t**final** class\n");
            }

            if (description.isInterface()) {
                sb.append("\t**interface**\n");
            }

            if (description.isTrait()) {
                sb.append("\t**trait**\n");
            }

            if (extend != null) {
                sb.append("\t**extends**: :doc:`")
                        .append(extend).append(" <api/").append(extend.replace('\\', '/')).append(">`\n");
            }

            if (implement != null) {
                if (description.isInterface()) {
                    sb.append("\t**extends**: ");
                } else {
                    sb.append("\t**implements**: ");
                }
                int i = 0;
                for(String e : implement) {
                    if (i != 0)
                        sb.append("`>, ");
                    sb.append(":doc:`").append(e).append(" <api/").append(e.replace('\\', '/'));
                    if (implement.length == 1)
                        sb.append("`>");

                    i++;
                }
                sb.append("\n");
            }
        }

        if (description.getDescription() != null) {
            sb.append("\n\t");
            sb.append(addTabToDescription(description.getDescription(), 1));
            sb.append("\n");
        }

        sb.append("\n");
    }

    @Override
    protected void print(MethodDescription description) {
        print((FunctionDescription)description);
    }

    protected String addTabToDescription(String description, int tabCount) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(description));

        String line;
        int i = 0;
        try {
            while ((line = reader.readLine()) != null){
                if (i != 0){
                    line = StringUtils.repeat('\t', tabCount) + line;
                    builder.append("\n");
                }
                builder.append(line);
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    @Override
    protected void print(FunctionDescription description) {
        if (description instanceof MethodDescription && ((MethodDescription) description).isStatic()) {
            sb.append("\t..php:staticmethod:: ");
        } else {
            sb.append("\t..php:method:: ");
        }

        sb.append(description.getName()).append("(");
        int i = 0;

        Collection<ArgumentDescription> args = description.getArguments();
        for (ArgumentDescription arg : args) {
            if (i != 0)
                sb.append(", ");

            sb.append("$").append(arg.getName());
            if (arg.getValue() != null) {
                sb.append(" = ").append(arg.getValue());
            }

            i++;
        }

        sb.append(")\n");

        if (description instanceof MethodDescription) {
            MethodDescription meth = (MethodDescription)description;
            boolean add = false;
            if (meth.isFinal()) {
                sb.append("\n\t\t**final**\n");
                add = true;
            }

            if (meth.isAbstract()) {
                sb.append("\n\t\t**abstract**\n");
                add = true;
            }

            if (meth.isPrivate()) {
                sb.append("\n\t\t**private**\n");
                add = true;
            }

            if (meth.isProtected()) {
                sb.append("\n\t\t**protected**\n");
                add = true;
            }

            if (add)
                sb.append("\n");
        }

        if (description.getDescription() != null && !description.getDescription().isEmpty()) {
            sb.append("\n\t\t")
                    .append(addTabToDescription(description.getDescription().trim(), 2))
                    .append("\n\n");
        }
    }

    @Override
    protected void print(ArgumentDescription description) {
        sb.append("\t\t:param ");

        if (description.getTypes() != null)
            sb.append(StringUtils.join(description.getTypes(), " | ")).append(" ");

        sb.append("$").append(description.getName()).append(": ");
        if (description.getDescription() != null) {
            sb.append(addTabToDescription(description.getDescription(), 2));
        }

        sb.append("\n");
    }

    @Override
    protected void onAfterMethod(MethodDescription desc) {
        onAfterFunction(desc);
    }

    @Override
    protected void onAfterFunction(FunctionDescription desc) {
        if (desc.getReturnTypes() != null
                || (desc.getReturnDescription() != null && !desc.getReturnDescription().isEmpty())) {
            sb.append("\t\t:returns: ");
            if (desc.getReturnTypes() != null) {
                sb.append(StringUtils.join(desc.getReturnTypes(), " | ")).append(" ");
            }

            if (desc.getReturnDescription() != null) {
                sb.append(addTabToDescription(desc.getReturnDescription().trim(), 2));
            }

            sb.append("\n");
        }

        sb.append("\n");
    }

    @Override
    public void onEnd(File targetDirectory) {
        onEnd(targetDirectory, null);
    }

    protected void onEnd(File targetDirectory, String namespace) {
        StringBuilder sb = new StringBuilder();

        sb.append(namespace == null ? "API" : namespace)
                .append("\n")
                .append(StringUtils.repeat('-', 30)).append("\n\n");

        sb.append(".. toctree::\n" +
                "   :maxdepth: 3\n\n");

        File[] files = targetDirectory.listFiles();
        if (files != null) {
            for(File dir : files) {
                if (dir.isFile() && dir.getName().endsWith(".rst")) {
                    if (!dir.getName().equals("index.rst"))
                        sb.append("   ").append(dir.getName()).append("\n");
                } else if (dir.isDirectory()) {
                    sb.append("   ").append(dir.getName()).append("/index\n");
                    onEnd(dir, namespace == null ? dir.getName() : namespace + "\\\\" + dir.getName());
                }
            }
        }

        File file = new File(targetDirectory, "/index.rst");
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
