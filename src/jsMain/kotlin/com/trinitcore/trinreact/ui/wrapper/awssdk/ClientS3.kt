package com.trinitcore.trinreact.ui.wrapper.awssdk

import org.w3c.fetch.Body

interface CommandParams

class S3UploadParams(
    @JsName("Bucket") val bucket: String, @JsName("Key") val key: String, @JsName("Body") val body: Body
    ) : CommandParams