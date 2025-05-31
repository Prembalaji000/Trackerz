package com.example.portfolioapplication.showCase

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.portfolioapplication.showCase.theme.component.IntroShowcaseState
import com.example.portfolioapplication.showCase.theme.component.ShowcasePopup
import com.example.portfolioapplication.showCase.theme.component.ShowcaseStyle
import com.example.portfolioapplication.showCase.theme.component.introShowcaseTarget
import com.example.portfolioapplication.showCase.theme.component.rememberIntroShowcaseState

@Composable
fun IntroShowcase(
    showIntroShowCase: Boolean,
    onShowCaseCompleted: () -> Unit,
    state: IntroShowcaseState = rememberIntroShowcaseState(),
    dismissOnClickOutside: Boolean = false,
    content: @Composable IntroShowcaseScope.() -> Unit,
) {
    val scope = remember(state) {
        IntroShowcaseScope(state)
    }

    scope.content()

    if (showIntroShowCase) {
        ShowcasePopup(
            state = state,
            dismissOnClickOutside = dismissOnClickOutside,
            onShowCaseCompleted = onShowCaseCompleted,
        )
    }
}


class IntroShowcaseScope(
    private val state: IntroShowcaseState,
) {

    /**
     * Modifier that marks Compose UI element as a target for [IntroShowcase]
     */
    fun Modifier.introShowCaseTarget(
        index: Int,
        style: ShowcaseStyle = ShowcaseStyle.Default,
        content: @Composable BoxScope.() -> Unit,
    ): Modifier = introShowcaseTarget(
        state = state,
        index = index,
        style = style,
        content = content,
    )
}
