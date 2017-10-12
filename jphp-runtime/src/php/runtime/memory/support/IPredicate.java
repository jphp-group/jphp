/*
 * Copyright 2013 by Thomas Mauch
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
 * $Id: IPredicate.java 2637 2014-12-09 21:48:08Z origo $
 */
package php.runtime.memory.support;

/**
 * A predicate interface for use in pre-Java 8.
 * In Java 8 and later, java.util.function.Predicate could then be used.
 *
 * @param <T>	type
 *
 * @author Thomas Mauch
 * @version $Id: IPredicate.java 2637 2014-12-09 21:48:08Z origo $
 */
public interface IPredicate<T> {
	/**
	 * Evaluates predicate on specified element.
	 *
	 * @param elem	element to evaluate
	 * @return		true if the input matches the predicate, false otherwise
	 */
	boolean test(T elem);
}