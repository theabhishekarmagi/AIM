# Local Codex and Android Studio handoff

Verified baseline: `b870a2b2cc1789ca2fbf7993cb26ebc1a372040e`, 28 October 2026.
Repository: https://github.com/theabhishekarmagi/AIM

## Product and current scope

AIM is an Android phone agent that observes the screen and operates apps using accessibility,
screenshots, taps, typing, scrolling, and navigation. The intended product uses a dedicated
second Android phone with its own SIM/number and consumer WhatsApp account. The available
first test device is the user's primary OnePlus running Android 14; its exact model is unknown.

The active codebase is an imported AURA application. Read `SOURCE_PROVENANCE.md`, `LICENSE`,
and `LICENSE-EXCEPTION.md`; preserve the existing attribution and license notices during a
redesign. This is a native Kotlin/Jetpack Compose application. Agent orchestration and device
tools run on the phone; model inference uses external APIs.

The user chose **GPT through the OpenAI API** for initial testing. Onboarding accepts an OpenAI
key, fetches GPT model candidates, and saves the selected model. It does not require Gemini and
disables Gemini Live when saving this setup. Other reasoning providers remain selectable under
Agent → Brain. OpenAI speech recognition/realtime voice has not been added; start with typed
commands. A ChatGPT subscription does not include API billing.

Artemis is outside the current implementation. No Python environment or desktop MCP bridge is
needed for a phone-only test. Do not introduce Twilio or replacement cloud telephony.

The next work is **local Android Studio setup, then the user's UI redesign and requested
behavior changes**. Specific screens, visual direction, and behavior changes have not yet
been supplied. Inspect the current app and clarify those choices before replacing screens.

## Get the project locally

1. Install Git, local Codex, and a current stable Android Studio that supports AGP 8.9.1 and
   Android API 36. Choose installers for the computer's OS and CPU architecture.
2. In a local terminal, clone into your chosen development directory:

   ```text
   git clone https://github.com/theabhishekarmagi/AIM.git
   ```

   If the repository already exists locally, check its remote and working tree before updating
   it. Keep any local edits; do not reset or overwrite the folder.
3. Open the **AIM repository root** in local Codex so it can read `AGENTS.md` and these documents.
4. In Android Studio, choose Open and select **AIM/aura-android**, which contains
   `settings.gradle.kts`. Use the existing Gradle wrapper, version **8.13**.
5. In Android Studio's SDK Manager install **Android SDK Platform 36**, **Build-Tools 35.0.0**,
   **Platform-Tools/ADB**, and command-line tools. Build-Tools 35.0.0 is the verified build
   environment version; the project does not explicitly pin `buildToolsVersion`. An emulator
   image is optional; the physical
   OnePlus can be used directly. KVM is a Linux virtualization facility, not a requirement for
   building an APK or running it on a physical phone.
6. Set **Gradle JDK to JDK 17**. A newer JDK bundled with Android Studio may be selected by
   default; use a separate JDK 17 installation/download for the verified build setup. Terminal
   builds must also use JDK 17 through their local Java configuration.
7. Let Android Studio generate `aura-android/local.properties` with this computer's SDK path.
   Do not copy the cloud SDK path or its proxy/trust configuration to the local computer.
8. Copy `aura-android/app/google-services.example.json` to
   `aura-android/app/google-services.json`. This ignored placeholder lets debug builds compile
   and skip Google sign-in; Firebase-backed features remain unavailable. Provider keys belong
   in the installed app, not source files or chat.
9. Sync Gradle, select the **app** run configuration and **debug** build variant, then build.
   Decline unrelated AGP/Kotlin/Gradle upgrade suggestions during the initial setup so the first
   local build can be compared with the verified baseline.

The cloud SDK, JDK, Gradle caches, ignored local configuration, and debug signing key are not
part of the Git repository. Local Codex needs this handoff; a new local chat should not be
assumed to contain the cloud chat history.

### macOS setup

The user is continuing on **macOS**. Install Android Studio for **Apple silicon** on an M-series
Mac, or **Intel** on an Intel Mac. Use the corresponding architecture for JDK 17.

In Terminal, first navigate to the development directory you want to use, then:

```bash
git clone https://github.com/theabhishekarmagi/AIM.git
cd AIM
cp aura-android/app/google-services.example.json aura-android/app/google-services.json
```

These commands assume a fresh clone. Open `AIM` as the local Codex workspace and paste the
continuation prompt below. Open `AIM/aura-android` in Android Studio. Under Android Studio's
**Settings → Build, Execution, Deployment → Build Tools → Gradle**, select **JDK 17** as the
Gradle JDK. Let SDK Manager and Gradle sync complete before running the app.

Android Studio normally creates the SDK at `~/Library/Android/sdk`; use the path actually
shown by SDK Manager. It belongs in the ignored `local.properties`, not in committed build
files. No KVM installation is needed on macOS. Start with the physical OnePlus connected by
USB; an Android emulator can be configured later if needed.

## Build and test

Run from `AIM/aura-android`:

```bash
# macOS or Linux: the tracked wrapper script is not executable, so invoke it through bash.
bash gradlew :app:assembleDebug
bash gradlew :app:testDebugUnitTest :mcp-server:test
```

