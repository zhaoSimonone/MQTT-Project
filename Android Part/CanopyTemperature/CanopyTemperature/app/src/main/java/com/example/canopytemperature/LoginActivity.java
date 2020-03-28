package com.example.canopytemperature;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * LoginActivity的作用：
 * 验证用户信息，进行登录，登录成功，则跳转到主界面
 * 本程序使用的是SQLite来存储数据，但是SQLite并不是唯一的 ，还有一些更简单的，比如使用SharedPreferences存储类
 */

public class LoginActivity extends AppCompatActivity {
    EditText editName, editPassword;
    Button buttonLogin;
    TextView textSignUp;
    UserOpenHelper helper;//UserOpenHelper继承自SQLiteOpenHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //该方法实现设置LoginActivity页面的ToolBar
        setToolBar();
        //实现沉浸式布局
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
        //初始化当前页面的一些控件，包括Button,EditText
        initControls();
        //创建UserOpenHelper对象，传入的参数通常为this，this指代这个类的引用
        helper=new UserOpenHelper(this);
        //Login页面，EditText控件的监听
        textSignUpListener();
        //Login页面，Button控件的监听
        buttonLoginListener();
    }

    /**
     * 注意观察登录的主界面，下面有一段文字：我没有账号，立即新建
     * 文字的点击事件便是由该监听实现的
     */
    public void textSignUpListener(){
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //第一个参数表示给数据库命名，这个名字可以任意取，MODE_PRIVATE表示只能被自己的应用程序访问
                Intent intent=new Intent(LoginActivity.this,SignUpAcitivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    /**
     * 用于点击登录按键的监听
     */
    public void buttonLoginListener(){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_value= editName.getText().toString();
                String pswd_value= editPassword.getText().toString();
                if (user_value.length()==0 || pswd_value.length()==0){
                    Toast.makeText(LoginActivity.this,"请输入用户名/密码",Toast.LENGTH_SHORT).show();
                }else{
                    SQLiteDatabase db=helper.getWritableDatabase();
                    /*
                        SQLite的查询语句，相当于是从数据库中查找是否有用户输入的用户名和用户密码
                        若用户输入的用户名和用户密码与记录在数据库中的用户名和用户密码相同，则验证成功，允许登录
                     */
                    String sql=String.format("select _NAME,_PSWD from users where _NAME='%s' and _PSWD='%s';",user_value,pswd_value);
                    Cursor cursor=db.rawQuery(sql,null);
                    if (cursor!=null && cursor.getCount()>0){
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        Toast.makeText(LoginActivity.this,"欢迎登陆，"+user_value+"!",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"用户名/密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initControls(){
        editName =(EditText)findViewById(R.id.login_name);
        editPassword =(EditText)findViewById(R.id.login_pswd);
        buttonLogin =(Button)findViewById(R.id.login_in);
        textSignUp =(TextView) findViewById(R.id.sign_in);
    }

    /**
     * 设置Toolbar
     */
    public void setToolBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设计隐藏标题
    }
}
