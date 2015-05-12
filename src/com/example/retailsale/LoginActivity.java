package com.example.retailsale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retailsale.adapter.RetialSaleDbAdapter;
import com.example.retailsale.util.Utility;

public class LoginActivity extends Activity implements OnClickListener
{
    private static final String TAG = "Login";
    private static final String DEFAULT_PHOTO = "default_photo";
    private static final String INSTALLED = "installed";

    private EditText inputUserId;
    private EditText inputUserPassword;
    private ProgressDialog progressDialog;
    private RetialSaleDbAdapter retialSaleDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        findViews();
        retialSaleDbAdapter = new RetialSaleDbAdapter(LoginActivity.this);
        
        Utility.setAlarmTime(LoginActivity.this);
        
        Utility.createFolder(Utility.LOG_FOLDER_PATH);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        boolean hadLogin = Utility.hadLogin(Login.this);
//
//        if (hadLogin) startManageFragment();
        
        // To set default IP, if IP not exist
        Utility.setDefaultIP(this);
        
        if (hadLogin()) 
        {
            startManageFragment();
            return;
        }
        
        retialSaleDbAdapter.open();
        
        if (!hadDefaultPhoto())
        {
            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            
            Thread initialThread = new Thread()
            {
                @Override
                public void run()
                {   
                    getDataOption();
                    getUser();
                    getAppUser();
                    
                    boolean isSuccess = Utility.copyAssets(LoginActivity.this);
                    Log.d(TAG, "isSuccess === " + isSuccess);
                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                }
            };
            initialThread.start();
        }
        
//        Thread thread = new Thread()
//        {
//            public void run() 
//            {
//                Utility.generateDefaultBase64File(new File(Utility.FILE_PATH));
//            }
//        };
//        
//        thread.run();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void findViews()
    {
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        inputUserId = (EditText) findViewById(R.id.login_input_user_id);
        inputUserPassword = (EditText) findViewById(R.id.login_input_user_pwd);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.login_btn:
            String id = inputUserId.getText().toString();
            String password = inputUserPassword.getText().toString();
            
            Cursor appUserCursor = retialSaleDbAdapter.getUserByAccountAndPassword(id, password);
            if (appUserCursor != null)
            {
                int count = appUserCursor.getCount();
                if (count > 0)
                {
                    while (appUserCursor.moveToNext())
                    {
                        String getAccount = appUserCursor.getString(appUserCursor
                                .getColumnIndex(RetialSaleDbAdapter.KEY_APP_USER_ACCOUNT));
                        String getPassword = appUserCursor.getString(appUserCursor
                                .getColumnIndex(RetialSaleDbAdapter.KEY_APP_USER_PASSWORD));
                        
                        int getGroup = appUserCursor.getInt(appUserCursor
                                .getColumnIndex(RetialSaleDbAdapter.KEY_APP_USER_GROUP));
                        
                        saveData(getAccount, getPassword, getGroup);
                        
                        showToast(getResources().getString(R.string.login_successfully));
                        startManageFragment();
                        retialSaleDbAdapter.close();
                    }
                }
                else
                {
                    showToast(getResources().getString(R.string.login_failed));
                }
                
                appUserCursor.close();
            }
            else
            {
                showToast(getResources().getString(R.string.login_failed));
            }
            
//            if (Utility.isInternetAvailable(Login.this))
//            {
//                handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
//                login(id, password);
//                // startManageFragment();
//            }
//            else
//            {
//                showToast(getResources().getString(R.string.api_no_network));
//            }
            break;
        }
    }

    private void startManageFragment()
    {
        Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
        startActivity(intent);
//		Login.this.finish();
    }
    
    private boolean hadDefaultPhoto()
    {
        SharedPreferences settings = this.getSharedPreferences(DEFAULT_PHOTO, Utility.DEFAULT_ZERO_VALUE);
        boolean installed = settings.getBoolean(INSTALLED, false);
        
        Log.d(TAG, "installed === " + installed);
        
        if (!installed)
        {
            settings.edit().putBoolean(INSTALLED, true).commit();
        }
        return installed;
    }

