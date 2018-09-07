package com.push.redditing.ui.main.SubReddit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import com.push.redditing.datalayer.datasource.local.Entities.LSubmission;
import com.push.redditing.ui.SubmissionDetail.SubmissionActivity;
import dagger.android.support.DaggerFragment;
import net.dean.jraw.models.Submission;
import timber.log.Timber;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement thed
 * {@link SubRedditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubRedditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubRedditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM_FULLNAME = "arg_param_fullname";

    // TODO: Rename and change types of parameters
    public String mParam_fullname;

    public  static OnFragmentInteractionListener mListener;

    SubmissionAdapter  submissionAdapter;
    @BindView(R.id.submission_list)
    RecyclerView  mRecyclerView;
    @BindView(R.id.submission_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
           loadSubmission(true);
        }
    };



    public SubRedditFragment() {
        // Required empty public constructor
        submissionAdapter = new SubmissionAdapter(submission -> {
            Intent intent = new Intent(getContext(), SubmissionActivity.class);
            Bundle  bundle = new Bundle();
            bundle.putParcelable(SubmissionActivity.SUBMISSION_EXTRA, submission);
            intent.putExtra(SubmissionActivity.SUBMISSION_BUNDLE,bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SubRedditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubRedditFragment newInstance(String mParam_fullname, OnFragmentInteractionListener mListener){
        SubRedditFragment fragment = new SubRedditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_FULLNAME, mParam_fullname);
        fragment.setArguments(args);
        fragment.setFInteractionListener( mListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam_fullname = getArguments().getString(ARG_PARAM_FULLNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState ){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_reddit, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(submissionAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        loadSubmission(false);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String fulName  = getArguments().getString(ARG_PARAM_FULLNAME);

    }
    public void setFInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener=mListener;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() +" must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    private void loadSubmission(Boolean forceRemoteLoding) {
        if(mListener != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mListener.onFragmentCreate(mParam_fullname,forceRemoteLoding);
        }

    }
    public void setSubmissionList( List<LSubmission> new_data){
        mSwipeRefreshLayout.setRefreshing(false);
        submissionAdapter.swapSubmissions(new_data);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onFragmentCreate(String full_name,Boolean forceRemoteLaoding);
    }




    public  static class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>{

        public interface  SubmisssionItemListener{
            void onSubmissionItemClick(LSubmission  submission);
        }
        private  List<LSubmission> submissions;

        private SubmisssionItemListener listener;

        public  SubmissionAdapter(SubmisssionItemListener  listener){
            this.listener= listener;
        }


        @NonNull
        @Override
        public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_item, parent, false);
            SubmissionViewHolder viewHolder = new SubmissionViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position){
            LSubmission submission = submissions.get(position);
            holder.bindViewData(submission);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubmissionItemClick(submission);
                }
            });

        }


        @Override
        public int getItemCount() {
            if (submissions!= null){
             Timber.d("GET ITEM_COUNT CALL  FOR SIZE "+submissions.size()+" for fullname");
                return submissions.size();
            }
            return 0;
        }

        public  void swapSubmissions(List<LSubmission> submissions){
            if(submissions!=null && !submissions.isEmpty()){
                this.submissions=submissions;
                notifyDataSetChanged();
            }
        }


        class SubmissionViewHolder extends  RecyclerView.ViewHolder{

            @BindView(R.id.submission_title_tv)
            TextView titleView;

            @BindView(R.id.author_tv)
            TextView authorView;

            @BindView(R.id.comment_count_tv)
            TextView commentView;

            @BindView(R.id.date_tv)
            TextView dateView;



            private Context context;

            private LSubmission mSubmission;

            public SubmissionViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                context = itemView.getContext();
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                    }
                });
            }

            public void bindViewData(LSubmission submission){
                mSubmission = submission;
                Integer commentCount = submission.getCommentCount();
                String quantityString = context.getResources().getQuantityString(R.plurals.numberOfComment, commentCount, commentCount);
                submission.getCreated();
                titleView.setText(submission.getTitle());
                authorView.setText(submission.getAuthor());
                commentView.setText(quantityString);
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
                dateView.setText(dateFormat.format(submission.getCreated()));

            }


        }


    }
}
