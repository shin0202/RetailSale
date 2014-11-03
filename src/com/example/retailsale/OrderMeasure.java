package com.example.retailsale;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.retailsale.fragment.AddFragment;
import com.example.retailsale.manager.addcustomer.CustomerInfo;
import com.example.retailsale.manager.dataoption.GsonDataOptionType;
import com.example.retailsale.manager.dataoption.OptionAdapter;
import com.example.retailsale.util.Utility;

public class OrderMeasure extends Activity implements OnClickListener, OnCheckedChangeListener
{
    private static final String TAG = "OrderMeasure";
    private OptionAdapter spaceAdapter, budgetAdapter, statusAdapter;
    private List<GsonDataOptionType> spaceList, budgetList, statusList;

    private boolean isSendNoteMsgChecked = false, isAsAboveChecked = false;
    private CustomerInfo customerInfo;
    private RetialSaleDbAdapter retialSaleDbAdapter;

    // views
//	private ToggleButton sendNoteMsgTB;
    private CheckBox asAboveCheckBox;
    private DatePicker measureDate;
    private TimePicker measureTime;
    private Spinner spaceSpinner, budgetSpinner, statusSpinner;
    private TextView saleCreateDateTV, consumerNameTV, phoneNumberTV;
    private EditText caseNameET, cantDescriptionET, consumerAddressET, contactAddressET;//, commentET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_measure);

        findViews();
        getBundle();

        // get optionType
        getOptionType();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        retialSaleDbAdapter.close();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.order_measure_cancel_btn:
            showAlertDialog(false);
            break;
        case R.id.order_measure_save_btn:
            showAlertDialog(true);
            break;
        }
    }

    private void findViews()
    {
        Button cancelBtn = (Button) findViewById(R.id.order_measure_cancel_btn);
        Button saveBtn = (Button) findViewById(R.id.order_measure_save_btn);
//		sendNoteMsgTB = (ToggleButton) findViewById(R.id.order_measure_send_note_msg_btn);
        asAboveCheckBox = (CheckBox) findViewById(R.id.order_measure_as_above_checkbox);
        measureDate = (DatePicker) findViewById(R.id.order_measure_datePicker);
        measureTime = (TimePicker) findViewById(R.id.order_measure_timePicker);
        saleCreateDateTV = (TextView) findViewById(R.id.order_measure_sale_create_date);
        consumerNameTV = (TextView) findViewById(R.id.order_measure_consumer_name);
        phoneNumberTV = (TextView) findViewById(R.id.order_measure_consumer_phone_number);
        caseNameET = (EditText) findViewById(R.id.order_measure_case_name);
        cantDescriptionET = (EditText) findViewById(R.id.order_measure_cant_description);
        consumerAddressET = (EditText) findViewById(R.id.order_measure_consumer_address);
        contactAddressET = (EditText) findViewById(R.id.order_measure_consumer_contact_address);
//		commentET = (EditText) findViewById(R.id.order_measure_consumer_comment);

        statusSpinner = (Spinner) findViewById(R.id.order_measure_sale_status_request);
        spaceSpinner = (Spinner) findViewById(R.id.order_measure_consumer_request);
        budgetSpinner = (Spinner) findViewById(R.id.order_measure_consumer_cost);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        // time picker to set 24h
        measureTime.setIs24HourView(true);

        // send note msg toggle btn
//		sendNoteMsgTB.setOnCheckedChangeListener(this);

        // as above check box
        asAboveCheckBox.setOnCheckedChangeListener(this);
    }

    private void getBundle()
    {
        Intent intent = this.getIntent();
        if (intent != null)
        {
            customerInfo = intent.getParcelableExtra(AddFragment.SEND_CUSTOMER_INFO);
            isSendNoteMsgChecked = intent.getBooleanExtra(AddFragment.SEND_NOTE_MSG, isSendNoteMsgChecked);
            Log.d(TAG, "isSendNoteMsgChecked is " + isSendNoteMsgChecked);
//			sendNoteMsgTB.setChecked(isSendNoteMsgChecked);
            Log.d(TAG, "customer visit date is " + customerInfo.getCustomerVisitDate());
            String reservationDate = customerInfo.getReservationDate();
            Log.d(TAG, "reservation date is " + reservationDate);
            String createTime = customerInfo.getCreateTime();
            Log.d(TAG, "create time is " + createTime);
            saleCreateDateTV.setText(createTime);
            if (reservationDate == null || Utility.SPACE_STRING.equals(reservationDate))
            {
//				setInfo(true);
            }
            else
            {
                setInfo(false, customerInfo.getReservationWorkAlias(), customerInfo.getReservationWork(),
                        customerInfo.getReservationContact(), customerInfo.getReservationYear(),
                        customerInfo.getReservationMonth(), customerInfo.getReservationDay(),
                        customerInfo.getReservationHour(), customerInfo.getReservationMinute(),
                        customerInfo.getReservationStatusComment(), customerInfo.getReservationStatus(),
                        customerInfo.getReservationComment(), customerInfo.getReservationSpace(),
                        customerInfo.getReservationBudget());
            }

            if (customerInfo != null)
            {
                consumerNameTV.setText(customerInfo.getCustometName());
                phoneNumberTV.setText(customerInfo.getCustomerHome());
            }
        }
    }

    private void setInfo(boolean isDefault, String caseName, String workAddress, String contactAddress,
            int reservationYear, int reservationMonth, int reservationDay, int reservationHour, int reservationMinute,
            String cantDescription, int statusPosition, String comment, int requestPosition, int costPosition)
    {
        if (isDefault)
        {
        }
        else
        {
            caseNameET.setText(caseName);
            consumerAddressET.setText(workAddress);
            contactAddressET.setText(contactAddress);
            measureDate.updateDate(reservationYear, reservationMonth - 1, reservationDay);
            measureTime.setCurrentHour(reservationHour);
            measureTime.setCurrentMinute(reservationMinute);
            cantDescriptionET.setText(cantDescription);
            statusSpinner.setSelection(statusPosition);
//			commentET.setText(comment);
            spaceSpinner.setSelection(requestPosition);
            budgetSpinner.setSelection(costPosition);
        }
    }

    private void saveData()
    {
        // case name
        customerInfo.setReservationWorkAlias(caseNameET.getText().toString());

        // work address
        customerInfo.setReservationWork(consumerAddressET.getText().toString());

        Log.d(TAG, "work is " + customerInfo.getReservationWork());

        // contact address
        if (asAboveCheckBox.isChecked())
        {
            customerInfo.setReservationContact(consumerAddressET.getText().toString());
        }
        else
        {
            customerInfo.setReservationContact(contactAddressET.getText().toString());
        }

        // measure date & time
        int year = measureDate.getYear();
        int month = measureDate.getMonth() + 1;
        int day = measureDate.getDayOfMonth();
        int hour = measureTime.getCurrentHour();
        int minute = measureTime.getCurrentMinute();
        String dateString = Utility.covertDateToString(year, month, day);

        String timeString = Utility.covertTimeToString(hour, minute) + ":00";
        Log.d(TAG, "date: " + dateString + " time : " + timeString);
        Log.d(TAG, "hour: " + hour + " minute : " + minute);
        customerInfo.setReservationYear(year);
        customerInfo.setReservationMonth(month);
        customerInfo.setReservationDay(day);
        customerInfo.setReservationHour(hour);
        customerInfo.setReservationMinute(minute);

        customerInfo.setReservationDate(dateString + timeString);
        customerInfo.setReservationTime(timeString);

        // can't sale description
        customerInfo.setReservationStatusComment(cantDescriptionET.getText().toString());

        // status
        customerInfo.setReservationStatus(statusSpinner.getSelectedItemPosition());

        // comment
//		customerInfo.setReservationComment(commentET.getText().toString());
        customerInfo.setReservationComment(Utility.SPACE_STRING);

        // request
        customerInfo.setReservationSpace(spaceSpinner.getSelectedItemPosition());

        // cost
        customerInfo.setReservationBudget(budgetSpinner.getSelectedItemPosition());

        // send result
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AddFragment.SEND_CUSTOMER_INFO, customerInfo);
        resultIntent.putExtra(AddFragment.SEND_NOTE_MSG, isSendNoteMsgChecked);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
