package com.example.retailsale;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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

import com.example.retailsale.adapter.CommonAdapter;
import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.fragment.NeedUploadListFragment;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.manager.dataoption.DataOption;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.manager.userlist.UserDataForList;
import com.example.retailsale.util.TWZipCode;
import com.example.retailsale.util.Utility;

public class EditCustomerActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
    private static final String TAG = "EditCustomerActivity";
    public static final String SEND_CUSTOMER_INFO = "send_customer_info";
    public static final String SEND_NOTE_MSG = "send_note_msg";
    private boolean isNotLeaveChecked = false;
    private CustomerInfo customerInfo;
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private boolean isSendMsg = false; // it is not used in screen
    private String createDateTime;
    private List<DataOption> infoList, jobList, ageList, sexList, titleList;
    private List<DataOption> statusList, spaceList, budgetList, repairItemList, areaList;
    private List<UserDataForList> userDataList;
    private List<Boolean> spaceStateList;
//    private List<String> phoneCodeList, contactCountyList, contactCityList, workCountyList, workCityList;
    private List<String> phoneCodeList, workCountyList, workCityList;
//    private int contactCountyPosition = 0, contactCityPosition = 0;
    private String spaceSelectedContent = "";
    
    private List<String> yearList, monthList, dayList;
    
    private boolean hasBundle = false;
    
    // views
//    private Spinner infoSpinner, jobSpinner, ageSpinner, sexSpinner, titleSpinner, designerSpinner;
    private Spinner infoSpinner, jobSpinner, ageSpinner, titleSpinner, designerSpinner;
    private Spinner yearSpinner, monthSpinner, daySpinner;
    private Spinner phoneNumberSpinner, companyPhoneNumberSpinner;
    private Spinner repairItemSpinner, areaSpinner;
    private Spinner statusSpinner;
//    private Spinner contactCountySpinner, contactCitySpinner, workCountySpinner, workCitySpinner;
    private Spinner workCountySpinner, workCitySpinner; // county : 縣 city : 市,鄉鎮,區
    private Spinner budgetSpinner;
//    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
//            introducerET, memoET, contactET, workET;
    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
    introducerET, memoET, workET, caseNameET, cantDescriptionET;
