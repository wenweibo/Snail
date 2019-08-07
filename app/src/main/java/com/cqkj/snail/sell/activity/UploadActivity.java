package com.cqkj.snail.sell.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cqkj.snail.R;
import com.cqkj.snail.sell.adapter.TruckPicAdapter;
import com.fxkj.publicframework.activity.BaseTitleActivity;
import com.fxkj.publicframework.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import win.smartown.android.library.certificateCamera.TruckPic;

/**
 * 上传照片页面
 *
 * @author 闻维波
 * @since 2019/08/06 16:34
 */
public class UploadActivity extends BaseTitleActivity {
    // 必须车辆照片列表
    @BindView(R.id.ngv_must)
    NoScrollGridView ngvMust;
    // 非必须车辆标头
    @BindView(R.id.lin_un_must)
    LinearLayout lin_un_must;
    // 非必须车辆照片列表
    @BindView(R.id.ngv_un_must)
    NoScrollGridView ngvUnmust;

    private final String[] picNameArr = new String[]{"车辆铭牌", "左前45度", "车身正面照",
            "车辆牌照", "右后45度", "驾驶室", "车辆前轮", "车辆后轮", "前围骨架", "玻璃及编号",
            "发动机(正)", "发动机(左)", "发动机(右)", "发动机(下)", "变速箱(上)", "车身骨架",
            "传动轴", "后驱动桥", "仪表台", "车辆登记证书", "行驶证", "上装左", "上装右", "上装后"};
    // 必输数量
    private final int mustNum = 3;
    // 必输列表
    ArrayList<TruckPic> mustTruckPics;
    // 非必输列表
    ArrayList<TruckPic> unmustTruckPics;
    private TruckPicAdapter mustTruckPicAdapter;
    private TruckPicAdapter unmustTruckPicAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void initView() {
        super.initView();
        title_text.setText(R.string.upload_pic);
        back.setVisibility(View.VISIBLE);
        title_do.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        mustTruckPics = getTruckPics(1);
        unmustTruckPics = getTruckPics(0);
        mustTruckPicAdapter = new TruckPicAdapter(this, mustTruckPics, 0);
        ngvMust.setAdapter(mustTruckPicAdapter);

        unmustTruckPicAdapter = new TruckPicAdapter(this, unmustTruckPics, 1);
        ngvUnmust.setAdapter(unmustTruckPicAdapter);
        ngvUnmust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取必输 & 非必输 集合
     *
     * @param mustFlag
     * @return
     */
    private ArrayList<TruckPic> getTruckPics(int mustFlag) {
        ArrayList<TruckPic> truckPics = new ArrayList<>();
        for (int i = 0; i < picNameArr.length; i++) {
            // 如果是必输
            if (mustFlag == 1) {
                //则必输数量以内的添加至集合
                if (i < mustNum) {
                    TruckPic truckPic = new TruckPic();
                    truckPic.setSpecimenRes(getResources().getIdentifier("left_xiangji_" + (i + 1), "mipmap", getPackageName()));
                    truckPic.setTitle(picNameArr[i]);
                    truckPics.add(truckPic);
                } else {
                    break;
                }
            } else {
                // 否则，不是必输
                // 必输数量以内的不添加至集合
                if (i < mustNum) {
                    continue;
                } else {
                    // 必输以外的才添加至集合
                    TruckPic truckPic = new TruckPic();
                    truckPic.setSpecimenRes(getResources().getIdentifier("left_xiangji_" + (i + 1), "mipmap", getPackageName()));
                    truckPic.setTitle(picNameArr[i]);
                    truckPics.add(truckPic);
                }
            }
        }
        return truckPics;
    }

    /**
     * @param mustType
     */
    public void setUnSelect(int mustType) {
        TruckPicAdapter truckPicAdapter = mustTruckPicAdapter;
        List<TruckPic> list = mustTruckPics;
        if (mustType == 0) {
            truckPicAdapter = unmustTruckPicAdapter;
            list = unmustTruckPics;
        }
        for (TruckPic truckPic : list) {
            truckPic.setSelected(0);
        }
        truckPicAdapter.notifyDataSetChanged();
    }
}
