package com.push.redditing.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.push.redditing.R;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.datalayer.datasource.local.Entities.LSubreddit;
import com.push.redditing.di.ActivityScoped;
import com.push.redditing.ui.Post.PostActivity;
import com.push.redditing.ui.main.SubReddit.SubRedditFragment;
import dagger.android.support.DaggerFragment;
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
    private static final String CURRENT_TAB_EXTRA = "CURRENT_TAB_EXTRA";
    private static final String SUBMISSIONS_EXTRA = "SUBMISSIONS_EXTRA";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @Inject
    MainPresenter mMainPresenter;
    private Unbinder unbinder;
    private  int mCurrentTab;
    Map<String, List<LSubmission>> mCachedSubmission = new HashMap<>();

//     give new pretty  name later
    public ArrayList<LSubreddit> subreddits = new ArrayList<LSubreddit>();
    private  OnOauthRequired oauthRequired ;

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Timber.d("____+++++_____ Postion is changing to ++++>>>"+ mViewPager.getCurrentItem()  );
            Timber.d("____+++++_____ Postion is changing to ++++>>>"+ tab.getPosition()  );
            mCurrentTab = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public interface  OnOauthRequired{
        void OnOauthFailed();
    }

    public void setOauthRequired(OnOauthRequired oauthRequired) {
        this.oauthRequired = oauthRequired;
    }


    @Inject
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_TAB_EXTRA,mCurrentTab);
        outState.putParcelableArrayList(BUNDLE_SUBREDDIT_LIST,subreddits);
     //   outState.putParcelableArrayList(SUBMISSIONS_EXTRA, mCachedSubmission);
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
        int curentpager ;
        if (savedInstanceState != null) {
            subreddits = savedInstanceState.getParcelableArrayList(BUNDLE_SUBREDDIT_LIST);
           curentpager= savedInstanceState.getInt(CURRENT_TAB_EXTRA);
            mSectionsPagerAdapter.swapSubreddits(subreddits);
        }

        if (subreddits != null && subreddits.size() > 0) {

            tabLayout.getTabAt(mCurrentTab).select();

        } else {
            mMainPresenter.loadSubreddits(false);
        }

        return view ;
    }

    @OnClick(R.id.new_post_fab)
    public  void openPostActivity(){
        final int currentItem = mViewPager.getCurrentItem();
        LSubreddit subreddit = subreddits.get(currentItem);
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra(PostActivity.SUBREDDIT_NAME_EXTRA, subreddit.getName());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        mMainPresenter.takeView(this);
    }

    @Override
    public void onPause() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener);
        super.onPause();
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
    public void showTabs(List<LSubreddit> subredditList) {
        Timber.d("this is where the data is swaping  with size "+ subredditList.size());
        mSectionsPagerAdapter.swapSubreddits(subredditList);
    }


    @Override
    public void transferSubmission(String full_name, List<LSubmission> submissions) {
        Timber.d(" transfer befor checking  "+full_name);
        if( submissions != null ) {
            mCachedSubmission.put(full_name, submissions);
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
        public void onFragmentCreate(String full_name, Boolean forceRemoteLoading ) {
            if (forceRemoteLoading) {
                mMainPresenter.loadSubmission(full_name);
            } else{
                if (mCachedSubmission.get(full_name) != null && !mCachedSubmission.get(full_name).isEmpty()) {
                    transferSubmission(full_name, mCachedSubmission.get(full_name));
                } else {
                    mMainPresenter.loadSubmission(full_name);
                }

            }

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

        public void swapSubreddits(List<LSubreddit> subredditList){
            subreddits= (ArrayList<LSubreddit>) subredditList;
            notifyDataSetChanged();
            tabLayout.getTabAt(mCurrentTab).select();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return subreddits.get(position).getName();
        }


        public List<LSubreddit> getSubreddits() {
            return subreddits;
        }
    }
}
