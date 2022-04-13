package com.thebedesseegroup.sales.product;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.product.brand.Brand;
import com.thebedesseegroup.sales.product.category.Category2;
import com.thebedesseegroup.sales.product.status.Status;
import com.thebedesseegroup.sales.provider.Contract;
import com.thebedesseegroup.sales.utilities.Utilities;

/**
 * Fragment that holds a list of products. If no brand or category is passed into the constructor,
 * the fragment will show all products by default.
 */
public class ProductFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    final public static String TAG = "ProductList";

    public static ListView sListView;
    public GridView mGridView;
    private EditText mEditSearch;
    private ProductsFilter mFilter;
    private com.thebedesseegroup.sales.product.brand.Brand mBrand;
    private Category2 mCategory;
    private Status mStatus;

    private ProductAdapter mAdapter2;


    public enum ProductsFilter {
        ALL, BRAND, CATEGORY, STATUS
    }

    public ProductFragment() {
        mFilter = ProductsFilter.ALL;
    }

    public void setFilter(Brand brand) {
        mBrand = brand;
        mFilter = ProductsFilter.BRAND;
    }


    public void setFilter(Category2 category) {
        mCategory = category;
        mFilter = ProductsFilter.CATEGORY;
    }

    public void setFilter(Status status) {
        mStatus = status;
        mFilter = ProductsFilter.STATUS;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mEditSearch = (EditText) rootView.findViewById(R.id.editText_search);

        rootView.findViewById(R.id.btnClearSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditSearch.setText(null);
            }
        });

        mAdapter2 = new ProductAdapter(getActivity());

        mGridView.setAdapter(mAdapter2);

        final int loaderId;
        switch(mFilter) {
            case BRAND:
                loaderId = mBrand.hashCode();
                break;
            case CATEGORY:
                loaderId = mCategory.hashCode();
                break;
            case STATUS:
                loaderId = mStatus.hashCode();
                break;
            default:
                loaderId = mFilter.hashCode();
                break;
        }
        getActivity().getLoaderManager().initLoader(loaderId, null, this);


        ProductScrollListener gridScrollListener = new ProductScrollListener(mAdapter2);
        mGridView.setOnScrollListener(gridScrollListener);

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                MixPanelManager.trackSearch(getActivity(), "Products screen", s.toString());

                final String filter = getFilterWhereClause();

                final String whereClause = Contract.ProductColumns.COLUMN_BRAND + " LIKE '%" + s + "%'" + " OR " +
                        Contract.ProductColumns.COLUMN_NUMBER + " LIKE '%" + s + "%'" + " OR " +
                        Contract.ProductColumns.COLUMN_DESCRIPTION + " LIKE '%" + s + "%'";

                mAdapter2.getFilter().filter(filter + (!filter.equals("") ? " AND " : "") + "(" + whereClause + ")");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus();
                    Utilities.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        final int loaderId = getLoaderId();
        getActivity().getLoaderManager().restartLoader(loaderId, null, this);
    }


    private int getLoaderId() {
        switch(mFilter) {
            case BRAND:
                return mBrand.hashCode();
            case CATEGORY:
                return mCategory.hashCode();
            case STATUS:
                return mStatus.hashCode();
            default:
                return mFilter.hashCode();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Product product = ((ProductAdapter) sListView.getAdapter()).getItem(position);
        MixPanelManager.selectProduct(getActivity(), product.getBrand() + " " + product.getDescription());
        ProductDetailsDialog.show(getActivity(), product);
    }


    private void appendSearchbarHint(String string) {
        mEditSearch.setHint(mEditSearch.getHint() + " >> " + string);
    }


    private String getFilterWhereClause() {
        switch(mFilter) {
            case BRAND:
                return Contract.ProductColumns.COLUMN_BRAND + " = '" + mBrand.getName().toUpperCase() + "'";

            case CATEGORY:
                return Contract.ProductColumns.COLUMN_NUMBER + " like '" + mCategory.getChar() + "%'";

            case STATUS:
                return Contract.ProductColumns.COLUMN_STATUS_CODE + " = '" + mStatus.name() + "'";
            default:
                return "";
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch(mFilter) {
            case BRAND:
                mEditSearch.setHint("Filter");
                appendSearchbarHint("Brand");
                appendSearchbarHint(mBrand.getName());
                return new CursorLoader(getActivity(), Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_BRAND + " = '" + mBrand.getName().toUpperCase() + "'", null, Contract.ProductColumns.COLUMN_NUMBER + " ASC");
            case CATEGORY:
                mEditSearch.setHint("Filter");
                appendSearchbarHint("Category");
                appendSearchbarHint(mCategory.getDescription());
                return new CursorLoader(getActivity(), Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_NUMBER + " like '" + mCategory.getChar() + "%'", null, Contract.ProductColumns.COLUMN_NUMBER + " ASC");

            case STATUS:
                mEditSearch.setHint("Filter");
                appendSearchbarHint("Status");
                appendSearchbarHint(mStatus.getDescription());
                return new CursorLoader(getActivity(), Contract.Product.CONTENT_URI, null, Contract.ProductColumns.COLUMN_STATUS_CODE + " = '" + mStatus.name() + "'", null, Contract.ProductColumns.COLUMN_NUMBER + " ASC");
            default:
                mEditSearch.setHint("Filter");
                appendSearchbarHint("All products");
                return new CursorLoader(getActivity(), Contract.Product.CONTENT_URI, null, null, null, Contract.ProductColumns.COLUMN_NUMBER + " ASC");
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            mAdapter2.changeCursor(cursor);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter2.changeCursor(null);
    }


}
