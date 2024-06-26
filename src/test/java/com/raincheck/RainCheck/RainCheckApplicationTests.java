package com.raincheck.RainCheck;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RainCheckApplicationTests {

    @Test
    public void applicationContextLoaded() {
    }

    @Test
    public void applicationContextTest() {
        RainCheckApplication.main(new String[] {});
    }

}
