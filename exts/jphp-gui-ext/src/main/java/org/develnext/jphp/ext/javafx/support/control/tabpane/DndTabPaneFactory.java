/*******************************************************************************
 * Copyright (c) 2014 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.develnext.jphp.ext.javafx.support.control.tabpane;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.develnext.jphp.ext.javafx.support.control.markers.PositionMarker;
import org.develnext.jphp.ext.javafx.support.control.markers.TabOutlineMarker;
import org.develnext.jphp.ext.javafx.support.control.tabpane.skin.DnDTabPaneSkin;

/**
 * Factory to create a tab pane who support DnD
 */
public final class DndTabPaneFactory {
    private static MarkerFeedback CURRENT_FEEDBACK;
//	private static Map<TabSerializationStrategy<?>, Boolean> SERIALIZERS = new WeakHashMap<>();

    private DndTabPaneFactory() {

    }

//	public static final class TabSerializationStrategy<O> {
//		private final Function<Tab, String> serializationFunction;
//		private final Function<String, O> deserializationFunction;
//		final String prefix = UUID.randomUUID().toString();
//
//		public TabSerializationStrategy(Function<Tab, String> serializationFunction, Function<String, O> deserializationFunction) {
//			this.serializationFunction = serializationFunction;
//			this.deserializationFunction = deserializationFunction;
//		}
//
//		public final String toString(Tab tab) {
//			return this.prefix + "#" + this.serializationFunction.apply(tab); //$NON-NLS-1$
//		}
//
//		public final O toData(String data) {
//			return deserializationFunction.apply(data.substring(prefix.length() + 1));
//		}
//	}
//
//	public static <O> TabSerializationStrategy<O> register(Function<Tab, String> serializationFunction, Function<String, O> deserializationFunction) {
//		TabSerializationStrategy<O> t = new TabSerializationStrategy<O>(serializationFunction, deserializationFunction);
//		SERIALIZERS.put(t, Boolean.TRUE);
//		return t;
//	}

    /**
     * Create a tab pane and set the drag strategy
     *
     * @param setup the setup instance for the pane
     * @return the tab pane
     */
    public static DndTabPane createDndTabPane(final Consumer<DragSetup> setup) {
        return new DndTabPane() {
            @Override
            protected javafx.scene.control.Skin<?> createDefaultSkin() {
                DnDTabPaneSkin skin = new DnDTabPaneSkin(this);
                setup.accept(skin);
                return skin;
            }
        };
    }

    /**
     * Create a tab pane with a default setup for drag feedback
     *
     * @param feedbackType the feedback type
     * @param setup        consumer to set up the tab pane
     * @return a pane containing the TabPane
     */
    public static Pane createDefaultDnDPane(final FeedbackType feedbackType, Consumer<TabPane> setup) {
        final StackPane pane = new StackPane();
        DndTabPane tabPane = new DndTabPane() {
            @Override
            protected javafx.scene.control.Skin<?> createDefaultSkin() {
                DnDTabPaneSkin skin = new DnDTabPaneSkin(this);
                setup(feedbackType, pane, skin);

                return skin;
            }
        };

        if (setup != null) {
            setup.accept(tabPane);
        }

        pane.getChildren().add(tabPane);
        return pane;
    }

    /**
     * Extract the tab content
     *
     * @param e the event
     * @return the content
     */
    public static boolean hasDnDContent(DragEvent e) {
        return e.getDragboard().hasContent(DnDTabPaneSkin.TAB_MOVE);
    }

//	/**
//	 * Extract the tab content
//	 * 
//	 * @param e
//	 *            the event
//	 * @param clazz
//	 *            the type
//	 * @return the content
//	 */
//	public static <O> O getDnDContent(DragEvent e, Class<O> clazz) {
//		String data = (String) e.getDragboard().getContent(DnDTabPaneSkin.TAB_MOVE);
//		Object rv = null;
//		for (TabSerializationStrategy<?> s : SERIALIZERS.keySet()) {
//			if (data.startsWith(s.prefix + "#")) { //$NON-NLS-1$
//				rv = s.toData(data);
//			}
//		}
//
//		if (rv == null) {
//			return (O) null;
//		} else {
//			if (clazz.isAssignableFrom(rv.getClass())) {
//				return (O) rv;
//			}
//		}
//
//		return (O) null;
//	}

    /**
     * Extract the content
     *
     * @param e the event
     * @return the return value
     */
    public static String getDnDContent(DragEvent e) {
        return (String) e.getDragboard().getContent(DnDTabPaneSkin.TAB_MOVE);
    }

