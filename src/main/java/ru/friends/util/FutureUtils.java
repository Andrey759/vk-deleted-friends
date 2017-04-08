package ru.friends.util;

import com.google.common.base.Throwables;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class FutureUtils {

    public static void checkExceptions(Collection<CompletableFuture> futures) {
        for (CompletableFuture future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw Throwables.propagate(e.getCause());
            }
        }
    }

}
