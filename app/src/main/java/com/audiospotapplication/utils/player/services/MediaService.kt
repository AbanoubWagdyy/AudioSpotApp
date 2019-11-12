package com.audiospotapplication.utils.player.services

import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat

import com.audiospotapplication.R
import com.audiospotapplication.utils.player.notifications.MediaNotificationManager
import com.audiospotapplication.utils.player.players.MediaPlayerAdapter
import com.audiospotapplication.utils.player.players.PlaybackInfoListener
import com.bumptech.glide.Glide

import java.util.ArrayList

import com.audiospotapplication.utils.Constants.MEDIA_QUEUE_POSITION
import com.audiospotapplication.utils.Constants.QUEUE_NEW_PLAYLIST
import com.audiospotapplication.utils.Constants.SEEK_BAR_MAX
import com.audiospotapplication.utils.Constants.SEEK_BAR_PROGRESS
import com.audiospotapplication.utils.player.MyPreferenceManager
import com.visionvalley.letuno.DataLayer.RepositorySource
import org.koin.core.KoinComponent
import org.koin.core.inject

class MediaService : MediaBrowserServiceCompat(), KoinComponent {

    private var mSession: MediaSessionCompat? = null
    private var mPlayback: MediaPlayerAdapter? = null
    private var mMyPrefManager: MyPreferenceManager? = null
    private var mMediaNotificationManager: MediaNotificationManager? = null
    private var mIsServiceStarted: Boolean = false

    val mRepositorySource: RepositorySource by inject()

    override fun onCreate() {
        super.onCreate()
        mMyPrefManager = MyPreferenceManager(this)

        //Build the MediaSession
        mSession = MediaSessionCompat(this, TAG)

        mSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    // https://developer.android.com/guide/topics/media-apps/mediabuttons#mediabuttons-and-active-mediasessions
                    // Media buttons on the device
                    // (handles the PendingIntents for MediaButtonReceiver.buildMediaButtonPendingIntent)

                    MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
        ) // Control the items in the queue (aka playlist)
        // See https://developer.android.com/guide/topics/media-apps/mediabuttons for more info on flags

        mSession!!.setCallback(MediaSessionCallback())

        // A token that can be used to create a MediaController for this session
        sessionToken = mSession!!.sessionToken


        mPlayback = MediaPlayerAdapter(this, MediaPlayerListener())

