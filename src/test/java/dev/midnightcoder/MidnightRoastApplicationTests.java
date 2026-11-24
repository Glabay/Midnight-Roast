package dev.midnightcoder;

import dev.midnightcoder.app.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = Application.class)
class MidnightRoastApplicationTests {

    @Test
    void contextLoads() {
    }

}
