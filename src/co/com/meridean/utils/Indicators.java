package co.com.meridean.utils;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Indicators {

    private Hashtable<String, Integer> amountTotalByGoldLabels;
    private Hashtable<String, Integer> amountTotalByEstimatedLabels;
    private Hashtable<String, Integer> amountCorrectByEstimatedLabels;

    private Hashtable<String, Double> precisionIndicatorByLabel;
    private Hashtable<String, Double> recallIndicatorByLabel;
    private Hashtable<String, Double> fscoreIndicatorByLabel;

    public Indicators() {
        amountTotalByGoldLabels         = new Hashtable<String, Integer>();
        amountTotalByEstimatedLabels    = new Hashtable<String, Integer>();
        amountCorrectByEstimatedLabels  = new Hashtable<String, Integer>();

        precisionIndicatorByLabel       = new Hashtable<String, Double>();
        recallIndicatorByLabel          = new Hashtable<String, Double>();
        fscoreIndicatorByLabel          = new Hashtable<String, Double>();
    }

    public void processLinesFileGold(){
        Util util = new Util();
        ArrayList<String> linesFileGold = util.readGoldFile();

        for(int i=0; i<linesFileGold.size(); i++){
            String line = linesFileGold.get(i);
            processLine(line);
        }

        fillEmptyLabels();

        calculateIndicatorPrecision();
        calculateIndicatorRecall();
        calculateIndicatorFscore();

        System.out.println(amountTotalByGoldLabels);
        System.out.println(amountTotalByEstimatedLabels);
        System.out.println(amountCorrectByEstimatedLabels);

        System.out.println(precisionIndicatorByLabel);
        System.out.println(recallIndicatorByLabel);
        System.out.println(fscoreIndicatorByLabel);
    }

    private void processLine(String line){
        String goldLabel = "";
        String estimatedLabel = "";
        StringTokenizer stLine = new StringTokenizer(line, "\t");
        int posToken = 0;
        while (stLine.hasMoreTokens()){
            String token = stLine.nextToken();
            if(posToken == 0){
                goldLabel = token;
                if(amountTotalByGoldLabels.containsKey(goldLabel)){
                    int tempAmount = amountTotalByGoldLabels.get(goldLabel);
                    amountTotalByGoldLabels.put(goldLabel, ++tempAmount);
                }
                else{
                    amountTotalByGoldLabels.put(goldLabel, 1);
                }
            }
            else if(posToken == 1){
                estimatedLabel = token;
                if(amountTotalByEstimatedLabels.containsKey(estimatedLabel)){
                    int tempAmount = amountTotalByEstimatedLabels.get(estimatedLabel);
                    amountTotalByEstimatedLabels.put(estimatedLabel, ++tempAmount);
                }
                else{
                    amountTotalByEstimatedLabels.put(estimatedLabel, 1);
                }
            }
            else if(posToken == 2){
                int flag = Integer.parseInt(token);
                if(flag == 1){
                    if(amountCorrectByEstimatedLabels.containsKey(estimatedLabel)){
                        int tempAmount = amountCorrectByEstimatedLabels.get(estimatedLabel);
                        amountCorrectByEstimatedLabels.put(estimatedLabel, ++tempAmount);
                    }
                    else{
                        amountCorrectByEstimatedLabels.put(estimatedLabel, 1);
                    }
                }
            }
            posToken++;
        }
    }

    private void fillEmptyLabels(){
        Enumeration<String> labels = amountTotalByGoldLabels.keys();
        while (labels.hasMoreElements()){
            String label = labels.nextElement();
            if(!amountTotalByEstimatedLabels.containsKey(label)){
                amountTotalByEstimatedLabels.put(label,0);
            }
            if(!amountCorrectByEstimatedLabels.containsKey(label)){
                amountCorrectByEstimatedLabels.put(label,0);
            }
        }
    }

    private void calculateIndicatorPrecision(){
        Enumeration<String> labels = amountTotalByGoldLabels.keys();
        while (labels.hasMoreElements()){
            String label = labels.nextElement();
            double indicatorPrecision = 0;
            double amountCorrect = amountCorrectByEstimatedLabels.get(label) + 0.0;
            double amountTotal   = amountTotalByEstimatedLabels.get(label)   + 0.0;

            indicatorPrecision = amountCorrect / amountTotal;

            precisionIndicatorByLabel.put(label, indicatorPrecision);
        }
    }

    private void calculateIndicatorRecall(){
        Enumeration<String> labels = amountTotalByGoldLabels.keys();
        while (labels.hasMoreElements()){
            String label = labels.nextElement();
            double indicatorRecall = 0;
            double amountCorrect = amountCorrectByEstimatedLabels.get(label) + 0.0;
            double amountGoldTotal   = amountTotalByGoldLabels.get(label)   + 0.0;

            indicatorRecall = amountCorrect / amountGoldTotal;

            recallIndicatorByLabel.put(label, indicatorRecall);
        }
    }

    private void calculateIndicatorFscore(){
        Enumeration<String> labels = amountTotalByGoldLabels.keys();
        while (labels.hasMoreElements()){
            String label = labels.nextElement();
            double indicatorFscore = 0;
            double precision = precisionIndicatorByLabel.get(label);
            double recall    = recallIndicatorByLabel.get(label);

            if((precision+recall) != 0.0){
                indicatorFscore = ( 2 * ( (precision*recall) / (precision+recall) ) );
            }
            else {
                indicatorFscore = 0.0;
            }

            fscoreIndicatorByLabel.put(label, indicatorFscore);
        }
    }
}
