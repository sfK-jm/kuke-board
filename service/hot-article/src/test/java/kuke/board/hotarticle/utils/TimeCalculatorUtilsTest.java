package kuke.board.hotarticle.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

class TimeCalculatorUtilsTest {

    @Test
    void test() throws Exception {
        Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();
        System.out.println("duration.getSeconds() / 60 = " + duration.getSeconds() / 60);
    }
}