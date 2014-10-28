package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.example.retailsale.RetialSaleDbAdapter;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.manager.dataoption.GsonDataOptionType;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.util.Utility;

public class AddFragment extends Fragment implements OnClickListener, OnCheckedChangeListener
{
	private static final String TAG = "AddFragment";
	private static final int REQUEST_ORDER_MEASURE = 999;
	public static final String SEND_CUSTOMER_INFO = "send_customer_info";
	public static final String SEND_NOTE_MSG = "send_note_msg";
	private OptionAdapter infoAdapter, jobAdapter, ageAdapter, sexAdapter, titleAdapter;
	private List<GsonDataOptionType> infoList, jobList, ageList, sexList, titleList;
	private boolean isChecked = false;
	private CustomerInfo customerInfo;
	private RetialSaleDbAdapter retialSaleDbAdapter;
	private boolean isSendMsg = false;
	// views
	private MainActivity mainActivity;
	private Spinner infoSpinner, jobSpinner, ageSpinner, sexSpinner, titleSpinner;
	private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET,
			emailET, customerBirthdayET, introducerET;
	private CheckBox leaveInfoCB;
	private TextView companyNameTV, customerIDTV, designerStoreTV, designerNameTV, createDateTV;
	private DatePicker consumerVisitDateDP;
	private TimePicker consumerVisitTimeTP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume()");
		openDatabase();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause()");
		retialSaleDbAdapter.close();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.d(TAG, "onAttach()");
		mainActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.d(TAG, "onCreateView()");
		View view = inflater.inflate(R.layout.add_tab, container, false);
		infoSpinner = (Spinner) view.findViewById(R.id.add_tab_info_from);
		jobSpinner = (Spinner) view.findViewById(R.id.add_tab_job);
		ageSpinner = (Spinner) view.findViewById(R.id.add_tab_age_selection);
		sexSpinner = (Spinner) view.findViewById(R.id.add_tab_sex_selection);
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
		consumerVisitDateDP = (DatePicker) view
				.findViewById(R.id.add_tab_consumer_visit_datePicker);
		consumerVisitTimeTP = (TimePicker) view
				.findViewById(R.id.add_tab_consumer_visit_timePicker);
		designerStoreTV = (TextView) view.findViewById(R.id.add_tab_designer_store);
		// save btn
		saveBtn.setOnClickListener(this);
		newBtn.setOnClickListener(this);
		// leave info
		leaveInfoCB.setOnCheckedChangeListener(this);
		// time picker to set 24h
		consumerVisitTimeTP.setIs24HourView(true);
		// set & check option data
		// setOptionType(); login will do this, get data from server
		retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
		retialSaleDbAdapter.open();
		// get optionType
		getOptionType();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated()");
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
					isSendMsg = data.getBooleanExtra(AddFragment.SEND_NOTE_MSG, isSendMsg);
					Log.d(TAG, "date is " + customerInfo.getReservationDate());
					Log.d(TAG, "time is " + customerInfo.getReservationTime());
					Log.d(TAG, "hour is " + customerInfo.getReservationHour());
					Log.d(TAG, "month is " + customerInfo.getReservationMonth());
					Log.d(TAG, "progress is " + customerInfo.getReservationStatus());
					Log.d(TAG, "isSendMsg is " + isSendMsg);
				}
			}
		}
	}

	private boolean setCustomerData()
	{
		String phoneNumber = phoneNumberET.getText().toString();
		String cellPhoneNumber = cellPhoneNumberET.getText().toString();
		String companyPhoneNumber = companyPhoneNumberET.getText().toString();
		String email = emailET.getText().toString();
		String customerBirthday = customerBirthdayET.getText().toString();
		String dateString = Utility.covertDateToString(consumerVisitDateDP.getYear(),
				consumerVisitDateDP.getMonth() + 1, consumerVisitDateDP.getDayOfMonth());
		String timeString = Utility.covertTimeToString(consumerVisitTimeTP.getCurrentHour(),
				consumerVisitTimeTP.getCurrentMinute()) + ":00";
		String customerName = customerNameET.getText().toString();
		String introducer = introducerET.getText().toString();
		int msgSelectedPosition = infoSpinner.getSelectedItemPosition();
		int jobSelectedPosition = jobSpinner.getSelectedItemPosition();
		int ageSelectedPosition = ageSpinner.getSelectedItemPosition();
		int sexSelectedPosition = sexSpinner.getSelectedItemPosition();
		int titleSelectedPosition = titleSpinner.getSelectedItemPosition();
		Log.d(TAG, "msgSelectedPosition: " + msgSelectedPosition + " jobSelectedPosition: "
				+ jobSelectedPosition + " ageSelectedPosition: " + ageSelectedPosition
				+ " sexSelectedPosition: " + sexSelectedPosition + " titleSelectedPosition: "
				+ titleSelectedPosition);
		Log.d(TAG, "date: " + dateString + "time : " + timeString);
		Log.d(TAG, "customerName : " + customerName + " phoneNumber: " + phoneNumber
				+ " cellPhoneNumber: " + cellPhoneNumber + " companyPhoneNumber: "
				+ companyPhoneNumber + " email: " + email + " birthday: " + customerBirthday
				+ " introducer: " + introducer);
		
		// check phone number
		if (!isChecked && !Utility.isPhoneValid(phoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.home_phone_field_error));
			return;
		}
		// check cellphone number
		if (!isChecked && !Utility.isCellphoneValid(cellPhoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.cell_phone_field_error));
			return;
		}
		// check company phone number
		if (!isChecked && !companyPhoneNumber.equals("")
				&& !Utility.isCompanyPhoneValid(phoneNumber))
		{
			showToast(this.getActivity().getResources()
					.getString(R.string.company_phone_field_error));
			return;
		}
		// check email
		if (!isChecked && !Utility.isEmailValid(email))
		{
			showToast(this.getActivity().getResources().getString(R.string.email_field_error));
			return;
		}
		// check birthday
