package com.example.dm.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.beans.AppUser;
import com.example.dm.myapplication.beans.ComUserPostInfo;
import com.example.dm.myapplication.com.ComPostTopicActivity;
import com.example.dm.myapplication.customviews.xlistview.XListView;
import com.example.dm.myapplication.customviews.xlistview.adapter.ComAppAdapter;
import com.example.dm.myapplication.utiltools.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by dm on 16-3-29.
 * 第二个页面
 */
public class SecondFragment extends Fragment implements XListView.IXListViewListener {
    private static String liuDeHuaUrl = "http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/16.jpg?v=2";
    private static String laoWaiUrl = "http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/18.jpg?v=2";
    private static String liuYanUrl = "http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/12.jpg?v=2";
    private static String zhiLingUrl = "http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/2.jpg?v=2";
    private static String fanBingUrl = "http://www.faceplusplus.com.cn/wp-content/themes/faceplusplus/assets/img/demo/1.jpg?v=2";

    private ImageView mPostNewImv;
    private XListView mListView = null;
    private ProgressBar mProgressBar;

    private Handler handler = null;
    private ComAppAdapter mComAppAdapter;
    private List<ComUserPostInfo> mList = new ArrayList<>();

    private String currentTimeStr = DateUtil.getCurrentTimeStr();

    private View view;

    private ArrayList<String> stringArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg2, container, false);

        initView();
        generateData1();
        dealEvents();   // 事件处理: gridview item的点击事件

        return view;
    }


    private void initView() {
        mPostNewImv = (ImageView) view.findViewById(R.id.com_post_new_rout);
        mListView = (XListView) view.findViewById(R.id.lv_main);
        mProgressBar = (ProgressBar) view.findViewById(R.id.com_loading_prbar);

        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(SecondFragment.this);
        handler = new Handler();

        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        stringArrayList = new ArrayList<>();
        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
        stringArrayList.add(liuYanUrl);
        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
        stringArrayList.add(liuYanUrl);
        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
        stringArrayList.add(laoWaiUrl);
        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
        stringArrayList.add(fanBingUrl);
        stringArrayList.add(liuDeHuaUrl);
    }

    private void dealEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击了第" + position + "个list.", Toast.LENGTH_SHORT).show();
            }
        });

        mPostNewImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUser appUser = BmobUser.getCurrentUser(getActivity(), AppUser.class);
                if (appUser == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), ComPostTopicActivity.class));
                }

