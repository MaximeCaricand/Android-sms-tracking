package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class PositionHistory extends AppCompatActivity {

    private DBHelper dbHelper;
    ListView positionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_history);

        this.dbHelper = new DBHelper(getApplicationContext());
        this.updateListView();
    }

    public void updateListView() {
        List<Position> phList = this.dbHelper.getAllPositions();
        positionListView = findViewById(R.id.position_history_listview);
        positionListView.setAdapter(new PositionHistoryItemAdaptater(this, phList));
    }

    public void quit(View view) {
        this.finish();
    }

    class PositionHistoryItemAdaptater extends BaseAdapter {

        private Context context;
        private List<Position> phList;
        private LayoutInflater inflater;

        public PositionHistoryItemAdaptater(Context context, List<Position> phList) {
            this.context = context;
            this.phList = phList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.phList.size();
        }

        @Override
        public Position getItem(int position) {
            return this.phList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            DecimalFormat latLngFormat = new DecimalFormat("0.00000000");

            view = inflater.inflate(R.layout.position_history_item, null);

            Position currentItem = this.getItem(position);

            TextView nameView = view.findViewById(R.id.ph_date);
            nameView.setText(currentItem.getFullDateFormat());

            TextView dateView = view.findViewById(R.id.ph_phoneNumber);
            dateView.setText("+33" + currentItem.getPhoneNumber());

            TextView startHourView = view.findViewById(R.id.ph_lat);
            startHourView.setText("Lat : " + latLngFormat.format(currentItem.getLat()));

            TextView endHourView = view.findViewById(R.id.ph_lng);
            endHourView.setText("Lng : " + latLngFormat.format(currentItem.getLng()));

            return view;
        }
    }
}