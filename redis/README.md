## Redis使用过程中遇到的问题
官方权威文档 https://github.com/redisson/redisson/wiki/Table-of-Content  
其实只要看完官方文档网上99%的内容都不用看了，包括下面的内容。
### Redisson
#### Redisson提供的锁
Redisson中最常用的是可重入锁re-entrant Lock
也提供了其他的各种锁实现
联锁（MultiLock），读写锁（ReadWriteLock），公平锁（Fair Lock)，信号量（Semaphore），可过期性信号量（PermitExpirableSemaphore）和闭锁（CountDownLatch）
redisson实现上使用lua脚本保障加锁和释放锁的原子性。
#### getLock
redisson提供的RLock，继承了JUC包的Lock。也就是说我们可以像使用JUC的lock一样使用RLock。使用方法一样，只是RLock是分布式锁。
lock.lock()和lock(long leaseTime, TimeUnit unit)区别：
无参的lock(), 会使用默认的锁超时时间30秒。
redisson内部会使用看门狗机制自动进行续期，续期时间是三分之一的锁超时时间即10s。
也就是说在锁的ttl到20s时，如果业务还没有执行完，redisson会自动发请求给redis，将锁的key过期时间设置为30s
这就是redisson的自动续期。
lock(long leaseTime, TimeUnit unit)要注意的是，我们自己设置了过期时间之后。redisson不会再自动续期。

建议的用法：
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
为什么还是建议使用这种不会自动续期的用法呢？
因为如果一个业务要执行超过10s，那么大概率是出了异常。比如数据库连不上了等等。
这个时候不断续期，也解决不了问题。反而不断堆积请求。
所以我们还是自己设置锁时间，
只是把锁过期时间设的稍微长点，最好比99%的业务场景都长一点。
另外，如果同步的代码时间超过10s，我们绝对要分析代码写的是不是有问题，加锁的代码段是不是过长。

还有个lock.tryLock方法，tryLock是成功和不成功都会立刻返回。lock.lock会等待获取锁。
对应的，boolean tryLock(long waitTime, long leaseTime, TimeUnit unit)也有一个带参数的方法。可以设置等待多长时间获取锁。

- getLock获取的锁是非公平锁
- getFairLock获取的是公平锁

#### 读写锁
```
RReadWriteLock rwlock = redisson.getReadWriteLock("myLock");

RLock lock = rwlock.readLock();
// or
RLock lock = rwlock.writeLock();

// traditional lock method
lock.lock();
```
并发读，排他写。
补充说明，如果当前是写锁，那么读锁必须等待。这个很好理解。  
如果当前是读锁。写锁也必须等待。  
如果当前是读锁，其他的读操作可并发进行。  

#### 信号量
加一减一操作
```
   RSemaphore semaphore = redisson.getSemaphore("mySemaphore");
   
   // acquire single permit
   semaphore.acquire();
   
   // or acquire 10 permits
   semaphore.acquire(10);
   
   // or try to acquire permit
   boolean res = semaphore.tryAcquire();
   
   // or try to acquire permit or wait up to 15 seconds
   boolean res = semaphore.tryAcquire(15, TimeUnit.SECONDS);
   
   // or try to acquire 10 permit
   boolean res = semaphore.tryAcquire(10);
   
   // or try to acquire 10 permits or wait up to 15 seconds
   boolean res = semaphore.tryAcquire(10, 15, TimeUnit.SECONDS);
   if (res) {
      try {
        ...
      } finally {
          semaphore.release();
      }
   }
   ```

#### 闭锁CountDownLatch
用法举例：
```
   RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");
   
   latch.trySetCount(5);
   // await for count down
   latch.await();
   
   // in other thread or JVM
   RCountDownLatch latch = redisson.getCountDownLatch("myCountDownLatch");
   latch.countDown();
 ```
 
和juc中用法一样

### redisTemplate

