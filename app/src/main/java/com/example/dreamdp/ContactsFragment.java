package com.example.dreamdp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, LIST_NAMES);
        listView.setAdapter(adapter);
        return v;
    }
}
