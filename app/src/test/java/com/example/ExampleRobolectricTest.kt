package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ResourceCategory
import com.example.data.repository.CharityRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("The Love Life for Humanity Project", appName)
  }

  @Test
  fun `charity repository returns charities for valid zip code`() {
    val repository = CharityRepository()
    val results = repository.findCharitiesByZip("90001")
    assertTrue("Should find charities for 90001", results.isNotEmpty())

    val first = results.first()
    assertNotNull(first.name)
    assertNotNull(first.phone)
    assertTrue("Phone number should not be empty", first.phone.isNotBlank())
    assertTrue("Address should not be empty", first.address.isNotBlank())
  }

  @Test
  fun `charity repository bookmark toggling works`() {
    val repository = CharityRepository()
    val testId = "nat-211"
    assertFalse(repository.isBookmarked(testId))

    repository.toggleBookmark(testId)
    assertTrue(repository.isBookmarked(testId))

    repository.toggleBookmark(testId)
    assertFalse(repository.isBookmarked(testId))
  }

  @Test
  fun `zip prefix mapping correctly resolves states`() {
    assertEquals("CA", CharityRepository.getStateForZipPrefix("90"))
    assertEquals("IL", CharityRepository.getStateForZipPrefix("60"))
    assertEquals("NY", CharityRepository.getStateForZipPrefix("10"))
    assertEquals("TX", CharityRepository.getStateForZipPrefix("77"))
  }

  @Test
  fun `social media app url and default message are valid`() {
    assertTrue(com.example.ui.util.IntentUtils.DEFAULT_APP_URL.startsWith("https://"))
    assertTrue(com.example.ui.util.IntentUtils.DEFAULT_SHARE_MESSAGE.contains("The Love Life for Humanity Project"))
    assertTrue(com.example.ui.util.IntentUtils.DEFAULT_SHARE_MESSAGE.contains("Tiffany K Casteel"))
  }

  @Test
  fun `printable resource guide generation includes title creator and hotlines`() {
    val repository = CharityRepository()
    val charities = repository.findCharitiesByZip("90001")
    val textGuide = com.example.ui.print.ResourceGuideHelper.generatePlainTextGuide("90001", charities)

    assertTrue(textGuide.contains("THE LOVE LIFE FOR HUMANITY PROJECT"))
    assertTrue(textGuide.contains("Tiffany K Casteel"))
    assertTrue(textGuide.contains("2-1-1"))
    assertTrue(textGuide.contains("INTAKE & DOCUMENTATION CHECKLIST"))

    val htmlGuide = com.example.ui.print.ResourceGuideHelper.generateHtmlPrintDocument("90001", charities)
    assertTrue(htmlGuide.contains("The Love Life for Humanity Project"))
    assertTrue(htmlGuide.contains("Tiffany K Casteel"))
    assertTrue(htmlGuide.contains("Universal Intake Preparation Checklist"))
  }

  @Test
  fun `veterans category returns dedicated military and family resources`() {
    val repository = CharityRepository()
    val allCharities = repository.findCharitiesByZip("ALL")
    val veteranCharities = allCharities.filter { it.categories.contains(ResourceCategory.VETERANS) }

    assertTrue("Should include veteran resources", veteranCharities.isNotEmpty())
    val names = veteranCharities.map { it.name }
    assertTrue(names.any { it.contains("Veterans Crisis Line") })
    assertTrue(names.any { it.contains("Homeless Veterans") })
    assertTrue(names.any { it.contains("VFW") })
    assertTrue(names.any { it.contains("Operation Homefront") })
    assertTrue(names.any { it.contains("Wounded Warrior") })

    // Check intake checklist has DD-214 guidance
    val checklist = com.example.ui.print.ResourceGuideHelper.getUniversalIntakeChecklist()
    assertTrue(checklist.any { it.title.contains("Veterans") && it.description.contains("DD-214") })
  }

  @Test
  fun `build config has bumped version code and version name`() {
    assertEquals(3, BuildConfig.VERSION_CODE)
    assertEquals("3.0", BuildConfig.VERSION_NAME)
  }
}
