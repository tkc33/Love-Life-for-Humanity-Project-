package com.example.data.model

data class Charity(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val targetZipCodes: List<String>,
    val city: String,
    val state: String,
    val address: String,
    val phone: String,
    val tollFreePhone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val hours: String,
    val categories: List<ResourceCategory>,
    val services: List<String>,
    val eligibility: String,
    val intakeProcess: String,
    val isWalkInFriendly: Boolean = true,
    val isFreeService: Boolean = true,
    val languagesSupported: List<String> = listOf("English", "Spanish"),
    val isNationwideOrRegional: Boolean = false
)
