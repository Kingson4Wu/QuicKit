package com.kxw.quickit;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReadWriteLockWrapperTest {

    @Test
    public void execute() {
        ReadWriteLockWrapper<String> lockWrapper = ReadWriteLockWrapper.newLock();

        Supplier<String> loadFromRemote = () -> {
            return "OK";
        };
        Supplier<String> loadFromCache = () -> {
            return "OK";
        };
        Function<String, Boolean> isExist = Objects::nonNull;

        String result = lockWrapper.execute(loadFromRemote, loadFromCache, isExist);

        Assert.assertEquals(result, "OK");

    }

    @Test
    public void execute2() {
        ReadWriteLockWrapper<List<String>> lockWrapper = ReadWriteLockWrapper.newLock();

        Supplier<List<String>> loadFromRemote = () -> Collections.singletonList("fff");
        Supplier<List<String>> loadFromCache = Collections::emptyList;
        Function<List<String>, Boolean> isExist = list -> list != null && !list.isEmpty();

        List<String> result = lockWrapper.execute(loadFromRemote, loadFromCache, isExist);

        Assert.assertEquals(result.get(0), "fff");

    }
}
