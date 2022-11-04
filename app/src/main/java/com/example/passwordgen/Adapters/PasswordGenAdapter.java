package com.example.passwordgen.Adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordgen.AddNewPassword;
import com.example.passwordgen.MainActivity;
import com.example.passwordgen.Model.PasswordGenModel;
import com.example.passwordgen.R;
import com.example.passwordgen.Utils.DatabaseHandler;

import java.text.ParseException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PasswordGenAdapter extends RecyclerView.Adapter<PasswordGenAdapter.ViewHolder> {

    private List<PasswordGenModel> passwordList;
    private final DatabaseHandler db;
    private final MainActivity activity;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    public PasswordGenAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_layout, parent, false);
        createNotificationChannel();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final PasswordGenModel item = passwordList.get(position);

        holder.password.setText(item.getWebsite());
        try {
            int progress = getProgress(item.getTimestamp());
            holder.circle.setProgress(progress);
            holder.circle.setMax(90);
            if (progress >= 90){
                sendNotification(item.getWebsite(),position);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public int getProgress(String dateInString) throws ParseException {
        Date d1;
        Date d2;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        d1 = sdf.parse(sdf.format(new Date()));
        d2 = sdf.parse(dateInString);
        assert d1 != null;
        assert d2 != null;
        long difference = Math.abs(d1.getTime() - d2.getTime());
        long differenceDates = (difference / (24 * 60 * 60 * 1000));
        return Math.toIntExact(differenceDates);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = activity.getString(R.string.channel_name);
            String description = activity.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void sendNotification(String web,int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_edit)
                .setContentTitle("update password !!")
                .setContentText(web)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(String.format("update password for %s",web)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView password;
        ProgressBar circle;
        ViewHolder(View view) {
            super(view);
            password = view.findViewById(R.id.passwordTextView);
            circle = view.findViewById(R.id.progressBar);
        }
    }
}
