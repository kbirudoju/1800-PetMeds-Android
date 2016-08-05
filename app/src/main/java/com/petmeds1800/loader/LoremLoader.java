package com.petmeds1800.loader;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.api.ApiService;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.io.IOException;

import javax.inject.Inject;

public class LoremLoader extends AbstractLoader<String> {

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @Inject
    ApiService mApiService;

    private final int mParagraphs;

    public LoremLoader(final PetMedsApplication application, final int paragraphs) {
        super(application);
        mParagraphs = paragraphs;
        application.component().inject(this);
    }

    @Override
    public String loadInBackground() {
        try {
            return mApiService.getLorem(mParagraphs).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract static class AbstractLoremLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

        private final PetMedsApplication mApplication;
        private final int mParagraphs;

        protected AbstractLoremLoaderCallbacks(PetMedsApplication application, final int paragraphs) {
            mApplication = application;
            mParagraphs = paragraphs;
        }

        @Override
        public LoremLoader onCreateLoader(final int id, final Bundle args) {
            return new LoremLoader(mApplication, mParagraphs);
        }

        @Override
        public abstract void onLoadFinished(final Loader<String> loader, final String lorem);

        @Override
        public void onLoaderReset(final Loader loader) {
            // unused
        }
    }
}
