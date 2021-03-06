package com.kurukurupapa.pffsimu.domain;

/**
 * バトル形式enum
 */
public enum BattleType {
	NORMAL("バランス"), //
	ATTACK("攻撃重視"), //
	DEFENCE("防御重視"), //
	PHYSICAL_DEFENCE("物理防御重視"), //
	MAGIC_DEFENCE("魔法防御重視"), //
	RECOVERY("回復重視"), //
	HP_DEFENCE_RECOVERY("HP・防御・回復重視"), //
	EXA_BATTLIA("エクサバトリア"), //
	;

	private String mText;

	private BattleType(String text) {
		mText = text;
	}

	/**
	 * enum定数名を取得します。（Thymeleaf用です。）
	 * 
	 * @return 文字列
	 */
	public String getName() {
		return name();
	}

	public String getText() {
		return mText;
	}
}
