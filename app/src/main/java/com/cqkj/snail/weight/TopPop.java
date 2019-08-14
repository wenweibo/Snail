package com.cqkj.snail.weight;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqkj.publicframework.tool.CommonUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.DictInfo;
import com.cqkj.snail.sell.adapter.DictInfoAdapter;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.xuexiang.xui.widget.XUIWrapContentListView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 顶部字典弹窗
 *
 * @author 闻维波
 * @since 2019/08/14 16:57
 */
public class TopPop extends PopupWindow implements OnRangeChangedListener, View.OnClickListener {
    // 字典列表
    private List<DictInfoEntity> dictInfos;
    // 字典类型码
    private String dictCode;
    // 字典选中回调
    private DictInfoAdapter.DictInfoInterface dictInfoInterface;
    // 上下文
    private Context context;
    // 主视图
    private View pView;
    // 价格展示文本控件
    private TextView tv_price;
    // 价格选择器
    private RangeSeekBar range_seek_bar;

    public TopPop(Context context, List<DictInfoEntity> dictInfos, String dictCode,
                  DictInfoAdapter.DictInfoInterface dictInfoInterface) {
        super(context);
        this.context = context;
        this.dictInfos = dictInfos;
        this.dictCode = dictCode;
        this.dictInfoInterface = dictInfoInterface;
        init();
    }

    private void init() {
        // 初始化视图
        pView = LayoutInflater.from(context)
                .inflate(R.layout.top_pop, null);
        setContentView(pView);
        // 字典列表
        XUIWrapContentListView xlv_dictinfo = pView.findViewById(R.id.xlv_dictinfo);
        xlv_dictinfo.setAdapter(new DictInfoAdapter(context, dictInfos, dictInfoInterface,
                this));
        // 价格选择
        LinearLayout lin_price = pView.findViewById(R.id.lin_price);
        // 如果是选价格，则显示价格选择器
        if (DictInfo.PRICE.equals(dictCode)) {
            lin_price.setVisibility(View.VISIBLE);
        } else {
            // 否则，不显示
            lin_price.setVisibility(View.GONE);
        }
        // 价格区间选择器
        range_seek_bar = pView.findViewById(R.id.range_seek_bar);
        range_seek_bar.setProgress(0, range_seek_bar.getMaxProgress());
        range_seek_bar.setOnRangeChangedListener(this);
        tv_price = pView.findViewById(R.id.tv_price);

        Button btn_sure = pView.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);

        // 设置底部弹出属性
        setWidth(CommonUtil.getScreen(context)[0]);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(com.cqkj.publicframework.R.style.pop_top);
        CommonUtil.backgroundAlpha(context, 0.5f);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtil.backgroundAlpha(context, 1f);
            }
        });
    }

    @Override
    public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue,
                               boolean isFromUser) {
        // 如果最小价格等于0且最大价格等于最大值，则显示“不限”
        if (leftValue == range_seek_bar.getMinProgress()
                && rightValue == range_seek_bar.getMaxProgress()) {
            tv_price.setText("不限");
        } else if (leftValue != range_seek_bar.getMinProgress()
                && rightValue == range_seek_bar.getMaxProgress()) {
            // 如果最小价格不等于0且最大价格等于最大值，则显示leftValue以上
            tv_price.setText(Math.round(leftValue) + "万以上");
        } else if (leftValue == range_seek_bar.getMinProgress()
                && rightValue != range_seek_bar.getMaxProgress()) {
            // 如果最小价格等于0且最大价格不等于最大值，则显示rightValue以下
            tv_price.setText(Math.round(rightValue) + "万以下");
        } else {
            tv_price.setText(Math.round(leftValue) + "-" + Math.round(rightValue) + "万");
        }
    }

    @Override
    public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
    }

    @Override
    public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
    }

    @Override
    public void onClick(View v) {
        DictInfoEntity dictInfoEntity = new DictInfoEntity();
        String dictCodeName = tv_price.getText().toString();
        dictInfoEntity.setDictName(dictCodeName);
        dictInfoEntity.setDictCode(dictCodeName.replace("万", ""));
        dictInfoEntity.setParentId(DictInfo.PRICE);
        dictInfoInterface.onSelect(dictInfoEntity);
        dismiss();
    }
}
