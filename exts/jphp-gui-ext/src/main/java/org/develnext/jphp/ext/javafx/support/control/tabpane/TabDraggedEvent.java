/*******************************************************************************
 * Copyright (c) 2015 SIB Visions and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robert Zenz<robert.zenz@sibvisions.com> - creation
 *******************************************************************************/
package org.develnext.jphp.ext.javafx.support.control.tabpane;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Tab;

/**
 * The event that occurres if a tab is dragged.
 */
@SuppressWarnings("serial")
public class TabDraggedEvent extends Event {
	private int fromIndex;
	private int toIndex;
	
	public static final EventType<TabDraggedEvent> TAB_DRAGGED = new EventType<>(Event.ANY, "TAB_DRAGGED");
	
	/**
	 * Creates a new instance of {@link TabDraggedEvent}.
	 * 
	 * @param draggedTab the dragged tab.
	 * @param fromIndex the from index.
	 * @param toIndex the to index.
	 */
	public TabDraggedEvent(Tab draggedTab, int fromIndex, int toIndex) {
		super(TAB_DRAGGED);
		
		this.source = draggedTab;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}
	
	/**
	 * Gets the dragged tab.
	 * 
	 * @return the dragged tab.
	 */
	public Tab getDraggedTab() {
		return (Tab) source;
	}
	
	/**
	 * Gets the from index.
	 * 
	 * @return the from index.
	 */
	public int getFromIndex() {
		return fromIndex;
	}
	
	/**
	 * Gets the to index.
	 * 
	 * @return the to index.
	 */
	public int getToIndex() {
		return toIndex;
	}
}
