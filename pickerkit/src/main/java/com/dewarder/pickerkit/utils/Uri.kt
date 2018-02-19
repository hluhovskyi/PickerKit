package com.dewarder.pickerkit.utils

import android.net.Uri

fun Uri.withoutLastSegment(): Uri =
        Uri.parse(this.toString().removeSuffix(this.lastPathSegment))