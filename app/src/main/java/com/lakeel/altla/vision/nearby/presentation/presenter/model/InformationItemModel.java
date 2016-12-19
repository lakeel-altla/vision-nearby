package com.lakeel.altla.vision.nearby.presentation.presenter.model;

import android.content.Context;
import android.support.annotation.StringRes;

import com.marshalchen.ultimaterecyclerview.expanx.ExpandableItemData;
import com.marshalchen.ultimaterecyclerview.expanx.LinearExpanxURVAdapter;

import java.util.List;
import java.util.UUID;

public final class InformationItemModel extends ExpandableItemData {

    public InformationItemModel(int type, String text, String path, int depth, List<InformationItemModel> children) {
        super(type, text, path, UUID.randomUUID().toString(), depth, children);
    }

    public static InformationItemModel parent(final String title, final String path, final List<InformationItemModel> carrying_list) {
        return new InformationItemModel(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_PARENT, title, path, 0, carrying_list);
    }

    public static InformationItemModel parent(final Context ctx, final @StringRes int title, final String path, final List<InformationItemModel> carrying_list) {
        return new InformationItemModel(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_PARENT,
                ctx.getResources().getString(title), path, 0, carrying_list);
    }

    public static InformationItemModel child(final String title, final String path) {
        return new InformationItemModel(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_CHILD, title, path, 1, null);
    }

    public static InformationItemModel child(final Context ctx, final @StringRes int title, final String path) {
        return new InformationItemModel(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_CHILD, ctx.getResources().getString(title), path, 1, null);
    }
}
