package istyazhkina.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import istyazhkina.pages.GitHubPage;
import istyazhkina.utils.AuthConfig;
import istyazhkina.utils.GitHubLabel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;


class AllureLambdaTest {

    private AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    @Test
    void createIssueTest() {
        Allure.feature("GitHub Issues");
        Allure.story("Create Issue");

        String issueTitle = "Test issue Title";
        String issueComment = "Test issue comment";
        GitHubLabel[] labels = {GitHubLabel.DUPLICATE, GitHubLabel.DOCUMENTATION};

        step("Open GitHub", () -> {
            open(GitHubPage.url);
        });

        step("Login to GitHub", (step) -> {
            $(".HeaderMenu-link[href='/login']").click();
            step.parameter("Username", authConfig.username());
            $("#login_field").setValue(authConfig.username());
            $("#password").setValue(authConfig.password());
            $("input[name='commit']").click();
        });

        step("Go to Repository", () -> {
            $("summary.Header-link .avatar-user").click();
            $$(".dropdown-item").find(text("Your repositories")).click();
            $$("ul[data-filterable-for='your-repos-filter'] a").get(0).click();
        });

        step("Create issue", (step) -> {
            $("[data-content='Issues']").click();
            $(".repository-content").$(byText("New issue")).click();
            step.parameter("Issue title", issueTitle);
            $("#issue_title").setValue(issueTitle);
            step.parameter("Issue body", issueComment);
            $("#issue_body").setValue(issueComment);
            $(".js-issue-assignees button").click();

            step.parameter("Labels", labels);
            $(".label-select-menu svg").click();
            for (GitHubLabel label : labels) {
                $$(".label-select-menu-item .name").find(text(label.getLabelName())).click();
            }
            $(".discussion-sidebar-heading[data-hotkey='l']").pressEscape();

            $(".timeline-comment-wrapper").$(byText("Submit new issue")).click();
        });

        step("Search for issue", (step) -> {
            $("[data-content='Issues']").click();
            step.parameter("Issue Title", issueTitle);
            $("div[aria-label='Issues']").$(byText(issueTitle)).click();
        });

        step("Check issue", (step) -> {
            step.parameter("Issue Title", issueTitle);
            $(".gh-header-title").shouldHave(text(issueTitle));
            step.parameter("Issue comment", issueComment);
            $(".js-comment-body").shouldHave(text(issueComment));
            step.parameter("Asignee", authConfig.username());
            $(".assignee").shouldHave(text(authConfig.username()));
            step.parameter("Labels", labels);
            SelenideElement labelList = $(".sidebar-labels");
            for (GitHubLabel label : labels) {
                labelList.shouldHave(text(label.getLabelName()));
            }
        });
    }

    @AfterEach
    void shutDown() {
        Selenide.closeWebDriver();
    }
}
