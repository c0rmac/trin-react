@file:JsModule("@aws-sdk/client-s3")
@file:JsNonModule

package com.trinitcore.trinreact.ui.wrapper.awssdk

import kotlin.js.Promise

external class S3Client(params: dynamic/*region: String, credentials: CognitoIdentityCredentials*/) {
    fun send(command: Command): Promise<dynamic>
}

open external class Command

external class PutObjectCommand(commandParams: CommandParams) : Command

external class ListObjectsCommand : Command

external class DeleteObjectCommand : Command

external class DeleteObjectsCommand : Command