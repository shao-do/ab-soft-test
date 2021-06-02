import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ArticlesPage;

import java.util.ArrayList;
import java.util.List;

import static utils.CollectionsUtil.getNRandomIndexesFromListExcludingFirstAndLast;
import static utils.CollectionsUtil.getRandomIndex;

public class ArticlesTagsTest extends BaseTest {

    private List<String> popularTags = new ArrayList<>();
    private final ArticlesPage page = new ArticlesPage();

    @BeforeClass
    public void getTagsValues() {

        popularTags = page
                .open()
                .getPopularTagsTexts();

        if (popularTags.isEmpty()) {
            throw new SkipException("Test skipped: no tags were found");
        }
    }

    @DataProvider(name = "firstLastAndRandomTag")
    public Object[][] provideFirstLastAndRandomTagIndexes() {
        int firstTagIndex = 0;
        int lastTagIndex = popularTags.size() - 1;
        int randomTagIndex = getNRandomIndexesFromListExcludingFirstAndLast(1, popularTags)
                .get(0);

        return new Object[][]{
                {firstTagIndex},
                {randomTagIndex},
                {lastTagIndex}};
    }

    @Test(dataProvider = "firstLastAndRandomTag")
    @Feature("Articles")
    @Description("WHEN click on a tag THEN the results page shows filtered articles by this tag")
    public void testFilteringByFirstLastAndRandomTag(int tagIndex) {
        String expectedTag = popularTags.get(tagIndex);

        List<SelenideElement> articlesOnPage = page
                .clickTag(tagIndex)
                .goToPage(getRandomIndex(page.getPagination()))
                .getArticles();

        // create a list of integers, where every int indicates how many tags of an article match given text
        List<Integer> exactTagsInEachArticle = new ArrayList<>();

        // iterate through articles and save number of exact matches
        for (int i = 0; i < articlesOnPage.size(); i++) {
            int howManyTagsOfArticleMatch = page.howManyTagsOfArticleMatch(i, expectedTag);
            exactTagsInEachArticle.add(howManyTagsOfArticleMatch);
        }

        Assertions.assertThat(exactTagsInEachArticle)
                .as("\nVerify that after filtering each article should have exactly 1 tag with text '%s'",
                        expectedTag)
                .withFailMessage("\nFAIL: The list %s shows how many tags in each article match given text '%s'",
                        exactTagsInEachArticle, expectedTag)
                .allMatch(tag -> tag.equals(1));
    }

    @Test
    @Feature("Articles")
    @Description("WHEN click on 'Global feed' THEN tag filtering is reset")
    public void testResetFiltering() {

        int tagIndex = getRandomIndex(popularTags);
        String expectedTag = popularTags.get(tagIndex);

        List<SelenideElement> articlesInitially = page.getArticles();
        List<Integer> exactTagsInEachArticleInitially = new ArrayList<>();
        List<String> articlesTitlesInitially = page.getTitlesOfAllArticlesOnPage();
        // iterate through initial list of articles and save number of exact matches
        for (int i = 0; i < articlesInitially.size(); i++) {
            int howManyTagsOfArticleMatch = page.howManyTagsOfArticleMatch(i, expectedTag);
            exactTagsInEachArticleInitially.add(howManyTagsOfArticleMatch);
        }

        List<SelenideElement> articlesAfterFiltering = page.clickTag(tagIndex).getArticles();
        List<Integer> exactTagsInEachArticleAfterFiltering = new ArrayList<>();
        // iterate through the list of articles after filtering and save number of exact matches
        for (int i = 0; i < articlesAfterFiltering.size(); i++) {
            int howManyTagsOfArticleMatch = page.howManyTagsOfArticleMatch(i, expectedTag);
            exactTagsInEachArticleAfterFiltering.add(howManyTagsOfArticleMatch);
        }

        List<SelenideElement> articlesAfterReset = page.clickGlobalFeedTab().getArticles();
        List<Integer> exactTagsInEachArticleAfterReset = new ArrayList<>();
        List<String> articlesTitlesAfterReset = page.getTitlesOfAllArticlesOnPage();
        // iterate through the list of articles after filtering and save number of exact matches
        for (int i = 0; i < articlesAfterReset.size(); i++) {
            int howManyTagsOfArticleMatch = page.howManyTagsOfArticleMatch(i, expectedTag);
            exactTagsInEachArticleAfterReset.add(howManyTagsOfArticleMatch);
        }

        SoftAssertions.assertSoftly(softAssertion -> {
            softAssertion.assertThat(exactTagsInEachArticleInitially)
                    .as("\nVerify that initially each article should have 0 tags with text '%s'", expectedTag)
                    .withFailMessage("\nFAIL: The list %s shows how many tags in each article match given text '%s'",
                            exactTagsInEachArticleInitially, expectedTag)
                    .allMatch(numOfExactTags -> numOfExactTags.equals(0));

            softAssertion.assertThat(exactTagsInEachArticleAfterFiltering)
                    .as("\nVerify that after filtering each article should have exactly 1 tag with text '%s'",
                            expectedTag)
                    .withFailMessage("\nFAIL: The list %s shows how many tags in each article match given text '%s'",
                            exactTagsInEachArticleAfterFiltering, expectedTag)
                    .allMatch(numOfExactTags -> numOfExactTags.equals(1));

            softAssertion.assertThat(exactTagsInEachArticleAfterReset)
                    .as("\nVerify that after reset each article should have 0 tags with text '%s'", expectedTag)
                    .withFailMessage("\nFAIL: The list %s shows how many tags in each article match given text '%s'",
                            exactTagsInEachArticleAfterReset, expectedTag)
                    .allMatch(numOfExactTags -> numOfExactTags.equals(0));

            softAssertion.assertThat(articlesTitlesAfterReset)
                    .as("\nVerify that after filter reset a list of articles should return to initial state",
                            articlesTitlesAfterReset, articlesTitlesInitially)
                    .withFailMessage("\nFAIL: The list of articles after filter reset %s is NOT equal to the initial list of articles %s",
                            articlesTitlesAfterReset, articlesTitlesInitially)
                    .containsExactlyElementsOf(articlesTitlesInitially);
        });
    }

    @AfterMethod
    public void resetFiltering() {
        page.clickGlobalFeedTab();
    }
}