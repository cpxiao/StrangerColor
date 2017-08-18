package com.cpxiao.strangercolor.mode.extra;

import com.cpxiao.R;

/**
 * @author cpxiao on 2017/08/17.
 */

public final class ColorExtra {
    public static final int COUNT_3 = 3;
    public static final int COUNT_4 = 4;
    public static final int COUNT_5 = 5;
    public static final int COUNT_DEFAULT = COUNT_3;


    private static final int[] colorArray3 = {R.array.colorRed, R.array.colorGreen, R.array.colorBlue};
    private static final int[] colorArray4 = {R.array.colorRed, R.array.colorGreen, R.array.colorBlue, R.array.colorYellow};
    private static final int[] colorArray5 = {R.array.colorRed, R.array.colorGreen, R.array.colorBlue, R.array.colorYellow, R.array.colorGrey};

    public static int[] getColorArray(int colorCount) {
        if (colorCount == COUNT_3) {
            return colorArray3;
        } else if (colorCount == COUNT_4) {
            return colorArray4;
        } else if (colorCount == COUNT_5) {
            return colorArray5;
        } else {
            return colorArray3;
        }


    }


}