    /**
     * Setup insert marker
     *
     * @param type       the feedback type.
     * @param layoutNode the layout node used to position
     * @param setup      the setup
     */
    public static void setup(final FeedbackType type, final Pane layoutNode, DragSetup setup) {
        setup.setStartFunction(new Function<Tab, Boolean>() {
            @Override
            public Boolean apply(Tab t) {
                DndTabPane tabPane = (DndTabPane) t.getTabPane();

                return Boolean.valueOf(!t.isDisabled() && ((DndTabPane) t.getTabPane()).isDraggingEnabled() && tabPane.isDraggableTab(t));
            }
        });
        setup.setFeedbackConsumer(new Consumer<FeedbackData>() {
            @Override
            public void accept(FeedbackData d) {
                DndTabPane tabPane = (DndTabPane) d.draggedTab.getTabPane();

                if (!tabPane.isDraggableTab(d.draggedTab)) {
                    cleanup();
                    return;
                }

                handleFeedback(type, layoutNode, d);
            }
        });
        setup.setDropConsumer(new Consumer<DroppedData>() {
            @Override
            public void accept(DroppedData data) {
                DndTabPane tabPane = (DndTabPane) data.draggedTab.getTabPane();

                if (!tabPane.isDraggableTab(data.targetTab)) {
                    return;
                }

                DndTabPaneFactory.handleDropped(data);
            }
        });
        setup.setDragFinishedConsumer(new Consumer<Tab>() {
            @Override
            public void accept(Tab tab) {
                DndTabPaneFactory.handleFinished(tab);
            }
        });
    }

    private static void fireTabDraggedEvent(DndTabPane tabPane, Tab draggedTab, int fromIndex, int toIndex) {
        tabPane.fireTabDragged(draggedTab, fromIndex, toIndex);
    }

    private static void handleDropped(DroppedData data) {
        TabPane targetPane = data.targetTab.getTabPane();
        int oldIndex = data.draggedTab.getTabPane().getTabs().indexOf(data.draggedTab);
        data.draggedTab.getTabPane().getTabs().remove(data.draggedTab);
        int idx = targetPane.getTabs().indexOf(data.targetTab);

        if (data.dropType == DropType.AFTER) {
            if (idx + 1 <= targetPane.getTabs().size()) {
                targetPane.getTabs().add(idx + 1, data.draggedTab);
            } else {
                targetPane.getTabs().add(data.draggedTab);
            }
        } else {
            targetPane.getTabs().add(idx, data.draggedTab);
        }

        fireTabDraggedEvent((DndTabPane) targetPane, data.draggedTab, oldIndex, targetPane.getTabs().indexOf(data.draggedTab));

        data.draggedTab.getTabPane().getSelectionModel().select(data.draggedTab);
    }

    private static void handleFeedback(FeedbackType type, Pane layoutNode, FeedbackData data) {
        if (data.dropType == DropType.NONE) {
            cleanup();
            return;
        }

        MarkerFeedback f = CURRENT_FEEDBACK;
        if (f == null || !f.data.equals(data)) {
            cleanup();
            if (type == FeedbackType.MARKER) {
                CURRENT_FEEDBACK = handleMarker(layoutNode, data);
            } else {
                CURRENT_FEEDBACK = handleOutline(layoutNode, data);
            }
        }
    }

    private static void handleFinished(Tab tab) {
        cleanup();
    }

    static void cleanup() {
        if (CURRENT_FEEDBACK != null) {
            CURRENT_FEEDBACK.hide();
            CURRENT_FEEDBACK = null;
        }
    }

    private static MarkerFeedback handleMarker(Pane layoutNode, FeedbackData data) {
        PositionMarker marker = null;
        for (Node n : layoutNode.getChildren()) {
            if (n instanceof PositionMarker) {
                marker = (PositionMarker) n;
            }
        }

        if (marker == null) {
            marker = new PositionMarker();
            marker.setManaged(false);
            layoutNode.getChildren().add(marker);
        } else {
            marker.setVisible(true);
        }

        double w = marker.getBoundsInLocal().getWidth();
        double h = marker.getBoundsInLocal().getHeight();

        double ratio = data.bounds.getHeight() / h;
        ratio += 0.1;
        marker.setScaleX(ratio);
        marker.setScaleY(ratio);

        double wDiff = w / 2;
        double hDiff = (h - h * ratio) / 2;

        if (data.dropType == DropType.AFTER) {
            marker.relocate(data.bounds.getMinX() + data.bounds.getWidth() - wDiff, data.bounds.getMinY() - hDiff);
        } else {
            marker.relocate(data.bounds.getMinX() - wDiff, data.bounds.getMinY() - hDiff);
        }

        final PositionMarker fmarker = marker;

        return new MarkerFeedback(data) {

            @Override
            public void hide() {
                fmarker.setVisible(false);
            }
        };
    }

