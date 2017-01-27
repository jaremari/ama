package de.ama;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShowNotesActivity extends AppCompatActivity {

    ListView listViewNotes;

    ArrayList<File> datalist;
    ArrayList<String> textlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

    }

    @Override
    protected void onResume() {
        super.onResume();

        listViewNotes = (ListView) findViewById(R.id.listViewNotes);

        arrayListSetup();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowNotesActivity.this, android.R.layout.simple_list_item_1, textlist);
        listViewNotes.setAdapter(arrayAdapter);
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(ShowNotesActivity.this, EditNotesActivity.class);
                editIntent.putExtra("EXTRA_NOTE_NEXT",textlist.get(position));
                editIntent.putExtra("EXTRA_NOTE_FILE", datalist.get(position));
                startActivity(editIntent);
            }
        });
    }

    private void arrayListSetup(){

        datalist = new ArrayList<>();
        textlist = new ArrayList<>();

        File folder = new File(Environment.getExternalStorageDirectory(), "AMA-Notes");
        if(!folder.exists()){
            folder.mkdirs();
        }

        datalist.addAll(Arrays.asList(folder.listFiles()));
        Collections.sort(datalist);
        Collections.reverse(datalist);

        int datacounter = 0;

        while (datacounter < datalist.size()){
            textlist.add(getTextFromFile(datalist.get(datacounter)));
            datacounter++;
        }
    }

    public String getTextFromFile(File datafile){

        StringBuilder stringBuilder = new StringBuilder();

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(datafile));

            String currentRow;

            while ((currentRow = bufferedReader.readLine()) != null){
                stringBuilder.append(currentRow);
                stringBuilder.append("\n");
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return stringBuilder.toString().trim();
    }
}
