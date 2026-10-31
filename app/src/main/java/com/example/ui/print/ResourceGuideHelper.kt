package com.example.ui.print

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.data.model.Charity
import com.example.ui.util.IntentUtils

object ResourceGuideHelper {

    data class ChecklistItem(
        val title: String,
        val description: String,
        val isEssential: Boolean
    )

    fun getUniversalIntakeChecklist(): List<ChecklistItem> {
        return listOf(
            ChecklistItem(
                title = "Emergency Food Pantries: Zero ID Required",
                description = "Most community food pantries & soup kitchens do NOT require photo ID or proof of income to receive emergency groceries or hot meals.",
                isEssential = true
            ),
            ChecklistItem(
                title = "Photo Identification (If Available)",
                description = "State ID, driver's license, passport, school ID, or consular card. If you do not have an ID, staff can often assist with no-cost ID replacement waivers.",
                isEssential = false
            ),
            ChecklistItem(
                title = "Proof of Address / Residency (For Utility & Rent Grants)",
                description = "Any recent piece of mail, lease agreement, or utility bill showing your name and current address in the service territory.",
                isEssential = false
            ),
            ChecklistItem(
                title = "Utility Disconnect Notice (For Emergency LIHEAP Assistance)",
                description = "If you have a past-due electric, gas, or water bill or a shut-off warning notice, bring the physical notice or account number.",
                isEssential = false
            ),
            ChecklistItem(
                title = "Household Income or Benefit Proof (If Applying for Subsidies)",
                description = "Pay stub, SSI / SSDI award letter, SNAP/EBT card, or unemployment statement. Self-declaration of zero income is accepted at many community action agencies.",
                isEssential = false
            ),
            ChecklistItem(
                title = "Veterans & Military Families: DD-214 or Military ID",
                description = "Discharge paperwork (DD-214), VA Healthcare ID, or dependent military ID for VA housing vouchers (HUD-VASH), VFW Unmet Needs cash grants, or American Legion family relief.",
                isEssential = false
            )
        )
    }

    /**
     * Generates clean formatted plain text for offline sharing via SMS, WhatsApp, or email.
     */
    fun generatePlainTextGuide(zipCode: String, charities: List<Charity>): String {
        return buildString {
            appendLine("════════════════════════════════════════════════════════")
            appendLine("  THE LOVE LIFE FOR HUMANITY PROJECT")
            appendLine("  Created by Tiffany K Casteel")
            appendLine("  One-Page Emergency Community Assistance Guide")
            appendLine("  Serving ZIP Code: $zipCode")
            appendLine("════════════════════════════════════════════════════════")
            appendLine()
            appendLine("🚨 24/7 CRITICAL EMERGENCY HELPLINES:")
            appendLine(" • Dial 2-1-1 : United Way Community Resources & Shelter Referral")
            appendLine(" • Dial 988   : Suicide & Mental Health Crisis Lifeline")
            appendLine(" • Dial 988 (Press 1) : Veterans Crisis Line (Text 838255)")
            appendLine(" • 1-877-4AID-VET (1-877-424-3838) : Homeless Veterans Emergency Line")
            appendLine(" • 1-866-3-HUNGRY (1-866-348-6479) : USDA National Food Pantries")
            appendLine(" • 1-866-674-6327 : LIHEAP Emergency Power/Heating Utility Grants")
            appendLine(" • 1-800-799-7233 : National Domestic Violence 24/7 Safety Hotline")
            appendLine()
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📍 TOP VERIFIED LOCAL CHARITIES & ASSISTANCE:")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

            val topCharities = charities.take(8)
            if (topCharities.isEmpty()) {
                appendLine("Call 2-1-1 directly to connect with neighborhood programs for ZIP $zipCode.")
            } else {
                topCharities.forEachIndexed { index, charity ->
                    appendLine("${index + 1}. ${charity.name.uppercase()}")
                    appendLine("   Tagline: ${charity.tagline}")
                    appendLine("   Phone: ${charity.phone}" + if (!charity.tollFreePhone.isNullOrBlank()) " | Toll-Free: ${charity.tollFreePhone}" else "")
                    appendLine("   Address: ${charity.address}, ${charity.city}, ${charity.state}")
                    appendLine("   Hours: ${charity.hours}")
                    appendLine("   Services: " + charity.services.joinToString("; "))
                    appendLine("   Intake / Walk-In: ${charity.intakeProcess} | Free: ${if (charity.isFreeService) "100% Free" else "Sliding scale"}")
                    appendLine()
                }
            }

            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("📋 INTAKE & DOCUMENTATION CHECKLIST:")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            getUniversalIntakeChecklist().forEach { item ->
                appendLine(" [ ] ${item.title}")
                appendLine("     ${item.description}")
            }
            appendLine()
            appendLine("Online Resource Directory: ${IntentUtils.DEFAULT_APP_URL}")
            appendLine("Generated with love by Tiffany K Casteel.")
        }
    }