//    private CheckBox leaveInfoCB, asAboveCB;
    private CheckBox leaveInfoCB;
    @SuppressWarnings("unused")
    private TextView companyNameTV, designerStoreTV, createDateTV;
    private DatePicker consumerVisitDateDP, reservationDP; // visit date cannot selected and modified
    private TimePicker consumerVisitTimeTP, reservationTP;
    private Dialog errorDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_add_customer);
        
        Log.d(TAG, "onCreate()");

        findViews();
        
        getBundle();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
        openDatabase();
        
        // get optionType
        getOptionType();
        
        // set contact county list
        setContactCountyList();
        
        // set customer birthday spinner(year, month, day)
        setCustomerBirthdayYearSpinner();
        
        // get designer list
        getUserList();
        
        consumerVisitDateDP.setEnabled(false);
        
        initData();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
        closeDatabase();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.add_tab_save_btn:
            saveData();
            break;
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
        case R.id.add_tab_back_btn:
            Log.d(TAG, "Back to list");
            EditCustomerActivity.this.finish();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
        case R.id.add_tab_customer_birthday_year:
            if (hasBundle)
            {
                return;
            }
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
            if (hasBundle)
            {
                return;
            }
            Log.d(TAG, "To select month, the position is " + position);
            if (yearSpinner.getSelectedItemPosition() != Utility.DEFAULT_ZERO_VALUE)
            {
                int days = Utility.getDays((String)yearSpinner.getSelectedItem() + "-" + (String)monthSpinner.getSelectedItem());
                Log.d(TAG, "days : " + days);
                setCustomerBirthdayDaySpinner(days);
            }
            break;
        case R.id.add_tab_work_address_county:
            if (hasBundle)
            {
                hasBundle = false;
                return;
            }
            Log.d(TAG, "To select work county, the position is " + position);
            handleCountyEvent(position, false, false);
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
    
    private void findViews()
    {
        infoSpinner = (Spinner) findViewById(R.id.add_tab_info_from);
        jobSpinner = (Spinner) findViewById(R.id.add_tab_job);
        ageSpinner = (Spinner) findViewById(R.id.add_tab_age_selection);
//        sexSpinner = (Spinner) view.findViewById(R.id.add_tab_sex_selection);
        titleSpinner = (Spinner) findViewById(R.id.add_tab_title_selection);
        budgetSpinner = (Spinner) findViewById(R.id.add_tab_budget);
        Button spaceButton = (Button) findViewById(R.id.add_tab_space);
        spaceButton.setOnClickListener(this);
        statusSpinner = (Spinner) findViewById(R.id.add_tab_sale_status);
        Button saveBtn = (Button) findViewById(R.id.add_tab_save_btn);
        Button backBtn = (Button) findViewById(R.id.add_tab_back_btn);
//        Button newBtn = (Button) view.findViewById(R.id.add_tab_new_btn);
        customerNameET = (EditText) findViewById(R.id.add_tab_edit_customer_name);
        phoneNumberET = (EditText) findViewById(R.id.add_tab_edit_phone_number);
        cellPhoneNumberET = (EditText) findViewById(R.id.add_tab_edit_cellphone_number);
        companyPhoneNumberET = (EditText) findViewById(R.id.add_tab_edit_phone_number_company);
        introducerET = (EditText) findViewById(R.id.add_tab_edit_introducer);
        emailET = (EditText) findViewById(R.id.add_tab_edit_email);
        memoET = (EditText) findViewById(R.id.add_tab_edit_customer_memo);
//        contactET = (EditText) view.findViewById(R.id.add_tab_edit_contact_address);
        workET = (EditText) findViewById(R.id.add_tab_edit_work_address);
        caseNameET = (EditText) findViewById(R.id.add_tab_edit_case_name);
        cantDescriptionET = (EditText) findViewById(R.id.add_tab_edit_cant_description);
        leaveInfoCB = (CheckBox) findViewById(R.id.add_tab_leave_info_checkbox);
//        asAboveCB = (CheckBox) view.findViewById(R.id.add_tab_as_above_checkbox);
        consumerVisitDateDP = (DatePicker) findViewById(R.id.add_tab_consumer_visit_datePicker);
        consumerVisitTimeTP = (TimePicker) findViewById(R.id.add_tab_consumer_visit_timePicker);
        reservationDP = (DatePicker) findViewById(R.id.add_tab_consumer_reservation_datePicker);
        reservationTP = (TimePicker) findViewById(R.id.add_tab_consumer_reservation_timePicker);
        designerStoreTV = (TextView) findViewById(R.id.add_tab_designer_store);
        designerSpinner = (Spinner) findViewById(R.id.add_tab_user);
        createDateTV = (TextView) findViewById(R.id.add_tab_create_date);
        
        yearSpinner = (Spinner) findViewById(R.id.add_tab_customer_birthday_year);
        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner = (Spinner) findViewById(R.id.add_tab_customer_birthday_month);
        monthSpinner.setOnItemSelectedListener(this);
        daySpinner = (Spinner) findViewById(R.id.add_tab_customer_birthday_day);
        daySpinner.setOnItemSelectedListener(this);
        
        phoneNumberSpinner = (Spinner) findViewById(R.id.add_tab_phone_number_spinner);
        companyPhoneNumberSpinner = (Spinner) findViewById(R.id.add_tab_company_phone_number_spinner);
        
        repairItemSpinner = (Spinner) findViewById(R.id.add_tab_repair_item);
        areaSpinner = (Spinner) findViewById(R.id.add_tab_area);
        
//        contactCountySpinner = (Spinner) view.findViewById(R.id.add_tab_contact_address_county);
//        contactCountySpinner.setOnItemSelectedListener(this);
//        contactCitySpinner = (Spinner) view.findViewById(R.id.add_tab_contact_address_city);
//        contactCitySpinner.setOnItemSelectedListener(this);
//        contactCitySpinner.setEnabled(false);
        
        workCountySpinner = (Spinner) findViewById(R.id.add_tab_work_address_county);
        workCountySpinner.setOnItemSelectedListener(this);
        workCitySpinner = (Spinner) findViewById(R.id.add_tab_work_address_city);
        workCitySpinner.setEnabled(false);
        
        String[] phoneCodeArray = getResources().getStringArray(R.array.phone_code);
        phoneCodeList = Arrays.asList(phoneCodeArray);
        
        CommonAdapter phoneCodeAdapter = new CommonAdapter(EditCustomerActivity.this, phoneCodeList);
        
        phoneNumberSpinner.setAdapter(phoneCodeAdapter);
        companyPhoneNumberSpinner.setAdapter(phoneCodeAdapter);
        
        // save btn
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
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
    }
    
    private void getBundle()
    {
        Intent intent = EditCustomerActivity.this.getIntent();
        
        if (intent != null)
        {
            customerInfo = intent.getParcelableExtra(NeedUploadListFragment.SEND_CUSTOMER_INFO);
            
            Log.d(TAG, "get customer info from bundle  === " + customerInfo);
            hasBundle = true;
        }
    }

    private boolean checkCustomerData()
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
                showToast(getResources().getString(R.string.home_phone_or_cellphone_field_error));
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
            showToast(getResources().getString(R.string.email_field_error));
            return false;
        }
        // check birthday
