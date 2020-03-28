package com.example.canopytemperature;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * SignUpActivity的作用：
 * 注册用户信息，主要是用户名以及用户密码，主要是针对新用户进行注册
 */
public class SignUpAcitivity extends AppCompatActivity {
    ImageButton ib;
    EditText name ,password, rePassword;
    Button sign;
    UserOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //设置SignUp页面的ToolBar
        setToolBar();
        //TODO 为了实现沉浸式布局
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
        sign=(Button)findViewById(R.id.sign);
        initControls();


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_value=name.getText().toString();
                String pswd_value= password.getText().toString();
                String repswd_value= rePassword.getText().toString();
                if (name_value.length()==0 || pswd_value.length()==0){
                    Toast.makeText(SignUpAcitivity.this,"请输入用户名/密码",Toast.LENGTH_SHORT).show();
                }else
                if(pswd_value.equals(repswd_value)){
                    //链接服务器，添加数据
                    SQLiteDatabase db=helper.getWritableDatabase();
                    //判断是否注册过
                    String find=String.format("select _NAME from users where _NAME='%s';",name_value);
                    String sql= String.format("insert into users(_NAME,_PSWD) values('%s','%s');"
                            ,name_value,pswd_value);
                    Cursor cursor=db.rawQuery(find,null);
                    if (cursor!=null && cursor.getCount()>0){
                        Toast.makeText(SignUpAcitivity.this,"该用户已被注册",Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            db.execSQL(sql);
                            Toast.makeText(SignUpAcitivity.this, "注册成功，欢迎使用", Toast.LENGTH_SHORT).show();
                            db.close();
                            Intent intent = new Intent(SignUpAcitivity.this, MainActivity.class);
                            startActivity(intent);
                            SignUpAcitivity.this.finish();

                        } catch (Exception e) {
                            Toast.makeText(SignUpAcitivity.this, "添加失败，请检查输入", Toast.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    Toast.makeText(SignUpAcitivity.this,"两次输入不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initControls(){
        sign=(Button)findViewById(R.id.sign);
        name=(EditText)findViewById(R.id.name);
        password =(EditText)findViewById(R.id.pswd);
        rePassword =(EditText)findViewById(R.id.repswd);
        helper=new UserOpenHelper(this);
    }

    public void setToolBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_sign);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设计隐藏标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置显示返回键
        //返回按键的监控事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpAcitivity.this,LoginActivity.class);
                startActivity(intent);
                SignUpAcitivity.this.finish();
            }
        });
    }
}
