package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
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

import com.example.retailsale.MainFragmentActivity;
import com.example.retailsale.OrderMeasureActivity;
import com.example.retailsale.R;
import com.example.retailsale.adapter.CommonAdapter;
import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.manager.dataoption.DataOption;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.manager.userlist.UserDataForList;
import com.example.retailsale.util.TWZipCode;
import com.example.retailsale.util.Utility;

public class AddFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
    private static final String TAG = "AddFragment";
    private static final int REQUEST_ORDER_MEASURE = 999;
    public static final String SEND_CUSTOMER_INFO = "send_customer_info";
    public static final String SEND_NOTE_MSG = "send_note_msg";
    private boolean isNotLeaveChecked = false;
    private CustomerInfo customerInfo;
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private boolean isSendMsg = false;
    private String createDateTime;
    private List<DataOption> infoList, jobList, ageList, sexList, titleList;
    private List<DataOption> statusList, spaceList, budgetList, repairItemList, areaList;
    private List<UserDataForList> userDataList;
    private List<Boolean> spaceStateList;
//    private List<String> phoneCodeList, contactCountyList, contactCityList, workCountyList, workCityList;
    private List<String> phoneCodeList, workCountyList, workCityList;
//    private int contactCountyPosition = 0, contactCityPosition = 0;
    private String spaceSelectedContent = "";
    // views
    private MainFragmentActivity mainActivity;
//    private Spinner infoSpinner, jobSpinner, ageSpinner, sexSpinner, titleSpinner, designerSpinner;
    private Spinner infoSpinner, jobSpinner, ageSpinner, titleSpinner, designerSpinner;
    private Spinner yearSpinner, monthSpinner, daySpinner;
    private Spinner phoneNumberSpinner, companyPhoneNumberSpinner;
    private Spinner repairItemSpinner, areaSpinner;
//    private Spinner spaceSpinner, statusSpinner;
    private Spinner statusSpinner;
//    private Spinner contactCountySpinner, contactCitySpinner, workCountySpinner, workCitySpinner;
    private Spinner workCountySpinner, workCitySpinner;
    private Spinner budgetSpinner;
//    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
//            introducerET, memoET, contactET, workET;
    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
    introducerET, memoET, workET, caseNameET, cantDescriptionET;
//    private CheckBox leaveInfoCB, asAboveCB;
    private CheckBox leaveInfoCB;
    @SuppressWarnings("unused")
    private TextView companyNameTV, designerStoreTV, createDateTV;
    private DatePicker consumerVisitDateDP, reservationDP;
    private TimePicker consumerVisitTimeTP, reservationTP;
    private Dialog errorDialog = null;

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
        mainActivity = (MainFragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);
        infoSpinner = (Spinner) view.findViewById(R.id.add_tab_info_from);
        jobSpinner = (Spinner) view.findViewById(R.id.add_tab_job);
        ageSpinner = (Spinner) view.findViewById(R.id.add_tab_age_selection);
//        sexSpinner = (Spinner) view.findViewById(R.id.add_tab_sex_selection);
        titleSpinner = (Spinner) view.findViewById(R.id.add_tab_title_selection);
        budgetSpinner = (Spinner) view.findViewById(R.id.add_tab_budget);
//        spaceSpinner = (Spinner) view.findViewById(R.id.add_tab_space);
        statusSpinner = (Spinner) view.findViewById(R.id.add_tab_sale_status);
        Button saveBtn = (Button) view.findViewById(R.id.add_tab_save_btn);
//        Button newBtn = (Button) view.findViewById(R.id.add_tab_new_btn);
        customerNameET = (EditText) view.findViewById(R.id.add_tab_edit_customer_name);
        phoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_phone_number);
        cellPhoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_cellphone_number);
        companyPhoneNumberET = (EditText) view.findViewById(R.id.add_tab_edit_phone_number_company);
        introducerET = (EditText) view.findViewById(R.id.add_tab_edit_introducer);
        emailET = (EditText) view.findViewById(R.id.add_tab_edit_email);
        memoET = (EditText) view.findViewById(R.id.add_tab_edit_customer_memo);
//        contactET = (EditText) view.findViewById(R.id.add_tab_edit_contact_address);
        workET = (EditText) view.findViewById(R.id.add_tab_edit_work_address);
        caseNameET = (EditText) view.findViewById(R.id.add_tab_edit_case_name);
        cantDescriptionET = (EditText) view.findViewById(R.id.add_tab_edit_cant_description);
        leaveInfoCB = (CheckBox) view.findViewById(R.id.add_tab_leave_info_checkbox);
//        asAboveCB = (CheckBox) view.findViewById(R.id.add_tab_as_above_checkbox);
        consumerVisitDateDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_visit_datePicker);
        consumerVisitTimeTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_visit_timePicker);
        reservationDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_reservation_datePicker);
        reservationTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_reservation_timePicker);
        designerStoreTV = (TextView) view.findViewById(R.id.add_tab_designer_store);
        designerSpinner = (Spinner) view.findViewById(R.id.add_tab_user);
        createDateTV = (TextView) view.findViewById(R.id.add_tab_create_date);
        
        yearSpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_year);
        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_month);
        monthSpinner.setOnItemSelectedListener(this);
        daySpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_day);
        daySpinner.setOnItemSelectedListener(this);
        
        phoneNumberSpinner = (Spinner) view.findViewById(R.id.add_tab_phone_number_spinner);
        companyPhoneNumberSpinner = (Spinner) view.findViewById(R.id.add_tab_company_phone_number_spinner);
        
        repairItemSpinner = (Spinner) view.findViewById(R.id.add_tab_repair_item);
        areaSpinner = (Spinner) view.findViewById(R.id.add_tab_area);
        
