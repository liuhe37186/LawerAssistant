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

import com.he.lawerassistant.http.RetrofitClient;
import com.he.lawerassistant.http.bean.ResponseBean;
import com.he.lawerassistant.service.CommonService;
import com.he.lawerassistant.utils.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.list_view)
    ListView listView;

    Context context;

    String[] strings = {"宪法", "民法总则", "刑事诉讼法", "合同法"};
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String url = "file:///android_asset/www/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        toolbar.setTitle("律师助手");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", url+strings[position]+".html");
                startActivity(intent);
            }
        });

        Call<ResponseBean> call = RetrofitClient.getInstance().getRetrofit().create(CommonService.class).getChuKuXiaoXi("");
        call.enqueue(new Callback<ResponseBean>() {
            @Override
            public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
                KLog.e("xxxx",response);
            }

            @Override
            public void onFailure(Call<ResponseBean> call, Throwable t) {

            }
        });
    }
}
