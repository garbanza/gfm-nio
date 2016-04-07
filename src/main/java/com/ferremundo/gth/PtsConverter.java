package com.ferremundo.gth;

public class PtsConverter {
	
	public static float cm2Pt(float cm){
		return cm*72.0f/2.54f;//*(24/25.5f);
	}
	public static float mm2Pt(float mm){
		return cm2Pt(mm/10);//*(24/25.5f);
	}

	public static float mm2Pt2(float mm){
		return cm2Pt((111.4f+mm)/10);//*(24/25.5f);
	}

	public static float pt2mm(float pt){
		return 10*2.54f*pt/72;
	}
	
}
