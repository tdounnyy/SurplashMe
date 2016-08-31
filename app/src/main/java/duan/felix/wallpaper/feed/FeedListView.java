package duan.felix.wallpaper.feed;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class FeedListView extends LinearLayout {

    private static final String TAG = "FeedListView";

    private static final int THRESHOLD = 5;

    PhotoListAdapter mAdapter;

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    public FeedListView(Context context) {
        this(context, null);
    }

    public FeedListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.list_container, this);
        ButterKnife.bind(this);
        mAdapter = new PhotoListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setOnScrollListener(new LoadMoreListener(THRESHOLD));
    }

    public void setItems(List<Photo> items) {
        mAdapter.setItems(items);
    }

    public void appendItems(List<Photo> items) {
        mAdapter.appendItems(items);
    }

    class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {

        List<Photo> items = null;

        void setItems(List<Photo> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        void appendItems(List<Photo> append) {
            items.addAll(append);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(
                            parent.getContext())
                            .inflate(R.layout.photo_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Photo photo = items.get(position);
            holder.setPhoto(photo);
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            Photo photo = null;
            @BindView(R.id.photo_item)
            SimpleDraweeView mDraweeView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void setPhoto(Photo photo) {
                this.photo = photo;
                LogUtils.d(TAG, "setPhoto");
                mDraweeView.setImageURI(photo.urls.regular);
            }

            void setAspectRatio(boolean wide) {
                mDraweeView.setAspectRatio(wide ? 1.125f : 0.56f);
            }
        }
    }

}
