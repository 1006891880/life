package com.android.library.model

import org.json.JSONObject

class HomeBlogEntity {
    var title: String? = null
    var logo: String? = null
    var imageUrl: String? = null
    var url: String? = null
    var summary: String? = null
    var time: String? = null
    var author: String? = null

    constructor() {}
    constructor(jsonObject: JSONObject) {
        title = jsonObject.optString("title")
        logo = jsonObject.optString("logo")
        imageUrl = jsonObject.optString("imageUrl")
        url = jsonObject.optString("url")
        summary = jsonObject.optString("summary")
        time = jsonObject.optString("time")
        author = jsonObject.optString("author")
    }

}