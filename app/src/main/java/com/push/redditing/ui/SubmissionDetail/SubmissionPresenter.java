package com.push.redditing.ui.SubmissionDetail;


import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import com.push.redditing.di.ActivityScoped;
import net.dean.jraw.models.Comment;
import net.dean.jraw.tree.CommentNode;

import javax.inject.Inject;
import java.util.List;

@ActivityScoped
public class SubmissionPresenter implements SubmissionContract.Prenseter {

    private SubRedditRepository mRedditRepository;
    private SubmissionContract.View view;

    @Inject
    public SubmissionPresenter(SubRedditRepository repository) {
        this.mRedditRepository = repository;
    }



    @Override
    public void takeView(SubmissionContract.View view) {
        this.view = view;
    }

    @Override
    public void LoadComments(String submissionId) {
        view.showloadIndicator(true);
        mRedditRepository.getComments(submissionId, new SubRedditDataSource.LoadCommentCallback() {

            @Override
            public void onCommentsLoad(List<CommentNode<Comment>> comments) {
                view.showCommentsList(comments);
                view.showloadIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                view.showloadIndicator(false);
                view.showEmptyComment();
            }
        });
    }

    @Override
    public void dropView() {
        this.view = null;
    }
}
