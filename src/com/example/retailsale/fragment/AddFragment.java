package com.example.retailsale.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import com.example.retailsale.MainActivity;
import com.example.retailsale.OrderMeasure;
import com.example.retailsale.R;
import com.example.retailsale.RetialSaleDbAdapter;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.manager.dataoption.DataOption;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.manager.userlist.UserDataForList;
import com.example.retailsale.util.Utility;

public class AddFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
    private static final String TAG = "AddFragment";
    private static final int REQUEST_ORDER_MEASURE = 999;
    public static final String SEND_CUSTOMER_INFO = "send_customer_info";
    public static final String SEND_NOTE_MSG = "send_note_msg";
    private boolean isChecked = false;
    private CustomerInfo customerInfo;
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private boolean isSendMsg = false;
    private String createDateTime;
    private List<DataOption> infoList, jobList, ageList, sexList, titleList;
    private List<DataOption> statusList, spaceList, budgetList;
    private List<UserDataForList> userDataList;
    private List<String> phoneCodeList;
    // views
    private MainActivity mainActivity;
    private Spinner infoSpinner, jobSpinner, ageSpinner, sexSpinner, titleSpinner, designerSpinner;
    private Spinner yearSpinner, monthSpinner, daySpinner;
    private Spinner phoneNumberSpinner, companyPhoneNumberSpinner;
    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
            introducerET;
    private CheckBox leaveInfoCB;
    private TextView companyNameTV, customerIDTV, designerStoreTV, createDateTV;
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
        introducerET = (EditText) view.findViewById(R.id.add_tab_edit_introducer);
        emailET = (EditText) view.findViewById(R.id.add_tab_edit_email);
        leaveInfoCB = (CheckBox) view.findViewById(R.id.add_tab_leave_info_checkbox);
        consumerVisitDateDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_visit_datePicker);
        consumerVisitTimeTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_visit_timePicker);
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
        
        String[] phoneCodeArray = getResources().getStringArray(R.array.phone_code);
        phoneCodeList = Arrays.asList(phoneCodeArray);
        
        CommonAdapter phoneCodeAdapter = new CommonAdapter(this.getActivity(), phoneCodeList);
        
        phoneNumberSpinner.setAdapter(phoneCodeAdapter);
        companyPhoneNumberSpinner.setAdapter(phoneCodeAdapter);
        
        // save btn
        saveBtn.setOnClickListener(this);
        newBtn.setOnClickListener(this);
        // leave info
        leaveInfoCB.setOnCheckedChangeListener(this);
        // time picker to set 24h
        consumerVisitTimeTP.setIs24HourView(true);
        
        // create date
        createDateTime = Utility.getCurrentDateTime();
        createDateTV.setText(createDateTime.replace("T", ""));

        retialSaleDbAdapter = new RetialSaleDbAdapter(AddFragment.this.getActivity());
        retialSaleDbAdapter.open();
        
        // get optionType
        getOptionType();
        
        // get designer list
        getUserList();
        
        // set customer birthday spinner(year, month, day)
        setCustomerBirthdayYearSpinner();
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
        String customerName = customerNameET.getText().toString();
        String introducer = introducerET.getText().toString();
        
        int msgSelectedSerial = infoList.get(infoSpinner.getSelectedItemPosition()).getOptSerial();
        int jobSelectedSerial = jobList.get(jobSpinner.getSelectedItemPosition()).getOptSerial();
        int ageSelectedSerial = ageList.get(ageSpinner.getSelectedItemPosition()).getOptSerial();
        int sexSelectedSerial = sexList.get(sexSpinner.getSelectedItemPosition()).getOptSerial();
        int titleSelectedSerial = titleList.get(titleSpinner.getSelectedItemPosition()).getOptSerial();
        
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
        
        Log.d(TAG, "msgSelectedSerial: " + msgSelectedSerial + " jobSelectedSerial: " + jobSelectedSerial
                + " ageSelectedSerial: " + ageSelectedSerial + " sexSelectedSerial: " + sexSelectedSerial
                + " titleSelectedSerial: " + titleSelectedSerial);
        
        Log.d(TAG, "date: " + dateString + "time : " + timeString);
        
        Log.d(TAG, "customerName : " + customerName + " phoneNumber: " + phoneNumber + " cellPhoneNumber: "
                + cellPhoneNumber + " companyPhoneNumber: " + companyPhoneNumber + " email: " + email + " birthday: "
                + customerBirthday + " introducer: " + introducer);

        // check phone number
        if (!isChecked && !Utility.isPhoneValid(phoneNumber))
        {
            showToast(this.getActivity().getResources().getString(R.string.home_phone_field_error));
            return false;
        }
        // check cellphone number
        if (!isChecked && !Utility.isCellphoneValid(cellPhoneNumber))
        {
            showToast(this.getActivity().getResources().getString(R.string.cell_phone_field_error));
            return false;
        }
        // check company phone number
        if (!isChecked && !companyPhoneNumber.equals(Utility.SPACE_STRING) && !Utility.isCompanyPhoneValid(phoneNumber))
        {
            showToast(this.getActivity().getResources().getString(R.string.company_phone_field_error));
            return false;
        }
        // check email
        if (!isChecked && !email.equals(Utility.SPACE_STRING) && !Utility.isEmailValid(email))
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
        
        // the creator is not from share preference but from creator list
        int creator = userDataList.get(designerSpinner.getSelectedItemPosition()).getUserSerial();
        int group = userDataList.get(designerSpinner.getSelectedItemPosition()).getUserGroup();
        
        Log.d(TAG, "creator: " + creator + " group: " + group);

        try
        {
            if (customerInfo == null)
            {
                if (isChecked)
                {
                    String noData = AddFragment.this.getResources().getString(R.string.no_data);
                    customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE_STRING, noData, noData,
                            noData, noData, sexSelectedSerial, titleSelectedSerial, noData,
                            dateString + timeString, msgSelectedSerial, noData, jobSelectedSerial,
                            ageSelectedSerial, noData, creator, group, createDateTime,
                            Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING,
                            Utility.SPACE_STRING, Utility.SPACE_STRING, spaceList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(), statusList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(),
                            Utility.SPACE_STRING, Utility.SPACE_STRING, budgetList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(),
                            Utility.SPACE_STRING);
                }
                else
                {
                    customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName,
                            cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedSerial,
                            titleSelectedSerial, email, dateString + timeString, msgSelectedSerial,
                            introducer, jobSelectedSerial, ageSelectedSerial,
                            customerBirthday.toString(), creator, group, createDateTime,
                            Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING,
                            Utility.SPACE_STRING, Utility.SPACE_STRING, spaceList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(), statusList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(),
                            Utility.SPACE_STRING, Utility.SPACE_STRING, budgetList.get(
                                    Utility.DEFAULT_ZERO_VALUE).getOptSerial(),
                            Utility.SPACE_STRING);
                }
            }
            else
            {
                customerInfo.modifyCustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName,
                        cellPhoneNumber, phoneNumber, companyPhoneNumber, sexSelectedSerial,
                        titleSelectedSerial, email, dateString + timeString, msgSelectedSerial,
                        introducer, jobSelectedSerial, ageSelectedSerial,
                        customerBirthday.toString(), creator, group, dateString + timeString);
                
                Log.d(TAG, "ReservationBudgetPosition() === " + customerInfo.getReservationBudgetPosition());
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

        if (!workAddress.equals(Utility.SPACE_STRING) && contactAddress.equals(Utility.SPACE_STRING))
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
            SharedPreferences settings = mainActivity.getSharedPreferences(Utility.LoginField.DATA, 0);
