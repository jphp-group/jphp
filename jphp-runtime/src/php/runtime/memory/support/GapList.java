/*
 * Copyright 2012 by Thomas Mauch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: GapList.java 3081 2016-01-05 00:29:01Z origo $
 */
package php.runtime.memory.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * GapList combines the strengths of both ArrayList and LinkedList.
 * It is implemented to offer both efficient random access to elements
 * by index (as ArrayList does) and at the same time efficient adding
 * and removing elements to and from beginning and end (as LinkedList does).
 * It also exploits the locality of reference often seen in applications
 * to further improve performance, e.g. for iterating over the list.<p>
 *
 * <strong>Note that this implementation is not synchronized.</strong><p>
 *
 * Note that the iterators provided are not fail-fast.<p>
 *
 * @author Thomas Mauch
 * @version $Id: GapList.java 3081 2016-01-05 00:29:01Z origo $
 *
 * @param <E> type of elements stored in the list
 * @see	    java.util.List
 * @see	    java.util.ArrayList
 * @see	    java.util.LinkedList
 */
public class GapList<E> extends IList<E> {
    /*
     * Helper variables to enable code for debugging.
     * As the variables are declared as "static final boolean", the JVM
     * will be able to detect unused branches and will not execute the
     * code (the same approach is used for the assert statement).
     */
    /** If true the invariants the GapList are checked for debugging */
    private static final boolean DEBUG_CHECK = false;
    /** If true the calls to some methods are traced out for debugging */
    private static final boolean DEBUG_TRACE = false;
    /** If true the internal state of the GapList is traced out for debugging */
    private static final boolean DEBUG_DUMP = false;

	/** Empty array used for default initialization */
    private static Object[] EMPTY_VALUES = new Object[0];

    // -- EMPTY --

    // Cannot make a static reference to the non-static type E:
    // public static GapList<E> EMPTY = GapList.create().unmodifiableList();
    // Syntax error:
    // public static <EE> GapList<EE> EMPTY = GapList.create().unmodifiableList();

    /** Unmodifiable empty instance */
    @SuppressWarnings("rawtypes")
    private static final GapList EMPTY = GapList.create().unmodifiableList();

    /**
     * @return unmodifiable empty instance
     */
    @SuppressWarnings("unchecked")
    public static <EE> GapList<EE> EMPTY() {
        return EMPTY;
    }


    /** UID for serialization */
    private static final long serialVersionUID = -4477005565661968383L;

    /** Default capacity for list */
    public static final int DEFAULT_CAPACITY = 10;

	/** Array holding raw data */
	private E[] values;
	/** Number of elements stored in this GapList */
	private int size;
	/** Physical position of first element */
	private int start;
	/** Physical position after last element */
	private int end;
	/** Size of gap (0 if there is no gap) */
	private int gapSize;
	/** Logical index of first element after gap (ignored if gapSize=0) */
	private int gapIndex;
	/** Physical position of first slot in gap (ignored if gapSize=0) */
	private int gapStart;


	// --- Static methods ---

    /**
     * Create new list.
     *
     * @return          created list
     * @param <E>       type of elements stored in the list
     */
    // This separate method is needed as the varargs variant creates the list with specific size
    public static <E> GapList<E> create() {
        return new GapList<E>();
    }

    /**
     * Create new list with specified elements.
     *
     * @param coll      collection with element
     * @return          created list
     * @param <E>       type of elements stored in the list
     */
	public static <E> GapList<E> create(Collection<? extends E> coll) {
		return new GapList<E>(coll);
	}

	/**
	 * Create new list with specified elements.
	 *
	 * @param elems 	array with elements
	 * @return 			created list
	 * @param <E> 		type of elements stored in the list
	 */
	public static <E> GapList<E> create(E... elems) {
		GapList<E> list = new GapList<E>();
		list.init(elems);
		return list;
	}

	/**
	 * Calculate index for physical access to an element.
	 *
	 * @param idx	logical index of element
	 * @return		physical index to access element in values[]
	 */
	private final int physIndex(int idx) {
		int physIdx = idx+start;
		if (idx >= gapIndex) {
			physIdx += gapSize;
		}
		if (physIdx >= values.length) {
			physIdx -= values.length;
		}
		return physIdx;
	}

