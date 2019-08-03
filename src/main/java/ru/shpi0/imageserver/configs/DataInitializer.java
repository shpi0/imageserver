package ru.shpi0.imageserver.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shpi0.imageserver.cache.Cache;
import ru.shpi0.imageserver.model.Avatar;
import ru.shpi0.imageserver.repository.AvatarRepositoryImpl;
import ru.shpi0.imageserver.service.AvatarService;
import ru.shpi0.imageserver.util.CacheFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DataInitializer {

    private static Logger logger = LoggerFactory.getLogger(AvatarRepositoryImpl.class);

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private CacheFactory<Avatar> cacheFactory;

    public void init() {
        logger.info("Start init() method");

        if (avatarService.getCount() == 0) {
            logger.debug("Filling database with images");
            try {
                Files.find(Paths.get("./icons"),
                        Integer.MAX_VALUE,
                        (filePath, fileAttr) -> fileAttr.isRegularFile())
                        .forEach(file -> {
                            try {
                                avatarService.save(new Avatar(Files.readAllBytes(file)));
                            } catch (Exception e) {
                                logger.error("Error during reading from file '{}'", file.toAbsolutePath().toString(), e);
                            }
                        });
            } catch (Exception e) {
                logger.error("Error during init() method", e);
            }
        }

        Cache<Avatar> cache = cacheFactory.getCache();
        logger.debug("Warming cache with {} values", cache.getCapacity());
        for (int i = 1; i <= cache.getCapacity(); i++) {
            avatarService.getById(i);
        }

    }

}
