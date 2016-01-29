package brainbreaker.witty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import brainbreaker.witty.R;

/**
 * Created by brainbreaker.
 */
public class SellerChatListAdapter extends BaseAdapter{
    Context context;
    String ProductName;
    ArrayList<String> ProductID;
    ArrayList<Firebase> Productrefs;
    String ImageURL;
    ArrayList<Firebase> Statusrefs;
    Integer status;
    public SellerChatListAdapter(Context context,ArrayList<Firebase> Productrefs, ArrayList<Firebase> Statusrefs,ArrayList<String> ProductID){
        this.context = context;
        this.ProductID = ProductID;
        this.Productrefs = Productrefs;
        this.Statusrefs = Statusrefs;
    }

    @Override
    public int getCount() {
        return ProductID.size();
    }

    @Override
    public String  getItem(int position) {
        return ProductName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewItemHolder item = new ViewItemHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seller_chat_list_item_layout, parent,false);
        }

        item.ChatTitle = (TextView) convertView.findViewById(R.id.chattitle);
        item.BargainStatusTextView = (TextView) convertView.findViewById(R.id.BargainStatusTextView);
        item.ProductImageView = (ImageView) convertView.findViewById(R.id.ProdutThumbnail);

        Productrefs.get(position).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProductName = dataSnapshot.getValue().toString();
                item.ChatTitle.setText(ProductName);
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Productrefs.get(position).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageURL = dataSnapshot.getValue().toString();
                Picasso.with(context).setIndicatorsEnabled(false);
                Picasso.with(context)
                        .load(ImageURL)
                        .resize(100,100)
                        .error(R.drawable.witty)
                        .into(item.ProductImageView);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Statusrefs.get(position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                /**IT MIGHT BE POSSIBLE THAT USER HAS SENT A MESSAGE TO SELLER FOR ASKING DETAILS,
                 * HE HASN'T REQUESTED ANY BARGAIN ON THE PRODUCT
                 * SO WE HAVE TO CHECK IF THE STATUS IS RETURNING NULL WHICH MEANS PRODUCT IS NOT IN BARGAINCARTS
                 * AND HANDLE ACCORDINGLY**/
                if (snapshot.getValue()!=null) {
                    status = Integer.parseInt(snapshot.getValue().toString());
                }
                else {
                    status = 3;  //SO THAT IT GOES TO THE DEFAULT VALUE IN SWITCH
                }
                switch (status){
                    case 0: item.BargainStatusTextView.setText("Bargain Status- Pending");
                        break;
                    case -1: item.BargainStatusTextView.setText("Bargain Status- Rejected");
                        break;
                    case 1: item.BargainStatusTextView.setText("Bargain Status- Accepted");
                        break;
                    case 2: item.BargainStatusTextView.setText("Bargain Status- Offered");
                        break;
                    default: item.BargainStatusTextView.setText("Bargain Status- Not Requested");
                        break;
                }
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return convertView;
    }

    static class ViewItemHolder {
        ImageView ProductImageView;
        TextView ChatTitle;
        TextView BargainStatusTextView;
    }

}

