package ru.friends.util;

import org.springframework.util.StopWatch;

public class StopWatchUtils {

    public static StopWatch createAndStart(String taskName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(taskName);
        return stopWatch;
    }

    public static String stopAndPrintSeconds(StopWatch stopWatch) {
        stopWatch.stop();
        return String.format("METRICS: %s: %.1f sec", stopWatch.getLastTaskName(), stopWatch.getTotalTimeSeconds());
    }

}
