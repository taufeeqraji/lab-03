package com.example.listycity;

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
    private static final String ARG_CITY = "arg_city";

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(City city);
    }

    private AddCityDialogListener listener;

    /** Required empty constructor (Android may recreate the fragment). */
    public AddCityFragment() {}

    /** Preferred creation method for editing. */
    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
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

        City cityToEdit = null;
        Bundle args = getArguments();
        if (args != null && args.getSerializable(ARG_CITY) instanceof City) {
            cityToEdit = (City) args.getSerializable(ARG_CITY);
        }

        final boolean isEditMode = (cityToEdit != null);
        if (isEditMode) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        City finalCityToEdit = cityToEdit;

        return builder
                .setView(view)
                .setTitle(isEditMode ? "Edit city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEditMode ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (isEditMode) {
                        finalCityToEdit.setName(cityName);
                        finalCityToEdit.setProvince(provinceName);
                        listener.updateCity(finalCityToEdit);
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
