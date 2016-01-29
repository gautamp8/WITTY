package brainbreaker.witty.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import brainbreaker.witty.Classes.ProductClass;
import brainbreaker.witty.R;

/**
 * Created by brainbreaker. ADAPTER FOR SHOWING PRODUCT LIST IN USER ACTIVITY
 */
public class CustomProductListAdapter extends BaseAdapter {

    private Context mContext;
    private ButtonClickListener mcartClickListener = null;
    private ButtonClickListener mbargainClickListener  = null;
    private ArrayList<ProductClass> ProductList;
    int screenWidth;
    ViewHolder viewHolder;
    public CustomProductListAdapter(Context c, ArrayList<ProductClass> ProductList,int screenWidth, ButtonClickListener cartlistener,
                                    ButtonClickListener bargainlistner) {
        mContext = c;
        this.ProductList = ProductList;
        this.screenWidth = screenWidth;
        mcartClickListener = cartlistener;
        mbargainClickListener = bargainlistner;
    }

    static class ViewHolder {
        ImageView ProductImage;
        TextView ProductName;
        TextView AddedInfo;
        TextView ProductPrice;
        info.hoang8f.widget.FButton AddToCart;
        info.hoang8f.widget.FButton Bargain;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_list_item_layout, parent, false);
        }
        final ProductClass curProduct = ProductList.get(position);

        viewHolder = new ViewHolder();
        viewHolder.ProductName = (TextView) convertView.findViewById(R.id.productname);
        viewHolder.ProductPrice = (TextView) convertView.findViewById(R.id.productprice);
        viewHolder.ProductImage = (ImageView) convertView.findViewById(R.id.productimageview);
        viewHolder.AddedInfo = (TextView)convertView.findViewById(R.id.addedInfo);
        viewHolder.AddedInfo.setVisibility(View.GONE);
        viewHolder.AddToCart = (info.hoang8f.widget.FButton) convertView.findViewById(R.id.Addtocart);
        viewHolder.Bargain = (info.hoang8f.widget.FButton) convertView.findViewById(R.id.bargainbutton);

        viewHolder.AddToCart.setTag(position);
        viewHolder.Bargain.setTag(position);

        final View finalConvertView1 = convertView;
        viewHolder.AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mcartClickListener != null)
                    mcartClickListener.onButtonClick((Integer) v.getTag(), finalConvertView1);

            }
        });

        final View finalConvertView = convertView;
        viewHolder.Bargain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mbargainClickListener != null)
                    mbargainClickListener.onButtonClick((Integer) v.getTag(), finalConvertView);
            }
        });

        viewHolder.ProductImage.getLayoutParams().width = screenWidth;
        viewHolder.ProductImage.getLayoutParams().height = Math.round(screenWidth / 2);
        viewHolder.ProductImage.requestLayout();

        viewHolder.ProductName.setText(curProduct.getName());
        viewHolder.ProductPrice.setText("Rs. "+curProduct.getprice());

        Picasso.with(mContext)
                .setIndicatorsEnabled(false);
        Picasso.with(mContext)
                .load(curProduct.getImage())
                .placeholder(R.drawable.witty)
                .error(R.drawable.witty)
                .into(viewHolder.ProductImage);

        return convertView;
    }

    public interface ButtonClickListener {
        public abstract void onButtonClick(int position, View view);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ProductList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}

