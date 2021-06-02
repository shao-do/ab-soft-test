package pages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.disappear;
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

    @Step("Get list of 'Popular tags'")
    public List<String> getPopularTagsTexts() {
        return getPopularTags()
                .stream()
                .map(tag -> tag.innerText().replace("\u200c", "&zwnj;"))
                .collect(Collectors.toList());
    }

    @Step("Click on tag by index {0}")
    public ArticlesPage clickTag(int index) throws IndexOutOfBoundsException{
        getPopularTags().get(index).click();
        pageLoader.shouldBe(disappear);
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
        pageLoader.shouldBe(disappear);
        return this;
    }

    @Step("Get a list of articles on the page")
    public List<SelenideElement> getArticles() {
        return articles
                .shouldBe(Condition.visible)
                .$$("a.preview-link");
    }

    @Step("Get a list of articles' titles on the page")
    public List<String> getTitlesOfAllArticlesOnPage() {
        List<SelenideElement> articles = getArticles();
        List<String> articlesTitles = new ArrayList<>();
        for(int i = 0; i < articles.size(); i++) {
            articlesTitles.add(getTitleOfArticle(i));
        }
        return articlesTitles;
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

    @Step("Get a title of an article with index {0}")
    public String getTitleOfArticle(int index) {
        return getArticles().get(index).$("h1").getText();
    }

    //TODO: remove after test refactoring
    @Step("Get a title of a given article")
    public String getTitleOf(SelenideElement article) {
        return article.$("h1").getText();
    }

    //TODO: remove after test refactoring
    @Step("Get list of tags of a given article")
    public List<String> getTagsOf(SelenideElement article) {
        return article
                .$$("ul li")
                .stream()
                .map(tag -> tag.innerText().replace("\u200c", "&zwnj;").trim())
                .collect(Collectors.toList());
    }

    @Step("Get list of tags of the article with index {0}")
    public List<String> getTagsOfArticle(int index) throws IndexOutOfBoundsException{
        SelenideElement article = getArticles().get(index);

        return article
                .$$("ul li")
                .stream()
                .map(tag -> tag.innerText().replace("\u200c", "&zwnj;").trim())
                .collect(Collectors.toList());
    }

    @Step("Get all tags of article with index {0}. Get number of tags that exactly match '{1}'")
    public int howManyTagsOfArticleMatch(int articleIndex, String textToMatch) throws IndexOutOfBoundsException{
        List<String> tagsOfArticle = getTagsOfArticle(articleIndex);
        List<String> exactTags = tagsOfArticle
                .stream()
                .filter(t -> t.equals(textToMatch))
                .collect(Collectors.toList());

        return exactTags.size();
    }


    @Step("Get list of pages")
    public List<SelenideElement> getPagination() {
        return pagination
                .scrollTo()
                .shouldBe(Condition.visible)
                .$$("a");
    }

    @Step("Go to page by index {0}")
    public ArticlesPage goToPage(int index) throws IndexOutOfBoundsException{
        getPagination().get(index).click();
        pageLoader.shouldBe(disappear);
        return this;
    }


    @Step("Go to random page and return its number")
    public int goToRandomPage() {
        val pageBtns = getPagination();
        Random random = new Random();
        val randomIndex = random.nextInt(pageBtns.size());
        pageBtns.get(randomIndex).click();
        pageLoader.shouldBe(disappear);
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

    @Step("Click on 'Global Feed' tab")
    public ArticlesPage clickGlobalFeedTab() {
        feedTabs.$x("li/a[contains(text(), 'Global Feed')]").click();
        pageLoader.shouldBe(disappear);
        return this;
    }
}