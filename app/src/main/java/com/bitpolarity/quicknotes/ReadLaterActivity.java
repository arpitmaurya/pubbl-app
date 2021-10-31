package com.bitpolarity.quicknotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bitpolarity.quicknotes.databinding.ActivityNoteAdditionBinding;
import com.bitpolarity.quicknotes.db.AppDatabase;
import com.bitpolarity.quicknotes.db.Note;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ReadLaterActivity extends AppCompatActivity {

    ActivityNoteAdditionBinding binding;
    String titlestr;
    boolean edited = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteAdditionBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        showBottomSheet(text.toString());



    }


    void showBottomSheet(String note){

        final BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_addnotes);

        AppCompatButton addbtn= bottomSheetDialog.findViewById(R.id.bottomsheet_addnote_button);
        ImageButton editbtn = bottomSheetDialog.findViewById(R.id.edit_note_button_bottom_sheet);
        EditText title = bottomSheetDialog.findViewById(R.id.bottomsheet_title_tv);
        EditText edit_area = bottomSheetDialog.findViewById(R.id.edit_note_bottom_sheet);

        assert edit_area != null;
        edit_area.setText(note);

        assert addbtn != null;
        addbtn.setOnClickListener(view -> {

            assert title != null;
            titlestr = title.getText().toString();
            if(edited){
            saveNewNote(titlestr, edit_area.getText().toString());
            }else{
                saveNewNote(titlestr, note);
            }

        });

        bottomSheetDialog.show();

        assert editbtn != null;
        editbtn.setOnClickListener(view -> {

            if(edit_area.getVisibility()==View.VISIBLE){
                edit_area.setVisibility(View.GONE);
        }else{
                assert edit_area != null;
                edit_area.setVisibility(View.VISIBLE);
                edit_area.requestFocus();
                edited = true;
            }

        });



        if(!bottomSheetDialog.isShowing()) {
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void saveNewNote(String title, String desc){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        Note note = new Note();

        note.title = title;
        note.desc = desc;
        db.noteDao().insertNote(note);
        Toast.makeText(this, "Added to read later", Toast.LENGTH_SHORT).show();
        finish();
    }
}