package com.example.dreamdp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.app.Activity.RESULT_FIRST_USER;

public class ContactsFragment extends Fragment {

    public JSONArray jsonArray;
    public ListViewAdapter adapter;
    ListView listView;
    public String filename = "file1.txt";
    public ContactsFragment() {
        // Required empty public constructor
    }

    static final String[] LIST_NAMES = {"name1", "name2", "name3"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = (ListView) v.findViewById(R.id.contacts_list);
        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);



        AssetManager assetManager = getActivity().getAssets();
        try{
            FileInputStream fis = new FileInputStream(getContext().getFilesDir() + "/" + filename);
            //InputStream is = assetManager.open("jsons/contacts");
            //InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            Drawable pic;

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                String jsonData = line;
                //textView.setText(jsonData);
                JSONObject jsonObject = new JSONObject(jsonData);
                String name = jsonObject.getString("name");
                String number = jsonObject.getString("number");
                int pic_n = jsonObject.getInt("picture");
                if(pic_n == 0) {pic = getResources().getDrawable(R.drawable.finn_circle);}
                else if(pic_n == 1){pic = getResources().getDrawable(R.drawable.jake_circle);}
                else {pic = getResources().getDrawable(R.drawable.bmo_circle);}

                adapter.addItem(pic_n, pic, name, number);
            }
            fis.close();

        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }

        SearchView searchView = v.findViewById(R.id.contact_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query == ""){
                    listView.setAdapter(adapter);
                }
                else{
                    ListViewAdapter adapter_search = new ListViewAdapter();
                    int num_item = adapter.getCount();
                    for(int i=0;i<num_item;i++){
                        ListViewItem item1 = (ListViewItem) adapter.getItem(i);
                        if(item1.getTitle().contains(query) || item1.getDesc().contains(query)){
                            adapter_search.addItem(item1.getPic_num(),item1.getIcon(),item1.getTitle(),item1.getDesc());
                        }
                    }
                    listView.setAdapter(adapter_search);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == ""){
                    listView.setAdapter(adapter);
                }
                else{
                    ListViewAdapter adapter_search = new ListViewAdapter();
                    int num_item = adapter.getCount();
                    for(int i=0;i<num_item;i++){
                        ListViewItem item1 = (ListViewItem) adapter.getItem(i);
                        if(item1.getTitle().contains(newText) || item1.getDesc().contains(newText)){
                            adapter_search.addItem(item1.getPic_num(),item1.getIcon(),item1.getTitle(),item1.getDesc());
                        }
                    }
                    listView.setAdapter(adapter_search);
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle();
                String descStr = item.getDesc();
                int pic_n = item.getPic_num();
                Drawable iconDrawable = item.getIcon();

                Intent i = new Intent(getContext(), ContactViewActivity.class);
                i.putExtra("name",titleStr);
                i.putExtra("number",descStr);
                i.putExtra("picture",pic_n);
                startActivity(i);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_contact, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.Revise_contact:
                                Intent i = new Intent(getContext(), ContactReviseActivity.class);
                                ListViewItem list_item_R = (ListViewItem) parent.getItemAtPosition(position);
                                String name_r = list_item_R.getTitle();
                                String number_r = list_item_R.getDesc();
                                int pic_ = list_item_R.getPic_num();
                                i.putExtra("name", name_r);
                                i.putExtra("number", number_r);
                                i.putExtra("picture",pic_);
                                i.putExtra("position",position);

                                startActivityForResult(i,1);

                                break;
                            case R.id.Delete_contact:
                                ListViewItem list_item = (ListViewItem) parent.getItemAtPosition(position);
                                String name = list_item.getTitle();
                                String number = list_item.getDesc();
                                Drawable icon = item.getIcon();
                                adapter.getItem(position);
                                adapter.rmItem(position);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);
                                File file = new File(getContext().getFilesDir() + "/" + filename);
                                String dummy = "";
                                try{
                                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                                    String line;
                                    for(int j=0; j<position; j++){
                                        line = br.readLine();
                                        dummy += (line + "\n");
                                    }
                                    String delData = br.readLine();
                                    while((line = br.readLine()) != null){
                                        dummy += (line + "\n");
                                    }

                                    FileWriter fw = new FileWriter(getContext().getFilesDir() + "/" + filename);
                                    fw.write(dummy);
                                    fw.close();
                                    br.close();
                                }
                                 catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();

                return true;
            }
        });

        return v;
    }



    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        String name = args.getString("name");
        String number = args.getString("number");
        int n = args.getInt("picture");
        Drawable pic;
        if(n == 0){pic = getResources().getDrawable(R.drawable.finn_circle);}
        else if(n == 1){pic = getResources().getDrawable(R.drawable.jake_circle);}
        else {pic = getResources().getDrawable(R.drawable.bmo_circle);}
        adapter.addItem(n,pic,name,number);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        try {
            File file = getContext().getFilesDir();
            FileOutputStream fos = new FileOutputStream(getContext().getFilesDir() + "/" + filename, true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            JSONObject json1 = new JSONObject();
            json1.put("name",name);
            json1.put("number",number);
            json1.put("picture",n);
            String json = json1.toString();
            writer.write(json + "\n");
            writer.flush();
            writer.close();
            fos.close();
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    public void Delete_all(){
        File file = new File(getContext().getFilesDir() +"/" + filename);
        file.delete();
        adapter.rmAll();
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_FIRST_USER) {
            super.onActivityResult(requestCode, resultCode, data);
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("number");
            int pic_ = data.getIntExtra("picture", 0);
            int position = data.getIntExtra("position",0);
            JSONObject json = new JSONObject();

            File file = new File(getContext().getFilesDir() + "/" + filename);
            String dummy = "";
            try {
                json.put("name", name);
                json.put("number", number);
                json.put("picture",pic_);
                String revise_info = json.toString();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                for (int j = 0; j < position; j++) {
                    line = br.readLine();
                    dummy += (line + "\n");
                }
                String delData = br.readLine();
                dummy += revise_info + "\n";
                while ((line = br.readLine()) != null) {
                    dummy += (line + "\n");
                }

                FileWriter fw = new FileWriter(getContext().getFilesDir() + "/" + filename);
                fw.write(dummy);
                fw.close();
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListViewItem item = (ListViewItem) adapter.getItem(position);
            item.setTitle(name);
            item.setDesc(number);
            item.setPic_num(pic_);
            if(pic_ == 0){item.setIcon(getResources().getDrawable(R.drawable.finn_circle));}
            else if(pic_ == 1){item.setIcon(getResources().getDrawable(R.drawable.jake_circle));}
            else {item.setIcon(getResources().getDrawable(R.drawable.bmo_circle));}
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
