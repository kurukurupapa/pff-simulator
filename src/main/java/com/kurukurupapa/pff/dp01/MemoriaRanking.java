package com.kurukurupapa.pff.dp01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.kurukurupapa.pff.domain.ItemDataSet;
import com.kurukurupapa.pff.domain.MemoriaData;
import com.kurukurupapa.pff.domain.MemoriaDataSet;

/**
 * メモリア順位付けクラス
 * 
 * メモリアを評価し、順位をつけます。 これにより、不要なメモリアを判断しやすくなると思います。
 */
public class MemoriaRanking {
	/** ロガー */
	private Logger mLogger = Logger.getLogger(MemoriaRanking.class);

	private MemoriaDataSet mMemoriaDataSet;
	private ItemDataSet mItemDataSet;
	private Fitness mFitness;
	private Party mParty;
	private List<MemoriaFitnessValue> mFitnessList;

	public void setParams(MemoriaDataSet memoriaDataSet,
			ItemDataSet itemDataSet, Fitness fitness) {
		setParams(memoriaDataSet, itemDataSet, fitness, new Party());
	}

	public void setParams(MemoriaDataSet memoriaDataSet,
			ItemDataSet itemDataSet, Fitness fitness, Party party) {
		mMemoriaDataSet = memoriaDataSet;
		mItemDataSet = itemDataSet;
		mFitness = fitness;
		mParty = party;
	}

	public void run() {
		mLogger.trace("Start");
		mLogger.debug("メモリア数=" + mMemoriaDataSet.size());

		// メモリアとアイテムの一覧
		MemoriaDataSet memoriaDataSet = mMemoriaDataSet.clone();
		ItemDataSet itemDataSet = mItemDataSet.clone();
		for (Memoria e : mParty.getMemoriaList()) {
			memoriaDataSet.remove(e.getName());
			itemDataSet.removeWeapon(e.getWeapon());
			itemDataSet.removeMagicOrAccessory(e.getAccessories());
		}

		// 各メモリアの評価
		mFitnessList = new ArrayList<MemoriaFitnessValue>();
		Dp01 dp;
		for (MemoriaData e : memoriaDataSet) {
			MemoriaDataSet tmpMemoriaDataSet = new MemoriaDataSet(itemDataSet);
			tmpMemoriaDataSet.add(e);
			dp = new Dp01(tmpMemoriaDataSet, itemDataSet, mFitness);
			dp.run(1);
			mFitnessList.add(dp.getParty().getFitnessObj()
					.getMemoriaFitnesses().get(0));
		}

		// 評価値の降順でソート
		Collections.sort(mFitnessList, new Comparator<MemoriaFitnessValue>() {
			@Override
			public int compare(MemoriaFitnessValue arg0,
					MemoriaFitnessValue arg1) {
				// 降順
				return arg1.getValue() - arg0.getValue();
			}
		});

		mLogger.trace("End");
	}

	public List<MemoriaFitnessValue> getFitnesses() {
		return mFitnessList;
	}
}