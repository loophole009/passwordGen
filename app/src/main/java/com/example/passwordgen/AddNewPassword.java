package com.example.passwordgen;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.passwordgen.Adapters.PasswordGenAdapter;
import com.example.passwordgen.Model.PasswordGenModel;
import com.example.passwordgen.Utils.DatabaseHandler;

import java.util.Objects;

public class AddNewPassword extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newWebsiteText;
    private EditText newUserText;
    private EditText newPasswordText;
    private Button newPasswordSaveButton;

    private DatabaseHandler db;

    public static AddNewPassword newInstance(){
        return new AddNewPassword();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_password, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newWebsiteText = requireView().findViewById(R.id.newWebsiteText);
        newUserText = requireView().findViewById(R.id.newUserText);
        newPasswordText = requireView().findViewById(R.id.newPasswordText);
        newPasswordSaveButton = getView().findViewById(R.id.newPasswordButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String website = bundle.getString("website");
            String user = bundle.getString("user");
            String password = bundle.getString("password");
            newWebsiteText.setText(website);
            newUserText.setText(user);
            newPasswordText.setText(password);
            assert website != null;
            if(password != bundle.getString("password"))
                newPasswordSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("") && s.toString().equals(bundle.getString("password"))){
                    newPasswordSaveButton.setEnabled(false);
                    newPasswordSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newPasswordSaveButton.setEnabled(true);
                    newPasswordSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newPasswordSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String websiteText = newWebsiteText.getText().toString();
                String userText = newUserText.getText().toString();
                String passwordText = newPasswordText.getText().toString();
                if(finalIsUpdate){
                    db.updatePassword(bundle.getString("website"), passwordText);
                }
                else {
                    PasswordGenModel password = new PasswordGenModel();
                    password.setWebsite(websiteText);
                    password.setUser(userText);
                    password.setPassword(passwordText);
                    db.insertPassword(password);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
