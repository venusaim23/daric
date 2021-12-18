package com.alanai.daric;

import static com.alanai.daric.Utility.Constants.KEY;
import static com.alanai.daric.Utility.Constants.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alanai.daric.Adapters.DiaryAdapter;
import com.alanai.daric.Models.Diary;
import com.alanai.daric.databinding.FragmentDairiesBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DiariesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentDairiesBinding binding;

    private DiariesInterface listener;

    private Context context;
    private int position;

    private List<Diary> diaries;
    private List<Diary> specificDiaries;

    private DiaryAdapter adapter;

    public interface DiariesInterface {
        void refreshItems();
    }

    public DiariesFragment(Context context, int position, List<Diary> diaries) {
        this.context = context;
        this.position = position;
        this.diaries = diaries;

        specificDiaries = new ArrayList<>();

        if (context instanceof DiariesInterface)
            listener = (DiariesInterface) context;
        else
            throw new RuntimeException(context.toString() + " must implement DiariesInterface");
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

    private void loadSpecificList() {
        for (Diary d: diaries) {
            if (d.isProfessional() && position == 0) {
                specificDiaries.add(0, d);
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.emptyTv.setVisibility(View.GONE);
            }

            if (!d.isProfessional() && position == 1) {
                specificDiaries.add(0, d);
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.emptyTv.setVisibility(View.GONE);
            }
        }

        if (specificDiaries.isEmpty()) {
            binding.emptyTv.setVisibility(View.VISIBLE);
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDairiesBinding.inflate(inflater, container, false);

        adapter = new DiaryAdapter(context, specificDiaries);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (specificDiaries.isEmpty())
//            binding.emptyTv.setVisibility(View.VISIBLE);
//        else
//            binding.emptyTv.setVisibility(View.GONE);

        binding.swipeRefreshLayout.setRefreshing(true);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.alan_blue);
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);

        loadSpecificList();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {
        specificDiaries.clear();
        adapter.notifyDataSetChanged();
        binding.swipeRefreshLayout.setRefreshing(true);
        listener.refreshItems();
        loadSpecificList();
    }
}