package com.push.redditing.ui.SubmissionDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.push.redditing.R;
import com.push.redditing.di.ActivityScoped;
import com.squareup.picasso.Picasso;
import dagger.android.support.DaggerFragment;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.CommentNode;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
@ActivityScoped
public class SubmissionFragment extends DaggerFragment implements SubmissionContract.View {

    @Inject
    SubmissionPresenter mSubmissionPresenter;
    @BindView(R.id.self_text_tv)
    TextView textViewSubSelfText;
    @BindView(R.id.fab)
    FloatingActionButton commentButton;
    @BindView(R.id.recyclerView)
    RecyclerView commentRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.thumbnail_img)
    ImageView thumbnailImageView;

    CommentAdapter mCommentAdapter;
    private Submission submission;




    @Inject
    public SubmissionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submission, container, false);
        ButterKnife.bind(this, view);
        showSubmissionContent();
        mCommentAdapter = new CommentAdapter();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        commentRecyclerView.setLayoutManager(mLayoutManager);
        commentRecyclerView.setAdapter(mCommentAdapter);
        return view;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubmissionPresenter.takeView(this);
        mSubmissionPresenter.LoadComments(submission.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubmissionPresenter.dropView();
    }

    @Override
    public void showloadIndicator(Boolean isloading) {
        if (isloading) {
            progressBar.setVisibility(View.VISIBLE);
            commentRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            commentRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCommentsList(List<CommentNode<Comment>> commentNodes) {
       mCommentAdapter.swapData(commentNodes);

        int size = commentNodes.size();
        Timber.d(" this has  new list of comment of lines " + size);
    }

    @Override
    public void showEmptyComment() {
    }

    private void showSubmissionContent() {
        if (submission.isSelfPost()) {
            thumbnailImageView.setVisibility(View.INVISIBLE);
            textViewSubSelfText.setVisibility(View.VISIBLE);
            textViewSubSelfText.setText(submission.getSelfText());
        }else {
            textViewSubSelfText.setVisibility(View.INVISIBLE);
            thumbnailImageView.setVisibility(View.VISIBLE);
            if (submission.hasThumbnail()) {

                Picasso.get().load(submission.getUrl()).into(thumbnailImageView);
                Timber.d(" this will be the url of +++++"+submission.getThumbnail());
            }
        }
    }


    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        List<CommentNode<Comment>> commentNodeList ;

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            CommentViewHolder viewHolder = new CommentViewHolder(rootView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            CommentNode node = commentNodeList.get(position);
            holder.authorTextView.setText(node.getSubject().getAuthor());
            holder.commentTextView.setText(node.getSubject().getBody());
        }

        @Override
        public int getItemCount() {
            if(commentNodeList != null) return commentNodeList.size();
            return 0;
        }

        public void swapData(List<CommentNode<Comment>> commentNodes) {
            if (commentNodes != null) {
                commentNodeList = commentNodes;
                notifyDataSetChanged();
            }

        }

        class CommentViewHolder extends  RecyclerView.ViewHolder{
            @BindView(R.id.comment_textView)
            TextView commentTextView;

            @BindView(R.id.author_textView)
            TextView authorTextView;

            @BindView(R.id.date_textView)
            TextView dateTextView;

            public CommentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}