	/**
	 * Calculate indexes for physical access to a range of elements.
	 * The method returns between one and three ranges of physical indexes.
	 *
	 * @param idx0  start index
	 * @param idx1	end index
	 * @return		array with physical start and end indexes (may contain 2, 4, or 6 elements)
	 */
	private int[] physIndex(int idx0, int idx1) {
		assert(idx0 >=0 && idx1 <= size && idx0 <= idx1);

		if (idx0 == idx1) {
			return new int[0];
		}

		// Decrement idx1 to make sure we get the physical index
		// of an existing position. We will increment the physical index
		// again before returning.
		idx1--;
		int pidx0 = physIndex(idx0);
		if (idx1 == idx0) {
			return new int[] {
				pidx0, pidx0+1
			};
		}

		int pidx1 = physIndex(idx1);
		if (pidx0 < pidx1) {
			if (gapSize > 0 && pidx0 < gapStart && pidx1 > gapStart) {
				assert(pidx0 < gapStart);
				assert(gapStart+gapSize < pidx1+1);

				return new int[] {
					pidx0, gapStart,
					gapStart+gapSize, pidx1+1
				};
			} else {
				return new int[] {
					pidx0, pidx1+1
				};
			}
		} else {
			assert(pidx0 > pidx1);
			assert(start != 0);
			if (gapSize > 0 && pidx1 > gapStart && gapStart > 0) {
				assert(pidx0 < values.length);
				assert(0 < gapStart);
				assert(gapStart+gapSize < pidx1+1);

				return new int[] {
					pidx0, values.length,
					0, gapStart,
					gapStart+gapSize, pidx1+1
				};
			} else if (gapSize > 0 && pidx0 < gapStart && gapStart+gapSize < values.length) {
				assert(pidx0 < gapStart);
				assert(gapStart+gapSize < values.length);
				assert(0 < pidx1+1);

				return new int[] {
					pidx0, gapStart,
					gapStart+gapSize, values.length,
					0, pidx1+1
				};
			} else {
				assert(pidx0 < values.length);
				assert(0 < pidx1+1);

				int end = values.length;
				if (gapSize > 0 && gapStart > pidx0) {
					end = gapStart;
				}
				int start = 0;
				if (gapSize > 0 &&  (gapStart+gapSize)%values.length < pidx1+1) {
					start = (gapStart+gapSize)%values.length;
				}

				return new int[] {
					pidx0, end,
					start, pidx1+1
				};
			}
		}
	}

	@Override
	protected void doAssign(IList<E> that) {
		GapList<E> list = (GapList<E>) that;
        this.values = list.values;
        this.size = list.size;
        this.start = list.start;
        this.end = list.end;
        this.gapSize = list.gapSize;
        this.gapIndex = list.gapIndex;
        this.gapStart = list.gapStart;
	}

    /**
     * Constructor used internally, e.g. for ImmutableGapList.
     *
     * @param copy true to copy all instance values from source,
     *             if false nothing is done
     * @param that list to copy
     */
    protected GapList(boolean copy, GapList<E> that) {
        if (copy) {
        	doAssign(that);
        }
    }

	/**
	 * Construct a list with the default initial capacity.
	 */
	public GapList() {
		init();
	}

	/**
     * Construct a list with specified initial capacity.
	 *
	 * @param capacity	capacity
	 */
	public GapList(int capacity) {
		init(new Object[capacity], 0);
	}

	/**
	 * Construct a list to contain the specified elements.
	 * The list will have an initial capacity to hold these elements.
	 *
	 * @param coll	collection with elements
	 */
	public GapList(Collection<? extends E> coll) {
		init(coll);
	}

	/**
	 * Initialize the list to be empty.
	 * The list will have the default initial capacity.
	 */
	public void init() {
		init(EMPTY_VALUES, 0);
	}

	/**
	 * Initialize the list to contain the specified elements only.
	 * The list will have an initial capacity to hold these elements.
	 *
	 * @param coll collection with elements
	 */
	public void init(Collection<? extends E> coll) {
		Object[] array = toArray(coll);
		init(array, array.length);
	}

