package com.push.redditing.ui.Post;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.*;

import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.di.ActivityScoped;
import dagger.android.support.DaggerFragment;
import net.dean.jraw.models.SubmissionKind;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ActivityScoped
public class PostFragment extends DaggerFragment implements PostContract.View {

    private OnFragmentInteractionListener mListener;
    private String subredditName ;
    @BindView(R.id.editText_title)
    EditText  titleEditText;
    @BindView(R.id.editText_content)
    EditText  contentEditText;
    @BindView(R.id.send_replay_switch)
    Switch sendReplaySwitch;
    @Inject
    PostPresenter mPostPresenter;




    @Inject
    public PostFragment(){
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        mPostPresenter.takeView(this);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_post :
                if( titleEditText.getText().length()> 0 || contentEditText.getText().length()>0){

                    Post post = new Post();
                    post.setTitle( titleEditText.getText().toString());
                    post.setContent(contentEditText.getText().toString());
                    post.setFull_name(subredditName);
                    post.setSendReplies(sendReplaySwitch.isActivated());
                    post.setSubmissionKind(SubmissionKind.SELF);
                    mPostPresenter.postSubmission(post);


                }else {
                    Toast.makeText(getContext(),R.string.empty_edit_msg, Toast.LENGTH_SHORT).show();
                }

                return  true ;

            case R.id.action_settings :

                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showPostLoadingIndicator(Boolean isloading) {


    }

    @Override
    public void OnPostFails() {
        Toast.makeText(getContext(), R.string.post_failed_msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void OnPostSuccess() {
        getActivity().finish();
    }

    public void setSubredditName(String fullName) {
        this.subredditName = fullName;
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
    }
}
