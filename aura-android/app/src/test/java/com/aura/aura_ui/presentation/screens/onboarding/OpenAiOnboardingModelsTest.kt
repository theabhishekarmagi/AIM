package com.aura.aura_ui.presentation.screens.onboarding

import com.aura.aura_ui.agent.llm.ModelInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OpenAiOnboardingModelsTest {

    @Test
    fun `accepts standard multimodal GPT families and dated snapshots`() {
        val ids = listOf(
            "gpt-4o", "gpt-4o-mini", "gpt-4o-2024-11-20", "gpt-4.1-mini",
            "gpt-4.1-2025-04-14", "gpt-5", "gpt-5-mini", "gpt-5.2-2026-01-15",
        )

        assertEquals(ids, OpenAiOnboardingModels.eligible(ids.map(::model)).map { it.id })
    }

    @Test
    fun `rejects specialised variants even when catalog labels them vision capable`() {
        val ids = listOf(
            "gpt-4o-realtime-preview", "gpt-4o-audio-preview", "gpt-4o-mini-transcribe",
            "gpt-4o-transcription", "gpt-4o-mini-tts", "gpt-image-1", "gpt-5-image-mini",
            "gpt-4o-search-preview", "gpt-5.2-deep-research", "gpt-5.2-codex",
            "gpt-5-pro", "gpt-5.2-pro-2026-01-15", "gpt-5-chat-latest", "gpt-4o-instruct",
        )

        assertEquals(emptyList<ModelInfo>(), OpenAiOnboardingModels.eligible(ids.map(::model)))
    }

    @Test
    fun `requires GPT prefix and vision support`() {
        val models = listOf(
            model("gpt-4.1-mini").copy(visionCapable = false),
            model("o3"), model("gemini-3-flash"), model("vendor/gpt-4.1-mini"),
            model("chatgpt-4o-latest"), model("text-embedding-3-large"),
        )

        assertEquals(emptyList<ModelInfo>(), OpenAiOnboardingModels.eligible(models))
    }

    @Test
    fun `preserves fetched metadata and order while deduplicating ids`() {
        val first = model("gpt-5").copy(contextWindow = 100_000, reasoningCapable = true)
        val second = model("gpt-4.1-mini")

        assertEquals(
            listOf(first, second),
            OpenAiOnboardingModels.eligible(listOf(first, second, first.copy(contextWindow = 200_000))),
        )
    }

    @Test
    fun `retains eligible saved selection before the default`() {
        assertEquals(
            "gpt-5-mini",
            OpenAiOnboardingModels.preferred(listOf(model("gpt-4.1-mini"), model("gpt-5-mini")), "gpt-5-mini"),
        )
    }

    @Test
    fun `ignores missing or unsuitable saved selection and prefers returned default`() {
        val models = listOf(model("gpt-5"), model("gpt-4o-audio-preview"), model("gpt-4.1-mini"))

        for (saved in listOf(null, "gpt-removed", "gpt-4o-audio-preview")) {
            assertEquals("gpt-4.1-mini", OpenAiOnboardingModels.preferred(models, saved))
        }
    }

    @Test
    fun `does not invent a default absent from the account listing`() {
        assertEquals("gpt-5-mini", OpenAiOnboardingModels.preferred(listOf(model("gpt-5-mini")), null))
        assertNull(OpenAiOnboardingModels.preferred(emptyList(), "gpt-4.1-mini"))
        assertNull(OpenAiOnboardingModels.preferred(listOf(model("gpt-4o-audio-preview")), null))
    }

    private fun model(id: String): ModelInfo = ModelInfo(id = id, visionCapable = true)
}
