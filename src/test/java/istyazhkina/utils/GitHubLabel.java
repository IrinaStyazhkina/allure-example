package istyazhkina.utils;

public enum GitHubLabel {

    BUG("bug"),
    DOCUMENTATION("documentation"),
    DUPLICATE("duplicate"),
    ENHANCEMENT("enhancement"),
    GOOD_FIRST_ISSUE("good first issue"),
    HELP_WANTED("help wanted"),
    INVALID("invalid"),
    QUESTION("question"),
    WONTFIX("wontfix");

    private String labelName;

    GitHubLabel(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }
}
