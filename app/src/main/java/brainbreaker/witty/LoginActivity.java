package brainbreaker.witty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class LoginActivity extends AppCompatActivity{
    // UI references.
    private AutoCompleteTextView mUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // REMOVE TITLE AND MAKE ACTIVITY FULLSCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.welcome_app_name));
        setContentView(R.layout.activity_login);

        /**SHIMMERING TEXT VIEW ANIMATIONS**/
        ShimmerTextView tagline = (ShimmerTextView) findViewById(R.id.Tagline);
        ShimmerTextView tagline2 = (ShimmerTextView) findViewById(R.id.Tagline2);
        ShimmerTextView tagline3 = (ShimmerTextView) findViewById(R.id.Tagline3);
        Shimmer shimmer = new Shimmer();
        Shimmer shimmer1 = new Shimmer();
        Shimmer shimmer2 = new Shimmer();
        shimmer.setRepeatCount(500)
                .setDuration(3000)
                .setStartDelay(0)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .start(tagline);
        shimmer1.setRepeatCount(500)
                .setDuration(3000)
                .setStartDelay(0)
                .setDirection(Shimmer.ANIMATION_DIRECTION_RTL)
                .start(tagline2);
        shimmer2.setRepeatCount(500)
                .setDuration(3000)
                .setStartDelay(0)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .start(tagline3);

        mUserView = (AutoCompleteTextView) findViewById(R.id.loginusername);

        /** FACEBOOK BUTTON **/
        ImageButton facebook =(ImageButton) findViewById(R.id.fb);
        facebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(getResources().getString(R.string.facebook));
            }
        });
        /** EMAIL BUTTON **/
        ImageButton googleMail =(ImageButton) findViewById(R.id.gmail);
        googleMail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" +getResources().getString(R.string.googlemail) ));
                startActivity(intent);
            }
        });
        /** LINKEDIN BUTTON **/
        ImageButton Linkedin =(ImageButton) findViewById(R.id.linkedin);
        Linkedin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(getResources().getString(R.string.linkedin));
            }
        });
        /** GITHUB BUTTON **/
        ImageButton Github =(ImageButton) findViewById(R.id.github);
        Github.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(getResources().getString(R.string.github));
            }
        });


        /** ONCLICKLISTENER ON VIEW AS USER BUTTON **/
        Button mSignInButtonUser = (Button) findViewById(R.id.sign_in_user);
        mSignInButtonUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserView.getText().toString().trim().equalsIgnoreCase(""))
                    mUserView.setError("Please let us know your Name");
                else {
                    setDefaults("UserID", mUserView.getText().toString(), LoginActivity.this);
                    Intent userintent = new Intent(LoginActivity.this, UserActivity.class);
                    LoginActivity.this.startActivity(userintent);
                }
            }
        });
        /** ONCLICKLISTENER ON VIEW AS SELLER BUTTON **/
        Button mSignInButtonSeller = (Button) findViewById(R.id.sign_in_seller);
        mSignInButtonSeller.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellerintent = new Intent(LoginActivity.this, SellerActivity.class);
                LoginActivity.this.startActivity(sellerintent);
            }
        });

    }

    /** FUNCTIONS FOR SETTING AND GETTING LOGIN IFNO IN/FROM SHARED PREFS **/
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    // OPENING OF BROWSER FOR PROFILE URLS
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}

