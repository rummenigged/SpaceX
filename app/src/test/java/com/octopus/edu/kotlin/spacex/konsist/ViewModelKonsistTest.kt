package com.octopus.edu.kotlin.spacex.konsist

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentOf
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertTrue
import com.octopus.edu.kotlin.spacex.feature.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.junit.Test
import javax.inject.Inject

class ViewModelKonsistTest {
    @Test
    fun `validate view model class name`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(BaseViewModel::class)
            .assertTrue { declaration -> declaration.name.endsWith("ViewModel") }
    }

    @Test
    fun `validate all view model extends from BaseViewModel`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withoutName(BaseViewModel::class.simpleName ?: "")
            .withNameEndingWith("ViewModel")
            .assertTrue { declaration -> declaration.hasParentOf(BaseViewModel::class) }
    }

    @Test
    fun `validate view model constructor and parameters`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(BaseViewModel::class)
            .assertTrue { declaration ->
                val hashSingleContructor =
                    declaration
                        .constructors
                        .let { it.size == 1 }

                val constructorParametersHavePrivateModifier =
                    declaration
                        .constructors
                        .first()
                        .parameters
                        .all {
                            it.modifiers.isEmpty() ||
                                (it.isVal && it.hasModifier(KoModifier.PRIVATE))
                        }

                hashSingleContructor && constructorParametersHavePrivateModifier
            }
    }

    @Test
    fun `validate view model constructor has inject annotations`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(BaseViewModel::class)
            .assertTrue { declaration ->
                val hasClassAnnotation = declaration.hasAnnotationOf(HiltViewModel::class)
                val hasConstructorAnnotation =
                    declaration
                        .constructors
                        .first()
                        .hasAnnotationOf(Inject::class)

                hasClassAnnotation && hasConstructorAnnotation
            }
    }
}
