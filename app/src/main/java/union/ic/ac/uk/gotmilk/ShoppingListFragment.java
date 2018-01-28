package union.ic.ac.uk.gotmilk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import behaviour.union.ic.ac.uk.gotmilk.R;

/**
 * Created by leszek on 1/27/18.
 */

public class ShoppingListFragment extends Fragment {
    private RecyclerView mShoppingRecycleView;
    private ShoppingAdapter mAdapter;
    private LinearLayoutManager linearLayout;
    private List<Shopping> shoppings;
    private View view;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.shopping_list_fragment, container, false);

        fab = getActivity().findViewById(R.id.fab);
        fab.show();

        mShoppingRecycleView = view.findViewById(R.id.shopping_list_recycler_view);
        linearLayout = new LinearLayoutManager(getContext());
        mShoppingRecycleView.setLayoutManager(linearLayout);
        ShoppingLab shoppingLab = ShoppingLab.get(getContext());
        shoppings = shoppingLab.getShoppingArr();

        //when there are no shoppings display a message
        LinearLayout linearLayout = view.findViewById(R.id.shopping_list_linear_layout);
        if (shoppings.size() == 0) {
            TextView noListMessage = new TextView(getContext());
            linearLayout.addView(noListMessage);
        }

        updateUI();

        return view;
    }

    private void updateUI() {
        mAdapter = new ShoppingAdapter(shoppings);
        mShoppingRecycleView.setAdapter(mAdapter);
    }

    private class ShoppingHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mSize;
        public Shopping mShopping;


        public ShoppingHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    fab.hide();
                    Fragment fragment = new ShoppingFragment();
                    Bundle args = new Bundle();
                    args.putInt("index", getAdapterPosition());
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().
                            replace(R.id.flContent, fragment).addToBackStack(null).commit();
                }
            });

            mName = itemView.findViewById(R.id.list_shopping_name);
            mSize = itemView.findViewById(R.id.list_shopping_size);
        }
    }

    private class ShoppingAdapter extends RecyclerView.Adapter<ShoppingHolder> {
        private List<Shopping> mShoppingArr;
        public ShoppingAdapter(List<Shopping> shoppings) {
            mShoppingArr = shoppings;
        }

        @Override
        public ShoppingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.shopping_list_row, parent, false);
            return new ShoppingHolder(view);
        }
        @Override
        public void onBindViewHolder(ShoppingHolder holder, int position) {
            Shopping shopping = mShoppingArr.get(position);
            holder.mShopping = shopping;
            holder.mName.setText(shopping.getName());
            holder.mSize.setText(String.valueOf(shopping.getSize()));
        }

        @Override
        public int getItemCount() {
            return mShoppingArr.size();
        }

    }
}
