package win.smartown.android.library.certificateCamera;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 货车图片实体
 * @author 闻维波
 * @since 2019/08/06 13:40
 */
public class TruckPic implements Parcelable {
    // 标题
    private String title;
    // 样张资源图片id
    private int specimenRes;
    // 照片地址
    private String imgPath;
    // 是否必须 0：否，1：是
    private int mustFlag;
    // 是否选中
    private int selected;


    public TruckPic(){}
    protected TruckPic(Parcel in) {
        title = in.readString();
        specimenRes = in.readInt();
        imgPath = in.readString();
        mustFlag = in.readInt();
        selected = in.readInt();
    }

    public static final Creator<TruckPic> CREATOR = new Creator<TruckPic>() {
        @Override
        public TruckPic createFromParcel(Parcel in) {
            return new TruckPic(in);
        }

        @Override
        public TruckPic[] newArray(int size) {
            return new TruckPic[size];
        }
    };

    public int getMustFlag() {
        return mustFlag;
    }

    public void setMustFlag(int mustFlag) {
        this.mustFlag = mustFlag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpecimenRes(int specimenRes) {
        this.specimenRes = specimenRes;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public int getSpecimenRes() {
        return specimenRes;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(specimenRes);
        dest.writeString(imgPath);
        dest.writeInt(mustFlag);
        dest.writeInt(selected);
    }
}