	/**
     * Initialize the list to contain the specified elements only.
	 * The list will have an initial capacity to hold these elements.
     *
	 * @param elems array with elements
	 */
	public void init(E... elems) {
		Object[] array = elems.clone();
		init(array, array.length);
	}

	@Override
	public E getDefaultElem() {
		return null;
	}

	/**
     * Returns a shallow copy of this <tt>GapList</tt> instance.
     * (the new list will contain the same elements as the source list, i.e. the elements themselves are not copied).
     * This method is identical to clone() except that the result is casted to GapList.
     *
     * @return a copy of this <tt>GapList</tt> instance
	 */
	@Override
    public GapList<E> copy() {
	    return (GapList<E>) super.copy();
	}

    /**
     * Increases the capacity of this <tt>GapList</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity
     */
    // Only overridden to change Javadoc
	@Override
    public void ensureCapacity(int minCapacity) {
		super.ensureCapacity(minCapacity);
    }

    /**
     * Returns a shallow copy of this <tt>GapList</tt> instance
     * (The elements themselves are not copied).
     * The capacity of the list will be set to the number of elements,
     * so after calling clone(), size and capacity are equal.
     *
     * @return a copy of this <tt>GapList</tt> instance
     */
    // Only overridden to change Javadoc
    @Override
    public Object clone() {
		return super.clone();
    }

	@Override
    public GapList<E> unmodifiableList() {
        // Naming as in java.util.Collections#unmodifiableList
        return new ImmutableGapList<E>(this);
    }

	@Override
	protected void doClone(IList<E> that) {
		// Do not simply clone the array, but make sure its capacity
		// is equal to the size (as in ArrayList)
		init(that.toArray(), that.size());
	}

	/**
	 * Normalize data of GapList so the elements are found
	 * from values[0] to values[size-1].
	 * This method can help to speed up operations like sort or
	 * binarySearch.
	 */
	private void normalize() {
		if (start == 0 && end == 0 && gapSize == 0 && gapStart == 0 && gapIndex == 0) {
			return;
		}
		init(toArray(), size());
	}

	/**
	 * Initialize all instance fields.
	 *
	 * @param values	new values array
	 * @param size		new size
	 */
	@SuppressWarnings("unchecked")
	void init(Object[] values, int size) {
		this.values = (E[]) values;
		this.size = size;

		start = 0;
		end = 0;
		gapSize = 0;
		gapStart = 0;
		gapIndex = 0;

		if (DEBUG_CHECK) debugCheck();
	}

	@Override
	protected void doClear() {
		init(values, 0);
		for (int i=0; i<values.length; i++) {
			values[i] = null;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int capacity() {
		return values.length;
	}

    @Override
    public E get(int index) {
        // A note about the inlining capabilities of the Java HotSpot Performance Engine
        // (http://java.sun.com/developer/technicalArticles/Networking/HotSpot/inlining.html)
        // The JVM seems not able to inline the methods called within
        // this method, irrespective whether they are "private final" or not.
        // Also -XX:+AggressiveOpts seems not to help.
        // We therefore do inlining manually.

        // INLINE: checkIndex(index);
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + " (size: " + size() + ")");
        }
        return doGet(index);
    }

    @Override
    protected E doGet(int index) {
        assert (index >= 0 && index < size);

        // INLINE: return values[physIndex(index)];
        int physIdx = index+start;
        if (index >= gapIndex) {
            physIdx += gapSize;
        }
        if (physIdx >= values.length) {
            physIdx -= values.length;
        }
        return values[physIdx];
    }

	@Override
    protected E doSet(int index, E elem) {
        assert (index >= 0 && index < size);

        int physIdx = physIndex(index);
        E oldElem = values[physIdx];
        values[physIdx] = elem;
        return oldElem;
    }

    @Override
    protected E doReSet(int index, E elem) {
        assert (index >= 0 && index < size);

        int physIdx = physIndex(index);
        E oldElem = values[physIdx];
        values[physIdx] = elem;
        return oldElem;
    }

    @Override
	public boolean add(E elem) {
		if (DEBUG_TRACE) {
			debugLog("add: " + elem);
			if (DEBUG_DUMP) debugDump();
		}
		return doAdd(-1, elem);
	}

