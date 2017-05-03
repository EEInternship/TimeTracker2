package eeinternship.com.timetracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.ProfileDataDropdown;
import Data.ProfileDataLine;
import Data.Project;

/**
 * Created by IsakFe on 30. 03. 2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ProfileDataDropdown> profileDataDropdownArrayList;
    private ArrayList<Project> allProjects;

    public ExpandableListAdapter(Context context, ArrayList<ProfileDataDropdown> profileDataDropdownArrayList,ArrayList<Project> projectArrayList) {
        this._context = context;
        this.profileDataDropdownArrayList = profileDataDropdownArrayList;
        this.allProjects = projectArrayList;

        for(ProfileDataDropdown profileDataDropdown : profileDataDropdownArrayList){
            for(ProfileDataLine profileDataLine: profileDataDropdown.getProfileDataLineArrayList()){
                for(Project project: allProjects){
                    if(profileDataLine.getProjectName().equals(project.projectName))
                        profileDataLine.setProjectColor(project.getTicketColor());
                }
            }
        }


    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return profileDataDropdownArrayList.get(groupPosition).getProifleDataLine(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ProfileDataLine profileDataLine = (ProfileDataLine) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView projectName = (TextView) convertView
                .findViewById(R.id.list_item_name_project);
        TextView startTime = (TextView) convertView.findViewById((R.id.start_time));
        TextView finishTime = (TextView) convertView.findViewById(R.id.end_time);
        TextView description = (TextView) convertView.findViewById(R.id.list_item_name_task);
        TextView taskTime = (TextView) convertView.findViewById(R.id.sum_time);
        LinearLayout projectColor = (LinearLayout) convertView.findViewById(R.id.project_color);



        projectName.setText(profileDataLine.getProjectName());
        startTime.setText(profileDataLine.getStartingTime());
        finishTime.setText(profileDataLine.getFinishTime());
        description.setText(profileDataLine.getWorkDescription());
        taskTime.setText(profileDataLine.getWorkTime());
        projectColor.setBackgroundColor(Color.parseColor(profileDataLine.getProjectColor()));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.profileDataDropdownArrayList.get(groupPosition).getProfileDataLineArrayList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.profileDataDropdownArrayList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return profileDataDropdownArrayList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProfileDataDropdown headerTitle = (ProfileDataDropdown) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.day_date_label);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getDate());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
