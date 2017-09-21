package com.siqi.bmob;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 主页
 * <p>
 * Bmob 后端云 全方位一体化的后端服务平台
 * 无需再造应用后端服务 轻松拥有开发中需要的各种后端能力
 * <p>
 * 插入  修改  删除数据  可在bmob后台查看
 */
public class MainActivity extends BaseActivity {

    @BindView(value = R.id.tv_show)
    TextView showView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化  必须在setContentView之后
        ButterKnife.bind(this);

        // showView.setTranslationZ(100);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.tv_insert, R.id.tv_get, R.id.tv_modify, R.id.tv_delete})
    public void dataDeal(View view) {

        switch (view.getId()) {

            // 添加
            case R.id.tv_insert:
                addData();
                break;

            // 获取
            case R.id.tv_get:
                getData();
                break;

            // 修改
            case R.id.tv_modify:
                modifyData();
                break;

            // 删除
            case R.id.tv_delete:
                deleteData();
                break;
        }
    }

    /**
     * 删除数据
     */
    private void deleteData() {
        final Person p2 = new Person();
        p2.setObjectId("43fa5ac989"); // 设备ID

        p2.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "删除成功:" + p2.getUpdatedAt(), Toast.LENGTH_SHORT).show();

                    showView.setText("没数据啦！");
                } else {
                    Toast.makeText(getBaseContext(), "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /**
     * 修改数据
     */
    private void modifyData() {

        //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
        final Person p2 = new Person();
        p2.setAddress("北京朝阳");
        p2.update("43fa5ac989", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "更新成功" + p2.getUpdatedAt(), Toast.LENGTH_SHORT).show();

                    showView.setText("address:北京朝阳");
                } else {
                    Toast.makeText(getBaseContext(), "更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();

        bmobQuery.getObject("43fa5ac989", new QueryListener<Person>() {
            @Override
            public void done(Person object, BmobException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "查询成功", Toast.LENGTH_SHORT).show();

                    showView.setText("查询的id为：" + "43fa5ac989");
                } else {
                    Toast.makeText(getBaseContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 插入数据
     */
    private void addData() {
        Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");

        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {

                if (e == null) {
                    Toast.makeText(getBaseContext(), "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();

                    showView.setText("name: lucky" + "&& address:北京海淀");
                } else {
                    Toast.makeText(getBaseContext(), "创建数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
