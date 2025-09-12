package ru.itmo.edugoolda.features.lesson.presentation.createLesson

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itmo.edugoolda.core.theme.AppTheme
import ru.itmo.edugoolda.core.theme.custom.CustomTheme
import ru.itmo.edugoolda.core.widget.button.AppButton
import ru.itmo.edugoolda.core.widget.button.ButtonType
import ru.itmo.edugoolda.core.widget.text_field.AppTextField
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonType
import ru.itmo.edugoolda.features.lesson.R

@Composable
fun CreateLessonUi(
    component: CreateLessonComponent,
    modifier: Modifier = Modifier,
) {
    val groupListState by component.groupListState.collectAsState()
    val selectedType by component.selectedLessonType.collectAsState()
    val isCreateLessonButtonEnabled by component.isCreateLessonButtonEnabled.collectAsState()

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(modifier = modifier
        .navigationBarsPadding()
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(50.dp + statusBarHeight)
                .background(CustomTheme.colors.content.contentActive)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { component.onReturnBackClick() },
                Modifier.padding(top = 3.dp + statusBarHeight, bottom = 6.dp, start = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left_white),
                    contentDescription = "Arrow Left",
                    tint = Color.Unspecified
                )
            }

            Text(
                text = stringResource(R.string.lesson_create_title),
                modifier = Modifier.padding(
                    start = 30.dp,
                    top = 3.dp + statusBarHeight,
                    bottom = 6.dp
                ),
                fontWeight = CustomTheme.typography.title.bold.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                color = CustomTheme.colors.text.invert
            )
        }

        AppTextField(
            inputControl = component.lessonNameInputControl,
            placeholder = stringResource(id = R.string.lesson_placeholder_lesson_name),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(top = 25.dp, bottom = 16.dp),
            minLines = 1,
            maxLines = 1
        )

        AppTextField(
            inputControl = component.descriptionInputControl,
            placeholder = stringResource(id = R.string.lesson_placeholder_lesson_description),
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(bottom = 15.dp),
            minLines = 5,
            maxLines = 5
        )

        Text(
            text = stringResource(id = R.string.lesson_create_type_title),
            fontWeight = CustomTheme.typography.title.boldSmallerSize.fontWeight,
            fontSize = CustomTheme.typography.title.boldSmallerSize.fontSize,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 21.dp)
        )

        Column(
            modifier = Modifier.padding(start = 8.dp, top = 6.dp),
        ) {
            LessonType.entries.forEach {
                LessonTypeItem(
                    isSelected = it == selectedType,
                    lessonType = it,
                    onClick = { component.onLessonTypeSelect(it) }
                )
            }
        }

        AppButton(
            onClick = { component.onAddGroupButtonClick() },
            text = stringResource(R.string.lesson_add_group_button),
            buttonType = ButtonType.Primary,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 10.dp)
                .padding(horizontal = 105.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 18.dp)
        )

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .padding(horizontal = 10.dp)
                .weight(1f)
        ) {
            items(groupListState) { item ->
                GroupItem(
                    { component.onGroupDeleteClick(item.id) },
                    item.name
                )
            }
        }

        AppButton(
            onClick = { component.onCreateLesson() },
            text = stringResource(R.string.lesson_create_button),
            buttonType = ButtonType.Primary,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .padding(horizontal = 100.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            isEnabled = isCreateLessonButtonEnabled,
            contentPadding = PaddingValues(vertical = 14.dp, horizontal = 18.dp)
        )
    }
}

@Composable
fun LessonTypeItem(
    isSelected: Boolean,
    lessonType: LessonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(
        when (lessonType) {
            LessonType.Informational -> R.string.lesson_create_type_informational
            LessonType.Practical -> R.string.lesson_create_type_practical
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
        )
        Text(text = text,
            modifier = Modifier.clickable {
                onClick()
            })
    }
}

@Composable
fun GroupItem(
    onGroupDelete: () -> Unit,
    name: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            )
            .clip(RoundedCornerShape(8.dp))
            .background(CustomTheme.colors.background.backgroundPrimary)
    ) {
        Row(
            modifier = Modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Group Icon",
                modifier = Modifier
                    .padding(start = 15.dp, end = 10.dp)
                    .size(27.dp)
            )

            Text(
                text = name,
                fontWeight = CustomTheme.typography.body.regular.fontWeight,
                fontSize = CustomTheme.typography.body.regular.fontSize,
                modifier = Modifier
                    .padding(end = 30.dp)
                    .weight(1f)
                    .basicMarquee()

            )

            IconButton(
                onClick = { onGroupDelete() }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Group Icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LessonTypeItemPreview() {
    AppTheme {
        LessonTypeItem(
            isSelected = true,
            onClick = {},
            modifier = Modifier,
            lessonType = LessonType.Practical
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateLessonUiPreview() {
    AppTheme {
        CreateLessonUi(component = FakeCreateLessonComponent())
    }
}