package com.kurukurupapa.pff.domain;

import java.util.List;

/**
 * アイテムデータ拡張クラス
 */
public class ItemDataEx {
    /** アイテムデータ */
    protected ItemData mItemData;
    /** アイテム種別 */
    protected ItemType mItemType;
    /** アイテム種別2 */
    protected ItemType2 mItemType2;
    /** 属性 */
    protected Attr mAttr;

    /**
     * コンストラクタ
     *
     * @param itemData
     */
    public ItemDataEx(ItemData itemData, ItemType itemType,
            ItemType2 itemType2, Attr attr) {
        mItemData = itemData;
        mItemType = itemType;
        mItemType2 = itemType2;
        mAttr = attr;
    }

    /**
     * 指定メモリアに対して装備可能か判定します。
     *
     * @param MemoriaData
     *            メモリアデータ
     * @return 装備可能な場合true
     */
    public boolean isValid(MemoriaData memoriaData) {
        return true;
    }

    /**
     * 物理攻撃におけるブート係数を取得します。
     *
     * @param weakList
     *            敵弱点一覧
     * @return 係数
     */
    public float getBoostValueForPhysical(List<Attr> weakList) {
        return 0.0f;
    }

    public float getBoostValueForBlackMagic(List<Attr> weakList) {
        return 0;
    }

}
