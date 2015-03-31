package com.example.retailsale;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONStringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.POSTThread;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;

public class UploadReceiver extends BroadcastReceiver
{
    private static final String TAG = "UploadReceiver";
    
    private RetialSaleDbAdapter retialSaleDbAdapter;
    
    private Context context;
    
    private String id;
    private String password;
    
    private int needCount = 0;
    private int currentCount = 0;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent != null)
        {
            String action = intent.getAction();
            Log.d(TAG, "action : " + action);
            if (action != null)
            {
                if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_DATE_CHANGED)
                        || action.equals(Intent.ACTION_TIME_CHANGED))
                {
                    Utility.setAlarmTime(context);
                }
                else if (intent.getAction() != null)
                {
                    this.context = context;
                    
                    if (!Utility.hasAppRunning(context))
                    {
                        if (Utility.isInternetAvailable(context))
                        {
                            Utility.openLogFile(Utility.LOG_FILE_PATH);
                            handleLoginAction(context);
                        }
                    }
                }
            }
        }
    }
    
    private synchronized void handleLoginAction(Context context)
    {

        boolean hadLogin = Utility.hadLogin(context);
        
        Log.d(TAG, "had login ? " + hadLogin);

        if (hadLogin)
        {
            login(context, true);
        }
        else
        {
            Utility.writeLogData(context.getResources().getString(R.string.sync_tab_upload_error));
            Utility.closeLogFile();
        }
    }
    
    private synchronized void login(final Context context, boolean hadLogin)
    {
        Utility.openLogFile(Utility.LOG_FILE_PATH);
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);

        if (hadLogin)
        {
            id = settings.getString(Utility.LoginField.ID, "");
            password = settings.getString(Utility.LoginField.PASSWORD, "");
        }

        HttpManager httpManager = new HttpManager();
        httpManager.login(context, id, password, new GetLoginListener()
        {
            @Override
            public void onResult(Boolean isSuccess, GsonLoginInfo userInfo)
            {

                if (isSuccess)
                {
                    if (userInfo != null)
                    {
                        if (userInfo.getValue() != null && userInfo.getValue().size() > 0)
                        {
                            int userSerial = userInfo.getValue().get(0).getUserSerial();
                            int userGroup = userInfo.getValue().get(0).getUserGroup();
                            String loginKey = userInfo.getValue().get(0).getLoginKey();
                            String message = userInfo.getValue().get(0).getMessage();
                            Log.d(TAG, " userSerial : " + userSerial + " userGroup : " + userGroup + " loginKey : "
                                    + loginKey + " message : " + message);
                            if (message.equals(context.getResources().getString(R.string.login_successful)))
                            {
                                Log.d(TAG, "Message is successfully");
                                Utility.saveData(context, id, password, userSerial, userGroup, loginKey);

                                addCustomer(context);
                            }
                            else
                            {
                                Log.d(TAG, "Message is failed");
                                Utility.writeLogData(context.getResources().getString(R.string.sync_tab_upload_error));
                                Utility.closeLogFile();
                            }
                        }
                        else
                        {
                            Log.d(TAG, "value is null or size less/equal than 0");
                            Utility.writeLogData(context.getResources().getString(R.string.sync_tab_upload_error));
                            Utility.closeLogFile();
                        }
                    }
                    else
                    {
                        Log.d(TAG, "userInfo is null");
                        Utility.writeLogData(context.getResources().getString(R.string.sync_tab_upload_error));
                        Utility.closeLogFile();
                    }
                }
                else
                {
                    Log.d(TAG, "Login failed");
                    Utility.writeLogData(context.getResources().getString(R.string.sync_tab_upload_error));
                    Utility.closeLogFile();
                }
            }
        });
    }
    
    private synchronized void addCustomer(Context context)
    {

        if (retialSaleDbAdapter == null)
            retialSaleDbAdapter = new RetialSaleDbAdapter(
                    context);

        if (!retialSaleDbAdapter.isDbOpen())
            retialSaleDbAdapter.open();

        needCount = 0;
        currentCount = 0;

        Cursor cursor = retialSaleDbAdapter.getCustomerNotUpload();
        if (cursor != null)
        {
            needCount = cursor.getCount();
            if (needCount > 0)
            {
                while (cursor.moveToNext())
                {
                    int creator = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATOR));
                    int creatorGroup = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATOR_GROUP));

                    Log.d(TAG, "creator is " + creator);

                    long rowId = cursor.getLong(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_ID));
                    String customerAccount = Utility.DEFAULT_VALUE_STRING;
                    String custometName = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CUSTOMER_NAME));
                    String customerMobile = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MOBILE));
                    String customerHome = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_HOME));
                    String customerCompany = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_COMPANY));
                    int customerSex = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_SEX));
                    int customerTitle = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_TITLE));
                    String customerMail = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_EMAIL));
                    String customerVisitDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_VISIT_DATE));
                    int customerInfo = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INFO));
                    String customerIntroducer = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_INTRODUCER));
                    int customerJob = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_JOB));
                    int customerAge = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_AGE));
                    String customerMemo = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_MEMO));
                    String customerBirth = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_BIRTHDAY));
                    String createDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_ADD_CREATE_DATE));
                    /***************************************************************/
                    int reservationSerial = -1;
                    int customerSerial = -1;
                    String reservationDate = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_RESERVATION_DATE));
                    String reservationWork = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_WORK));
                    String workCode = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_WROK_POSTCODE));
                    String reservationWorkAlias = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_WORK_ALIAS));
                    String reservationContact = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT));
                    String contactCode = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_CONTACT_POSTCODE));
                    String reservationSpace = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_SPACE));
                    reservationSpace = reservationSpace.substring(0, reservationSpace.length() - 1);
                    int reservationStatus = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS));
                    String reservationUpateTime = createDate; // not incorrect?
                    String reservationStatusComment = cursor.getString(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_STATUS_COMMENT));
                    int reservationBudget = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_BUDGET));
                    int reservationRepairItem = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_REPAIR_ITEM));
                    int reservationArea = cursor.getInt(cursor
                            .getColumnIndex(RetialSaleDbAdapter.KEY_AREA));
                    String reservationDataSerial = Utility.DEFAULT_VALUE_STRING;
                    // didn't have the field "comment", "send note"
                    JSONStringer json = null, customerReservationJson = null;
                    try
                    {
                        customerReservationJson = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SERIAL)
                                .value(reservationSerial)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SERIAL)
                                .value(customerSerial)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATE)
                                .value(reservationDate)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK)
                                .value(reservationWork)
                                .key(Utility.AddCustomerJsonTag.WORK_POSTCODE).value(workCode)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_WORK_ALIAS)
                                .value(reservationWorkAlias)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_CONTACT)
                                .value(reservationContact)
                                .key(Utility.AddCustomerJsonTag.CONTACT_POSTCODE)
                                .value(contactCode)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_REPAIR_ITEM)
                                .value(reservationRepairItem)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_AREA)
                                .value(reservationArea)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_SPACE)
                                .value(reservationSpace)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS)
                                .value(reservationStatus)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_UPDATE_TIME)
                                .value(reservationUpateTime)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_STATUS_COMMENT)
                                .value(reservationStatusComment)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_BUDGET)
                                .value(reservationBudget)
                                .key(Utility.AddCustomerJsonTag.RESERVATION_DATA_SERIAL)
                                .value(reservationDataSerial)
                                .key(Utility.AddCustomerJsonTag.CREATOR).value(creator)
                                .key(Utility.AddCustomerJsonTag.CREATOR_GROUP).value(creatorGroup)
                                .key(Utility.AddCustomerJsonTag.CREATE_TIME).value(createDate)
                                .endObject();
                        // Log.d(TAG, "customerReservationJson === " +
                        // customerReservationJson.toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Utility.writeLogData(context.getResources().getString(R.string.sync_tab_sync_db_error));
                        Utility.closeLogFile();
                        break;
                    }
                    try
                    {
                        json = new JSONStringer().object()
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_ACCOUNT)
                                .value(customerAccount)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_NAME).value(custometName)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_Mobile)
                                .value(customerMobile)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_HOME).value(customerHome)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_COMPANY)
                                .value(customerCompany)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_SEX).value(customerSex)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_TITLE)
                                .value(customerTitle).key(Utility.AddCustomerJsonTag.CUSTOMER_MAIL)
                                .value(customerMail)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_VISIT_DATE)
                                .value(customerVisitDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INFO).value(customerInfo)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_INTRODUCER)
                                .value(customerIntroducer)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_JOB).value(customerJob)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_AGE).value(customerAge)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_MEMO).value(customerMemo)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_BIRTH)
                                .value(customerBirth).key(Utility.AddCustomerJsonTag.CREATOR)
                                .value(creator).key(Utility.AddCustomerJsonTag.CREATOR_GROUP)
                                .value(creatorGroup).key(Utility.AddCustomerJsonTag.CREATE_TIME)
                                .value(createDate)
                                .key(Utility.AddCustomerJsonTag.CUSTOMER_RESERVATION)
                                .value(customerReservationJson).endObject();
                        // Log.d(TAG, "json === " + json.toString());
                        POSTThread postThread = new POSTThread(context, json, custometName, handler, rowId,
                                retialSaleDbAdapter);
                        postThread.start();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Log.d(TAG, "db get error");
                        Utility.writeLogData(context.getResources().getString(R.string.sync_tab_sync_db_error));
                        Utility.closeLogFile();
                        break;
                    }
                }
            }
            else
            {
                Log.d(TAG, "no customer");
                Utility.writeLogData(context.getResources().getString(R.string.sync_tab_sync_no_customer));
                Utility.closeLogFile();
            }
            cursor.close();
        }
        else
        {
            Log.d(TAG, "no customer, cursor is null");
            Utility.writeLogData(context.getResources().getString(R.string.sync_tab_sync_no_customer));
            Utility.closeLogFile();
        }
    }
    
    private void closeDb()
    {

        if (retialSaleDbAdapter.isDbOpen())
        {
            retialSaleDbAdapter.close();
        }
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            currentCount++;
            if (currentCount == needCount)
            {
                closeDb();
            }
            String customerName = (String) msg.obj;
            int statusCode = msg.arg1;
            if (statusCode == HttpStatus.SC_CREATED)
            {
                Utility.writeLogData(customerName + context.getResources().getString(R.string.sync_tab_sync_upload_success));
            }
            else
            {
                Utility.writeLogData(customerName + context.getResources().getString(R.string.sync_tab_sync_upload_failed));
            }
        }
    };
}
