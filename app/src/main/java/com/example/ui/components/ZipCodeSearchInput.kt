package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * A dedicated Compose UI component with a text field for users to enter their
 * 5-digit ZIP code and a button to trigger the instant offline resource lookup.
 */
@Composable
fun ZipCodeSearchInput(
    zipCode: String,
    onZipCodeChange: (String) -> Unit,
    onTriggerLookup: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Enter 5-digit ZIP",
    buttonText: String = "Find",
    showOfflineBadge: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    textFieldTestTag: String = "zip_code_text_field",
    buttonTestTag: String = "trigger_lookup_button"
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("zip_code_search_input_component")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = zipCode,
                onValueChange = { input ->
                    // Accept digits only, limit to 5 characters for US ZIP codes
                    val filtered = input.filter { it.isDigit() }.take(5)
                    onZipCodeChange(filtered)
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag(textFieldTestTag),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                isError = isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onTriggerLookup()
                    }
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    if (zipCode.isNotEmpty()) {
                        IconButton(
                            onClick = { onZipCodeChange("") },
                            modifier = Modifier.testTag("clear_zip_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear ZIP Code input",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onTriggerLookup()
                },
                modifier = Modifier
                    .height(56.dp)
                    .testTag(buttonTestTag),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("zip_input_error_text")
            )
        } else if (showOfflineBadge) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.testTag("offline_lookup_indicator")
            ) {
                Icon(
                    imageVector = Icons.Default.OfflineBolt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "100% Offline Lookup • No internet connection required",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Stateful overload of [ZipCodeSearchInput] managing its own internal state,
 * triggering [onTriggerLookup] with the entered ZIP code.
 */
@Composable
fun StatefulZipCodeSearchInput(
    initialZip: String = "",
    onTriggerLookup: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Enter 5-digit ZIP",
    buttonText: String = "Lookup"
) {
    var text by remember { mutableStateOf(initialZip) }

    ZipCodeSearchInput(
        zipCode = text,
        onZipCodeChange = { text = it },
        onTriggerLookup = { onTriggerLookup(text) },
        modifier = modifier,
        placeholder = placeholder,
        buttonText = buttonText
    )
}
