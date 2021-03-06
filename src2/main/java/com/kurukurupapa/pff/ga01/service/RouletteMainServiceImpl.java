package com.kurukurupapa.pff.ga01.service;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import com.kurukurupapa.pff.ga01.domain.Fitness;
import com.kurukurupapa.pff.ga01.domain.FitnessForBattle;
import com.kurukurupapa.pff.ga01.domain.Population;
import com.kurukurupapa.pff.ga01.domain.PopulationByRoulette;
import com.kurukurupapa.pffsimu.domain.item.ItemDataSet;
import com.kurukurupapa.pffsimu.domain.memoria.MemoriaDataSet;

/**
 * メインサービスの実装クラスです。
 */
@Component
public class RouletteMainServiceImpl implements RouletteMainService {

    private MemoriaDataSet mMemoriaDataSet;
    private ItemDataSet mItemDataSet;
    private List<Population> mPopulationList;
    private DefaultCategoryDataset mDataset;
    private int mMaxFitness;

    @Override
    public void run(String[] args) {
        if (!setUp(args)) {
            return;
        }

        while (!isEnd()) {
            runMain();
        }
        tearDown();
    }

    /**
     * 前処理です。
     *
     * @param args
     *            実行時引数
     * @return 処理を継続する場合true。中止する場合false。
     */
    protected boolean setUp(String[] args) {
        // データ読み込み
        mItemDataSet = new ItemDataSet();
        mItemDataSet.read();
        mMemoriaDataSet = new MemoriaDataSet(mItemDataSet);
        mMemoriaDataSet.readTestFile();

        // 評価基準
        Fitness fitness = new FitnessForBattle();

        // 集団の生成
        mPopulationList = new ArrayList<Population>();
        mPopulationList.add(new PopulationByRoulette("Roulette 0.1%",
                mMemoriaDataSet, mItemDataSet, 20, 0.001f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 0.5%",
                mMemoriaDataSet, mItemDataSet, 20, 0.005f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 1%",
                mMemoriaDataSet, mItemDataSet, 20, 0.01f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 5%",
                mMemoriaDataSet, mItemDataSet, 20, 0.05f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 10%",
                mMemoriaDataSet, mItemDataSet, 20, 0.1f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 50%",
                mMemoriaDataSet, mItemDataSet, 20, 0.5f, fitness));
        mPopulationList.add(new PopulationByRoulette("Roulette 100%",
                mMemoriaDataSet, mItemDataSet, 20, 1.0f, fitness));

        // グラフデータ準備
        mDataset = new DefaultCategoryDataset();
        mMaxFitness = 0;

        return true;
    }

    /**
     * 主処理です。
     */
    protected void runMain() {
        String columnKey = String.valueOf(mPopulationList.get(0).getCount());

        for (Population e : mPopulationList) {
            // 次の世代を生成する。
            if (e.getCount() <= 0) {
                e.init();
                print(e.toString());
            } else {
                e.next();
                print(e.getShortString());
            }

            // グラフデータ登録
            int fitness = e.getAverageFitness();
            mMaxFitness = Math.max(mMaxFitness, fitness);
            mDataset.addValue(fitness, e.getName() + " Average fitness",
                    columnKey);
        }

        mDataset.addValue(mMaxFitness, "Max fitness", columnKey);
    }

    private boolean isEnd() {
        // TODO 終わり判定処理を実装する。
        // TODO とりあえず10世代とする
        return mPopulationList.get(0).getCount() >= 100;
    }

    /**
     * 後処理です。
     * <p>
     * 各種リソースの後始末や、処理結果などの出力を行います。
     * </p>
     */
    protected void tearDown() {
        // print("後処理です。");
        for (Population e : mPopulationList) {
            print(e.toString());
        }

        // グラフ描画
        JFreeChart chart = ChartFactory.createLineChart("Sample Line Chart",
                "Generation", "Fitness", mDataset, PlotOrientation.VERTICAL,
                true, true, false);
        ChartFrame frame = new ChartFrame("Sample Line Chart", chart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * メッセージを出力します。
     *
     * @param msg
     *            メッセージ
     */
    private void print(String msg) {
        System.out.println(msg);
    }

}
