package com.kurukurupapa.pffsimu.domain.item;

import java.util.List;

import com.kurukurupapa.pffsimu.domain.Attr;
import com.kurukurupapa.pffsimu.domain.Mement;

/**
 * 黒魔法アイテムデータ拡張クラス
 */
public class BlackMagicItemDataEx extends MagicItemDataEx {

	public BlackMagicItemDataEx(ItemData itemData, ItemType itemType,
			ItemType2 itemType2, Attr attr, int magicCharge, int magicEffect) {
		super(itemData, itemType, itemType2, attr, magicCharge, magicEffect);
	}

	/**
	 * 黒魔法ダメージを取得します。
	 *
	 * @param intelligence
	 *            知性
	 * @param magicAttack
	 *            黒魔法攻撃特性
	 * @param magicResistance
	 *            敵の魔法防御力
	 *
	 * @return ダメージ
	 */
	public float getAttackDamage(int intelligence, float magicAttack,
			int magicResistance) {
		// 魔法ダメージ＝(知性＋効果値)×倍率－魔法防御力
		// http://wikiwiki.jp/pictlogicaff/?%C0%EF%C6%AE%BE%F0%CA%F3#measure
		// ※知恵メメントで黒魔法 1.20を前提とします。
		return (intelligence + mMagicEffect) * magicAttack
				* Mement.CHIE_BLACK_RATE - magicResistance;
	}

	public float getWeakAttrValue(List<Attr> weakList) {
		return mAttr.isAttrWithoutFlight(weakList) ? 1 : 0;
	}

	public float getResistanceAttrRate(List<Attr> resistanceList) {
		return mAttr.isAttrWithoutFlight(resistanceList) ? 0.8f : 1;
	}

}
