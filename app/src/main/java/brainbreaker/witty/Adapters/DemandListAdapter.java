package brainbreaker.witty.Adapters;

/**
 * Created by brainbreaker.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brainbreaker.witty.Adapters.CustomProductListAdapter;
import brainbreaker.witty.Classes.DemandProductClass;
import brainbreaker.witty.R;

public class DemandListAdapter extends BaseAdapter {

    private List<DemandProductClass> mDemandProductList;
    private CustomProductListAdapter.ButtonClickListener mAddtoStockClickListener = null;
    private LayoutInflater mInflater;
    private Context context;
    public DemandListAdapter(ArrayList<DemandProductClass> DemandProductList, LayoutInflater inflater
            , Context context,CustomProductListAdapter.ButtonClickListener mAddtoStockClickListener) {
        mDemandProductList = DemandProductList;
        mInflater = inflater;
        this.context = context;
        this.mAddtoStockClickListener = mAddtoStockClickListener;
    }

    @Override
    public int getCount() {
        return mDemandProductList.size();
    }

    @Override
    public DemandProductClass getItem(int position) {
        return mDemandProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewItemHolder item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.demand_fragment_list_item_layout, null);
            item = new ViewItemHolder();
            item.DemandProductTitle = (TextView) convertView.findViewById(R.id.demandproductname);
            item.DemandProductImageView = (ImageView) convertView.findViewById(R.id.demandimageview);
            item.DemandPriceTextView = (TextView) convertView.findViewById(R.id.demandproductprice);
            item.DemandBrandTextView = (TextView) convertView.findViewById(R.id.brandname);
            item.DemandCategoryTextView = (TextView) convertView.findViewById(R.id.demandproductcategory);
            item.AddToStockButton = (ImageButton) convertView.findViewById(R.id.AddToStock);

            convertView.setTag(item);
        } else {
            item = (ViewItemHolder) convertView.getTag();
        }

        final DemandProductClass curProduct = mDemandProductList.get(position);

        Picasso.with(context).setIndicatorsEnabled(false);
        Picasso.with(context)
                .load(curProduct.getImage())
                .resize(200, 200)
                .placeholder(R.drawable.witty)
                .error(R.drawable.witty)
                .into(item.DemandProductImageView);

        item.DemandProductTitle.setText(curProduct.getName());
        item.DemandCategoryTextView.setText(curProduct.getCategory());
        item.DemandPriceTextView.setText("Rs. "+curProduct.getprice());
        item.DemandBrandTextView.setText(curProduct.getBrand());
        item.AddToStockButton.setTag(position);

        final View finalConvertView = convertView;
        item.AddToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddtoStockClickListener != null)
                    mAddtoStockClickListener.onButtonClick((Integer) v.getTag(), finalConvertView);
            }
        });
        return convertView;
    }

    private class ViewItemHolder {
        ImageView DemandProductImageView;
        TextView DemandProductTitle;
        TextView DemandPriceTextView;
        TextView DemandBrandTextView;
        TextView DemandCategoryTextView;
        ImageButton AddToStockButton;
    }

}

