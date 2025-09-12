package ru.itmo.edugoolda.features.main.presentation.teacher

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.core.dialog.simple.SimpleDialogControl
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.data.solutions.api.SolutionId
import ru.itmo.edugoolda.features.group.presentation.teacherGroups.TeacherGroupsComponent
import ru.itmo.edugoolda.features.home.presentation.teacher.HomeTeacherComponent
import ru.itmo.edugoolda.features.lesson.presentation.teacherLessonList.LessonInfoListTeacherComponent
import ru.itmo.edugoolda.features.main.R
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileComponent
import ru.itmo.edugoolda.core.R as CoreR

interface MainTeacherComponent {

    val selectedTab: StateFlow<Tab>
    val simpleDialogControl: SimpleDialogControl<Unit>
    val stack: StateFlow<ChildStack<*, Child>>

    fun onTabClick(tab: Tab)
    fun onCreateLessonClick()
    fun onCreateGroupClick()

    enum class Tab(
        @StringRes val titleResId: Int,
        @DrawableRes val iconResId: Int
    ) {
        Home(R.string.main_tab_home, CoreR.drawable.ic_24_home),
        Create(R.string.main_tab_create, CoreR.drawable.ic_24_add),
        Lessons(R.string.main_tab_lessons, CoreR.drawable.ic_24_lessons),
        Groups(R.string.main_tab_groups, CoreR.drawable.ic_24_groups),
        Profile(R.string.main_tab_profile, CoreR.drawable.ic_24_profile)
    }

    sealed interface Child {
        data class Home(val component: HomeTeacherComponent) : Child
        data class Groups(val component: TeacherGroupsComponent) : Child
        data class Profile(val component: ProfileComponent) : Child
        data class Lessons(val component: LessonInfoListTeacherComponent) : Child
    }

    interface Communication {
        fun onGroupDetailsRequested(id: GroupId)
        fun onSolutionDetailsRequested(solutionId: SolutionId)
        fun onAllSolutionsRequested()
        fun onAllJoinRequestsRequested()
        fun createLessonRequested()
        fun createGroupRequested()
        fun onLessonDetailsRequested(lessonId: LessonId)
        fun onLogoutRequested()
    }
}