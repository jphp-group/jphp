package org.develnext.jphp.swing.event;

import php.runtime.Memory;
import php.runtime.common.CollectionUtils;
import php.runtime.common.Function;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.ComponentProperties;
import org.develnext.jphp.swing.classes.components.tree.UITree;
import org.develnext.jphp.swing.classes.components.tree.WrapTreeNode;
import org.develnext.jphp.swing.support.JTreeX;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.TrueMemory;

import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Set;

public class JTreeXEventProvider extends EventProvider<JTreeX> {
    @Override
    public Class<JTreeX> getComponentClass() {
        return JTreeX.class;
    }

    @Override
    public void register(final Environment env, final JTreeX component, final ComponentProperties properties) {
        final ObjectMemory self = new ObjectMemory(new UITree(env, component));

        component.getContent().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                trigger(env, properties, "selected", new Function<Memory[]>() {
                    @Override
                    public Memory[] call() {
                        Memory oldNode = Memory.NULL;
                        if (e.getOldLeadSelectionPath() != null){
                            oldNode = new ObjectMemory(new WrapTreeNode(
                                    env, (DefaultMutableTreeNode)e.getOldLeadSelectionPath().getLastPathComponent()
                            ));
                        }

                        Memory node = Memory.NULL;
                        if (e.getNewLeadSelectionPath() != null) {
                            node = new ObjectMemory(new WrapTreeNode(
                                    env, (DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent()
                            ));
                        }

                        return new Memory[]{self, node, oldNode, TrueMemory.valueOf(e.isAddedPath())};
                    }
                });
            }
        });

        component.getContent().addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(final TreeExpansionEvent event) {
                trigger(env, properties, "willExpand", new Function<Memory[]>() {
                    @Override
                    public Memory[] call() {
                        return new Memory[]{
                                self,
                                new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)event.getPath().getLastPathComponent()))
                        };
                    }
                });
            }

            @Override
            public void treeWillCollapse(final TreeExpansionEvent event) {
                trigger(env, properties, "willCollapse", new Function<Memory[]>() {
                    @Override
                    public Memory[] call() {
                        return new Memory[]{
                                self,
                                new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)event.getPath().getLastPathComponent()))
                        };
                    }
                });
            }
        });

        component.getContent().addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(final TreeExpansionEvent event) {
                trigger(env, properties, "expanded", new Function<Memory[]>() {
                    @Override
                    public Memory[] call() {
                        return new Memory[]{
                                self,
                                new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)event.getPath().getLastPathComponent()))
                        };
                    }
                });
            }

            @Override
            public void treeCollapsed(final TreeExpansionEvent event) {
                trigger(env, properties, "collapsed", new Function<Memory[]>() {
                    @Override
                    public Memory[] call() {
                        return new Memory[]{
                                self,
                                new ObjectMemory(new WrapTreeNode(env, (DefaultMutableTreeNode)event.getPath().getLastPathComponent()))
                        };
                    }
                });
            }
        });
    }

    private final static Set<String> allows = CollectionUtils.newSet(
            "collapsed", "expanded", "willcollapse", "willexpand", "selected"
    );

    @Override
    public boolean isAllowedEventType(Component component, String code) {
        return allows.contains(code.toLowerCase());
    }
}
