package com.example.fish.common.exception

import com.example.fish.common.viewstate.ViewError

data class ViewErrorException(val viewError: ViewError) : Exception()