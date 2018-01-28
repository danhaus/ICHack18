package union.ic.ac.uk.gotmilk;

/**
 * Created by leszek on 1/27/18.
 */

public class ShopItem {
    private String productName, category;
    private int barcode;
    private double price, energy, fat, carbohydrates, sugars, protein, salt;

    public ShopItem(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return productName;
    }
}
