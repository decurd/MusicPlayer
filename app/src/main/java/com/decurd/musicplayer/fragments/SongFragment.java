package com.decurd.musicplayer.fragments;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.decurd.musicplayer.R;
import com.decurd.musicplayer.adapter.CursorRecyclerViewAdapter;
import com.decurd.musicplayer.model.Song;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by decur on 2017-05-31.
 */

public class SongFragment extends Fragment {

    

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        List<Song> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add(new Song("제목" + i, "아티스트" + i));
        }


        Cursor cursor = getActivity().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        mRecyclerView.setAdapter(new SongRecyclerAdapter(getActivity(), cursor));


    }

    public static class SongRecyclerAdapter extends CursorRecyclerViewAdapter<ViewHolder> {

        private Context mContext;

        public SongRecyclerAdapter(Context context, Cursor cursor) {
            super(context, cursor);
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            final Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mContext, uri);

            // 미디어정보
            String title = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_TITLE));
            String artist = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_ARTIST));
            String duration = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_DURATION));

            // 오디오 앨범 아트
            /*byte[] albumImage = retriever.getEmbeddedPicture();
            if (null != albumImage) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(albumImage, 0, albumImage.length);
            }*/

            viewHolder.titleTextView.setText(title);
            viewHolder.artistTextView.setText(artist);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * MainAcitvity#playMusic()
                     */
                    EventBus.getDefault().post(uri);
                }
            });
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView artistTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(android.R.id.text1);
            artistTextView = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }
}
