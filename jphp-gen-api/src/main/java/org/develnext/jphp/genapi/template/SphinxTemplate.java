package org.develnext.jphp.genapi.template;

import org.develnext.jphp.genapi.description.ArgumentDescription;
import org.develnext.jphp.genapi.description.ClassDescription;
import org.develnext.jphp.genapi.description.FunctionDescription;
import org.develnext.jphp.genapi.description.MethodDescription;
import php.runtime.common.StringUtils;

import java.util.Collection;

public class SphinxTemplate extends BaseTemplate {

    @Override
    protected void print(ClassDescription description) {
        sb.append(".. php:class:: ").append(description.getName()).append("\n\n");
    }

    @Override
    protected void print(MethodDescription description) {
        print((FunctionDescription)description);
    }

    @Override
    protected void print(FunctionDescription description) {
        sb.append("\t..php:method:: ").append(description.getName()).append("(");
        int i = 0;

        Collection<ArgumentDescription> args = description.getArguments();
        for (ArgumentDescription arg : args) {
            if (i != 0)
                sb.append(", ");

            sb.append("$").append(arg.getName());
            if (arg.getValue() != null)
                sb.append(" = ").append(arg.getValue());

            i++;
        }

        sb.append(")\n");

        if (description.getDescription() != null && !description.getDescription().isEmpty())
            sb.append("\t\t").append(description.getDescription()).append("\n\n");
    }

    @Override
    protected void print(ArgumentDescription description) {
        sb.append("\t\t:param ");

        if (description.getTypes() != null)
            sb.append(StringUtils.join(description.getTypes(), " | ")).append(" ");

        sb.append("$").append(description.getName()).append(": ");
        if (description.getDescription() != null)
            sb.append(description.getDescription());

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

            if (desc.getReturnDescription() != null)
                sb.append(desc.getReturnDescription());

            sb.append("\n");
        }
    }
}
