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
 * @author 闻维波
 * @since 2019/08/13 11:03
 */
public class ConditionAdapter extends BaseTagAdapter<DictInfoEntity, View> {
    private ConditionInterface conditionInterface;
    public ConditionAdapter(Context context,ConditionInterface conditionInterface) {
        super(context);
        this.conditionInterface = conditionInterface;
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
        ImageView iv_delete = holder.findViewById(R.id.iv_delete);
        DeleteOnClick deleteOnClick = new DeleteOnClick(item);
        iv_delete.setOnClickListener(deleteOnClick);
        tv_title.setOnClickListener(deleteOnClick);
    }
    class DeleteOnClick implements View.OnClickListener{
        DictInfoEntity dictInfoEntity;
        public DeleteOnClick(DictInfoEntity dictInfoEntity) {
            this.dictInfoEntity = dictInfoEntity;
        }
        @Override
        public void onClick(View v) {
            ConditionAdapter.this.removeElement(dictInfoEntity);
            conditionInterface.deleteTag(dictInfoEntity);
            dictInfoEntity.setSelectFlag(0);
        }
    }
    public interface ConditionInterface{
        void deleteTag(DictInfoEntity dictInfoEntity);
    }
}
