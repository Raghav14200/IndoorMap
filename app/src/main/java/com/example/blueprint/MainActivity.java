package com.example.blueprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private Print map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.include);
        map=findViewById(R.id.map);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Navigate");
    }

//    public void setSource(View view){
//        System.out.println("button clicked");
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("testing","tested");
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem.OnActionExpandListener onActionExpandListener=new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView=(SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search Destination...");

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.addLocation:
                map.setLoc(1);
                Toast.makeText(this, "Tap source location in map", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addDestination:
                map.setLoc(-1);
                Toast.makeText(this, "Tap destination location in map", Toast.LENGTH_SHORT).show();
        }
        return  super.onOptionsItemSelected(item);
    }

}