package com.push.redditing.ui.Post;

import android.support.annotation.NonNull;
import com.push.redditing.BasePresenter;
import com.push.redditing.BaseView;
import com.push.redditing.datalayer.datasource.Post;

public class PostContract {

    interface View extends BaseView {
        void showPostLoadingIndicator(Boolean isloading);
        void OnPostFails();

        void OnPostSuccess();

    }

    interface Presenter extends BasePresenter<View> {
        void postSubmission(@NonNull Post post);

    }

}
