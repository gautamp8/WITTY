package brainbreaker.witty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.Adapters.CustomProductListAdapter;
import brainbreaker.witty.MenuItemListeners.MenuBargainItemListener;
import brainbreaker.witty.MenuItemListeners.MenuCartItemListener;
import brainbreaker.witty.Classes.ProductClass;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progress;
    private int cartindex = 0;
    private int bargaincartindex = 0;
    ListView ProductList;
    private static ArrayList<ProductClass> ProductClassList;
    CustomProductListAdapter adapter;
    TextView cartcounterTV;
    TextView bargaincounterTV;
    /** MAKING A REFERENCE TO PRODUCT DISPLAY FIREBASE **/
    Firebase ProductRef = new Firebase("https://bargainhawk.firebaseio.com/productDisplay");
    /** MAKING A REFERENCE TO CART URL IN FIREBASE FOR THE VALUES TO BE PUSHED **/
    Firebase cartRef = new Firebase("https://bargainhawk.firebaseio.com/")
            .child("Carts");
    /** MAKING A REFERENCE TO BARGAIN REQUESTS CART **/
    Firebase bargaincartRef = new Firebase("https://bargainhawk.firebaseio.com/")
            .child("BargainCarts");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProductList=(ListView)findViewById(R.id.ProductList);
        /**CHECK IF IN ANY CASE USERNAME STRING IS NULL OR EMPTY GO BACK TO LOGIN ACTIVITY**/
        if(LoginActivity.getDefaults("UserID",this)==null || LoginActivity.getDefaults("UserID",this).equals(""))
        {
            Intent intent = new Intent(this,LoginActivity.class);
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**GETTING THE NAVIGATION BAR'S HEADER VIEW**/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        /** SETTING THE TEXTS IN NAVIGATION HEADER **/
        TextView Email = (TextView) header.findViewById(R.id.Email);
        Email.setText(LoginActivity.getDefaults("UserID",this));
        TextView Name = (TextView) header.findViewById(R.id.Name);
        Name.setText(LoginActivity.getDefaults("UserID",this));

        /**RANDOM TIPS GENERATION**/
        TextView UserTips = (TextView) findViewById(R.id.UserTips);
        Resources res = getResources();

        String[] userTips = res.getStringArray(R.array.UserTips);
        Random random = new Random();
        String tip = userTips[random.nextInt(userTips.length)];
        UserTips.setText(tip);

        /** CODE FOR SIGN OUT IN NAVIGATION HEADER **/
        Button SignOut = (Button) header.findViewById(R.id.signout);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signoutintent = new Intent(UserActivity.this, LoginActivity.class);
                LoginActivity.setDefaults("UserID","",UserActivity.this);
                UserActivity.this.startActivity(signoutintent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** INITIALISATION **/
        ProductClassList = new ArrayList<>();

        /** SETTING THE CART COUNTS IF THERE ARE ITEMS IN THE CART AND BARGAIN REQUESTS REFERENCES ALREADY **/
        FirebaseCartCount(cartRef);
        FirebaseBargainCartCount(bargaincartRef);

        /** CREATING AND STARTING THE PROGRESS BAR **/
        progress=new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.Loading1));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        /** ADDING THE VALUE EVENT LISTENER TO PRODUCTREF FOR DISPLAYING THE LIST OF PRODUCTS **/

        ProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductClass post = postSnapshot.getValue(ProductClass.class);
                    System.out.println(post.getName() + " - " + post.getDescription() + "-" + post.getId());
                    ProductClass currentProduct = new ProductClass(post.getName()
                            ,post.getImage()
                            ,post.getprice()
                            ,post.getDescription()
                            ,post.getSeller()
                            ,post.getId()
                            ,post.getCategory());
                    ProductClassList.add(currentProduct);
                }

                if(ProductClassList!=null){
                    /** SETTING UP THE ADAPTER **/
                    adapter = new CustomProductListAdapter(UserActivity.this
                            ,ProductClassList
                            ,getWindowManager().getDefaultDisplay().getWidth()
                            , /** ADD TO CART BUTTON ON CLICK LISTENER **/
                            new CustomProductListAdapter.ButtonClickListener() {
                            @Override
                            public void onButtonClick(int position,View v) {
                                //Custom Toast Display Function defined below - Displays custom toast message
                                CustomToastDisplay(UserActivity.this,getResources().getString(R.string.SuccessfulCart));
                                /**FUNCTION DEFINED IN THE LAST- PUSHING THE PRODUCT TO CART DATABASE**/
                                CartButtonClick(position,v,ProductClassList,cartRef);
                            }
                        }/** BARGAIN BUTTON ON CLICK LISTENER **/
                        ,new CustomProductListAdapter.ButtonClickListener() {
                        @Override
                        public void onButtonClick(final int position, View v) {
                            /**FUNCTION DEFINED AT THE END OF THE CLASS, SHOW AN DIALOG ASKING FOR BARGAIN PRICE AND PUSH IT TO FIRBASE*/
                            BargainButtonAlertDialog(UserActivity.this,position,v,ProductClassList,bargaincartRef);
                        }
                    });
                    /** SET THE ADAPTER IN LISTVIEW AND DISMISS THE PROGRESS BAR**/
                    ProductList.setAdapter(adapter);
                    progress.dismiss();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        /**ONCLICKLISTENER ON THE CARDS-PASSING THE DETAILS VIA INTENT TO DESCRIPTION ACTIVITY**/

        ProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProductClass CurrentProduct = ProductClassList.get(position);
                    Intent DescriptionIntent = new Intent(UserActivity.this, DescriptionActivity.class);
                    DescriptionIntent.putExtra("ProductDetails",CurrentProduct);
                    UserActivity.this.startActivity(DescriptionIntent);
                }
            });
    }

    /** THIS FUNCTION WILL UPDATE THE TOTAL CART COUNT IN FIREBASE **/
    private void UpdateCartCounterInFirebase() {
        Firebase counterRef = new Firebase("https://bargainhawk.firebaseio.com/userData/counts/cartCount");
        counterRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }
            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });
    }

    /**THIS FUNCTION WILL UPDATE THE BARGAIN REQUESTS COUNTER IN FIREBASE**/
    private void UpdateBargainCartCounterInFirebase() {
        Firebase counterRef = new Firebase("https://bargainhawk.firebaseio.com/userData/counts/boxCount");
        counterRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }
            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        // CONFIRMING THAT CORRECT NUMBER OF PRODUCTS ARE DISPLAYED EVEN ON RESTART
        bargaincartindex = BargainCartActivity.getBargainCartListSize();
        cartindex = CartActivity.getCartListSize();
        invalidateOptionsMenu();
        super.onRestart();
    }
    /**HANDLING THE ACTION BAR ITEM CLICKS HERE**/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // INFLATE THE MENU; THIS WILL ADD ITEMS TO ACTION BAR IF THEY ARE PRESENT.
        getMenuInflater().inflate(R.menu.user, menu);
        View menu_item_witty = menu.findItem(R.id.action_witty).getActionView();
        View menu_item_cart = menu.findItem(R.id.action_cart).getActionView();
        cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        bargaincounterTV = (TextView) menu_item_witty.findViewById(R.id.wittycounter);

        /**UPDATING INITIAL THE CART COUNTS AND ONMENUITEMLISTENER FOR MENU_WITTY ITEM**/

        UpdateCartCount(cartindex);
        new MenuBargainItemListener(menu_item_witty, "Show message") {
            @Override
            public void onClick(View v) {
                Intent Witty_Intent = new Intent(UserActivity.this,BargainCartActivity.class);
                UserActivity.this.startActivity(Witty_Intent);
            }
        };

        /**UPDATING INITIAL THE BARGAIN REQUEST COUNTS AND ONMENUITEMLISTENER FOR MENU_CART ITEM**/
        UpdateBargainCartCount(bargaincartindex);
        new MenuCartItemListener(menu_item_cart, "Show message") {
            @Override
            public void onClick(View v) {
                Intent Cart_Intent = new Intent(UserActivity.this,CartActivity.class);
                UserActivity.this.startActivity(Cart_Intent);
            }
        };

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // NAVIGATION BAR ITEM CLICKS ARE HANDLED HERE
        int id = item.getItemId();
        final ArrayList<ProductClass> RefinedProductList = new ArrayList<>();
        if (id == R.id.nav_all) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                RefinedProductList.add(currentProduct);
            }
        }
        if (id == R.id.nav_electronics) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Electronics")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("ELECTRONICS");
        } else if (id == R.id.nav_mobiles) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Mobiles")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("MOBILES");
        } else if (id == R.id.nav_fashion) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Fashion")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("FASHION");
        } else if (id == R.id.nav_footwear) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Footwears")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("FOOTWEAR");
        } else if (id == R.id.nav_laptops) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Laptops")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("LAPTOPS");
        }
        else if (id == R.id.nav_sports) {
            for (int i = 0; i< ProductClassList.size();i++){
                ProductClass currentProduct = ProductClassList.get(i);
                if (currentProduct.getCategory().equals("Sports")){
                    RefinedProductList.add(currentProduct);
                }
            }
            setActionBarTitle("SPORTS");
        } else if (id == R.id.nav_source) {

        } else if (id == R.id.nav_contact) {

        }
        adapter = new CustomProductListAdapter(UserActivity.this,RefinedProductList,getWindowManager().getDefaultDisplay().getWidth()
                , /** ADD TO CART BUTTON ON CLICK LISTENER **/
                new CustomProductListAdapter.ButtonClickListener() {
                    @Override
                    public void onButtonClick(int position,View v) {
                        //Custom Toast Display Function defined below - Displays custom toast message
                        CustomToastDisplay(UserActivity.this,getResources().getString(R.string.SuccessfulCart));
                        /**FUNCTION DEFINED IN THE LAST- PUSHING THE PRODUCT TO CART DATABASE**/
                        CartButtonClick(position,v,RefinedProductList,cartRef);
                    }
                }/** BARGAIN BUTTON ON CLICK LISTENER **/
                ,new CustomProductListAdapter.ButtonClickListener() {
            @Override
            public void onButtonClick(final int position, View v) {
                /**FUNCTION DEFINED AT THE END OF THE CLASS, SHOW AN DIALOG ASKING FOR BARGAIN PRICE AND PUSH IT TO FIRBASE*/
                BargainButtonAlertDialog(UserActivity.this,position,v,RefinedProductList,bargaincartRef);
            }
        });

        ProductList.setAdapter(adapter);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**FUNCTION TO UPDATE CART COUNTS IN ACTION BAR USING FIREBASE**/

    public void FirebaseCartCount(Firebase ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UpdateCartCount((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /**FUNCTION TO UPDATE BARGAIN REQUESTS COUNTS IN ACTION BAR USING FIREBASE**/

    public void FirebaseBargainCartCount(Firebase ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UpdateBargainCartCount((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /**FUNCTION TO UPDATE CART COUNTS IN ACTION BAR IN OFFLINE MODE**/
    public void UpdateCartCount(final int new_number) {
        cartindex = new_number;
        if (cartcounterTV == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_number == 0)
                    cartcounterTV.setVisibility(View.INVISIBLE);
                else {
                    cartcounterTV.setVisibility(View.VISIBLE);
                    cartcounterTV.setText(Integer.toString(new_number));
                }
            }
        });
    }

    /**FUNCTION TO UPDATE BARGAIN REQUESTS COUNTS IN ACTION BAR IN OFFLINE MODE**/
    public void UpdateBargainCartCount(final int new_number) {
        bargaincartindex = new_number;
        if (bargaincounterTV == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_number == 0)
                    bargaincounterTV.setVisibility(View.INVISIBLE);
                else {
                    bargaincounterTV.setVisibility(View.VISIBLE);
                    bargaincounterTV.setText(Integer.toString(new_number));
                }
            }
        });
    }

    /**FUNCTION TO SHOW CUSTOM TOAST MESSAGE**/
    public static void CustomToastDisplay(Context context, String Message){
        /** CUSTOM TOAST MESSAGE **/
        Toast toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.rounded_square);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.colorWhite));
        text.setText(Message);
        toast.show();
    }

    public void CartButtonClick(int position, View v,ArrayList<ProductClass> ProductList , Firebase ref){
        TextView addedInfo = (TextView) v.findViewById(R.id.addedInfo);
        addedInfo.setVisibility(View.VISIBLE);
        addedInfo.setText("This product is in your cart");
        UpdateCartCount(++cartindex);
        ProductClass Product = ProductList.get(position);
        // WE HAVE DEFINED NEW VARIABLE FOR CART PRODUCT AS WE ALSO HAVE TO ADD QUANTITY TO CART
        ProductClass CartProduct = new ProductClass(Product.getName()
                ,Product.getImage()
                ,Product.getprice()
                ,Product.getDescription()
                ,Product.getSeller()
                ,Product.getId()
                ,1);
        /** PUSHING THE PRODUCT TO THE CART DATABASE **/
        ref.child(CartProduct.getId()).setValue(CartProduct);
        //THIS FUNCTION WILL UPDATE THE CART COUNTER NODE IN OUR DATABASE
        UpdateCartCounterInFirebase();
    }

    public void BargainButtonAlertDialog(Context context, final int position, View v, final ArrayList<ProductClass> ProductList , final Firebase ref){
        final TextView addedInfo = (TextView) v.findViewById(R.id.addedInfo);
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle(R.string.BargainQuestion);
        // SET UP THE INPUT
        final EditText input = new EditText(context);
        // Specifying the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(getResources().getColor(R.color.colorPrimary));
        input.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        builder.setView(input);

        // SETUP THE BUTTONS
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProductClass bargainproduct = ProductList.get(position);
                // A CHECK IF THE BARGAIN PRICE REQUESTED IS NOT LESS THAN 40% OR TWICE OF PRODUCT PRICE
                if((Integer.parseInt(input.getText().toString()))< (0.4*bargainproduct.getprice()) || (Integer.parseInt(input.getText().toString()))> (2*bargainproduct.getprice())){
                    CustomToastDisplay(UserActivity.this,getResources().getString(R.string.bargainError));
                }
                else{
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
                            ,System.currentTimeMillis());
                    /** PUSHING THE BARGAINPRODUCT TO THE BARGAINREQUESTS DATABASE **/
                    ref.push().setValue(BargainProduct);
                    //THIS FUNCTION WILL UPDATE THE BARGAIN REQUEST COUNTER NODE IN OUR DATABASE
                    UpdateBargainCartCounterInFirebase();
                    bargaincartindex = bargaincartindex + 1;
                    UpdateBargainCartCount(bargaincartindex);
                    //MAKING ADDED INFO TEXT VIEW VISIBLE
                    addedInfo.setVisibility(View.VISIBLE);
                    addedInfo.setText("Bargain Requested. Click 'W' icon on the top for more details");
                }
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
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
