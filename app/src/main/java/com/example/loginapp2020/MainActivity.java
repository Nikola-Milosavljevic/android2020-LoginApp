package com.example.loginapp2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
        
    }

    private void showAddUserDialog() {
        DialogFragment dialog = new AddUserDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_add_user));

    }

    private void tryLogin() {
        DialogFragment dialog = new ConfirmDialogFragment();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_confirm));
        Toast.makeText(this, "Tekst ispisan pre klika na confirm dialog", Toast.LENGTH_LONG).show();
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
