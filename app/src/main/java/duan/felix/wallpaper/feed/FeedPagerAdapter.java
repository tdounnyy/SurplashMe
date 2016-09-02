package duan.felix.wallpaper.feed;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.scaffold.utils.ObjectUtils;
import duan.felix.wallpaper.widget.PhotoItemContainer;

/**
 * @author Felix.Duan.
 */

public class FeedPagerAdapter extends PagerAdapter {

    private static final String TAG = "FeedPagerAdapter";

    private List<Photo> mItems = null;

    public void setViewPager(ViewPager pager) {
        pager.setAdapter(this);
    }

    public void setItems(List<Photo> items) {
        LogUtils.d(TAG, "setItems " + items.size());
        mItems = items;
        notifyDataSetChanged();
    }

    public void appendItems(List<Photo> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        LogUtils.d(TAG, "getCount " + (mItems == null ? 0 : mItems.size()));
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ObjectUtils.equals(view, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoItemContainer v = (PhotoItemContainer) LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item, container, false);
        v.setPhoto(mItems.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
