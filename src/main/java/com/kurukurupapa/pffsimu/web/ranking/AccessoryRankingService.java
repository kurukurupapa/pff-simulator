package com.kurukurupapa.pffsimu.web.ranking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kurukurupapa.pff.domain.ItemDataSet;
import com.kurukurupapa.pff.domain.MemoriaDataSet;
import com.kurukurupapa.pff.dp01.AccessoryFitness;
import com.kurukurupapa.pff.dp01.AccessoryRanking;
import com.kurukurupapa.pff.dp01.Fitness;
import com.kurukurupapa.pff.dp01.Party;

/**
 * ランキング機能 アクセサリランキングサービスクラス
 */
@Service
public class AccessoryRankingService {
	private ItemDataSet itemDataSet;
	private MemoriaDataSet memoriaDataSet;
	private Fitness fitness;
	private Party party;
	private int memoriaIndex;
	private AccessoryRanking accessoryRanking;

	public AccessoryRankingService() {
		// データ読み込み
		itemDataSet = new ItemDataSet();
		itemDataSet.readUserFile();
		memoriaDataSet = new MemoriaDataSet(itemDataSet);
		memoriaDataSet.readUserFile();
	}

	public void setup() {
		setup(null, null, 0);
	}

	public void setup(Fitness fitness, Party party, int memoriaIndex) {
		this.fitness = fitness;
		this.party = party;
		this.memoriaIndex = memoriaIndex;
	}

	public void run() {
		accessoryRanking = new AccessoryRanking();
		accessoryRanking.setParams(memoriaDataSet, itemDataSet, fitness, party,
				memoriaIndex);
		accessoryRanking.run();
	}

	public List<AccessoryFitness> getFitnesses() {
		return accessoryRanking.getFitnesses();
	}

}