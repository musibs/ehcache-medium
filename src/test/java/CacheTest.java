import io.codefountain.ehcache.CacheUtils;
import org.ehcache.Cache;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class CacheTest {

    @Test
    public void whenAccessInmemoryCacheWithKeyThenGetTheValue(){

        Cache myCache = CacheUtils.buildInMemoryCache("myCache", Integer.class, String.class);
        myCache.put(1, "Medium");
        assertThat((String) myCache.get(1), is("Medium1"));
    }

    @Test
    public void whenAccessPersistentCacheWithKeyThenGetTheValue() {
        Cache myCache = CacheUtils.buildPersistenceStorageCache("myCache", Integer.class, String.class);
        IntStream.range(1, 5000).boxed().forEach((i) -> {
            myCache.put(i, "Medium-" + i);
        });
        assertThat((String) myCache.get(100), is("Medium-" + 100));
    }

    @Test
    public void whenAccessInmemoryCacheWithExpiryThenDataShouldExpire() throws InterruptedException{
        Cache myCache = CacheUtils.buildExpiryEnabledInMemoryCache("myCache", Integer.class, String.class, 2);
        myCache.put(1, "Medium");
        TimeUnit.SECONDS.sleep(4);
        assertNull(myCache.get(1));
    }
}
