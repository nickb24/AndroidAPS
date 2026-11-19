package app.aaps.plugins.constraints.signatureVerifier

import app.aaps.core.interfaces.configuration.Config
import app.aaps.core.interfaces.notifications.NotificationManager
import app.aaps.core.interfaces.resources.ResourceHelper
import app.aaps.core.interfaces.utils.DateUtil
import app.aaps.core.interfaces.versionChecker.VersionDefinition
import app.aaps.core.keys.interfaces.Preferences
import app.aaps.plugins.constraints.versionChecker.VersionCheckerUtilsImpl
import app.aaps.plugins.constraints.versionChecker.numericVersionPart
import app.aaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.kotlin.whenever

@Suppress("SpellCheckingInspection")
class VersionCheckerUtilsKtTest : TestBase() {

    private lateinit var versionCheckerUtils: VersionCheckerUtilsImpl

    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var config: () -> Config
    @Mock lateinit var dateUtil: DateUtil
    @Mock lateinit var notificationManager: NotificationManager
    @Mock lateinit var preferences: Preferences

    private fun generateSupportedVersions(): String =
        "{\n" +
            "  \"Latest versions\":\"\",\n" +
            "  \"30\": \"3.3.1.4\",\n" +
            "  \"31\": \"3.3.1.4\",\n" +
            "  \"32\": \"3.3.1.4\",\n" +
            "  \"33\": \"3.3.1.4\",\n" +
            "  \"34\": \"3.3.1.4\",\n" +
            "  \"35\": \"3.3.1.4\",\n" +
            "  \"36\": \"3.3.1.4\",\n" +
            "  \"37\": \"3.3.1.4\",\n" +
            "  \"38\": \"3.3.1.4\",\n" +
            "  \"Expire dates\": \"\",\n" +
            "  \"3.3.1.3\": \"2025-05-31\"\n" +
            "}"

    @BeforeEach fun setup() {
        val definition = VersionDefinition { Json.parseToJsonElement(generateSupportedVersions()).jsonObject }
        versionCheckerUtils = VersionCheckerUtilsImpl(aapsLogger, preferences, rh, config, dateUtil, notificationManager, definition)
    }

    @Test
    fun `should handle invalid version`() {
        assertThat(versionCheckerUtils.versionDigits("definitely not version string")).isEmpty()
    }

    @Test
    fun `should handle empty version`() {
        assertThat(versionCheckerUtils.versionDigits("")).isEmpty()
    }

    @Test
    fun `should parse 2 digit version`() {
        assertThat(versionCheckerUtils.versionDigits("0.999-beta")).asList().containsExactly(0, 999).inOrder()
    }

    @Test
    fun `should parse 3 digit version`() {
        assertThat(versionCheckerUtils.versionDigits("6.83.93")).asList().containsExactly(6, 83, 93).inOrder()
    }

    @Test
    fun `should parse 4 digit version but only 3 digits are taken`() {
        assertThat(versionCheckerUtils.versionDigits("42.7.13.101")).asList().containsExactly(42, 7, 13).inOrder()
    }

    @Test
    fun `should parse 3 digit version with extra`() {
        assertThat(versionCheckerUtils.versionDigits("1.2.3-RC5")).asList().containsExactly(1, 2, 3).inOrder()
    }

    @Test
    fun `should parse version but only 3 digits are taken`() {
        assertThat(versionCheckerUtils.versionDigits("67.8.31.5.153.4.2")).asList().containsExactly(67, 8, 31).inOrder()
    }

    @Test
    fun `should keep 2 digit version`() {
        assertThat("1.2".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should keep 3 digit version`() {
        assertThat("1.2.3".numericVersionPart()).isEqualTo("1.2.3")
    }

    @Test
    fun `should strip 2 digit version RC`() {
        assertThat("1.2-RC1".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 2 digit version RC old format`() {
        assertThat("1.2RC1".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 2 digit version RC without digit`() {
        assertThat("1.2-RC".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 2 digit version dev`() {
        assertThat("1.2-dev".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 2 digit version dev old format 1`() {
        assertThat("1.2dev".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 2 digit version dev old format 2`() {
        assertThat("1.2dev-a3".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `should strip 3 digit version RC`() {
        assertThat("1.2.3-RC1".numericVersionPart()).isEqualTo("1.2.3")
    }

    @Test
    fun `should strip 3 digit version RC long`() {
        assertThat("1.2.3-RC5".numericVersionPart()).isEqualTo("1.2.3")
    }

    @Test
    fun `should strip even with dot`() {
        assertThat("1.2.RC5".numericVersionPart()).isEqualTo("1.2")
    }

    @Test
    fun `evaluateVersion always returns same version`() {
        val result = VersionCheckerUtilsImpl.VersionResult.SAME_VERSION
        assertThat(versionCheckerUtils.evaluateVersion(newVersion = "3.0.0", currentVersion = "2.2.2")).isEqualTo(result)
        assertThat(versionCheckerUtils.evaluateVersion(newVersion = "2.2.2", currentVersion = "2.3")).isEqualTo(result)
        assertThat(versionCheckerUtils.evaluateVersion(newVersion = null, currentVersion = "2.2.2")).isEqualTo(result)
        assertThat(versionCheckerUtils.evaluateVersion(newVersion = "2.2.2", currentVersion = "")).isEqualTo(result)
    }

    @BeforeEach
    fun `set time`() {
        whenever(dateUtil.now()).thenReturn(10000000000L)
        assertThat(dateUtil.now()).isEqualTo(10000000000L)

        whenever(rh.gs(anyInt(), anyString())).thenReturn("")
    }

}
