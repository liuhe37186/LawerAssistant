package com.he.lawerassistant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.corelibs.api.RetrofitManager;
import com.he.lawerassistant.R;
import com.he.lawerassistant.http.Constant;
import com.he.lawerassistant.http.bean.UpdateBean;
import com.he.lawerassistant.http.response.ResponseTransformer;
import com.he.lawerassistant.http.schedulers.SchedulerProvider;
import com.he.lawerassistant.http.service.CommonService;
import com.he.lawerassistant.utils.LogUtil;
import com.he.lawerassistant.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseCommonActivity {
    @BindView(R.id.list_view)
    ListView listView;

    Context context;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    String url = "file:///android_asset/www/";
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        toolbarTitle.setText(getResources().getString(R.string.app_name));
        initDrawer(toolbar);
        initData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", url + list.get(position) + ".html");
                startActivity(intent);

            }
        });
        navigation.setNavigationItemSelectedListener(new NavigationItemSelected());
        navigation.inflateHeaderView(R.layout.drawer_header);
        check_update();

    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                //更换
                actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            }
            drawerLayout.addDrawerListener(mDrawerToggle);
        }
    }


    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
//                case R.id.navigation_item_1:
//                    currentIndex = 0;
//                    menuItem.setChecked(true);
//                    currentFragment = new FristFragment();
//                    switchContent(currentFragment);
//                    return true;
                case R.id.navigation_item_2:
                    Intent search = new Intent(context, OnlineLawsActivity.class);
                    startActivity(search);
                    return true;
                case R.id.navigation_item_3:
                    Intent intent = new Intent(context, AboutActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_item_night:

                    SharedPreferencesUtil.setBoolean(mActivity, Constant.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    return true;
                case R.id.navigation_item_day:

                    SharedPreferencesUtil.setBoolean(mActivity, Constant.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    return true;
                default:
                    return true;
            }
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawers();
    }

    private void check_update() {
        CommonService service = RetrofitManager.getInstance().create(CommonService.class);

        service.getChuKuXiaoXi(10)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<UpdateBean>() {
                    @Override
                    public void accept(UpdateBean updateBean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


        /*Call<ResponseBean<UpdateBean>> call = service.getChuKuXiaoXi(10);
        call.enqueue(new Callback<ResponseBean<UpdateBean>>() {
            @Override
            public void onResponse(Call<ResponseBean<UpdateBean>> call, Response<ResponseBean<UpdateBean>> response) {
                LogUtil.e("xxxx", response.toString());

                LogUtil.e("xxx", response.body().getData());
                LogUtil.e("xxx", response.body().getCode());
                LogUtil.e("xxx", response.body().getMsg());
                String url = response.body().getData().getUrl();
                String content = response.body().getData().getContent();
                String isForce = response.body().getData().getIsForce();
                DownloadBuilder builder = AllenVersionChecker
                        .getInstance()
                        .downloadOnly(
                                UIData.create().setDownloadUrl(url).setTitle("版本更新").setContent(content + "\n\n" + "建议在wifi条件下更新")
                        );
                if (isForce.equals("1")) {
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
        });*/
    }


    private void initData() {
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
        list.add("law01.01");
    }
}
