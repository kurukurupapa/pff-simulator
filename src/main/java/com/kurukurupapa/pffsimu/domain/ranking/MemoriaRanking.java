package com.kurukurupapa.pffsimu.domain.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.kurukurupapa.pffsimu.domain.fitness.FitnessCalculator;
import com.kurukurupapa.pffsimu.domain.fitness.FitnessValue;
import com.kurukurupapa.pffsimu.domain.fitness.MemoriaFitness;
import com.kurukurupapa.pffsimu.domain.item.ItemDataSet;
import com.kurukurupapa.pffsimu.domain.memoria.LeaderSkill;
import com.kurukurupapa.pffsimu.domain.memoria.LeaderSkillFactory;
import com.kurukurupapa.pffsimu.domain.memoria.Memoria;
import com.kurukurupapa.pffsimu.domain.memoria.MemoriaData;
import com.kurukurupapa.pffsimu.domain.memoria.MemoriaDataSet;
import com.kurukurupapa.pffsimu.domain.party.Party;
import com.kurukurupapa.pffsimu.domain.partyfinder.impl1.Dp01;

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
	private FitnessCalculator mFitnessCalculator;
	private Party mParty;
	private List<MemoriaFitness> mFitnessList;

	public void setParams(MemoriaDataSet memoriaDataSet,
			ItemDataSet itemDataSet, FitnessCalculator fitnessCalculator) {
		setParams(memoriaDataSet, itemDataSet, fitnessCalculator, new Party());
	}

	public void setParams(MemoriaDataSet memoriaDataSet,
			ItemDataSet itemDataSet, FitnessCalculator fitnessCalculator,
			Party party) {
		mMemoriaDataSet = memoriaDataSet;
		mItemDataSet = itemDataSet;
		mFitnessCalculator = fitnessCalculator;
		mParty = party;
	}

	public void run() {
		mLogger.trace("Start");
		mLogger.debug("メモリア数=" + mMemoriaDataSet.size());

		// メモリアとアイテムの一覧
		MemoriaDataSet memoriaDataSet = mMemoriaDataSet.clone();
		ItemDataSet itemDataSet = mItemDataSet.clone();
		if (mParty != null) {
			for (Memoria e : mParty.getMemoriaList()) {
				memoriaDataSet.remove(e.getName());
				itemDataSet.removeWeapon(e.getWeapon());
				itemDataSet.removeMagicOrAccessory(e.getAccessories());
			}
		}

		// 各メモリアの評価
		mFitnessList = new ArrayList<MemoriaFitness>();
		Dp01 dp;
		for (MemoriaData e : memoriaDataSet) {
			// 当該メモリアの最大評価を計算
			MemoriaDataSet tmpMemoriaDataSet = new MemoriaDataSet(itemDataSet);
			tmpMemoriaDataSet.add(e);
			dp = new Dp01(tmpMemoriaDataSet, itemDataSet, mFitnessCalculator);
			dp.run(1);
			FitnessValue max = dp.getParty().getFitnessObj();

			// リーダースキルを考慮する
			for (MemoriaData e2 : mMemoriaDataSet) {
				LeaderSkill leaderSkill = LeaderSkillFactory.get(e2);
				// TODO 自分自身のリーダースキルは省いた方が良いかも。
				Party party = dp.getParty().clone();
				party.setLeaderSkill(leaderSkill);
				party.calcFitness(mFitnessCalculator);
				if (max.getValue() < party.getFitness()) {
					max = party.getFitnessObj();
				}
			}

			mFitnessList.add(max.getMemoriaFitnesses().get(0));
		}

		// 評価値の降順でソート
		Collections.sort(mFitnessList, new Comparator<MemoriaFitness>() {
			@Override
			public int compare(MemoriaFitness arg0, MemoriaFitness arg1) {
				// 降順
				return arg1.getValue() - arg0.getValue();
			}
		});

		mLogger.trace("End");
	}

	public List<MemoriaFitness> getFitnesses() {
		return mFitnessList;
	}

}