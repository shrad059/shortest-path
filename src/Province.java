import java.util.Objects;

public class Province {
    private String name;
    private double latitude;
    private double longitude;

    public Province(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Province province = (Province) obj;
        return Objects.equals(name, province.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
