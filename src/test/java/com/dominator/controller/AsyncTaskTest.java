package com.dominator.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AsyncTaskTest {

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void doTask1() throws Exception {
        for (int i = 0; i < 100; i++) {
            asyncTask.doTask1(i);
        }

        log.info("All tasks finished.");
        log.error("All tasks finished.---error");
    }
}