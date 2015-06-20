package com.kurukurupapa.pff.domain;

import java.util.List;

/**
 * 属性enum
 */
public enum Attr {
	/** 属性なし */
	NONE("なし", "属性なし"),
	/** 炎属性 */
	FIRE("炎", "炎属性"),
	/** 氷属性 */
	ICE("氷", "氷属性"),
	/** 風属性 */
	WIND("風", "風属性"),
	/** 地属性 */
	EARTH("地", "地属性"),
	/** 雷属性 */
	THUNDER("雷", "雷属性"),
	/** 聖属性 */
	HOLINESS("聖", "聖属性"),
	/** 水属性 */
	WATER("水", "水属性"),
	/** 飛行 */
	FLIGHT("飛行", "飛行属性"),
	/** 冷気属性 */
	COLD("冷気", "冷気属性"),
	/** 闇属性 */
	DARKNESS("闇", "闇属性"),
	//
	;

	private final String mShortText;
	private final String mLongText;

	private Attr(String shortText, String longText) {
		mShortText = shortText;
		mLongText = longText;
	}

	@Override
	public String toString() {
		return mLongText;
	}

	public static Attr parse(String nameOrText) {
		for (Attr e : values()) {
			if (e.name().equals(nameOrText)
					|| e.getShortText().equals(nameOrText)
					|| e.getLongText().equals(nameOrText)) {
				return e;
			}
		}
		throw new AppException("不正な引数です。nameOrText=" + nameOrText);
	}

	public String getShortText() {
		return mShortText;
	}

	public String getLongText() {
		return mLongText;
	}

	public boolean isNone() {
		return this == NONE;
	}

	public boolean isFlight() {
		return this == FLIGHT;
	}

	public boolean isAttrWithoutFlight(List<Attr> weakList) {
		if (this == NONE || isFlight()) {
			return false;
		}
		for (Attr e : weakList) {
			if (this == e) {
				return true;
			}
		}
		return false;
	}
}
