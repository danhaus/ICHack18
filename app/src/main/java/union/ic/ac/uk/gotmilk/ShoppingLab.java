package union.ic.ac.uk.gotmilk;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by leszek on 1/27/18.
 */

public class ShoppingLab {
    private static ShoppingLab sShoppingLab;
    private ArrayList<Shopping> shoppingArr;

    public static ShoppingLab get(Context context) {
        if (sShoppingLab == null) {
            SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
            //Retrieve the values
            String dataSerialized = sharedPref.getString(Constants.DATA_KEY, null);
            if (dataSerialized == null) {
                sShoppingLab = new ShoppingLab(new ArrayList<Shopping>());
            } else {
                Gson gson = new Gson();
                sShoppingLab = gson.fromJson(dataSerialized, ShoppingLab.class);
            }

        }
        return sShoppingLab;
    }

    public ShoppingLab(ArrayList<Shopping> shoppingArr) {
        this.shoppingArr = shoppingArr;
    }

    public void addShopping(Shopping shopping,Context context) {
        shoppingArr.add(shopping);
        saveData(context);
    }

    public ArrayList<Shopping> getShoppingArr() {
        return shoppingArr;
    }

    public void saveData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString(Constants.DATA_KEY, gson.toJson(sShoppingLab));
        editor.apply();
    }

}
