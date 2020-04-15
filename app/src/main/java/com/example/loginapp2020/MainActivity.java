package com.example.loginapp2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddUserDialog.AddUserDialogListener {

    public ArrayList<User> users = new ArrayList<User>();
    public UserAdapter adapter;

    private int notificationIdCounter = 0;

    public static class ConfirmDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_are_you_sure)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doSomethingAfterYesClick();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doSomethingAfterNoClick();
                        }
                    });
            return  builder.create();
        }
    }

    private static void doSomethingAfterYesClick() {
        //Toast.makeText(this, R.string.dialog_yes, Toast.LENGTH_SHORT).show();
    }

    private static void doSomethingAfterNoClick() {
        //Toast.makeText(getApplicationContext(), R.string.dialog_no, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_view_users);
        adapter = new UserAdapter(users);
        listView.setAdapter(adapter);

        Button addUserButton = (Button) findViewById(R.id.button_add_user);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "channel", importance);
            channel.setDescription("description");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showAddUserDialog() {
        DialogFragment dialog = new AddUserDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_add_user));

    }

    private void tryLogin() {

        String username = ((EditText) findViewById(R.id.edit_text_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.edit_text_password)).getText().toString();

        boolean loginOk = false;
        String correctPassword = "";
        int notificationId = -1;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().compareTo(username) == 0) {
                loginOk = true;
                notificationId = i;
                correctPassword = users.get(i).getPassword();
            }
        }

        if (!loginOk) {
            sendNoUserNotification(username);
            return;
        } else {
            loginOk = password.compareTo(correctPassword) == 0;
        }

        if (loginOk) {
            sendOkNotification(username, notificationId);
        } else {
            sendNotOkNotification(username, correctPassword, password, notificationId);
        }

        //DialogFragment dialog = new ConfirmDialogFragment();
        //dialog.show(getSupportFragmentManager(), getString(R.string.dialog_confirm));
        //Toast.makeText(this, "Tekst ispisan pre klika na confirm dialog", Toast.LENGTH_LONG).show();
    }

    private void sendNoUserNotification(String username) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("USERNAME DOES NOT EXIST");
        builder.setContentText(username);
        builder.setGroup("NO_USERNAME_GROUP");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(--notificationIdCounter, builder.build());
    }

    private void sendOkNotification(String username, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("LOGIN SUCCESS");
        builder.setContentText(username);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

    private void sendNotOkNotification(String username, String correctPassword, String password, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("LOGIN FAIL");
        builder.setContentText(username);
        builder.setAutoCancel(true);

        Intent intent = new Intent(this, SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("USERNAME", username);
        intent.putExtra("PASSWORD", correctPassword);
        intent.putExtra("TRY", password);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        //builder.addAction(R.drawable.ic_launcher_background, "CLICK", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog d = dialog.getDialog();
        String username = ((EditText) d.findViewById(R.id.edit_text_dialog_username)).getText().toString();
        String password = ((EditText) d.findViewById(R.id.edit_text_dialog_password)).getText().toString();
        String role = ((Spinner) d.findViewById(R.id.spinner_roles)).getSelectedItem().toString();
        User user = new User(username, password, User.Role.valueOf(role));
        users.add(user);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
