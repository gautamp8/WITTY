package brainbreaker.witty.ChatClasses;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

import brainbreaker.witty.R;

/**

 * using FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        Long timestamp = chat.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(timestamp);
        System.out.println(sdf.format(resultdate));

        Spannable AuthorSpan = new SpannableString(author);
        AuthorSpan.setSpan(new ForegroundColorSpan(Color.rgb(255,64,129)),0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) view.findViewById(R.id.message)).setText(AuthorSpan + ": " + chat.getMessage());

        ((TextView) view.findViewById(R.id.timestamp)).setText(sdf.format(resultdate));

    }
}
