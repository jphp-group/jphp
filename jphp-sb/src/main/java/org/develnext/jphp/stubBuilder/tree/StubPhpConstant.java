package org.develnext.jphp.stubBuilder.tree;

/**
 * @author VISTALL
 * @since 19:39/23.03.14
 */
public class StubPhpConstant extends StubPhpVariable {
    public StubPhpConstant(String name) {
        super(name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("const ");
        builder.append(getName());
        if(getOptionalValue() != null) {
            builder.append(" = ").append(getOptionalValue());
        }
        return builder.toString();
    }
}
