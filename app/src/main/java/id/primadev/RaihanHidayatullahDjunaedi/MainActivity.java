package id.primadev.RaihanHidayatullahDjunaedi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.primadev.RaihanHidayatullahDjunaedi.Models.Contact;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    public RecyclerView rv;
    public ContactAdapter contactAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public List<Contact> listContact = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });

        rv = findViewById(R.id.rvContact);
        //List kontak yang akan di tampilkan (nama, telepon, url gambar

        listContact.add(new Contact("Iron Man",
                "102018308",
                "https://cdn4.iconfinder.com/data/icons/famous-characters-add-on-vol-1-flat/48/Famous_Character_-_Add_On_1-14-512.png"));

        listContact.add(new Contact("Bat Man",
                "102018309",
                "https://cdn4.iconfinder.com/data/icons/famous-characters-add-on-vol-1-flat/48/Famous_Character_-_Add_On_1-22-512.png"));

        listContact.add(new Contact("Groot",
                "102018307",
                "https://cdn4.iconfinder.com/data/icons/famous-characters-add-on-vol-1-flat/48/Famous_Character_-_Add_On_1-21-512.png"));

        listContact.add(new Contact("Sonic",
                "102018301",
                "https://cdn4.iconfinder.com/data/icons/famous-characters-add-on-vol-1-flat/48/Famous_Character_-_Add_On_1-26-512.png"));

        contactAdapter = new ContactAdapter(listContact);
        contactAdapter.setListener(this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setAdapter(contactAdapter);
        rv.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view, int position) {
        Contact contact = listContact.get(position);
        Toast.makeText(this, contact.getName(), Toast.LENGTH_LONG)
                .show();
    }
    public void insertItem(int position){
        listContact.add(position, new Contact("New Item" + position,
                "102018308",
                "https://cdn4.iconfinder.com/data/icons/famous-characters-add-on-vol-1-flat/48/Famous_Character_-_Add_On_1-14-512.png"));
        contactAdapter.notifyDataSetChanged();
    }

    public void removeItem(int position){
        listContact.remove(position);
        contactAdapter.notifyDataSetChanged();
    }
}
