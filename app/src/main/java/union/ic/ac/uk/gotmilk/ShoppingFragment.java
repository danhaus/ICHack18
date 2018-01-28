package union.ic.ac.uk.gotmilk;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import behaviour.union.ic.ac.uk.gotmilk.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by leszek on 1/27/18.
 */

public class ShoppingFragment extends Fragment {
    private RecyclerView mShoppingRecycleView;
    private ShoppingFragment.ShoppingAdapter mAdapter;
    private LinearLayoutManager linearLayout;
    private List<ShopItem> shopItems;
    private Shopping shopping;
    private int shoppingIndex;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.shopping_fragment, container, false);

        
        ShoppingLab shoppingLab = ShoppingLab.get(getContext());
        ArrayList<Shopping> shoppingArr = shoppingLab.getShoppingArr();

        final EditText shoppingName = view.findViewById(R.id.shopping_name);

        shoppingName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                shopping.setName(shoppingName.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        
        if (getArguments().getBoolean("new")) { //set next fragment
            //if the shopping is new the new list has the name "shopping" + size of array +1
            shopping = new Shopping("shopping " + String.valueOf(shoppingArr.size()+1));
            shoppingArr.add(shopping);
        } else {
            shoppingIndex = getArguments().getInt("index");
            shopping = shoppingArr.get(shoppingIndex);
        }
        shopItems = shopping.getShopItems();
        shoppingName.setText(shopping.getName());

        Button productAdd = view.findViewById(R.id.shopping_add);
        Button productCamera = view.findViewById(R.id.shopping_camera);
        final EditText productName = view.findViewById(R.id.shopping_product_name);

        productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.addShopItem(new ShopItem(productName.getText().toString()));
                mAdapter.notifyDataSetChanged();
                //scroll to the end of the recycler view
                mShoppingRecycleView.smoothScrollToPosition(mShoppingRecycleView.getAdapter().getItemCount() - 1);
            }
        });

        productCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        mShoppingRecycleView = view.findViewById(R.id.shopping_recycler_view);
        linearLayout = new LinearLayoutManager(getContext());
        mShoppingRecycleView.setLayoutManager(linearLayout);

        updateUI();

        return view;
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ERROR in photo", Log.getStackTraceString(ex));
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void updateUI() {
        mAdapter = new ShoppingFragment.ShoppingAdapter(shopItems);
        mShoppingRecycleView.setAdapter(mAdapter);
    }

    private class ShoppingHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mNum;
        public Button mRemove;
        public ShopItem mShopItem;


        public ShoppingHolder(View itemView) {
            super(itemView);
            mRemove = itemView.findViewById(R.id.shopping_remove);
            mName = itemView.findViewById(R.id.shopping_name);
            mNum = itemView.findViewById(R.id.shopping_num);

            //replace the empty shopItem by what the user entered
            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shopping.removeShopItem(getAdapterPosition());
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class ShoppingAdapter extends RecyclerView.Adapter<ShoppingFragment.ShoppingHolder> {
        private List<ShopItem> mShopItems;
        public ShoppingAdapter(List<ShopItem> shopItems) {
            mShopItems = shopItems;
        }

        @Override
        public ShoppingFragment.ShoppingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.shopping_row, parent, false);
            return new ShoppingFragment.ShoppingHolder(view);
        }
        @Override
        public void onBindViewHolder(ShoppingFragment.ShoppingHolder holder, int position) {
            ShopItem shopItem= mShopItems.get(position);
            holder.mShopItem = shopItem;
            holder.mName.setText(shopItem.getName());
            holder.mNum.setText(String.valueOf(position+1) + ".");
        }

        @Override
        public int getItemCount() {
            return mShopItems.size();
        }

    }
}
