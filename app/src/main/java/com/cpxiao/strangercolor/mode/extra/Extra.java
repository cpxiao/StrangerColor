package com.cpxiao.strangercolor.mode.extra;

/**
 * @author cpxiao on 2017/08/18.
 */

public final class Extra {
    public static final class Key {
        private static final String BEST_SCORE = "BEST_SCORE_%s";

        public static String getBestScoreKey(int colorCount) {
            return String.format(BEST_SCORE, String.valueOf(colorCount));
        }
    }
}
