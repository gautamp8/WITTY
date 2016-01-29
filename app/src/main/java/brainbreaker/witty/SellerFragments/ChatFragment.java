package brainbreaker.witty.SellerFragments;

/**
 * Created by brainbreaker.
 */

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import brainbreaker.witty.ChatActivity;
import brainbreaker.witty.Adapters.SellerChatListAdapter;
import brainbreaker.witty.R;
import brainbreaker.witty.SellerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayList<String> ProductId = new ArrayList<>();
    ArrayList<Firebase> statusrefs = new ArrayList<>();
    ArrayList<Firebase> ProductRefs = new ArrayList<>();
    ProgressDialog progress;
    public ChatFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.seller_chat_fragment, container, false);
        ((SellerActivity) getActivity())
                .setActionBarTitle("MESSAGES");
        Firebase.setAndroidContext(getActivity());
        final ListView ChatListView = (ListView)  rootView.findViewById(R.id.ChatListView);

        /**PROGRESS BAR**/
        progress = new ProgressDialog(getActivity());
        progress.setMessage("LOADING YOUR MESSAGES...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        final Firebase ChatRef = new Firebase("https://bargainhawk.firebaseio.com/chats/");
        ChatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProductId.add(dataSnapshot.getKey());
                /**MAKING THE REFERENCE TO THE NAME OF PRODUCT USING PRODUCT ID WHICH IS TO BE USED IN GETVIEW OF ADAPTER.**/
                ProductRefs.add(new Firebase("https://bargainhawk.firebaseio.com/productDisplay/"
                            + dataSnapshot.getKey()));
                System.out.println(ProductRefs);
                /**MAKING THE REFERENCE TO THE STATUS OF PRODUCT WHICH IS TO BE USED IN GETVIEW OF ADAPTER.**/
                statusrefs.add(new Firebase("https://bargainhawk.firebaseio.com/BargainCarts/"
                            + dataSnapshot.getKey() + "/"
                            + "status"));

                if (ProductId.isEmpty()){
                    TextView noMessages = (TextView) rootView.findViewById(R.id.noMessages);
                    noMessages.setVisibility(View.VISIBLE);
                }
                else {
                    System.out.println(ProductRefs);
                    System.out.println(statusrefs);
                }
                SellerChatListAdapter adapter = new SellerChatListAdapter(getActivity(), ProductRefs, statusrefs, ProductId);
                ChatListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("2 "+dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("3 "+dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView itemname = (TextView) view.findViewById(R.id.chattitle);
                Intent chatintent = new Intent(getActivity(), ChatActivity.class);
                chatintent.putExtra("PRODUCTID", ProductId.get(position));
                chatintent.putExtra("Seller", "seller");
                chatintent.putExtra("PRODUCTNAME", itemname.getText().toString());
                getActivity().startActivity(chatintent);
            }
        });
        return rootView;
    }
}