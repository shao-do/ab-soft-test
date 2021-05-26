package utils;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectionsUtil {

    public static <T> List<T> getNRandomElementsFromList(int numOfElem, List<T> source) {
        if (numOfElem <= source.size()) {
            List<T> result = new ArrayList<>();

            for(int i=0; i<numOfElem; i++) {
                Random random = new Random();
                int randomIndex = random.nextInt(source.size());
                Object elem = source.get(randomIndex);
                source.remove(elem);
                result.add((T) elem);
            }
            return result;
        } else throw new IndexOutOfBoundsException("Error: Number of elements to get is greater than list size");
    }

    public static List<Integer> getNRandomIndexesFromList(int numOfElem, List<?> sourceList) {
        if (numOfElem <= sourceList.size()) {
            List<Integer> allIndexes = new ArrayList<>();
            List<Integer> randomIndexes = new ArrayList<>();

            for (Object obj : sourceList) {
                allIndexes.add(sourceList.indexOf(obj));
            }

            for(int i=0; i<numOfElem; i++) {
                Random random = new Random();
                int randomIndex = random.nextInt(allIndexes.size());
                val elem = allIndexes.get(randomIndex);
                allIndexes.remove(elem);
                randomIndexes.add(elem);
            }
            return randomIndexes;
        } else throw new IndexOutOfBoundsException("Error: Number of indexes to get is greater than list size");
    }
}
