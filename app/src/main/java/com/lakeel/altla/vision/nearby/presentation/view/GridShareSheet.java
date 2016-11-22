package com.lakeel.altla.vision.nearby.presentation.view;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuItem;

public final class GridShareSheet {

    private static final String MENU_TITLE_SHARE = "Share";

    private BottomSheetLayout mBottomSheetLayout;

    private MenuSheetView mMenuSheetView;

    private MenuSheetView.OnMenuItemClickListener mOnMenuItemClickListener;

    public GridShareSheet(Context context, BottomSheetLayout bottomSheetLayout, @MenuRes int menuRes) {
        mBottomSheetLayout = bottomSheetLayout;

        mMenuSheetView =
                new MenuSheetView(context, MenuSheetView.MenuType.GRID, MENU_TITLE_SHARE, item -> {
                    if (mBottomSheetLayout.isSheetShowing()) {
                        mBottomSheetLayout.dismissSheet();
                    }
                    mOnMenuItemClickListener.onMenuItemClick(item);
                    return true;
                });
        mMenuSheetView.inflateMenu(menuRes);
    }

    public void setOnMenuItemClickListener(MenuSheetView.OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    public void hideMenuItem(int itemId) {
        Menu menu = mMenuSheetView.getMenu();
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setVisible(false);
            mMenuSheetView.updateMenu();
        }
    }

    public void show() {
        mBottomSheetLayout.showWithSheetView(mMenuSheetView);
    }
}
