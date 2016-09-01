package duan.felix.wallpaper.feed;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import duan.felix.wallpaper.core.event.LoadAfterEvent;
import duan.felix.wallpaper.scaffold.event.Bus;

/**
 * @author Felix.Duan.
 */
class LoadMoreListener extends RecyclerView.OnScrollListener {

    private final int threshold;
    private LinearLayoutManager layoutManager;

    // TODO:** load direction
    LoadMoreListener(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (layoutManager == null) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            }
            tryLoad(layoutManager, threshold);
        }
    }

    private void tryLoad(LinearLayoutManager layoutManager, int threshold) {
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        if (lastVisibleItemPosition + threshold >= itemCount) {
            Bus.post(new LoadAfterEvent());
        }

    }
}
