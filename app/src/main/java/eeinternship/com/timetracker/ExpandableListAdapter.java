package eeinternship.com.timetracker;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.graphics.Color;
import java.util.ArrayList;

import Data.ProfileDataDropdown;
import Data.ProfileDataLine;

import static android.graphics.Color.parseColor;

/**
 * Created by IsakFe on 30. 03. 2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ProfileDataDropdown> profileDataDropdownArrayList;

    public ExpandableListAdapter(Context context, ArrayList<ProfileDataDropdown> profileDataDropdownArrayList) {
        this._context = context;
        this.profileDataDropdownArrayList = profileDataDropdownArrayList;

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

        RelativeLayout infoTicket=(RelativeLayout)convertView.findViewById(R.id.ticket_profile);
        infoTicket.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator v = (Vibrator)_context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
                openEditDialog();
                return true;
            }
        });

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

    public void openEditDialog(){
        LayoutInflater infalInflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertLayout = infalInflater.inflate(R.layout.edit_dialog, null);
        AlertDialog.Builder editDialog = new AlertDialog.Builder(_context);
        editDialog.setView(alertLayout);

        TextView labelProject = (TextView) alertLayout.findViewById(R.id.project_name_edit);
        EditText editDescription = (EditText) alertLayout.findViewById(R.id.edit_description);
        TimePicker timePicker = (TimePicker) alertLayout.findViewById(R.id.time_choose);
        timePicker.setIs24HourView(true);
        final TextView labelStartingFinish = (TextView) alertLayout.findViewById(R.id.starting_finsihed_time);
        final Switch pickTime = (Switch) alertLayout.findViewById(R.id.select_time);
        Button saveBtn = (Button) alertLayout.findViewById(R.id.btn_save_edit);

        pickTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pickTime.isChecked()) {
                    labelStartingFinish.setText("FINISHED TIME");
                    labelStartingFinish.setTextColor(parseColor("#f1490b"));
                   // pickTime.thum
                } else {
                    labelStartingFinish.setText("STARTING TIME");
                    labelStartingFinish.setTextColor(parseColor("#04b795"));
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AlertDialog dialog = editDialog.create();
        dialog.show();
    }
}
