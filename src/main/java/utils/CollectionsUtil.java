package utils;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionsUtil {


    // including min and excluding max
    public static int getRandomIntegerFromRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static int getRandomIndex(List<?> list) {
        return getRandomIntegerFromRange(0, list.size());
    }

    public static List<Integer> getNRandomIndexesFromList(int numOfIndexes, List<?> sourceList) {
        if (numOfIndexes <= sourceList.size()) {
            List<Integer> allIndexes = new ArrayList<>();
            List<Integer> randomIndexes = new ArrayList<>();

            for (Object obj : sourceList) {
                allIndexes.add(sourceList.indexOf(obj));
            }

            for (int i = 0; i < numOfIndexes; i++) {
                int randomIndex = getRandomIndex(allIndexes);
                val elem = allIndexes.get(randomIndex);
                allIndexes.remove(elem);
                randomIndexes.add(elem);
            }
            return randomIndexes;
        } else throw new IndexOutOfBoundsException("Error: Number of indexes to get is greater than list size");
    }

    public static List<Integer> getNRandomIndexesFromListExcludingFirstAndLast(int numOfIndexes, List<?> sourceList) {

        List<Integer> selectedIndexes = new ArrayList<>();
        List<Integer> targetIndexes = new ArrayList<>();

        if (numOfIndexes <= (sourceList.size() - 2)) {
            // get list of all indexes except the first and the last
            for (int i = 1; i < (sourceList.size() - 1); i++) {
                selectedIndexes.add(i);
            }
            // iterate through selected indexes to get a list of random indexes
            for (int i = 0; i < numOfIndexes; i++) {
                // get random int from list of selected indexes
                int randomInt = getRandomIntegerFromRange(0, selectedIndexes.size());
                // add to target indexes an element from selected indexes
                targetIndexes.add(selectedIndexes.get(randomInt));
                // remove from selected indexes the added element
                selectedIndexes.remove(randomInt);
            }
            return targetIndexes;
        } else throw new IndexOutOfBoundsException("Error: Number of indexes to get is greater than list size");
    }
}
