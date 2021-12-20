package com.trinitcore.trinreact.exception

import com.trinitcore.trinreact.Language
import com.trinitcore.trinreact.TLocalisable
import com.trinitcore.trinreact.text
import kotlinx.serialization.Serializable

data class ExceptionDTO(val message: String)

class ReferenceFieldPropertyTypeException(referenceFieldProperty: String)
    : Throwable("The field property type of '$referenceFieldProperty' is incorrect.")


open class RESTException(
    override val en: String,
    override val ga: String,
    override val es: String? = null,

    val status: HttpStatus,

    val defaultLanguage: Language = Language.EN
) : Exception(en), TLocalisable {

    var defaultText: String

    init {
        defaultText = when (defaultLanguage) {
            Language.EN -> en
            Language.GA -> ga
            Language.ES -> es ?: en
        }
    }

    fun toError(lang: Language): RESTError {
        return RESTError(this.text(lang), this::class.simpleName!!)
    }
}

@Serializable
class RESTError(
    val message: String,
    val exceptionIdentifier: String
)

private const val publicUnexErrorMsgEn = "An internal error occurred. Please bear with us until we find a solution."
private const val publicUnexErrorMsgGa = "Tharla earráid inmheánach. Fan linn le do thoil go dtaga muid ar réiteach."

private const val publicAuthErrorMsgEn = "You must be logged in to execute this action."
private const val publicAuthErrorMsgGa = "Ní mór dhuit a bheith logáilte isteach leis an ngníomh seo a chur i gcrích."

class InternalClientRESTException(
    internalMessage: String,
    language: Language,
    override val cause: Throwable? = null
) : RESTException(
    en = publicUnexErrorMsgEn,
    ga = publicUnexErrorMsgGa,
    status = HttpStatus.BAD_REQUEST,
    defaultLanguage = language
) {
    override val message: String? = internalMessage
}

class InternalServerRESTException(
    internalMessage: String,
    override val cause: Throwable? = null
) : RESTException(
    en = publicUnexErrorMsgEn,
    ga = publicUnexErrorMsgGa,
    status = HttpStatus.INTERNAL_SERVER_ERROR
) {
    override val message: String? = internalMessage
}

class UnauthorisedRESTException(
    internalMessage: String = "You are not logged in.",
    override val cause: Throwable? = null
) : RESTException(
    en = publicAuthErrorMsgEn,
    ga = publicAuthErrorMsgGa,
    status = HttpStatus.UNAUTHORISED
) {
    override val message: String? = internalMessage
}

/** Representation for standard HTTP Error codes all of which are able to be handled by the Client Application.
 * The HTTP Error codes may be assigned for client side errors, namely 400 BAD_REQUEST, if say the client
 * fails to parse the response. */
enum class HttpStatus(val code: Int) {
    /** Error 400 */ BAD_REQUEST(400),
    /** Error 404 */ NOT_FOUND(404),
    /** Error 422 */ UNPROCESSABLE_ENTITY(422),
    /** Error 501 */ NOT_IMPLEMENTED(501),
    /** Error 401 */ UNAUTHORISED(401),
    /** Error 500 */ INTERNAL_SERVER_ERROR(500),
    /** Error 503 */ SERVICE_BUSY(503)
}