package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.CommentedImage
import com.delitx.furnituremaster.data_models.MultiLanguageString

class CommentedImageNetworkDto(
    val id: Int,
    val link: String,
    val comment: Map<String, String>
) {
    fun toModel(): CommentedImage {
        return CommentedImage(id = id, link = link, comment = MultiLanguageString(comment))
    }
}
