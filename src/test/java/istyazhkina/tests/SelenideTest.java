package istyazhkina.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
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
import static istyazhkina.utils.NamedBy.css;

class SelenideTest {

    private AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    @Test
    void createIssueTest() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        String issueTitle = "Test issue Title";
        String issueComment = "Test issue comment";
        GitHubLabel[] labels = {GitHubLabel.HELP_WANTED, GitHubLabel.ENHANCEMENT};

        open(GitHubPage.url);

        $(css(".HeaderMenu-link[href='/login']").as("Sign In Button")).click();
        $(css("#login_field").as("Login Input")).setValue(authConfig.username());
        $(css("#password").as("Password Input")).setValue(authConfig.password());
        $(css("input[name='commit']").as("Login Button")).click();

        $(css("summary.Header-link .avatar-user").as("Dropdown Collapser")).click();
        $$(css(".dropdown-item").as("Dropdown items")).find(text("Your repositories")).click();
        $$(css("ul[data-filterable-for='your-repos-filter'] a").as("Repositories list")).get(0).click();

        $(css("[data-content='Issues']").as("Issues")).click();
        $(".repository-content").$(byText("New issue")).click();
        $("#issue_title").setValue(issueTitle);
        $("#issue_body").setValue(issueComment);
        $(css(".js-issue-assignees button").as("Self-assign")).click();

        $(css(".label-select-menu svg").as("Labels collapser")).click();
        for (GitHubLabel label : labels) {
            $$(css(".label-select-menu-item .name").as("Labels")).find(text(label.getLabelName())).click();
        }
        $(css(".discussion-sidebar-heading[data-hotkey='l']").as("Labels Escape")).pressEscape();
        $(".timeline-comment-wrapper").$(byText("Submit new issue")).click();

        $(css("[data-content='Issues']").as("Issues")).click();
        $(css("div[aria-label='Issues']").as("Issues List")).$(byText(issueTitle)).click();

        $(css(".gh-header-title").as("Issue Title")).shouldHave(text(issueTitle));
        $(css(".js-comment-body").as("Issue Body")).shouldHave(text(issueComment));
        $(".assignee").shouldHave(text(authConfig.username()));
        SelenideElement labelList = $(".sidebar-labels");
        for (GitHubLabel label : labels) {
            labelList.shouldHave(text(label.getLabelName()));
        }
    }

    @AfterEach
    void shutDown() {
        Selenide.closeWebDriver();
    }
}