//		if (!isChecked && !Utility.isBirthdayValid(customerBirthday))
//		{
//			showToast(this.getActivity().getResources().getString(R.string.birthday_field_error));
//			return false;
//		}
		
		if (customerInfo == null)
		{
			if (isChecked)
			{
				String noData = AddFragment.this.getResources().getString(R.string.no_data);
				customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE, noData, noData, noData,
						noData, 0, 0, noData, dateString + timeString, 0, noData, 0, 0, noData,
						Utility.getCreator(getActivity()), Utility.getCreatorGroup(getActivity()),
						dateString + timeString, "", "", "", "", "", 0, 0, "", "", 0, "");
			}
			else
			{
				customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE, customerName,
						cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedPosition,
						titleSelectedPosition, email, dateString + timeString, msgSelectedPosition,
						introducer, jobSelectedPosition, ageSelectedPosition, customerBirthday,
						Utility.getCreator(getActivity()), Utility.getCreatorGroup(getActivity()),
						dateString + timeString, "", "", "", "", "", 0, 0, "", "", 0, "");
			}
		}
		else {
			customerInfo.modifyCustomerInfo(Utility.DEFAULT_VALUE, customerName, cellPhoneNumber,
					phoneNumber, companyPhoneNumber, sexSelectedPosition, titleSelectedPosition,
					email, dateString + timeString, msgSelectedPosition, introducer,
					jobSelectedPosition, ageSelectedPosition, customerBirthday,
					Utility.getCreator(getActivity()), Utility.getCreatorGroup(getActivity()), dateString + timeString);
		}
		
		return true;
	}

	private void saveData()
	{
		if (!setCustomerData())
			return;
		
//		String reservationDate = customerInfo.getReservationDate();
//		
//		if (reservationDate.equals(""))
//		{
//			Log.d(TAG, "reservationDate is empty ");
//			customerInfo.setReservationDate(customerInfo.getCreateTime());
//		}
		
		openDatabase();
		
	      // to check work address had, but contact address not, then contact equals to work?
        String workAddress = customerInfo.getReservationWork();
        String contactAddress = customerInfo.getReservationContact();
        
        Log.d(TAG, "saveData() customerInfo === " + customerInfo.toString());
        
        if (!workAddress.equals("") && contactAddress.equals(""))
        {
            showAlertDialog();
        }
        else
        {
            insertToDB();
        }
	}
	
	private void showAlertDialog()
	{
        new AlertDialog.Builder(AddFragment.this.getActivity())
                .setTitle(AddFragment.this.getResources().getString(R.string.add_tab_check_address_title))
                .setMessage(AddFragment.this.getResources().getString(R.string.add_tab_check_address_message))
                .setPositiveButton(AddFragment.this.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                customerInfo.setReservationContact(customerInfo.getReservationWork());
                                insertToDB();
                            }
                        }).setNegativeButton(AddFragment.this.getResources().getString(R.string.cancel), null).show();
	}
	
	private void insertToDB()
	{
		if (customerInfo != null)
		{
			int sendNoteValue = isSendMsg ? 1 : 0;
			String createDateTime = Utility.getCurrentDateTime();
			Log.d(TAG, "sendNoteValue is " + sendNoteValue + " createDateTime is " + createDateTime);
			SharedPreferences settings = mainActivity.getSharedPreferences(Utility.LoginField.DATA,
					0);
			int userSerial = settings.getInt(Utility.LoginField.USER_SERIAL, -1);
			int userGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);
			Log.d(TAG, "userSerial === " + userSerial + "userGroup === " + userGroup);
			long id = retialSaleDbAdapter.create(customerInfo.getCustometName(),
					customerInfo.getCustomerHome(), customerInfo.getCustomerMobile(),
					customerInfo.getCustomerCompany(), customerInfo.getCustomerMail(),
					customerInfo.getCustomerSex(), customerInfo.getCustomerBirth(),
					customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(),
					customerInfo.getCustomerJob(), customerInfo.getCustomerIntroducer(),
					customerInfo.getCustomerAge(), customerInfo.getCustomerVisitDate(), userSerial,
					userGroup, createDateTime, sendNoteValue,
					customerInfo.getReservationWorkAlias(),
					customerInfo.getReservationStatusComment(),
					customerInfo.getReservationStatus(), customerInfo.getReservationWork(),
					customerInfo.getReservationContact(), customerInfo.getReservationComment(),
					customerInfo.getReservationSpace(), customerInfo.getReservationBudget(),
					customerInfo.getReservationDate(), RetialSaleDbAdapter.NOTUPLOAD);
			Log.d(TAG, "id is " + id);
			
			mainActivity.finishActivity();
		}
		else
		{
			Log.d(TAG, "customerInfo is null, cannot access data to db.");
		}
	}

	private void startOrderMeasureActivity()
	{
		if (setCustomerData())
		{
			Intent orderintent = new Intent(this.getActivity(), OrderMeasure.class);
			orderintent.putExtra(SEND_CUSTOMER_INFO, customerInfo);
			orderintent.putExtra(AddFragment.SEND_NOTE_MSG, isSendMsg);
			startActivityForResult(orderintent, REQUEST_ORDER_MEASURE);
			this.getActivity().overridePendingTransition(R.anim.activity_conversion_in_from_right,
					R.anim.activity_conversion_out_to_left);
		}
	}

	private void enableField(boolean enabled)
	{
		// spinner
		infoSpinner.setEnabled(enabled);
		jobSpinner.setEnabled(enabled);
		ageSpinner.setEnabled(enabled);
		sexSpinner.setEnabled(enabled);
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
		consumerVisitDateDP.setEnabled(enabled);
		// timepicker
		consumerVisitTimeTP.setEnabled(enabled);
	}

	private void showToast(String showString)
	{
		Toast.makeText(this.getActivity(), showString, Toast.LENGTH_SHORT).show();
	}

	private void openDatabase()
	{
		if (retialSaleDbAdapter == null)
		{
			retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
		}
		if (!retialSaleDbAdapter.isDbOpen())
		{ // not open, then open it
			retialSaleDbAdapter.open();
		}
	}

	private void getOptionType()
	{
		infoList = new ArrayList<GsonDataOptionType>();
		jobList = new ArrayList<GsonDataOptionType>();
		ageList = new ArrayList<GsonDataOptionType>();
		sexList = new ArrayList<GsonDataOptionType>();
		titleList = new ArrayList<GsonDataOptionType>();
		// to get option type content
		Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
		int optionType, optionSerial;
		String optionAlias, optionName;
		String sexType = mainActivity.getResources().getString(R.string.option_customer_sex);
		String titleType = mainActivity.getResources().getString(R.string.option_customer_title);
		String infoType = mainActivity.getResources().getString(R.string.option_customer_info);
		String jobType = mainActivity.getResources().getString(R.string.option_customer_job);
		String ageType = mainActivity.getResources().getString(R.string.option_customer_age);
		if (optionTypeCursor != null)
		{
			int count = optionTypeCursor.getCount();
			if (count > 0)
			{
				while (optionTypeCursor.moveToNext())
				{
					optionType = optionTypeCursor.getInt(optionTypeCursor
							.getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_TYPE));
					optionSerial = optionTypeCursor.getInt(optionTypeCursor
							.getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_SERIAL));
					optionAlias = optionTypeCursor.getString(optionTypeCursor
							.getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_ALIAS));
					optionName = optionTypeCursor.getString(optionTypeCursor
							.getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_NAME));
					if (optionAlias.equals(sexType))
					{
						sexList.add(new GsonDataOptionType(optionSerial, optionName));
					}
					else if (optionAlias.equals(titleType))
					{
						titleList.add(new GsonDataOptionType(optionSerial, optionName));
					}
					else if (optionAlias.equals(infoType))
					{
						infoList.add(new GsonDataOptionType(optionSerial, optionName));
					}
					else if (optionAlias.equals(jobType))
					{
						jobList.add(new GsonDataOptionType(optionSerial, optionName));
					}
					else if (optionAlias.equals(ageType))
					{
						ageList.add(new GsonDataOptionType(optionSerial, optionName));
					}
				}
			}
			optionTypeCursor.close();
		}
		else
		{
			Log.d(TAG, "option cursor is null ");
		}
		// msg spinner
		infoAdapter = new OptionAdapter(this.getActivity(), infoList);
		infoSpinner.setAdapter(infoAdapter);
		// job spinner
		jobAdapter = new OptionAdapter(this.getActivity(), jobList);
		jobSpinner.setAdapter(jobAdapter);
		// age spinner
		ageAdapter = new OptionAdapter(this.getActivity(), ageList);
		ageSpinner.setAdapter(ageAdapter);
		// sex spinner
		sexAdapter = new OptionAdapter(this.getActivity(), sexList);
		sexSpinner.setAdapter(sexAdapter);
		// title spinner
		titleAdapter = new OptionAdapter(this.getActivity(), titleList);
		titleSpinner.setAdapter(titleAdapter);
		Cursor userStoreCursor = retialSaleDbAdapter.getOptionByOptionSerial(Utility
				.getCreatorGroup(AddFragment.this.getActivity()));
		
		String userStoreOptionName;
		
		if (userStoreCursor != null)
		{
			int count = userStoreCursor.getCount();
			if (count > 0)
			{
				while (userStoreCursor.moveToNext())
				{
					userStoreOptionName = userStoreCursor.getString(optionTypeCursor
							.getColumnIndex(RetialSaleDbAdapter.KEY_DATA_OPTION_NAME));
					
					designerStoreTV.setText(userStoreOptionName);
				}
			}
			else
			{
				designerStoreTV.setText(AddFragment.this.getActivity().getResources().getString(R.string.no_data));
			}
		}
		else
		{
			designerStoreTV.setText(AddFragment.this.getActivity().getResources().getString(R.string.no_data));
		}
	}
}
