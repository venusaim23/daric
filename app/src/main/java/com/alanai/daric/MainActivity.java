package com.alanai.daric;

import static com.alanai.daric.Utility.Constants.KEY;
import static com.alanai.daric.Utility.Constants.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alanai.daric.Adapters.DiaryAdapter;
import com.alanai.daric.Models.Diary;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import com.alanai.daric.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddDiaryFragment.DiaryInterface, DiaryAdapter.AdapterInterface, DiariesFragment.DiariesInterface {

    //    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

//    private SectionsPagerAdapter sectionsPagerAdapter;

    private DiariesFragment professionalFragment, personalFragment;
    private AddDiaryFragment diaryFragment;
    private FragmentManager fragmentManager;

    private List<Diary> diaries;

//    keep list in main and perform operations through interfaces

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        loadDiaries();

        professionalFragment = new DiariesFragment(this, 0, diaries);
        personalFragment = new DiariesFragment(this, 1, diaries);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, professionalFragment).commit();

//        sectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager,
//                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
//        binding.contentMain.viewPager.setAdapter(sectionsPagerAdapter);
//        binding.tabs.setupWithViewPager(binding.contentMain.viewPager);

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                DiariesFragment fragment = new DiariesFragment(MainActivity.this, tab.getPosition(), diaries);
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                DiariesFragment fragment = new DiariesFragment(MainActivity.this, tab.getPosition(), diaries);
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            }
        });

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                fragmentManager.beginTransaction().replace(R.id.frame_layout,
                        new AddDiaryFragment(binding.tabs.getSelectedTabPosition() == 0, MainActivity.this, diaries))
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void updateItems() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(diaries);
        editor.putString(KEY, json);
        editor.apply();
    }

    private void loadDiaries() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY, null);
        Type type = new TypeToken<ArrayList<Diary>>() {}.getType();
        diaries = gson.fromJson(json, type);

        if (diaries == null)
            diaries = new ArrayList<>();
    }

    private void saveDetails(Diary diary) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        diaries.add(diary);
        String json = gson.toJson(diaries);
        editor.putString(KEY, json);
        editor.apply();
    }

    @Override
    public void refreshItems() {
        loadDiaries();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}