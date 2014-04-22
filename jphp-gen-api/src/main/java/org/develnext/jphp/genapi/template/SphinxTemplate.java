package org.develnext.jphp.genapi.template;

import org.develnext.jphp.genapi.description.ArgumentDescription;
import org.develnext.jphp.genapi.description.ClassDescription;
import org.develnext.jphp.genapi.description.FunctionDescription;
import org.develnext.jphp.genapi.description.MethodDescription;
import org.develnext.jphp.genapi.parameter.BaseParameter;
import php.runtime.common.StringUtils;

import java.io.*;
import java.util.*;

public class SphinxTemplate extends BaseTemplate {

    protected Map<String, List<ClassDescription>> classes = new LinkedHashMap<String, List<ClassDescription>>();

    public SphinxTemplate(String language, String languageName) {
        super(language, languageName);
    }

    protected void echoType(String type) {
        sb.append(type.replaceAll("\\\\", "\\\\\\\\"));
    }

    protected void echoTypes(String... types) {
        int i = 0;
        if (types != null) {
            sb.append(":doc:`");
                for(String type : types) {
                    String ref = type;
                    if (ref.endsWith("[]"))
                        ref = ref.substring(0, ref.length() - 2);

                    if (i != 0)
                        sb.append(">`, :doc:`");

                    echoType(type);

                    if (BaseParameter.isNotClass(ref))
                        ref = ".types/" + ref;

                    sb.append(" </api_"+ language +"/").append(ref.replace('\\', '/'));
                    i++;
                }

            sb.append(">`");
        }
    }

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

        echoType(description.getShortName());
        sb.append("\n")
                .append(StringUtils.repeat('-', description.getName().length()))
                .append("\n\n");

        sb.append(".. php:class:: ");
        echoType(description.getName());

        String extend = description.getExtends();
        String[] implement = description.getImplements();

        sb.append("\n");
        if (extend != null || implement != null || description.isAbstract() || description.isFinal()) {
            sb.append("\n");
            if (description.isAbstract()) {
                sb.append(" **abstract** class\n\n");
            }

            if (description.isFinal()) {
                sb.append(" **final** class\n\n");
            }

            if (description.isInterface()) {
                sb.append(" **interface**\n\n");
            }

            if (description.isTrait()) {
                sb.append(" **trait**\n\n");
            }

            if (extend != null) {
                sb.append(" **extends**: ");
                echoTypes(extend);
                sb.append("\n\n");
            }

            if (implement != null) {
                if (description.isInterface()) {
                    sb.append(" **extends**: ");
                } else {
                    sb.append(" **implements**: ");
                }
                echoTypes(implement);
                sb.append("\n\n");
            }
        }

        if (description.getDescription() != null) {
            sb.append("\n ");
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
        description = getDescription(description, language);

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(description));

        String line;
        int i = 0;
        try {
            while ((line = reader.readLine()) != null){
                if (i != 0){
                    line = StringUtils.repeat(' ', tabCount) + line;
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
            sb.append(" .. php:staticmethod:: ");
        } else {
            sb.append(" .. php:method:: ");
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

        sb.append(")\n\n");

        if (description instanceof MethodDescription) {
            MethodDescription meth = (MethodDescription)description;
            boolean add = false;
            if (meth.isFinal()) {
                sb.append("  **final**\n\n");
                add = true;
            }

            if (meth.isAbstract()) {
                sb.append("  **abstract**\n\n");
                add = true;
            }

            if (meth.isPrivate()) {
                sb.append("  **private**\n\n");
                add = true;
            }

            if (meth.isProtected()) {
                sb.append("  **protected**\n\n");
                add = true;
            }

            if (add)
                sb.append("\n");
        }

        if (description.getDescription() != null && !description.getDescription().isEmpty()) {
            sb.append("  ")
                    .append(addTabToDescription(description.getDescription().trim(), 2))
                    .append("\n\n");
        }
    }

    @Override
    protected void print(ArgumentDescription description) {
        sb.append("  :param ");

        sb.append("$").append(description.getName()).append(": ");

        if (description.getTypes() != null) {
            echoTypes(description.getTypes());
            sb.append(" ");
        }

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
            sb.append("  :returns: ");
            if (desc.getReturnTypes() != null) {
                echoTypes(desc.getReturnTypes());
                sb.append(" ");
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

    protected List<String> onEnd(File targetDirectory, String namespace) {
        StringBuilder sb = new StringBuilder();

        sb.append(namespace == null ? "API (" + languageName + ")" : namespace)
                .append("\n")
                .append(StringUtils.repeat('-', 30)).append("\n\n");

        sb.append(".. toctree::\n" +
                "   :maxdepth: 3\n\n");

        File[] files = targetDirectory.listFiles();

        List<String> ctree = new ArrayList<String>();
        if (files != null) {
            for(File dir : files) {
                if (dir.isFile() && dir.getName().endsWith(".rst")) {
                    if (!dir.getName().equals("index.rst")) {
                        ctree.add(dir.getName());
                    }
                } else if (dir.isDirectory() && !dir.getName().startsWith(".")) {
                    ctree.add(dir.getName() + "/index");
                }
            }

            for(File dir : files) {
                if (dir.isDirectory() && !dir.getName().startsWith(".")) {
                    List<String> tmp = onEnd(dir, namespace == null ? dir.getName() : dir.getName());
                    if (ctree.isEmpty() || ctree.size() == 1) {
                        ctree.clear();
                        for(String e : tmp) {
                            ctree.add(dir.getName() + "/" + e);
                        }
                    }
                }
            }
        }

        for(String e : ctree) {
            sb.append("   ").append(e).append("\n");
        }

        File file = new File(targetDirectory, "/index.rst");
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ctree;
    }
}
