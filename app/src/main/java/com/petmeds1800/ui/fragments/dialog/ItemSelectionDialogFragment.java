package com.petmeds1800.ui.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.petmeds1800.R;
import com.petmeds1800.ui.HomeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pooja on 8/5/2016.
 */
public class ItemSelectionDialogFragment extends DialogFragment {

    /**
     * An item that can be displayed and selected by the ItemSelectionDialogFragment
     */
    private OnItemSelectedListener mOnItemSelectedListener;
    public static class Item {
        private String title;
        private boolean intValue;
        private String code;

        private static final String KEY_TITLE        = "title";
        private static final String KEY_INT_VALUE    = "intValue";
        private static final String KEY_CODE         = "code";


        /**
         * Construct with title and integer value
         *
         * @param title Name displayed in list
         * @param value Integer value associated with item
         */
        public Item(String title, boolean value,String code) {
            assert(!TextUtils.isEmpty(title));

            this.title = title;
            this.intValue = value;
            this.code=code;
        }


        /**
         * Construct from a bundle of values
         * @param bundle
         */
        public Item(Bundle bundle) {
            title = bundle.getString(KEY_TITLE, null);
            intValue = bundle.getBoolean(KEY_INT_VALUE, false);
            code=bundle.getString(KEY_CODE,null);
        }

        /**
         * Get a Bundle of values that can be passed to the Item(Bundle) constructor
         * to re-create the object
         *
         * @return Bundle
         */
        public Bundle getValuesBundle() {
            Bundle bundle = new Bundle();

            bundle.putString(KEY_TITLE, title);
            bundle.putBoolean(KEY_INT_VALUE, intValue);
            bundle.putString(KEY_CODE, code);

            return bundle;
        }

        public String getTitle() {
            return title;
        }

        public boolean getIntValue() {
            return intValue;
        }

        public String getCode() {
            return code;
        }

        /**
         * Given a list of items, create a Bundle that can be passed to
         * Item.itemsFromBundle() to recreate them.
         *
         * @param items list of items
         * @return Bundle
         */
        public static Bundle bundleOfItems(List<Item> items) {
            int itemCount = items.size();
            ArrayList<Bundle> itemBundles = new ArrayList<>();
            for (int i = 0; i < itemCount; ++i) {
                itemBundles.add(items.get(i).getValuesBundle());
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ARG_ITEMS, itemBundles);
            return bundle;
        }

        /**
         * Given a Bundle created by Item.bundleOfItems(), recreate the
         * original list of items.
         *
         * @param bundle Bundle created by Item.bundleOfItems()
         * @return ArrayList&lt;Item&gt;
         */
        public static ArrayList<Item> itemsFromBundle(Bundle bundle) {
            ArrayList<Bundle> itemBundles = bundle.getParcelableArrayList(ARG_ITEMS);
            ArrayList<Item> items = new ArrayList<>();
            for (Bundle itemBundle: itemBundles) {
                items.add(new Item(itemBundle));
            }
            return items;
        }
    }

    /**
     * Interface for notification of item selection
     *
     * If the owning Activity implements this interface, then the fragment will
     * invoke its onItemSelected() method when the user clicks the OK button.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(ItemSelectionDialogFragment fragment, Item item, int index);
    }

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_ITEMS = "ARG_ITEMS";
    private static final String ARG_SELECTED_INDEX = "ARG_SELECTED_INDEX";

    /**
     * Create a new instance of ItemSelectionDialogFragment with specified arguments
     *
     * @param title Dialog title text
     * @param items Selectable items
     * @param selectedIndex initial selection index, or -1 if no item should be pre-selected
     * @return ItemSelectionDialogFragment
     */
    public static ItemSelectionDialogFragment newInstance(String title, ArrayList<Item> items, int selectedIndex) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putBundle(ARG_ITEMS, Item.bundleOfItems(items));
        args.putInt(ARG_SELECTED_INDEX, selectedIndex);

        ItemSelectionDialogFragment fragment = new ItemSelectionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Item> items;
    private int selectedIndex;

    /**
     * Constructor
     */
    public ItemSelectionDialogFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_SELECTED_INDEX, selectedIndex);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Activity activity = getActivity();
        if (args != null) {
            items = Item.itemsFromBundle(args.getBundle(ARG_ITEMS));
            selectedIndex = args.getInt(ARG_SELECTED_INDEX, -1);
        }

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(ARG_SELECTED_INDEX, selectedIndex);
        }

        String[] itemTitles = getItemTitlesArray();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the adapter

        AlertDialog alertDialog = builder.create();


        LayoutInflater inflater = getActivity().getLayoutInflater();
        // create view for add item in dialog
        View convertView = (View) inflater.inflate(R.layout.view_filter_list, null);

        alertDialog.setView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.mylistview);
        MyAdapter myadapter = new MyAdapter(getActivity(),
                R.layout.view_list_item_multiple_choice, Arrays.asList(itemTitles));
        lv.setAdapter(myadapter);
        /*final ListView listView = alertDialog.getListView();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (  getActivity(), R.layout.view_list_item_multiple_choice, itemTitles ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if(selectedIndex!=-1){
                    view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.petmeds_blue));
                }
                return view;
            }
        };*/
        //listView.setAdapter(arrayAdapter);

      /*  listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.view_list_item_multiple_choice, itemTitles));*/
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       /* if(selectedIndex!=-1){

            listView.setItemChecked(selectedIndex,true);
        }
*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex=position;
                view.setSelected(true);
                if (activity instanceof HomeActivity) {
                    if ( selectedIndex >-1 && selectedIndex < items.size()) {
                        Item item = items.get(selectedIndex);
                        //OnItemSelectedListener listener = (OnItemSelectedListener)activity;
                        mOnItemSelectedListener.onItemSelected(ItemSelectionDialogFragment.this, item, selectedIndex);
                    }
                }

            }
        });
        lv.setDivider(null);
        lv.setDividerHeight(-1);

        //code to set position of dialog
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y=200;
        window.setAttributes(wlp);
        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private String[] getItemTitlesArray() {
        final int itemCount = items.size();
        String[] itemTitles = new String[itemCount];
        for (int i = 0; i < itemCount; ++i) {
            itemTitles[i] = items.get(i).getTitle();
        }
        return itemTitles;
    }

    public void setItemSelectionListener(OnItemSelectedListener onItemSelectedListener){
        this.mOnItemSelectedListener=onItemSelectedListener;
    }




    private class ViewHolder {
        TextView tvSname;

    }
    class MyAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;
        Context myContext;
        List<String> newList;
        public MyAdapter(Context context, int resource, List<String> list) {
            super(context, resource, list);
            // TODO Auto-generated constructor stub
            myContext = context;
            newList = list;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.view_list_item_multiple_choice, null);
                holder.tvSname = (CheckedTextView) view.findViewById(R.id.filterLabel);

                view.setTag(holder);
                if(selectedIndex!=-1 && position==selectedIndex){
                    holder.tvSname.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.pet_meds_blue_selected));
                    holder.tvSname.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                }else{
                    holder.tvSname.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    holder.tvSname.setTextColor(ContextCompat.getColor(getActivity(),R.color.petmeds_blue));

                }
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.tvSname.setText(newList.get(position));

            return view;
        }
    }

}

