package org.nuaa.tomax.harcs.exception;

/**
 * @Name: RedisException
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-02 15:37
 * @Version: 1.0
 */
public class RedisException extends Exception{
    public RedisException() {
        super();
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    protected RedisException(String message, Throwable cause, boolean enableSupression, boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }
}
