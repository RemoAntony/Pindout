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

public class RegisterActivity_Second extends AppCompatActivity {
	private Toolbar toolbar;
	private EditText inputPassword, inputConfirmPassword;
	private TextInputLayout inputLayoutPassword, inputLayoutConfirmPassword;
	private Button btnNextSecond;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_second);
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
		inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirmpass);
		inputPassword = (EditText) findViewById(R.id.input_password);
		inputConfirmPassword = (EditText) findViewById(R.id.input_confirm_pass);
		btnNextSecond = (Button) findViewById(R.id.btn_next_second);

		intent = getIntent();

		inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
		inputConfirmPassword.addTextChangedListener(new MyTextWatcher(
				inputConfirmPassword));

		btnNextSecond.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitForm();
			}
		});
	}

	private void submitForm() {
		if (!validatePassword()) {
			return;
		}

		if (!validateConfirmPassword()) {
			return;
		}
		Intent i = new Intent(RegisterActivity_Second.this,
				RegisterActivity_Third.class);
		i.putExtra("first_name", intent.getStringExtra("first_name"));
		i.putExtra("last_name", intent.getStringExtra("last_name"));
		i.putExtra("email_or_mob", intent.getStringExtra("email_or_mob"));
		i.putExtra("password", inputPassword.getText().toString());
		startActivity(i);
		overridePendingTransition(R.anim.right_to_left_enter,
				R.anim.right_to_left_exit);
	}

	private boolean validatePassword() {
		if (inputPassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(getString(R.string.err_msg_enterpass));
			requestFocus(inputPassword);
			return false;
		} else {
			if (inputPassword.length() < 8) {
				inputLayoutPassword
						.setError(getString(R.string.err_msg_pass_eight_char));
				requestFocus(inputPassword);
				return false;
			} else {
				inputLayoutPassword.setErrorEnabled(false);
			}
		}
		return true;
	}

	private boolean validateConfirmPassword() {
		if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
			inputLayoutConfirmPassword
					.setError(getString(R.string.err_msg_enterconfirmpass));
			requestFocus(inputConfirmPassword);
			return false;
		} else {
			String pass = inputPassword.getText().toString();
			String confirmpass = inputConfirmPassword.getText().toString();
			if (pass.equals(confirmpass)) {
				inputLayoutConfirmPassword.setErrorEnabled(false);
			} else {
				inputLayoutConfirmPassword
						.setError(getString(R.string.err_msg_passdonotmatch));
				requestFocus(inputConfirmPassword);
				return false;
			}
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
				validatePassword();
				break;
			case R.id.input_last_name:
				validateConfirmPassword();
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