package com.petmeds1800.ui.vet.support;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.petmeds1800.R;
import com.petmeds1800.api.PetMedsApiService;
import com.petmeds1800.model.SearchVetByZipCodeResponse;
import com.petmeds1800.model.VetList;
import com.petmeds1800.ui.vet.AddVetContract;
import com.petmeds1800.ui.vet.AddVetPresenter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by pooja on 10/4/2016.
 */
public class VetListSuggestionAdapter extends BaseAdapter implements Filterable,AddVetContract.View{
    private List<VetList> vetList;
    private Context mContext;
    private AddVetContract.Presenter mPresenter;
    private String mZipCode;

    @Inject
    PetMedsApiService mPetMedsApiService;

    public VetListSuggestionAdapter(Activity context, String zipCode) {
        vetList = new ArrayList<VetList>();
        this.mContext=context;
        mPresenter=new AddVetPresenter(this);
        this.mZipCode=zipCode;
    }


    @Override
    public int getCount() {
        return vetList.size();
    }

    @Override
    public Object getItem(int index) {
        return vetList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the drop down item for the view
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_vet_suggestion, parent, false);
        }
        // Populate the data for the view
        ((TextView) convertView.findViewById(R.id.vet_name_label))
                .setText(((VetList)getItem(position)).getName());
        ((TextView) convertView.findViewById(R.id.vet_address_label))
                .setText(((VetList)getItem(position)).getAddress());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                vetList=new ArrayList<VetList>();
                if(constraint != null){
                    List<VetList> dataObject = findDataForConstraint(mContext, constraint.toString());
                    String searchString = constraint.toString().toLowerCase();
                    for(VetList c : dataObject) {
                        if (c.getName().toLowerCase().contains(searchString)) {
                            vetList.add(c);
                        }  else if (c.getClinic().toLowerCase().contains(searchString)) {
                            vetList.add(c);
                        }else{

                        }

                    }
                    // Assign the data to the FilterResults
                    filterResults.values = vetList;
                    filterResults.count = vetList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    vetList = (List<VetList>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void onSuccess(ArrayList<VetList> vetList) {

    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void setPresenter(AddVetContract.Presenter presenter) {

    }

    private List<VetList> findDataForConstraint(Context context, String searchTerm) {
        if(mPresenter == null) {
            throw new NullPointerException("ISearchConstraint implementation object cannot be null");
        }

        // Get the result list
        SearchVetByZipCodeResponse response = mPresenter.getVetList("02062");
        List<VetList> resultList=response.getVetList();

        // Apply the Max number of results
       /* if(resultList != null &&
                resultList.size() > mMaxResults) {
            return resultList.subList(0, mMaxResults);
        }*/

        return resultList;
    }
}
