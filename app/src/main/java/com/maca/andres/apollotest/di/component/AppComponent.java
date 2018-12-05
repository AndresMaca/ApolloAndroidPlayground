package com.maca.andres.apollotest.di.component;

import android.app.Application;

import com.maca.andres.apollotest.App;
import com.maca.andres.apollotest.di.modules.ActivityModule;
import com.maca.andres.apollotest.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(App app);
}
