package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.util.IntentUtils

@Composable
fun AboutView(
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("about_view")
    ) {
        // Hero Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "The Love Life for Humanity Project",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Connecting Low-Income Individuals with Dignity & Real Resources",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = "Created by Tiffany K Casteel",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Our Mission
        Text(
            text = "Our Mission",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "The Love Life for Humanity Project was created by Tiffany K Casteel on a single compassionate principle: nobody in our communities should struggle in silence without knowing where to turn. By simply typing your 5-digit ZIP code, you receive direct, immediate access to verified charities, food pantries, emergency rent & utility bill aid, and healthcare clinics — with direct phone numbers and one-tap directions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Key Pillars
        Text(
            text = "What You Can Expect",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        PillarItem(
            icon = Icons.Default.Phone,
            title = "Direct Contact Information",
            description = "No runarounds or hidden walls. Direct telephone numbers, intake desk contacts, and street addresses so you can reach live coordinators immediately."
        )

        PillarItem(
            icon = Icons.Default.Handshake,
            title = "Zero Judgment & Free Services",
            description = "Community organizations listed here are non-profit charities whose express goal is helping families, unhoused neighbors, and low-income workers get back on their feet."
        )

        PillarItem(
            icon = Icons.Default.Security,
            title = "Your Rights & Privacy",
            description = "This application collects no personal identifiers or account logins. All you enter is your ZIP code. Most neighborhood food pantries require zero documentation to receive an emergency box of groceries."
        )

        PillarItem(
            icon = Icons.Default.Lightbulb,
            title = "Immediate 2-1-1 Safety Net",
            description = "If you cannot find a specific program for your exact situation, dialing 2-1-1 connects you free of charge 24/7 to professional community resource operators in your city."
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Social Media Sharing Section
        Text(
            text = "Share on Social Media",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Spread hope and help families in need discover food pantries, emergency shelters, and utility grants in their area.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("about_social_share_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Social buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { IntentUtils.shareToWhatsApp(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorWhatsApp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_whatsapp")
                    ) {
                        Text("WhatsApp", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = { IntentUtils.shareToFacebook(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorFacebook),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_facebook")
                    ) {
                        Text("Facebook", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = { IntentUtils.shareToX(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorXTwitter),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_x")
                    ) {
                        Text("X", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { IntentUtils.shareToLinkedIn(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorLinkedIn),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_linkedin")
                    ) {
                        Text("LinkedIn", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = { IntentUtils.shareToReddit(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorReddit),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_reddit")
                    ) {
                        Text("Reddit", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = { IntentUtils.shareViaSms(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorSms),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("about_share_sms")
                    ) {
                        Text("SMS", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Copy Link Row
                androidx.compose.material3.OutlinedButton(
                    onClick = {
                        IntentUtils.copyToClipboard(context, "App Link", IntentUtils.DEFAULT_APP_URL)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("about_copy_link_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy App Link (${IntentUtils.DEFAULT_APP_URL})", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                IntentUtils.dialPhoneNumber(context, "211")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("about_dial_211_button"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(imageVector = Icons.Default.Phone, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Dial 2-1-1 Emergency Helpline Now")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateToSearch,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("about_search_button"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Start ZIP Code Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Created with love & purpose by Tiffany K Casteel\nThe Love Life for Humanity Project",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("creator_footer_attribution")
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PillarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
