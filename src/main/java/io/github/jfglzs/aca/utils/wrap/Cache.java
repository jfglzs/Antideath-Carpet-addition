package io.github.jfglzs.aca.utils.wrap;

public class Cache<T> {
    public T value;
    public int expireTime;

    public Cache(T value, int expireTime) {
        this.value = value;
        this.expireTime = expireTime;
    }

    public void tick() {
        this.expireTime--;
    }

    public boolean isExpired() {
        return this.expireTime <= 0;
    }

    public T getValue() {
        return this.value;
    }
}
