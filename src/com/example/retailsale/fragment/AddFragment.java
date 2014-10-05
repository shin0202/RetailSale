package com.example.retailsale.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.retailsale.MainActivity;
import com.example.retailsale.OrderMeasure;
import com.example.retailsale.R;
import com.example.retailsale.manager.CustomerInfo;
import com.example.retailsale.util.Utility;

public class AddFragment extends Fragment implements OnClickListener, OnCheckedChangeListener
{
	private static final String TAG = "AddFragment";
	private static final int REQUEST_ORDER_MEASURE = 999;
	public static final String SEND_CUSTOMER_INFO = "send_customer_info";
	private ArrayAdapter<String> msgArrayAdapter, jobArrayAdapter, ageArrayAdapter,
			maleArrayAdapter, titleArrayAdapter;
	private String[] msgList, jobList, ageList, maleList, titleList;
	private boolean isChecked = false;
	private CustomerInfo customerInfo;
	// views
	private MainActivity mainActivty;
	private Spinner msgSpinner, jobSpinner, ageSpinner, maleSpinner, titleSpinner;
	private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET,
			emailET, customerBirthdayET, introducerET;
	private CheckBox leaveInfoCB;
	private TextView companyNameTV, customerIDTV, designerStoreTV, designerNameTV, createDateTV;
	private DatePicker consumerComeDateDP;
	private TimePicker consumerComeTimeTP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mainActivty = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.add_tab, container, false);
		msgSpinner = (Spinner) view.findViewById(R.id.add_tab_msg_from);
		jobSpinner = (Spinner) view.findViewById(R.id.add_tab_job);
		ageSpinner = (Spinner) view.findViewById(R.id.add_tab_age_selection);
		maleSpinner = (Spinner) view.findViewById(R.id.add_tab_male_selection);
		titleSpinner = (Spinner) view.findViewById(R.id.add_tab_title_selection);
		Button saveBtn = (Button) view.findViewById(R.id.add_tab_save_btn);
		Button newBtn = (Button) view.findViewById(R.id.add_tab_new_btn);
		customerNameET = (EditText) view.findViewById(R.id.add_tab_edit_customer_name);
		phoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_phone_number);
		cellPhoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_cellphone_number);
		companyPhoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_phone_number_company);
		customerBirthdayET = (EditText) view.findViewById(R.id.add_tab_edit_customer_birthday);
		introducerET = (EditText) view.findViewById(R.id.add_tab_edit_introducer);
		emailET = (EditText) view.findViewById(R.id.add_tab_edit_email);
		leaveInfoCB = (CheckBox) view.findViewById(R.id.add_tab_leave_info_checkbox);
		consumerComeDateDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_come_datePicker);
		consumerComeTimeTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_come_timePicker);
		// save btn
		saveBtn.setOnClickListener(this);
		newBtn.setOnClickListener(this);
		// msg spinner
		Resources res = getResources();
		msgList = res.getStringArray(R.array.msg_provider);
		jobList = res.getStringArray(R.array.job);
		maleList = res.getStringArray(R.array.male);
		ageList = res.getStringArray(R.array.age);
		titleList = res.getStringArray(R.array.title);
		msgArrayAdapter = new ArrayAdapter<String>(AddFragment.this.getActivity(),
				android.R.layout.simple_spinner_item, msgList);
		msgSpinner.setAdapter(msgArrayAdapter);
		// job spinner
		jobArrayAdapter = new ArrayAdapter<String>(AddFragment.this.getActivity(),
				android.R.layout.simple_spinner_item, jobList);
		jobSpinner.setAdapter(jobArrayAdapter);
		// age spinner
		ageArrayAdapter = new ArrayAdapter<String>(AddFragment.this.getActivity(),
				android.R.layout.simple_spinner_item, ageList);
		ageSpinner.setAdapter(ageArrayAdapter);
		// male spinner
		maleArrayAdapter = new ArrayAdapter<String>(AddFragment.this.getActivity(),
				android.R.layout.simple_spinner_item, maleList);
		maleSpinner.setAdapter(maleArrayAdapter);
		// title spinner
		titleArrayAdapter = new ArrayAdapter<String>(AddFragment.this.getActivity(),
				android.R.layout.simple_spinner_item, titleList);
		titleSpinner.setAdapter(titleArrayAdapter);
		// leave info
		leaveInfoCB.setOnCheckedChangeListener(this);
		// time picker to set 24h
		consumerComeTimeTP.setIs24HourView(true);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		// TextView txtResult = (TextView)
		// this.getView().findViewById(R.id.textView1);
		// txtResult.setText(value);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.add_tab_save_btn:
			saveData();
			mainActivty.finishActivity();
			break;
		case R.id.add_tab_new_btn:
			startOrderMeasureActivity();
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if (isChecked)
		{ // user select, didn't leave info
			enableField(false);
		}
		else
		{
			enableField(true);
		}
		this.isChecked = isChecked;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_ORDER_MEASURE)
		{
			if (resultCode == android.app.Activity.RESULT_OK)
			{
				if (data != null)
				{
					customerInfo = data.getParcelableExtra(SEND_CUSTOMER_INFO);
					Log.d(TAG, "date is " + customerInfo.getReservationDate());
					Log.d(TAG, "time is " + customerInfo.getReservationTime());
					Log.d(TAG, "hour is " + customerInfo.getReservationHour());
					Log.d(TAG, "month is " + customerInfo.getReservationMonth());
					Log.d(TAG, "progress is " + customerInfo.getReservationStatus());
					
				}
			}
		}
	}

	private void saveData()
	{
		String phoneNumber = phoneNumberET.getText().toString();
		String cellPhoneNumber = cellPhoneNumberET.getText().toString();
		String companyPhoneNumber = companyPhoneNumberET.getText().toString();
		String email = emailET.getText().toString();
		String customerBirthday = customerBirthdayET.getText().toString();
		String dateString = Utility.covertDateToString(consumerComeDateDP.getYear(),
				consumerComeDateDP.getMonth() + 1, consumerComeDateDP.getDayOfMonth());
		String timeString = Utility.covertTimeToString(consumerComeTimeTP.getCurrentHour(),
				consumerComeTimeTP.getCurrentMinute());
		String customerName = customerNameET.getText().toString();
		String introducer = introducerET.getText().toString();
		int msgSelectedPosition = msgSpinner.getSelectedItemPosition();
		int jobSelectedPosition = jobSpinner.getSelectedItemPosition();
		int ageSelectedPosition = ageSpinner.getSelectedItemPosition();
		int maleSelectedPosition = maleSpinner.getSelectedItemPosition();
		int titleSelectedPosition = titleSpinner.getSelectedItemPosition();
		Log.d(TAG, "msgSelectedPosition: " + msgSelectedPosition + " jobSelectedPosition: "
				+ jobSelectedPosition + " ageSelectedPosition: " + ageSelectedPosition
				+ " maleSelectedPosition: " + maleSelectedPosition
				+ " titleSelectedPosition: " + titleSelectedPosition);
		Log.d(TAG, "date: " + dateString + " time : " + timeString);
		Log.d(TAG, "customerName : " + customerName + " phoneNumber: " + phoneNumber
				+ " cellPhoneNumber: " + cellPhoneNumber + " companyPhoneNumber: "
				+ companyPhoneNumber + " email: " + email + " birthday: " + customerBirthday
				+ " introducer: " + introducer);
		// check phone number
		if (!isChecked && !Utility.isPhoneValid(phoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.field_error));
			return;
		}
		// check cellphone number
		if (!isChecked && !Utility.isCellphoneValid(cellPhoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.field_error));
			return;
		}
		// check company phone number
		if (!isChecked && !companyPhoneNumber.equals("") && !Utility.isPhoneValid(phoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.field_error));
			return;
		}
		// check email
		if (!isChecked && !Utility.isEmailValid(email))
		{
			showToast(this.getActivity().getResources().getString(R.string.field_error));
			return;
		}
		// check birthday
		if (!isChecked && !Utility.isBirthdayValid(customerBirthday))
		{
			showToast(this.getActivity().getResources().getString(R.string.field_error));
			return;
		}
		
		if (customerInfo == null)
		{
			customerInfo = new CustomerInfo("customerAccount", customerName, cellPhoneNumber,
					phoneNumber, companyPhoneNumber, maleSelectedPosition, titleSelectedPosition,
					email, dateString + timeString, msgSelectedPosition, introducer,
					jobSelectedPosition, ageSelectedPosition, customerBirthday, -1, -1, dateString
							+ timeString);
		}
	}

	private void startOrderMeasureActivity()
	{
		saveData();
		if (customerInfo != null)
		{
			Intent orderintent = new Intent(this.getActivity(), OrderMeasure.class);
			orderintent.putExtra(SEND_CUSTOMER_INFO, customerInfo);
			startActivityForResult(orderintent, REQUEST_ORDER_MEASURE);
			this.getActivity().overridePendingTransition(R.anim.activity_conversion_in_from_right,
					R.anim.activity_conversion_out_to_left);
		}
	}

	private void enableField(boolean enabled)
	{
		// spinner
		msgSpinner.setEnabled(enabled);
		jobSpinner.setEnabled(enabled);
		ageSpinner.setEnabled(enabled);
		maleSpinner.setEnabled(enabled);
		titleSpinner.setEnabled(enabled);
		// edittext
		customerNameET.setEnabled(enabled);
		cellPhoneNumberET.setEnabled(enabled);
		phoneNumberET.setEnabled(enabled);
		companyPhoneNumberET.setEnabled(enabled);
		emailET.setEnabled(enabled);
		customerBirthdayET.setEnabled(enabled);
		introducerET.setEnabled(enabled);
		// datepicker
		consumerComeDateDP.setEnabled(enabled);
		// timepicker
		consumerComeTimeTP.setEnabled(enabled);
	}

	private void showToast(String showString)
	{
		Toast.makeText(this.getActivity(), showString, Toast.LENGTH_SHORT).show();
	}
}