//        contactCountySpinner = (Spinner) view.findViewById(R.id.add_tab_contact_address_county);
//        contactCountySpinner.setOnItemSelectedListener(this);
//        contactCitySpinner = (Spinner) view.findViewById(R.id.add_tab_contact_address_city);
//        contactCitySpinner.setOnItemSelectedListener(this);
//        contactCitySpinner.setEnabled(false);
        
        workCountySpinner = (Spinner) view.findViewById(R.id.add_tab_work_address_county);
        workCountySpinner.setOnItemSelectedListener(this);
        workCitySpinner = (Spinner) view.findViewById(R.id.add_tab_work_address_city);
        workCitySpinner.setEnabled(false);
        
        String[] phoneCodeArray = getResources().getStringArray(R.array.phone_code);
        phoneCodeList = Arrays.asList(phoneCodeArray);
        
        CommonAdapter phoneCodeAdapter = new CommonAdapter(this.getActivity(), phoneCodeList);
        
        phoneNumberSpinner.setAdapter(phoneCodeAdapter);
        companyPhoneNumberSpinner.setAdapter(phoneCodeAdapter);
        
        // save btn
        saveBtn.setOnClickListener(this);
//        newBtn.setOnClickListener(this);
        // leave info
        leaveInfoCB.setOnCheckedChangeListener(this);
        // time picker to set 24h
        consumerVisitTimeTP.setIs24HourView(true);
        reservationTP.setIs24HourView(true);
        
        // as above
//        asAboveCB.setOnCheckedChangeListener(this);
        
        // create date
        createDateTime = Utility.getCurrentDateTime();
        createDateTV.setText(createDateTime.replace("T", ""));
        
        // space button
        Button spaceButton = (Button) view.findViewById(R.id.add_tab_space);
        spaceButton.setOnClickListener(this);

        retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
        retialSaleDbAdapter.open();
        
        // get optionType
        getOptionType();
        
        // set contact county list
        setContactCountyList();
        
        // set customer birthday spinner(year, month, day)
        setCustomerBirthdayYearSpinner();
        
        // get designer list
        getUserList();
        
        consumerVisitDateDP.setEnabled(false);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.add_tab_save_btn:
            saveData();
            break;
//        case R.id.add_tab_new_btn:
//            startOrderMeasureActivity();
//            break;
        case R.id.error_dialog_btn:
            if (errorDialog != null)
            {
                errorDialog.dismiss();
            }
            break;
        case R.id.add_tab_space:
            Log.d(TAG, "To select space");
            setMultiDialog(spaceList);
            break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
        case R.id.add_tab_leave_info_checkbox:
            if (isChecked)
            { // user select, didn't leave info
                enableField(false);
            }
            else
            {
                enableField(true);
            }
            this.isNotLeaveChecked = isChecked;
            break;
//        case R.id.add_tab_as_above_checkbox:
//            
//            Log.d(TAG, "contactCountyPosition === " + contactCountyPosition);
//            
//            if (isChecked)
//            { // work address same as contact address
//                workCountySpinner.setSelection(contactCountyPosition);
//                handleCountyEvent(contactCountyPosition, false, true); // to set work city position in this
//
//                workCountySpinner.setEnabled(false);
//
//                workET.setText(contactET.getText().toString());
//                workET.setEnabled(false);
//            }
//            else
//            {
//                workCountySpinner.setEnabled(true);
//                workCountySpinner.setSelection(0);
//                workCitySpinner.setAdapter(null);
//                workCitySpinner.setEnabled(false);
//                workET.setEnabled(true);
//            }
//            break;
        }
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
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
        case R.id.add_tab_customer_birthday_year:
            Log.d(TAG, "To select year, the position is " + position);
            if (position == Utility.DEFAULT_ZERO_VALUE)
            {
                monthSpinner.setAdapter(null);
                daySpinner.setAdapter(null);
            }
            else
            {
                setCustomerBirthdayMonthSpinner();
            }
            break;
        case R.id.add_tab_customer_birthday_month:
            Log.d(TAG, "To select month, the position is " + position);
            if (yearSpinner.getSelectedItemPosition() != Utility.DEFAULT_ZERO_VALUE)
            {
                int days = Utility.getDays((String)yearSpinner.getSelectedItem() + "-" + (String)monthSpinner.getSelectedItem());
                Log.d(TAG, "days : " + days);
                setCustomerBirthdayDaySpinner(days);
            }
            break;
//        case R.id.add_tab_contact_address_county:
//            Log.d(TAG, "To select contact county, the position is " + position);
//            contactCountyPosition = position;
//            handleCountyEvent(position, true, false);
//            break;
        case R.id.add_tab_work_address_county:
//            if (asAboveCB.isChecked())
//                return;
            Log.d(TAG, "To select work county, the position is " + position);
            handleCountyEvent(position, false, false);
         
            break;
