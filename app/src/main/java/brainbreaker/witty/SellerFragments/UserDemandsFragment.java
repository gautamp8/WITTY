package brainbreaker.witty.SellerFragments;

/**
 * Created by brainbreaker.
 */

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import brainbreaker.witty.Adapters.CustomProductListAdapter;
import brainbreaker.witty.Adapters.DemandListAdapter;
import brainbreaker.witty.Classes.DemandProductClass;
import brainbreaker.witty.R;
import brainbreaker.witty.SellerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserDemandsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayList<DemandProductClass> DemandProductList = new ArrayList<>();
    public UserDemandsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserDemandsFragment newInstance() {
        UserDemandsFragment fragment = new UserDemandsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((SellerActivity) getActivity())
                .setActionBarTitle("USER DEMANDS");
        View rootView = inflater.inflate(R.layout.demand_fragment, container, false);
        Firebase.setAndroidContext(getActivity());

        final ListView demandlist = (ListView) rootView.findViewById(R.id.demandlistView);

        /**PROGRESS BAR**/
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("LOADING USER DEMANDS...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        //POPULATING DEMAND PRODUCT LIST FROM FIREBASE. IT CAN ONLY BE UPDATED FROM WEBSITE YET
        Firebase ProductDemandRef = new Firebase("https://bargainhawk.firebaseio.com/demands/");
        ProductDemandRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final DemandProductClass product = dataSnapshot.getValue(DemandProductClass.class);
                DemandProductList.add(product);
                DemandListAdapter adapter = new DemandListAdapter(DemandProductList, inflater, getActivity(), new CustomProductListAdapter.ButtonClickListener() {
                    @Override
                    public void onButtonClick(int position, View view) {
//                        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
//                        viewPager.setCurrentItem(2,true);
//                        TextView ProductName = (TextView) viewPager.findViewById(R.id.demandproductname);
//                        ProductName.setText(product.getName());
                    }
                });
                demandlist.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }
}