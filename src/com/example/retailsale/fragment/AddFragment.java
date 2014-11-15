package com.example.retailsale.fragment;

import java.util.ArrayList;
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
import com.example.retailsale.manager.dataoption.GsonDataOptionType;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.util.Utility;

public class AddFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
    private static final String TAG = "AddFragment";
    private static final int REQUEST_ORDER_MEASURE = 999;
    public static final String SEND_CUSTOMER_INFO = "send_customer_info";
    public static final String SEND_NOTE_MSG = "send_note_msg";
    private OptionAdapter infoAdapter, jobAdapter, ageAdapter, sexAdapter, titleAdapter;
    private boolean isChecked = false;
    private CustomerInfo customerInfo;
    private RetialSaleDbAdapter retialSaleDbAdapter;
    private boolean isSendMsg = false;
    private String createDateTime;
    // views
    private MainActivity mainActivity;
    private Spinner infoSpinner, jobSpinner, ageSpinner, sexSpinner, titleSpinner;
    private Spinner yearSpinner, monthSpinner, daySpinner;
    private EditText customerNameET, cellPhoneNumberET, phoneNumberET, companyPhoneNumberET, emailET,
            introducerET;
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
        introducerET = (EditText) view.findViewById(R.id.add_tab_edit_introducer);
        emailET = (EditText) view.findViewById(R.id.add_tab_edit_email);
        leaveInfoCB = (CheckBox) view.findViewById(R.id.add_tab_leave_info_checkbox);
        consumerVisitDateDP = (DatePicker) view.findViewById(R.id.add_tab_consumer_visit_datePicker);
        consumerVisitTimeTP = (TimePicker) view.findViewById(R.id.add_tab_consumer_visit_timePicker);
        designerStoreTV = (TextView) view.findViewById(R.id.add_tab_designer_store);
        createDateTV = (TextView) view.findViewById(R.id.add_tab_create_date);
        
        yearSpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_year);
        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_month);
        monthSpinner.setOnItemSelectedListener(this);
        daySpinner = (Spinner) view.findViewById(R.id.add_tab_customer_birthday_day);
        daySpinner.setOnItemSelectedListener(this);
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
        
        // set customer birthday spinner(year, month, day)
        setCustomerBirthdayYearSpinner();