//        case R.id.add_tab_contact_address_city:
//            Log.d(TAG, "To select contact city, the position is " + position);
//            contactCityPosition = position;
//            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private boolean setCustomerData()
    {
        String phoneCode = phoneCodeList.get(phoneNumberSpinner.getSelectedItemPosition());
        String companyPhoneCode = phoneCodeList.get(companyPhoneNumberSpinner.getSelectedItemPosition());
        Log.d(TAG, "phone number : " + phoneCode + " company : " + companyPhoneCode);
        
        String phoneNumber = phoneCode + Utility.FILL_DASH + phoneNumberET.getText().toString();
        String cellPhoneNumber = cellPhoneNumberET.getText().toString();
        String companyPhoneNumber = companyPhoneCode + Utility.FILL_DASH + companyPhoneNumberET.getText().toString();
        String email = emailET.getText().toString();
        StringBuilder customerBirthday = new StringBuilder();
        String dateString = Utility.covertDateToString(consumerVisitDateDP.getYear(),
                consumerVisitDateDP.getMonth() + 1, consumerVisitDateDP.getDayOfMonth());
        String timeString = Utility.covertTimeToString(consumerVisitTimeTP.getCurrentHour(),
                consumerVisitTimeTP.getCurrentMinute())
                + ":00";
        
        String reservationDateString = Utility.covertDateToString(reservationDP.getYear(),
                reservationDP.getMonth() + 1, reservationDP.getDayOfMonth());
        String reservationTimeString = Utility.covertTimeToString(reservationTP.getCurrentHour(),
                reservationTP.getCurrentMinute()) + ":00";
        String customerName = customerNameET.getText().toString();
        String introducer = introducerET.getText().toString();
        String memo = memoET.getText().toString();
        
        String reservationWorkAlias = caseNameET.getText().toString();
        String reservationStatusComment = cantDescriptionET.getText().toString();
        
        int msgSelectedSerial = infoList.get(infoSpinner.getSelectedItemPosition()).getOptSerial();
        int jobSelectedSerial = jobList.get(jobSpinner.getSelectedItemPosition()).getOptSerial();
        int ageSelectedSerial = ageList.get(ageSpinner.getSelectedItemPosition()).getOptSerial();
//        int sexSelectedSerial = sexList.get(sexSpinner.getSelectedItemPosition()).getOptSerial();
        int sexSelectedSerial = 0;
        int titleSelectedSerial = titleList.get(titleSpinner.getSelectedItemPosition()).getOptSerial();
        int repairSelectedSerial = repairItemList.get(repairItemSpinner.getSelectedItemPosition()).getOptSerial();
        int areaSelectedSerial = areaList.get(areaSpinner.getSelectedItemPosition()).getOptSerial();
        int budgetSelectedSerial = budgetList.get(budgetSpinner.getSelectedItemPosition()).getOptSerial();
//        int spaceSelectedSerial = spaceList.get(spaceSpinner.getSelectedItemPosition()).getOptSerial();
        int statusSelectedSerial = statusList.get(statusSpinner.getSelectedItemPosition()).getOptSerial();
        
        int yearSelectedPosition = yearSpinner.getSelectedItemPosition();
        
        if (yearSelectedPosition == Utility.DEFAULT_ZERO_VALUE)
        {
            customerBirthday.append(getResources().getString(R.string.no_data));
        }
        else
        {
            customerBirthday.append(yearSpinner.getSelectedItem()).append("-")
                    .append(monthSpinner.getSelectedItem()).append("-")
                    .append(daySpinner.getSelectedItem());
        }

        Log.d(TAG, "msgSelectedSerial: " + msgSelectedSerial + " jobSelectedSerial: "
                + jobSelectedSerial + " ageSelectedSerial: " + ageSelectedSerial
                + " sexSelectedSerial: " + sexSelectedSerial + " titleSelectedSerial: "
                + titleSelectedSerial + " repairSelectedSerial: " + repairSelectedSerial
                + " areaSelectedSerial : " + areaSelectedSerial + " budgetSelectedSerial : "
                + budgetSelectedSerial + " statusSelectedSerial : " + statusSelectedSerial);

        Log.d(TAG, "date: " + dateString + " time : " + timeString + " reservationDate : "
                + reservationDateString + " reservationTimeString : " + reservationTimeString);

        Log.d(TAG, "customerName : " + customerName + " phoneNumber: " + phoneNumber
                + " cellPhoneNumber: " + cellPhoneNumber + " companyPhoneNumber: "
                + companyPhoneNumber + " email: " + email + " birthday: " + customerBirthday
                + " introducer: " + introducer + " reservationWorkAlias : " + reservationWorkAlias
                + " reservationStatusComment : " + reservationStatusComment);
        
        // handle contact address
//        int contactCountyPosition = contactCountySpinner.getSelectedItemPosition();
        int workCountyPosition = workCountySpinner.getSelectedItemPosition();
        String contactAddress = "", workAddress = "";
        String contactZipCode = "", workZipCode = "";
        String contactCountyCity = "", workCountyCity = "";
        TWZipCode tZip = new TWZipCode();
        
//        if (contactCountyPosition == 0) // default, no data
//        {
//            contactAddress = contactCountyList.get(contactCountyPosition);
//        }
//        else
//        {
//            tZip.setCountry(contactCountyList.get(contactCountyPosition));
//            tZip.setTown(contactCityList.get(contactCitySpinner.getSelectedItemPosition()));
//            
//            contactCountyCity = contactCountyList.get(contactCountyPosition)
//                    + contactCityList.get(contactCitySpinner.getSelectedItemPosition());
//            contactAddress = contactCountyCity + contactET.getText();
//        }
        
        contactZipCode = tZip.getZipCode();
        
//        Log.d(TAG, "contact address county : " + contactCountyPosition + " countyCity : " + contactCountyCity
//                + " contactAddress : " + contactAddress + " zip code : " + contactZipCode);
        
        if (workCountyPosition == 0) // default, no data
        {
            workAddress = workCountyList.get(workCountyPosition);
        }
        else
        {
            tZip.setCountry(workCountyList.get(workCountyPosition));
            tZip.setTown(workCityList.get(workCitySpinner.getSelectedItemPosition()));
            
            workCountyCity = workCountyList.get(workCountyPosition)
                    + workCityList.get(workCitySpinner.getSelectedItemPosition());
//            workAddress = workCountyCity + contactET.getText();
            workAddress = workCountyCity + workET.getText();
        }
        
        workZipCode = tZip.getZipCode();
        
        Log.d(TAG, "work address county : " + workCountyPosition + " countyCity : " + workCountyCity
                + " workAddress : " + workAddress + " zip code : " + workZipCode);
        
        // handler space string
        Log.d(TAG, "space selected content === " + spaceSelectedContent);
        
        // check phone and cellphone number, only need to select one item to write
        
        if (isNotLeaveChecked)
        {
            phoneNumber = Utility.SPACE_STRING;
            cellPhoneNumber = Utility.SPACE_STRING;
        }
        else
        {
            // let phone number is space string when get space string from phoneNumberET
            if (phoneNumberET.getText().toString().equals(Utility.SPACE_STRING))
            {
                phoneNumber = Utility.SPACE_STRING;
            }
            
            Log.d(TAG, " ----- phoneNumber: " + phoneNumber + " cellPhoneNumber: "
                    + cellPhoneNumber);
            
            boolean isPhoneValid = Utility.isPhoneValid(phoneNumber);
            boolean isCellPhoneValid = Utility.isCellphoneValid(cellPhoneNumber);
            
            if ((!isPhoneValid && !isCellPhoneValid) || (!phoneNumber.equals(Utility.SPACE_STRING) && !isPhoneValid)
                    || (!cellPhoneNumber.equals(Utility.SPACE_STRING) && !isCellPhoneValid))
            {
                showToast(this.getActivity().getResources().getString(R.string.home_phone_or_cellphone_field_error));
                return false;
            }
        }
        
        
        // check company phone number
//        if (!isNotLeaveChecked && !companyPhoneNumber.equals(Utility.SPACE_STRING) && !Utility.isCompanyPhoneValid(companyPhoneNumber))
//        {
//            showToast(this.getActivity().getResources().getString(R.string.company_phone_field_error));
//            return false;
//        }
        if (companyPhoneNumberET.getText().toString().equals(Utility.SPACE_STRING))
        {
            companyPhoneNumber = Utility.SPACE_STRING;
        }
        
        // check email
        if (!isNotLeaveChecked && !email.equals(Utility.SPACE_STRING) && !Utility.isEmailValid(email))
        {
            showToast(this.getActivity().getResources().getString(R.string.email_field_error));
            return false;
        }
        // check birthday
//		if (!isChecked && !Utility.isBirthdayValid(customerBirthday))
//		{
//			showToast(this.getActivity().getResources().getString(R.string.birthday_field_error));
//			return false;
//		}
        
        // to check user data list (designer)
        if (userDataList == null)
        {
            showErrorDialog();
            return false;
        }
        else
        {
            if (userDataList.size() <= 0)
            {
                showErrorDialog();
                return false;
            }
        }
        
        // the creator is not from share preference but from creator list
        int creator = 0, group = 0;
        
        if (designerSpinner.getSelectedItemPosition() >= userDataList.size())
        {
            Log.d(TAG, "creator index error ");
        }
        else
        {
            creator = userDataList.get(designerSpinner.getSelectedItemPosition()).getUserSerial();
            group = userDataList.get(designerSpinner.getSelectedItemPosition()).getUserGroup();
        }
        
        Log.d(TAG, "creator: " + creator + " group: " + group);

        try
        {
            if (customerInfo == null)
            {
                if (isNotLeaveChecked)
                {
                    String noData = AddFragment.this.getResources().getString(R.string.no_data);
                    customerInfo = new CustomerInfo(Utility.DEFAULT_ROW_ID, Utility.DEFAULT_VALUE_STRING, noData, noData,
                            noData, noData, sexSelectedSerial, titleSelectedSerial, noData,
                            dateString + timeString, msgSelectedSerial, noData, jobSelectedSerial,
                            ageSelectedSerial, memo, noData, creator, group, createDateTime,
                            reservationDateString + reservationTimeString, reservationTimeString, workAddress, workZipCode,
                            reservationWorkAlias, contactAddress, contactZipCode,
                            spaceSelectedContent, statusSelectedSerial, Utility.SPACE_STRING,
                            reservationStatusComment, budgetSelectedSerial, repairSelectedSerial,
                            areaSelectedSerial);
                }
                else
                {
                    customerInfo = new CustomerInfo(Utility.DEFAULT_ROW_ID, Utility.DEFAULT_VALUE_STRING, customerName,
                            cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedSerial,
                            titleSelectedSerial, email, dateString + timeString, msgSelectedSerial,
                            introducer, jobSelectedSerial, ageSelectedSerial, memo,
                            customerBirthday.toString(), creator, group, createDateTime,
                            reservationDateString + reservationTimeString, reservationTimeString, workAddress, workZipCode,
                            reservationWorkAlias, contactAddress, contactZipCode,
                            spaceSelectedContent, statusSelectedSerial, Utility.SPACE_STRING,
                            reservationStatusComment, budgetSelectedSerial, repairSelectedSerial,
                            areaSelectedSerial);
                }
            }
            else
            {
                customerInfo.modifyCustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName,
                        cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedSerial,
                        titleSelectedSerial, email, dateString + timeString, msgSelectedSerial,
                        introducer, jobSelectedSerial, ageSelectedSerial, memo,
                        customerBirthday.toString(), creator, group, dateString + timeString,
                        repairSelectedSerial, areaSelectedSerial, budgetSelectedSerial, contactAddress, contactZipCode, workAddress, workZipCode);
                
                Log.d(TAG, "ReservationContact === " + customerInfo.getReservationContact());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void saveData()
    {
        if (!setCustomerData()) return;

        openDatabase();

        // to check work address had, but contact address not, then contact equals to work?
        String workAddress = customerInfo.getReservationWork();
        String contactAddress = customerInfo.getReservationContact();

        Log.d(TAG, "saveData() customerInfo === " + customerInfo.toString());

//        if (!workAddress.equals(Utility.SPACE_STRING) && contactAddress.equals(Utility.SPACE_STRING))
//        {
//            showAlertDialog();
//        }
//        else
//        {
            insertToDB();
//        }
    }

    private void showAlertDialog()
    {
        new AlertDialog.Builder(AddFragment.this.getActivity())
                .setTitle(AddFragment.this.getResources().getString(R.string.add_tab_check_address_title))
                .setMessage(AddFragment.this.getResources().getString(R.string.add_tab_check_address_message))
                .setPositiveButton(AddFragment.this.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
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
//            String createDateTime = Utility.getCurrentDateTime();
            Log.d(TAG, "sendNoteValue is " + sendNoteValue + " createDateTime is " + createDateTime);
//            SharedPreferences settings = mainActivity.getSharedPreferences(Utility.LoginField.DATA, 0);
//            int userSerial = settings.getInt(Utility.LoginField.USER_SERIAL, -1);
//            int userGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);
            Log.d(TAG, "userSerial === " + customerInfo.getCreator() + "userGroup === " + customerInfo.getCreatorGroup());
            long id = retialSaleDbAdapter.create(customerInfo.getCustomerName(), customerInfo.getCustomerHome(),
                    customerInfo.getCustomerMobile(), customerInfo.getCustomerCompany(),
                    customerInfo.getCustomerMail(), customerInfo.getCustomerSex(), customerInfo.getCustomerBirth(),
                    customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(), customerInfo.getCustomerJob(),
                    customerInfo.getCustomerIntroducer(), customerInfo.getCustomerAge(),
                    customerInfo.getCustomerMemo(), customerInfo.getCustomerVisitDate(), customerInfo.getCreator(),
                    customerInfo.getCreatorGroup(), Utility.covertDateStringToServer(createDateTime), sendNoteValue,
                    customerInfo.getReservationWorkAlias(), customerInfo.getReservationStatusComment(),
                    customerInfo.getReservationStatus(), customerInfo.getReservationWork(),
                    customerInfo.getWorkPostcode(), customerInfo.getReservationContact(),
                    customerInfo.getContactPostcode(), customerInfo.getReservationSpace(),
                    customerInfo.getReservationBudget(), customerInfo.getReservationDate(),
                    customerInfo.getReservationRepairItem(), customerInfo.getReservationArea(),
                    RetialSaleDbAdapter.NOTUPLOAD);
            Log.d(TAG, "id is " + id);

            mainActivity.setManageTab();
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
            Intent orderintent = new Intent(this.getActivity(), OrderMeasureActivity.class);
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
//        sexSpinner.setEnabled(enabled);
        titleSpinner.setEnabled(enabled);
        // edittext
        customerNameET.setEnabled(enabled);
        cellPhoneNumberET.setEnabled(enabled);
        phoneNumberET.setEnabled(enabled);
        companyPhoneNumberET.setEnabled(enabled);
        emailET.setEnabled(enabled);

        yearSpinner.setEnabled(enabled);
        monthSpinner.setEnabled(enabled);
        daySpinner.setEnabled(enabled);
        
        repairItemSpinner.setEnabled(enabled);
        areaSpinner.setEnabled(enabled);
        
        introducerET.setEnabled(enabled);
        // datepicker
        consumerVisitDateDP.setEnabled(false);
        // timepicker
        consumerVisitTimeTP.setEnabled(enabled);
        
        reservationDP.setEnabled(enabled);
        reservationTP.setEnabled(enabled);
        
        if (!enabled)
        {
            infoSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            jobSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            ageSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
//            sexSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            titleSpinner.setSelection(2);
            repairItemSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            areaSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);

            customerNameET.setText(this.getResources().getString(R.string.no_data));
            cellPhoneNumberET.setText(this.getResources().getString(R.string.no_data));
            phoneNumberET.setText(this.getResources().getString(R.string.no_data));
            companyPhoneNumberET.setText(this.getResources().getString(R.string.no_data));
            emailET.setText(this.getResources().getString(R.string.no_data));
            
            yearSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            monthSpinner.setAdapter(null);
            daySpinner.setAdapter(null);
            
            introducerET.setText(this.getResources().getString(R.string.no_data));
            
        }
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
        infoList = new ArrayList<DataOption>();
        jobList = new ArrayList<DataOption>();
        ageList = new ArrayList<DataOption>();
        sexList = new ArrayList<DataOption>();
        titleList = new ArrayList<DataOption>();
        statusList = new ArrayList<DataOption>();
        spaceList = new ArrayList<DataOption>();
        budgetList = new ArrayList<DataOption>();
        repairItemList = new ArrayList<DataOption>();
        areaList = new ArrayList<DataOption>();
        spaceStateList = new ArrayList<Boolean>();
        // to get option type content
        Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
        @SuppressWarnings("unused")
        int optionType, optionSerial;
        String optionAlias, optionName;
        String sexType = mainActivity.getResources().getString(R.string.option_customer_sex);
        String titleType = mainActivity.getResources().getString(R.string.option_customer_title);
        String infoType = mainActivity.getResources().getString(R.string.option_customer_info);
        String jobType = mainActivity.getResources().getString(R.string.option_customer_job);
        String ageType = mainActivity.getResources().getString(R.string.option_customer_age);
        String statusType = mainActivity.getResources().getString(R.string.option_customer_status);
        String spaceType = mainActivity.getResources().getString(R.string.option_customer_space);
        String budgetType = mainActivity.getResources().getString(R.string.option_customer_budget);
        String repairItemType = mainActivity.getResources().getString(R.string.option_customer_repair_item);
        String areaType = mainActivity.getResources().getString(R.string.option_customer_area);
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
                        sexList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(titleType))
                    {
                        titleList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(infoType))
                    {
                        infoList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(jobType))
                    {
                        jobList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(ageType))
                    {
                        ageList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(statusType))
                    {
                        statusList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(spaceType))
                    {
                        spaceList.add(new DataOption("", optionSerial, optionName, false));
                        spaceStateList.add(false);
                    }
                    else if (optionAlias.equals(budgetType))
                    {
                        budgetList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(repairItemType))
                    {
                        repairItemList.add(new DataOption("", optionSerial, optionName, false));
                    }
                    else if (optionAlias.equals(areaType))
                    {
                        areaList.add(new DataOption("", optionSerial, optionName, false));
                    }
                }
            }
            optionTypeCursor.close();
        }
        else
        {
            Log.d(TAG, "option cursor is null ");
        }
        
        OptionAdapter infoAdapter, jobAdapter, ageAdapter, titleAdapter, repairItemAdapter, areaAdapter, budgetAdapter, spaceAdapter, statusAdapter;
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
//        sexAdapter = new OptionAdapter(this.getActivity(), sexList);
//        sexSpinner.setAdapter(sexAdapter);
        // title spinner
        titleAdapter = new OptionAdapter(this.getActivity(), titleList);
        titleSpinner.setAdapter(titleAdapter);
        
