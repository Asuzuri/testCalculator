import com.phonecompany.billing.TelephoneBillCalculator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Calculate implements TelephoneBillCalculator {

    public Calculate() {

    }

    private double CalculateMinRate(double rate, long diffInMin) {

        double sumToReturn = 0;
        double rate2 = rate;

        if (diffInMin > 5) {
            rate = 0.2;
            diffInMin = diffInMin - 5;
            sumToReturn = diffInMin*rate;
            diffInMin = 5;
            rate = rate2;
        }

        sumToReturn += diffInMin*rate;
        return sumToReturn;

    }

    private double CalculateEdgeMinRate(double rateStart, double rateOther, long diffInMin, Date starterDateTime) {
        double sumToReturn = 0;
        double rateBegin = rateStart;

        if (diffInMin > 5) {
            rateStart = 0.2;
            sumToReturn = rateStart * (diffInMin - 5);
            diffInMin = 5;
        }

        sumToReturn += (60-starterDateTime.getMinutes())*rateBegin;

        diffInMin -= (60-starterDateTime.getMinutes());


        sumToReturn += diffInMin*rateOther;

        return sumToReturn;

    }

    private double getPriceCalculation(String largestNumber, HashMap<String, LogOfCall> mapOfNumbers) {
        double priceForCalls = 0L;

        for (Map.Entry<String, LogOfCall> set : mapOfNumbers.entrySet()) {
            if (!set.getValue().getNumber().equals(largestNumber)) {
                priceForCalls += set.getValue().getPrice();
            }
        }

        return priceForCalls;

    }

    @Override
    public BigDecimal calculate(String phoneLog) {

        String[] newPhoneLog = phoneLog.split("\\r?\\n");

        Date starterDateTime = null;
        Date endDateTime = null;
        //long difference;
        long differencemin;
        double rate = 0;
        double sum = 0;
        double price = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        HashMap<String,LogOfCall> mapOfNumbers = new HashMap<>();

        for (int i = 0; i < newPhoneLog.length; i++) {
            try {
                starterDateTime = formatter.parse(newPhoneLog[i].substring(13,32));
                endDateTime = formatter.parse(newPhoneLog[i].substring(33,52));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            /*difference = endDateTime.getTime() - starterDateTime.getTime();
            differencemin = TimeUnit.MILLISECONDS.toMinutes(difference);*/
            differencemin = TimeUnit.MILLISECONDS.toMinutes(endDateTime.getTime() - starterDateTime.getTime());

            if (starterDateTime.getHours() == 7 && starterDateTime.getMinutes() > 55) {

                price = CalculateEdgeMinRate(0.5,1, differencemin, starterDateTime);

            }

            else if (starterDateTime.getHours() == 15 &&  starterDateTime.getMinutes() > 55) {

                price = CalculateEdgeMinRate(1,0.5, differencemin, starterDateTime);

            }

            else if (starterDateTime.getHours() < 8 || starterDateTime.getHours() > 16) {

                price = CalculateMinRate(0.5,differencemin);

            }

            else if (starterDateTime.getHours() >= 8 && starterDateTime.getHours() < 16) {

                price = CalculateMinRate(1,differencemin);

            }

            if (!mapOfNumbers.containsKey(newPhoneLog[i].substring(0,11))) {

                mapOfNumbers.put(newPhoneLog[i].substring(0,11),new LogOfCall(newPhoneLog[i].substring(0,11), price));

            }

            else {

                mapOfNumbers.get(newPhoneLog[i].substring(0,11)).setNumOfOccur(1);
                mapOfNumbers.get(newPhoneLog[i].substring(0,11)).setPrice(price);

            }

        }

        Set<String> mostUsedNumbers = new HashSet<>();
        int numOfOccurHelp = 1;

        for (Map.Entry<String, LogOfCall> set :
                mapOfNumbers.entrySet()) {

            if (set.getValue().getNumOfOccur() == numOfOccurHelp) {
                mostUsedNumbers.add(set.getKey());
            }

            if (set.getValue().getNumOfOccur() > numOfOccurHelp) {
                mostUsedNumbers.clear();
                mostUsedNumbers.add(set.getKey());
                numOfOccurHelp = set.getValue().getNumOfOccur();
            }
        }


        Iterator<String> numberIterator = mostUsedNumbers.iterator();
        Long largestNumber = 0L;
        Long largestNumberHelper = 0L;

        while (numberIterator.hasNext()) {
            largestNumberHelper = Long.parseLong(numberIterator.next());
            if (largestNumberHelper > largestNumber) {
                largestNumber = largestNumberHelper;
            }
        }

        return BigDecimal.valueOf(getPriceCalculation(Long.toString(largestNumber), mapOfNumbers));
    }


}