//		case R.id.order_measure_send_note_msg_btn:
//			Log.d(TAG, "To change note msg, the isChecked" + isChecked);
//			isSendNoteMsgChecked = isChecked;
//			break;
        case R.id.order_measure_as_above_checkbox:
            Log.d(TAG, "To change as above checkbox, the isChecked" + isChecked);
            isAsAboveChecked = isChecked;
            handleContactAddress(isAsAboveChecked);
            break;
        }
    }

    private void getOptionType()
    {
        spaceList = new ArrayList<GsonDataOptionType>();
        budgetList = new ArrayList<GsonDataOptionType>();
        statusList = new ArrayList<GsonDataOptionType>();

        retialSaleDbAdapter = new RetialSaleDbAdapter(OrderMeasure.this);
        retialSaleDbAdapter.open();

        // to get option type content
        Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
        int optionType, optionSerial;
        String optionAlias, optionName;
        String statusType = this.getResources().getString(R.string.option_customer_status);
        String budgetType = this.getResources().getString(R.string.option_customer_budget);
        String spaceType = this.getResources().getString(R.string.option_customer_space);

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

                    if (optionAlias.equals(statusType))
                    {
                        statusList.add(new GsonDataOptionType(optionSerial, optionName));
                    }
                    else if (optionAlias.equals(budgetType))
                    {
                        budgetList.add(new GsonDataOptionType(optionSerial, optionName));
                    }
                    else if (optionAlias.equals(spaceType))
                    {
                        spaceList.add(new GsonDataOptionType(optionSerial, optionName));
                    }
                }
            }
            optionTypeCursor.close();
        }
        else
        {
            Log.d(TAG, "option cursor is null ");
        }

        // status spinner
        statusAdapter = new OptionAdapter(OrderMeasure.this, statusList);
        statusSpinner.setAdapter(statusAdapter);

        // budget spinner
        budgetAdapter = new OptionAdapter(OrderMeasure.this, budgetList);
        budgetSpinner.setAdapter(budgetAdapter);

        // space spinner
        spaceAdapter = new OptionAdapter(OrderMeasure.this, spaceList);
        spaceSpinner.setAdapter(spaceAdapter);
    }

    private void handleContactAddress(boolean isEnable)
    {
        if (isEnable)
        {
            contactAddressET.setText(consumerAddressET.getText().toString());
            contactAddressET.setEnabled(false);
        }
        else
        {
            contactAddressET.setEnabled(true);
        }
    }
    
    private void finishActivity()
    {
        this.finish();
        overridePendingTransition(R.anim.activity_conversion_in_from_right, R.anim.activity_conversion_out_to_left);
    }
    
    private void showAlertDialog(final boolean isSaved)
    {
        new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.remind))
                .setMessage(this.getResources().getString(R.string.order_measure_dialog_msg))
                .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (isSaved)
                        {
                            if (customerInfo != null)
                            {
                                saveData();
                                finishActivity();
                            }
                        }
                        else
                        {
                            finishActivity();
                        }
                    }
                }).show();
    }
}
