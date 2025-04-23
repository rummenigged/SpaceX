package com.octopus.edu.kotlin.spacex.konsist

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ComposeKonsistTest {
    @Test
    fun `validate all compose preview function name ends with preview`() {
        Konsist
            .scopeFromProject()
            .functions()
            .withAnnotationOf(Preview::class, PreviewLightDark::class)
            .assertTrue { declaration ->
                declaration.name.endsWith("Preview")
            }
    }

    @Test
    fun `validate all compose preview function are private`() {
        Konsist
            .scopeFromProject()
            .functions()
            .withAnnotationOf(Preview::class, PreviewLightDark::class)
            .assertTrue { declaration -> declaration.hasModifier(KoModifier.PRIVATE) }
    }
}
