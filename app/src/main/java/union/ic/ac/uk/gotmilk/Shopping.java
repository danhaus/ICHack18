package union.ic.ac.uk.gotmilk;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by leszek on 1/27/18.
 */

public class Shopping {
    private String name;
    private ArrayList<ShopItem> shopItems;

    public Shopping(String name) {
        this.name = name;
        shopItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        if (shopItems.size() == 0) return 0;
        return shopItems.size()-1;
    }

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public void addShopItem(ShopItem shopItem) {
        shopItems.add(shopItem);
    }

    public void removeShopItem(int index) {
        shopItems.remove(index);
    }

    public void setName(String name) {
        this.name = name;
    }
}
