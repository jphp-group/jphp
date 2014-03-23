package org.develnext.jphp.stubBuilder.tree;

/**
 * @author VISTALL
 * @since 18:28/23.03.14
 */
public class StubPhpClass extends StubPhpNamedMemberWithChildren {
    public StubPhpClass(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "public class " + getName();
    }
}
