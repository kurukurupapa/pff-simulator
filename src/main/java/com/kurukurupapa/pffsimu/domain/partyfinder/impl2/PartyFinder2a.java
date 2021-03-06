package com.kurukurupapa.pffsimu.domain.partyfinder.impl2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import com.kurukurupapa.pffsimu.domain.AppException;
import com.kurukurupapa.pffsimu.domain.fitness.FitnessCalculator;
import com.kurukurupapa.pffsimu.domain.item.ItemData;
import com.kurukurupapa.pffsimu.domain.item.ItemDataSet;
import com.kurukurupapa.pffsimu.domain.memoria.Memoria;
import com.kurukurupapa.pffsimu.domain.memoria.MemoriaData;
import com.kurukurupapa.pffsimu.domain.memoria.MemoriaDataSet;
import com.kurukurupapa.pffsimu.domain.party.Party;

/**
 * 最適パーティ計算クラス
 * <p>
 * 全探索で計算してみます。計算回数が多くなるので、パーティ構成でメモリア2体が限界です。
 * </p>
 * <p>
 * ループの回し方は、（メモリア1 × 武器 × 魔法/アクセサリ1 × 魔法/アクセサリ2） × （メモリア2 × 武器 × 魔法/アクセサリ1 ×
 * 魔法/アクセサリ2） × ・・・です。
 * </p>
 */
public class PartyFinder2a {
	private Logger mLogger;
	private MemoriaDataSet mMemoriaDataSet;
	private ItemDataSet mItemDataSet;
	private FitnessCalculator mFitnessCalculator;
	private Party mBestParty;

	public PartyFinder2a(MemoriaDataSet memoriaDataSet,
			ItemDataSet itemDataSet, FitnessCalculator fitnessCalculator) {
		mLogger = Logger.getLogger(PartyFinder2a.class);

		mMemoriaDataSet = memoriaDataSet;
		mItemDataSet = itemDataSet;
		mFitnessCalculator = fitnessCalculator;
	}

	public void run() {
		run(Party.MAX_MEMORIAS);
	}

	public void run(int maxMemorias) {
		Validate.validState(1 <= maxMemorias
				&& maxMemorias <= Party.MAX_MEMORIAS);

		if (maxMemorias == 1) {
			run1();
		} else if (maxMemorias == 2) {
			run2();
		} else {
			throw new AppException("未実装");
		}
	}

	public void run1() {
		Party maxParty = null;
		long calcCount = 0;
		long mCount = 0;
		long wCount = 0;
		long maCount1 = 0;
		long maCount2 = 0;

		// メモリアループ
		for (MemoriaData mdata : mMemoriaDataSet.getMemoriaDataList()) {

			// 当該メモリアが装備可能な武器/魔法/アクセサリリスト
			List<ItemData> wlist = getWeapons(mdata);
			List<ItemData> malist = getMagicsAccessories(mdata);

			// 武器ループ
			for (ItemData wdata : wlist) {
				// スロット1ループ
				for (int i = 0; i < malist.size(); i++) {
					// スロット2ループ
					for (int j = i + 1; j < malist.size(); j++) {

						// 適応度計算
						Memoria memoria = new Memoria(mdata);
						memoria.setWeapon(wdata);
						memoria.addAccessory(malist.get(i));
						memoria.addAccessory(malist.get(j));
						Party party = new Party(memoria);
						party.calcFitness(mFitnessCalculator);
						calcCount++;
						// mLogger.debug(party);

						// 最大適応度のパーティを残す
						maxParty = max(maxParty, party);

						maCount2++;
					}
					maCount1++;
				}
				wCount++;
			}
			mCount++;
		}

		mBestParty = maxParty;
		mLogger.debug("計算カウント=" + calcCount + "(" + mCount + "," + wCount + ","
				+ maCount1 + "," + maCount2 + ")");
	}

