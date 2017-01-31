package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public final class EmptySupportedRecyclerView extends RecyclerView {

    private boolean isFirstCalled = true;

    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            if (isFirstCalled) {
                isFirstCalled = false;
                EmptySupportedRecyclerView.this.setVisibility(View.VISIBLE);
                return;
            }

            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    EmptySupportedRecyclerView.this.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    EmptySupportedRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public EmptySupportedRecyclerView(Context context) {
        super(context);
    }

    public EmptySupportedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySupportedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