//            int userSerial = settings.getInt(Utility.LoginField.USER_SERIAL, -1);
//            int userGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);
            Log.d(TAG, "userSerial === " + customerInfo.getCreator() + "userGroup === " + customerInfo.getCreatorGroup());
            long id = retialSaleDbAdapter.create(customerInfo.getCustometName(), customerInfo.getCustomerHome(),
                    customerInfo.getCustomerMobile(), customerInfo.getCustomerCompany(),
                    customerInfo.getCustomerMail(), customerInfo.getCustomerSex(), customerInfo.getCustomerBirth(),
                    customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(), customerInfo.getCustomerJob(),
                    customerInfo.getCustomerIntroducer(), customerInfo.getCustomerAge(),
                    customerInfo.getCustomerVisitDate(), customerInfo.getCreator(), customerInfo.getCreatorGroup(),
                    Utility.covertDateStringToServer(createDateTime), sendNoteValue,
                    customerInfo.getReservationWorkAlias(), customerInfo.getReservationStatusComment(),
                    customerInfo.getReservationStatus(), customerInfo.getReservationWork(),
                    customerInfo.getReservationContact(), customerInfo.getReservationComment(),
                    customerInfo.getReservationSpace(), customerInfo.getReservationBudget(),
                    customerInfo.getReservationDate(), RetialSaleDbAdapter.NOTUPLOAD);
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

        yearSpinner.setEnabled(enabled);
        monthSpinner.setEnabled(enabled);
        daySpinner.setEnabled(enabled);
        
        introducerET.setEnabled(enabled);
        // datepicker
        consumerVisitDateDP.setEnabled(enabled);
        // timepicker
        consumerVisitTimeTP.setEnabled(enabled);
        
        if (!enabled)
        {
            infoSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            jobSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            ageSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            sexSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);
            titleSpinner.setSelection(Utility.DEFAULT_ZERO_VALUE);

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
        // to get option type content
        Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
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
                    }
                    else if (optionAlias.equals(budgetType))
                    {
                        budgetList.add(new DataOption("", optionSerial, optionName, false));
                    }
                }
            }
            optionTypeCursor.close();
        }
        else
        {
            Log.d(TAG, "option cursor is null ");
        }
        
        OptionAdapter infoAdapter, jobAdapter, ageAdapter, sexAdapter, titleAdapter;
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
        Cursor userStoreCursor = retialSaleDbAdapter.getOptionByOptionSerial(Utility.getCreatorGroup(AddFragment.this
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
        Cursor userCursor = retialSaleDbAdapter.getAllUser();

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
        // user spinner
        UserAdapter userAdapter = new UserAdapter(this.getActivity(), userDataList);
        designerSpinner.setAdapter(userAdapter);
    }
    
    private void setCustomerBirthdayYearSpinner()
    {
        int currentYear = Utility.getCurrentYear();
        int startYear = currentYear - 150;
        Log.d(TAG, "current year : " + currentYear + " start year : " + startYear);
        
        List<String> yearList = new ArrayList<String>();
        
        yearList.add(getResources().getString(R.string.no_data));
        
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
    
    private class CommonAdapter extends BaseAdapter
    {
        private static final String TAG = "CommonAdapter";
        private static final int BASE_INDEX = 1000;
        private List<String> birthList;
        private Context context;
        private ViewTag viewTag;

        public CommonAdapter(Context context, List<String> birthList)
        {
            this.context = context;
            this.birthList = birthList;
        }

        @Override
        public int getCount()
        {
            return birthList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return birthList.get(position);
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
                convertView = layoutInflater.inflate(R.layout.option_layout, null);
                viewTag = new ViewTag((TextView) convertView.findViewById(R.id.option_text));
                convertView.setTag(viewTag);
            }
            else
            {
                viewTag = (ViewTag) convertView.getTag();
            }

            convertView.setId(BASE_INDEX + position);

            if (position < birthList.size())
            {
                viewTag.itemName.setText(birthList.get(position));
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
    
    private class UserAdapter extends BaseAdapter
    {
        private static final String TAG = "UserAdapter";
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
                convertView = layoutInflater.inflate(R.layout.option_layout, null);
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