	@Override
	public void add(int index, E elem)	{
		if (DEBUG_TRACE) {
			debugLog("add: " + index + ", " + elem);
			if (DEBUG_DUMP) debugDump();
		}
		checkIndexAdd(index);
		doAdd(index, elem);
	}

	@Override
	public GapList<E> getAll(int index, int len) {
		return (GapList<E>) super.getAll(index, len);
	}

	@Override
	public GapList<E> getAll(E elem) {
		return (GapList<E>) super.getAll(elem);
	}

	@Override
    public <R> GapList<R> mappedList(IFunction<E,R> mapper) {
		return (GapList<R>) super.mappedList(mapper);
	}

	@Override
	protected boolean doAdd(int index, E elem) {
        doEnsureCapacity(size + 1);

		if (index == -1) {
		    index = size;
		}
        assert(index >= 0 && index <= size);

        int physIdx;
		// Add at last position
		if (index == size && (end != start || size == 0)) {
			if (DEBUG_TRACE) debugLog("Case A0");
			physIdx = end;
			end++;
			if (end >= values.length) {
				end -= values.length;
			}

		// Add at first position
		} else if (index == 0 && (end != start || size == 0)) {
			if (DEBUG_TRACE) debugLog("Case A1");
			start--;
			if (start < 0) {
				start += values.length;
			}
			physIdx = start;
			if (gapSize > 0) {
				gapIndex++;
			}

		// Shrink gap
		} else if (gapSize > 0 && index == gapIndex) {
			if (DEBUG_TRACE) debugLog("Case A2");
			physIdx = gapStart+gapSize-1;
			if (physIdx >= values.length) {
				physIdx -= values.length;
			}
			gapSize--;

		// Add at other positions
		} else {
			physIdx = physIndex(index);

			if (gapSize == 0) {
				// Create new gap
				if (start < end && start > 0) {
					// S4: Space is at head and tail
					assert(debugState() == 4);
					int len1 = physIdx-start;
					int len2 = end-physIdx;
					if (len1 <= len2) {
						if (DEBUG_TRACE) debugLog("Case A3");
						moveData(start, 0, len1);
						gapSize = start-1;
						gapStart = len1;
						gapIndex = len1;
						start = 0;
						physIdx--;
					} else {
						if (DEBUG_TRACE) debugLog("Case A4");
						moveData(physIdx, values.length-len2, len2);
						gapSize = values.length-end-1;
						gapStart = physIdx+1;
						gapIndex = index+1;
						end = 0;
					}

				} else if (physIdx < end) {
					assert(debugState() == 2 || debugState() == 5);
					if (DEBUG_TRACE) debugLog("Case A5");
					int len = end-physIdx;
					int rightSize = (start-end+values.length)%values.length;
					moveData(physIdx, end+rightSize-len, len);
					end = start;
					gapSize = rightSize-1;
					gapStart = physIdx+1;
					gapIndex = index+1;

				} else {
					assert(debugState() == 3 || debugState() == 5);
					assert(physIdx > end);
					if (DEBUG_TRACE) debugLog("Case A6");
					int len = physIdx-start;
					int rightSize = start-end;
					moveData(start, end, len);
					start -= rightSize;
					end = start;
					gapSize = rightSize-1;
					gapStart = start+len;
					gapIndex = index;
					physIdx--;
				}
			} else {
				// Move existing gap
				boolean moveLeft;
				int gapEnd = (gapStart+gapSize-1) % values.length + 1;
				if (gapEnd < gapStart) {
					assert(debugState() == 9 || debugState() == 12);
					// Gap is at head and tail
					int len1 = physIdx-gapEnd;
					int len2 = gapStart-physIdx-1;
					if (len1 <= len2) {
						if (DEBUG_TRACE) debugLog("Case A7a");
						moveLeft = true;
					} else {
						if (DEBUG_TRACE) debugLog("Case A8a");
						moveLeft = false;
					}

				} else {
					assert(debugState() == 6 || debugState() == 7 || debugState() == 8 || debugState() == 9 || debugState() == 10 ||
						debugState() == 11 || debugState() == 12 || debugState() == 13 || debugState() == 14 || debugState() == 15);
					if (physIdx > gapStart) {
						if (DEBUG_TRACE) debugLog("Case A7b");
						moveLeft = true;
					} else  {
						if (DEBUG_TRACE) debugLog("Case A8b");
						moveLeft = false;
					}
				}
				if (moveLeft) {
					int src = gapStart+gapSize;
					int dst = gapStart;
					int len = physIdx-gapEnd;
					moveDataWithGap(src, dst, len);
					physIdx--;
					gapSize--;
					gapIndex = index;
					gapStart += len;
					if (gapStart >= values.length) {
						gapStart -= values.length;
					}

					if (index == 0) {
						start = physIdx;
						if ((gapStart + gapSize) % values.length == start) {
							end = gapStart;
							gapSize = 0;
						}
					}
				} else {
					int src = physIdx;
					int dst = physIdx+gapSize;
					int len = gapStart-physIdx;
					moveDataWithGap(src, dst, len);
					gapSize--;
					gapStart = physIdx+1;
					gapIndex = index+1;

					if (index == 0) {
						start = physIdx;
						end = physIdx;
					} else if (index == size) {
						if ((gapStart + gapSize) % values.length == start) {
							end = gapStart;
							gapSize = 0;
						}
					}
				}
			}
		}

		values[physIdx] = elem;
		size++;

		if (DEBUG_DUMP) debugDump();
		if (DEBUG_CHECK) debugCheck();

		return true;
	}

