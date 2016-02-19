package com.app.pindout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity_First extends AppCompatActivity {

	private Toolbar toolbar;
	private TextView gotologin;
	private EditText inputFirstName, inputLastName, inputMobileEmail;
	private TextInputLayout inputLayoutFirstName, inputLayoutLastName,
			inputLayoutMobileEmail;
	private Button btnNextFirst;
	private String firstname = "", lastname = "", emailormob = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_first);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);
		inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_lastname);
		inputLayoutMobileEmail = (TextInputLayout) findViewById(R.id.input_layout_mobileemail);
		inputFirstName = (EditText) findViewById(R.id.input_first_name);
		inputLastName = (EditText) findViewById(R.id.input_last_name);
		inputMobileEmail = (EditText) findViewById(R.id.input_mobile_or_email);
		gotologin = (TextView) findViewById(R.id.go_to_login);
		btnNextFirst = (Button) findViewById(R.id.btn_next_first);

		inputFirstName
				.addTextChangedListener(new MyTextWatcher(inputFirstName));
		inputLastName.addTextChangedListener(new MyTextWatcher(inputLastName));
		inputMobileEmail.addTextChangedListener(new MyTextWatcher(
				inputMobileEmail));

		btnNextFirst.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitForm();
			}
		});
		gotologin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(RegisterActivity_First.this,
						Check_LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	private void submitForm() {
		if (!validateFirstName()) {
			return;
		}

		if (!validateLastName()) {
			return;
		}

		if (!validateEmail()) {
			return;
		}
		firstname = inputFirstName.getText().toString();
		lastname = inputLastName.getText().toString();
		emailormob = inputMobileEmail.getText().toString();

		Intent i = new Intent(RegisterActivity_First.this,
				RegisterActivity_Second.class);
		i.putExtra("first_name", firstname);
		i.putExtra("last_name", lastname);
		i.putExtra("email_or_mob", emailormob);
		startActivity(i);
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
	}

	private boolean validateFirstName() {
		if (inputFirstName.getText().toString().trim().isEmpty()) {
			inputLayoutFirstName.setError(getString(R.string.err_msg_name));
			requestFocus(inputFirstName);
			return false;
		} else {
			inputLayoutFirstName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateLastName() {
		if (inputLastName.getText().toString().trim().isEmpty()) {
			inputLayoutLastName.setError(getString(R.string.err_msg_lastname));
			requestFocus(inputLastName);
			return false;
		} else {
			inputLayoutLastName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateEmail() {
		if (inputMobileEmail.getText().toString().trim().isEmpty()) {
			inputLayoutMobileEmail.setError(getString(R.string.err_msg_email));
			requestFocus(inputMobileEmail);
			return false;
		} else {
			if (!isValidEmail(inputMobileEmail.getText().toString())) {
				inputLayoutMobileEmail
						.setError(getString(R.string.err_msg_invalidemail));
			} else {
				inputLayoutMobileEmail.setErrorEnabled(false);
			}
		}
		return true;
	}

	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
				validateFirstName();
				break;
			case R.id.input_last_name:
				validateLastName();
				break;
			case R.id.input_mobile_or_email:
				validateEmail();
				break;
			}
		}
	}
}