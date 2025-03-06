package ru.itmo.edugoolda.core.dialog.standard

import com.arkivanov.decompose.ComponentContext
import ru.itmo.edugoolda.core.dialog.simple.SimpleDialogControl
import ru.itmo.edugoolda.core.dialog.simple.fakeSimpleDialogControl
import ru.itmo.edugoolda.core.dialog.simple.simpleDialogControl

typealias StandardDialogControl = SimpleDialogControl<StandardDialogData>

fun ComponentContext.standardDialogControl(
    key: String
): StandardDialogControl {
    return simpleDialogControl(
        key = key,
        dismissableByUser = { data -> data.dismissableByUser }
    )
}

fun fakeStandardDialogControl(data: StandardDialogData = StandardDialogData.MOCK): StandardDialogControl {
    return fakeSimpleDialogControl(data)
}