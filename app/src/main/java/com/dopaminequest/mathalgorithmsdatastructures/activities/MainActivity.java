package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.fragments.AlgorithmsTabFragment;
import com.dopaminequest.mathalgorithmsdatastructures.fragments.DataStructuresTabFragment;
import com.dopaminequest.mathalgorithmsdatastructures.fragments.MathTabFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private AlgorithmsTabFragment algorithmsTabFragment;
    private DataStructuresTabFragment dataStructuresTabFragment;
    private MathTabFragment mathTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        algorithmsTabFragment = new AlgorithmsTabFragment();
        dataStructuresTabFragment = new DataStructuresTabFragment();
        mathTabFragment = new MathTabFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        viewPagerAdapter.addFragment(mathTabFragment, "Math");
        viewPagerAdapter.addFragment(algorithmsTabFragment, "Algorithms");
        viewPagerAdapter.addFragment(dataStructuresTabFragment, "Data Structures");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(1).setIcon(R.drawable.algorithms_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.data_structures_24dp);
        tabLayout.getTabAt(0).setIcon(R.drawable.e_math_24dp);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

}
