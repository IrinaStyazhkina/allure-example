package istyazhkina.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import istyazhkina.pages.GitHubPage;
import istyazhkina.utils.AuthConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static istyazhkina.utils.GitHubLabel.*;


class AllureAnnotationsTest {

    private AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    @Test
    @DisplayName("Создание Issue в Github. Использование аннотаций")
    @Feature("GitHub Issues")
    @Story("Create issue")
    @Owner("Irina Styazhkina")
    @Severity(SeverityLevel.NORMAL)
    void createIssueTest() {
        String issueTitle = "Test issue Title";
        String issueComment = "Test issue comment";

        open(GitHubPage.url, GitHubPage.class)
                .login(authConfig.username(), authConfig.password())
                .goToRepository(1)
                .createIssue(issueTitle, issueComment, ENHANCEMENT, GOOD_FIRST_ISSUE)
                .searchForIssue(issueTitle)
                .checkIssue(issueTitle, issueComment, authConfig.username(), ENHANCEMENT, GOOD_FIRST_ISSUE);
    }

    @AfterEach
    void shutDown() {
            Selenide.closeWebDriver();
    }
}