```powershell
# Windows PowerShell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest :mcp-server:test
```

The first build downloads Gradle/dependencies and the pinned sherpa-onnx AAR. Output:
`aura-android/app/build/outputs/apk/debug/app-debug.apk`.

On the OnePlus, enable Developer options and USB debugging only when using Android Studio's
USB Run workflow. Connect a data-capable cable, approve this computer's debugging prompt,
select the phone in Android Studio, and Run. On Windows, install the appropriate USB driver if
the phone is not detected. Building and manually sideloading an APK does not need USB debugging.
For onboarding and the first read-only tasks, follow [Phone testing](PHONE_TESTING.md).

The app still appears as **AURA Assistant**. The debug package is
`com.aura.aura_ui.feature.debug`; minimum Android API is 26 and target/compile API is 36.
The local debug signing key will differ from the GitHub Actions key. If a CI debug APK is
already installed, Android can reject the local update with a signature mismatch. Uninstalling
the debug app deletes its saved key/configuration and requires setup again; choose that
explicitly on the phone rather than silently deleting app data. Keep using the same local
debug signing key for subsequent local updates.

## UI files for the redesign

Read `AGENTS.md`, the root `README.md`, and
`aura-android/app/src/main/java/com/aura/aura_ui/presentation/README.md` before UI edits.
Paths below are relative to `aura-android/app/src/main/java/com/aura/aura_ui/`:

| Area | Entry point |
| --- | --- |
| Activity entry | `MainActivity.kt` |
| App navigation | `presentation/navigation/AuraAppShell.kt` |
| Home | `presentation/screens/home/AuraHomeScreen.kt` |
| Onboarding and GPT setup | `presentation/screens/onboarding/OnboardingScreen.kt` |
| Agent hub | `presentation/screens/agent/AgentHubScreen.kt` |
| Provider/model settings | `presentation/screens/ProviderSettingsScreen.kt` |
| App settings | `presentation/screens/settings/MonoSettingsScreen.kt` |
| Colors, typography, shapes | `ui/theme/`, particularly `Mono.kt`, `Capsule.kt`, `Theme.kt`, `Type.kt` |
| Shared UI components | `presentation/components/` |
| Floating assistant | `presentation/screens/VoiceAssistantOverlay.kt` |
| Running-task controls | `presentation/overlay/` |
| Overlay lifecycle | `overlay/AuraOverlayService.kt` |

The floating assistant is hosted by a service and has **no Activity window**. Its composables
cannot assume Activity-scoped ViewModels, `hiltViewModel()`, or Activity window access. Use
Android Studio's Run workflow for full behavior; Compose previews do not establish permission,
overlay, accessibility, or AI behavior. Keep pause/stop controls available during a redesign.

## Verified evidence and limits

[GitHub CI run 40687142988](https://github.com/theabhishekarmagi/AIM/actions/runs/40687142988)
built the debug APK and passed these suites on the baseline commit:

| Suite | Tests | Failures/errors/skips |
| --- | ---: | --- |
| app `testDebugUnitTest` | 1,896 | 0/0/0 |
| mcp-server `testDebugUnitTest` | 588 | 0/0/0 |
| mcp-server `testReleaseUnitTest` | 588 | 0/0/0 |

There were **3,072 executions**, representing **2,484 unique tests** because the MCP variants
run the same cases. The app total includes seven GPT onboarding model-selection tests.
The Linux cloud rerun also passed all three suites and the local APK signature check.

The initial cloud app-test attempt had 352 Robolectric runtime-download failures caused by a
forked JVM missing the platform proxy settings. A task-local Gradle init script forwarded the
existing proxy and trusted certificate configuration; the rerun passed without test/source
changes or permanent policy changes. This workaround is specific to that restricted cloud
environment and is not a local-computer prerequisite.

Android Studio setup on the user's computer, physical OnePlus installation/permissions,
live OpenAI inference/billing, WhatsApp, real SIM speech, and background reliability remain
unverified. No real messages, calls, bookings, or purchases were performed during these checks.
Local UI work should use Settings or disposable test apps until device behavior is established.

## Prompt for the first local Codex chat

```text
Continue AIM from docs/LOCAL_DEVELOPMENT_HANDOFF.md. Read AGENTS.md, README.md,
SOURCE_PROVENANCE.md, docs/PHONE_TESTING.md, and the presentation README first.
We are moving development from cloud to this local checkout so I can use Android Studio
to redesign the Kotlin/Jetpack Compose app and make behavior changes.
Verify the local JDK 17, SDK 36/Build-Tools 35, Gradle wrapper 8.13, and debug Firebase
placeholder setup, then build the existing app before editing the UI.
Use GPT through the OpenAI API; do not require Gemini or reintroduce Artemis/Twilio.
Preserve attribution/license notices. Do not read or print provider API keys.
My available phone is a OnePlus running Android 14, and it is my primary phone.
Keep first tests supervised and read-only. Do not send messages, place calls, make
bookings/purchases, wipe app data, or change signing/package identity without my instruction.
After setup, ask which screens and behaviors I want to change, then implement the redesign
in this local checkout and verify it on the phone with me.
```
