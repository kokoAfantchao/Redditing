package com.push.redditing.ui.Post;

import android.support.annotation.NonNull;
import com.push.redditing.datalayer.datasource.Post;
import com.push.redditing.datalayer.datasource.SubRedditDataSource;
import com.push.redditing.datalayer.repository.SubRedditRepository;
import net.dean.jraw.models.Submission;

import javax.inject.Inject;

public class PostPresenter  implements  PostContract.Presenter {

    private SubRedditRepository mReditRepository;
    private PostContract.View  mPostView;

    @Inject
    public  PostPresenter(SubRedditRepository redditRepository){
        this.mReditRepository=redditRepository;
    }

    @Override
    public void takeView(PostContract.View view) {
     mPostView=view;
    }

    @Override
    public void dropView() {
        mPostView= null;
    }

    @Override
    public void postSubmission(@NonNull Post post) {
     mReditRepository.postSubmission(post, new SubRedditDataSource.PostSubmissionCallback() {
         @Override
         public void onPostSuccess(Submission submission) {
             mPostView.OnPostSuccess();
         }

         @Override
         public void onPostFailed() {
             mPostView.OnPostFails();
         }
     });


    }
}
