package com.cqkj.snail.tool;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import com.cqkj.snail.truck.entity.ImageViewInfo;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共工具类
 */
public class CommonUtil {
    /**
     * 查找信息
     * @return
     */
    public static  List<ImageViewInfo> computeBoundsBackward(ArrayList<String> list_path) {
        List<ImageViewInfo> list = new ArrayList<>();
        for (int i = 0;i < list_path.size(); i++) {
            ImageViewInfo imageViewInfo = new ImageViewInfo(list_path.get(i));
            Rect bounds = new Rect();
            imageViewInfo.setBounds(bounds);
            list.add(imageViewInfo);
        }
        return list;
    }

}
