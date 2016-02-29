package net.openhft.chronicle.core.pool;

import org.junit.Test;

import static junit.framework.Assert.assertSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

/**
 * Created by skidder on 2/29/16.
 */
public class ParsingCacheTest {

    @Test
    public void testParsingStrToInt() throws Exception {
        ParsingCache<Integer> cache = new ParsingCache<>(4096, Integer::valueOf);

        Integer value = cache.intern("20160229");
        assertEquals(20160229, (int) value);

        assertSame(value, cache.intern("20160229"));
        assertNotSame(value, cache.intern("20160230"));

        for (int i = 0; i < 2048; i++) {
            Integer v = cache.intern(String.valueOf(i));
        }
        assertSame(value, cache.intern("20160229"));

        // overflow the cache
        for (int i = 2048; i < 4096; i++) {
            Integer v = cache.intern(String.valueOf(i));
        }

        // new instance
        assertNotSame(value, cache.intern("20160229"));

        // check again
        value = cache.intern("20160229");
        assertSame(value, cache.intern("20160229"));
    }
}