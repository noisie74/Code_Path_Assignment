package michael.com.codepath_todolist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoItemActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    public FloatingActionButton mFabButton;
    @BindView(R.id.header)
    public EditText mItemTitle;
    @BindView(R.id.editText)
    public EditText mEditText;
    @BindView(R.id.list_view)
    public ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems;
    private String getItemsDescription;
    int numbers = 0;
    int position;
    String ITEM_DATA = "data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);
        ButterKnife.bind(this);

        String itemText = getIntent().getStringExtra(ITEM_DATA);
        position = getIntent().getIntExtra("position", 0);
        mItemTitle.setText(itemText);
        mItemTitle.setSelection(itemText.length());

        setAdapter();
        editText();
        deleteToDoItems();
    }


    private void setAdapter() {
        mItems = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mAdapter);
    }

    private void editText() {
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemsDescription = mEditText.getText().toString();
                mEditText.setText("");
                mItems.add(++numbers + " " + getItemsDescription);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    private void deleteToDoItems() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == mItems.size() - 1) {
                    mItems.remove(position);
                    numbers--;
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ToDoItemActivity.this, R.string.delete_entry, Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    public void onEditItem() {
        Intent data = new Intent();
        data.putExtra("editedItemText", mItemTitle.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onBackPressed() {
        onEditItem();
        super.onBackPressed();
    }
}
