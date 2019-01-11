package io.github.christianmz.organizze.helpers

import android.util.Base64

object Base64Custom {

    fun encode(text: String) = Base64.encodeToString(text.toByteArray(), Base64.DEFAULT).replace("(\\n|\\r)".toRegex(), "")
    fun decode(textCoded: String) = String(Base64.decode(textCoded, Base64.DEFAULT))

}