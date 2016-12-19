//package com.lakeel.altla.vision.nearby.presentation.view.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lakeel.altla.vision.nearby.R;
//import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationItemModel;
//import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
//import com.marshalchen.ultimaterecyclerview.expanx.SmartItem;
//import com.marshalchen.ultimaterecyclerview.expanx.Util.ItemDataClickListener;
//import com.marshalchen.ultimaterecyclerview.expanx.Util.easyTemplateChild;
//import com.marshalchen.ultimaterecyclerview.expanx.Util.easyTemplateParent;
//import com.marshalchen.ultimaterecyclerview.expanx.customizedAdapter;
//
//import java.util.List;
//
//public final class ExpandAdapter extends customizedAdapter<ExpandAdapter.Category, ExpandAdapter.SubCategory> {
//
//    public ExpandAdapter(Context context) {
//        super(context);
//    }
//
//    @Override
//    protected Category iniCustomParentHolder(View parentView) {
//        return new Category(parentView);
//    }
//
//    @Override
//    protected SubCategory iniCustomChildHolder(View childView) {
//        return new SubCategory(childView);
//    }
//
//    @Override
//    protected int getLayoutResParent() {
//        return R.layout.information_item;
//    }
//
//    @Override
//    protected int getLayoutResChild() {
//        return R.layout.information_sub_item;
//    }
//
//    @Override
//    protected List<SmartItem> getChildrenByPath(String path, int depth, int position) {
//        return null;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder newFooterHolder(View view) {
//        return new UltimateRecyclerviewViewHolder(view);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder newHeaderHolder(View view) {
//        return new UltimateRecyclerviewViewHolder(view);
//    }
//
//    public final class Category extends easyTemplateParent<InformationItemModel, RelativeLayout, TextView> {
//
//        public Category(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void bindView(final InformationItemModel itemData, final int position, final ItemDataClickListener imageClickListener) {
//
//        }
//    }
//
//    public final class SubCategory extends easyTemplateChild<InformationItemModel, TextView, RelativeLayout> {
//
//        public SubCategory(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void bindView(final InformationItemModel itemData, int position) {
//
//        }
//    }
//}
