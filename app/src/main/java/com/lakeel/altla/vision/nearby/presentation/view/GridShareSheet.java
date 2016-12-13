package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuItem;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

public final class GridShareSheet {

    private static final String MENU_TITLE_SHARE = "Share";

    private BottomSheetLayout bottomSheetLayout;

    private MenuSheetView menuSheetView;

    private MenuSheetView.OnMenuItemClickListener onMenuItemClickListener;

    public GridShareSheet(Context context, BottomSheetLayout bottomSheetLayout, @MenuRes int menuRes) {
        this.bottomSheetLayout = bottomSheetLayout;

        menuSheetView =
                new MenuSheetView(context, MenuSheetView.MenuType.GRID, MENU_TITLE_SHARE, item -> {
                    if (this.bottomSheetLayout.isSheetShowing()) {
                        this.bottomSheetLayout.dismissSheet();
                    }
                    onMenuItemClickListener.onMenuItemClick(item);
                    return true;
                });
        menuSheetView.inflateMenu(menuRes);
    }

    public void setOnMenuItemClickListener(MenuSheetView.OnMenuItemClickListener listener) {
        onMenuItemClickListener = listener;
    }

    public void hideMenuItem(int itemId) {
        Menu menu = menuSheetView.getMenu();
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setVisible(false);
            menuSheetView.updateMenu();
        }
    }

    public void show() {
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }
}
