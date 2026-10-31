package com.example.ui.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.Charity

object IntentUtils {

    const val DEFAULT_APP_URL = "https://ais-pre-k77wqqtlt7d54ffxmypwrr-330075208600.us-east1.run.app"
    const val DEFAULT_SHARE_MESSAGE = "The Love Life for Humanity Project (Created by Tiffany K Casteel): Find free food pantries, emergency shelter, rent/utility relief, and free healthcare clinics by simply entering your 5-digit ZIP code. Direct phone numbers, hours, and directions included."

    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        try {
            val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openDirections(context: Context, query: String, address: String) {
        try {
            val encodedQuery = Uri.encode("$query, $address")
            val uri = Uri.parse("geo:0,0?q=$encodedQuery")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            try {
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode("$query, $address")}")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(webIntent)
            } catch (err: Exception) {
                Toast.makeText(context, "Could not open map: ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openWebsite(context: Context, url: String) {
        try {
            val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else url
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open website: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendEmail(context: Context, emailAddress: String, subject: String) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open email client: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareCharityDetails(context: Context, charity: Charity) {
        try {
            val shareText = buildString {
                appendLine("🤝 The Love Life for Humanity Project (Created by Tiffany K Casteel) - Resource Details:")
                appendLine("━━━━━━━━━━━━━━━━━━━━━")
                appendLine(charity.name)
                appendLine(charity.tagline)
                appendLine()
                appendLine("📍 Address: ${charity.address}")
                appendLine("📞 Phone: ${charity.phone}")
                if (!charity.tollFreePhone.isNullOrBlank()) {
                    appendLine("☎️ Toll-Free: ${charity.tollFreePhone}")
                }
                if (!charity.website.isNullOrBlank()) {
                    appendLine("🌐 Website: ${charity.website}")
                }
                appendLine("⏰ Hours: ${charity.hours}")
                appendLine()
                appendLine("Services Provided:")
                charity.services.forEach { service ->
                    appendLine(" • $service")
                }
                appendLine()
                appendLine("Eligibility: ${charity.eligibility}")
                appendLine("Intake: ${charity.intakeProcess}")
                appendLine()
                appendLine("Find more community resources at: $DEFAULT_APP_URL")
            }

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Community Resource Info").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not share info: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ================= SOCIAL MEDIA SHARING =================

    fun shareAppGeneral(context: Context, message: String = DEFAULT_SHARE_MESSAGE, url: String = DEFAULT_APP_URL) {
        try {
            val fullText = "$message\n\n$url"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, fullText)
                putExtra(Intent.EXTRA_SUBJECT, "The Love Life for Humanity Project")
                type = "text/plain"
            }
            val chooser = Intent.createChooser(sendIntent, "Share The Love Life for Humanity Project").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to share: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareToWhatsApp(context: Context, message: String = DEFAULT_SHARE_MESSAGE, url: String = DEFAULT_APP_URL) {
        try {
            val fullText = "$message\n$url"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(fullText)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, message, url)
        }
    }

    fun shareToFacebook(context: Context, url: String = DEFAULT_APP_URL) {
        try {
            val shareUrl = "https://www.facebook.com/sharer/sharer.php?u=${Uri.encode(url)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, url = url)
        }
    }

    fun shareToX(context: Context, message: String = DEFAULT_SHARE_MESSAGE, url: String = DEFAULT_APP_URL) {
        try {
            val tweetUrl = "https://x.com/intent/tweet?text=${Uri.encode(message)}&url=${Uri.encode(url)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, message, url)
        }
    }

    fun shareToLinkedIn(context: Context, url: String = DEFAULT_APP_URL) {
        try {
            val shareUrl = "https://www.linkedin.com/sharing/share-offsite/?url=${Uri.encode(url)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, url = url)
        }
    }

    fun shareToReddit(context: Context, title: String = "The Love Life for Humanity Project: Free Food, Shelter & Utilities by ZIP Code", url: String = DEFAULT_APP_URL) {
        try {
            val shareUrl = "https://www.reddit.com/submit?url=${Uri.encode(url)}&title=${Uri.encode(title)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, title, url)
        }
    }

    fun shareViaSms(context: Context, message: String = DEFAULT_SHARE_MESSAGE, url: String = DEFAULT_APP_URL) {
        try {
            val fullText = "$message $url"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:?body=${Uri.encode(fullText)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareAppGeneral(context, message, url)
        }
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Could not copy link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
