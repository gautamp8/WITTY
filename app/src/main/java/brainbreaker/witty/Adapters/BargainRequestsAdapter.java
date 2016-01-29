package brainbreaker.witty.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;

import java.util.ArrayList;

import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.R;

/**
 * Created by brainbreaker
 */
public class BargainRequestsAdapter extends BaseAdapter {
    private ArrayList<Integer> BargainPrice;
    private String username;
    private ArrayList<String> ProductName;
    private Context context;
    private ArrayList<Integer> status;
    private ArrayList<Integer> ActualPrice;
    private ArrayList<Firebase> Bidref;
    public BargainRequestsAdapter(Context context
            ,String username
            ,ArrayList<Integer> BargainPrice
            ,ArrayList<Integer> ActualPrice
            ,ArrayList<String> ProductName
            ,ArrayList<Integer> status
            ,ArrayList<Firebase> Bidref
            ) {
        this.BargainPrice = BargainPrice;
        this.ActualPrice = ActualPrice;
        this.username = username;
        this.context = context;
        this.ProductName = ProductName;
        this.status = status;
        this.Bidref = Bidref;
    }

    @Override
    public int getCount() {
        return BargainPrice.size();
    }

    @Override
    public String  getItem(int position) {
        return ProductName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewItemHolder item;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bargain_request_list_item_layout, parent,false);

            item = new ViewItemHolder();
            item.Username = (TextView) convertView.findViewById(R.id.Username);
            item.Username.setText("user");
            item.RequestProductName = (TextView) convertView.findViewById(R.id.Requestproductname);
            item.RequestProductPrice = (TextView) convertView.findViewById(R.id.Requestproductprice);
            item.bargainprice = (TextView) convertView.findViewById(R.id.bargainprice);
            item.accept = (Button) convertView.findViewById(R.id.acceptbutton);
            item.reject = (Button) convertView.findViewById(R.id.rejectbutton);
            item.offer = (Button) convertView.findViewById(R.id.offerbutton);
            item.cross = (ImageView) convertView.findViewById(R.id.cross);
            item.tick = (ImageView) convertView.findViewById(R.id.tick);
            item.present = (ImageView) convertView.findViewById(R.id.present);
            item.seller_offered = (TextView) convertView.findViewById(R.id.seller_offered);
            item.seller_accepted = (TextView) convertView.findViewById(R.id.seller_accepted);
            item.seller_rejected = (TextView) convertView.findViewById(R.id.seller_rejected);
            convertView.setTag(item);
        } else {
            item = (ViewItemHolder) convertView.getTag();
        }

        item.cross.setVisibility(View.GONE);
        item.tick.setVisibility(View.GONE);
        item.present.setVisibility(View.GONE);
        item.seller_rejected.setVisibility(View.GONE);
        item.seller_accepted.setVisibility(View.GONE);
        item.seller_offered.setVisibility(View.GONE);


        item.RequestProductName.setText(ProductName.get(position));
        item.RequestProductPrice.setText("Actual Price of Product "+ ActualPrice.get(position));

        switch (status.get(position)) {
            case 1:
                item.accept.setText("ACCEPTED");
                item.seller_accepted.setVisibility(View.VISIBLE);
                item.tick.setVisibility(View.VISIBLE);
                break;
            case 2:
                item.offer.setText("OFFERED");
                item.present.setVisibility(View.VISIBLE);
                item.seller_offered.setVisibility(View.VISIBLE);
                break;
            case -1:
                item.reject.setClickable(false);
                item.reject.setText("REJECTED");
                item.seller_rejected.setVisibility(View.VISIBLE);
                item.cross.setVisibility(View.VISIBLE);
                break;
            case 3:
                item.seller_accepted.setText("OFFER ACCEPTED BY USER");
                item.seller_accepted.setVisibility(View.VISIBLE);
                item.present.setVisibility(View.VISIBLE);
                break;
            case 4:
                item.seller_accepted.setText("OFFER REJECTED BY USER");
                item.seller_accepted.setVisibility(View.VISIBLE);
                item.cross.setVisibility(View.VISIBLE);
                break;
            default:
                item.cross.setVisibility(View.INVISIBLE);
                item.tick.setVisibility(View.INVISIBLE);
                item.present.setVisibility(View.INVISIBLE);
                item.seller_rejected.setVisibility(View.INVISIBLE);
                item.seller_accepted.setVisibility(View.INVISIBLE);
                item.seller_offered.setVisibility(View.INVISIBLE);
                break;
        }
        item.Username.setText(username);
        item.bargainprice.setText("Requested at Rs. "+BargainPrice.get(position));
        item.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bidref.get(position).child("status").setValue(1);
                item.accept.setText("ACCEPTED");
                item.seller_accepted.setVisibility(View.VISIBLE);
                item.tick.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Bargain Request Accepted Successfully.", Toast.LENGTH_SHORT).show();

            }
        });
        item.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bidref.get(position).child("status").setValue(-1);
                item.reject.setClickable(false);
                item.reject.setText("REJECTED");
                item.seller_rejected.setVisibility(View.VISIBLE);
                item.cross.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Bargain Request Rejected.", Toast.LENGTH_SHORT).show();
            }
        });
        item.offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.OfferText));
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 30, 0, 0);

                // SET UP THE OFFER INPUT
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint(context.getResources().getString(R.string.OfferHint));
                input.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                input.setHintTextColor(context.getResources().getColor(R.color.colorPrimary));
                input.setTextSize(15);
                input.getBackground().setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                layout.addView(input);

                //SET UP THE FINAL PRICE INPUT
                final EditText finalprice = new EditText(context);
                finalprice.setInputType(InputType.TYPE_CLASS_NUMBER);
                finalprice.setHint(context.getResources().getString(R.string.FinalPriceText));
                finalprice.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                finalprice.setHintTextColor(context.getResources().getColor(R.color.colorPrimary));
                finalprice.setTextSize(15);
                finalprice.getBackground().setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                layout.addView(finalprice, layoutParams);
                builder.setView(layout);
                // SETUP THE BUTTONS
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String offer = input.getText().toString();
                        Integer price = Integer.parseInt(finalprice.getText().toString());
                        Bidref.get(position).child("status").setValue(2);
                        Bidref.get(position).child("additionals").setValue(offer);
                        Bidref.get(position).child("finalprice").setValue(price);
                        item.offer.setText("OFFERED");
                        item.present.setVisibility(View.VISIBLE);
                        item.seller_offered.setVisibility(View.VISIBLE);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        convertView.setClickable(false);
        return convertView;
    }
    private class ViewItemHolder {
        TextView Username;
        TextView RequestProductName;
        TextView RequestProductPrice;
        TextView bargainprice;
        TextView seller_accepted;
        TextView seller_rejected;
        TextView seller_offered;
        ImageView cross;
        ImageView tick;
        ImageView present;
        Button accept;
        Button reject;
        Button offer;
    }
}