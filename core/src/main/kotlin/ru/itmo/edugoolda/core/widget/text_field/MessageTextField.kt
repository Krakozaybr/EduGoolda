package ru.itmo.edugoolda.core.widget.text_field

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.mobileup.kmm_form_validation.control.InputControl

@Composable
fun MessageTextField(
    onSendClick: () -> Unit,
    inputControl: InputControl,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    textStyle: TextStyle = AppTextFieldDefaults.textStyle,
    onFocusChange: (Boolean) -> Unit = {},
) {
    val text by inputControl.text.collectAsState()

    val currentValue by rememberUpdatedState(text)
    var currentSelection by rememberSaveable(stateSaver = TextRangeSaver) {
        mutableStateOf(TextRange(0))
    }
    var currentComposition by rememberSaveable(stateSaver = NullableTextRangeSaver) {
        mutableStateOf(null)
    }
    val currentTextFieldValue by remember {
        derivedStateOf {
            TextFieldValue(currentValue, currentSelection, currentComposition)
        }
    }

    val focusRequester = remember { FocusRequester() }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (currentTextFieldValue.text.isNotEmpty()) 1f else 0.5f,
        animationSpec = tween(durationMillis = 200), label = ""
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = currentTextFieldValue,
                onValueChange = {
                    inputControl.onTextChanged(it.text)
                    currentSelection = it.selection
                    currentComposition = it.composition
                },
                maxLines = 1,
                textStyle = textStyle,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocusChange(it.isFocused) },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (currentTextFieldValue.text.isEmpty()) {
                            placeholder?.let {
                                Text(
                                    text = it,
                                    color = CustomTheme.colors.text.secondary
                                )
                            }
                        }
                        innerTextField()
                    }
                }
            )

            IconButton(
                onClick = onSendClick,
                enabled = currentTextFieldValue.text.isNotEmpty(),
                modifier = Modifier.alpha(animatedAlpha)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Отправить",
                    tint =
                    if (currentTextFieldValue.text.isNotEmpty()) {
                        CustomTheme.colors.content.contentActive
                    } else {
                        CustomTheme.colors.icon.secondary
                    }
                )
            }
        }
    }
}

private val TextRangeSaver = listSaver(
    save = { listOf(it.start, it.end) },
    restore = { TextRange(it[0], it[1]) }
)

private val NullableTextRangeSaver = listSaver<TextRange?, Int>(
    save = { if (it != null) listOf(it.start, it.end) else emptyList() },
    restore = { TextRange(it[0], it[1]) }
)