	/**
	 * メモリア2体のパーティ計算
	 * 
	 * 単純に全探索します。
	 */
	public void run2() {
		int mcount = mMemoriaDataSet.size();
		int wcount = mItemDataSet.getWeaponList().size();
		int macount = mItemDataSet.getMagicAccessoryList().size();
		mLogger.debug("メモリア数=" + mcount + ",武器数=" + wcount + ",魔法/アクセサリ数="
				+ macount);
		mLogger.debug("計算量="
				+ ((long) mcount * (mcount - 1) * wcount * (wcount - 1)
						* macount * (macount - 1) * (macount - 2) * (macount - 3)));

		// 準備
		List<MemoriaData> memorias = mMemoriaDataSet.getMemoriaDataList();
		Party maxParty = null;
		long calcCount = 0;
		long mCount = 0;
		long wCount = 0;
		long maCount1 = 0;
		long maCount2 = 0;

		// メモリアループ1
		for (int m1 = 0; m1 < memorias.size(); m1++) {
			MemoriaData mdata1 = memorias.get(m1);
			mLogger.debug("ループカウント=" + m1 + "/" + mcount);
			// 当該メモリアで装備可能なアイテム
			List<ItemData> wlist1 = getWeapons(mdata1);
			List<ItemData> malist1 = getMagicsAccessories(mdata1);
			// メモリアループ2
			for (int m2 = m1 + 1; m2 < memorias.size(); m2++) {
				MemoriaData mdata2 = memorias.get(m2);
				// 当該メモリアで装備可能なアイテム
				List<ItemData> wlist2 = getWeapons(mdata2);
				List<ItemData> malist2 = getMagicsAccessories(mdata2);
				// 武器ループ1
				for (ItemData wdata1 : wlist1) {
					// 武器ループ2
					for (ItemData wdata2 : wlist2) {
						if (wdata1 == wdata2) {
							continue;
						}
						// スロット1-1ループ
						for (int s11 = 0; s11 < malist1.size() - 1; s11++) {
							ItemData idata11 = malist1.get(s11);
							// スロット1-2ループ
							for (int s12 = s11 + 1; s12 < malist1.size(); s12++) {
								ItemData idata12 = malist1.get(s12);
								// スロット2-1ループ
								for (int s21 = 0; s21 < malist2.size() - 1; s21++) {
									ItemData idata21 = malist2.get(s21);
									if (idata21 == idata11
											|| idata21 == idata12) {
										continue;
									}
									// スロット2-2ループ
									for (int s22 = s21 + 1; s22 < malist2
											.size(); s22++) {
										ItemData idata22 = malist2.get(s22);
										if (idata22 == idata11
												|| idata22 == idata12) {
											continue;
										}

										// 適応度計算
										Memoria memoria1 = new Memoria(mdata1);
										memoria1.setWeapon(wdata1);
										memoria1.addAccessory(idata11);
										memoria1.addAccessory(idata12);
										Memoria memoria2 = new Memoria(mdata2);
										memoria2.setWeapon(wdata2);
										memoria2.addAccessory(idata21);
										memoria2.addAccessory(idata22);
										Party party = new Party();
										party.add(memoria1);
										party.add(memoria2);
										party.calcFitness(mFitnessCalculator);
										calcCount++;

										// 最大適応度のパーティを残す
										maxParty = max(maxParty, party);

										maCount2++;
									}
									maCount1++;
								}
								maCount2++;
							}
							maCount1++;
						}
						wCount++;
					}
					wCount++;
				}
				mCount++;
			}
			mCount++;
		}
		mBestParty = maxParty;

		mLogger.debug("計算カウント=" + calcCount + "(" + mCount + "," + wCount + ","
				+ maCount1 + "," + maCount2 + ")");
	}

	private List<ItemData> getWeapons(MemoriaData memoria) {
		List<ItemData> list = new ArrayList<ItemData>();
		for (ItemData e : mItemDataSet.getWeaponList()) {
			if (e.isValid(memoria)) {
				for (int i = 0; i < e.getNumber(); i++) {
					list.add(e);
				}
			}
		}
		return list;
	}

	private List<ItemData> getMagicsAccessories(MemoriaData memoria) {
		List<ItemData> result = new ArrayList<ItemData>();
		for (ItemData e : mItemDataSet.getMagicAccessoryList()) {
			if (e.isValid(memoria)) {
				for (int i = 0; i < e.getNumber(); i++) {
					result.add(e);
				}
			}
		}
		return result;
	}

	public Party getParty() {
		return mBestParty;
	}

	private Party max(Party p1, Party p2) {
		if (p1 == null || p1.getFitness() < p2.getFitness()) {
			return p2;
		} else {
			return p1;
		}
	}

}