//                String currentTime = DateUtil.getCurrentTimeStr();
//                Toast.makeText(getActivity(), "发表", Toast.LENGTH_SHORT).show();
//                ComUserPostInfo comUserPostInfo = new ComUserPostInfo();
//
//                if (appUser != null) {
//                    comUserPostInfo.setUserNameStr(appUser.getUsername());
//                    comUserPostInfo.setUserHeadImgUrl(appUser.getUserAvatarUrl());
//                    comUserPostInfo.setUserNickNameStr(appUser.getUserNickName());
//                    comUserPostInfo.setUserTimeStr(currentTime);
//                    comUserPostInfo.setUserContentStr("事在人为休言万般皆是命，境由心造退一步天地宽!");
//                    comUserPostInfo.setUserImageUrlList(stringArrayList);
//                    comUserPostInfo.setUserRepostCounter(10);
//                    comUserPostInfo.setUserCommentCounter(25);
//                    comUserPostInfo.setUserLikeCounter(123);
//                    comUserPostInfo.save(getActivity(), new SaveListener() {
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(getActivity(), "发表成功!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            Toast.makeText(getActivity(), "发表失败, 请稍后再试!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getActivity(), "请先登录!",
//                            Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    /**
     * 测试数据：生成静态数据
     */
    private void generateData1() {
        BmobQuery<ComUserPostInfo> postInfoBmobQuery = new BmobQuery<>();
        postInfoBmobQuery.addWhereLessThanOrEqualTo("createdAt", new BmobDate(new Date()));
        postInfoBmobQuery.findObjects(getActivity(), new FindListener<ComUserPostInfo>() {
            @Override
            public void onSuccess(List<ComUserPostInfo> list) {
                for (ComUserPostInfo comUserPostInfo : list) {
                    mList.add(comUserPostInfo);
                }

//        Log.i("LOG", "mList.toString" + mList.toString());
                mComAppAdapter = new ComAppAdapter(getActivity());
                mComAppAdapter.setData(mList);
                mListView.setAdapter(mComAppAdapter);
                mProgressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onError(int i, String s) {
                mProgressBar.setVisibility(ProgressBar.GONE);

            }
        });

        Log.i("LOG", "mList.toString" + mList.toString());
    }

//    /**
//     * 测试数据：生成静态数据
//     */
//    private void generateData() {
//
//        // 添加数据，含一张图片的情况
//        ComUserPostInfo mComUserPostInfo0 = new ComUserPostInfo();
//
//        mComUserPostInfo0.setUserNickNameStr("乔布斯");
//        mComUserPostInfo0.setUserHeadImgUrl(liuDeHuaUrl);
//        mComUserPostInfo0.setUserTimeStr(currentTimeStr);
//        mComUserPostInfo0.setUserContentStr("事在人为休言万般皆是命，境由心造退一步天地宽!");
//        mComUserPostInfo0.setUserCommentCounter(999);
//        mComUserPostInfo0.setUserLikeCounter(9999);
//
//
//        List<String> photoStringLists = new ArrayList<>();
//        photoStringLists.add(liuDeHuaUrl);
//        mComUserPostInfo0.setUserImageUrlList(photoStringLists);
//
//        mList.add(mComUserPostInfo0);
//
//        // 使用构造器初始化
//        ArrayList<String> imgsArrayList = new ArrayList<>();
//        imgsArrayList.add(liuYanUrl);
//        imgsArrayList.add(fanBingUrl);
//        imgsArrayList.add(zhiLingUrl);
//
//        ComUserPostInfo mComUserPostInfo00 = new ComUserPostInfo("", liuYanUrl, "sslk",
//                currentTimeStr, "你好，明天", imgsArrayList, 1, 15, 22);
//
//        mList.add(mComUserPostInfo00);
//
//        // 添加数据，含2张图片的情况
//        ComUserPostInfo mComUserPostInfo1 = new ComUserPostInfo();
//
//        mComUserPostInfo1.setUserNickNameStr("马云");
//        mComUserPostInfo1.setUserHeadImgUrl(fanBingUrl);
//        mComUserPostInfo1.setUserTimeStr("2015-05-13 23:59:59");
//        mComUserPostInfo1.setUserContentStr("淘宝不是假货多，是你太贪" +
//                "“二十五块钱就想买一个劳力士手表，这是不可能的，这是你自己太贪了”，阿里巴巴董事局主席马云将淘宝上假货泛滥归咎于易受骗和“贪心”的消费者。这位首富还有一句名言“空气是不行的，水是不行的，手机再好又有什么用呢？”把雷布斯当场给噎住了。");
//        mComUserPostInfo1.setUserRepostCounter(100);
//        mComUserPostInfo1.setUserCommentCounter(1000);
//        mComUserPostInfo1.setUserLikeCounter(19999);
//
//        ArrayList<String> imgsArrayList2 = new ArrayList<>();
//        imgsArrayList2.add(liuYanUrl);
//        imgsArrayList2.add(fanBingUrl);
//        imgsArrayList2.add(zhiLingUrl);
//        mComUserPostInfo1.setUserImageUrlList(imgsArrayList2);
//        mList.add(mComUserPostInfo1);
//
//        // 添加数据，含5张图片的情况
//        ComUserPostInfo mComUserPostInfo3 = new ComUserPostInfo();
//
//        mComUserPostInfo3.setUserNickNameStr("李彦宏");
//        mComUserPostInfo3.setUserHeadImgUrl(zhiLingUrl);
//        mComUserPostInfo3.setUserTimeStr("2015-05-15 21:59:49");
//        mComUserPostInfo3.setUserContentStr("“与马云每天睡懒觉不同，我每天5点多就醒了，我很着急。”在百度CEO李彦宏，机会多是一种负担，企业一定要学会放弃，要把目光聚焦在自己最喜爱、能力范围内最擅长的领域。");
//
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(laoWaiUrl);
//        arrayList.add(fanBingUrl);
//        arrayList.add(laoWaiUrl);
//        arrayList.add(liuDeHuaUrl);
//        arrayList.add(liuYanUrl);
//        mComUserPostInfo3.setUserImageUrlList(arrayList);
//        mList.add(mComUserPostInfo3);
//
//        // 添加数据，含9张图片的情况
//        ComUserPostInfo mComUserPostInfo4 = new ComUserPostInfo();
//
//        mComUserPostInfo4.setUserNickNameStr("罗永浩");
//        mComUserPostInfo4.setUserHeadImgUrl("http://img.blog.csdn.net/20160405180856569");
//        mComUserPostInfo4.setUserTimeStr("2015-05-16 23:58:45");
//        mComUserPostInfo4.setUserContentStr("我特别反感有的手机厂商在新品上市时定一个高价，之后很快又会降价的做法。我们的这个价格会一直坚持整个产品周期，除非下一代产品上市了，前一代需要清理库存了，才有可能降价销售。");
//        mComUserPostInfo4.setUserCommentCounter(1100000);
//        mComUserPostInfo4.setUserLikeCounter(1101101);
//
//        ArrayList<String> stringArrayList = new ArrayList<>();
//        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
//        stringArrayList.add(liuYanUrl);
//        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
//        stringArrayList.add(liuYanUrl);
//        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
//        stringArrayList.add(laoWaiUrl);
//        stringArrayList.add("http://img.blog.csdn.net/20160405180856569");
//        stringArrayList.add(fanBingUrl);
//        stringArrayList.add(liuDeHuaUrl);
//
//        mComUserPostInfo4.setUserImageUrlList(stringArrayList);
//        mList.add(mComUserPostInfo4);
//    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateData1();

                mComAppAdapter = new ComAppAdapter(getActivity());
                mComAppAdapter.setData(mList);
                mListView.setAdapter(mComAppAdapter);
                onLoad();
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateData1();
                mComAppAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);

    }

    /**
     * 监听数据的改变，加载数据
     */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(currentTimeStr);
    }
}
