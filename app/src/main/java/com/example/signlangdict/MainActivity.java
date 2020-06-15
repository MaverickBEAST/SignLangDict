package com.example.signlangdict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this );

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//login user refresh
        navigationView.setCheckedItem(R.id.navSearch);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Images");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navSearch:
                startActivity(new Intent(MainActivity.this , MainActivity.class));
                break;
            case R.id.navUpload:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    startActivity(new Intent(MainActivity.this, UploadActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }else {
                    Toast.makeText(MainActivity.this, "You're not logged in, Please Log in to Upload", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this , LoginActivity.class));
                }
                break;
            case R.id.navRegister:
                startActivity(new Intent(MainActivity.this , RegisterActivity.class));
                break;
            case R.id.navLogin:
                FirebaseUser user3 = FirebaseAuth.getInstance().getCurrentUser();
                if (user3!=null){
                    startActivity(new Intent(MainActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    Toast.makeText(MainActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(MainActivity.this , LoginActivity.class));
                }
                break;
            case R.id.navLogout:
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if (user1!=null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "You're not logged in", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {

            super.onBackPressed();
        }

    }

    private void firebaseSearch(String searchText){

        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query+"\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.row,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
                viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getMeaning(), model.getImage());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.row,
                ViewHolder.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
                viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getMeaning(), model.getImage());
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem item1 = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

