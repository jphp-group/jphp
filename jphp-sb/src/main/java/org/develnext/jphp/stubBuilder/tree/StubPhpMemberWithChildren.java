package org.develnext.jphp.stubBuilder.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 18:28/23.03.14
 */
public abstract class StubPhpMemberWithChildren extends StubPhpMember {
    private List<StubPhpMember> members = new ArrayList<StubPhpMember>();

    public void add(StubPhpMember member) {
        members.add(member);
    }

    public List<StubPhpMember> getMembers() {
        return members;
    }
}
