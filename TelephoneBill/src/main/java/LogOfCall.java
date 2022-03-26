import java.util.Date;

public class LogOfCall {

    private String number;
    private double price;
    private int numOfOccur;

    public LogOfCall(String number, double price) {
        this.number = number;
        this.price = price;
        this.numOfOccur = 1;
    }

    public String getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public int getNumOfOccur() {
        return numOfOccur;
    }

    public void setPrice(double price) {
        this.price += price;
    }

    public void setNumOfOccur(int numOfOccur) {
        this.numOfOccur += numOfOccur;
    }
}
