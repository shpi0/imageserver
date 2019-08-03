package ru.shpi0.imageserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.shpi0.imageserver.configs.DataInitializer;

/**
 * Create an in-memory cache (for caching Objects) with configurable max size and eviction strategy.
 * Two strategies should be implemented: LRU and LFU.
 * For this task it is assumed that only one thread will access the cache, so there is no need to make it thread-safe.
 * Please provide an example of usage of the cache as a unit test(s).
 */

@SpringBootApplication
@PropertySource("classpath:/application.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(initMethod = "init")
    public DataInitializer initializer() {
        return new DataInitializer();
    }

}
