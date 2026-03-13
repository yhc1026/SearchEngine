package com.searchengine;

import com.searchengine.controller.GiteeSearchController;
import com.searchengine.dto.gitee.GiteeReadme;
import com.searchengine.dto.gitee.GiteeRepository;
import com.searchengine.dto.gitee.GiteeUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Gitee 搜索 API 测试。
 * 注意：JUnit 测试方法不能使用 @RequestParam，参数需在方法内直接写死或通过成员传入。
 */
@SpringBootTest(classes = SearchEngineApplication.class)
class GitApiTest {

    @Autowired
    private GiteeSearchController giteeSearchController;

    @Test
    public void searchUsers() {
        String keyword = "yhc1026";
        List<GiteeUser> list = giteeSearchController.searchUsers(keyword);
        for( GiteeUser giteeUser : list ) {
            String name = giteeUser.getName();
            Long Id = giteeUser.getId();
            System.out.println("姓名：" + name + " id：" + Id);
        }
    }

    @Test
    void searchUserRepo() {
        String keyword = "青苗";
        List<GiteeRepository> repos = giteeSearchController.searchRepositories(keyword);
        // 注意：Service 在无结果或异常时返回空列表，不会返回 null
        assertNotNull(repos, "返回列表不应为 null");
        System.out.println("查询到仓库数量：" + repos.size());
        for (GiteeRepository repo : repos) {
            String url = repo.getHtmlUrl();
            System.out.println("仓库地址：" + url);
        }
    }

    @Test
    void searchGroupRepo() {
        String keyword = "aizuda";
        List<GiteeRepository> repos= giteeSearchController.getOrgRepos(keyword);
        assertNotNull(repos, "返回列表不应为 null");
        System.out.println("查询到仓库数量：" + repos.size());
        for (GiteeRepository repo : repos) {
            String url = repo.getHtmlUrl();
            System.out.println("仓库地址：" + url);
        }
    }

    @Test
    void searchReadmeRepo() {
        String name = "dromara";
        String url = "warm-flow-test";
        GiteeReadme readme= giteeSearchController.getRepoReadme(name, url);
        System.out.println(readme.toString());
    }
}
