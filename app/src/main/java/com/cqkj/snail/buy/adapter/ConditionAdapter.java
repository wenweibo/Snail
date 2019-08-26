package com.cqkj.snail.buy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqkj.snail.R;
import com.cqkj.snail.buy.entity.DictInfoEntity;
import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter;

/**
 * 筛选条件适配器
 *
 * @author 闻维波
 * @since 2019/08/13 11:03
 */
public class ConditionAdapter extends BaseTagAdapter<DictInfoEntity, View> {
    private ConditionInterface conditionInterface;
    // 是否删除
    private boolean isDelete;

    public ConditionAdapter(Context context, ConditionInterface conditionInterface, boolean isDelete) {
        super(context);
        this.conditionInterface = conditionInterface;
        this.isDelete = isDelete;
    }

    @Override
    protected View newViewHolder(View convertView) {
        return convertView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_condition;
    }

    @Override
    protected void convert(View holder, final DictInfoEntity item, final int position) {
        TextView tv_title = holder.findViewById(R.id.tv_title);
        tv_title.setText(item.getDictName());
        DeleteOnClick deleteOnClick = new DeleteOnClick(item);
        ImageView iv_delete = holder.findViewById(R.id.iv_delete);
        // 如果是允许删除，则显示删除“x"
        if (isDelete) {
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setOnClickListener(deleteOnClick);
        } else {
            // 否则，隐藏
            iv_delete.setVisibility(View.GONE);
        }
        tv_title.setOnClickListener(deleteOnClick);
    }

    class DeleteOnClick implements View.OnClickListener {
        DictInfoEntity dictInfoEntity;

        public DeleteOnClick(DictInfoEntity dictInfoEntity) {
            this.dictInfoEntity = dictInfoEntity;
        }

        @Override
        public void onClick(View v) {
            // 如果是允许删除
            if (isDelete) {
                // 则删除对应的标签
                ConditionAdapter.this.removeElement(dictInfoEntity);
                dictInfoEntity.setSelectFlag(0);
            }
            conditionInterface.deleteTag(dictInfoEntity);
        }
    }

    public interface ConditionInterface {
        void deleteTag(DictInfoEntity dictInfoEntity);
    }
}
