package com.he.lawerassistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.he.lawerassistant.http.RetrofitManager;
import com.he.lawerassistant.http.bean.ResponseBean;
import com.he.lawerassistant.service.CommonService;
import com.he.lawerassistant.utils.KLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.list_view)
    ListView listView;

    Context context;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String url = "file:///android_asset/www/";
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        toolbar.setTitle("律师助手");
        initData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", url+list.get(position)+".html");
                startActivity(intent);
                check_update();
            }
        });


    }

    private void check_update() {
        CommonService service = new RetrofitManager(this).createService(CommonService.class);
//        Map<String,Integer> map = new HashMap<>();
//        map.put("version_code",12);
        Call<ResponseBean> call = service.getChuKuXiaoXi(12);
        call.enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                KLog.e(response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {
                KLog.e(t.toString());
                t.printStackTrace();
            }
        });
    }


    private void initData(){
        list.add("宪法");
        list.add("民法总则");
        list.add("民法通则");
        list.add("刑事诉讼法");
        list.add("合同法");
        list.add("劳动合同法");
        list.add("劳动法");
        list.add("侵权责任法");
        list.add("公司法");
        list.add("刑法（2015修正）");
        list.add("合伙企业法");
        list.add("婚姻法");
        list.add("治安管理处罚法");
        list.add("消费者权益保护法");
        list.add("物权法");
        list.add("立法法");
        list.add("网络安全法");
        list.add("著作权法（2010年修正）");
        list.add("道路交通安全法");
        list.add("食品安全法");
        list.add("工伤保险条例");
    }
}
