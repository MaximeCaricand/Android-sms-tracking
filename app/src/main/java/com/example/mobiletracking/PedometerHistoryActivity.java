package com.example.mobiletracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class PedometerHistoryActivity extends AppCompatActivity {

    private final static int CREATE_CODE = 0;

    private DBHelper dbHelper;
    ListView pedometerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_history);

        dbHelper = new DBHelper(getApplicationContext());
        updateListView();
    }

    public void updateListView() {
        List<Walker> lw = dbHelper.getAllWalkers();

        pedometerListView = findViewById(R.id.pedometer_list_view);
        pedometerListView.setAdapter(new PedometerHistoryItemAdaptater(this, lw));
    }

    public void quit(View view) {
        this.finish();
    }

    class PedometerHistoryItemAdaptater extends BaseAdapter {

        private Context context;
        private List<Walker> lw;
        private LayoutInflater inflater;
       // private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        public PedometerHistoryItemAdaptater(Context context, List<Walker> lw) {
            this.context = context;
            this.lw = lw;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lw.size();
        }

        @Override
        public Walker getItem(int position) {
            return lw.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflater.inflate(R.layout.pedometer_item, null);

            Walker currentItem = getItem(position);

            TextView tvDateHour = view.findViewById(R.id.tv_date_hour);
            tvDateHour.setText(currentItem.getDateFormat());

            TextView tvNumberStep = view.findViewById(R.id.tv_number_step);
            tvNumberStep.setText(""+currentItem.getNumberStep() + " pas");

            TextView tvCurrentTimeSpeed = view.findViewById(R.id.tv_current_time_speed);
            tvCurrentTimeSpeed.setText(""+currentItem.getCurrentTimeSpeed() + " mètres/s");

            TextView tvDistTraveled = view.findViewById(R.id.tv_dist_traveled);
            tvDistTraveled.setText(""+currentItem.getDistTraveled() + " mètres");

            return view;
        }
    }
}