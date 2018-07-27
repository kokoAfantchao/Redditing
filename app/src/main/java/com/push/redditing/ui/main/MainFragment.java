package com.push.redditing.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.push.redditing.R;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.ui.main.SubReddit.SubRedditFragment;
import dagger.android.support.DaggerFragment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 */
@ActivityScoped
public class MainFragment extends DaggerFragment implements  MainContract.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @Inject
    MainPresenter mMainPresenter;
    private Unbinder unbinder;




    @Inject
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

      return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainPresenter.dropView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLoadingIndicator(Boolean aBoolean) {

    }

    @Override
    public void showTabs(List<Subreddit> subredditList) {
        int size = subredditList.size();
        mSectionsPagerAdapter.swapSubreddits(subredditList);
    }

    @Override
    public void transferSubmission(String full_name, List<Submission> submissions) {
        Timber.d(" transfer befor checking  "+full_name);
        if( submissions != null ){
            Timber.d(" transfer after checking  "+full_name);
            (findFragmentByName(full_name)).setSubmissionList(submissions);
        }

    }
    SubRedditFragment.OnFragmentInteractionListener  callback = new SubRedditFragment.OnFragmentInteractionListener(){
        @Override
        public void onFragmentInteraction(Uri uri) {

        }

        @Override
        public void onFragmentCreate(String full_name) {
            Timber.d( " callback  for my listerner for  "+ full_name);
            mMainPresenter.loadSubmission(full_name);

        }
    };


    private SubRedditFragment findFragmentByName(String full_name ){
        Fragment registeredFragment = mSectionsPagerAdapter.getRegisteredFragment(full_name);
        return (SubRedditFragment) registeredFragment;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter{


        private List<Subreddit> subreddits = new ArrayList<Subreddit>();
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        private Map<String,Fragment> registerFr = new HashMap<String, Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            String argFullName = getFragmentArgFullName(fragment);
            registerFr.put(argFullName, fragment);
            registeredFragments.put(position,fragment);
            return fragment;
        }
        private String  getFragmentArgFullName(Fragment f){
            String arg = f.getArguments().getString(SubRedditFragment.ARG_PARAM_FULLNAME);
            return arg;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Fragment fragment = registeredFragments.get(position);
            String argFullName = getFragmentArgFullName(fragment);
            registerFr.remove(argFullName);
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        public Fragment getRegisteredFragment(String sFullName){
            return registerFr.get(sFullName);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            String fullName = subreddits.get(position).getName();
            SubRedditFragment subRedditFragment = SubRedditFragment.newInstance(fullName, callback);
            return subRedditFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //Timber.d(" this is my subscription ");
            if(subreddits!= null ) {
                return subreddits.size();
            }
            return 0;
        }

        public void swapSubreddits(List<Subreddit> subreddits){
            this.subreddits=subreddits;
            notifyDataSetChanged();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return subreddits.get(position).getName();
        }
    }
}
