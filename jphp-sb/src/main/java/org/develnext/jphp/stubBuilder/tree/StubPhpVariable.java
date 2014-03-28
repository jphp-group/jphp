package org.develnext.jphp.stubBuilder.tree;

/**
 * @author VISTALL
 * @since 19:41/23.03.14
 */
public class StubPhpVariable extends StubPhpMember {
    private final String name;
    private String typeClass;
    private String optionalValue;

    public StubPhpVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public String getOptionalValue() {
        return optionalValue;
    }

    public void setOptionalValue(String optionalValue) {
        this.optionalValue = optionalValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(getTypeClass() != null) {
            builder.append(getTypeClass()).append(" ");
        }
        builder.append('$');
        builder.append(getName());
        if(getOptionalValue() != null) {
            builder.append(" = ").append(getOptionalValue());
        }
        return builder.toString();
    }
}
