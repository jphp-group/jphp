package org.develnext.jphp.stubBuilder.tree;

/**
 * @author VISTALL
 * @since 18:56/23.03.14
 */
public class StubPhpNamespace extends StubPhpNamedMemberWithChildren {
    public StubPhpNamespace(String name) {
        super(name);
    }

    public String toString() {
        return "namespace " + getName();
    }
}
