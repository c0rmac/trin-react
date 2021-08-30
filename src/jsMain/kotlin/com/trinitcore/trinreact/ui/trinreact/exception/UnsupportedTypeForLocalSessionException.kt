package com.trinitcore.trinreact.ui.trinreact.exception

class UnsupportedTypeForLocalSessionException(value: Any)
    : Throwable("The value $value could not be saved to local session. Its class type ${value::class.simpleName} is unsupported.")