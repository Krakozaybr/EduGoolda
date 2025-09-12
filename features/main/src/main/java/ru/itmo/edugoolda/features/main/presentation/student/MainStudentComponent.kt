package ru.itmo.edugoolda.features.main.presentation.student

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.itmo.edugoolda.data.group.group_list.api.GroupId
import ru.itmo.edugoolda.data.lesson.lesson_details.api.LessonId
import ru.itmo.edugoolda.features.group.presentation.studentGroups.StudentGroupsComponent
import ru.itmo.edugoolda.features.home.presentation.student.HomeStudentComponent
import ru.itmo.edugoolda.features.lesson.presentation.studentLessonList.LessonInfoListStudentComponent
import ru.itmo.edugoolda.features.main.R
import ru.itmo.edugoolda.features.profile.presentation.viewProfile.ProfileComponent
import ru.itmo.edugoolda.core.R as CoreR

interface MainStudentComponent {

    val selectedTab: StateFlow<Tab>
    val stack: StateFlow<ChildStack<*, Child>>

    fun onTabClick(tab: Tab)

    enum class Tab(
        @StringRes val titleResId: Int,
        @DrawableRes val iconResId: Int
    ) {
        Home(R.string.main_tab_home, CoreR.drawable.ic_24_home),
        Lessons(R.string.main_tab_lessons, CoreR.drawable.ic_24_lessons),
        Groups(R.string.main_tab_groups, CoreR.drawable.ic_24_groups),
        Profile(R.string.main_tab_profile, CoreR.drawable.ic_24_profile)
    }

    sealed interface Child {
        data class Home(val component: HomeStudentComponent) : Child
        data class Lessons(val component: LessonInfoListStudentComponent) : Child
        data class Groups(val component: StudentGroupsComponent) : Child
        data class Profile(val component: ProfileComponent) : Child
    }

    interface Communication {
        fun onGroupDetailsRequested(id: GroupId)

        fun onGroupAddRequested()

        fun onAllJoinRequestsRequested()

        fun onLessonDetailsRequested(lessonId: LessonId)

        fun onLogoutRequested()
    }
}