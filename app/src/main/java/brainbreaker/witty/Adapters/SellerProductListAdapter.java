package brainbreaker.witty.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import brainbreaker.witty.R;

/**
 * Created by brainbreaker.
 */
public class SellerProductListAdapter extends BaseAdapter {
    private Context mContext;
    private ButtonClickListener mdetailsClickListener  = null;
    private final ArrayList<String> productname;
    private final ArrayList<String> productimageurl;
    private final ArrayList<Integer> productprice;
    int screenWidth;
    ViewHolder viewHolder;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public SellerProductListAdapter(Context c, ArrayList<String> productname, ArrayList<String> productimageurl,
                                    ArrayList<Integer> productprice,int screenWidth,
                                    ButtonClickListener detailslistner) {
        mContext = c;
        this.productimageurl = productimageurl;
        this.productname = productname;
        this.productprice = productprice;
        this.screenWidth = screenWidth;
        mdetailsClickListener = detailslistner;

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.MAX_PRIORITY)
                .threadPoolSize(5)
                .diskCacheExtraOptions(screenWidth, Math.round(screenWidth / 2), null)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    static class ViewHolder {
        ImageView ProductImage;
        TextView ProductName;
        TextView ProductPrice;
        TextView CartCounters;
        TextView BargainRequestCounters;
        Button details;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seller_product_list_item_layout, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.ProductName = (TextView) convertView.findViewById(R.id.sellerproductname);
        viewHolder.ProductPrice = (TextView) convertView.findViewById(R.id.sellerproductprice);
        viewHolder.CartCounters = (TextView) convertView.findViewById(R.id.cartcounter);
        viewHolder.BargainRequestCounters = (TextView) convertView.findViewById(R.id.bargainrequests);
        viewHolder.ProductImage = (ImageView) convertView.findViewById(R.id.sellerproductimageview);
        viewHolder.details = (info.hoang8f.widget.FButton) convertView.findViewById(R.id.ShowDetails);

        viewHolder.details.setTag(position);

        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mdetailsClickListener != null)
                    mdetailsClickListener.onButtonClick((Integer) v.getTag());
            }
        });

        viewHolder.ProductImage.getLayoutParams().width = screenWidth;
        viewHolder.ProductImage.getLayoutParams().height = Math.round(screenWidth / 2);
        viewHolder.ProductImage.requestLayout();

        viewHolder.ProductName.setText(productname.get(position));
        viewHolder.ProductPrice.setText("Your Price - Rs." +productprice.get(position));

        Picasso.with(mContext).setIndicatorsEnabled(false);
        Picasso.with(mContext)
                .load(productimageurl.get(position))
                .error(R.drawable.witty)
                .into(viewHolder.ProductImage);

        return convertView;
    }

    public interface ButtonClickListener {
        public abstract void onButtonClick(int position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return productname.size();
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

