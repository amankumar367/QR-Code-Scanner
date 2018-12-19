package com.dev.aman.qrcodescanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.dev.aman.qrcodescanner.DBHelper.DatabaseHelper;
import com.dev.aman.qrcodescanner.adapter.QrListAdapter;
import com.dev.aman.qrcodescanner.model.QrModel;

import java.util.ArrayList;

public class ViewScannedCode extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QrListAdapter adapter;
    private ArrayList<QrModel> arrayList;
    private DatabaseHelper mDatabase;
    private ImageView changeToGrid,changeToLinear,back;
    private TextView itemList,sortList;
    ListPopupWindow listPopupWindow;
    String[] sortTo={"Date", "Size"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scanned_code);

        init();
        mDatabase = new DatabaseHelper(this);
        listPopupWindow = new ListPopupWindow(this);

        listPopupWindow.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,sortTo));
        listPopupWindow.setAnchorView(sortList);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sortBy(i);
            }
        });
        changeLayoutRecyclerView(false);
        adapter = new QrListAdapter(mDatabase.getAllData(""),mDatabase, this);
        recyclerView.setAdapter(adapter);
        onClicks();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        changeToGrid = findViewById(R.id.change_grid);
        changeToLinear = findViewById(R.id.change_linear);
        sortList = findViewById(R.id.sort_list);
        back = findViewById(R.id.back);
    }

    private void sortBy(int i) {

        sortList.setText("Sorted By " + sortTo[i]);
        if(!sortTo[i].equals("Size")){
            recyclerView.removeAllViews();
            adapter = new QrListAdapter(mDatabase.getAllData(""),mDatabase, this);

        } else {
            adapter = new QrListAdapter(mDatabase.getAllData("Size"),mDatabase, this);
        }
        recyclerView.setAdapter(adapter);
        listPopupWindow.dismiss();
    }

    private void onClicks() {

        changeToGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              changeLayoutRecyclerView(true);
            }
        });


        changeToLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutRecyclerView(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPopupWindow.show();
            }
        });
    }

    private void changeLayoutRecyclerView(Boolean isGrid){
        if(isGrid){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            changeToLinear.setVisibility(View.VISIBLE);
            changeToGrid.setVisibility(View.GONE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            changeToGrid.setVisibility(View.VISIBLE);
            changeToLinear.setVisibility(View.GONE);
        }
    }
}
