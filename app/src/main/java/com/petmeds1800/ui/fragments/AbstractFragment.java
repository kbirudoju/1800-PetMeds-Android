package com.petmeds1800.ui.fragments;

import android.support.v4.app.Fragment;

public abstract class AbstractFragment extends Fragment {
//Since we have just started and have no idea on UI part, so commenting below code
  /*  private View mSpinnerView;
    private ViewGroup mContentView;

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_spinner, container, false);

        mSpinnerView = ButterKnife.findById(view, R.id.view_spinner);
        mContentView = ButterKnife.findById(view, R.id.view_content);

        final View content = onCreateContentView(inflater, mContentView, savedInstanceState);
        if (content != mContentView) {
            mContentView.addView(content);
        }

        return view;
    }

    public abstract View onCreateContentView(final LayoutInflater inflater, final ViewGroup container,
                                             final Bundle savedInstanceState);

    public void showSpinner() {
        mSpinnerView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    public void hideSpinner() {
        mSpinnerView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    public boolean isSpinnerVisible() {
        return mSpinnerView.getVisibility() == View.VISIBLE;
    }*/
}