    /**
     * Builds standard high-contrast, print-formatted HTML document for printing via Android PrintManager.
     */
    fun generateHtmlPrintDocument(zipCode: String, charities: List<Charity>): String {
        val topCharities = charities.take(8)

        val charityRows = topCharities.joinToString("\n") { charity ->
            """
            <div class="charity-card">
                <div class="charity-header">
                    <span class="charity-name">${charity.name}</span>
                    <span class="charity-badge">${if (charity.isWalkInFriendly) "Walk-in Welcome" else "Appointment Recommended"} • 100% Free</span>
                </div>
                <div class="charity-sub">${charity.tagline}</div>
                <div class="charity-contact">
                    <strong>Phone:</strong> ${charity.phone} &nbsp;|&nbsp; 
                    <strong>Hours:</strong> ${charity.hours}
                </div>
                <div class="charity-address">
                    <strong>Location:</strong> ${charity.address}, ${charity.city}, ${charity.state}
                </div>
                <div class="charity-services">
                    <strong>Services:</strong> ${charity.services.joinToString(", ")}
                </div>
            </div>
            """.trimIndent()
        }

        val checklistItemsHtml = getUniversalIntakeChecklist().joinToString("\n") { item ->
            """
            <div class="check-item">
                <span class="checkbox">&#9633;</span>
                <span class="check-text"><strong>${item.title}:</strong> ${item.description}</span>
            </div>
            """.trimIndent()
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>The Love Life for Humanity Project - Guide for $zipCode</title>
            <style>
                @page {
                    size: letter portrait;
                    margin: 12mm 12mm 12mm 12mm;
                }
                body {
                    font-family: Arial, Helvetica, sans-serif;
                    color: #111;
                    line-height: 1.25;
                    font-size: 11pt;
                    margin: 0;
                    padding: 0;
                }
                .header {
                    border-bottom: 2px solid #1E3A8A;
                    padding-bottom: 6px;
                    margin-bottom: 8px;
                }
                .title {
                    font-size: 16pt;
                    font-weight: bold;
                    color: #1E3A8A;
                    margin: 0;
                }
                .subtitle {
                    font-size: 10pt;
                    color: #444;
                    margin-top: 2px;
                }
                .attribution {
                    font-size: 9pt;
                    font-weight: bold;
                    color: #B45309;
                    margin-top: 2px;
                }
                .emergency-box {
                    background-color: #F1F5F9;
                    border: 1.5px solid #CBD5E1;
                    border-radius: 4px;
                    padding: 6px 10px;
                    margin-bottom: 8px;
                    font-size: 9pt;
                }
                .emergency-title {
                    font-weight: bold;
                    color: #DC2626;
                    text-transform: uppercase;
                    margin-bottom: 3px;
                }
                .emergency-grid {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 6px 14px;
                }
                .section-heading {
                    font-size: 11pt;
                    font-weight: bold;
                    color: #1E3A8A;
                    background: #E2E8F0;
                    padding: 3px 6px;
                    margin: 6px 0 4px 0;
                    text-transform: uppercase;
                }
                .charity-card {
                    border-bottom: 1px dashed #94A3B8;
                    padding: 4px 0;
                    margin-bottom: 4px;
                    page-break-inside: avoid;
                }
                .charity-header {
                    display: flex;
                    justify-content: space-between;
                    align-items: baseline;
                }
                .charity-name {
                    font-size: 10.5pt;
                    font-weight: bold;
                }
                .charity-badge {
                    font-size: 8pt;
                    background: #DBEAFE;
                    padding: 1px 5px;
                    border-radius: 3px;
                    font-weight: bold;
                }
                .charity-sub {
                    font-size: 8.5pt;
                    color: #334155;
                    font-style: italic;
                }
                .charity-contact, .charity-address, .charity-services {
                    font-size: 8.5pt;
                    margin-top: 2px;
                }
                .checklist-container {
                    margin-top: 6px;
                    page-break-inside: avoid;
                }
                .check-item {
                    display: flex;
                    align-items: flex-start;
                    margin-bottom: 3px;
                    font-size: 8.5pt;
                }
                .checkbox {
                    font-size: 11pt;
                    margin-right: 6px;
                    line-height: 1;
                }
                .footer {
                    margin-top: 8px;
                    border-top: 1px solid #CBD5E1;
                    padding-top: 4px;
                    text-align: center;
                    font-size: 8pt;
                    color: #64748B;
                }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="title">The Love Life for Humanity Project</div>
                <div class="attribution">Created by Tiffany K Casteel &bull; One-Page Emergency Assistance Guide</div>
                <div class="subtitle">Direct contacts, hours & food/shelter/utility resources for ZIP Code: <strong>$zipCode</strong></div>
            </div>

            <div class="emergency-box">
                <div class="emergency-title">24/7 Immediate Emergency Helplines</div>
                <div class="emergency-grid">
                    <div><strong>Dial 2-1-1</strong>: United Way Food & Shelter Hotline</div>
                    <div><strong>Dial 988 (Press 1)</strong>: Veterans Crisis Line</div>
                    <div><strong>1-877-4AID-VET</strong>: Homeless Veterans Assistance</div>
                    <div><strong>1-866-3-HUNGRY</strong>: National Food Pantries</div>
                    <div><strong>1-866-674-6327</strong>: LIHEAP Utility Bill Aid</div>
                    <div><strong>1-800-799-7233</strong>: Domestic Violence Safety</div>
                </div>
            </div>

            <div class="section-heading">Verified Local Community Organizations (ZIP $zipCode)</div>
            $charityRows

            <div class="checklist-container">
                <div class="section-heading">Universal Intake Preparation Checklist</div>
                $checklistItemsHtml
            </div>

            <div class="footer">
                The Love Life for Humanity Project &bull; Created by Tiffany K Casteel &bull; Web: ${IntentUtils.DEFAULT_APP_URL} &bull; Keep this sheet handy.
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    /**
     * Sends the HTML document directly to the Android System Print Spooler.
     * Works with wireless printers, Google Print, or Save to PDF.
     */
    fun printDocument(context: Context, zipCode: String, charities: List<Charity>) {
        try {
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
            if (printManager == null) {
                Toast.makeText(context, "Print service unavailable on this device", Toast.LENGTH_SHORT).show()
                return
            }

            val webView = WebView(context)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    val jobName = "Love_Life_For_Humanity_Guide_$zipCode"
                    val printAdapter = webView.createPrintDocumentAdapter(jobName)
                    val printAttributes = PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build()

                    printManager.print(jobName, printAdapter, printAttributes)
                }
            }

            val htmlContent = generateHtmlPrintDocument(zipCode, charities)
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            Toast.makeText(context, "Printing error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
