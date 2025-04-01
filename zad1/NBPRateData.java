package zad1;

import java.util.List;

public class NBPRateData {
    private List<NBPRate> rates;

    public List<NBPRate> getRates() {
        return rates;
    }

    public void setRates(List<NBPRate> rates) {
        this.rates = rates;
    }
}

class NBPRate {
    private double mid;

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }
}
