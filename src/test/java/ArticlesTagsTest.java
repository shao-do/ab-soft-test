import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import lombok.val;
import org.assertj.core.api.SoftAssertions;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ArticlesPage;
import utils.CollectionsUtil;

import java.util.ArrayList;
import java.util.List;

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

    @DataProvider(name = "randomTags")
    public Object[][] provideRandomTagsIndexes() {
        val tagsToIterateThrough = CollectionsUtil.getNRandomIndexesFromList(4, popularTags);
        return new Object[][]{
                {tagsToIterateThrough.get(0)},
                {tagsToIterateThrough.get(1)},
                {tagsToIterateThrough.get(2)},
                {tagsToIterateThrough.get(3)},
        };
    }

    @Test(dataProvider = "randomTags")
    @Feature("Articles")
    @Description("Verify that clicking on a tag filters out articles with this tag")
    public void testArticleTagWithIndex(int tagIndex) {

        val expectedTag = popularTags.get(tagIndex);

        page.clickTag(tagIndex);
        val activeTab = page.getActiveFeedTab();
        val pageNumber = page.goToRandomPage();
        val article = page.getRandomArticle();
        val articleTitle = page.getTitleOf(article);
        val articleTags = page.getTagsOf(article);

        SoftAssertions.assertSoftly(softAssertion -> {
            softAssertion.assertThat(activeTab)
                    .as("FAIL: tab's name should be tha same as the test tag's name.")
                    .withFailMessage("Test tag : '%s' (number : %d), page : %s, article title : '%s'",
                            expectedTag, tagIndex + 1, pageNumber, articleTitle)
                    .isEqualTo(expectedTag);

            softAssertion.assertThat(articleTags)
                    .as("FAIL: among the article tags there should be exactly one test tag")
                    .withFailMessage("Test tag : '%s' (number : %d), page : %s, article title : '%s'",
                            expectedTag, tagIndex + 1, pageNumber, articleTitle)
                    .containsOnlyOnce(expectedTag);
        });
    }
}