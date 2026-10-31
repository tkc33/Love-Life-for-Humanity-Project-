package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ResourceCategory

@Composable
fun CategoryFilterChips(
    selectedCategory: ResourceCategory,
    onCategorySelected: (ResourceCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResourceCategory.values().forEach { category ->
                val isSelected = category == selectedCategory
                val icon = getCategoryIcon(category)

                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.Check else icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("filter_chip_${category.name.lowercase()}")
                )
            }
        }
    }
}

fun getCategoryIcon(category: ResourceCategory): ImageVector {
    return when (category) {
        ResourceCategory.ALL -> Icons.Default.AllInclusive
        ResourceCategory.VETERANS -> Icons.Default.MilitaryTech
        ResourceCategory.FOOD -> Icons.Default.Restaurant
        ResourceCategory.HOUSING -> Icons.Default.Home
        ResourceCategory.UTILITIES -> Icons.Default.AttachMoney
        ResourceCategory.HEALTHCARE -> Icons.Default.LocalHospital
        ResourceCategory.CLOTHING -> Icons.Outlined.ShoppingBag
        ResourceCategory.JOBS -> Icons.Default.Work
        ResourceCategory.LEGAL -> Icons.Default.Gavel
        ResourceCategory.CRISIS -> Icons.Default.CrisisAlert
    }
}
