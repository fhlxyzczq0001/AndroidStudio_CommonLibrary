package me.stefan.pickturelib.widget;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.Collections;

import me.stefan.pickturelib.PickBuilder;
import me.stefan.pickturelib.adapter.DisplayRecyclerAdapter;
import me.stefan.pickturelib.interf.OnOperateListener;
import me.stefan.pickturelib.interf.OnStartDragListener;
import me.stefan.pickturelib.utils.ItemTouchHelperCallback;

/**
 * Created by Stefan on 2016/10/17.
 * 用于照片展示的RecyclerView，包含了一切可控性的逻辑，用于降低与Activity或者Fragment的耦合度
 */
public class PickRecyclerView extends RecyclerView implements OnStartDragListener {

    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ArrayList<String> imageList = new ArrayList<>();
    private RequestManager mGlideRequestManager;
    private DisplayRecyclerAdapter mDisplayRecyclerAdapter;
    private PickBuilder mBuilder;
    private OnOperateListener mOnOperateListener ;
    private float picWidth = -1;
    private float picHeight = -1;
    private boolean isShowflag;
    public boolean isShowflag() {
        return isShowflag;
    }

    private boolean showflag = false;//是否显示了删除图标
    public void setDeleteShowInCenter(boolean deleteShowInCenter) {
        this.isDeleteShowInCenter = deleteShowInCenter;
        if (null != mDisplayRecyclerAdapter){
            mDisplayRecyclerAdapter.setDeleteShowInCenter(deleteShowInCenter);
            mDisplayRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private boolean isDeleteShowInCenter = false;//删除图标是否显示在正中间
    /**
     * 用户的照片路径列表，主要是用于保存用户Activity的索引，便于
     */
    private ArrayList<String> userList = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    /**
     * 设置基础参数
     * 所有的初始化将放到设置了基础参数之后
     *
     * @param mBuilder 基础参数
     */
    public void setBuilder(PickBuilder mBuilder) {
        this.mBuilder = mBuilder;

        mGlideRequestManager = Glide.with(mContext);
        if (mBuilder == null) this.mBuilder = new PickBuilder();
        mDisplayRecyclerAdapter = new DisplayRecyclerAdapter(mGlideRequestManager, imageList, mBuilder.getMax(), this, mOnOperateListener);
        mDisplayRecyclerAdapter.setDeleteShowInCenter(isDeleteShowInCenter);
        mDisplayRecyclerAdapter.setAddImg(this.mBuilder.getAddImg());
        mDisplayRecyclerAdapter.setRemoveImg(this.mBuilder.getRemoveImg());
        mDisplayRecyclerAdapter.setImageSize(picWidth,picHeight);
        setLayoutManager(new GridLayoutManager(mContext, this.mBuilder.getColumn()));
        setItemAnimator(new DefaultItemAnimator());
        setAdapter(mDisplayRecyclerAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mDisplayRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    public PickRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context mContext) {
        this.mContext = mContext;
//        mPickStyleableHelper = new PickStyleableHelper(mContext);
    }


    public void setOnOperateListener(OnOperateListener mOnOperateListener) {
        this.mOnOperateListener = mOnOperateListener;
        if (mDisplayRecyclerAdapter != null)
            mDisplayRecyclerAdapter.setOnOperateListener(this.mOnOperateListener);
    }


    /**
     * 绑定数据，请在onActivityResult() 方法中调用此方法
     *
     * @param newList 新的列表
     */
    public void bind(ArrayList<String> newList) {

        if (newList == null || newList.size() == 0) {
            throw new IllegalArgumentException("please {@see me.stefan.pickturelib.widget.PickRecyclerView.bind()} a not null list");
        }
        if (equals(newList)) return;

        userList = newList;

        imageList.clear();
        imageList.addAll(newList);
        mDisplayRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 判断是否为相同数据
     *
     * @param newList
     * @return
     */
    public boolean equals(ArrayList<String> newList) {
        if (imageList.size() == newList.size()) {
            for (int i = 0; i < imageList.size(); i++) {
                if (!imageList.get(i).equals(newList.get(i))) {
                    return false;
                }
            }
        } else return false;
        return true;
    }

    /**
     * 删除相应位置的数据
     *
     * @param pos
     */
    public void remove(int pos) {
        if (pos >= 0 && imageList.size() > pos) {

            mDisplayRecyclerAdapter.removeData(pos);
            //同步更新用户的数据
            userList.remove(pos);
        }
    }

    /**
     * 删除路径
     *
     * @param path 路径
     */
    public void remove(String path) {
        int pos = imageList.indexOf(path);
        if (pos >= 0 && imageList.size() > pos) {

            mDisplayRecyclerAdapter.removeData(pos);
            //同步更新用户的数据
            if (!userList.remove(path)) {
                Log.e(TAG, "remove " + path + " failed");
            }
        }
    }
    public void ItemMove(int fromPosition, int toPosition) {
        //数据机构上实现from到to的插入
        if (fromPosition < toPosition) {
            //分别把中间所有的item的位置重新交换
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(userList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(userList, i, i - 1);
            }
        }
    }


    /**
     * 隐藏删除布局
     */
    public void hideDeleteSqrl(){
        mDisplayRecyclerAdapter.goneDelete();
        isShowflag = false;
    }
    /**
     * 显示删除布局
     */
    public void showDeleteSqrl(){
        mDisplayRecyclerAdapter.showDelete();
        isShowflag = true;
    }

    /**
     * 同步用户数据顺序
     *
     * @param fromPosition
     * @param toPosition
     */
    public void move(int fromPosition, int toPosition) {
        if (toPosition > userList.size()) return;
        Collections.swap(userList, fromPosition, toPosition);
    }


    /**
     * 获取当前选中的照片数据
     *
     * @return
     */
    public ArrayList<String> getImageList() {
        return imageList;
    }

    public int getSize() {
        return imageList == null ? 0 : imageList.size();
    }

    @Override
    public void startDrag(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    public void setImageSize(float picWidth, float picHeight) {
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        if (null != mDisplayRecyclerAdapter){
            mDisplayRecyclerAdapter.setImageSize(picWidth,picHeight);
            mDisplayRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
