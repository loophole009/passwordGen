package com.example.passwordgen;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.passwordgen.Adapters.PasswordGenAdapter;
import com.example.passwordgen.Model.PasswordGenModel;
import com.example.passwordgen.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView passwordsRecyclerView;
    private PasswordGenAdapter passwordsAdapter;
    private FloatingActionButton fab;

    private List<PasswordGenModel> passwordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        passwordsRecyclerView = findViewById(R.id.passwordsRecyclerView);
        passwordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        passwordsAdapter = new PasswordGenAdapter(db,MainActivity.this);
        passwordsRecyclerView.setAdapter(passwordsAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(passwordsAdapter));
        itemTouchHelper.attachToRecyclerView(passwordsRecyclerView);

        fab = findViewById(R.id.fab);

        passwordList = db.getAllPasswords();
        Collections.reverse(passwordList);

        passwordsAdapter.setPassword(passwordList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewPassword.newInstance().show(getSupportFragmentManager(), AddNewPassword.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        passwordList = db.getAllPasswords();
        Collections.reverse(passwordList);
        passwordsAdapter.setPassword(passwordList);
        passwordsAdapter.notifyDataSetChanged();
    }
}