package com.push.redditing.ui.SubmissionDetail;

import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;
import net.dean.jraw.models.Comment;
import net.dean.jraw.tree.CommentNode;

import java.util.List;

public class SubmissionContract {

    interface  View extends BaseView {
        void showloadIndicator(Boolean isloading);
        void showCommentsList(List<CommentNode<Comment>> comments);
        void showEmptyComment();

    }

    interface  Prenseter extends BasePresenter<View>{
        void LoadComments(String submissionId);
    }
}
