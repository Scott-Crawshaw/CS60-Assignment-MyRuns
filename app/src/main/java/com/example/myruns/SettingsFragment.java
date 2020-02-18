package com.example.myruns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    int unitChecked = 0;
    String comments = "";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
        findPreference("profile").setOnPreferenceClickListener(this);
        findPreference("privacy").setOnPreferenceClickListener(this);
        findPreference("unit").setOnPreferenceClickListener(this);
        findPreference("comments").setOnPreferenceClickListener(this);
        findPreference("webpage").setOnPreferenceClickListener(this);

        SharedPreferences settingsPrefs = getActivity().getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
        unitChecked = settingsPrefs.getInt("unit", 0);
        comments = settingsPrefs.getString("comments", "");
        ((CheckBoxPreference) findPreference("privacy")).setChecked(settingsPrefs.getBoolean("privacy", false));

    }

    @Override
    public boolean onPreferenceClick (Preference preference)
    {
        String key = preference.getKey();
        if(key.equals("profile")){
            Intent profile = new Intent(getActivity(), Profile.class);
            startActivity(profile);
        }
        else if(key.equals("privacy")){
            SharedPreferences settingsPrefs = getActivity().getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
            SharedPreferences.Editor editor = settingsPrefs.edit();
            editor.putBoolean("privacy", ((CheckBoxPreference) preference).isChecked());
            editor.commit();
        }
        else if(key.equals("unit")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Unit Preference");
            builder.setSingleChoiceItems(R.array.units, unitChecked, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    SharedPreferences settingsPrefs = getActivity().getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = settingsPrefs.edit();
                    editor.putInt("unit", which);
                    editor.commit();
                    unitChecked = which;
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else if(key.equals("comments")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Comments");
            final EditText input = new EditText(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(comments);
            builder.setView(input);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences settingsPrefs = getActivity().getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = settingsPrefs.edit();
                    editor.putString("comments", input.getText().toString());
                    editor.commit();
                    comments = input.getText().toString();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(key.equals("webpage")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://web.cs.dartmouth.edu/"));
            startActivity(browserIntent);
        }

        return true;
    }

}
