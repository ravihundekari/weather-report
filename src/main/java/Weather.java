import java.io.Serializable;

public class Weather implements Serializable {
    private String regionCode;
    private String weatherParam;
    private int year;
    private String key;
    private float value;

    Weather(String regionCode, String weatherParam, int year, String key, float value) {
        this.regionCode = regionCode;
        this.weatherParam = weatherParam;
        this.year = year;
        this.key = key;
        this.value = value;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getWeatherParam() {
        return weatherParam;
    }

    public void setWeatherParam(String weatherParam) {
        this.weatherParam = weatherParam;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "regionCode='" + regionCode + '\'' +
                ", weatherParam='" + weatherParam + '\'' +
                ", year=" + year +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
