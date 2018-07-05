package com.push.redditing.ui.main.SubReddit;

import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;
import net.dean.jraw.models.Submission;

import java.util.List;

public class SubRedditContract {
    interface  View  extends BaseView{
        void showLoadingIndicator(Boolean  aBoolean);
        void showLoadindError();
        void showSubmissionList(List<Submission> submissionList);

    }
    interface Presenter extends BasePresenter<View>{
        /*
        * @param fullname   fullname of subReddit
        *
        */
        void getSubmissionList(String fullName);

    }
}
