package com.push.redditing.ui.main.SubReddit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import dagger.android.support.DaggerFragment;
import net.dean.jraw.models.Submission;
import timber.log.Timber;

import javax.inject.Inject;
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

    OnFragmentInteractionListener mListener;

    SubmissionAdapter  submissionAdapter;
    @BindView(R.id.submission_list)
    RecyclerView  mRecyclerView;



    public SubRedditFragment() {
        // Required empty public constructor
        submissionAdapter = new SubmissionAdapter(new SubmissionAdapter.SubmisssionItemListener() {
            @Override
            public void onSubmissionItemClick(Submission submission) {

            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SubRedditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubRedditFragment newInstance(String mParam_fullname, OnFragmentInteractionListener mListener) {
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
        if (getArguments() != null) {
            mParam_fullname = getArguments().getString(ARG_PARAM_FULLNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_reddit, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(submissionAdapter);
        mListener.onFragmentCreate(mParam_fullname);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void setSubmissionList( List<Submission> new_data){
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
        void onFragmentCreate(String full_name);
    }




    public  static class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>{

        public interface  SubmisssionItemListener{
            void onSubmissionItemClick(Submission  submission);
        }
        private  List<Submission> submissions;

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
            Submission submission = submissions.get(position);

            holder.bindViewData(submission);


        }


        @Override
        public int getItemCount() {
            if (submissions!= null){
             Timber.d("GET ITEM_COUNT CALL  FOR SIZE "+submissions.size()+" for fullname");
                return submissions.size();
            }
            return 0;
        }

        public  void swapSubmissions(List<Submission> submissions){
            if(submissions!=null && !submissions.isEmpty()){
                this.submissions=submissions;
            }
        }


        class SubmissionViewHolder extends  RecyclerView.ViewHolder{

            @BindView(R.id.submission_title_tv)
            TextView titleView;

            @BindView(R.id.author_tv)
            TextView authorView;

            @BindView(R.id.comment_count_tv)
            TextView commentView;

            @BindView(R.id.point_count_tv)
            TextView pointCountView;

            public SubmissionViewHolder(View itemView){
                super(itemView);
                ButterKnife.bind(this,itemView);
            }

            public void bindViewData(Submission submission){
                titleView.setText(submission.getTitle());
                authorView.setText(submission.getAuthor());
                commentView.setText(submission.getCommentCount());
                //pointCountView.setText(submission.ge);
            }


        }


    }
}
