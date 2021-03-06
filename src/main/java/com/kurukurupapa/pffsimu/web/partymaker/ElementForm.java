package com.kurukurupapa.pffsimu.web.partymaker;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * パーティメーカー機能 要素選択フォームクラス
 */
public class ElementForm {

	/**
	 * ボタンの値
	 * 
	 * 要素リストのうち、ユーザが選択したインデックスが設定されます。負の値は未選択を表します。
	 */
	@NotNull
	private Integer btn;

	public int getBtn() {
		return btn;
	}

	public void setBtn(int btn) {
		this.btn = btn;
	}

}
