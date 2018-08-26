package com.push.redditing.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.push.redditing.R;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.ui.Post.PostActivity;
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
    private static final String BUNDLE_SUBREDDIT_LIST = "BUNDLE_SUBREDDIT_LIST";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @Inject
    MainPresenter mMainPresenter;
    private Unbinder unbinder;

//     give new pretty  name later
    public interface  OnOauthRequired{
          void OnOauthFailed();
    }
     private  OnOauthRequired oauthRequired ;

    public void setOauthRequired(OnOauthRequired oauthRequired) {
        this.oauthRequired = oauthRequired;
    }

    public List<Subreddit> subreddits = new ArrayList<Subreddit>();


    @Inject
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
//            savedInstanceState.getBundle()
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        //Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

      return view ;
    }

    @OnClick(R.id.new_post_fab)
    public  void openPostActivity(){
        final int currentItem = mViewPager.getCurrentItem();
        Subreddit subreddit = subreddits.get(currentItem);
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra(PostActivity.SUBREDDIT_NAME_EXTRA, subreddit.getName());
        startActivity(intent);
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
    public void showLoadingIndicator(Boolean aBoolean) { }

    @Override
    public void showTabs(List<Subreddit> subredditList) {
        int size = subredditList.size();
        mSectionsPagerAdapter.swapSubreddits(subredditList);
    }

    @Override
    public void transferSubmission(String full_name, List<Submission> submissions) {
        Timber.d(" transfer befor checking  "+full_name);
        if( submissions != null ) {
            Timber.d(" transfer after checking  " + full_name);

            SubRedditFragment fragmentByName = findFragmentByName(full_name);
            if (fragmentByName != null) {
                fragmentByName.setSubmissionList(submissions);
            }
        }

    }

    @Override
    public void showLoginView() {
        oauthRequired.OnOauthFailed();
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter{


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
            //Timber.d(" this is my subscription  ");
            if(subreddits!= null ) {
                return subreddits.size();
            }
            return 0;
        }

        public void swapSubreddits(List<Subreddit> subredditList){
            subreddits=subredditList;
            notifyDataSetChanged();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return subreddits.get(position).getName();
        }


        public List<Subreddit> getSubreddits() {
            return subreddits;
        }
    }
}
