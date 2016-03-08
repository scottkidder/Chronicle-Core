/*
 *     Copyright (C) 2015  higherfrequencytrading.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.openhft.chronicle.core.values;

/**
 * User: peter.lawrey Date: 10/10/13 Time: 07:11
 */
public interface LongValue {
    long getValue();

    void setValue(long value);

    long getVolatileValue();

    void setOrderedValue(long value);

    long addValue(long delta);

    long addAtomicValue(long delta);

    boolean compareAndSwapValue(long expected, long value);

    default void setMaxValue(long value) {
        for (; ; ) {
            long pos = getVolatileValue();
            if (pos >= value)
                break;
            if (compareAndSwapValue(pos, value))
                break;
        }
    }

    default void setMinValue(long value) {
        for (; ; ) {
            long pos = getVolatileValue();
            if (pos <= value)
                break;
            if (compareAndSwapValue(pos, value))
                break;
        }
    }
}
