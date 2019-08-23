package com.cqkj.snail.sell.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cqkj.publicframework.beans.CallBackObject;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.config.ResultCode;
import com.cqkj.snail.requestdata.RequestManager;
import com.cqkj.snail.requestdata.RequestUrl;
import com.cqkj.snail.sell.adapter.TruckPicAdapter;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.widget.NoScrollGridView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import win.smartown.android.library.certificateCamera.CameraActivity;
import win.smartown.android.library.certificateCamera.TruckPic;
import win.smartown.android.library.certificateCamera.twgallery.PicPreViewActivity;

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
    // 上传按钮
    @BindView(R.id.btn_upload)
    Button btnUpload;

    private final String[] picNameArr = new String[]{"车辆铭牌", "左前45度", "车身正面照",
            "车辆牌照", "驾驶室"};
//    private final String[] picNameArr = new String[]{"车辆铭牌", "左前45度", "车身正面照",
//            "车辆牌照", "右后45度", "驾驶室", "车辆前轮", "车辆后轮", "前围骨架", "玻璃及编号",
//            "发动机(正)", "发动机(左)", "发动机(右)", "发动机(下)", "变速箱(上)", "车身骨架",
//            "传动轴", "后驱动桥", "仪表台", "车辆登记证书", "行驶证", "上装左", "上装右", "上装后"};
    // 必输数量
    private final int mustNum = 3;
    // 必输列表
    ArrayList<TruckPic> mustTruckPics;
    // 非必输列表
    ArrayList<TruckPic> unmustTruckPics;
    private TruckPicAdapter mustTruckPicAdapter;
    private TruckPicAdapter unmustTruckPicAdapter;

    // 当前操作的列表
    private ArrayList<TruckPic> currentTruckPics;
    // 上传列表
    private ArrayList<TruckPic> uploadTruckPics = new ArrayList<>();
    // 当前操作的适配器
    private TruckPicAdapter currentTruckPicAdapter;
    // 当前操作的列表下标
    private int currentPosition;


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
        mustTruckPics = getIntent().getExtras().getParcelableArrayList("mustTruckPics");
        unmustTruckPics = getIntent().getExtras().getParcelableArrayList("unmustTruckPics");
        if (mustTruckPics == null) {
            mustTruckPics = getTruckPics(1);
        }
        if (unmustTruckPics == null) {
            unmustTruckPics = getTruckPics(0);
        }
        setBtnStatus();
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

    @Override
    protected void initListener() {
        super.initListener();
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_upload:

                // 清空上传列表
                uploadTruckPics.clear();
                // 批量上传照片
                List<String> paths = new ArrayList<>();
                // 添加必输列表
                paths.addAll(structurePicPaths(mustTruckPics));
                // 添加非必输列表
                paths.addAll(structurePicPaths(unmustTruckPics));
                // 如果所以照片都上传了，则不启动上传
                if (paths.isEmpty()){
                    ToastUtil.showShort(this, R.string.all_pic_uploaded);
                    return;
                }
                showDialog("");
                RequestManager.getRequestManager().postFile(RequestUrl.post_file, paths,
                        this);
                break;
        }
    }

    @Override
    public void onSuccess(int flag, CallBackObject obj) throws ParseException {
        super.onSuccess(flag, obj);
        switch (flag) {
            case RequestUrl.post_file:
                String attachId = (String) obj.getObject();
                if (!TextUtils.isEmpty(attachId)) {
                    String[] idArr = attachId.split(",");
                    for (int i = 0; i < uploadTruckPics.size(); i++) {
                        uploadTruckPics.get(i).setImgId(idArr[i]);
                    }

                    ToastUtil.showShort(this, R.string.upload_success);
                    Intent data = new Intent();
//                    data.putExtra("attachId", attachId);
                    data.putExtra("firstPic", mustTruckPics.get(0).getImgPath());
                    data.putParcelableArrayListExtra("mustTruckPics", mustTruckPics);
                    data.putParcelableArrayListExtra("unmustTruckPics", unmustTruckPics);
                    setResult(ResultCode.UPLOAD_PIC, data);
                    finish();
                } else {
                    ToastUtil.showShort(this, R.string.upload_fail);
                }
                break;
        }
    }

    @Override
    public void onFailure(int flag, String message) {
        super.onFailure(flag, message);
        ToastUtil.showShort(this, message);
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
                    truckPic.setMustFlag(1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    String path = "";
                    for (LocalMedia media : selectList) {
                        if (media.isCut()) {
                            path = media.getCutPath();
                        } else {
                            path = media.getPath();
                        }
                    }
                    TruckPic truckPic = currentTruckPics.get(currentPosition);
                    truckPic.setImgPath(path);
                    truckPic.setImgId("");
                    currentTruckPicAdapter.notifyDataSetChanged();
                    break;
            }
        } else if (resultCode == CameraActivity.RESULT_CODE || resultCode == PicPreViewActivity.RESULT_CODE_PREVIEW) {
            // 拍照后返回 或者 预览返回
            if (data != null) {
                setResultStatus(data);
            }
        }
        setBtnStatus();
    }

    /**
     * 操作图片后的处理
     *
     * @param data
     */
    private void setResultStatus(Intent data) {
        // 获取是否是必须列表
        int must = data.getExtras().getInt("must");
        ArrayList<TruckPic> truckPics = data.getExtras().getParcelableArrayList("truckPics");
        // 当前操作的列表集合
        ArrayList<TruckPic> oldTruckPics = mustTruckPics;
        // 当前操作的列表适配器
        TruckPicAdapter truckPicAdapter = mustTruckPicAdapter;
        // 如果是非必须列表
        if (must == 1) {
            truckPicAdapter = unmustTruckPicAdapter;
            oldTruckPics = unmustTruckPics;
        }
        oldTruckPics.clear();
        oldTruckPics.addAll(truckPics);
        truckPicAdapter.notifyDataSetChanged();
    }

    /**
     * 设置当前操作内容
     *
     * @param currentTruckPics
     * @param currentTruckPicAdapter
     * @param currentPosition
     */
    public void setCurrent(ArrayList<TruckPic> currentTruckPics, TruckPicAdapter currentTruckPicAdapter, int currentPosition) {
        this.currentTruckPics = currentTruckPics;
        this.currentTruckPicAdapter = currentTruckPicAdapter;
        this.currentPosition = currentPosition;
    }

    /**
     * 设置提交按钮是否可用
     */
    private void setBtnStatus() {
        boolean can = true;
        // 但凡是必须上传列表中含有未选照片的数据，则不可上传
        for (TruckPic truckPic : mustTruckPics) {
            if (TextUtils.isEmpty(truckPic.getImgPath())) {
                can = false;
            }
        }
        btnUpload.setEnabled(can);
    }

    /**
     * 构建上传图片路径集合
     *
     * @param pics
     * @return
     */
    private List<String> structurePicPaths(List<TruckPic> pics) {
        List<String> list = new ArrayList<>();
        if (pics != null) {
            for (TruckPic truckPic : pics) {
                // 如果有图片路径，且没有图片id，说明该图片没有上传，则需添加到上传列表中
                if (!TextUtils.isEmpty(truckPic.getImgPath())
                        && TextUtils.isEmpty(truckPic.getImgId())) {
                    list.add(truckPic.getImgPath());
                    // 添加到上传列表中
                    uploadTruckPics.add(truckPic);
                }
            }
        }
        return list;
    }
}
