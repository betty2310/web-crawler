public class DateCustom {
    private int day;
    private int month;
    private int year;
    public DateCustom(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public DateCustom(int month, int year) {
        this.day = -1;
        this.month = month;
        this.year = year;
    }
    public DateCustom(int year) {
        this.day = -1;
        this.month = -1;
        this.year = year;
    }
    public DateCustom() {
        this.day = -1;
        this.month = -1;
        this.year = -1;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    @Override
    public String toString() {
        return "Date{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
