package com.example.retailsale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;

public class Login extends Activity implements OnClickListener
{
    private static final String TAG = "Login";

    private EditText inputUserId;
    private EditText inputUserPassword;
    private ProgressDialog progressDialog;
    private RetialSaleDbAdapter retialSaleDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        retialSaleDbAdapter = new RetialSaleDbAdapter(Login.this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        boolean hadLogin = Utility.hadLogin(Login.this);

        if (hadLogin) startManageFragment();
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

            if (id.equals(""))
            {
                id = "A123456";
                password = "1qaz@wsx";
            }
            handler.sendEmptyMessage(Utility.SHOW_WAITING_DIALOG);
            login(id, password);
//            startManageFragment();
            break;
        }
    }

    private void startManageFragment()
    {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
//		Login.this.finish();
    }

    private void login(final String id, final String password)
    {
        HttpManager httpManager = new HttpManager();
        httpManager.login(Login.this, id, password, new GetLoginListener()
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
                            if (message.equals(Login.this.getResources().getString(R.string.login_successful)))
                            {
                                Log.d(TAG, "Message is successfully");
                                Utility.saveData(Login.this, id, password, userSerial, userGroup, loginKey);
                                retialSaleDbAdapter.open();
                                getDataOption();
                                getUser();
                                startManageFragment();
                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            }
                            else
                            {
                                Log.d(TAG, "Message is failed");
                                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                            }
                        }
                        else
                        {
                            Log.d(TAG, "value is null or size less/equal than 0");
                            handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                        }
                    }
                    else
                    {
                        Log.d(TAG, "userInfo is null");
                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                    }
                }
                else
                {
                    Log.d(TAG, "Login failed");
                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
                }
            }
        });
    }
    
    private void getDataOption()
    {  
        Cursor optionTypeCursor = retialSaleDbAdapter.getAllOption();
        if (optionTypeCursor != null)
        {
            int count = optionTypeCursor.getCount();
            optionTypeCursor.close();
            if (count <= 0)
            {
                setDataOption();
            }
        }
        else
        {
            Log.d(TAG, "option cursor is null ");
            setDataOption();
        }
    }
    
    private void setDataOption()
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
        retialSaleDbAdapter.create(4, "客戶訊息來源", 74, "電台撥放");
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
    }
    
    private void getUser()
    {
        Cursor userCursor = retialSaleDbAdapter.getAllUser();
        if (userCursor != null)
        {
            int count = userCursor.getCount();
            userCursor.close();
            if (count <= 0)
            {
                setUser();
            }
        }
        else
        {
            Log.d(TAG, "user cursor is null ");
            setUser();
        }
    }
    
    private void setUser()
    {
        retialSaleDbAdapter.create(1, "Admin", 23, 40, "系統管理者",  "斗六店");
        retialSaleDbAdapter.create(24, "測試使用者", 22, 2, "設計師兼任店長",  "文心店");
        retialSaleDbAdapter.create(25, "王小明", 22, 2, "設計師兼任店長",  "文心店");
        retialSaleDbAdapter.create(28, "王小至", 22, 2, "設計師",  "文心店");
        retialSaleDbAdapter.create(26, "李大華", 41, 3, "設計師",  "環中店");
    }


//    private void getDataOption()
//    {
//        HttpManager httpManager = new HttpManager();
//        httpManager.getDataOptions(Login.this, new GetDataOptionListener()
//        {
//            @Override
//            public void onResult(Boolean isSuccess, GsonDataOption dataOption)
//            {
//                if (isSuccess)
//                {
//                    if (dataOption != null)
//                    {
//                        List<DataOption> value = dataOption.getValue();
//                        if (value != null)
//                        {
//                            RetialSaleDbAdapter retialSaleDbAdapter = new RetialSaleDbAdapter(Login.this);
//                            retialSaleDbAdapter.open();
//                            retialSaleDbAdapter.deleteAllOption();
//                            for (int i = 0; i < value.size(); i++)
//                            {
//                                int optSerial = value.get(i).getOptSerial();
//                                String optName = value.get(i).getOptName();
//                                String typeName = value.get(i).getTypeName();
//                                boolean optLock = value.get(i).getOptLock();
//
//                                Log.d(TAG, "optSerial : " + optSerial + " optName : " + optName + " typeName : "
//                                        + typeName + " optLock : " + optLock);
//                                retialSaleDbAdapter.create(-1, typeName, optSerial, optName);
//                            }
//                            retialSaleDbAdapter.close();
////                            startManageFragment();
//                        }
//                        else
//                        {
//                            Log.d(TAG, "value is null");
//                        }
//                        getUserList();
//                    }
//                    else
//                    {
//                        Log.d(TAG, "dataOption is null");
//                        handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                    }
//                }
//                else
//                {
//                    Log.d(TAG, "Get data option failed");
//                    handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//                }
////                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//            }
//        });
//    }
    
//    private void getUserList()
//    {
//        HttpManager httpManager = new HttpManager();
//        httpManager.getUserListByGroup(Login.this, new GetUsetListByGroupListener()
//        {
//            @Override
//            public void onResult(Boolean isSuccess, GsonUserByGroup user)
//            {
//                if (isSuccess)
//                {
//                    if (user != null)
//                    {
//                        if (user.getValue() != null)
//                        {
//                            int size = user.getValue().size();
//                            Log.d(TAG, "user list size : " + size);
//                            
//                            int userSerial, userGroup, userType;
//                            String userName, userGroupNm, userTypeNm;
//                            
//                            RetialSaleDbAdapter retialSaleDbAdapter = new RetialSaleDbAdapter(Login.this);
//                            retialSaleDbAdapter.open();
//                            retialSaleDbAdapter.deleteAllUser();
//                            
//                            for (int i = 0; i < size; i++)
//                            {
//                                userSerial = user.getValue().get(i).getUserSerial();
//                                userName = user.getValue().get(i).getUserName();
//                                userGroup = user.getValue().get(i).getUserGroup();
//                                userType = user.getValue().get(i).getUserType();
//                                userGroupNm = user.getValue().get(i).getUserGroupNm();
//                                userTypeNm = user.getValue().get(i).getUserTypeNm();
//                                Log.d(TAG, "userSerial : " + userSerial + " userName : " + userName
//                                        + " userGroup : " + userGroup + " userType : " + userType
//                                        + " userGroupNm : " + userGroupNm + " userTypeNm : " + userTypeNm);
//                                retialSaleDbAdapter.create(userSerial, userName, userGroup, userType, userGroupNm, userTypeNm);
//                            }
//                            retialSaleDbAdapter.close();
//                            startManageFragment();
//                        }
//                        else
//                        {
//                            Log.d(TAG, "value is null or size less/equal than 0");
//                        }
//                    }
//                    else
//                    {
//                        Log.d(TAG, "user list is null");
//                    }
//                }
//                else
//                {
//                    Log.d(TAG, "Get user list failed");
//                }
//                handler.sendEmptyMessage(Utility.DISMISS_WAITING_DIALOG);
//            }
//        }, Utility.getCreatorGroup(this));
//    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {
            case Utility.SHOW_WAITING_DIALOG:
                Log.d(TAG, "show waiting dialog ");
                progressDialog = ProgressDialog.show(Login.this, Utility.SPACE_STRING,
                        getResources().getString(R.string.loading));
                break;
            case Utility.DISMISS_WAITING_DIALOG:
                Log.d(TAG, "dismiss dialog ");
                if (progressDialog != null) progressDialog.dismiss();
                break;
            }
        }
    };
}
