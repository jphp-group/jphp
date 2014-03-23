package org.develnext.jphp.stubBuilder.tree;

import java.util.List;

/**
 * @author VISTALL
 * @since 18:27/23.03.14
 */
public class StubPhpFile extends StubPhpMemberWithChildren {
    private String name;

    public StubPhpFile(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (StubPhpMember temp : getMembers()) {
            build(builder, temp, 0);
        }
        return builder.toString();
    }

    private static void build(StringBuilder builder, StubPhpMember member, int level) {
        indent(builder, level);
        builder.append(member.toString());
        line(builder);

        if (member instanceof StubPhpMemberWithChildren) {
            indent(builder, level);
            builder.append("{");
            line(builder);

            List<StubPhpMember> members = ((StubPhpMemberWithChildren) member).getMembers();
            for (int i = 0; i < members.size(); i++) {
                if(i != 0) {
                    line(builder);
                }
                StubPhpMember temp = members.get(i);
                build(builder, temp, level + 1);
            }

            indent(builder, level);
            builder.append('}');
            line(builder);
        }
    }

    private static void line(StringBuilder builder) {
        builder.append('\n');
    }

    private static void indent(StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) {
            builder.append("\t");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
