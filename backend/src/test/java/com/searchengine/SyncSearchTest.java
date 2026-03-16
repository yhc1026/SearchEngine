package com.searchengine;


import com.searchengine.entity.Repository;
import com.searchengine.service.RepositorySyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootTest(classes = SearchEngineApplication.class)


public class SyncSearchTest {

    @Autowired
    RepositorySyncService repositorySyncService;

    @Test
    void SyncSearchTest() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        List<Repository> list=repositorySyncService.syncByUsername("SPHARX");
        System.out.println(list.get(0).toString());
    }
}
