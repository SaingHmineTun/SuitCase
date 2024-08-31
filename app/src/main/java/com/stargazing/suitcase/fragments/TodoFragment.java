package com.stargazing.suitcase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stargazing.suitcase.activities.DetailActivity;
import com.stargazing.suitcase.activities.HomeActivity;
import com.stargazing.suitcase.adapters.FragmentLifeCycle;
import com.stargazing.suitcase.adapters.ItemAdapter;
import com.stargazing.suitcase.adapters.ItemClickListener;
import com.stargazing.suitcase.database.dao.ItemDao;
import com.stargazing.suitcase.database.entities.Item;
import com.stargazing.suitcase.databinding.FragmentTodoBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TodoFragment extends Fragment implements FragmentLifeCycle {

    private FragmentTodoBinding binding;
    private String userEmail;
    private ItemDao itemDao;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        initListView();
        initListeners();
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    public void refreshListView() {

        itemList = itemDao.getTodoItemsByUserEmail(userEmail);
        itemAdapter.updateListItems(itemList);

        if (itemList.isEmpty()) binding.ivEmpty.setVisibility(View.VISIBLE);
        else binding.ivEmpty.setVisibility(View.GONE);

    }

    private void initListView() {
        itemAdapter = new ItemAdapter(new ItemClickListener() {
            @Override
            public void onItemClicked(Item item) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }

            @Override
            public void onFinishItemClicked(Item item) {
                itemDao.updateItem(item);
                refreshListView();
            }
        });
        binding.rvItems.setAdapter(itemAdapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListeners() {
        binding.fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            startActivity(intent);
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                var item = itemList.get(position);
                if (swipeDir == ItemTouchHelper.LEFT) {
                    // Swipe left to delete
                    itemDao.deleteItem(item);
                    refreshListView();
                    Snackbar.make(binding.getRoot(), "Delete item success!", Snackbar.LENGTH_LONG).setAction("Undo delete", (v) -> {
                        itemDao.addItem(item);
                        refreshListView();
                    }).show();
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    // Swipe right to finish
                    item.setFinish(!item.isFinish());
                    itemDao.updateItem(item);
                    Snackbar.make(binding.getRoot(), "Mark as finish success!", Snackbar.LENGTH_LONG).setAction("Undo finish", v -> {
                        item.setFinish(!item.isFinish());
                        itemDao.updateItem(item);
                        refreshListView();
                    }).show();
                }
                refreshListView();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvItems);
    }

    private void initUi() {
        userEmail = ((HomeActivity) getActivity()).userEmail;
        itemDao = ((HomeActivity) getActivity()).itemDao;
        itemList = itemDao.getTodoItemsByUserEmail(userEmail);
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        refreshListView();

    }
}