package com.richardsolomou.atmos;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.richardsolomou.atmos.helper.DatabaseHelper;
import com.richardsolomou.atmos.model.Student;


public class AddStudentActivity extends BaseActivity {

	private DatabaseHelper db;
	private EditText etStudentID;
	private String uid;
	private int id_length = 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addstudent);

		db = new DatabaseHelper(getApplicationContext());
		uid = getIntent().getStringExtra("uid");

		etStudentID = (EditText) findViewById(R.id.etStudentID);
		Button btnAdd = (Button) findViewById(R.id.btnAdd);

		etStudentID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				boolean handled = false;

				if (i == EditorInfo.IME_ACTION_DONE) {
					String id = etStudentID.getText().toString();

					if (id == null || id.length() < id_length) {
						etStudentID.setError("Student ID must be " + id_length + " characters or more.");
					} else {
						addStudent(textView);
						handled = true;
					}
				}

				return handled;
			}
		});

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String id = etStudentID.getText().toString();
				if (id == null || id.length() < id_length) {
					etStudentID.setError("Student ID must be " + id_length + " characters or more.");
				} else {
					addStudent(view);
				}
			}
		});
	}

	public void addStudent(View view) {
		Student student = new Student();

		student.setStudentID(etStudentID.getText().toString());
		student.setUID(uid);
		student.setCreatedAt(db.getDateTime());
		student.setUpdatedAt(db.getDateTime());

		boolean createStudent = db.createStudent(student);

		if (createStudent) {
			Toast.makeText(getApplicationContext(), "Student " + student.getStudentID() + " was successfully added to the database.", Toast.LENGTH_SHORT).show();
			Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(objIntent);
		} else {
			Toast.makeText(getApplicationContext(), "Failed to add student.", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancelAdd(View view) {
		Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(objIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

}