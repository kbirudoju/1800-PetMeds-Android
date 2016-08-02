package com.petmeds1800.ui.fragments;

public class LoremFragment extends AbstractFragment {

   /* private static final String ARG_PARAGRAPHS = "ARG_PARAGRAPHS";

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @BindView(R.id.view_lorem)
    TextView mLoremView;

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @Inject
    PermissionUtils mPermissionUtils;

    public static LoremFragment newInstance(final int paragraphs) {
        final Bundle args = new Bundle();
        args.putInt(ARG_PARAGRAPHS, paragraphs);

        final LoremFragment loremFragment = new LoremFragment();
        loremFragment.setArguments(args);

        return loremFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getApplication().component().inject(this);
    }

    @Override
    public View onCreateContentView(final LayoutInflater inflater, final ViewGroup container,
                                    final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lorem, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(R.id.loader_lorem, null,
                new LoremLoaderCallbacks(getApplication(), getParagraphs()));
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_lorem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            getLoaderManager().restartLoader(R.id.loader_lorem, null,
                    new LoremLoaderCallbacks(getApplication(), getParagraphs()));

            return true;
        } else if (id == R.id.menu_save) {
            if (!TextUtils.isEmpty(mLoremView.getText())) {
                if (mPermissionUtils.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    saveLorem();
                } else {
                    mPermissionUtils.askUserForPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            PermissionUtils.STORAGE_PERMISSION_REQUEST_CODE, getString(R.string.permission_storage_rationale));
                }
            } else {
                Snackbar.make(mLoremView, R.string.label_text_not_ready, Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }

    public void saveLorem() {
        getLoaderManager().restartLoader(R.id.loader_save_lorem, null,
                new SaveLoremLoaderCallbacks(
                        getApplication(), getFileTitle(getParagraphs()), mLoremView.getText().toString()
                ));
    }

    private String getFileTitle(int paragraphs) {
        return getResources().getStringArray(R.array.label_drawer_digits)[paragraphs - 1];
    }

    private TemplateApplication getApplication() {
        final Activity activity = getActivity();
        return (TemplateApplication) activity.getApplication();
    }

    private int getParagraphs() {
        return getArguments().getInt(ARG_PARAGRAPHS);
    }

    private final class LoremLoaderCallbacks extends LoremLoader.AbstractLoremLoaderCallbacks {

        private LoremLoaderCallbacks(final TemplateApplication application, final int paragraphs) {
            super(application, paragraphs);
        }

        @Override
        public LoremLoader onCreateLoader(final int id, final Bundle args) {
            showSpinner();
            return super.onCreateLoader(id, args);
        }

        @Override
        public void onLoadFinished(final Loader<String> loader, final String lorem) {
            hideSpinner();
            mLoremView.setText(lorem);
        }
    }


    private final class SaveLoremLoaderCallbacks extends SaveLoremLoader.AbstractSaveLoremLoaderCallbacks {

        private SaveLoremLoaderCallbacks(final TemplateApplication application, final String fileName, final String fileContent) {
            super(application, fileName, fileContent);
        }

        @Override
        public SaveLoremLoader onCreateLoader(final int id, final Bundle args) {
            return super.onCreateLoader(id, args);
        }

        @Override
        public void onLoadFinished(final Loader<Boolean> loader, final Boolean isFileSaved) {
            final int message;
            if (isFileSaved) {
                message = R.string.label_text_saved;
            } else {
                message = R.string.label_text_not_saved;
            }
            Snackbar.make(mLoremView, message, Snackbar.LENGTH_SHORT).show();
        }
    }*/
}
