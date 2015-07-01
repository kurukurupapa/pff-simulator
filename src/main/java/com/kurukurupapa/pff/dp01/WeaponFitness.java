package com.kurukurupapa.pff.dp01;

import org.apache.commons.lang3.Validate;

/**
 * 武器適応度クラス
 */
public class WeaponFitness extends ItemFitness {

	/**
	 * 適応度を計算します。
	 * 
	 * @param memoria
	 *            武器装備前のメモリア
	 */
	public void calc(Memoria memoria) {
		// 当該武器を装備するため、メモリアは武器未装備であること。
		Validate.validState(memoria.getRemainWeaponSlot() >= 1);

		// 武器装備後のメモリア
		mMemoria = memoria.clone();
		mMemoria.setWeapon(mItem);

		// 当該武器の適応度
		Fitness fitness = createFitness();
		calcDifference(fitness, memoria, mMemoria);
	}

}