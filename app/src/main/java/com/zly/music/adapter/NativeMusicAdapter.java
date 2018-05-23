package com.zly.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zly.music.R;
import com.zly.music.bean.MusicData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/22.
 */
public class NativeMusicAdapter extends RecyclerView.Adapter<NativeMusicAdapter.CustomViewHolder> {

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<MusicData> mList;

    public NativeMusicAdapter(Context context, ArrayList<MusicData> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.native_music_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Bitmap bm = mList.get(position).getBitmap();
        if (null != bm) {
            holder.mCoverIv.setImageBitmap(mList.get(position).getBitmap());
        }

        String tempStr = mList.get(position).getTitle();
        if (!tempStr.isEmpty()) {
            holder.mTitleTv.setText(tempStr);
        }

        tempStr = tempStr.format(mContext.getResources().getString(R.string.item_native_music_album_text),
                mList.get(position).getArtist(), mList.get(position).getAlbum());
        if (!tempStr.isEmpty()) {
            holder.mAlbumTv.setText(tempStr);
        }

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView mCoverIv;
        TextView mTitleTv;
        TextView mAlbumTv;
        ImageView mMore;

        CustomViewHolder(View itemView) {
            super(itemView);
            mCoverIv = (ImageView)itemView.findViewById(R.id.cover_iv);
            mTitleTv = (TextView)itemView.findViewById(R.id.music_title_tv);
            mAlbumTv = (TextView)itemView.findViewById(R.id.music_album_tv);
            mMore = (ImageView)itemView.findViewById(R.id.item_more_iv);
        }
    }

}
