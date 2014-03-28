package org.develnext.jphp.stubBuilder.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 18:35/23.03.14
 */
public class StubPhpFunction extends StubPhpNamedMemberWithChildren {
    private final List<StubPhpVariable> parameters = new ArrayList<StubPhpVariable>(5);

    public StubPhpFunction(String name) {
        super(name);
    }

    public void addParameter(StubPhpVariable parameter) {
        parameters.add(parameter);
    }

    public List<StubPhpVariable> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("public function ");
        builder.append(getName());
        builder.append("(");
        for(int i = 0; i < parameters.size(); i++) {
            if(i != 0) {
                builder.append(", ");
            }
            builder.append(parameters.get(i));
        }
        builder.append(")");
        return builder.toString();
    }
}