	/**
	 * Move a range of elements in the values array and adjust the gap.
	 * The elements are first copied and the source range is then
	 * filled with null.
	 *
	 * @param src	start index of source range
	 * @param dst	start index of destination range
	 * @param len	number of elements to move
	 */
	private void moveDataWithGap(int src, int dst, int len) {
		if (DEBUG_TRACE) {
			debugLog("moveGap: " + src + "-" + src+len + " -> " + dst + "-" + dst+len);
		}

		if (src > values.length) {
			src -= values.length;
		}
		if (dst > values.length) {
			dst -= values.length;
		}
		assert(len >= 0);
		assert(src+len <= values.length);

		if (start >= src && start < src+len) {
			start += dst-src;
			if (start >= values.length) {
				start -= values.length;
			}
		}
		if (end >= src && end < src+len) {
			end += dst-src;
			if (end >= values.length) {
				end -= values.length;
			}
		}
		if (dst+len <= values.length) {
			moveData(src, dst, len);
		} else {
			// Destination range overlaps end of range so do the
			// move in two calls
			int len2 = dst+len - values.length;
			int len1 = len - len2;
			if (!(src <= len2 && len2 < dst)) {
				moveData(src+len1, 0, len2);
				moveData(src, dst, len1);
			} else {
				moveData(src, dst, len1);
				moveData(src+len1, 0, len2);
			}
		}
	}

	/**
	 * Move a range of elements in the values array.
	 * The elements are first copied and the source range is then
	 * filled with null.
	 *
	 * @param src	start index of source range
	 * @param dst	start index of destination range
	 * @param len	number of elements to move
	 */
	private void moveData(int src, int dst, int len) {
		if (DEBUG_TRACE) {
			debugLog("moveData: " + src + "-" + src+len + " -> " + dst + "-" + dst+len);
			if (DEBUG_DUMP) {
				debugLog(debugPrint(values));
			}
		}
		System.arraycopy(values, src, values, dst, len);

		// Write null into array slots which are not used anymore
		// This is necessary to allow GC to reclaim non used objects.
		int start;
		int end;
		if (src <= dst) {
			start = src;
			end = (dst < src+len) ? dst : src+len;
		} else {
			start = (src > dst+len) ? src : dst+len;
			end = src+len;
		}
		// Inline of Arrays.fill
		assert(end-start <= len);
		for (int i=start; i<end; i++) {
			values[i] = null;
		}

		if (DEBUG_TRACE) {
			if (DEBUG_DUMP) {
				debugLog(debugPrint(values));
			}
		}
	}

	@Override
	public E remove(int index) {
		checkIndex(index);

		if (DEBUG_TRACE) {
			debugLog("remove: " + index);
			if (DEBUG_DUMP) debugDump();
		}
		return doRemove(index);
	}