        mMediaNotificationManager = MediaNotificationManager(this)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.d(TAG, "onTaskRemoved: stopped")
        super.onTaskRemoved(rootIntent)
        mPlayback!!.stop()
        stopSelf()
    }

    override fun onDestroy() {
        mSession!!.release()
        Log.d(TAG, "onDestroy: MediaPlayerAdapter stopped, and MediaSession released")
    }


    override fun onGetRoot(
        s: String,
        i: Int,
        bundle: Bundle?
    ): MediaBrowserServiceCompat.BrowserRoot? {

        Log.d(TAG, "onGetRoot: called. ")
        return if (s == applicationContext.packageName) {

            // Allowed to browse media
            MediaBrowserServiceCompat.BrowserRoot("some_real_playlist", null) // return no media
        } else MediaBrowserServiceCompat.BrowserRoot("empty_media", null)
// return no media
    }

    override fun onLoadChildren(
        s: String,
        result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        Log.d(TAG, "onLoadChildren: called: $s, $result")

        //  Browsing not allowed
        if (TextUtils.equals("empty_media", s)) {
            result.sendResult(null)
            return
        }
        result.sendResult(mRepositorySource.getMediaItems()); // return all available media
    }


    inner class MediaSessionCallback : MediaSessionCompat.Callback() {

        private val mPlaylist = ArrayList<MediaSessionCompat.QueueItem>()
        private var mQueueIndex = -1
        private var mPreparedMedia: MediaMetadataCompat? = null

        private val isReadyToPlay: Boolean
            get() = !mPlaylist.isEmpty()

        private fun resetPlaylist() {
            mPlaylist.clear()
            mQueueIndex = -1
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle) {
            Log.d(TAG, "onPlayFromMediaId: CALLED.")
            if (extras.getBoolean(QUEUE_NEW_PLAYLIST, false)) {
                resetPlaylist()
            }

            mPreparedMedia = mRepositorySource.getTreeMap().get(mediaId);
            mSession!!.setMetadata(mPreparedMedia)
            if (!mSession!!.isActive) {
                mSession!!.isActive = true
            }
            mPlayback!!.playFromMedia(mPreparedMedia)

            val newQueuePosition = extras.getInt(MEDIA_QUEUE_POSITION, -1)
            if (newQueuePosition == -1) {
                mQueueIndex++
            } else {
                mQueueIndex = extras.getInt(MEDIA_QUEUE_POSITION)
            }
            mMyPrefManager?.saveQueuePosition(mQueueIndex);
            mMyPrefManager?.saveLastPlayedMedia(mPreparedMedia?.description?.mediaId);
        }

        override fun onAddQueueItem(description: MediaDescriptionCompat?) {
            Log.d(TAG, "onAddQueueItem: CALLED: position in list: " + mPlaylist.size)
            mPlaylist.add(
                MediaSessionCompat.QueueItem(
                    description!!,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mQueueIndex == -1) 0 else mQueueIndex
            mSession!!.setQueue(mPlaylist)
        }

        override fun onRemoveQueueItem(description: MediaDescriptionCompat?) {
            mPlaylist.remove(
                MediaSessionCompat.QueueItem(
                    description!!,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (mPlaylist.isEmpty()) -1 else mQueueIndex
            mSession!!.setQueue(mPlaylist)
        }

        override fun onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()) {
                // Nothing to play.
                return
            }

            val mediaId = mPlaylist[mQueueIndex].description.mediaId
            mPreparedMedia = mRepositorySource.getTreeMap().get(mediaId);
            mSession!!.setMetadata(mPreparedMedia)

            if (!mSession!!.isActive) {
                mSession!!.isActive = true
            }
        }

        override fun onPlay() {

            if (!isReadyToPlay) {
                // Nothing to play.
                return
            }

            if (mPreparedMedia == null) {
                onPrepare()
            }

            mPlayback!!.playFromMedia(mPreparedMedia)
            mMyPrefManager?.saveQueuePosition(mQueueIndex)
            mMyPrefManager?.saveLastPlayedMedia(mPreparedMedia?.getDescription()?.getMediaId())
        }

        override fun onPause() {
            mPlayback!!.pause()
        }

        override fun onStop() {
            mPlayback!!.stop()
            mSession!!.isActive = false
        }

        override fun onSkipToNext() {
            Log.d(TAG, "onSkipToNext: SKIP TO NEXT")
            // increment and then check using modulus
            mQueueIndex = ++mQueueIndex % mPlaylist.size
            mPreparedMedia = null
            onPlay()
        }

        override fun onSkipToPrevious() {
            Log.d(TAG, "onSkipToPrevious: SKIP TO PREVIOUS")
            mQueueIndex = if (mQueueIndex > 0) mQueueIndex - 1 else mPlaylist.size - 1
            mPreparedMedia = null
            onPlay()
        }

        override fun onSeekTo(pos: Long) {
            mPlayback!!.seekTo(pos)
        }
    }


    inner class MediaPlayerListener internal constructor() : PlaybackInfoListener {

        private val mServiceManager: ServiceManager

        init {
            mServiceManager = ServiceManager()
        }

        override fun updateUI(newMediaId: String) {
            Log.d(TAG, "updateUI: CALLED: $newMediaId")
            val intent = Intent()
            intent.action = getString(R.string.broadcast_update_ui)
            intent.putExtra(getString(R.string.broadcast_new_media_id), newMediaId)
            sendBroadcast(intent)
        }

        override fun onPlaybackStateChange(state: PlaybackStateCompat) {
            // Report the state to the MediaSession.
            mSession!!.setPlaybackState(state)


            // Manage the started state of this service.
            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.STATE_PAUSED -> mServiceManager.updateNotification(
                    state,
                    mPlayback!!.currentMedia.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)
                )
                PlaybackStateCompat.STATE_STOPPED -> {
                    Log.d(TAG, "onPlaybackStateChange: STOPPED.")
                    mServiceManager.moveServiceOutOfStartedState()
                }
            }
        }

        override fun onSeekTo(progress: Long, max: Long) {
            //            Log.d(TAG, "onSeekTo: CALLED: updating seekbar: " + progress + ", max: " + max);
            val intent = Intent()
            intent.action = getString(R.string.broadcast_seekbar_update)
            intent.putExtra(SEEK_BAR_PROGRESS, progress)
            intent.putExtra(SEEK_BAR_MAX, max)
            sendBroadcast(intent)
        }

        override fun onPlaybackComplete() {
            Log.d(TAG, "onPlaybackComplete: SKIPPING TO NEXT.")
            mSession!!.controller.transportControls.skipToNext()
        }


        internal inner class ServiceManager {

            private var mDisplayImageUri: String? = null
            private val mCurrentArtistBitmap: Bitmap? = null
            private var mState: PlaybackStateCompat? = null

            fun updateNotification(state: PlaybackStateCompat, displayImageUri: String) {
                mState = state

                if (displayImageUri != mDisplayImageUri) {
                    // download new bitmap

                    Glide.with(this@MediaService)
                        .asBitmap()
                        .load(displayImageUri)

                    mDisplayImageUri = displayImageUri
                } else {
                    displayNotification(mCurrentArtistBitmap)
                }
            }

            fun displayNotification(bitmap: Bitmap?) {

                // Manage the started state of this service.
                var notification: Notification? = null
                when (mState!!.state) {

                    PlaybackStateCompat.STATE_PLAYING -> {
                        notification = mMediaNotificationManager!!.buildNotification(
                            mState!!, sessionToken, mPlayback!!.currentMedia.description, bitmap
                        )

                        if (!mIsServiceStarted) {
                            ContextCompat.startForegroundService(
                                this@MediaService,
                                Intent(this@MediaService, MediaService::class.java)
                            )
                            mIsServiceStarted = true
                        }

                        startForeground(MediaNotificationManager.NOTIFICATION_ID, notification)
                    }

                    PlaybackStateCompat.STATE_PAUSED -> {
                        stopForeground(false)
                        notification = mMediaNotificationManager!!.buildNotification(
                            mState!!, sessionToken, mPlayback!!.currentMedia.description, bitmap
                        )
                        mMediaNotificationManager!!.notificationManager
                            .notify(MediaNotificationManager.NOTIFICATION_ID, notification)
                    }
                }
            }

            fun moveServiceOutOfStartedState() {
                stopForeground(true)
                stopSelf()
                mIsServiceStarted = false
            }
        }
    }

    companion object {
        private val TAG = "MediaService"
    }
}