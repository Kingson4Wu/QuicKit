package com.kxw.quickit;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <a href="https://www.cnblogs.com/zaizhoumo/p/7782941.html">...</a>
 * @param <R>
 */
public class ReadWriteLockWrapper<R> {

    private ReentrantReadWriteLock rwl;

    private ReadWriteLockWrapper(){}

    public static <R> ReadWriteLockWrapper<R> newLock() {
        return ReadWriteLockWrapper.<R>builder().rwl(new ReentrantReadWriteLock()).build();
    }

    protected static <R> Builder<R> builder() {
        return new Builder<>();
    }

    protected static final class Builder<R> {

        private ReentrantReadWriteLock rwl;

        private Builder() {
        }

        public Builder<R> rwl(ReentrantReadWriteLock rwl) {
            this.rwl = rwl;
            return this;
        }


        public ReadWriteLockWrapper<R> build() {
            ReadWriteLockWrapper<R> wrapper = new ReadWriteLockWrapper<>();
            wrapper.rwl = this.rwl;
            return wrapper;
        }
    }

    public R execute(Supplier<R> loadFromRemote, Supplier<R> loadFromCache, Function<R, Boolean> isExist) {
        rwl.readLock().lock();

        R data = loadFromCache.get();

        if (!isExist.apply(data)) {
            // Must release read lock before acquiring write lock
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            // Recheck state because another thread might have acquired
            data = loadFromCache.get();
            if (!isExist.apply(data)) {
                data = loadFromRemote.get();
            }
            // Downgrade by acquiring read lock before releasing write lock
            rwl.readLock().lock();
            rwl.writeLock().unlock(); // Unlock write, still hold read
        }
        rwl.readLock().unlock();
        return data;
    }

}
