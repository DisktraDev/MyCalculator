package me.disktradev.calculator;

public class Currency {
    private String mName;
    private String mCode;
    private double mBaseValue;

    private double mUSDValue;
    private double mEURValue;
    private double mJPYValue;
    private double mSEKValue;

    public Currency(String mCode, String mName) {
        this.mName = mName;
        this.mCode = mCode;
        this.mBaseValue = 1;
    }

    public String getCode() {
        return mCode;
    }

    public double getUSDValue() {
        return mUSDValue;
    }

    public void setUSDValue(double mUSDValue) {
        this.mUSDValue = mUSDValue;
    }

    public double getEURValue() {
        return mEURValue;
    }

    public void setEURValue(double mEURValue) {
        this.mEURValue = mEURValue;
    }

    public double getJPYValue() {
        return mJPYValue;
    }

    public void setJPYValue(double mJPYValue) {
        this.mJPYValue = mJPYValue;
    }

    public double getSEKValue() {
        return mSEKValue;
    }

    public void setSEKValue(double mSEKValue) {
        this.mSEKValue = mSEKValue;
    }
}
