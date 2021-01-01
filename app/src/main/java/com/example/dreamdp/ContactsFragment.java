package com.example.dreamdp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.renderscript.ScriptGroup;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.Buffer;
import java.util.Collection;

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
            Drawable pic = getResources().getDrawable(R.drawable.android);
            FileInputStream fis = new FileInputStream(getContext().getFilesDir() + "/" + filename);
            InputStream is = assetManager.open("jsons/contacts");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                String jsonData = line;
                //textView.setText(jsonData);
                JSONObject jsonObject = new JSONObject(jsonData);
                String name = jsonObject.getString("name");
                String number = jsonObject.getString("number");
                adapter.addItem(pic, name, number);
            }
            fis.close();

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
                                Intent i = new Intent(getContext(),Contact_Revise.class);
                                ListViewItem list_item_R = (ListViewItem) parent.getItemAtPosition(position);
                                String name_r = list_item_R.getTitle();
                                String number_r = list_item_R.getDesc();
                                i.putExtra("name", name_r);
                                i.putExtra("number", number_r);
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
        adapter.addItem(getResources().getDrawable(R.drawable.android),name,number);

        try {
            File file = getContext().getFilesDir();
            FileOutputStream fos = new FileOutputStream(getContext().getFilesDir() + "/" + filename, true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            JSONObject json1 = new JSONObject();
            json1.put("name",name);
            json1.put("number",number);
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
            int position = data.getIntExtra("position", 0);
            JSONObject json = new JSONObject();

            File file = new File(getContext().getFilesDir() + "/" + filename);
            String dummy = "";
            try {
                json.put("name", name);
                json.put("number", number);
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
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
