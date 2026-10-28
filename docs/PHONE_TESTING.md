# First phone test

This checkout uses an OpenAI API key and a fetched GPT model for the agent. Gemini is not
required during setup. Start with typed commands; OpenAI speech recognition and realtime
voice have not been added. Build success and host unit tests do not establish phone behavior.

## What you need

- An Android phone running Android 8 or newer. A OnePlus running Android 14 meets this version
  requirement; exact model and OxygenOS behavior still need testing.
- Internet access and an OpenAI API key with model access and API billing available. A ChatGPT
  subscription does not include API usage. Enter the key only in the app.
- The APK from this repository's successful **Android debug APK** Actions run. Sign into
  GitHub, open the run, download the `AIM-debug` artifact, and extract `AIM-debug.apk`.
  The archive also contains its SHA-256 checksum and source commit.

The app still appears as **AURA**. No root, bootloader unlock, desktop bridge, USB debugging,
or Android SDK installation on the phone is needed. The debug package is
`com.aura.aura_ui.feature.debug`.

## Install and configure

1. Install the APK, allowing installation from that browser or file app when Android asks.
2. Open AURA and choose **Skip (debug build)** at Google sign-in. The debug build uses
   placeholder Firebase configuration; Firebase features are unavailable.
3. If Android blocks enabling Accessibility, open **App info → ⋮ → Allow restricted settings**,
   then return to setup. Menu names can vary by OxygenOS build.
4. Follow setup for Accessibility, microphone, display over other apps, AURA Keyboard,
   notifications, and notification access. The keyboard needs enabling, not permanent selection
   as the default. These permissions give the app broad access; use a supervised first test.
5. Enter your OpenAI API key, tap **Fetch GPT models**, choose a model, and tap **Use this GPT
   model**. Only fetched candidate models are offered. Model listing checks key access; a real
   inference request is still needed to establish model compatibility and billing.
6. The final restricted-app scan sends installed-app package names and labels to the selected
   OpenAI model. Review the restricted-app list before launching AURA.

The setup disables Gemini Live. Other reasoning providers remain available under **Agent → Brain**.
Leave wake-word and voice testing for later. Keep the phone unlocked, screen on, and open the
assistant from the visible app for this test.

## First commands

Use **Talk to AURA** and its text input:

> Open Android Settings, navigate to Battery, and report the battery percentage and charging
> status displayed there. Do not change any settings.

Watch whether the phone actually opens the requested screen and compare the answer with what
is visible. A result returned from device-status APIs alone does not establish screen navigation.

Then test typing and navigation:

> Open Android Settings and use its search field to search for “display”. Show me the results.
> Do not change any settings.

While a task is running, test the pause pill and confirm actions stop. Keep initial tests to
Settings or a disposable test app. Calls, messages, bookings, purchases, personal-account
workflows, WhatsApp integration, background reliability, and real SIM speech remain unverified.

After testing, disable AURA Accessibility and notification access if you are finished using it.
Record the exact OnePlus model, Android/OxygenOS version, selected GPT model, any failing setup
step, and what the phone visibly did. Avoid sharing raw logs containing keys or other apps' data.

## Test-build updates

Hosted builds currently generate a fresh debug signing key for each run. Installing a later
artifact can require uninstalling the earlier debug app, which deletes its stored configuration.
Stable signing is a separate setup task. Actions artifacts expire after 14 days.
