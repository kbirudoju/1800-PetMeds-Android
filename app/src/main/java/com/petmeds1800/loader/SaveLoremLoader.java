package com.petmeds1800.loader;

import com.petmeds1800.TemplateApplication;
import com.petmeds1800.util.FileUtils;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;

public class SaveLoremLoader extends AbstractLoader<Boolean> {

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @Inject
    FileUtils mFileUtils;

    private final String mFileName;
    private final String mFileContent;

    public SaveLoremLoader(final TemplateApplication application, final String fileName,
            final String fileContent) {
        super(application);

        mFileName = fileName;
        mFileContent = fileContent;

        application.component().inject(this);
    }

    @Override
    public Boolean loadInBackground() {
        return mFileUtils.saveFile(mFileName, mFileContent);
    }

    public abstract static class AbstractSaveLoremLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<Boolean> {

        private final TemplateApplication mApplication;
        private final String mFileName;
        private final String mFileContent;

        protected AbstractSaveLoremLoaderCallbacks(final TemplateApplication application,
                final String fileName, final String fileContent) {
            mApplication = application;
            mFileName = fileName;
            mFileContent = fileContent;
        }

        @Override
        public SaveLoremLoader onCreateLoader(final int id, final Bundle args) {
            return new SaveLoremLoader(mApplication, mFileName, mFileContent);
        }

        @Override
        public abstract void onLoadFinished(final Loader<Boolean> loader, final Boolean fileSaved);

        @Override
        public void onLoaderReset(final Loader loader) {
            // unused
        }
    }
}
