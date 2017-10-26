package com.glide_library;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/**自定义GlideModule，解决imageview设置tag问题和自定义缓存策略
 * Created by Administrator on 2017/3/2.
 */

public class MyGlideMoudle implements GlideModule {
    private   final int HTTP_MEMORY_CACHE_SIZE = 10 * 1024 * 1024; // 10MB//内存缓存大小
    private  final int HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB//硬盘缓存大小
    private  final String HTTP_DISK_CACHE_DIR_NAME = "GLIDE_DISK_CACHE";//图片缓存文件夹
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);
        //存放在外置文件浏览器
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, HTTP_DISK_CACHE_DIR_NAME, HTTP_DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
