package duan.felix.wallpaper.feed;

/**
 * @author Felix.Duan.
 */
interface LoadMoreObserver {
    boolean needLoadMore(int total, int currentIndex);

    void loadMore();
}
