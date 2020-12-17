package com.test.Client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class ClientTest extends com.test.Client.Test {
    private static final int TESTS = 10;
    private static final long LEFT_LIMIT = 1L;
    private static final long RIGHT_LIMIT = 20L;

    public ClientTest(){
        super(TESTS, LEFT_LIMIT, RIGHT_LIMIT);
    }

    @BeforeAll
    static void init(){
        connectToServer(3500);
    }

    @Test
    @RepeatedTest(TESTS)
    @Override
    public void set() {
        super.set();
    }

    @Test
    @RepeatedTest(TESTS)
    @Override
    public void get() {
        super.get();
    }

    @Test
    @RepeatedTest(TESTS)
    @Override
    public void del() {
        super.del();
    }

    @Test
    @RepeatedTest(TESTS)
    @Override
    public void delKeyVersion() {
        super.delKeyVersion();
    }

    @Test
    @RepeatedTest(TESTS)
    @Override
    public void testAndSet() {
        super.testAndSet();
    }
}
