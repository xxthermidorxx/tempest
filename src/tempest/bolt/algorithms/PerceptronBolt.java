package tempest.bolt.algorithms;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.tuple.Fields;

public class PerceptronBolt extends BaseBasicBolt {

    private int __categorized;
    private Map<String, Integer> __featureVector;
    private Map<String, Integer> __weightVector;

    /**
     * constructor
     * @param void
     */
    public PerceptronBolt() {
        __weightVector = new HashMap<String, Integer>();
    }

    /**
     * set weight vector, if it has the kye of the word, set value 0
     * @param void
     * @return void
     */
    private void __setWeightVector() {
        // 重みベクトルの重みをランダムに生成
        for (Map.Entry<String, Integer> keyValuePair : __featureVector.entrySet()) {
            String key = keyValuePair.getKey();
            if ( !__weightVector.containsKey(key) ) {
                __weightVector.put( key, 0 );
            }
        }

        // 論文がそのジャンルに分類されるかをランダムに出力
        int[] num = {-1, 1};
        Random rnd = new Random();
        __categorized = num[ rnd.nextInt(1) ];
    }

    /**
     * calculate the dot product of __weightVector.values() and __featureVector.values()
     * @param void
     * @return int 1 if result is right else 0
     */
    private int __calcDotProduct() {
        int dot_product_sum = 0;
        for (String key : __featureVector.keySet()) {
            dot_product_sum += __featureVector.get(key) * __weightVector.get(key);
        }
        return (dot_product_sum > 0) ? 1 : 0;
    }

    /**
     * update the weight vector if the prediction is wrong
     * @param int prediction result
     * @return void
     */
    private void __updateWeightVector(int prediction) {
        for (String key : __featureVector.keySet()) {
            int updated =  __weightVector.get(key) + prediction * __featureVector.get(key);
            __weightVector.put(key, updated);
        }
    }

    /**
     * if right nothing happens and if wrong fix the _weightVector
     * @param void
     * @return int
     */
    private void __predict() {
        for (int loopCount=0; loopCount<1000; loopCount++) {
            int prediction = __calcDotProduct();
            if ( __categorized != prediction ) __updateWeightVector(prediction);
            else break;
        }
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        /** HACK: 無検査キャスト*/
        __featureVector = (HashMap<String, Integer>) tuple.getValue(0);
        __setWeightVector();
        __predict();

        collector.emit(new Values(__weightVector));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("weight_vector"));
    }
}
