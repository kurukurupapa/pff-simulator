package com.kurukurupapa.pff.dp01;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kurukurupapa.pff.domain.ItemDataSet;
import com.kurukurupapa.pff.domain.MemoriaDataSet;

public class MemoriaRankingTest {
	private static ItemDataSet mItemDataSet;
	private static MemoriaDataSet mMemoriaDataSet;

	private MemoriaRanking sut;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// データ読み込み
		mItemDataSet = new ItemDataSet();
		mItemDataSet.readUserFile();
		mMemoriaDataSet = new MemoriaDataSet(mItemDataSet);
		mMemoriaDataSet.readUserFile();
	}

	@Before
	public void setUp() throws Exception {
		sut = new MemoriaRanking();
	}

	@Test
	public void testCalc_Battle_パーティなし() {
		// 準備
		Fitness fitness = new FitnessForBattle();
		sut.setParams(mMemoriaDataSet, mItemDataSet, fitness);

		// テスト実行
		sut.run();
		List<MemoriaFitnessValue> actual = sut.getFitnesses();
		String actualStr = toString(actual);

		// 検証
		assertEquals("" //
				+ "9857,元帥シド+おろち+マーシャルネイ+マーシャルネイ\n" //
				+ "9732,パンネロ+ダンシングダガー+クリスタルの小手+ファイアRF+3\n" //
				+ "9528,ライトニング(No.119)+青紅の剣(レア5)+ルフェインブーツ+クリスタルの小手\n" //
				+ "8833,トレイ+黄忠の長弓(レア5)+パワーリスト+クリスタルの小手\n" //
				+ "7510,アーロン+おろち+ルフェインブーツ+クリスタルの小手\n" //
				+ "6652,マキナ+青紅の剣(レア5)+ルフェインブーツ+クリスタルの小手\n" //
				+ "6353,アーシェ+ネクロフォリア+ルフェインブーツ+クリスタルの小手\n" //
				+ "6266,ティナ+青紅の剣(レア5)+ルフェインブーツ+クリスタルの小手\n" //
				+ "5800,セシル+ネクロフォリア+ルフェインブーツ+クリスタルの小手\n" //
				+ "5040,ヴァニラ+イノセントロッド+ファイアRF+3+ケアル\n" //
				+ "4508,デシ+鉄壁のグリモア(レア3)+赤兎馬のたてがみ(レア5)+パワーリスト\n" //
				+ "4005,ユウナ(No.48)+燃える戦杖+ディアボロス+ケアルラ\n" //
		, actualStr);
	}

	@Test
	public void testCalc_Battle_パーティあり() {
		// 準備
		Fitness fitness = new FitnessForBattle();

		Party party = new Party();
		Memoria memoria = new Memoria(mMemoriaDataSet.find("元帥シド"));
		memoria.setWeapon(mItemDataSet.find("おろち"));
		memoria.addAccessory(mItemDataSet.find("マーシャルネイ"));
		memoria.addAccessory(mItemDataSet.find("マーシャルネイ"));
		party.add(memoria);
		memoria = new Memoria(mMemoriaDataSet.find("パンネロ"));
		memoria.setWeapon(mItemDataSet.find("ダンシングダガー"));
		memoria.addAccessory(mItemDataSet.find("クリスタルの小手"));
		memoria.addAccessory(mItemDataSet.find("ファイアRF+3"));
		party.add(memoria);

		sut.setParams(mMemoriaDataSet, mItemDataSet, fitness, party);

		// テスト実行
		sut.run();
		List<MemoriaFitnessValue> actual = sut.getFitnesses();
		String actualStr = toString(actual);

		// 検証
		assertEquals("" //
				+ "9381,ライトニング(No.119)+青紅の剣(レア5)+ルフェインブーツ+エクサイヤリング+1\n" //
				+ "8723,トレイ+黄忠の長弓(レア5)+赤兎馬のたてがみ(レア5)+パワーリスト\n" //
				+ "7421,アーロン+五月雨+赤兎馬のたてがみ(レア5)+ルフェインブーツ\n" //
				+ "6578,マキナ+青紅の剣(レア5)+ルフェインブーツ+エクサイヤリング+1\n" //
				+ "6270,アーシェ+ネクロフォリア+ルフェインブーツ+エクサイヤリング+1\n" //
				+ "6184,ティナ+青紅の剣(レア5)+ルフェインブーツ+エクサイヤリング+1\n" //
				+ "5691,セシル+ネクロフォリア+ルフェインブーツ+エクサイヤリング+1\n" //
				+ "4614,ヴァニラ+イノセントロッド+エアロラ+ケアルラ\n" //
				+ "4508,デシ+鉄壁のグリモア(レア3)+赤兎馬のたてがみ(レア5)+パワーリスト\n" //
				+ "4005,ユウナ(No.48)+燃える戦杖+ディアボロス+ケアルラ\n" //
		, actualStr);
	}

	private String toString(List<MemoriaFitnessValue> memoriaFitnessList) {
		StringBuilder sb = new StringBuilder();
		for (MemoriaFitnessValue e : memoriaFitnessList) {
			sb.append(e + "\n");
		}
		return sb.toString();
	}
}