package com.test.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class ClientStressTest extends com.test.Client.Test {
    private static final int TESTS = 1000;
    private static final long LEFT_LIMIT = 1L;
    private static final long RIGHT_LIMIT = 1000L;

    public ClientStressTest(){
        super(TESTS, LEFT_LIMIT, RIGHT_LIMIT);
    }
    @BeforeEach
    void init(){
        connectToServer();
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
<<<<<<< HEAD
    @DisplayName("testAndSet")
=======
    @DisplayName("TESTANDSET TEST")
>>>>>>> fc813500424880092b531ec2fe7d6a9e89c45896
    @Override
    public void testAndSet() {
        super.testAndSet();
    }
}
