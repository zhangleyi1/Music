package com.zly.music.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/22.
 */
public class MusicData implements Parcelable{

    private long id;
    private int type; // 歌曲类型:本地/网络
    private long songId; // [本地]歌曲ID
    private String title; // 音乐标题
    private String artist; // 艺术家
    private String album; // 专辑
    private long albumId; // [本地]专辑ID
    private String coverPath; // [在线]专辑封面路径
    private long duration; // 持续时间
    private String path; // 播放地址
    private String fileName; // [本地]文件名
    private long fileSize; // [本地]文件大小
    private Bitmap bitmap;

    public MusicData(Long id, int type, long songId, String title, String artist,
                     String album, long albumId, String coverPath, long duration,
                     String path, String fileName, long fileSize, Bitmap bitmap) {
        this.id = id;
        this.type = type;
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.coverPath = coverPath;
        this.duration = duration;
        this.path = path;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.bitmap = bitmap;
    }

    public MusicData() {}

    public MusicData(Parcel in) {
        id = in.readLong();
        type = in.readInt();
        songId = in.readLong();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        albumId = in.readLong();
        coverPath = in.readString();
        duration = in.readLong();
        path = in.readString();
        fileName = in.readString();
        fileSize = in.readLong();
        bitmap = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicData> CREATOR = new Creator<MusicData>() {
        @Override
        public MusicData createFromParcel(Parcel in) {
            return new MusicData(in);
        }

        @Override
        public MusicData[] newArray(int size) {
            return new MusicData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(type);
        dest.writeLong(songId);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeString(coverPath);
        dest.writeLong(duration);
        dest.writeString(path);
        dest.writeString(fileName);
        dest.writeLong(fileSize);
        dest.writeParcelable(bitmap, flags);
    }

    @Override
    public String toString() {
        return "zly --> MusicData{" +
                "id=" + id +
                ", type=" + type +
                ", songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", coverPath='" + coverPath + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
