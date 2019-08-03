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
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"data.cache.size=10", "data.cache.type=LFU"})
@ContextConfiguration(classes = {Application.class})
public class AvatarServiceLfuCachingTest {

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private CacheFactory<Avatar> cacheFactory;

    @Test
    public void checkLruCache() {
        Cache<Avatar> cache = cacheFactory.getCache();
        List<Long> expectingKeys = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < i; j++) {
                avatarService.getById(i);
            }
        }
        avatarService.getById(25);
        avatarService.getById(25);
        avatarService.getById(25);

        for (int i = 2; i <= 10; i++) {
            expectingKeys.add((long) i);
        }
        expectingKeys.add((long) 25);

        Assert.assertArrayEquals(expectingKeys.stream().sorted().toArray(Long[]::new), cache.getCacheState().stream().sorted().toArray(Long[]::new));

        avatarService.getById(30);
        expectingKeys.remove(0);
        expectingKeys.add((long) 30);

        Assert.assertArrayEquals(expectingKeys.stream().sorted().toArray(Long[]::new), cache.getCacheState().stream().sorted().toArray(Long[]::new));

        avatarService.getById(1);
        avatarService.getById(2);
        avatarService.getById(30);

        Assert.assertArrayEquals(expectingKeys.stream().sorted().toArray(Long[]::new), cache.getCacheState().stream().sorted().toArray(Long[]::new));

    }

    private void printDebugMessage(Long[] expecting, Long[] actual) {
        System.out.println("Debug start");
        System.out.println("Expect: " + Arrays.toString(expecting));
        System.out.println("Actual: " + Arrays.toString(actual));
        System.out.println("Debug end");
    }

}
