package com.app.pindout;

/**
 * Created by REMO on 19-11-2015.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Profile extends Fragment {

    EditText inputFirstName,inputEmail,inputLastName,inputPhone,inputAddress,inputpass;
    TextView inputDOB, inputanniversary;
    TextInputLayout inputLayoutFirstName,inputLayoutEmail,inputLayoutLastName,inputLayoutPhone,inputLayoutAddress,inputLayoutpass;
    View v3;
    DashBoard act;
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v3 = inflater.inflate(R.layout.profile, container, false);
       
        return v3;
    }
    @Override
    public  void onStart()
    {
        try {
            super.onStart();
            act = (DashBoard) getActivity();
            ArrayList<View> allv = act.disProfile();
            RelativeLayout lv = (RelativeLayout) getActivity().findViewById(R.id.profile);


            ArrayList<String> profdetails=new ArrayList<String>();
            profdetails=act.getProfdetails();

            inputFirstName = (EditText) v3.findViewById(R.id.input_first_name1);
            inputEmail = (EditText) v3.findViewById(R.id.input_email);
            inputLastName = (EditText) v3.findViewById(R.id.input_lastname);
            inputPhone = (EditText) v3.findViewById(R.id.input_phone);
            inputDOB = (TextView) v3.findViewById(R.id.input_dob);
            inputAddress = (EditText) v3.findViewById(R.id.input_address1);
            inputanniversary = (TextView) v3.findViewById(R.id.input_anniversary);
            inputpass=(EditText) v3.findViewById(R.id.input_password);
            Button btnsave = (Button)v3.findViewById(R.id.btn_submit11);
            Button dob1=(Button)v3.findViewById(R.id.dob1);
            Button ann1=(Button)v3.findViewById(R.id.anniv1);
//        Button btncancel = (Button)v3.findViewById(R.id.btn_cancel1);

            inputLayoutFirstName = (TextInputLayout)v3.findViewById(R.id.input_layout_firstname1);

            inputLayoutEmail = (TextInputLayout) v3.findViewById(R.id.input_layout_email);
            inputLayoutLastName = (TextInputLayout) v3.findViewById(R.id.input_layout_lastname);
            inputLayoutPhone = (TextInputLayout) v3.findViewById(R.id.input_layout_phone);
            inputLayoutAddress = (TextInputLayout) v3.findViewById(R.id.input_layout_address1);
            inputLayoutpass = (TextInputLayout) v3.findViewById(R.id.input_layout_pass);
            inputFirstName
                    .addTextChangedListener(new MyTextWatcher(inputFirstName));
          //  inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
            inputLastName.addTextChangedListener(new MyTextWatcher(
                    inputLastName));
            inputPhone.addTextChangedListener(new MyTextWatcher(
                    inputPhone));
            inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));
            inputpass.addTextChangedListener(new MyTextWatcher(
                    inputpass));

            inputFirstName.setText(profdetails.get(0));
            inputEmail.setText(profdetails.get(1));
            inputLastName.setText(profdetails.get(2));
            inputPhone.setText(profdetails.get(3));
            inputDOB.setText(profdetails.get(5));
            inputAddress.setText(profdetails.get(4));
            inputanniversary.setText(profdetails.get(6));
            inputpass.setText(profdetails.get(7));

            btnsave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    submitForm();
                }
            });
            dob1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Log.d("Entered", "");
                  //  onCreateDialog(DATE_DIALOG_ID);
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(),datePickerListener,year,month,day);
                    datePicker.setCancelable(true);
                    datePicker.setTitle("Select the date");
                    datePicker.show();
                }
            });
            ann1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(),datePickerListener1,year,month,day);
                    datePicker.setCancelable(true);
                    datePicker.setTitle("Select the date");
                    datePicker.show();
                }
            });
        }catch (Exception e){
            Log.d("Profile",""+e);
        }
    }



    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            inputDOB.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(" "));
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener1
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            inputanniversary.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(" "));
        }
    };
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

            /*    case R.id.input_email:
                   validateEmail();
                    break;*/

                case R.id.input_phone:
                    validatePhone();
                    break;



            }
        }
    }

    public void submitForm() {



        if (!validatePhone()) {
            return;
        }
        String first_name="",email="",lastname="",phone="",address="",dob="",anniversary="",password="",confirmpass="";
        try {
            first_name = inputFirstName.getText().toString();
            email =inputEmail.getText().toString();
            lastname = inputLastName.getText().toString();
            phone =inputPhone.getText().toString();
            address = inputAddress.getText().toString();
            dob = inputDOB.getText().toString();
            anniversary = inputanniversary.getText().toString();
            password=inputpass.getText().toString();
        //    confirmpass=inputconfirmpass.getText().toString();

            //  Toast.makeText(getApplicationContext(),lastname, Toast.LENGTH_SHORT).show();

        }
        catch(Exception x)
        {
            //Toast.makeText(getActivity(),"Fill in all details",)
        }
        if (inputEmail.getText().toString().trim().isEmpty())
            inputEmail.setText(" ");
        if (inputFirstName.getText().toString().trim().isEmpty())
            inputFirstName.setText(" ");
        if (inputLastName.getText().toString().trim().isEmpty())
            inputLastName.setText(" ");
        if (inputPhone.getText().toString().trim().isEmpty())
            inputPhone.setText(" ");
        if (inputAddress.getText().toString().trim().isEmpty())
            inputAddress.setText(" ");
        if (inputDOB.getText().toString().trim().isEmpty())
            inputDOB.setText(" ");if (inputEmail.getText().toString().trim().isEmpty())
            if (inputanniversary.getText().toString().trim().isEmpty())
                inputanniversary.setText(" ");
        try {
            first_name = inputFirstName.getText().toString();
            email =inputEmail.getText().toString();
            lastname = inputLastName.getText().toString();
            phone =inputPhone.getText().toString();
            address = inputAddress.getText().toString();
            dob = inputDOB.getText().toString();
            anniversary = inputanniversary.getText().toString();
            password=inputpass.getText().toString();

        }
        catch(Exception x)
        {
            Log.d("Name",""+x);
        }

        act.upppp(first_name,email,lastname,phone,address,dob,anniversary,password);
    }




    private boolean validateEmail() {
        try {


            if (inputEmail.getText().toString().trim().isEmpty()) {
                inputLayoutEmail.setErrorEnabled(false);
                return true;
            } else {
                String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
             //   Pattern pattern = Pattern.compile(EMAIL_PATTERN);
             //   Matcher matcher = pattern.matcher(inputEmail.getText().toString());
                Log.e("Match",""+EMAIL_PATTERN.matches(inputEmail.getText().toString()));
                if(EMAIL_PATTERN.matches(inputEmail.getText().toString())) {
                    inputLayoutEmail.setErrorEnabled(false);
                    return true;

                }
                else
                {
                    inputLayoutEmail.setError(getString(R.string.err_msg_email));
                    requestFocus(inputEmail);
                    return  false;
                }

            }
        }catch (Exception e){
            //inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
           getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePhone()
        {

        if (inputPhone.length() < 10) {
            inputLayoutPhone
                    .setError(getString(R.string.err_msg_invalid_mobileno));
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

}
