package io.brewday.domain;


public class PercentRange {

    private final double from;
    private final double to;

    public PercentRange(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
}
