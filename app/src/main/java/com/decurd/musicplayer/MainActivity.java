package com.decurd.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.decurd.musicplayer.fragments.ListViewFragment;
import com.decurd.musicplayer.fragments.PlayerFragment;
import com.decurd.musicplayer.fragments.SongFragment;

public class MainActivity extends AppCompatActivity {

    private PlayerFragment mPlayerFragment;
    private ListViewFragment mListViewFragment;
    private SongFragment mSongFrament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        requester.create().request(Manifest.permission.READ_EXTERNAL_STORAGE, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(MainActivity.this, "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mPlayerFragment = new PlayerFragment();
        mListViewFragment = new ListViewFragment();
        mSongFrament = new SongFragment();

        MusicPlayerPagerAdapter adapter = new MusicPlayerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    private class MusicPlayerPagerAdapter extends FragmentPagerAdapter {

        public MusicPlayerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mPlayerFragment;
                case 1:
                    return mListViewFragment;
                case 2:
                    return mSongFrament;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "플레이어";
                case 1:
                    return "아티스트";
                case 2:
                    return "노래";
            }
            return null;
        }
    }
}
