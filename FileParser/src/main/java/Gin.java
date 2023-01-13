import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"manufacturer", "bottle", "size", "price"})
public class Gin {

    private String manufacturer;
    private String bottle;
    private String size;
    private String price;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBottle() {
        return bottle;
    }

    public void setBottle(String bottle) {
        this.bottle = bottle;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
