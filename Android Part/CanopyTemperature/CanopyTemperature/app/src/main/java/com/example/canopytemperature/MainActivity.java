package com.example.canopytemperature;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * MainActivity对应的是主界面的节点，主要负责节点的跳转
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
        //得到toolbar的具体实例
        //Toolbar的标题栏是在AndroidMainfest.xml的application标签中的label属性指定的
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        //将Toolbar的实例toolbar传入这个方法就能既使用Toolbar，在外观和功能上又与Actionbar保持一致
        setSupportActionBar(toolbar);
        //设计隐藏标题，每一个app的页面的左上角都会有默认的标题，你可以把这句话去掉看看是什么效果
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 进行页面跳转
     * 这个方法不需要进行调用，由Android这个系统来帮我们进行调用，
     * 将该方法与当前页面的相应控件进行绑定，当点击该按键的时候就会执行该方法
     * @param view 当前页面的view
     */
    public void mainClicked(View view){
        switch(view.getId()){
            case R.id.node1_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                Intent node1Intent=new Intent(MainActivity.this,TemperatureActivity.class);
                startActivity(node1Intent);
                break;
            case R.id.node2_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                Intent node2Intent=new Intent(MainActivity.this,TemperatureActivity.class);
                startActivity(node2Intent);
                break;
            case R.id.node3_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.node4_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.node5_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.node6_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.node7_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            case R.id.node8_card:
                Toast.makeText(MainActivity.this, "You clicked card", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
