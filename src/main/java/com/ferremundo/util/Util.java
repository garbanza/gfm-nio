package com.ferremundo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

	public static float round2(float f){
		BigDecimal big = new BigDecimal(f);
		big = big.setScale(2, RoundingMode.HALF_UP);
		return big.floatValue();
	}
}
