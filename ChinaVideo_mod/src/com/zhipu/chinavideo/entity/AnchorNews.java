package com.zhipu.chinavideo.entity;

public class AnchorNews {
	private String mHeadIconUrl;//头像地址
	private String mPhotoUrl;//图片地址
	private boolean mHasLiked;//是否点赞
	private int mLikeCount ;//点赞数量
	private int mId ;
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmHeadIconUrl() {
		return mHeadIconUrl;
	}
	public void setmHeadIconUrl(String mHeadIconUrl) {
		this.mHeadIconUrl = mHeadIconUrl;
	}
	public String getmPhotoUrl() {
		return mPhotoUrl;
	}
	public void setmPhotoUrl(String mPhotoUrl) {
		this.mPhotoUrl = mPhotoUrl;
	}
	public boolean ismHasLiked() {
		return mHasLiked;
	}
	public void setmHasLiked(boolean mHasLiked) {
		this.mHasLiked = mHasLiked;
	}
	public int getmLikeCount() {
		return mLikeCount;
	}
	public void setmLikeCount(int mLikeCount) {
		this.mLikeCount = mLikeCount;
	}
	
}
