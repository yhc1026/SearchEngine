package com.searchengine;


import com.searchengine.entity.Repository;
import com.searchengine.mapper.RepositoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqlTest {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Test
    public void insertRepository() {
        Repository repository = new Repository();
        repository.setRepositoryId(10000L);
        repository.setFullName("test-repository");
        repository.setHtmlUrl("www.test-repository.com");
        repository.setReadme("Hello World");
        repositoryMapper.insertRepository(repository);
    }
}
