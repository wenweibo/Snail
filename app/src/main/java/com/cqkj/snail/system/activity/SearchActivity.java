package com.cqkj.snail.system.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.tool.SpUtils;
import com.cqkj.publicframework.tool.ToastUtil;
import com.cqkj.snail.R;
import com.cqkj.snail.buy.adapter.ConditionAdapter;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.cqkj.snail.config.ResultCode;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

import java.util.List;

import butterknife.BindView;

/**
 * 搜索页面
 *
 * @author 闻维波
 * @since 2019/08/26 15:13
 */
public class SearchActivity extends BaseTitleActivity implements ConditionAdapter.ConditionInterface {
    // 返回按钮
    @BindView(R.id.iv_back)
    ImageView ivBack;
    // 搜索按钮
    @BindView(R.id.tv_search)
    TextView tvSearch;
    // 搜索输入框
    @BindView(R.id.et_search)
    EditText etSearch;
    // 清空按钮
    @BindView(R.id.tv_clear)
    TextView tvClear;
    // 历史标签控件
    @BindView(R.id.ftl_history)
    FlowTagLayout ftlHistory;
    private ConditionAdapter conditionAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        super.initView();
        title_main_layout.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        // 初始化流标签适配器
        conditionAdapter = new ConditionAdapter(this, this, false);
        ftlHistory.setAdapter(conditionAdapter);
        // 从SharedPreferences中取出历史记录，历史记录是以“,”分割的字符串
        String history = SpUtils.getStringParam(this, "history", "");
        if (!TextUtils.isEmpty(history)) {
            String[] arr = null;
            if (history.contains(",")) {
                arr = history.split(",");
            } else {
                arr = new String[]{history};
            }
            for (int i = 0; i < arr.length; i++) {
                DictInfoEntity dictInfoEntity = new DictInfoEntity();
                dictInfoEntity.setDictName(arr[i]);
                conditionAdapter.addTag(dictInfoEntity);
            }
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        // 设置软键盘搜索监听
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 如果是搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    tvSearch.performClick();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                // 返回
                finish();
                break;
            case R.id.tv_search:
                // 搜索
                String searchStr = etSearch.getText().toString();
                if (TextUtils.isEmpty(searchStr)) {
                    ToastUtil.showShort(this, R.string.search_hint);
                    return;
                }
                // 存储历史记录
                DictInfoEntity dictInfoEntity = saveHistory(searchStr);
                toResult(dictInfoEntity);
                break;
            case R.id.tv_clear:
                // 清空
                SpUtils.putParam(this, "history", "");
                conditionAdapter.clearData();
                break;
        }
    }

    // 标签删除事件，但是此处用其为点击搜索功能
    @Override
    public void deleteTag(DictInfoEntity dictInfoEntity) {
        toResult(dictInfoEntity);
    }

    /**
     * 设置回调
     *
     * @param dictInfoEntity
     */
    private void toResult(DictInfoEntity dictInfoEntity) {
        // 将搜索内容传递到“买车”页面进行搜索
        Intent intent = new Intent();
        intent.putExtra("dictInfoEntity", dictInfoEntity);
        setResult(ResultCode.KEYWORD, intent);
        finish();
    }

    /**
     * 存储历史记录
     *
     * @param searchStr
     */
    private DictInfoEntity saveHistory(String searchStr) {
        // 先将当前搜索关键字加入到标签中
        List<DictInfoEntity> items = conditionAdapter.getItems();
        DictInfoEntity sameDict = null;
        // 检查是否有重复标签
        for (DictInfoEntity dictInfoEntity : items) {
            if (dictInfoEntity.getDictName().equals(searchStr)) {
                sameDict = dictInfoEntity;
            }
        }
        // 如果有重复标签，则去掉重复标签
        if (sameDict != null) {
            conditionAdapter.removeElement(sameDict);
        }
        // 将当前搜索关键字，加入到标签列表中
        DictInfoEntity searchDict = new DictInfoEntity();
        searchDict.setDictName(searchStr);
        conditionAdapter.addTag(searchDict);

        // 在将所有标签取出
        items = conditionAdapter.getItems();
        String history = "";
        // 组合为历史记录存储格式
        for (DictInfoEntity dictInfoEntity1 : items) {
            history += dictInfoEntity1.getDictName() + ",";
        }

        history = TextUtils.isEmpty(history) ?
                history : history.substring(0, history.length() - 1);
        // 将搜索内容保存到历史记录中
        SpUtils.putParam(this, "history", history);
        return searchDict;
    }
}
