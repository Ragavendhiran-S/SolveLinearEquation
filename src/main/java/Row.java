public class Row {
    Complex[] values;

    public Row(Complex[] values) {
        this.values = values;
    }

    public Complex[] multiplyAndGet(Complex factor) {
        Complex[] multipliedValues = new Complex[values.length];
        for (int i = 0; i < values.length; i++) {
            multipliedValues[i] = values[i].multiply(factor);
        }
        return multipliedValues;
    }

    public void addValues(Complex[] valuesToAdd) {
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].add(valuesToAdd[i]);
        }
    }
}