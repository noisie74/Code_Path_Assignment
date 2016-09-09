package michael.com.codepath_todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    public FloatingActionButton mFabButton;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.header)
    public TextView mHeaderTextView;
    @BindView(R.id.editText)
    public EditText mEditText;
    @BindView(R.id.list_view)
    public ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mToDoItems;
    private final int ITEM_REQUEST = 1;
    private final String ITEM_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mListView.setSelector(R.drawable.selector);
        setSupportActionBar(mToolbar);
        readToDoItems();
        setAdapter();
        setFabButton();
        deleteToDoItems();
        editToDoItems();
    }

    private void addToDoItems() {
        String getToDoItem = mEditText.getText().toString();
        if (!getToDoItem.isEmpty() && getToDoItem.length() <= 40) {
            mAdapter.add(getToDoItem);
            mEditText.setText("");
            saveToDoItems();
        } else {
            Toast.makeText(this, R.string.error_input, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mToDoItems);
        mListView.setAdapter(mAdapter);
    }


    private void setFabButton() {
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDoItems();
                hideKeyboard();
            }
        });

    }

    private void deleteToDoItems() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mToDoItems.remove(position);
                mAdapter.notifyDataSetChanged();
                saveToDoItems();
                return true;
            }
        });
    }

    private void readToDoItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            mToDoItems = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            mToDoItems = new ArrayList<>();
        }
    }

    private void saveToDoItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, mToDoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEM_REQUEST && resultCode == RESULT_OK) {

            if (data != null) {

                String itemText = data.getExtras().getString("editedItemText");
                int position = data.getExtras().getInt("position");
                mToDoItems.set(position, itemText);
                mAdapter.notifyDataSetChanged();
                saveToDoItems();
            }

        }
    }

    private void editToDoItems() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(MainActivity.this, ToDoItemActivity.class);
                String clickedItem = mToDoItems.get(position);
                mIntent.putExtra(ITEM_DATA, clickedItem);
                startActivityForResult(mIntent, ITEM_REQUEST);
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