    private static MarkerFeedback handleOutline(Pane layoutNode, FeedbackData data) {
        TabOutlineMarker marker = null;

        for (Node n : layoutNode.getChildren()) {
            if (n instanceof TabOutlineMarker) {
                marker = (TabOutlineMarker) n;
            }
        }

        if (marker == null) {
            marker = new TabOutlineMarker(layoutNode.getBoundsInLocal(), new BoundingBox(data.bounds.getMinX(), data.bounds.getMinY(), data.bounds.getWidth(), data.bounds.getHeight()), data.dropType == DropType.BEFORE);
            marker.setManaged(false);
            marker.setMouseTransparent(true);
            layoutNode.getChildren().add(marker);
        } else {
            marker.updateBounds(layoutNode.getBoundsInLocal(), new BoundingBox(data.bounds.getMinX(), data.bounds.getMinY(), data.bounds.getWidth(), data.bounds.getHeight()), data.dropType == DropType.BEFORE);
            marker.setVisible(true);
        }

        final TabOutlineMarker fmarker = marker;

        return new MarkerFeedback(data) {

            @Override
            public void hide() {
                fmarker.setVisible(false);
            }
        };
    }

    private abstract static class MarkerFeedback {
        public final FeedbackData data;

        public MarkerFeedback(FeedbackData data) {
            this.data = data;
        }

        public abstract void hide();
    }

    /**
     * The drop type
     */
    public enum DropType {
        /**
         * No dropping
         */
        NONE,
        /**
         * Dropped before a reference tab
         */
        BEFORE,
        /**
         * Dropped after a reference tab
         */
        AFTER
    }

    /**
     * The feedback type to use
     */
    public enum FeedbackType {
        /**
         * Show a marker
         */
        MARKER,
        /**
         * Show an outline
         */
        OUTLINE
    }

    /**
     * Data to create a feedback
     */
    public static class FeedbackData {
        /**
         * The tab dragged
         */
        public final Tab draggedTab;
        /**
         * The reference tab
         */
        public final Tab targetTab;
        /**
         * The bounds of the reference tab
         */
        public final Bounds bounds;
        /**
         * The drop type
         */
        public final DropType dropType;

        /**
         * Create a feedback data
         *
         * @param draggedTab the dragged tab
         * @param targetTab  the reference tab
         * @param bounds     the bounds of the reference tab
         * @param dropType   the drop type
         */
        public FeedbackData(Tab draggedTab, Tab targetTab, Bounds bounds, DropType dropType) {
            this.draggedTab = draggedTab;
            this.targetTab = targetTab;
            this.bounds = bounds;
            this.dropType = dropType;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.bounds == null) ? 0 : this.bounds.hashCode());
            result = prime * result + this.draggedTab.hashCode();
            result = prime * result + this.dropType.hashCode();
            result = prime * result + ((this.targetTab == null) ? 0 : this.targetTab.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FeedbackData other = (FeedbackData) obj;
            if (this.bounds == null) {
                if (other.bounds != null)
                    return false;
            } else if (!this.bounds.equals(other.bounds))
                return false;
            if (!this.draggedTab.equals(other.draggedTab))
                return false;
            if (this.dropType != other.dropType)
                return false;
            if (this.targetTab == null) {
                if (other.targetTab != null)
                    return false;
            } else if (!this.targetTab.equals(other.targetTab))
                return false;
            return true;
        }

    }

    /**
     * The drop data
     */
    public static class DroppedData {
        /**
         * The dragged tab
         */
        public final Tab draggedTab;
        /**
         * The reference tab
         */
        public final Tab targetTab;
        /**
         * The drop type
         */
        public final DropType dropType;

        /**
         * Create drop data
         *
         * @param draggedTab the dragged tab
         * @param targetTab  the target tab
         * @param dropType   the drop type
         */
        public DroppedData(Tab draggedTab, Tab targetTab, DropType dropType) {
            this.draggedTab = draggedTab;
            this.targetTab = targetTab;
            this.dropType = dropType;
        }
    }

    /**
     * Setup of the drag and drop
     */
    public interface DragSetup {
        /**
         * Function to handle the starting of the the drag
         *
         * @param startFunction the function
         */
        public void setStartFunction(Function<Tab, Boolean> startFunction);

        /**
         * Consumer called to handle the finishing of the drag process
         *
         * @param dragFinishedConsumer the consumer
         */
        public void setDragFinishedConsumer(Consumer<Tab> dragFinishedConsumer);

        /**
         * Consumer called to present drag feedback
         *
         * @param feedbackConsumer the consumer to call
         */
        public void setFeedbackConsumer(Consumer<FeedbackData> feedbackConsumer);

        /**
         * Consumer called when the drop has to be handled
         *
         * @param dropConsumer the consumer
         */
        public void setDropConsumer(Consumer<DroppedData> dropConsumer);

        /**
         * Function to translate the tab content into clipboard content
         *
         * @param clipboardDataFunction the function
         */
        public void setClipboardDataFunction(Function<Tab, String> clipboardDataFunction);
    }
}
