package com.chromanyan.chromaticarsenal.util;

import com.chromanyan.chromaticarsenal.config.ModConfig;

public class TooltipHelper {

    private static final ModConfig.Client config = ModConfig.CLIENT;

    // this class is just a bunch of methods to make tooltip methods less ugly for people looking at my code

    private TooltipHelper() {

    }

    public static String valueTooltip(Object object) {
        return "§b" + object + "§r";
    }

    public static String potionAmplifierTooltip(int level) {
        return valueTooltip(level + 1);
    }

    public static String ticksToSecondsTooltip(int ticks) {
        if (ticks / 20 >= config.tooltipDecimalThreshold.get())
            return valueTooltip(ticks / 20);
        else // add the decimal places to the value as they might be needed (e.g. lunar crystal)
            return valueTooltip((float) ticks / 20F);
    }

    public static String percentTooltip(float decimal) {
        return valueTooltip(Math.round(100 * decimal));
    }

    public static String percentTooltip(double decimal) {
        return percentTooltip((float) decimal);
    }

    public static String multiplierAsPercentTooltip(float multiplier) {
        if (multiplier < 1) { // there's definitely a better way to do this but i don't feel like working through the math in my head right now
            return percentTooltip(1.0F - multiplier);
        } else {
            return percentTooltip(multiplier - 1.0F);
        }
    }

    public static String multiplierAsPercentTooltip(double multiplier) {
        return multiplierAsPercentTooltip((float) multiplier);
    }
}
