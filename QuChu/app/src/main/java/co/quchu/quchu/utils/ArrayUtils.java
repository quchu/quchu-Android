package co.quchu.quchu.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/7/5.
 */
public class ArrayUtils {
    public static List<Integer> getMaxValues(float[] source) {
        List<Integer> maxArrayIndex = new ArrayList<>();
        float maxValue = source[0];
        int maxValuesIndex = 0;
        boolean notUnique = false;
        for (int i = 1; i < source.length; i++) {
            if (maxValue < source[i]) {
                maxValue = source[i];
                maxValuesIndex = i;
            } else if (maxValue == source[i]) {
                notUnique = true;
            }
        }
        if (notUnique) {
            for (int i = 0; i < source.length; i++) {
                if (maxValue == source[i]) {
                    maxArrayIndex.add(i);
                }
            }
        } else {
            maxArrayIndex.add(maxValuesIndex);
        }
        return maxArrayIndex;
    }

}
