package com.android.library.base

import android.os.Environment
import com.android.library.model.HomeBlogEntity
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*


object Constant {
    const val REALM_VERSION = 1L
    const val REALM_NAME = "life"
    const val SP_NAME = "yc"
    val EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().absolutePath + "/"
    const val GITHUB = "https://github.com/yangchong211/YCBlogs"
    const val LIFE_HELPER = "https://github.com/yangchong211/LifeHelper"
    const val JIAN_SHU = "https://www.jianshu.com/u/b7b2c6ed9284"
    const val JUE_JIN = "https://juejin.im/user/5939433efe88c2006afa0c6e"
    const val ZHI_HU = "https://www.zhihu.com/people/yczbj/activities"
    const val FLUTTER = "https://github.com/yangchong211/ycflutter"
    /**-------------------------------------记事本---------------------------------------------- */
    const val NEW_ACTIVITY_REQUEST_CODE = 0
    const val EDIT_ACTIVITY_REQUEST_CODE = 1
    const val INTENT_EXTRA_DAY_OF_WEEK = "DAY_OF_WEEK"
    const val INTENT_BUNDLE_NEW_TASK_DETAIL = "NEW_TASK_DETAIL"
    const val INTENT_EXTRA_EDIT_TASK_DETAIL_ENTITY = "EDIT_TASK_DETAIL_ENTITY"
    const val INTENT_EXTRA_MODE_OF_NEW_ACT = "MODE_OF_NEW_ACT"
    const val INTENT_EXTRA_SWITCH_TO_INDEX = "SWITCH_TO_INDEX"
    const val CHOOSE_PAPER_DIALOG_CHECK_ITEM_BUNDLE_KEY = "CHECK_ITEM_BUNDLE_KEY"
    const val DATABASE_FILE_PATH_FOLDER = "WeekToDo"
    const val DATABASE_FILE_PATH_FILE_NAME = "data.realm"
    const val DATABASE_FILE_BACKUP_PATH_FOLDER = "番茄周/备份"
    const val DATABASE_FILE_EXPORT_PATH_FOLDER = "番茄周/导出"
    const val AUTO_NOTIFY_INTERVAL_TIME = 60 * 60 * 1000.toLong()
    const val AUTO_NOTIFY_NOTIFICATION_ID = 0
    const val DAY_OF_WEEK = 7
    /**-------------------------------------键------------------------------------------------- */ //Sp键
    const val KEY_FIRST_SPLASH = "first_splash" //是否第一次启动
    const val KEY_IS_LOGIN = "is_login" //登录
    const val KEY_IS_SHOW_LIST_IMG = "is_show_list_img" //是否展示list页面图片
    const val KEY_IS_SHOW_GIRL_IMG = "is_show_girl_img" //启动页是否是妹子图
    const val KEY_IS_SHOW_IMG_RANDOM = "is_show_girl_random" //启动页是否概率出现
    const val KEY_THUMBNAIL_QUALITY = "thumbnail_quality" //启动页是否概率出现
    const val KEY_BANNER_URL = "banner_url" //启动页是否概率出现
    const val KEY_NIGHT_STATE = "night_state" //启动页夜间模式
    /**-------------------------------------集合------------------------------------------------- */ //
    var findNews: List<HomeBlogEntity> = ArrayList<HomeBlogEntity>()
    var findBottomNews: List<HomeBlogEntity> = ArrayList<HomeBlogEntity>()
    /**-------------------------------------集合------------------------------------------------- */
    const val URL = "url"
    const val ID = "id"
    const val TITLE = "title"
    const val CONTENT = "content"
    /**-------------------------------------music------------------------------------------------- */
    const val EXTRA_NOTIFICATION = "extra_notification"
    const val LOCK_SCREEN = "lock_screen"
    const val LOCK_SCREEN_ACTION = "cn.ycbjie.lock"
    const val FILTER_SIZE = "filter_size"
    const val FILTER_TIME = "filter_time"
    const val MUSIC_ID = "music_id"
    const val PLAY_MODE = "play_mode"
    const val IS_SCREEN_LOCK = "is_screen_lock"
    const val APP_OPEN_COUNT = "app_open_count"
    const val PLAY_POSITION = "play_position"
    /**
     * 网络缓存最大值
     */
    const val CACHE_MAXSIZE = 1024 * 1024 * 30
    /**
     * 网络缓存保存时间
     */
    const val TIME_CACHE = 60 * 60

    /**
     * 配合CoordinatorLayout使用
     */
    @Retention(RetentionPolicy.SOURCE)
    annotation class STATES {
        companion object {
            var EXPANDED = 3
            var COLLAPSED = 2
            var INTERMEDIATE = 1
        }
    }

    interface viewType {
        companion object {
            const val typeBanner = 1 //轮播图
            const val typeGv = 2 //九宫格
            const val typeTitle = 3 //标题
            const val typeList = 4 //list
            const val typeNews = 5 //新闻
            const val typeMarquee = 6 //跑马灯
            const val typePlus = 7 //不规则视图
            const val typeSticky = 8 //指示器
            const val typeFooter = 9 //底部
            const val typeGvSecond = 10 //九宫格
        }
    }

    object status {
        const val success = 200
        const val error = -1
    }

    interface MODE_OF_NEW_ACT {
        companion object {
            const val MODE_EDIT = 5
            const val MODE_CREATE = 6
            const val MODE_QUICK = 7
        }
    }

    interface CONFIG_KEY {
        companion object {
            const val SHOW_WEEK_TASK = "SHOW_WEEK_TASK"
            const val SHOW_AS_LIST = "SHOW_AS_LIST"
            const val SHOW_PRIORITY = "SHOW_PRIORITY"
            const val NIGHT_MODE = "NIGHT_MODE"
            const val AUTO_SWITCH_NIGHT_MODE = "AUTO_SWITCH_NIGHT_MODE"
            const val BACKUP = "BACKUP"
            const val RECOVERY = "RECOVERY"
            const val AUTO_NOTIFY = "AUTO_NOTIFY"
        }
    }

    interface TaskState {
        companion object {
            const val DEFAULT = 0
            const val FINISHED = 1
        }
    }

    interface LikeType {
        companion object {
            const val TYPE_ZHI_HU = 101
            const val TYPE_GIRL = 105
            const val TYPE_WE_CHAT = 106
            const val TYPE_GOLD = 108
        }
    }

    interface DetailKeys {
        companion object {
            const val IT_DETAIL_URL = "url"
            const val IT_DETAIL_IMG_URL = "img_url"
            const val IT_DETAIL_ID = "id"
            const val IT_DETAIL_TYPE = "type"
            const val IT_DETAIL_TITLE = "title"
            const val IT_GOLD_TYPE = "type"
            const val IT_GOLD_TYPE_STR = "type_str"
            const val IT_GOLD_MANAGER = "manager"
        }
    }
}