package brainbreaker.witty.SellerFragments;

/**
 * Created by brainbreaker.
 */

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.ListIterator;

import brainbreaker.witty.Adapters.BargainRequestsAdapter;
import brainbreaker.witty.Adapters.CustomBargainCartAdapter;
import brainbreaker.witty.Adapters.CustomProductListAdapter;
import brainbreaker.witty.ChatActivity;
import brainbreaker.witty.Adapters.SellerChatListAdapter;
import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.Classes.ProductClass;
import brainbreaker.witty.LoginActivity;
import brainbreaker.witty.R;
import brainbreaker.witty.SellerActivity;
import brainbreaker.witty.UserActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class BargainRequestsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static ArrayList<String> ProductName;
    private static ArrayList<Integer> StatusList;
    private static ArrayList<String> productid;
    private ArrayList<Integer> BargainPrice;
    private ArrayList<Firebase> BidReferences;
    private static String Username;
    private ArrayList<Integer> ActualPrice;
    BargainRequestsAdapter adapter;
    public BargainRequestsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BargainRequestsFragment newInstance() {
        BargainRequestsFragment fragment = new BargainRequestsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bargain_requests_fragment, container, false);
        ((SellerActivity) getActivity())
                .setActionBarTitle("BARGAIN REQUESTS");
        Firebase.setAndroidContext(getActivity());

        BargainPrice = new ArrayList<>();
        BidReferences = new ArrayList<>();
        ProductName = new ArrayList<>();
        ActualPrice = new ArrayList<>();
        StatusList = new ArrayList<>();
        productid = new ArrayList<>();

        final TextView NorequestsTV = (TextView) rootView.findViewById(R.id.Norequests);
        NorequestsTV.setVisibility(View.INVISIBLE);
        final ListView BargainRequestsListView = (ListView) rootView.findViewById(R.id.bargainrequestlistView);


        if (!LoginActivity.getDefaults("UserID",getActivity()).equals("")) {
            Username = LoginActivity.getDefaults("UserID",getActivity());
        } else {
            Username = "user";
        }

        /**PROGRESS BAR**/
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("LOADING BARGAIN REQUESTS ON THIS PRODUCT...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        /** CREATED REF FOR BARGAIN CARTS **/
        Firebase BargainCartRef = new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/");

        BargainCartRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e("child event listener",dataSnapshot.getValue().toString());
                BargainProductClass BargainCartProduct = dataSnapshot.getValue(BargainProductClass.class);
                ProductName.add(BargainCartProduct.getName());
                BargainPrice.add(BargainCartProduct.getbidValue());
                ActualPrice.add(BargainCartProduct.getPrice());
                StatusList.add(BargainCartProduct.getStatus());
                BidReferences.add(new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/"
                                + dataSnapshot.getKey()));
                productid.add(BargainCartProduct.getId());

                adapter = new BargainRequestsAdapter(getActivity(), Username, BargainPrice, ActualPrice,ProductName,StatusList, BidReferences);
                BargainRequestsListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(BargainRequestsFragment.this)
                        .attach(BargainRequestsFragment.newInstance())
                        .commit();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
                Log.e("ON CHILD REMOVED CALLED", dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        BargainRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatintent = new Intent(getActivity(), ChatActivity.class);
                chatintent.putExtra("PRODUCTID", productid.get(position));
                chatintent.putExtra("USERNAME", "seller");
                chatintent.putExtra("PRODUCTNAME", ProductName.get(position));
                getActivity().startActivity(chatintent);
            }
        });
        return rootView;
    }
}