package com.app.pindout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity_Third extends AppCompatActivity {
	private Toolbar toolbar;
	private EditText inputBusinessName, inputBusinessPhone,
			inputBusinessSearch;
	private TextInputLayout inputLayoutBusinessName, inputLayoutBusinessPhone,
			inputLayoutBusinessSearch;
	private Button btnNextThird;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_third);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		intent = getIntent();
		inputLayoutBusinessName = (TextInputLayout) findViewById(R.id.input_layout_businessname);
		inputLayoutBusinessPhone = (TextInputLayout) findViewById(R.id.input_layout_businessphone);
		inputLayoutBusinessSearch = (TextInputLayout) findViewById(R.id.input_layout_businessearch);
		inputBusinessName = (EditText) findViewById(R.id.input_businessname);
		inputBusinessPhone = (EditText) findViewById(R.id.input_businessphone);
		inputBusinessSearch = (EditText) findViewById(R.id.input_businessearch);

		btnNextThird = (Button) findViewById(R.id.btn_next_third);

		inputBusinessName.addTextChangedListener(new MyTextWatcher(
				inputBusinessName));
		inputBusinessPhone.addTextChangedListener(new MyTextWatcher(
				inputBusinessPhone));
		inputBusinessSearch.addTextChangedListener(new MyTextWatcher(
				inputBusinessSearch));

		btnNextThird.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitForm();
			}
		});
	}

	private void submitForm() {
		if (!validateBusinessName()) {
			return;
		}

		if (!validateBusinessPhone()) {
			return;
		}
		if (!validateBusinessSearchKey()) {
			return;
		}
		Intent i = new Intent(RegisterActivity_Third.this,
				RegisterActivity_Fourth.class);
		i.putExtra("first_name", intent.getStringExtra("first_name"));
		i.putExtra("last_name", intent.getStringExtra("last_name"));
		i.putExtra("email_or_mob", intent.getStringExtra("email_or_mob"));
		i.putExtra("password", intent.getStringExtra("password"));
		i.putExtra("business_name", inputBusinessName.getText().toString());
		i.putExtra("business_phone", inputBusinessPhone.getText().toString());
		i.putExtra("business_search", inputBusinessSearch.getText().toString());
		startActivity(i);	
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
	}

	private boolean validateBusinessName() {
		if (inputBusinessName.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessName
					.setError(getString(R.string.err_msg_businessname));
			requestFocus(inputBusinessName);
			return false;
		} else {
			inputLayoutBusinessName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateBusinessPhone() {
		if (inputBusinessPhone.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessPhone
					.setError(getString(R.string.err_msg_businessphone));
			requestFocus(inputBusinessPhone);
			return false;
		} else {
			if (inputBusinessPhone.length() < 10) {
				inputLayoutBusinessPhone
						.setError(getString(R.string.err_msg_invalid_mobileno));
				requestFocus(inputBusinessPhone);
				return false;
			} else {
				inputLayoutBusinessPhone.setErrorEnabled(false);
			}
		}
		return true;
	}

	private boolean validateBusinessSearchKey() {
		if (inputBusinessSearch.getText().toString().trim().isEmpty()) {
			inputLayoutBusinessSearch
					.setError(getString(R.string.err_msg_search_key));
			requestFocus(inputBusinessSearch);
			return false;
		} else {
			inputLayoutBusinessSearch.setErrorEnabled(false);
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
			case R.id.input_first_name:
				validateBusinessName();
				break;
			case R.id.input_last_name:
				validateBusinessPhone();
				break;
			case R.id.input_businessearch:
				validateBusinessSearchKey();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right_enter,
				R.anim.left_to_right_exit);
	}
}
