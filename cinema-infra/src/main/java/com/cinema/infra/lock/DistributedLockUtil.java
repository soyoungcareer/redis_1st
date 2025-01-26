package com.cinema.infra.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class DistributedLockUtil {
    private final RedissonClient redissonClient;

    public DistributedLockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 락을 획득한 후 안전하게 실행하는 함수형 메서드
     */
    public <T> T executeWithLock(String key, long waitTime, long leaseTime, Supplier<T> task) {
        RLock lock = redissonClient.getLock(key);

        try {
            if (!lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                throw new IllegalStateException("동일 좌석이 이미 예약 중입니다. 나중에 다시 시도해 주세요.");
            }

            return task.get();  // 함수 실행

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중 오류 발생", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}