	@Override
	protected E doRemove(int index) {
		int physIdx;

		// Remove at last position
		if (index == size-1) {
			if (DEBUG_TRACE) debugLog("Case R0");

			end--;
			if (end < 0) {
				end += values.length;
			}
			physIdx = end;

			// Remove gap if it is followed by only one element
			if (gapSize > 0) {
				if (gapIndex == index) {
					// R0-1
					end = gapStart;
					gapSize = 0;
				}
			}

		// Remove at first position
		} else if (index == 0) {
			if (DEBUG_TRACE) debugLog("Case R1");

			physIdx = start;
			start++;
			if (start >= values.length) {
				// R1-1
				start -= values.length;
			}

			// Remove gap if if it is preceded by only one element
			if (gapSize > 0) {
				if (gapIndex == 1) {
					start += gapSize;
					if (start >= values.length) {
						// R1-2
						start -= values.length;
					}
					gapSize = 0;
				} else {
					gapIndex--;
				}
			}

		} else {
			// Remove in middle of list
			physIdx = physIndex(index);

			// Create gap
			if (gapSize == 0) {
				if (DEBUG_TRACE) debugLog("Case R2");
				gapIndex = index;
				gapStart = physIdx;
				gapSize = 1;

			// Extend existing gap at tail
			} else if (index == gapIndex) {
				if (DEBUG_TRACE) debugLog("Case R3");
				gapSize++;

			// Extend existing gap at head
			} else if (index == gapIndex-1) {
				if (DEBUG_TRACE) debugLog("Case R4");
				gapStart--;
				if (gapStart < 0) {
					gapStart += values.length;
				}
				gapSize++;
				gapIndex--;

			} else {
				// Move existing gap
				assert(gapSize > 0);

				boolean moveLeft;
				int gapEnd = (gapStart+gapSize-1) % values.length + 1;
				if (gapEnd < gapStart) {
					// Gap is at head and tail: check where fewer
					// elements must be moved
					int len1 = physIdx-gapEnd;
					int len2 = gapStart-physIdx-1;
					if (len1 <= len2) {
						if (DEBUG_TRACE) debugLog("Case R5a");
						moveLeft = true;
					} else {
						if (DEBUG_TRACE) debugLog("Case R6a");
						moveLeft = false;
					}

				} else {
					if (physIdx > gapStart) {
						// Existing gap is left of insertion point
						if (DEBUG_TRACE) debugLog("Case R5b");
						moveLeft = true;
					} else  {
						// Existing gap is right of insertion point
						if (DEBUG_TRACE) debugLog("Case R6b");
						moveLeft = false;
					}
				}
				if (moveLeft) {
					int src = gapStart+gapSize;
					int dst = gapStart;
					int len = physIdx-gapEnd;
					moveDataWithGap(src, dst, len);
					gapStart += len;
					if (gapStart >= values.length) {
						gapStart -= values.length;
					}
					gapSize++;

				} else {
					int src = physIdx+1;
					int dst = physIdx+gapSize+1;
					int len = gapStart-physIdx-1;
					moveDataWithGap(src, dst, len);
					gapStart = physIdx;
					gapSize++;
				}
				gapIndex = index;
			}
		}

		E removed = values[physIdx];
		values[physIdx] = null;
		size--;

		if (DEBUG_DUMP) debugDump();
		if (DEBUG_CHECK) debugCheck();
		return removed;
	}

    @SuppressWarnings("unchecked")
	@Override
    protected void doEnsureCapacity(int minCapacity) {
		// Note: Same behavior as in ArrayList.ensureCapacity()
		int oldCapacity = values.length;
		if (minCapacity <= oldCapacity) {
			return;	// do not shrink
		}
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
	    int newCapacity = (oldCapacity*3)/2 + 1;
	    if (newCapacity < minCapacity) {
	    	newCapacity = minCapacity;
    	}

		E[] newValues = (E []) new Object[newCapacity];
		if (size == 0) {
			;
		} else if (start == 0) {
	        System.arraycopy(values, 0, newValues, 0, values.length);

		} else if (start > 0) {
			int grow = newCapacity-values.length;
			newValues = (E []) new Object[newCapacity];
			System.arraycopy(values, 0, newValues, 0, start);
			System.arraycopy(values, start, newValues, start+grow, values.length-start);
			if (gapStart > start && gapSize > 0) {
				gapStart += grow;
			}
			if (end > start) {
				end += grow;
			}
			start += grow;
		}
		if (end == 0 && start == 0 && size != 0) {
			// S1, S6
			end = values.length;
		}
		values = newValues;

		if (DEBUG_DUMP) debugDump();
		if (DEBUG_CHECK) debugCheck();
	}

