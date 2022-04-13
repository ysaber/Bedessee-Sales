package com.thebedesseegroup.sales.product.category;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thebedesseegroup.sales.main.MainActivity;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.product.ProductFragment;
import com.thebedesseegroup.sales.provider.Contract;

/**
 * Fragment that shows the list of categories. From here, the user can click on a category to go back
 * to the ProductsFragment but showing only the products from the selected category.
 */
public class CategoryFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public final static String TAG = "CategoryList";

    private CategoryAdapter mAdapter;

    private static CategoryFragment instance;

    public static CategoryFragment getInstance() {
        if (instance == null) {
            instance = new CategoryFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MixPanelManager.trackScreenView(getActivity(), "Categories screen");

        mAdapter = new CategoryAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, null, new String []{Contract.CategoryColumns.COLUMN_DESCRIPTION}, new int[] {android.R.id.text1});

        ListView listView = new ListView(getActivity());

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setPadding(5, 5, 5, 5);

        getActivity().getLoaderManager().initLoader(8080, null, this);

        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProductFragment productFragment = new ProductFragment();
        final Category2 category2 = mAdapter.getItem(position);
        MixPanelManager.selectCategory(getActivity(), category2.getDescription());
        productFragment.setFilter(category2);
        ((MainActivity)getActivity()).switchFragment(productFragment, ProductFragment.TAG);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Contract.Category.CONTENT_URI, null, Contract.CategoryColumns.COLUMN_ACTIVE + " = 'Y'", null, Contract.CategoryColumns.COLUMN_DESCRIPTION + " ASC");
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
