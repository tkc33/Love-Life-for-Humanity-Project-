package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.util.IntentUtils

data class HotlineInfo(
    val title: String,
    val number: String,
    val displayPhone: String,
    val description: String,
    val hours: String,
    val icon: ImageVector,
    val isUrgent: Boolean = false
)

@Composable
fun HotlinesView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val hotlines = listOf(
        HotlineInfo(
            title = "2-1-1 United Way Essential Community Services",
            number = "211",
            displayPhone = "Dial 211",
            description = "Free, confidential 24/7 referral to local food pantries, emergency rent relief, shelter beds, and utility cutoff aid.",
            hours = "24 Hours / 7 Days",
            icon = Icons.Default.Emergency,
            isUrgent = true
        ),
        HotlineInfo(
            title = "988 Suicide & Crisis Lifeline",
            number = "988",
            displayPhone = "Dial 988",
            description = "Immediate free emotional support, crisis counseling, and suicide prevention by phone or text.",
            hours = "24 Hours / 7 Days",
            icon = Icons.Default.HealthAndSafety,
            isUrgent = true
        ),
        HotlineInfo(
            title = "Veterans Crisis Line",
            number = "988",
            displayPhone = "Dial 988 (Press 1)",
            description = "Confidential 24/7 crisis support for Veterans, Service members, National Guard, Reserve, and their families (or text 838255).",
            hours = "24 Hours / 7 Days",
            icon = Icons.Default.MilitaryTech,
            isUrgent = true
        ),
        HotlineInfo(
            title = "National Call Center for Homeless Veterans",
            number = "18774243838",
            displayPhone = "1-877-4AID-VET (1-877-424-3838)",
            description = "Free 24/7 hotline connecting homeless and at-risk veterans to VA emergency housing, food, HUD-VASH vouchers, and healthcare.",
            hours = "24 Hours / 7 Days",
            icon = Icons.Default.MilitaryTech,
            isUrgent = true
        ),
        HotlineInfo(
            title = "VFW Unmet Needs Military Family Grants",
            number = "18667896333",
            displayPhone = "1-866-789-6333",
            description = "Emergency financial grants up to $1,500 for military families and veterans facing rent eviction, unpaid medical, or utility cutoff.",
            hours = "Mon–Fri: 8:00 AM – 4:00 PM CST",
            icon = Icons.Default.MilitaryTech
        ),
        HotlineInfo(
            title = "USDA National Hunger Hotline",
            number = "18663486479",
            displayPhone = "1-866-3-HUNGRY (1-866-348-6479)",
            description = "Direct assistance finding emergency food pantries, soup kitchens, summer meals for children, and SNAP enrollment.",
            hours = "Mon–Fri: 7:00 AM – 10:00 PM EST",
            icon = Icons.Default.Restaurant
        ),
        HotlineInfo(
            title = "LIHEAP Emergency Home Energy Aid",
            number = "18666746327",
            displayPhone = "1-866-674-6327",
            description = "Federal energy assistance clearinghouse helping low-income households prevent gas & electric shut-offs.",
            hours = "Mon–Fri: 9:00 AM – 5:00 PM EST",
            icon = Icons.Default.ElectricBolt
        ),
        HotlineInfo(
            title = "National Domestic Violence Support Hotline",
            number = "18007997233",
            displayPhone = "1-800-799-SAFE (1-800-799-7233)",
            description = "24/7 confidential safety planning, emergency shelter placement, and legal protection assistance.",
            hours = "24 Hours / 7 Days",
            icon = Icons.Default.Shield,
            isUrgent = true
        ),
        HotlineInfo(
            title = "Legal Services Corporation (LSC) Civil Aid",
            number = "12022951500",
            displayPhone = "1-202-295-1500",
            description = "Connecting low-income individuals to free legal assistance for illegal evictions, wage theft, and benefits denial.",
            hours = "Mon–Fri: 9:00 AM – 5:30 PM EST",
            icon = Icons.Default.Gavel
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("hotlines_view"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Emergency & Free National Helplines",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Direct, toll-free lines for immediate crisis help, hunger relief, energy grants, and emergency housing.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(hotlines) { hotline ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (hotline.isUrgent) {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hotline_card_${hotline.number}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = hotline.icon,
                            contentDescription = null,
                            tint = if (hotline.isUrgent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = hotline.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = hotline.hours,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = hotline.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            IntentUtils.dialPhoneNumber(context, hotline.number)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hotline_call_button_${hotline.number}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hotline.isUrgent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Call ${hotline.displayPhone}",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
