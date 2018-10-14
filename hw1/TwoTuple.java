package hw1;
class TwoTuple<T, U> {
    public final T first;
    public final U second;
    TwoTuple(T first, U second) {
        this.first = first;
        this.second = second;
    }
    public Integer firstToInt() {
        return (Integer) this.first;
    }
    public Float firstToFloat() {
        return (Float) this.first;
    }
    public Integer secondToInt() {
        return (Integer) this.second;
    }
    public Float secondToFloat() {
        return (Float) this.second;
    }
}
