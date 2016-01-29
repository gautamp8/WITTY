package brainbreaker.witty.SellerFragments;

/**
 * Created by brainbreaker.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import brainbreaker.witty.Classes.ProductClass;
import brainbreaker.witty.Adapters.SellerProductListAdapter;
import brainbreaker.witty.R;
import brainbreaker.witty.SellerActivity;
import brainbreaker.witty.SellerDetailsActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static ArrayList<String> ProductName;
    private static ArrayList<String> ProductURL;
    private static ArrayList<Integer> ProductPrice;
    private static ArrayList<String> ProductDescription;
    private static ArrayList<String> ProductSeller;
    private static ArrayList<String> ProductCategory;
    private static ArrayList<String> ProductId;
    private ProgressDialog progress;
    public ProductFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.seller_product_fragment, container, false);
        ((SellerActivity) getActivity())
                .setActionBarTitle("YOUR PRODUCTS");
        Firebase.setAndroidContext(getActivity());
        /** INITIALISATIONS **/
        ProductName = new ArrayList<>();
        ProductURL = new ArrayList<>();
        ProductPrice = new ArrayList<>();
        ProductDescription = new ArrayList<>();
        ProductCategory = new ArrayList<>();
        ProductSeller = new ArrayList<>();
        ProductId = new ArrayList<>();
        final ListView ProductListView = (ListView) rootView.findViewById(R.id.SellerProductList);
        final TextView NumberOfBargains = (TextView) rootView.findViewById(R.id.numberofbargain);

        /** CREATING AND STARTING THE PROGRESS BAR **/
        progress=new ProgressDialog(getActivity());
        progress.setMessage(getResources().getString(R.string.Loading2));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        /** MAKING A REF TO PRODUCT DISPLAY URL OF FIREBASE **/
        Firebase ProductDisplayRef = new Firebase("https://bargainhawk.firebaseio.com/productDisplay");


        ProductDisplayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ProductClass post = postSnapshot.getValue(ProductClass.class);
                    System.out.println(post.getName() + " - " + post.getDescription()+ "-" + post.getprice() + "-"+ post.getId());
                    ProductName.add(post.getName());
                    ProductURL.add(post.getImage());
                    ProductPrice.add(post.getprice());
                    ProductDescription.add(post.getDescription());
                    ProductSeller.add(post.getSeller());
                    ProductCategory.add(post.getCategory());
                    ProductId.add(post.getId());
                    System.out.println("INTENsjnsjsdnjs"+ProductId);
                }
                SellerProductListAdapter Adapter = new SellerProductListAdapter(getActivity(),ProductName,ProductURL,ProductPrice,
                        getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                        new SellerProductListAdapter.ButtonClickListener() {
                            @Override
                            public void onButtonClick(int position) {
                                System.out.println("INTENT PROddsfd"+position +" "+ProductId.get(position));
                                ProductClass IntentProduct = new ProductClass(ProductName.get(position)
                                        ,ProductURL.get(position)
                                        ,ProductPrice.get(position)
                                        ,ProductDescription.get(position)
                                        ,ProductSeller.get(position)
                                        ,ProductId.get(position)
                                        ,ProductCategory.get(position));

                                System.out.println("INTENT PRODUCT PRODUCT ID"+ IntentProduct.getId());
                                Intent sellerDetailIntent = new Intent(getActivity(), SellerDetailsActivity.class);
                                sellerDetailIntent.putExtra("IntentProduct",IntentProduct);
                                getActivity().startActivity(sellerDetailIntent);
                            }
                        });
                Adapter.notifyDataSetChanged();
                ProductListView.setAdapter(Adapter);
                progress.setProgress(100);
                progress.dismiss();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Firebase bargaincounterref = new Firebase("https://bargainhawk.firebaseio.com/userData/counts/boxCount");
        bargaincounterref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NumberOfBargains.setText("You have "+ dataSnapshot.getValue().toString()+ " pending requests on your products");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println(firebaseError);
            }
        });
        return rootView;
    }
}