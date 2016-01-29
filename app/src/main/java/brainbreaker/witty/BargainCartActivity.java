package brainbreaker.witty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.Adapters.CustomBargainCartAdapter;
import brainbreaker.witty.Adapters.CustomProductListAdapter;
import brainbreaker.witty.Classes.ProductClass;

public class BargainCartActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private static ArrayList<BargainProductClass> BargainProductList;
    private static CustomBargainCartAdapter adapter;
    private ArrayList<String> BargainID;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargain_cart);
        /**SETTING THE TOOLBAR AS ACTION BAR**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**ENABLING THE BACK BUTTON**/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Firebase.setAndroidContext(this);
        /**INITIALISATION OF GLOBAL VARIABLES**/
        BargainProductList = new ArrayList<>();
        BargainID = new ArrayList<>();
        /**GETTING LIST VIEW FROM ACTIVITY LAYOUT**/
        final ListView bargainlistView = (ListView) findViewById(R.id.hawklistView);

        /**PROGRESS BAR**/
        progress=new ProgressDialog(this);
        progress.setMessage("LOADING BARGAIN CART...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        /**CREATING THE FIREBASE REFERENCE TO THE BARGAIN REQUESTS PART**/
        final Firebase BargainCartRef = new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/");

        /**RETRIEVING THE BARGAIN REQUESTS AND UPDATING THE BARGAINCART ARRAYLIST**/
        BargainCartRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("child event listener",dataSnapshot.getValue().toString());

                    BargainProductClass BargainCartProduct = dataSnapshot.getValue(BargainProductClass.class);
                    //Defining new variable so that we can access data according to sequence defined in class constructor
                    BargainProductClass bargainedproduct = new BargainProductClass(BargainCartProduct.getName()
                            ,BargainCartProduct.getImage()
                            ,BargainCartProduct.getPrice()
                            ,BargainCartProduct.getbidValue()
                            ,BargainCartProduct.getDescription()
                            ,BargainCartProduct.getSeller()
                            ,BargainCartProduct.getStatus()
                            ,BargainCartProduct.getId()
                            ,BargainCartProduct.getAdditionals()
                            ,BargainCartProduct.getFinalprice()
                            ,BargainCartProduct.getQuantity()
                            ,BargainCartProduct.getTimestamp());
                    BargainProductList.add(bargainedproduct);
                    BargainID.add(dataSnapshot.getKey());

                if(BargainProductList.isEmpty()){
                    progress.dismiss();
                    /** CUSTOM TOAST MESSAGE **/
                    Toast toast = Toast.makeText(BargainCartActivity.this,"", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.rounded_square);
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    text.setText(" YOU HAVEN'T USED YOUR BARGAINING SKILLS YET! ");
                    text.setTextColor(getResources().getColor(R.color.colorWhite));
                    toast.show();
                    Intent intent = new Intent(BargainCartActivity.this, UserActivity.class);
                    BargainCartActivity.this.startActivity(intent);
                }
                else {
                    adapter = new CustomBargainCartAdapter(BargainProductList, BargainID, getLayoutInflater(), BargainCartActivity.this, new CustomProductListAdapter.ButtonClickListener() {
                        /**
                         * CODE FOR DELETE BUTTON ON EACH ITEM- CREATED THE FIREBASE REFERENCE AND REMOVED VALUE FROM IT, RELOADED THE ACTIVITY
                         **/
                        @Override
                        public void onButtonClick(int position, View v) {
                            Firebase deleteref = new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/"
                                    + BargainID.get(position));
                            deleteref.removeValue();
                            Snackbar.make(bargainlistView, getResources().getString(R.string.Deleted_Bargain), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            /**FINISHED AND RELOADED THE ACTIVITY AFTER DELETION**/
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }     /**ON CLICK LISTENER FOR THE 'MOVE TO CART' LINEAR LAYOUT **/
                            , new CustomProductListAdapter.ButtonClickListener() {
                        @Override
                        public void onButtonClick(int position,View v) {
                            /**GETTING THE CURRENT BARGAIN REQUEST PRODUCT AND MOVING IT TO CART**/
                            Firebase CartRef = new Firebase("https://bargainhawk.firebaseio.com/")
                                    .child("Carts");
                            /**TAKE THE CURRENT PRODUCT OF BARGAINPRODUCTCLASS AND RETRIEVE VALUES FROM IT.**/
                            BargainProductClass currentProduct = BargainProductList.get(position);

                            ProductClass ProductCart = new ProductClass(currentProduct.getName()
                                    , currentProduct.getImage()
                                    , currentProduct.getbidValue()
                                    , currentProduct.getDescription(), currentProduct.getSeller(), currentProduct.getId(),currentProduct.getQuantity());
                            CartRef.child(currentProduct.getId()).setValue(ProductCart);

                            /** CUSTOM TOAST MESSAGE **/
                            Toast toast = Toast.makeText(BargainCartActivity.this, "", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            view.setBackgroundResource(R.drawable.rounded_square);
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            text.setText(" Product successfully moved to cart ");
                            text.setTextColor(getResources().getColor(R.color.colorWhite));
                            toast.show();
                            /**SNACKBAR MESSAGE**/
                            Snackbar.make(bargainlistView, getResources().getString(R.string.SuccessfulCart), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            /**DELETING THE BARGAIN REQUEST**/
                            Firebase deleteref = new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/" + BargainID.get(position));
                            deleteref.removeValue();

                            /**FINISHED AND RELOADED THE ACTIVITY AFTER DELETION**/
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
                }
                /**SETTING THE ADAPTER IN THE LIST VIEW**/
                bargainlistView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Log.e("ONNCHILD CHANGED CALLED", dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Log.e("ONNCHILD REMOVED CALLED", dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**THIS IS THE CODE FOR OPENING UP OF DESCRIPTION ACTIVITY WHEN USER CLICKS THE ITEM IN THE BARGAIN REQUEST LIST**/
        bargainlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent DescriptionIntent = new Intent(BargainCartActivity.this, DescriptionActivity.class);
                DescriptionIntent.putExtra("ProductDetails",BargainProductList.get(position));
                BargainCartActivity.this.startActivity(DescriptionIntent);
            }
        });
    }

    /**GET THE BARGAIN CART LIST SIZE SO THAT IT IS UPDATED EVEN IN OFFLINE MODE**/
    public static int getBargainCartListSize() {
        try {
            if (BargainProductList.size() != 0) {
                return BargainProductList.size();
            } else {
                return 0;
            }
        } catch (NullPointerException e) {
            return 0;
        }
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
