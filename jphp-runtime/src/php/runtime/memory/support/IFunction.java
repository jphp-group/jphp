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
 * $Id: IFunction.java 2673 2015-01-06 01:11:54Z origo $
 */
package php.runtime.memory.support;

/**
 * A function interface for use in pre-Java 8.
 * In Java 8 and later, java.util.function.Function could then be used.
 *
 * @author Thomas Mauch
 * @version $Id: IFunction.java 2673 2015-01-06 01:11:54Z origo $
 *
 * @param <T> type of input argument
 * @param <R> type of return value
 */
public interface IFunction<T,R> {
    /**
     * Returns key for given value.
     *
     * @param elem 	element to apply function to
     * @return  	result of function evaluation
     */
    public R apply(T elem);
}
