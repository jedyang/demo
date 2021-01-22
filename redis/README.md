## Redis使用过程中遇到的问题
### Redisson
#### Redisson提供的锁
Redisson中最常用的是可重入锁re-entrant Lock
也提供了其他的各种锁实现
联锁（MultiLock），读写锁（ReadWriteLock），公平锁（Fair Lock），红锁（RedLock），信号量（Semaphore），可过期性信号量（PermitExpirableSemaphore）和闭锁（CountDownLatch）

```
RLock lock = redisson.getLock(lockKey);
try {
    // 设置锁过期时间
    lock.lock(30, TimeUnit.SECONDS);
    // 业务操作
} catch (Exception ex) {
    ex.printStackTrace();
} finally {
    if (lock.isLocked()) {
        lock.unlock();
    }
}
```
锁过期时间不能太短，最好比99%的业务场景都长一点。

lock.tryLock成功和不成功都会立刻返回。lock.lock会等待获取锁。