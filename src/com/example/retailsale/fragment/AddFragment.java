package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
		consumerVisitDateDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_visit_datePicker);
		consumerVisitTimeTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_visit_timePicker);
				
		// save btn
		saveBtn.setOnClickListener(this);
		newBtn.setOnClickListener(this);

		// leave info
		leaveInfoCB.setOnCheckedChangeListener(this);
		// time picker to set 24h
		consumerVisitTimeTP.setIs24HourView(true);
		
	    // set & check option data
//        setOptionType(); login will do this, get data from server
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
			mainActivity.finishActivity();
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

	private void setCustomerData()
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
				+ " sexSelectedPosition: " + sexSelectedPosition
				+ " titleSelectedPosition: " + titleSelectedPosition);
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
		if (!isChecked && !companyPhoneNumber.equals("") && !Utility.isCompanyPhoneValid(phoneNumber))
		{
			showToast(this.getActivity().getResources().getString(R.string.company_phone_field_error));
			return;
		}
		// check email
		if (!isChecked && !Utility.isEmailValid(email))
		{
			showToast(this.getActivity().getResources().getString(R.string.email_field_error));
			return;
		}
		// check birthday
		if (!isChecked && !Utility.isBirthdayValid(customerBirthday))
		{
			showToast(this.getActivity().getResources().getString(R.string.birthday_field_error));
			return;
		}
		
		if (customerInfo == null)
		{
			customerInfo = new CustomerInfo("customerAccount", customerName, cellPhoneNumber,
					phoneNumber, companyPhoneNumber, sexSelectedPosition, titleSelectedPosition,
					email, dateString + timeString, msgSelectedPosition, introducer,
					jobSelectedPosition, ageSelectedPosition, customerBirthday, -1, -1, dateString
							+ timeString);
		}
	}
	
	private void saveData() {
	    setCustomerData();
	    openDatabase();
        if (customerInfo != null) {
            int sendNoteValue = isSendMsg ? 1 : 0;
            String createDateTime = Utility.getCurrentDateTime();
            Log.d(TAG, "sendNoteValue is " + sendNoteValue + " createDateTime is " + createDateTime);
            
            SharedPreferences settings = mainActivity.getSharedPreferences(Utility.LoginField.DATA, 0);
            
            int userSerial = settings.getInt(Utility.LoginField.USER_SERIAL, -1);
            int userGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);
            
            Log.d(TAG, "userSerial === " + userSerial + "userGroup === " + userGroup);
            
            long id = retialSaleDbAdapter.create(customerInfo.getCustometName(), customerInfo.getCustomerHome(),
                    customerInfo.getCustomerMobile(), customerInfo.getCustomerCompany(),
                    customerInfo.getCustomerMail(), customerInfo.getCustomerSex(), customerInfo.getCustomerBirth(),
                    customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(), customerInfo.getCustomerJob(),
                    customerInfo.getCustomerIntroducer(), customerInfo.getCustomerAge(),
                    customerInfo.getCustomerVisitDate(), userSerial, userGroup, createDateTime, sendNoteValue,
                    customerInfo.getReservationWorkAlias(), customerInfo.getReservationStatusComment(),
                    customerInfo.getReservationStatus(), customerInfo.getReservationWork(),
                    customerInfo.getReservationContact(), customerInfo.getReservationComment(),
                    customerInfo.getReservationSpace(), customerInfo.getReservationBudget(),
                    customerInfo.getReservationDate(), RetialSaleDbAdapter.NOTUPLOAD);
            
            Log.d(TAG, "id is " + id);
        } else {
            Log.d(TAG, "customerInfo is null, cannot access data to db.");
        }
	}

	private void startOrderMeasureActivity()
	{
	    setCustomerData();
		if (customerInfo != null)
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
	
	private void openDatabase() {
        if (retialSaleDbAdapter == null) {
            retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
        }

        if (!retialSaleDbAdapter.isDbOpen()) { // not open, then open it
            retialSaleDbAdapter.open();
        }
	}
	
	private void setOptionType() {
	    retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
	    
	    retialSaleDbAdapter.open();
	    
	    // to get option type content
	    Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
	    
        if (optionTypeCursor != null) {
            int count = optionTypeCursor.getCount();

            Log.d(TAG, "option type count is === " + count);

            optionTypeCursor.close();

            if (count == 0) { // if no content then create
                // userType
                retialSaleDbAdapter.create(0, "userType", 1, "設計師兼店長");
                retialSaleDbAdapter.create(0, "userType", 2, "設計師");
                retialSaleDbAdapter.create(0, "userType", 3, "離職員工");

                // customerSex
                retialSaleDbAdapter.create(1, "customerSex", 0, "無");
                retialSaleDbAdapter.create(1, "customerSex", 1, "男");
                retialSaleDbAdapter.create(1, "customerSex", 2, "女");

                // customerTitle
                retialSaleDbAdapter.create(2, "customerTitle", 1, "先生");
                retialSaleDbAdapter.create(2, "customerTitle", 2, "小姐");
                retialSaleDbAdapter.create(2, "customerTitle", 3, "來賓");

                // customerInfo
                retialSaleDbAdapter.create(3, "customerInfo", 0, "無");
                retialSaleDbAdapter.create(3, "customerInfo", 1, "廣告");
                retialSaleDbAdapter.create(3, "customerInfo", 2, "新聞");
                retialSaleDbAdapter.create(3, "customerInfo", 3, "雜誌");
                retialSaleDbAdapter.create(3, "customerInfo", 4, "報紙");

                // customerJob
                retialSaleDbAdapter.create(4, "customerJob", 0, "無");
                retialSaleDbAdapter.create(4, "customerJob", 1, "服務業");
                retialSaleDbAdapter.create(4, "customerJob", 2, "老師");
                retialSaleDbAdapter.create(4, "customerJob", 3, "學生");
                retialSaleDbAdapter.create(4, "customerJob", 4, "程式員");

                // customerAge
                retialSaleDbAdapter.create(5, "customerAge", 0, "無");
                retialSaleDbAdapter.create(5, "customerAge", 1, "20-30歲");
                retialSaleDbAdapter.create(5, "customerAge", 2, "30-40歲");
                retialSaleDbAdapter.create(5, "customerAge", 3, "40-50歲");
                retialSaleDbAdapter.create(5, "customerAge", 4, "50歲以上");

                // serviceType
                retialSaleDbAdapter.create(6, "serviceType", 1, "圖片服務");
                retialSaleDbAdapter.create(6, "serviceType", 2, "新增服務");

                // userGroup
                retialSaleDbAdapter.create(7, "userGroup", 1, "1號店");
                retialSaleDbAdapter.create(7, "userGroup", 2, "2號店");

                // reservationStatus
                retialSaleDbAdapter.create(8, "reservationStatus", 1, "來店客");
                retialSaleDbAdapter.create(8, "reservationStatus", 2, "電話客");

                // reservationBudget
                retialSaleDbAdapter.create(9, "reservationBudget", 0, "無");
                retialSaleDbAdapter.create(9, "reservationBudget", 1, "0-20萬");
                retialSaleDbAdapter.create(9, "reservationBudget", 2, "20-30萬");
                retialSaleDbAdapter.create(9, "reservationBudget", 3, "30-40萬");
                retialSaleDbAdapter.create(9, "reservationBudget", 4, "40萬以上");

                // reservationSpace
                retialSaleDbAdapter.create(10, "reservationSpace", 0, "無");
                retialSaleDbAdapter.create(10, "reservationSpace", 1, "客廳");
                retialSaleDbAdapter.create(10, "reservationSpace", 2, "廚房");
                retialSaleDbAdapter.create(10, "reservationSpace", 3, "玄關");
            } else {
                // all exist, no need to create
            }
        } else {
            Log.d(TAG, "option cursor is null ");
        }
	}

	private void getOptionType() {
	    
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

        
        if (optionTypeCursor != null) {
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
        } else {
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
	}
}