    /**
     * Trims the capacity of this GapList instance to be the list's current size.
     * An application can use this operation to minimize the storage of an instance.
     */
    @Override
	public void trimToSize() {
        doModify();

    	if (size < values.length) {
    		init(toArray(), size);
    	}
    }

	@Override
	protected <T> void doGetAll(T[] array, int index, int len) {
		int[] physIdx = physIndex(index, index+len);
		int pos = 0;
        for (int i=0; i<physIdx.length; i+=2) {
        	int num = physIdx[i+1] - physIdx[i];
        	System.arraycopy(values, physIdx[i], array, pos, num);
        	pos += num;
        }
        assert(pos == len);
	}


	// --- Serialization ---

    /**
     * Serialize a GapList object.
     *
     * @serialData The length of the array backing the <tt>GapList</tt>
     *             instance is emitted (int), followed by all of its elements
     *             (each an <tt>Object</tt>) in the proper order.
     * @param oos  output stream for serialization
     * @throws 	   IOException if serialization fails
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        // Write out array length
	    int size = size();
        oos.writeInt(size);

        // Write out all elements in the proper order.
        for (int i=0; i<size; i++) {
        	oos.writeObject(doGet(i));
        }
    }

    /**
     * Deserialize a GapList object.
 	 *
     * @param ois  input stream for serialization
     * @throws 	   IOException if serialization fails
     * @throws 	   ClassNotFoundException if serialization fails
     */
    @SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Read in array length and allocate array
        size = ois.readInt();
        values = (E[]) new Object[size];

