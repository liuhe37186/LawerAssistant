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

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.he.lawerassistant.http.RetrofitManager;
import com.he.lawerassistant.http.bean.ResponseBean;
import com.he.lawerassistant.http.bean.UpdateBean;
import com.he.lawerassistant.service.CommonService;
import com.he.lawerassistant.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

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

            }
        });
        check_update();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        check_update();
    }

    private void check_update() {
        CommonService service = new RetrofitManager(this).createService(CommonService.class);
        Call<ResponseBean<UpdateBean>> call = service.getChuKuXiaoXi(10);
        call.enqueue(new Callback<ResponseBean<UpdateBean>>() {
            @Override
            public void onResponse(Call<ResponseBean<UpdateBean>> call, Response<ResponseBean<UpdateBean>> response) {
                LogUtil.e("xxxx",response.toString());

                if(response.body() == null){
                    return;
                }

                LogUtil.e("xxx",response.body().getData());
                LogUtil.e("xxx",response.body().getCode());
                LogUtil.e("xxx",response.body().getMsg());
                String url = response.body().getData().getUrl();
                String content = response.body().getData().getContent();
                String isForce = response.body().getData().getIsForce();
                DownloadBuilder builder = AllenVersionChecker
                        .getInstance()
                        .downloadOnly(
                                UIData.create().setDownloadUrl(url).setTitle("版本更新").setContent(content + "\n\n" + "建议在wifi条件下更新")
                        );
                if(isForce.equals("1")){
                    builder.setForceUpdateListener(new ForceUpdateListener() {
                        @Override
                        public void onShouldForceUpdate() {
                            finish();
                        }
                    });
                }

                builder.excuteMission(context);
            }

            @Override
            public void onFailure(Call<ResponseBean<UpdateBean>> call, Throwable t) {
                LogUtil.e(t.toString());
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
