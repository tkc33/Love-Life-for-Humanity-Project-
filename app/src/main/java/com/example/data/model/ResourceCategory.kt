package com.example.data.model

enum class ResourceCategory(
    val title: String,
    val description: String
) {
    ALL("All Needs", "View all community assistance programs"),
    VETERANS("Veterans & Military Families", "VA housing, family financial grants, PTSD support, caregiver relief"),
    FOOD("Food & Groceries", "Food pantries, hot meals, SNAP enrollment, emergency groceries"),
    HOUSING("Housing & Shelter", "Emergency shelter, rent assistance, transitional housing, eviction prevention"),
    UTILITIES("Utilities & Bills", "Electric, gas, water bill grants (LIHEAP) and utility relief"),
    HEALTHCARE("Health & Clinics", "Free & sliding-scale community clinics, dental, prescriptions"),
    CLOTHING("Clothing & Essentials", "Emergency clothes, warm coats, work attire, baby diapers & hygiene"),
    JOBS("Jobs & Training", "Job placement, vocational training, resume aid, GED programs"),
    LEGAL("Legal & Advocacy", "Free civil legal aid, tenant rights, disability & public benefits"),
    CRISIS("Crisis & Helpline", "24/7 immediate assistance, crisis intervention, emergency support")
}
