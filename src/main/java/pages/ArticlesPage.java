package pages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.val;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class ArticlesPage {

    private final static String URL = "https://demo.realworld.io";

    private SelenideElement popularTags = $("div.tag-list");
    private SelenideElement feedTabs = $("ul.nav-pills");
    private SelenideElement articles = $("article-list");
    private SelenideElement pagination = $(".pagination");
    private SelenideElement pageLoader = $x("//div[contains(text(),'Loading articles...')]");


    public ArticlesPage open() {
        Selenide.open(URL);
        return this;
    }

    private List<SelenideElement> getPopularTags() {
        return popularTags.shouldBe(Condition.visible).$$("a");
    }

    @Step("Get 'Popular tags'")
    public List<String> getPopularTagsTexts() {
        return getPopularTags()
                .stream()
                .map(tag -> tag.innerText().replace("\u200c", "&zwnj;"))
                .collect(Collectors.toList());
    }

    @Step("Click on tag by index {0}")
    public ArticlesPage clickTag(int index) throws IndexOutOfBoundsException{
        getPopularTags().get(index).click();
        pageLoader.shouldBe(Condition.disappear);
        return this;
    }

    @Step("Click on tag by text {0}")
    public ArticlesPage clickTag(String tagText) {
        val matchedTags = getPopularTags().stream()
                .filter(tag -> tag.innerText().replace("\u200c", "&zwnj;").equals(tagText))
                .collect(Collectors.toList());

        if(matchedTags.size() == 1) {
            matchedTags.get(0).click();
        } else if (matchedTags.isEmpty()) {
            throw new IllegalStateException("Error: there are no tags with text" + tagText);
        } else {
            throw new IllegalStateException("Error: there are more than one tag with text" + tagText);
        }
        pageLoader.shouldBe(Condition.disappear);
        return this;
    }

    public List<SelenideElement> getArticles() {
        return articles
                .shouldBe(Condition.visible)
                .$$("a.preview-link");
    }

    @Step("Get random article from the page")
    public SelenideElement getRandomArticle() {
        val articles = getArticles();
        if (articles.size() > 0) {
            Random random = new Random();
            val randomIndex = random.nextInt(articles.size());
            val randomArticle = articles.get(randomIndex).scrollTo();
            return randomArticle;
        } else throw new IllegalStateException("No articles present");
    }

    @Step("Get a title of a given article")
    public String getTitleOf(SelenideElement article) {
        return article.$("h1").getText();
    }

    @Step("Get list of tags of a given article")
    public List<String> getTagsOf(SelenideElement article) {
        return article
                .$$("ul li")
                .stream()
                .map(tag -> tag.innerText().replace("\u200c", "&zwnj;").trim())
                .collect(Collectors.toList());
    }

    public List<SelenideElement> getPagination() {
        return pagination
                .scrollTo()
                .shouldBe(Condition.visible)
                .$$("a");
    }


    @Step("Go to random page and return its number")
    public int goToRandomPage() {
        val pageBtns = getPagination();
        Random random = new Random();
        val randomIndex = random.nextInt(pageBtns.size());
        pageBtns.get(randomIndex).click();
        pageLoader.shouldBe(Condition.disappear);
        return randomIndex + 1;
    }

    @Step("Get name of the active tab")
    public String getActiveFeedTab() {
        return feedTabs
                .$("a.active")
                .innerText()
                .replace("\u200c", "&zwnj;")
                .trim();
    }
}