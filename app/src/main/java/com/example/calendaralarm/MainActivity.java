package com.example.calendaralarm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView selectedDateText;
    private Button setAlarmButton;
    private ListView alarmListView;
    private List<AlarmItem> alarmList;
    private AlarmAdapter alarmAdapter;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initCalendar();
        initAlarmList();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        selectedDateText = findViewById(R.id.selectedDateText);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        alarmListView = findViewById(R.id.alarmListView);
        setAlarmButton.setOnClickListener(v -> openSetAlarmDialog());
    }

    private void initCalendar() {
        selectedCalendar = Calendar.getInstance();
        updateSelectedDateText();
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedCalendar.set(year, month, dayOfMonth);
            updateSelectedDateText();
        });
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        selectedDateText.setText("已选择：" + sdf.format(selectedCalendar.getTime()));
    }

    private void initAlarmList() {
        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(this, alarmList);
        alarmListView.setAdapter(alarmAdapter);
        alarmListView.setOnItemClickListener((parent, view, position, id) -> {
            showAlarmOptions(alarmList.get(position), position);
        });
        loadAlarms();
    }

    private void showAlarmOptions(AlarmItem alarm, int position) {
        new AlertDialog.Builder(this)
                .setTitle("闹钟详情")
                .setMessage("时间：" + alarm.getTimeString() + "\n标签：" + alarm.getLabel())
                .setPositiveButton("删除", (dialog, which) -> deleteAlarm(position))
                .setNegativeButton("取消", null)
                .show();
    }

    private void openSetAlarmDialog() {
        if (selectedCalendar == null) {
            Toast.makeText(this, "请先选择日期", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("year", selectedCalendar.get(Calendar.YEAR));
        intent.putExtra("month", selectedCalendar.get(Calendar.MONTH));
        intent.putExtra("day", selectedCalendar.get(Calendar.DAY_OF_MONTH));
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);
            String label = data.getStringExtra("label");
            setSystemAlarm(hour, minute, label);
        }
    }

    private void setSystemAlarm(int hour, int minute, String label) {
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, label);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
        
        if (alarmIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(alarmIntent);
            Calendar alarmCal = (Calendar) selectedCalendar.clone();
            alarmCal.set(Calendar.HOUR_OF_DAY, hour);
            alarmCal.set(Calendar.MINUTE, minute);
            AlarmItem alarm = new AlarmItem(alarmCal.getTimeInMillis(), label);
            alarmList.add(alarm);
            alarmList.sort((a, b) -> Long.compare(a.getTimeInMillis(), b.getTimeInMillis()));
            alarmAdapter.notifyDataSetChanged();
            saveAlarms();
            Toast.makeText(this, "已跳转到系统闹钟设置", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未找到系统闹钟应用", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteAlarm(int position) {
        alarmList.remove(position);
        alarmAdapter.notifyDataSetChanged();
        saveAlarms();
        Toast.makeText(this, "闹钟已删除", Toast.LENGTH_SHORT).show();
    }

    private void saveAlarms() {
        StringBuilder sb = new StringBuilder();
        for (AlarmItem alarm : alarmList) {
            sb.append(alarm.getTimeInMillis()).append(",").append(alarm.getLabel()).append(";");
        }
        getSharedPreferences("alarms", MODE_PRIVATE)
                .edit()
                .putString("alarm_list", sb.toString())
                .apply();
    }

    private void loadAlarms() {
        String saved = getSharedPreferences("alarms", MODE_PRIVATE)
                .getString("alarm_list", "");
        if (!saved.isEmpty()) {
            String[] items = saved.split(";");
            for (String item : items) {
                if (!item.isEmpty()) {
                    String[] parts = item.split(",");
                    if (parts.length >= 2) {
                        try {
                            long time = Long.parseLong(parts[0]);
                            alarmList.add(new AlarmItem(time, parts[1]));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            alarmList.sort((a, b) -> Long.compare(a.getTimeInMillis(), b.getTimeInMillis()));
            alarmAdapter.notifyDataSetChanged();
        }
    }
}
