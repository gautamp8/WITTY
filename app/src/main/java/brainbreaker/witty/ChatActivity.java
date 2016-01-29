package brainbreaker.witty;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import brainbreaker.witty.ChatClasses.Chat;
import brainbreaker.witty.ChatClasses.ChatListAdapter;

public class ChatActivity extends ListActivity {

    private static final String FIREBASE_URL = "https://bargainhawk.firebaseio.com/";

    private static String mUsername;
    private static String ProductID;
    private static EditText inputText;
    private static Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inputText = (EditText) findViewById(R.id.messageInput);
        // Make sure we have a mUsername
        setupUsername();
        TextView ChatInfo =(TextView) findViewById(R.id.ChatInfo);
        ChatInfo.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        if (getIntent().getStringExtra("SellerName")!=null && getIntent().getStringExtra("ProductName") !=null) {
            ChatInfo.setText(getIntent().getStringExtra("SellerName") + "-" + getIntent().getStringExtra("ProductName"));
        }
        else {
            ChatInfo.setText("User" + "-" + getIntent().getStringExtra("PRODUCTNAME"));
        }
        ProductID = getIntent().getStringExtra("ProductID");

        // Setup our Firebase mFirebaseRef
        if(ProductID!=null) {
            mFirebaseRef = new Firebase(FIREBASE_URL).child("chats").child(ProductID);
        }
        else {
            mFirebaseRef = new Firebase(FIREBASE_URL).child("chats").child(getIntent().getStringExtra("PRODUCTID"));
        }

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        final EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage(inputText,mUsername,mFirebaseRef);
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(inputText,mUsername,mFirebaseRef);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();

        /** CREATING AND STARTING THE PROGRESS BAR **/
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("LOADING MESSAGES...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                 if (connected) {
                    Toast.makeText(ChatActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                     /**AFTER SETTING THE CHAT LIST DISMISS THE PROGRESS BAR**/
                     progress.dismiss();
                } else {
                    Toast.makeText(ChatActivity.this, "Disconnected. You need Internet connection to send and receive messages!", Toast.LENGTH_SHORT).show();
                     progress.dismiss();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println(firebaseError);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        if(getIntent().getStringExtra("Seller")!=null){
            mUsername = getIntent().getStringExtra("Seller");
        }
        else {
            mUsername = "user";
        }
    }

    public static void sendMessage(EditText chatInput, String mUsername, Firebase FirebaseRef) {

        String input = chatInput.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            long timestamp = System.currentTimeMillis();
            Chat chat = new Chat(input, mUsername,timestamp);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            FirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }


}
