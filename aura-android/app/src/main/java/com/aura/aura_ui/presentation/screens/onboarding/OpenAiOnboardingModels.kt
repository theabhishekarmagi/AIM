package com.aura.aura_ui.presentation.screens.onboarding

import com.aura.aura_ui.agent.llm.ModelInfo

/**
 * Narrows OpenAI's model listing to multimodal GPT candidates for the onboarding agent.
 *
 * - Contract: only filters models actually returned by the provider; preserves order and metadata.
 * - Why: model-list vision hints also match audio and specialised models that do not support the
 *   agent's Chat Completions tool loop. Eligibility is a conservative naming filter, not a live
 *   guarantee of inference access or tool compatibility.
 */
internal object OpenAiOnboardingModels {

    private val excludedVariants = setOf(
        "realtime", "audio", "transcribe", "transcription", "tts", "image", "images",
        "search", "research", "codex", "pro", "instruct",
    )

    fun eligible(models: List<ModelInfo>): List<ModelInfo> = models.filter { model ->
        val id = model.id.lowercase()
        model.visionCapable && id.startsWith("gpt-") &&
            id.split('-', '_', '.').none { it in excludedVariants } &&
            !id.contains("chat-latest")
    }.distinctBy { it.id }

    /** Selects an eligible saved model, a modest default if offered, or the first candidate. */
    fun preferred(models: List<ModelInfo>, savedId: String?): String? {
        val candidates = eligible(models)
        return candidates.firstOrNull { it.id == savedId }?.id
            ?: candidates.firstOrNull { it.id == "gpt-4.1-mini" }?.id
            ?: candidates.firstOrNull()?.id
    }
}
