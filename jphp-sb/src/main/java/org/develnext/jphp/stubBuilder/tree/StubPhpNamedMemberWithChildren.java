package org.develnext.jphp.stubBuilder.tree;

/**
 * @author VISTALL
 * @since 18:36/23.03.14
 */
public abstract class StubPhpNamedMemberWithChildren extends StubPhpMemberWithChildren {
    private final String name;

    public StubPhpNamedMemberWithChildren(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
