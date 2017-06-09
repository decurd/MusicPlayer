package com.decurd.musicplayer.fragments;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.decurd.musicplayer.MainActivity;
import com.decurd.musicplayer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by decur on 2017-05-31.
 */

public class MusicContrallerFragment extends Fragment {

    private ImageView mAlbumImageView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private Button mPlayButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.music_controller, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAlbumImageView = (ImageView) view.findViewById(R.id.album_art);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text);
        mArtistTextView = (TextView) view.findViewById(R.id.artist_text);

        mPlayButton = (Button) view.findViewById(R.id.play_button);
    }

    @Subscribe
    public void updateUi(final MediaMetadataRetriever retriever) {
        // 미디어정보
        String title = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_TITLE));
        String artist = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_ARTIST));
        String duration = retriever.extractMetadata((MediaMetadataRetriever.METADATA_KEY_DURATION));

        // 오디오 앨범 아트
        byte[] albumImage = retriever.getEmbeddedPicture();
        if (null != albumImage) {
            Glide.with(this).load(albumImage).into(mAlbumImageView);
        }

        mTitleTextView.setText(title);
        mArtistTextView.setText(artist);

        if (((MainActivity)getActivity()).isPlaying()) {
            mPlayButton.setText("중지");
        } else {
            mPlayButton.setText("재생");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