//        titleSpinner.setEnabled(false);
        titleSpinner.setSelection(2); // to set default title
        // repair spinner
        repairItemAdapter = new OptionAdapter(this.getActivity(), repairItemList);
        repairItemSpinner.setAdapter(repairItemAdapter);
        // area spinner
        areaAdapter = new OptionAdapter(this.getActivity(), areaList);
        areaSpinner.setAdapter(areaAdapter);
        // budget spinner
        budgetAdapter = new OptionAdapter(this.getActivity(), budgetList);
        budgetSpinner.setAdapter(budgetAdapter);
        // space spinner
        spaceAdapter = new OptionAdapter(this.getActivity(), spaceList);
//        spaceSpinner.setAdapter(spaceAdapter);
        // status spinner
        statusAdapter = new OptionAdapter(this.getActivity(), statusList);
        statusSpinner.setAdapter(statusAdapter);
        
        Cursor userStoreCursor = retialSaleDbAdapter.getOptionByOptionSerial(Utility.getAppCreatorGroup(AddFragment.this
                .getActivity()));

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
    
    private void getUserList()
    {
        userDataList = new ArrayList<UserDataForList>();
        
        int userSerial, userGroup, userType;
        String userName, userGroupNm, userTypeNm;

        // to get user content
//        Cursor userCursor = retialSaleDbAdapter.getAllUser();
        Cursor userCursor = retialSaleDbAdapter.getUserByCreator(Utility.getAppCreatorGroup(mainActivity));
//        Cursor userCursor = retialSaleDbAdapter.getUserByCreator(Utility.getCreatorGroup(mainActivity));

        if (userCursor != null)
        {
            int count = userCursor.getCount();
            if (count > 0)
            {
                while (userCursor.moveToNext())
                {
                    userSerial = userCursor.getInt(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_SERIAL));
                    userName = userCursor.getString(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_NAME));
                    userGroup = userCursor.getInt(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_GROUP));
                    userType = userCursor.getInt(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_TYPE));
                    userGroupNm = userCursor.getString(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_GROUP_NAMING));
                    userTypeNm = userCursor.getString(userCursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_USER_TYPE_NAMING));
                    userDataList.add(new UserDataForList(userSerial, userName, userGroup, userType, userGroupNm, userTypeNm));
                }
            }
            userCursor.close();
        }
        else
        {
            Log.d(TAG, "user cursor is null ");
        }
        
        if (userDataList.size() <= 0)
        {
            Log.d(TAG, "user data occur error or data were incorrect! ");
            showErrorDialog();
            return;
        }
        
        // user spinner
        UserAdapter userAdapter = new UserAdapter(this.getActivity(), userDataList);
        designerSpinner.setAdapter(userAdapter);
    }
    
    private void setContactCountyList()
    {
        String[] countyArray = getResources().getStringArray(R.array.county_city);
//        contactCountyList = Arrays.asList(countyArray);
//        
//        CommonAdapter contactCountyAdapter = new CommonAdapter(this.getActivity(), contactCountyList);
//        
//        contactCountySpinner.setAdapter(contactCountyAdapter);
        
        workCountyList = Arrays.asList(countyArray);
        
        CommonAdapter workCountyAdapter = new CommonAdapter(this.getActivity(), workCountyList);
        
        workCountySpinner.setAdapter(workCountyAdapter);
    }
    
    private void setContactCityList(String[] cityArray)
    {
//        contactCityList = Arrays.asList(cityArray);
//        
//        CommonAdapter cityAdapter = new CommonAdapter(this.getActivity(), contactCityList);
//        
//        contactCitySpinner.setAdapter(cityAdapter);
    }
    
    private void setWorkCityList(String[] cityArray, boolean isAbove)
    {
        workCityList = Arrays.asList(cityArray);
        
        CommonAdapter cityAdapter = new CommonAdapter(this.getActivity(), workCityList);
        
        workCitySpinner.setAdapter(cityAdapter);
        
//        if (isAbove)
//        {
//            Log.d(TAG, "contactCityPosition === " + contactCityPosition);
//            workCitySpinner.setEnabled(false);
//            workCitySpinner.setSelection(contactCityPosition);
//        }
    }
    
    private void handleCountyEvent(int position, boolean isContact, boolean isAbove)
    {
//        if (isContact)
//            contactCitySpinner.setEnabled(true);
//        else
            workCitySpinner.setEnabled(true);
        switch (position)
        {
        case Utility.County.NONE:
            if (isContact)
            {
//                contactCitySpinner.setEnabled(false);
//                contactCitySpinner.setAdapter(null);
            }
            else
            {
                workCitySpinner.setAdapter(null);
                workCitySpinner.setEnabled(false);
            }
            break;
        case Utility.County.KEELUNG_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.Keelung));
            else
                setWorkCityList(getResources().getStringArray(R.array.Keelung), isAbove);
            break;
        case Utility.County.TAIPEI_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.taipei));
            else
                setWorkCityList(getResources().getStringArray(R.array.taipei), isAbove);
            break;
        case Utility.County.NEW_TAIPEI_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.new_taipei));
            else
                setWorkCityList(getResources().getStringArray(R.array.new_taipei), isAbove);
            break;
        case Utility.County.YILAN_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.yilan));
            else
                setWorkCityList(getResources().getStringArray(R.array.yilan), isAbove);
            break;
        case Utility.County.HSINCHU_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.hsinchu_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.hsinchu_city), isAbove);
            break;
        case Utility.County.HSINCHU_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.hsinchu_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.hsinchu_county), isAbove);
            break;
        case Utility.County.TAOYUAN_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.taoyuan_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.taoyuan_city), isAbove);
            break;
        case Utility.County.MIAOLI_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.miaoli_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.miaoli_city), isAbove);
            break;
        case Utility.County.TAICHUNG_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.taichung_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.taichung_city), isAbove);
            break;
        case Utility.County.CHANGHUA_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.changhua_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.changhua_city), isAbove);
            break;
        case Utility.County.NANTOU_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.nantou_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.nantou_city), isAbove);
            break;
        case Utility.County.CHIAYI_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.chiayi_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.chiayi_city), isAbove);
            break;
        case Utility.County.CHIAYI_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.chiayi_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.chiayi_county), isAbove);
            break;
        case Utility.County.YUNLIN_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.yunlin_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.yunlin_county), isAbove);
            break;
        case Utility.County.TAINAN_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.tainan_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.tainan_city), isAbove);
            break;
        case Utility.County.KAOHSIUNG_CITY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.kaohsiung_city));
            else
                setWorkCityList(getResources().getStringArray(R.array.kaohsiung_city), isAbove);
            break;
        case Utility.County.PINGDONG_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.pingtung_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.pingtung_county), isAbove);
            break;
        case Utility.County.TAIDONG_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.taitung_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.taitung_county), isAbove);
            break;
        case Utility.County.HUALIAN_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.hualien_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.hualien_county), isAbove);
            break;
        case Utility.County.KINMEN_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.kinmen_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.kinmen_county), isAbove);
            break;
        case Utility.County.LIANJIANG_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.lianjiang_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.lianjiang_county), isAbove);
            break;
        case Utility.County.PENGHU_COUNTY:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.penghu_county));
            else
                setWorkCityList(getResources().getStringArray(R.array.penghu_county), isAbove);
            break;
        case Utility.County.SOUTH_SEA_ISLANDS:
            if (isContact)
                setContactCityList(getResources().getStringArray(R.array.south_sea_islands));
            else
                setWorkCityList(getResources().getStringArray(R.array.south_sea_islands), isAbove);
            break;
        }
    }
    
    private void setCustomerBirthdayYearSpinner()
    {
        int currentYear = Utility.getCurrentYear();
        int startYear = currentYear - 150;
        Log.d(TAG, "current year : " + currentYear + " start year : " + startYear);
        
        List<String> yearList = new ArrayList<String>();
        
        yearList.add(Utility.SPACE_STRING);
        
        for (int i = startYear; i < currentYear; i++)
        {
            yearList.add(String.valueOf(i));
        }
        
        CommonAdapter yearAdapter = new CommonAdapter(this.getActivity(), yearList);
        
        yearSpinner.setAdapter(yearAdapter);
    }
    
    private void setCustomerBirthdayMonthSpinner()
    {
        List<String> monthList = new ArrayList<String>();
        
        for (int i = 1; i < 13; i++)
        {
            if (i < 10)
            {
                monthList.add("0" + i);
            }
            else
            {
                monthList.add(String.valueOf(i));
            }
        }
        
        CommonAdapter monthAdapter = new CommonAdapter(this.getActivity(), monthList);
        
        monthSpinner.setAdapter(monthAdapter);
    }
    
    private void setCustomerBirthdayDaySpinner(int days)
    {
        List<String> dayList = new ArrayList<String>();
        
        for (int i = 1; i < days + 1; i++)
        {
            if (i < 10)
            {
                dayList.add("0" + i);
            }
            else
            {
                dayList.add(String.valueOf(i));
            }
        }
        
        CommonAdapter dayAdapter = new CommonAdapter(this.getActivity(), dayList);
        
        daySpinner.setAdapter(dayAdapter);
    }
    
    private void showErrorDialog()
    {
        if (errorDialog == null)
        {
            errorDialog = new Dialog(getActivity());
            errorDialog.setContentView(R.layout.dialog_error);
        }
        else
        {
            if (errorDialog.isShowing())
            {
                Log.d(TAG, "The error dialog is showing! ");
                return;
            }
            else
            {
                Log.d(TAG, "The error dialog is not showing! ");
            }
        }
        
        Button loginBtn = (Button) errorDialog.findViewById(R.id.error_dialog_btn);
        loginBtn.setOnClickListener(this);
        
        TextView errorMsg = (TextView) errorDialog.findViewById(R.id.error_dialog_message);
        errorMsg.setText(getString(R.string.designer_not_enough_error));

        errorDialog.setTitle(getResources().getString(R.string.field_error));
        errorDialog.show();
    }
    
    private int getOptionSelectedPosition(List<DataOption> dataList, int optSerial)
    {
        int position = 0;
        
        for (int i = 0; i < dataList.size(); i++)
        {
            if (dataList.get(i).getOptSerial() == optSerial){
                position = i;
                return position;
            }
        }
        
        return position;
    }
    
    private void setMultiDialog(final List<DataOption> items)
    {
        final String[] itemsArray = new String[items.size()];
        final boolean[] initStates = toPrimitiveArray(spaceStateList);
        
        

        for (int i = 0; i < items.size(); i++)
        {
            itemsArray[i] = items.get(i).getOptName();
        }

        AlertDialog dialog = new AlertDialog.Builder(AddFragment.this.getActivity())
                .setTitle(getString(R.string.add_tab_select_repair_item)).setIcon(android.R.drawable.ic_dialog_alert)
                .setMultiChoiceItems(itemsArray, initStates, new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {

                    }
                }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.d(TAG, "Positive Button : " + which);
                        String temp = "";
                        String tempname = "";
                        for (int i = 0; i < initStates.length; i++)
                        {
                            if (initStates[i])
                            {
                                temp += items.get(i).getOptSerial() + ",";
                                tempname += items.get(i).getOptName() + " ";
                                spaceStateList.set(i, true);
                            }
                            else
                            {
                                spaceStateList.set(i, false);
                            }
                        }

                        Toast.makeText(AddFragment.this.getActivity(),
                                getString(R.string.add_tab_show_select_item) + temp, Toast.LENGTH_SHORT).show();
                        spaceSelectedContent = temp;
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.d(TAG, "Negative Button : " + which);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    
    private boolean[] toPrimitiveArray(final List<Boolean> booleanList)
    {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList)
        {
            primitives[index++] = object;
        }
        return primitives;
    }
    
    private class UserAdapter extends BaseAdapter
    {
        private static final int BASE_INDEX = 1000;
        private List<UserDataForList> userList;
        private Context context;
        private ViewTag viewTag;

        public UserAdapter(Context context, List<UserDataForList> userList)
        {
            this.context = context;
            this.userList = userList;
        }

        @Override
        public int getCount()
        {
            return userList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.cell_of_option, null);
                viewTag = new ViewTag((TextView) convertView.findViewById(R.id.option_text));
                convertView.setTag(viewTag);
            }
            else
            {
                viewTag = (ViewTag) convertView.getTag();
            }

            convertView.setId(BASE_INDEX + position);

            if (position < userList.size())
            {
                viewTag.itemName.setText(userList.get(position).getUserName());
            }

            return convertView;
        }

        class ViewTag
        {
            TextView itemName;

            public ViewTag(TextView itemName)
            {
                this.itemName = itemName;
            }
        }
    }
}
