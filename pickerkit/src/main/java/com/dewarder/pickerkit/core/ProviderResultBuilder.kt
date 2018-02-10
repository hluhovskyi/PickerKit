package com.dewarder.pickerkit.core

interface ProviderResultBuilder {

    fun push(result: Result): ProviderResultBuilder

    fun commit()
}