package com.test.Client;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

class ClientStressTest extends com.test.Client.Test {
    private static final int TESTS = 1000;
    private static final long LEFT_LIMIT = 1L;
    private static final long RIGHT_LIMIT = 1000L;

    public ClientStressTest(){
        super(TESTS, LEFT_LIMIT, RIGHT_LIMIT);
    }
    @BeforeAll
    static void init(){
        connectToServer(3000);
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("SET FUNCTION")
    @Override
    public void set() {
        super.set();
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("GET FUNCTION")
    @Override
    public void get() {
        super.get();
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("DEL FUNCTION")
    @Override
    public void del() {
        super.del();
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("DELKEYVERSION TEST")
    @Override
    public void delKeyVersion() {
        super.delKeyVersion();
    }

    @Test
    @RepeatedTest(TESTS)
    @DisplayName("TESTANDSET TEST")
    @Override
    public void testAndSet() {
        super.testAndSet();
    }
}
