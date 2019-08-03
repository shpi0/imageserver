import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shpi0.imageserver.Application;
import ru.shpi0.imageserver.cache.Cache;
import ru.shpi0.imageserver.model.Avatar;
import ru.shpi0.imageserver.service.AvatarService;
import ru.shpi0.imageserver.util.CacheFactory;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"data.cache.size=10", "data.cache.type=LRU"})
@ContextConfiguration(classes = {Application.class})
public class AvatarServiceLruCachingTest {

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private CacheFactory<Avatar> cacheFactory;

    @Test
    public void checkLruCache() {
        Cache<Avatar> cache = cacheFactory.getCache();
        List<Long> expectingKeys = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            avatarService.getById(i);
        }

        for (int i = 91; i <= 100; i++) {
            expectingKeys.add((long) i);
        }

        Assert.assertArrayEquals(expectingKeys.toArray(), cache.getCacheState().toArray());

        avatarService.getById(200);
        avatarService.getById(300);
        avatarService.getById(100);
        expectingKeys.remove(0);
        expectingKeys.remove(0);
        expectingKeys.remove(expectingKeys.size() - 1);
        expectingKeys.add(200L);
        expectingKeys.add(300L);
        expectingKeys.add(100L);

        Assert.assertArrayEquals(expectingKeys.toArray(), cache.getCacheState().toArray());
    }

}
