package brainbreaker.witty;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import brainbreaker.witty.Classes.BargainProductClass;
import brainbreaker.witty.SellerFragments.AddProductFragment;
import brainbreaker.witty.SellerFragments.BargainRequestsFragment;
import brainbreaker.witty.SellerFragments.ChatFragment;
import brainbreaker.witty.SellerFragments.ProductFragment;
import brainbreaker.witty.SellerFragments.UserDemandsFragment;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initial View
        Fragment fragment = ProductFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        /**GETTING THE NAVIGATION BAR'S HEADER VIEW**/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        /** CODE FOR SIGN OUT IN NAVIGATION HEADER **/
        Button SignOut = (Button) header.findViewById(R.id.signoutseller);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signoutintent = new Intent(SellerActivity.this, LoginActivity.class);
                LoginActivity.setDefaults("UserID","",SellerActivity.this);
                SellerActivity.this.startActivity(signoutintent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            Intent signoutintent = new Intent(SellerActivity.this, LoginActivity.class);
            LoginActivity.setDefaults("UserID","",SellerActivity.this);
            SellerActivity.this.startActivity(signoutintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_products) {
            fragment = ProductFragment.newInstance();
            transaction.replace(R.id.fragment_container, fragment);
        }else if (id == R.id.nav_requests) {
            fragment = BargainRequestsFragment.newInstance();
            transaction.replace(R.id.fragment_container, fragment);
        } else if (id == R.id.nav_messages) {
            fragment = ChatFragment.newInstance();
            transaction.replace(R.id.fragment_container, fragment);
        } else if (id == R.id.nav_demands) {
            fragment = UserDemandsFragment.newInstance();
            transaction.replace(R.id.fragment_container, fragment);
        } else if (id == R.id.nav_addProduct) {
            fragment = AddProductFragment.newInstance();
            transaction.replace(R.id.fragment_container, fragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


}
