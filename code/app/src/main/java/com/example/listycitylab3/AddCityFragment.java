package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    private EditText cityEdit;
    private EditText provinceEdit;
    private AddCityDialogListener listener;
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, City city);
    }
    public static AddCityFragment newInstance(@Nullable String city, @Nullable String province, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        args.putString("province", province);
        args.putInt("position", position); // -1 for add mode
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        int position = -1;
        if (args != null) {
            editCityName.setText(args.getString("city", ""));
            editProvinceName.setText(args.getString("province", ""));
            position = args.getInt("position", -1);
        }

        int finalPosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(finalPosition == -1 ? "Add City" : "Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (!cityName.isEmpty() && !provinceName.isEmpty()) {
                        City newCity = new City(cityName, provinceName);
                        if (finalPosition == -1) {
                            listener.addCity(newCity);
                        } else {
                            listener.editCity(finalPosition, newCity);
                        }
                    }
                })
                .create();
    }
}
