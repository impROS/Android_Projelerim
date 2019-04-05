package impros.cale.yksgerisayim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import impros.cale.yksgerisayim.Fragments.frgHomePage;
import impros.cale.yksgerisayim.Fragments.frgNotifications;
import impros.cale.yksgerisayim.Fragments.frgSettings;
import impros.cale.yksgerisayim.Helpers.UI;
import impros.cale.yksgerisayim.Notification.AlarmReceiver;
import impros.cale.yksgerisayim.Notification.NotificationHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imgMenu;
    Fragment selectedFragment = null;

    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //** Init_Area
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        act = MainActivity.this;
        setSelectedFragment(new frgHomePage());

        UI.initSettings(act);
        //END_Init_Area

        //** findview_area
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //ENDfindview_area

/*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView.setNavigationItemSelectedListener(this);
        */
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        setupActionBar();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.home_page:
                            selectedFragment = new frgHomePage();
                            break;
                        case R.id.notifications:
                            selectedFragment = new frgNotifications();
                            break;
                        case R.id.settings:
                            selectedFragment = new frgSettings();
                            break;
                    }
                    setSelectedFragment(selectedFragment);
                    return true;
                }
            };

    public void setSelectedFragment(Fragment selectedFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, selectedFragment)
                .commit();
    }

    static ViewGroup actionBarLayout;

    private void setupActionBar() {

        actionBarLayout = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.action_bar, null);

        TextView txtDate = actionBarLayout.findViewById(R.id.txtDate);
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        txtDate.setText(date);

        changeUserName(UI.getSettings(act).getUserName());
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.FILL_PARENT);
        actionBar.setCustomView(actionBarLayout, layout);
    }

    public static void changeUserName(String username) {
        TextView txtUserName = actionBarLayout.findViewById(R.id.txtUserNameT);
        txtUserName.setText("Hoşgeldiniz "+username);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
/*
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        */
        return true;
    }

}
