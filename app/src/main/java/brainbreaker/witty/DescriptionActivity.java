package brainbreaker.witty;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.core.SyncPoint;
import com.squareup.picasso.Picasso;

import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.Classes.ProductClass;

public class DescriptionActivity extends AppCompatActivity {
    ProductClass ProductDetails;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        /** ENABLING THE HOME BUTTON ON ACTION BAR **/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**GETTING THE PRODUCT DETAILS FROM THE INTENT**/
        ProductDetails = (ProductClass) getIntent().getSerializableExtra("ProductDetails");
        /**FINDING THE VIEWS FROM THE LAYOUT OF THIS ACTIVITY**/
        ImageView PImage = (ImageView) findViewById(R.id.ProductImageView);
        TextView PName = (TextView) findViewById(R.id.ProductName);
        TextView PPrice = (TextView) findViewById(R.id.ProductPrice);
        TextView PDescription = (TextView) findViewById(R.id.ProductDescription);
        TextView PSeller = (TextView) findViewById(R.id.ProductSeller);
        info.hoang8f.widget.FButton AddTocart = (info.hoang8f.widget.FButton) findViewById(R.id.AddToCart);
        info.hoang8f.widget.FButton BargainButton = (info.hoang8f.widget.FButton) findViewById(R.id.BargainButton);

        // SETTING THE DETAILS IN THE VIEWS
        Picasso.with(DescriptionActivity.this).setIndicatorsEnabled(true);  //only for debug tests
        Picasso.with(DescriptionActivity.this)
                .load(ProductDetails.getImage())
                .placeholder(R.drawable.witty)
                .error(R.drawable.witty)
                .into(PImage);
        PName.setText(ProductDetails.getName());
        PPrice.setText("Rs. "+ProductDetails.getprice());
        PDescription.setText(ProductDetails.getDescription());
        PSeller.setText("SOLD BY: "+ProductDetails.getSeller());

        // Setting Click Listener on Start Chat Button
        final Button ChatButton = (Button) findViewById(R.id.StartChatButton);
        ChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChatIntent = new Intent(DescriptionActivity.this, ChatActivity.class);
                ChatIntent.putExtra("ProductName", ProductDetails.getName());
                ChatIntent.putExtra("SellerName", ProductDetails.getSeller());
                ChatIntent.putExtra("ProductID",ProductDetails.getId());
                DescriptionActivity.this.startActivity(ChatIntent);
            }
        });

        AddTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** MAKING A REFERENCE TO CART URL IN FIREBASE FOR THE VALUES TO BE PUSHED **/

                final Firebase cartRef = new Firebase("https://bargainhawk.firebaseio.com/")
                        .child("Carts");
                /** PUSHING THE PRODUCT TO THE CART DATABASE **/
                cartRef.child(ProductDetails.getId()).setValue(ProductDetails);

                /** CUSTOM TOAST MESSAGE **/
                Toast toast = Toast.makeText(DescriptionActivity.this,"", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.rounded_square);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorWhite));
                text.setText(getResources().getString(R.string.SuccessfulCart));
                toast.show();
                /** SNACKBAR **/
                Snackbar.make(v,getResources().getString(R.string.SNACKBAR_CART),Snackbar.LENGTH_LONG).show();
            }
        });

        BargainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** MAKING A REFERENCE TO BARGAIN REQUESTS CART **/

                final Firebase bargaincartRef = new Firebase("https://bargainhawk.firebaseio.com/")
                        .child("BargainCarts");
                AlertDialog.Builder builder = new AlertDialog.Builder(DescriptionActivity.this);
                builder.setTitle(R.string.BargainQuestion);
                // SET UP THE INPUT
                final EditText input = new EditText(DescriptionActivity.this);
                // Specifying the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setTextColor(getResources().getColor(R.color.colorPrimary));
                input.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                builder.setView(input);

                // SETUP THE BUTTONS
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProductClass bargainproduct = ProductDetails;
                        BargainProductClass BargainProduct = new BargainProductClass(bargainproduct.getName()
                                ,bargainproduct.getImage()
                                ,bargainproduct.getprice()
                                ,Integer.parseInt(input.getText().toString())
                                ,bargainproduct.getDescription()
                                ,bargainproduct.getSeller()
                                ,0
                                ,bargainproduct.getId()
                                ,""
                                ,0
                                ,1
                                , System.currentTimeMillis());
                        /** PUSHING THE BARGAINPRODUCT TO THE BARGAINREQUESTS DATABASE **/
                        bargaincartRef.push().setValue(BargainProduct);
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
    }

    /** WHEN CLICKED ON BACK BUTTON **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // BACK CLICKED. GO TO HOME.
                Intent intent = new Intent(this, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //FINISH THE CURRENT ACTIVITY
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
