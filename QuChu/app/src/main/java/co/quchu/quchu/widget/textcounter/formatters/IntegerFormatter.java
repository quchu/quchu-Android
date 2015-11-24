package co.quchu.quchu.widget.textcounter.formatters;


import java.text.NumberFormat;
import java.util.Locale;

import co.quchu.quchu.widget.textcounter.Formatter;

/**
 * Created by prem on 10/28/14.
 */
public class IntegerFormatter implements Formatter {

    @Override
    public String format(String prefix, String suffix, float value) {
        return prefix + NumberFormat.getNumberInstance(Locale.US).format(value) + suffix;
    }
}