//        setCustomerBirthdayMonthSpinner();
//        setCustomerBirthdayDaySpinner();
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
        String phoneNumber = phoneNumberET.getText().toString();
        String cellPhoneNumber = cellPhoneNumberET.getText().toString();
        String companyPhoneNumber = companyPhoneNumberET.getText().toString();
        String email = emailET.getText().toString();
        StringBuilder customerBirthday = new StringBuilder();
        String dateString = Utility.covertDateToString(consumerVisitDateDP.getYear(),
                consumerVisitDateDP.getMonth() + 1, consumerVisitDateDP.getDayOfMonth());
        String timeString = Utility.covertTimeToString(consumerVisitTimeTP.getCurrentHour(),
                consumerVisitTimeTP.getCurrentMinute())
                + ":00";
        String customerName = customerNameET.getText().toString();
        String introducer = introducerET.getText().toString();
        int msgSelectedPosition = infoSpinner.getSelectedItemPosition();
        int jobSelectedPosition = jobSpinner.getSelectedItemPosition();
        int ageSelectedPosition = ageSpinner.getSelectedItemPosition();
        int sexSelectedPosition = sexSpinner.getSelectedItemPosition();
        int titleSelectedPosition = titleSpinner.getSelectedItemPosition();
        int yearSelectedPosition = yearSpinner.getSelectedItemPosition();
        
        if (yearSelectedPosition == 0)
        {
            customerBirthday.append(getResources().getString(R.string.no_data));
        }
        else
        {
            customerBirthday.append(yearSpinner.getSelectedItem()).append("-")
                    .append(monthSpinner.getSelectedItem()).append("-")
                    .append(daySpinner.getSelectedItem());
        }
        
        Log.d(TAG, "msgSelectedPosition: " + msgSelectedPosition + " jobSelectedPosition: " + jobSelectedPosition
                + " ageSelectedPosition: " + ageSelectedPosition + " sexSelectedPosition: " + sexSelectedPosition
                + " titleSelectedPosition: " + titleSelectedPosition);
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

        if (customerInfo == null)
        {
            if (isChecked)
            {
                String noData = AddFragment.this.getResources().getString(R.string.no_data);
                customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE_STRING, noData, noData, noData, noData, 0, 0,
                        noData, dateString + timeString, 0, noData, 0, 0, noData, Utility.getCreator(getActivity()),
                        Utility.getCreatorGroup(getActivity()), createDateTime, Utility.SPACE_STRING,
                        Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING, 0, 0,
                        Utility.SPACE_STRING, Utility.SPACE_STRING, 0, Utility.SPACE_STRING);
            }
            else
            {
                customerInfo = new CustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName, cellPhoneNumber,
                        phoneNumber, companyPhoneNumber, sexSelectedPosition, titleSelectedPosition, email, dateString
                                + timeString, msgSelectedPosition, introducer, jobSelectedPosition,
                        ageSelectedPosition, customerBirthday.toString(), Utility.getCreator(getActivity()),
                        Utility.getCreatorGroup(getActivity()), createDateTime, Utility.SPACE_STRING,
                        Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING, Utility.SPACE_STRING, 0, 0,
                        Utility.SPACE_STRING, Utility.SPACE_STRING, 0, Utility.SPACE_STRING);
            }
        }
        else
        {
            customerInfo.modifyCustomerInfo(Utility.DEFAULT_VALUE_STRING, customerName, cellPhoneNumber, phoneNumber,
                    companyPhoneNumber, sexSelectedPosition, titleSelectedPosition, email, dateString + timeString,
                    msgSelectedPosition, introducer, jobSelectedPosition, ageSelectedPosition, customerBirthday.toString(),
                    Utility.getCreator(getActivity()), Utility.getCreatorGroup(getActivity()), dateString + timeString);
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
            int userSerial = settings.getInt(Utility.LoginField.USER_SERIAL, -1);
            int userGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);
            Log.d(TAG, "userSerial === " + userSerial + "userGroup === " + userGroup);
            long id = retialSaleDbAdapter.create(customerInfo.getCustometName(), customerInfo.getCustomerHome(),
                    customerInfo.getCustomerMobile(), customerInfo.getCustomerCompany(),
                    customerInfo.getCustomerMail(), customerInfo.getCustomerSex(), customerInfo.getCustomerBirth(),
                    customerInfo.getCustomerInfo(), customerInfo.getCustomerTitle(), customerInfo.getCustomerJob(),
                    customerInfo.getCustomerIntroducer(), customerInfo.getCustomerAge(),
                    customerInfo.getCustomerVisitDate(), userSerial, userGroup,
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
            infoSpinner.setSelection(0);
            jobSpinner.setSelection(0);
            ageSpinner.setSelection(0);
            sexSpinner.setSelection(0);
            titleSpinner.setSelection(0);

            customerNameET.setText(this.getResources().getString(R.string.no_data));
            cellPhoneNumberET.setText(this.getResources().getString(R.string.no_data));
            phoneNumberET.setText(this.getResources().getString(R.string.no_data));
            companyPhoneNumberET.setText(this.getResources().getString(R.string.no_data));
            emailET.setText(this.getResources().getString(R.string.no_data));
            
            yearSpinner.setSelection(0);
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
        List<GsonDataOptionType> infoList, jobList, ageList, sexList, titleList;
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
        
        BirthdayAdapter yearAdapter = new BirthdayAdapter(this.getActivity(), yearList);
        
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
        
        BirthdayAdapter monthAdapter = new BirthdayAdapter(this.getActivity(), monthList);
        
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
        
        BirthdayAdapter dayAdapter = new BirthdayAdapter(this.getActivity(), dayList);
        
        daySpinner.setAdapter(dayAdapter);
    }
    
    private class BirthdayAdapter extends BaseAdapter
    {
        private static final String TAG = "DateAdapter";
        private static final int BASE_INDEX = 1000;
        private List<String> birthList;
        private Context context;
        private ViewTag viewTag;

        public BirthdayAdapter(Context context, List<String> birthList)
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
        case R.id.add_tab_customer_birthday_year:
            Log.d(TAG, "To select year, the position is " + position);
            if (position == 0)
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
            if (yearSpinner.getSelectedItemPosition() != 0)
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
