package com.push.redditing.ui.widget;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WidgetModule {

    @ContributesAndroidInjector
    public abstract WidgetService widgetService();
}