        // Read in all elements in the proper order.
        for (int i=0; i<size; i++) {
            values[i] = (E) ois.readObject();
        }
    }

    @Override
    public IList<E> doCreate(int capacity) {
    	if (capacity == -1) {
    		capacity = DEFAULT_CAPACITY;
    	}
    	return new GapList<E>(capacity);
    }

    @Override
	protected void doRemoveAll(int index, int len) {
		if (len == size()) {
			doModify();
			doClear();
		} else {
			for (int i=0; i<len; i++) {
				doRemove(index);
			}
		}
	}

    @Override
    public void sort(int index, int len, Comparator<? super E> comparator) {
    	checkRange(index, len);

    	normalize();
    	Arrays.sort(values, index, index+len, comparator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K> int binarySearch(int index, int len, K key, Comparator<? super K> comparator) {
    	checkRange(index, len);

    	normalize();
    	return Arrays.binarySearch((Object[]) values, index, index+len, key, (Comparator<Object>) comparator);
    }

	// --- Helper methods for debugging ---

	/**
	 * Private method to check invariant of GapList.
	 * It is only used for debugging.
	 */
	private void debugCheck() {
		// If the GapList is not used for storing content in KeyListImpl, values may be null
		if (values == null) {
			assert(size == 0 && start == 0 && end == 0);
			assert(gapSize == 0 && gapStart == 0 && gapIndex == 0);
			return;
		}

		assert(size >= 0 && size <= values.length);
		assert(start >=0 && (start < values.length || values.length == 0));
		assert(end >= 0 && (end < values.length || values.length == 0));
		assert(values.length == 0 || (start+size+gapSize) % values.length == end);

		// Check that logical gap index is correct
		assert(gapSize >= 0);
		if (gapSize > 0) {
			assert(gapStart >= 0 && gapStart < values.length);
			// gap may not be at start or end
			assert(gapIndex > 0 && gapIndex < size);
			// gap start may not be the same as start or end
			assert(gapStart != start && gapStart != end);
			// check that logical and phyiscal gap index are correct
			assert(physIndex(gapIndex) == (gapStart+gapSize) % values.length);
		}

		// Check that gap positions contain null values
		if (gapSize > 0) {
			for (int i=gapStart; i<gapStart+gapSize; i++) {
				int pos = (i % values.length);
				assert(values[pos] == null);
			}
		}

		// Check that all end positions contain null values
		if (start < end) {
			for (int i=0; i<start; i++) {
				assert(values[i] == null);
			}
			for (int i=end; i<values.length; i++) {
				assert(values[i] == null);
			}
		} else if (end < start) {
			for (int i=end; i<start; i++) {
				assert(values[i] == null);
			}
		}
	}

	/**
	 * Private method to determine state of GapList.
	 * It is only used for debugging.
	 *
	 * @return	state in which GapList is
	 */
	private int debugState() {
		if (size == 0) {
			return 0;
		} else if (size == values.length) {
			return 1;
		} else if (gapSize == 0) {
			if (start == 0) {
				return 2;
			} else if (end == 0) {
				return 3;
			} else if (start < end) {
				return 4;
			} else if (start > end) {
				return 5;
			}
		} else if (gapSize > 0) {
			if (start == end) {
				if (start == 0) {
					return 6;
				} else if (gapStart < start) {
					return 7;
				} else if (gapStart > start) {
					int gapEnd = (gapStart+gapSize) % values.length;
					if (gapEnd > gapStart) {
						return 8;
					} else if (gapEnd < gapStart) {
						return 9;
					}
				}
			} else if (start != end) {
				if (start == 0) {
					return 10;
				} else if (gapStart < start) {
					return 14; //
				} else if (gapStart > start) {
					int gapEnd = (gapStart+gapSize) % values.length;
					if (gapEnd < gapStart) {
						return 12;
					} else {
						if (end == 0) {
							return 11;
						} else if (end > start) {
							return 13;
						} else if (end < start) {
							return 15;
						}
					}
				}
			}
		}
		assert(false);
		return -1;
	}

	/**
	 * Private method to dump fields of GapList.
	 * It is only called if the code is run in development mode.
	 */
	private void debugDump() {
		debugLog("values: size= " +values.length + ", data= "+ debugPrint(values));
		debugLog("size=" + size + ", start=" + start + ", end=" + end +
				", gapStart=" + gapStart + ", gapSize=" + gapSize + ", gapIndex=" + gapIndex);
		debugLog(toString());
	}

	/**
	 * Print array values into string.
	 *
	 * @param values	array with values
	 * @return			string representing array values
	 */
	private String debugPrint(E[] values) {
		StringBuilder buf = new StringBuilder();
		buf.append("[ ");
		for (int i=0; i<values.length; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			buf.append(values[i]);
		}
		buf.append(" ]");
		return buf.toString();
	}


	/**
	 * Private method write logging output.
	 * It is only used for debugging.
	 *
	 * @param msg message to write out
	 */
	private void debugLog(String msg) {
	}

	// --- ImmutableGapList ---

    /**
     * An immutable version of a GapList.
     * Note that the client cannot change the list,
     * but the content may change if the underlying list is changed.
     */
    protected static class ImmutableGapList<E> extends GapList<E> {

        /** UID for serialization */
        private static final long serialVersionUID = -1352274047348922584L;

        /**
         * Private constructor used internally.
         *
         * @param that  list to create an immutable view of
         */
        protected ImmutableGapList(GapList<E> that) {
            super(true, that);
        }

        @Override
        protected boolean doAdd(int index, E elem) {
        	error();
        	return false;
        }

        @Override
        protected E doSet(int index, E elem) {
        	error();
        	return null;
        }

        @Override
        protected E doReSet(int index, E elem) {
        	error();
        	return null;
        }

        @Override
        protected E doRemove(int index) {
        	error();
        	return null;
        }

        @Override
        protected void doRemoveAll(int index, int len) {
        	error();
        }

        @Override
        protected void doClear() {
        	error();
        }

        @Override
        protected void doModify() {
        	error();
        }

        /**
         * Throw exception if an attempt is made to change an immutable list.
         */
        private void error() {
            throw new UnsupportedOperationException("list is immutable");
        }
    }

}
