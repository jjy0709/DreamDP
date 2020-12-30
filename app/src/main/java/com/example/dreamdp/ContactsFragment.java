package com.example.dreamdp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ContactsFragment extends Fragment {

    public ContactsFragment() {
        // Required empty public constructor
    }

    static final String[] LIST_NAMES = {"name1", "name2", "name3"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView listView = (ListView) v.findViewById(R.id.contacts_list);
        ListViewAdapter adapter = new ListViewAdapter();
        listView.setAdapter(adapter);


        AssetManager assetManager = getActivity().getAssets();
        try{
            Drawable pic = getResources().getDrawable(R.drawable.android);
            InputStream is = assetManager.open("jsons/contacts");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();
            //textView.setText(jsonData);
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0; i<jsonArray.length();i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String name = jo.getString("name");
                String number = jo.getString("number");
                adapter.addItem(pic, name, number);
            }

        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle();
                String descStr = item.getDesc();
                Drawable iconDrawable = item.getIcon();


                Intent i = new Intent(getContext(), ContactActivity.class);
                i.putExtra("name",titleStr);
                i.putExtra("number",descStr);


            }
        });

        return v;
    }
}
