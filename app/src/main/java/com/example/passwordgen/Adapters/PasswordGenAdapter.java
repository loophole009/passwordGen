package com.example.passwordgen.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordgen.AddNewPassword;
import com.example.passwordgen.MainActivity;
import com.example.passwordgen.Model.PasswordGenModel;
import com.example.passwordgen.R;
import com.example.passwordgen.Utils.DatabaseHandler;

import java.util.List;

public class PasswordGenAdapter extends RecyclerView.Adapter<PasswordGenAdapter.ViewHolder> {

    private List<PasswordGenModel> passwordList;
    private DatabaseHandler db;
    private MainActivity activity;

    public PasswordGenAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final PasswordGenModel item = passwordList.get(position);
        holder.password.setText(item.getWebsite());
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setPassword(List<PasswordGenModel> passwordList) {
        this.passwordList = passwordList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        PasswordGenModel item = passwordList.get(position);
        db.deletePassword(item.getWebsite());
        passwordList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        PasswordGenModel item = passwordList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("website", item.getWebsite());
        bundle.putString("user", item.getUser());
        bundle.putString("password", item.getPassword());
        AddNewPassword fragment = new AddNewPassword();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewPassword.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView password;

        ViewHolder(View view) {
            super(view);
            password = view.findViewById(R.id.passwordTextView);
        }
    }
}
