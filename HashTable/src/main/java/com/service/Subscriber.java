package com.service;

import com.utils.ValueHandler;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public interface Subscriber {
    void update(ConcurrentHashMap<BigInteger, ValueHandler> storage);
}