//    private void login(final String id, final String password)
//    {
//        HttpManager httpManager = new HttpManager();
//        httpManager.login(Login.this, id, password, new GetLoginListener()
//        {
//            @Override
//            public void onResult(Boolean isSuccess, GsonLoginInfo userInfo)
//            {
//                if (isSuccess)
//                {
//                    if (userInfo != null)
//                    {
//                        if (userInfo.getValue() != null && userInfo.getValue().size() > 0)
//                        {
//                            int userSerial = userInfo.getValue().get(0).getUserSerial();
//                            int userGroup = userInfo.getValue().get(0).getUserGroup();
//                            String loginKey = userInfo.getValue().get(0).getLoginKey();
//                            String message = userInfo.getValue().get(0).getMessage();
//                            Log.d(TAG, " userSerial : " + userSerial + " userGroup : " + userGroup + " loginKey : "
//                                    + loginKey + " message : " + message);
//                            if (message.equals(Login.this.getResources().getString(R.string.login_successful)))
//                            {
//                                Log.d(TAG, "Message is successfully");
//                                Utility.saveData(Login.this, id, password, userSerial, userGroup, loginKey);
//                                retialSaleDbAdapter.open();
//                                boolean isGetDataOption = getDataOption();
//                                boolean isGetUser = getUser();
//                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                                
//                                if (isGetDataOption && isGetUser)
//                                {
//                                    startManageFragment();
//                                    showToast(getResources().getString(R.string.login_successfully));
//                                }
//                                else
//                                {
//                                    Log.d(TAG, "The data is incorrect");
//                                    showToast(getResources().getString(R.string.login_failed));
//                                }
//                            }
//                            else
//                            {
//                                Log.d(TAG, "Message is failed");
//                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                                showToast(getResources().getString(R.string.login_failed));
//                            }
//                        }
//                        else
//                        {
//                            Log.d(TAG, "value is null or size less/equal than 0");
//                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                            showToast(getResources().getString(R.string.login_failed));
//                        }
//                    }
//                    else
//                    {
//                        Log.d(TAG, "userInfo is null");
//                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                        showToast(getResources().getString(R.string.login_failed));
//                    }
//                }
//                else
//                {
//                    Log.d(TAG, "Login failed ");
//                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                    showToast(getResources().getString(R.string.login_failed));
//                }
//            }
//        });
//    }
    
    private boolean getDataOption()
    {  
        Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
        if (optionTypeCursor != null)
        {
            int count = optionTypeCursor.getCount();
            optionTypeCursor.close();
            if (count <= 0)
            {
                return setDataOption();
            }
            else
            {
                return true;
            }
        }
        else
        {
            Log.d(TAG, "option cursor is null ");
            return setDataOption();
        }
    }
    
    private boolean setDataOption()
    {

        try
        {
            retialSaleDbAdapter.create(1, "使用者類型", 2, "設計師兼任店長");
            retialSaleDbAdapter.create(1, "使用者類型", 3, "設計師");
            retialSaleDbAdapter.create(1, "使用者類型", 4, "離職員工");
            retialSaleDbAdapter.create(1, "使用者類型", 40, "系統管理者");

            retialSaleDbAdapter.create(2, "客戶性別", 5, "未選擇");
            retialSaleDbAdapter.create(2, "客戶性別", 7, "男");
            retialSaleDbAdapter.create(2, "客戶性別", 8, "女");

            retialSaleDbAdapter.create(3, "客戶稱謂", 9, "先生");
            retialSaleDbAdapter.create(3, "客戶稱謂", 10, "小姐");
            retialSaleDbAdapter.create(3, "客戶稱謂", 11, "來賓");

            retialSaleDbAdapter.create(4, "客戶訊息來源", 12, "已有先參觀過歐德");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 13, "舊客戶");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 50, "親友介紹");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 51, "路過門市");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 72, "雜誌訊息");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 73, "報紙報導");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 74, "電台播放");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 75, "電視播映");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 76, "網路搜尋");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 77, "電子郵件");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 78, "傢俱展覽");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 79, "室內設計師");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 80, "精裝本或DM");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 81, "配合專案");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 82, "客戶來電");
            retialSaleDbAdapter.create(4, "客戶訊息來源", 83, "開發");

            retialSaleDbAdapter.create(5, "客戶職業", 14, "無資料");
            retialSaleDbAdapter.create(5, "客戶職業", 15, "自營商");
            retialSaleDbAdapter.create(5, "客戶職業", 57, "自由業");
            retialSaleDbAdapter.create(5, "客戶職業", 58, "公務員");
            retialSaleDbAdapter.create(5, "客戶職業", 67, "教師");
            retialSaleDbAdapter.create(5, "客戶職業", 68, "醫生");
            retialSaleDbAdapter.create(5, "客戶職業", 69, "製造業");
            retialSaleDbAdapter.create(5, "客戶職業", 70, "金融業");
            retialSaleDbAdapter.create(5, "客戶職業", 71, "其他");

            retialSaleDbAdapter.create(6, "客戶年齡", 18, "無資料");
            retialSaleDbAdapter.create(6, "客戶年齡", 20, "21-30");
            retialSaleDbAdapter.create(6, "客戶年齡", 43, "31-40");
            retialSaleDbAdapter.create(6, "客戶年齡", 44, "41-50");
            retialSaleDbAdapter.create(6, "客戶年齡", 45, "51-60");
            retialSaleDbAdapter.create(6, "客戶年齡", 46, "61以上");

            retialSaleDbAdapter.create(7, "服務類型", 21, "圖片服務");
            retialSaleDbAdapter.create(7, "服務類型", 39, "目錄服務");

            retialSaleDbAdapter.create(8, "使用者店別", 22, "文心店");
            retialSaleDbAdapter.create(8, "使用者店別", 23, "大里店");
            retialSaleDbAdapter.create(8, "使用者店別", 41, "環中店");
            retialSaleDbAdapter.create(8, "使用者店別", 42, "彰化店");
            retialSaleDbAdapter.create(8, "使用者店別", 64, "斗六店");
            retialSaleDbAdapter.create(8, "使用者店別", 65, "豐原店");

            retialSaleDbAdapter.create(9, "銷售進度", 24, "來店客");
            retialSaleDbAdapter.create(9, "銷售進度", 59, "約丈量");
            retialSaleDbAdapter.create(9, "銷售進度", 60, "談圖");
            retialSaleDbAdapter.create(9, "銷售進度", 61, "議價");
            retialSaleDbAdapter.create(9, "銷售進度", 62, "施工");
            retialSaleDbAdapter.create(9, "銷售進度", 63, "已成交");

            retialSaleDbAdapter.create(10, "客戶預算", 25, "無資料");
            retialSaleDbAdapter.create(10, "客戶預算", 26, "0-10萬");
            retialSaleDbAdapter.create(10, "客戶預算", 52, "11-20萬");
            retialSaleDbAdapter.create(10, "客戶預算", 53, "21-30萬");
            retialSaleDbAdapter.create(10, "客戶預算", 54, "31-40萬");
            retialSaleDbAdapter.create(10, "客戶預算", 55, "41-50萬");
            retialSaleDbAdapter.create(10, "客戶預算", 56, "50萬以上");

            retialSaleDbAdapter.create(11, "客戶格局需求", 27, "全室規劃");
            retialSaleDbAdapter.create(11, "客戶格局需求", 28, "歐德沙發");
            retialSaleDbAdapter.create(11, "客戶格局需求", 29, "綠色床墊");
            retialSaleDbAdapter.create(11, "客戶格局需求", 47, "優渥實木");
            retialSaleDbAdapter.create(11, "客戶格局需求", 48, "義歐床組");
            retialSaleDbAdapter.create(11, "客戶格局需求", 49, "玄關");
            retialSaleDbAdapter.create(11, "客戶格局需求", 66, "客廳");
            retialSaleDbAdapter.create(11, "客戶格局需求", 84, "餐廳");
            retialSaleDbAdapter.create(11, "客戶格局需求", 85, "主臥室");
            retialSaleDbAdapter.create(11, "客戶格局需求", 86, "書房");
            retialSaleDbAdapter.create(11, "客戶格局需求", 87, "更衣間");
            retialSaleDbAdapter.create(11, "客戶格局需求", 88, "小孩房");
            retialSaleDbAdapter.create(11, "客戶格局需求", 89, "長輩房");
            retialSaleDbAdapter.create(11, "客戶格局需求", 90, "廚房");
            retialSaleDbAdapter.create(11, "客戶格局需求", 91, "和室或休憩區");
            retialSaleDbAdapter.create(11, "客戶格局需求", 92, "現成傢俱");
            retialSaleDbAdapter.create(11, "客戶格局需求", 93, "其他");
            retialSaleDbAdapter.create(12, "營修項目", 33, "無資料");
            retialSaleDbAdapter.create(13, "區域項目", 100, "無資料");
            
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean getUser()
    {
        Cursor userCursor = retialSaleDbAdapter.getAllUser();
        if (userCursor != null)
        {
            int count = userCursor.getCount();
            userCursor.close();
            if (count <= 0)
            {
                return setUser();
            }
            else
            {
                return true;
            }
        }
        else
        {
            Log.d(TAG, "user cursor is null ");
            return setUser();
        }
    }
    
    private boolean setUser()
    {

        try
        {
            retialSaleDbAdapter.create(1, "Admin", 23, 40, "系統管理者", "斗六店");
            retialSaleDbAdapter.create(24, "測試使用者", 22, 2, "設計師兼任店長", "文心店");
            retialSaleDbAdapter.create(25, "王小明", 22, 2, "設計師兼任店長", "文心店");
            retialSaleDbAdapter.create(28, "王小至", 22, 2, "設計師", "文心店");
            retialSaleDbAdapter.create(26, "李大華", 41, 3, "設計師", "環中店");
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean getAppUser()
    {
        Cursor appUserCursor = retialSaleDbAdapter.getAllAppUser();
        if (appUserCursor != null)
        {
            int count = appUserCursor.getCount();
            appUserCursor.close();
            if (count <= 0)
            {
                return setAppUser();
            }
            else
            {
                return true;
            }
        }
        else
        {
            Log.d(TAG, "app user cursor is null ");
            return setAppUser();
        }
    }
    
    private boolean setAppUser()
    {

        try
        {
            retialSaleDbAdapter.create("46A", "46A", 0, 22);
            retialSaleDbAdapter.create("J9A", "J9A", 0, 23);
            retialSaleDbAdapter.create("48A", "48A", 0, 41);
            retialSaleDbAdapter.create("47A", "47A", 0, 42);
            retialSaleDbAdapter.create("J6A", "J6A", 0, 64);
            retialSaleDbAdapter.create("45A", "45A", 0, 65);
            retialSaleDbAdapter.create("L3A", "L3A", 0, 96);
            retialSaleDbAdapter.create("P5A", "P5A", 0, 102);
            retialSaleDbAdapter.create("05A", "05A", 0, 103);
            retialSaleDbAdapter.create("08A", "08A", 0, 104);
            retialSaleDbAdapter.create("09A", "09A", 0, 105);
            retialSaleDbAdapter.create("12A", "12A", 0, 106);
            retialSaleDbAdapter.create("25A", "25A", 0, 107);
            retialSaleDbAdapter.create("B2A", "B2A", 0, 108);
            retialSaleDbAdapter.create("B9A", "B9A", 0, 109);
            retialSaleDbAdapter.create("J5A", "J5A", 0, 110);
            retialSaleDbAdapter.create("P3A", "P3A", 0, 111);
            
            retialSaleDbAdapter.create("62R", "62R", 0, 127);
            retialSaleDbAdapter.create("63R", "63R", 0, 128);
            retialSaleDbAdapter.create("65R", "65R", 0, 129);
            retialSaleDbAdapter.create("66R", "66R", 0, 130);
            retialSaleDbAdapter.create("71R", "71R", 0, 131);
            retialSaleDbAdapter.create("23R", "23R", 0, 132);
            retialSaleDbAdapter.create("36R", "36R", 0, 133);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {
            case Utility.SHOW_WAITING_DIALOG:
                Log.d(TAG, "show waiting dialog ");
                progressDialog = ProgressDialog.show(LoginActivity.this, Utility.SPACE_STRING,
                        getResources().getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            }
        }
    };
    
    private void showToast(String showString)
    {
        Toast.makeText(this, showString, Toast.LENGTH_SHORT).show();
    }
    
    private void saveData(String account, String password, int group)
    {
        SharedPreferences settings = getSharedPreferences(Utility.LoginField.APP_DATA, Utility.DEFAULT_ZERO_VALUE);
        settings.edit().putString(Utility.LoginField.APP_ACCOUNT, account).putString(Utility.LoginField.APP_PASSWORD, password)
                .putInt(Utility.LoginField.APP_GROUP, group).commit();
    }
    
    private boolean hadLogin()
    {
        SharedPreferences settings = getSharedPreferences(Utility.LoginField.APP_DATA, Utility.DEFAULT_ZERO_VALUE);
        String id = settings.getString(Utility.LoginField.APP_ACCOUNT, "");

        if (id != null && !id.equals(Utility.SPACE_STRING))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
