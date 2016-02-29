package net.openhft.chronicle.core.pool;

import net.openhft.chronicle.core.Maths;
import net.openhft.chronicle.core.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by peter on 29/02/16.
 */
public class ParsingCache<E> {
    protected final ParsedData<E>[] interner;
    protected final int mask, shift;
    private final Function<String, E> eFunction;
    protected boolean toggle = false;

    public ParsingCache(int capacity, Function<String, E> eFunction) throws IllegalArgumentException {
        this.eFunction = eFunction;
        int n = Maths.nextPower2(capacity, 128);
        shift = Maths.intLog2(n);
        interner = new ParsedData[n];
        mask = n - 1;
    }

    @Nullable
    public E intern(@Nullable CharSequence cs) {
        if (cs == null)
            return null;
        int hash = Maths.hash32(cs);
        int h = hash & mask;
        ParsedData<E> s = interner[h];
        if ( s != null && StringUtils.isEqual(s.string, cs))
            return s.e;
        int h2 = (hash >> shift) & mask;
        ParsedData<E> s2 = interner[h2];
        if (s2 != null && StringUtils.isEqual(s2.string, cs))
            return s2.e;
        String string = cs.toString();
        ParsedData<E> s3 = new ParsedData<>(string, eFunction.apply(string));
        interner[s == null || (s2 != null && toggle()) ? h : h2] = s3;

        return s3.e;
    }

    protected boolean toggle() {
        return toggle = !toggle;
    }

    public int valueCount() {
        return (int) Stream.of(interner).filter(s -> s != null).count();
    }

    static class ParsedData<E> {
        final String string;
        final E e;

        ParsedData(String string, E e) {
            this.string = string;
            this.e = e;
        }
    }
}
