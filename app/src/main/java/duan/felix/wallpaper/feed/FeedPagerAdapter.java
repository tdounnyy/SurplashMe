package duan.felix.wallpaper.feed;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.scaffold.utils.ObjectUtils;
import duan.felix.wallpaper.widget.PhotoItemContainer;

/**
 * @author Felix.Duan.
 */

class FeedPagerAdapter extends PagerAdapter {

    private static final String TAG = "FeedPagerAdapter";

    private List<Photo> mItems = null;

    private Queue<PhotoItemContainer> mRecyclerPool = new ArrayDeque<>();

    void setViewPager(ViewPager pager) {
        pager.setAdapter(this);
    }

    void setItems(List<Photo> items) {
        LogUtils.d(TAG, "setItems " + items.size());
        mItems = items;
        notifyDataSetChanged();
    }

    void appendItems(List<Photo> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ObjectUtils.equals(view, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoItemContainer view = mRecyclerPool.poll();
        if (view == null) {
            view = (PhotoItemContainer) LayoutInflater.from(container.getContext()).inflate(R.layout.a_photo_item, container, false);
        }
        view.setPhoto(mItems.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mRecyclerPool.offer((PhotoItemContainer) object);
    }
}
