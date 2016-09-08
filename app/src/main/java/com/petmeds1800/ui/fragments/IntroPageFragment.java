package com.petmeds1800.ui.fragments;

import com.petmeds1800.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Digvijay on 9/8/2016.
 */
public class IntroPageFragment extends Fragment {

    @BindView(R.id.text_title)
    TextView mTitleText;

    @BindView(R.id.text_description)
    TextView mDescriptionText;

    @BindView(R.id.image_icon)
    ImageView mIconImage;

    private static final String PARAM_ICON_ID = "iconId";

    private static final String PARAM_TITLE_ID = "titleId";

    private static final String PARAM_DESCRIPTION_ID = "descriptionId";

    public IntroPageFragment() {
    }

    public static Fragment newInstance(final int iconId, final int titleId, final int descriptionId) {
        final Fragment fragment = new IntroPageFragment();
        final Bundle args = new Bundle();
        args.putInt(PARAM_ICON_ID, iconId);
        args.putInt(PARAM_TITLE_ID, titleId);
        args.putInt(PARAM_DESCRIPTION_ID, descriptionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro_page, container, false);
        ButterKnife.bind(this, view);
        final Bundle args = getArguments();
        final int titleId = args.getInt(PARAM_TITLE_ID);
        final int descriptionId = args.getInt(PARAM_DESCRIPTION_ID);
        final int iconId = args.getInt(PARAM_ICON_ID);
        if (titleId == 0 || descriptionId == 0 || iconId == 0) {
            throw new RuntimeException("Invalid titleId or descriptionId or iconId!");
        }
        mTitleText.setText(getString(titleId));
        mDescriptionText.setText(getString(descriptionId));
        mIconImage.setImageResource(iconId);
        return view;
    }
}
