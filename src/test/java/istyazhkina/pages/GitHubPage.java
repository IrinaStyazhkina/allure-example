package istyazhkina.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import istyazhkina.utils.GitHubLabel;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GitHubPage {

    public static final String url = "https://github.com/";

    private SelenideElement
            signInButton = $(".HeaderMenu-link[href='/login']"),
            loginInput = $("#login_field"),
            passwordInput = $("#password"),
            loginButton = $("input[name='commit']"),
            avatarDropDown = $("summary.Header-link .avatar-user"),
            repositoriesLink = $$(".dropdown-item").find(text("Your repositories")),
            issuesButton = $("[data-content='Issues']"),
            newIssueButton = $(".repository-content").$(byText("New issue")),
            issueTitle = $("#issue_title"),
            issueComment = $("#issue_body"),
            assignYourselfButton = $(".js-issue-assignees button"),
            labelsDropDown = $(".label-select-menu svg"),
            submitIssueButton = $(".timeline-comment-wrapper").$(byText("Submit new issue"));

    private ElementsCollection repos = $$("ul[data-filterable-for='your-repos-filter'] a"),
            labelElements = $$(".label-select-menu-item .name");


    @Step("Login to GitHub with username {username}")
    public GitHubPage login(String username, String password) {
        signInButton.click();
        loginInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
        return this;
    }

    @Step("Go to repository")
    public GitHubPage goToRepository(int repositoryNumber) {
        avatarDropDown.click();
        repositoriesLink.click();
        repos.get(repositoryNumber - 1).click();
        return this;
    }

    @Step("Create issue with title: {title}, comment: {comment}, labels: {labels}")
    public GitHubPage createIssue(String title, String comment, GitHubLabel... labels){
        issuesButton.click();
        newIssueButton.click();
        issueTitle.setValue(title);
        issueComment.setValue(comment);
        assignYourselfButton.click();
        setLabels(labels);
        submitIssueButton.click();
        return this;
    }

    @Step("Search for issue with title; {title}")
    public GitHubPage searchForIssue(String title) {
        $("[data-content='Issues']").click();
        $("div[aria-label='Issues']").$(byText(title)).click();
        return this;
    }

    @Step("Check created issue with title: {title}, comment: {comment}, username {username}, labels: {labels}")
    public GitHubPage checkIssue(String title, String comment, String username, GitHubLabel... labels) {
        $(".gh-header-title").shouldHave(text(title));
        $(".js-comment-body").shouldHave(text(comment));
        $(".assignee").shouldHave(text(username));
        checkLabels(labels);
        return this;
    }

    private void setLabels(GitHubLabel... labels) {
        labelsDropDown.click();
        for (GitHubLabel label : labels) {
            labelElements.find(text(label.getLabelName())).click();
        }
        $(".discussion-sidebar-heading[data-hotkey='l']").pressEscape();
    }

    private void checkLabels(GitHubLabel... labels) {
        SelenideElement labelList = $(".sidebar-labels");
        for (GitHubLabel label : labels) {
            labelList.shouldHave(text(label.getLabelName()));
        }
    }
}
