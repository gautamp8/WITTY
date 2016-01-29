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

import brainbreaker.witty.Classes.ProductClass;
import brainbreaker.witty.R;

public class CustomCartListAdapter extends BaseAdapter {

    private List<ProductClass> mCartProductList;
    private CustomProductListAdapter.ButtonClickListener mdeleteClickListener = null;
    private LayoutInflater mInflater;
    private Context context;
    public CustomCartListAdapter(ArrayList<ProductClass> CartProductList, LayoutInflater inflater
            , Context context,CustomProductListAdapter.ButtonClickListener mdeleteClickListener) {
        mCartProductList = CartProductList;
        mInflater = inflater;
        this.context = context;
        this.mdeleteClickListener = mdeleteClickListener;
    }

    @Override
    public int getCount() {
        return mCartProductList.size();
    }

    @Override
    public ProductClass getItem(int position) {
        return mCartProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewItemHolder item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_item_layout, null);
            item = new ViewItemHolder();
            item.ProductTitle = (TextView) convertView.findViewById(R.id.productname);
            item.ProductImageView = (ImageView) convertView.findViewById(R.id.pimageview);
            item.PriceTextView = (TextView) convertView.findViewById(R.id.productprice);
            item.SellerTextView = (TextView) convertView.findViewById(R.id.sellername);
            item.DeleteButton = (ImageButton) convertView.findViewById(R.id.DeleteButton);

            convertView.setTag(item);
        } else {
            item = (ViewItemHolder) convertView.getTag();
        }

        final ProductClass curProduct = mCartProductList.get(position);

        Picasso.with(context).setIndicatorsEnabled(false);
        Picasso.with(context)
                .load(curProduct.getImage())
                .resize(200, 200)
                .placeholder(R.drawable.witty)
                .error(R.drawable.witty)
                .into(item.ProductImageView);

        item.ProductTitle.setText(curProduct.getName());
        item.PriceTextView.setText("Rs. "+curProduct.getprice());
        item.SellerTextView.setText(curProduct.getSeller());
        item.DeleteButton.setTag(position);

        final View finalConvertView = convertView;
        item.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mdeleteClickListener != null)
                    mdeleteClickListener.onButtonClick((Integer) v.getTag(), finalConvertView);
            }
        });
        return convertView;
    }

    private class ViewItemHolder {
        ImageView ProductImageView;
        TextView ProductTitle;
        TextView PriceTextView;
        TextView SellerTextView;
        ImageButton DeleteButton;
    }

}