//      if (!isChecked && !Utility.isBirthdayValid(customerBirthday))
//      {
//          showToast(this.getActivity().getResources().getString(R.string.birthday_field_error));
//          return false;
//      }
        
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
                    String noData = getResources().getString(R.string.no_data);
                    customerInfo = new CustomerInfo(Utility.DEFAULT_ROW_ID, Utility.DEFAULT_VALUE_STRING, noData,
                            noData, noData, noData, sexSelectedSerial, titleSelectedSerial, noData, dateString
                                    + timeString, msgSelectedSerial, noData, jobSelectedSerial, ageSelectedSerial,
                            memo, noData, creator, group, createDateTime,
                            reservationDateString + reservationTimeString, reservationTimeString, workAddress,
                            workZipCode, reservationWorkAlias, contactAddress, contactZipCode, spaceSelectedContent,
                            statusSelectedSerial, Utility.SPACE_STRING, reservationStatusComment, budgetSelectedSerial,
                            repairSelectedSerial, areaSelectedSerial);
                }
                else
                {
                    customerInfo = new CustomerInfo(Utility.DEFAULT_ROW_ID, Utility.DEFAULT_VALUE_STRING, customerName,
                            cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedSerial, titleSelectedSerial,
                            email, dateString + timeString, msgSelectedSerial, introducer, jobSelectedSerial,
                            ageSelectedSerial, memo, customerBirthday.toString(), creator, group, createDateTime,
                            reservationDateString + reservationTimeString, reservationTimeString, workAddress,
                            workZipCode, reservationWorkAlias, contactAddress, contactZipCode, spaceSelectedContent,
                            statusSelectedSerial, Utility.SPACE_STRING, reservationStatusComment, budgetSelectedSerial,
                            repairSelectedSerial, areaSelectedSerial);
                }
            }
            else
            { // had row id and we edit it
                customerInfo.setCustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName, cellPhoneNumber, phoneNumber,
                        companyPhoneNumber, sexSelectedSerial, titleSelectedSerial, email, dateString + timeString,
                        msgSelectedSerial, introducer, jobSelectedSerial, ageSelectedSerial, memo,
                        customerBirthday.toString(), creator, group, createDateTime, reservationDateString
                                + reservationTimeString, reservationTimeString, workAddress, workZipCode,
                        reservationWorkAlias, contactAddress, contactZipCode, spaceSelectedContent,
                        statusSelectedSerial, Utility.SPACE_STRING, reservationStatusComment, budgetSelectedSerial,
                        repairSelectedSerial, areaSelectedSerial);
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
        if (!checkCustomerData()) return;

        openDatabase();

        Log.d(TAG, "saveData() customerInfo === " + customerInfo.toString());

        if (customerInfo.getRowId() == Utility.DEFAULT_ROW_ID) // add
        {
            insertToDB();
        }
        else // edit
        {
            updateToDB();
        }
        
        EditCustomerActivity.this.finish();
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
        }
        else
        {
            Log.d(TAG, "customerInfo is null, cannot save data to db.");
        }
        
        closeDatabase();
    }
    
    private void updateToDB()
    {
        if (customerInfo != null)
        {
            int sendNoteValue = isSendMsg ? 1 : 0;
            Log.d(TAG, "sendNoteValue is " + sendNoteValue + " createDateTime is " + createDateTime);

            Log.d(TAG, "userSerial === " + customerInfo.getCreator() + "userGroup === " + customerInfo.getCreatorGroup());
            
            retialSaleDbAdapter.updateCustomer(customerInfo.getRowId(), customerInfo.getCustomerName(),
                    customerInfo.getCustomerHome(), customerInfo.getCustomerMobile(),
                    customerInfo.getCustomerCompany(), customerInfo.getCustomerMail(), customerInfo.getCustomerSex(),
                    customerInfo.getCustomerBirth(), customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(),
                    customerInfo.getCustomerJob(), customerInfo.getCustomerIntroducer(), customerInfo.getCustomerAge(),
                    customerInfo.getCustomerMemo(), customerInfo.getCustomerVisitDate(), customerInfo.getCreator(),
                    customerInfo.getCreatorGroup(), Utility.covertDateStringToServer(createDateTime), sendNoteValue,
                    customerInfo.getReservationWorkAlias(), customerInfo.getReservationStatusComment(),
                    customerInfo.getReservationStatus(), customerInfo.getReservationWork(),
                    customerInfo.getWorkPostcode(), customerInfo.getReservationContact(),
                    customerInfo.getContactPostcode(), customerInfo.getReservationSpace(),
                    customerInfo.getReservationBudget(), customerInfo.getReservationDate(),
                    customerInfo.getReservationRepairItem(), customerInfo.getReservationArea(),
                    RetialSaleDbAdapter.NOTUPLOAD);
        }
        else
        {
            Log.d(TAG, "customerInfo is null, cannot update data to db.");
        }
        
        closeDatabase();
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
        Toast.makeText(EditCustomerActivity.this, showString, Toast.LENGTH_SHORT).show();
    }

    private void openDatabase()
    {
        if (retialSaleDbAdapter == null)
        {
            retialSaleDbAdapter = new RetialSaleDbAdapter(EditCustomerActivity.this);
        }
        if (!retialSaleDbAdapter.isDbOpen())
        { // not open, then open it
            retialSaleDbAdapter.open();
        }
    }
    
    private void closeDatabase()
    {
        if (retialSaleDbAdapter != null)
        {
            retialSaleDbAdapter.close();
            retialSaleDbAdapter = null;
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
        String sexType = getResources().getString(R.string.option_customer_sex);
        String titleType = getResources().getString(R.string.option_customer_title);
        String infoType = getResources().getString(R.string.option_customer_info);
        String jobType = getResources().getString(R.string.option_customer_job);
        String ageType = getResources().getString(R.string.option_customer_age);
        String statusType = getResources().getString(R.string.option_customer_status);
        String spaceType = getResources().getString(R.string.option_customer_space);
        String budgetType = getResources().getString(R.string.option_customer_budget);
        String repairItemType = getResources().getString(R.string.option_customer_repair_item);
        String areaType = getResources().getString(R.string.option_customer_area);
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
        infoAdapter = new OptionAdapter(EditCustomerActivity.this, infoList);
        infoSpinner.setAdapter(infoAdapter);
        // job spinner
        jobAdapter = new OptionAdapter(EditCustomerActivity.this, jobList);
        jobSpinner.setAdapter(jobAdapter);
        // age spinner
        ageAdapter = new OptionAdapter(EditCustomerActivity.this, ageList);
        ageSpinner.setAdapter(ageAdapter);
        // sex spinner
//        sexAdapter = new OptionAdapter(this.getActivity(), sexList);
//        sexSpinner.setAdapter(sexAdapter);
        // title spinner
        titleAdapter = new OptionAdapter(EditCustomerActivity.this, titleList);
        titleSpinner.setAdapter(titleAdapter);
        
//        titleSpinner.setEnabled(false);
        titleSpinner.setSelection(2); // to set default title
        // repair spinner
        repairItemAdapter = new OptionAdapter(EditCustomerActivity.this, repairItemList);
        repairItemSpinner.setAdapter(repairItemAdapter);
        // area spinner
        areaAdapter = new OptionAdapter(EditCustomerActivity.this, areaList);
        areaSpinner.setAdapter(areaAdapter);
        // budget spinner
        budgetAdapter = new OptionAdapter(EditCustomerActivity.this, budgetList);
        budgetSpinner.setAdapter(budgetAdapter);
        // space spinner
        spaceAdapter = new OptionAdapter(EditCustomerActivity.this, spaceList);
//        spaceSpinner.setAdapter(spaceAdapter);
        // status spinner
        statusAdapter = new OptionAdapter(EditCustomerActivity.this, statusList);
        statusSpinner.setAdapter(statusAdapter);
        
        Cursor userStoreCursor = retialSaleDbAdapter.getOptionByOptionSerial(Utility
                .getAppCreatorGroup(EditCustomerActivity.this));

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
                designerStoreTV.setText(getResources().getString(R.string.no_data));
            }
        }
        else
        {
            designerStoreTV.setText(getResources().getString(R.string.no_data));
        }
    }
    
    private void getUserList()
    {
        userDataList = new ArrayList<UserDataForList>();
        
        int userSerial, userGroup, userType;
        String userName, userGroupNm, userTypeNm;

        // to get user content
//        Cursor userCursor = retialSaleDbAdapter.getAllUser();
        Cursor userCursor = retialSaleDbAdapter.getUserByCreator(Utility.getAppCreatorGroup(EditCustomerActivity.this));
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
        UserAdapter userAdapter = new UserAdapter(EditCustomerActivity.this, userDataList);
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
        
        CommonAdapter workCountyAdapter = new CommonAdapter(EditCustomerActivity.this, workCountyList);
        
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
        
        CommonAdapter cityAdapter = new CommonAdapter(EditCustomerActivity.this, workCityList);
        
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
        
        yearList = new ArrayList<String>();
        
        yearList.add(Utility.SPACE_STRING);
        
        for (int i = startYear; i < currentYear; i++)
        {
            yearList.add(String.valueOf(i));
        }
        
        CommonAdapter yearAdapter = new CommonAdapter(EditCustomerActivity.this, yearList);
        
        yearSpinner.setAdapter(yearAdapter);
    }
    
    private void setCustomerBirthdayMonthSpinner()
    {
        monthList = new ArrayList<String>();
        
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
        
        CommonAdapter monthAdapter = new CommonAdapter(EditCustomerActivity.this, monthList);
        
        monthSpinner.setAdapter(monthAdapter);
    }
    
    private void setCustomerBirthdayDaySpinner(int days)
    {
        dayList = new ArrayList<String>();
        
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
        
        CommonAdapter dayAdapter = new CommonAdapter(EditCustomerActivity.this, dayList);
        
        daySpinner.setAdapter(dayAdapter);
    }
    
    private void showErrorDialog()
    {
        if (errorDialog == null)
        {
            errorDialog = new Dialog(EditCustomerActivity.this);
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
    
    private void initSpaceData()
    {
        if (customerInfo.getReservationSpace() == null || "".equals(customerInfo.getReservationSpace()))
        {
            return;
        }
        String[] spaceArray = Utility.splitStringToArray(customerInfo.getReservationSpace(), ",");
        
        for (int i = 0; i < spaceList.size(); i++)
        {          
            for (int j = 0; j < spaceArray.length; j++)
            {
                if (spaceList.get(i).getOptSerial() == Integer.valueOf(spaceArray[j]))
                {
                    spaceStateList.set(i, true);
                }
            }
        }
    }
    
    private void setMultiDialog(final List<DataOption> items)
    {
        final String[] itemsArray = new String[items.size()];
        final boolean[] initStates = Utility.convertListToArray(spaceStateList);
        
        

        for (int i = 0; i < items.size(); i++)
        {
            itemsArray[i] = items.get(i).getOptName();
        }

        AlertDialog dialog = new AlertDialog.Builder(EditCustomerActivity.this)
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

                        Toast.makeText(EditCustomerActivity.this,
                                getString(R.string.add_tab_show_select_item) + tempname, Toast.LENGTH_SHORT).show();
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
    
    // if get data from bundle, then set it
    private void initData()
    {
        Log.d(TAG, "customerInfo === " + customerInfo);
        if (customerInfo != null)
        {
            // Left
            // set customer name
            customerNameET.setText(customerInfo.getCustomerName());
            
            // set home number
            if (getResources().getString(R.string.no_data).equals(customerInfo.getCustomerHome())
                    || "".equals(customerInfo.getCustomerHome())) // no data
            {
                phoneNumberSpinner.setSelection(0);
                phoneNumberET.setText("");
            }
            else
            {
                String[] homeNumberArray = Utility.splitStringToArray(customerInfo.getCustomerHome(), Utility.FILL_DASH);
                phoneNumberSpinner.setSelection(Utility.getPositionFromListByKeyword(phoneCodeList, homeNumberArray[0]));
                phoneNumberET.setText(homeNumberArray[1]);
            }
            
            // set cell phone number
            if (getResources().getString(R.string.no_data).equals(customerInfo.getCustomerMobile())
                    || "".equals(customerInfo.getCustomerMobile())) // no data
            {
                cellPhoneNumberET.setText("");
            }
            else
            {
                cellPhoneNumberET.setText(customerInfo.getCustomerMobile());
            }
            
            // set company number
            if (getResources().getString(R.string.no_data).equals(customerInfo.getCustomerCompany())
                    || "".equals(customerInfo.getCustomerCompany())) // no data
            {
                companyPhoneNumberSpinner.setSelection(0);
                companyPhoneNumberET.setText(customerInfo.getCustomerHome());
            }
            else
            {
                String[] companyNumberArray = Utility.splitStringToArray(customerInfo.getCustomerCompany(), Utility.FILL_DASH);
                companyPhoneNumberSpinner.setSelection(Utility.getPositionFromListByKeyword(phoneCodeList, companyNumberArray[0]));
                companyPhoneNumberET.setText(companyNumberArray[1]);
            }
            
            // set message info
            infoSpinner.setSelection(Utility.getPositionFromListByOptSerial(infoList, customerInfo.getCustomerInfo()));
            
            // set job
            jobSpinner.setSelection(Utility.getPositionFromListByOptSerial(jobList, customerInfo.getCustomerJob()));
            
            // set birthday
            if (getResources().getString(R.string.no_data).equals(customerInfo.getCustomerBirth())
                    || "".equals(customerInfo.getCustomerBirth())) // no data
            {
                
            }
            else
            {
                String[] birthdayArray = Utility.splitStringToArray(customerInfo.getCustomerBirth(), Utility.FILL_DASH);
                yearSpinner.setSelection(Utility.getPositionFromListByKeyword(yearList, birthdayArray[0]));
                setCustomerBirthdayMonthSpinner();
                monthSpinner.setSelection(Utility.getPositionFromListByKeyword(monthList, birthdayArray[1]));
                int days = Utility.getDays((String)yearSpinner.getSelectedItem() + "-" + (String)monthSpinner.getSelectedItem());
                setCustomerBirthdayDaySpinner(days);
                daySpinner.setSelection(Utility.getPositionFromListByKeyword(dayList, birthdayArray[2]));
            }
            
            // set introducer
            introducerET.setText(customerInfo.getCustomerIntroducer());
            
            // set work alias
            caseNameET.setText(customerInfo.getReservationWorkAlias());
            
            // set work city country
            if (customerInfo.getWorkPostcode() == null || "".equals(customerInfo.getWorkPostcode()))
            {
                workCountySpinner.setSelection(0);
                workET.setText("");
            }
            else
            {
                String[] address = Utility.splitAddressToArray(customerInfo.getWorkPostcode(), customerInfo.getReservationWork());
                
                int countyPosition = Utility.getPositionFromListByKeyword(workCountyList, address[0]);
                
                workCountySpinner.setSelection(countyPosition);
                
                handleCountyEvent(countyPosition, false, false);
                
                workCitySpinner.setSelection(Utility.getPositionFromListByKeyword(workCityList, address[1]));
                
                // set address
                workET.setText(address[2]);
            }
            
            // set visit date
            Integer[] visitDate = Utility.parseDateTime(customerInfo.getCustomerVisitDate());
            consumerVisitDateDP.updateDate(visitDate[0], visitDate[1] - 1, visitDate[2]);
            consumerVisitTimeTP.setCurrentHour(visitDate[3]);
            consumerVisitTimeTP.setCurrentMinute(visitDate[4]);
            consumerVisitTimeTP.setEnabled(false);
            
            // set reservation date
            Integer[] reservationDate = Utility.parseDateTime(customerInfo.getReservationDate());
            reservationDP.updateDate(reservationDate[0], reservationDate[1] - 1, reservationDate[2]);
            reservationTP.setCurrentHour(reservationDate[3]);
            reservationTP.setCurrentMinute(reservationDate[4]);
            
            // Right
            // set designer store, no need, because get data by creator
            
            // set designer name, no need, because get data by creator
            
            // set title
            titleSpinner.setSelection(Utility.getPositionFromListByOptSerial(titleList, customerInfo.getCustomerTitle()));
            
            // set mail
            if (getResources().getString(R.string.no_data).equals(customerInfo.getCustomerMail())
                    || "".equals(customerInfo.getCustomerMail())) // no data
            {
                emailET.setText("");
            }
            else
            {
                emailET.setText(customerInfo.getCustomerMail());
            }
            
            // set age
            ageSpinner.setSelection(Utility.getPositionFromListByOptSerial(ageList, customerInfo.getCustomerAge()));
            
            // set repair item & area
            repairItemSpinner.setSelection(Utility.getPositionFromListByOptSerial(repairItemList, customerInfo.getReservationRepairItem()));
            areaSpinner.setSelection(Utility.getPositionFromListByOptSerial(areaList, customerInfo.getReservationArea()));
            
            // set budget
            budgetSpinner.setSelection(Utility.getPositionFromListByOptSerial(budgetList, customerInfo.getReservationBudget()));
            
            // set status comment
            cantDescriptionET.setText(customerInfo.getReservationStatusComment());
            
            // set status
            statusSpinner.setSelection(Utility.getPositionFromListByOptSerial(statusList, customerInfo.getReservationStatus()));
            
            // set space request
            initSpaceData();
            
            // set memo
            memoET.setText(customerInfo.getCustomerMemo());
        }
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
