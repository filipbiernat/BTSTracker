package pl.edu.agh.student.fbierna.btstracker.main;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.student.fbierna.btstracker.R;


public class TutorialFragment extends Fragment{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public TutorialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) getActivity().findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }




    public static class TutorialElemFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TutorialElemFragment() {
        }

        public static TutorialElemFragment newInstance(int sectionNumber) {
            TutorialElemFragment fragment = new TutorialElemFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        @SuppressWarnings("fromHtml")
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_elem, container, false);

            int elemId = getArguments().getInt(ARG_SECTION_NUMBER) - 1;

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            if (Build.VERSION.SDK_INT >= 24) {
                textView.setText(Html.fromHtml(tutorialTips[elemId], Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(tutorialTips[elemId]));
            }

            Drawable drawable = getResources().getDrawable(tutorialLogos[elemId], null);
            Button button = (Button) rootView.findViewById(R.id.section_logo);
            button.setBackground(drawable);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return TutorialElemFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return tutorialTips.length;
        }
    }

    private static String[] tutorialTips = new String[]{
            "To <b>navigate</b> through this tutorial swipe <b>left</b> or <b>right</b>.",
            "<b><i>BTS Tracker</i></b> is an open-source mobile application dedicated to observe the <b>cellular infrastructure</b>.",
            "To <b>control</b> the application use <b>drawer menu</b> on the left-hand side.",
            "<b>Swipe</b> from left or use the <b>hamburger</b> button on the top left.",
            "Use <b>S<i>canning</i></b> button to enable/disable the background scanning service.",
            "<b><i>List</i></b> view makes you see which station you are connected to. They are <b>sorted</b> in order of the last dettach.",
            "You can see the stations on the <b><i>map </i></b>and (if location enabled) compare them to your current spot.",
            "The <b>technologies</b> are marked in different <b>colors</b>: pink (2G), blue (3G) and red (4G).",
            "The <b>Spot</b> button makes you go back to the latest cell, while the <b>Refresh</b> one reloads the view.",
            "You can <b>save</b> your data to reload it in the future. But remember, opening a new file will <b>discard</b> your current data.",
            "If you want to <b>analyse</b> collected data on your computer, use <b>Export</b> button. ",
            "In your <b>browser</b> visit: <i>google.com/mymaps</i> and simply import the <b>KML file</b>."
    };

    private static int[] tutorialLogos = new int[]{
            R.drawable.ic_tutorial_0,
            R.mipmap.ic_launcher,
            R.drawable.ic_tutorial_2,
            R.drawable.ic_tutorial_3,
            R.drawable.ic_tutorial_4,
            R.drawable.ic_tutorial_5,
            R.drawable.ic_tutorial_6,
            R.drawable.ic_tutorial_7,
            R.drawable.ic_tutorial_8,
            R.drawable.ic_tutorial_9,
            R.drawable.ic_tutorial_10,
            R.drawable.ic_tutorial_11
    };
}
