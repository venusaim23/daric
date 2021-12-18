package com.alanai.daric;

import static com.alanai.daric.Utility.Constants.KEY;
import static com.alanai.daric.Utility.Constants.PREF_NAME;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alanai.daric.Models.Diary;
import com.alanai.daric.databinding.FragmentAddDiaryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDiaryFragment extends Fragment {

    private DiaryInterface listener;

    private FragmentAddDiaryBinding binding;
    private boolean professional;
    private Context context;

    private EditText tagET;
    private String title, content, tag, date, time;
    private String[] months;

    private List<Diary> diaries;

    public interface DiaryInterface {
        void goBack();
    }

    public AddDiaryFragment(boolean professional, Context context, List<Diary> diaries) {
        this.professional = professional;
        this.context = context;
        this.diaries = diaries;

        if (context instanceof DiaryInterface)
            listener = (DiaryInterface) context;
        else
            throw new RuntimeException(context.toString() + " must implement DiaryInterface");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddDiaryBinding.inflate(inflater, container, false);

        months = context.getResources().getStringArray(R.array.months);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month = months[cal.get(Calendar.MONTH)];
        int year = cal.get(Calendar.YEAR);

        date = day + " " + month + ", " + year;
        binding.dateTv.setText(date);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addTagImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.add_a_tag));
                View dialogView = getLayoutInflater().inflate(R.layout.popup_dialog, null);

                tagET = dialogView.findViewById(R.id.tag_et_dialog);

                builder.setView(dialogView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tag = tagET.getText().toString();
                        if (!tag.isEmpty()) {
                            binding.tagCard.setVisibility(View.VISIBLE);
                            binding.tagTvNote.setText(tag);
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goBack();
            }
        });
    }

    private void getDetails() {
        title = binding.titleEt.getText().toString().trim();
        content = binding.contentEt.getText().toString();

        //set errors
        saveDetails();
    }

//    private void loadDiaries() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(KEY, null);
//        Type type = new TypeToken<ArrayList<Diary>>() {}.getType();
//        diaries = gson.fromJson(json, type);
//
//        if (diaries == null)
//            diaries = new ArrayList<>();
//    }

    private void saveDetails() {
        Calendar cal = Calendar.getInstance();
        time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        Diary diary = new Diary(System.currentTimeMillis(), title, content,
                date + " " + time, tag, false, 0, professional);

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        diaries.add(diary);
        String json = gson.toJson(diaries);
        editor.putString(KEY, json);
        editor.apply();

        listener.goBack();
    }
}