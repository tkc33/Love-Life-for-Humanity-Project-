package com.example.data.repository

import com.example.data.model.Charity
import com.example.data.model.ResourceCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharityRepository {

    // In-memory bookmark tracking
    private val _bookmarkedCharityIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedCharityIds: StateFlow<Set<String>> = _bookmarkedCharityIds.asStateFlow()

    fun toggleBookmark(charityId: String) {
        val current = _bookmarkedCharityIds.value
        _bookmarkedCharityIds.value = if (current.contains(charityId)) {
            current - charityId
        } else {
            current + charityId
        }
    }

    fun isBookmarked(charityId: String): Boolean {
        return _bookmarkedCharityIds.value.contains(charityId)
    }

    /**
     * Resolves charities for a 5-digit ZIP code.
     * Combines:
     * 1. Exact zip / city level charities.
     * 2. Regional / state safety net charities mapped by zip prefix.
     * 3. Universal safety net programs (2-1-1, LIHEAP, Feeding America, 988).
     */
    fun findCharitiesByZip(zipCode: String): List<Charity> {
        val cleanZip = zipCode.trim()
        if (cleanZip.length < 3) return emptyList()

        val exactMatches = allCharities.filter { charity ->
            charity.targetZipCodes.any { target ->
                target.equals(cleanZip, ignoreCase = true) ||
                (target.length == 3 && cleanZip.startsWith(target)) ||
                (target.length == 2 && cleanZip.startsWith(target))
            }
        }

        val stateMatches = resolveStateLevelCharities(cleanZip)
        val nationalMatches = allCharities.filter { it.isNationwideOrRegional }

        // Combine and distinct by ID
        val combined = (exactMatches + stateMatches + nationalMatches).distinctBy { it.id }
        return combined
    }

    fun getCharityById(id: String): Charity? {
        return allCharities.find { it.id == id }
    }

    fun getBookmarkedCharities(): List<Charity> {
        val saved = _bookmarkedCharityIds.value
        return allCharities.filter { saved.contains(it.id) }
    }

    /**
     * Maps 2-digit zip prefix to State abbreviation and regional assistance.
     */
    private fun resolveStateLevelCharities(zip: String): List<Charity> {
        if (zip.length < 2) return emptyList()
        val prefix2 = zip.substring(0, 2)
        val stateAbbr = getStateForZipPrefix(prefix2) ?: return emptyList()

        return allCharities.filter { it.state.equals(stateAbbr, ignoreCase = true) }
    }

    companion object {
        fun getStateForZipPrefix(prefix2: String): String? {
            val num = prefix2.toIntOrNull() ?: return null
            return when (num) {
                in 10..14 -> "NY"
                in 15..19 -> "PA"
                in 20..20 -> "DC"
                in 21..21 -> "MD"
                in 22..24 -> "VA"
                in 25..26 -> "WV"
                in 27..28 -> "NC"
                in 29..29 -> "SC"
                in 30..31 -> "GA"
                in 32..34 -> "FL"
                in 35..36 -> "AL"
                in 37..38 -> "TN"
                in 39..39 -> "MS"
                in 40..42 -> "KY"
                in 43..45 -> "OH"
                in 46..47 -> "IN"
                in 48..49 -> "MI"
                in 50..52 -> "IA"
                in 53..54 -> "WI"
                in 55..56 -> "MN"
                in 57..57 -> "SD"
                in 58..58 -> "ND"
                in 59..59 -> "MT"
                in 60..62 -> "IL"
                in 63..65 -> "MO"
                in 66..67 -> "KS"
                in 68..69 -> "NE"
                in 70..71 -> "LA"
                in 72..72 -> "AR"
                in 73..74 -> "OK"
                in 75..79 -> "TX"
                in 80..81 -> "CO"
                in 82..82 -> "WY"
                in 83..83 -> "ID"
                in 84..84 -> "UT"
                in 85..86 -> "AZ"
                in 87..88 -> "NM"
                in 89..89 -> "NV"
                in 90..96 -> "CA"
                in 97..97 -> "OR"
                in 98..99 -> "WA"
                in 1..2 -> "MA"
                3 -> "NH"
                4 -> "ME"
                5 -> "VT"
                6 -> "CT"
                in 7..8 -> "NJ"
                else -> null
            }
        }
    }

    private val allCharities: List<Charity> = listOf(
        // ================= NATIONAL & UNIVERSAL SAFETY NETS =================
        Charity(
            id = "nat-211",
            name = "United Way 2-1-1 Emergency Referral",
            tagline = "24/7 Free Local Assistance & Human Services Helpline",
            description = "Direct telephone helpline connecting low-income individuals to local food pantries, emergency rent & utility bill relief, emergency shelter, and health resources in their exact neighborhood.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Service",
            state = "US",
            address = "Dial 211 from any mobile phone or landline",
            phone = "211",
            tollFreePhone = "1-800-543-7709",
            email = "help@211.org",
            website = "https://www.211.org",
            hours = "Open 24 Hours / 7 Days a week",
            categories = listOf(
                ResourceCategory.CRISIS,
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING,
                ResourceCategory.UTILITIES
            ),
            services = listOf(
                "Immediate live operator matching for food and emergency shelter",
                "Direct contact numbers for active rent relief funds",
                "Utility cut-off prevention programs (electric, water, heating)",
                "Free non-emergency medical transportation and clinic referrals"
            ),
            eligibility = "Free for all low-income individuals and families, unhoused persons, and seniors.",
            intakeProcess = "Call 211 directly or send a text with your ZIP code to 898211.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "180+ languages via live translation"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-feeding-america",
            name = "Feeding America Food Bank Network",
            tagline = "Nation's Largest Domestic Hunger-Relief Charity",
            description = "Network of 200 food banks and 60,000 food pantries providing free fresh groceries, produce boxes, canned food, hot meals, and SNAP food stamp application assistance.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Community Network",
            state = "US",
            address = "Local affiliate network across all 50 states",
            phone = "1-800-771-2303",
            tollFreePhone = "1-866-348-6479",
            email = "info@feedingamerica.org",
            website = "https://www.feedingamerica.org",
            hours = "Monday – Friday: 8:00 AM – 6:00 PM",
            categories = listOf(ResourceCategory.FOOD),
            services = listOf(
                "Free weekly food pantry boxes and fresh produce bags",
                "Mobile food pantry drop-offs in low-income food deserts",
                "Emergency baby formula, diapers, and senior grocery boxes",
                "In-person and phone help applying for SNAP/EBT benefits"
            ),
            eligibility = "Open to any household facing food insecurity. No proof of income required at most pantry partners.",
            intakeProcess = "Call toll-free or visit online to locate the nearest neighborhood distribution site.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-liheap",
            name = "National Energy Assistance (LIHEAP Clearinghouse)",
            tagline = "Emergency Electric, Heating & Gas Bill Aid",
            description = "Federally funded community assistance program helping low-income households with utility reconnection, overdue electric and gas bills, furnace repairs, and weatherization.",
            targetZipCodes = listOf("ALL"),
            city = "Federal / State Community Action",
            state = "US",
            address = "Administered by local Community Action Agencies",
            phone = "1-866-674-6327",
            tollFreePhone = "1-866-674-6327",
            email = "energy@ncat.org",
            website = "https://liheapch.acf.hhs.gov",
            hours = "Monday – Friday: 9:00 AM – 5:00 PM EST",
            categories = listOf(ResourceCategory.UTILITIES),
            services = listOf(
                "Direct cash grants paid to utility companies for past-due balances",
                "Emergency crisis assistance for shut-off notice cancellation",
                "Free home energy weatherization to lower monthly power bills",
                "Summer cooling assistance (air conditioner units or power credits)"
            ),
            eligibility = "Income at or below 150% of the Federal Poverty Level or 60% of State Median Income.",
            intakeProcess = "Call 1-866-674-6327 to connect directly with your county's LIHEAP intake coordinator.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-svdp",
            name = "Society of St. Vincent de Paul",
            tagline = "Direct Emergency Financial Aid & Home Visits",
            description = "Parish-based charitable network providing immediate direct emergency funding for rent, past-due utility bills, prescription medication, thrift vouchers, and food pantries.",
            targetZipCodes = listOf("ALL"),
            city = "National Community Councils",
            state = "US",
            address = "Local conferences operating in almost every US diocese",
            phone = "1-314-576-3993",
            tollFreePhone = "1-888-375-7837",
            email = "svdphelp@svdpusa.org",
            website = "https://www.svdpusa.org",
            hours = "Monday – Friday: 9:00 AM – 4:00 PM",
            categories = listOf(
                ResourceCategory.UTILITIES,
                ResourceCategory.HOUSING,
                ResourceCategory.CLOTHING,
                ResourceCategory.FOOD
            ),
            services = listOf(
                "Emergency rent payments to avoid eviction",
                "Electric and water shut-off prevention payments",
                "Vouchers for free clothing, blankets, and furniture",
                "Emergency grocery bags and baby essentials"
            ),
            eligibility = "Families or individuals in urgent financial hardship. People of all backgrounds and faiths welcome.",
            intakeProcess = "Call to request a conference visit or phone intake from your local parish team.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-salvation-army",
            name = "The Salvation Army Social Services",
            tagline = "Emergency Shelter, Food & Family Support Services",
            description = "Community centers offering emergency family shelters, hot meal kitchens, winter heating assistance, hygiene kits, and emergency transit assistance.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Community Hubs",
            state = "US",
            address = "Operating community centers across every state",
            phone = "1-800-725-2769",
            tollFreePhone = "1-800-SAL-ARMY",
            email = "socialservices@salvationarmyusa.org",
            website = "https://www.salvationarmyusa.org",
            hours = "Monday – Friday: 8:30 AM – 4:30 PM",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.CLOTHING,
                ResourceCategory.UTILITIES
            ),
            services = listOf(
                "Emergency transitional shelter and family housing",
                "Hot daily meal kitchens and bag lunch distributions",
                "Emergency heating fuel, electricity, and water bill assistance",
                "Back-to-school backpacks, winter coats, and toy drives"
            ),
            eligibility = "Anyone in need of emergency food, shelter, or utility bill assistance.",
            intakeProcess = "Walk in to your local corps community center or call 1-800-SAL-ARMY.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-988",
            name = "988 Suicide & Crisis Lifeline",
            tagline = "Free, Confidential Mental Health & Crisis Support",
            description = "National 24/7 lifeline providing free support for individuals experiencing overwhelming emotional distress, suicidal crisis, substance use challenges, or domestic trauma.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide 24/7 Lifeline",
            state = "US",
            address = "Direct phone and text service across the USA",
            phone = "988",
            tollFreePhone = "1-800-273-8255",
            email = "contact@988lifeline.org",
            website = "https://988lifeline.org",
            hours = "Available 24/7/365",
            categories = listOf(ResourceCategory.CRISIS, ResourceCategory.HEALTHCARE),
            services = listOf(
                "Free confidential crisis counseling by phone or text",
                "Warm handoffs to local mobile crisis response teams",
                "Veterans Crisis Line option (Press 1)",
                "Spanish crisis counselor option (Press 2)"
            ),
            eligibility = "Anyone in crisis or supporting someone in emotional or financial crisis.",
            intakeProcess = "Call or text 988 directly. Free and confidential.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "150+ languages via translation"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-veterans-crisis",
            name = "Veterans Crisis Line & VA Mental Health",
            tagline = "24/7 Confidential Crisis Support for Veterans, Service Members & Families",
            description = "Direct telephone, text, and chat helpline providing immediate crisis counseling, suicide prevention, and connections to local VA medical centers for veterans, service members, and their loved ones.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide 24/7 Service",
            state = "US",
            address = "Dial 988 and press 1, or text 838255",
            phone = "988",
            tollFreePhone = "1-800-273-8255",
            email = "support@veteranscrisisline.net",
            website = "https://www.veteranscrisisline.net",
            hours = "Open 24 Hours / 7 Days a week",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.CRISIS,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "Immediate 24/7 crisis counselors trained in military culture (Press 1)",
                "Confidential text chat support available by texting 838255",
                "Warm handoffs to local VA suicide prevention coordinators",
                "Support and guidance for military spouses, partners, and children"
            ),
            eligibility = "All veterans, active duty, National Guard, Reserve service members, and their families. VA enrollment not required.",
            intakeProcess = "Call 988 and press 1, or text 838255 directly. Free and confidential.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Tele-interpreters for 140+ languages"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-homeless-veterans",
            name = "National Call Center for Homeless Veterans (HUD-VASH)",
            tagline = "Emergency Shelter, Rapid Re-Housing & Food for Veterans",
            description = "VA-operated national helpline connecting homeless veterans and those at risk of homelessness to community shelter beds, HUD-VASH housing vouchers, and emergency food.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Community Network",
            state = "US",
            address = "Toll-free 24/7 helpline connecting to local VA medical centers",
            phone = "1-877-424-3838",
            tollFreePhone = "1-877-4AID-VET",
            email = "homelessvets@va.gov",
            website = "https://www.va.gov/homeless",
            hours = "Available 24/7/365",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD
            ),
            services = listOf(
                "Emergency shelter placement and transitional housing referrals",
                "HUD-VASH permanent supportive housing vouchers",
                "Supportive Services for Veteran Families (SSVF) rapid rent aid",
                "Free food pantry distribution, healthcare clinic access, and hygiene kits"
            ),
            eligibility = "Veterans who are homeless or at risk of homelessness, and their families.",
            intakeProcess = "Call 1-877-424-3838 (1-877-4AID-VET) to connect with a live intake coordinator.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-vfw-unmet-needs",
            name = "VFW Unmet Needs Military Family Relief",
            tagline = "Emergency Financial Grants up to $1,500 for Military & Veteran Families",
            description = "Provides direct emergency financial assistance grants of up to $1,500 for eligible military service members, veterans, and military families facing sudden financial hardship.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Community Program",
            state = "US",
            address = "Administered by Veterans of Foreign Wars National Headquarters",
            phone = "1-866-789-6333",
            tollFreePhone = "1-866-789-6333",
            email = "unmetneeds@vfw.org",
            website = "https://www.vfw.org/assistance/financial-grants",
            hours = "Monday – Friday: 8:00 AM – 4:30 PM CST",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.UTILITIES,
                ResourceCategory.HOUSING
            ),
            services = listOf(
                "Direct cash grants to pay past-due rent and mortgage payments",
                "Electric, gas, and water bill shut-off prevention payments",
                "Emergency vehicle repair grants to maintain employment",
                "Infant diapers, formula, and urgent medical expense coverage"
            ),
            eligibility = "Active duty, military families, and veterans discharged within 36 months facing financial crisis not caused by misconduct.",
            intakeProcess = "Call 1-866-789-6333 or submit an application online with billing statements.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-operation-homefront",
            name = "Operation Homefront Critical Family Assistance",
            tagline = "Relief, Rent/Utility Aid & Recurring Support for Military Families",
            description = "National non-profit serving military families and post-9/11 wounded veterans with critical financial assistance for housing, utilities, food, and transitional family villages.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Community Network",
            state = "US",
            address = "Local regional chapters serving military bases and communities",
            phone = "1-800-722-6098",
            tollFreePhone = "1-800-722-6098",
            email = "info@operationhomefront.org",
            website = "https://operationhomefront.org",
            hours = "Monday – Friday: 8:00 AM – 5:00 PM CST",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.UTILITIES,
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING
            ),
            services = listOf(
                "Critical financial assistance grants for past-due rent and utility bills",
                "Emergency grocery cards and holiday food distribution",
                "Transitional housing villages for wounded veterans and families",
                "Baby essentials and diapers for expecting military mothers"
            ),
            eligibility = "E-1 to E-6 active duty military, veterans with service-connected wounds, and surviving spouses.",
            intakeProcess = "Apply online at operationhomefront.org or call 1-800-722-6098.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-wounded-warrior",
            name = "Wounded Warrior Project (WWP)",
            tagline = "Comprehensive Health, Career & Family Care for Veterans",
            description = "Serves veterans and service members who incurred physical or mental injuries during military service, offering mental wellness retreats, career placement, and caregiver relief.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Regional Offices",
            state = "US",
            address = "Regional offices and community teams across all states",
            phone = "1-877-832-6997",
            tollFreePhone = "1-877-TEAM-WWP",
            email = "resourcecenter@woundedwarriorproject.org",
            website = "https://www.woundedwarriorproject.org",
            hours = "Monday – Friday: 9:00 AM – 9:00 PM EST",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.JOBS
            ),
            services = listOf(
                "Mental health counseling and PTSD recovery retreats (Project Odyssey)",
                "Warrior Care Network free intensive clinical care",
                "Career guidance, resume writing, and civilian job placement",
                "Benefits service officers helping file and appeal VA disability claims"
            ),
            eligibility = "Veterans and service members with military-connected physical or mental illness, and their family support members.",
            intakeProcess = "Call 1-877-TEAM-WWP (1-877-832-6997) or register online.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-dav",
            name = "Disabled American Veterans (DAV)",
            tagline = "Free VA Benefits Claims Help & Transportation for Veterans",
            description = "Veteran advocacy organization providing 100% free assistance securing VA disability pensions, healthcare benefits, and free medical transport vans for medical visits.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Chapter Network",
            state = "US",
            address = "Over 1,200 local chapters and national service offices nationwide",
            phone = "1-877-426-2838",
            tollFreePhone = "1-877-426-2838",
            email = "feedback@dav.org",
            website = "https://www.dav.org",
            hours = "Monday – Friday: 8:00 AM – 4:30 PM",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.LEGAL,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "100% free accredited National Service Officers to file VA disability claims",
                "DAV Transportation Network free van rides to and from VA hospitals",
                "Disaster relief grants for veterans affected by natural disasters",
                "Job fairs and employment placement specifically for disabled veterans"
            ),
            eligibility = "All veterans, transitioning service members, disabled veterans, and their dependents.",
            intakeProcess = "Call 1-877-426-2838 to locate your nearest DAV Service Officer.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-fisher-house",
            name = "Fisher House Foundation",
            tagline = "Free Comfort Homes for Families of Hospitalized Military & Veterans",
            description = "Builds and operates comfort homes located at major military and VA medical centers where military and veteran families stay free of charge while loved ones undergo treatment.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Hospital Homes",
            state = "US",
            address = "Comfort homes at major VA and military medical hospitals nationwide",
            phone = "1-888-294-8560",
            tollFreePhone = "1-888-294-8560",
            email = "info@fisherhouse.org",
            website = "https://fisherhouse.org",
            hours = "Monday – Friday: 8:30 AM – 5:00 PM EST",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.HOUSING
            ),
            services = listOf(
                "Free lodging for family members of hospitalized veterans and military",
                "Hero Miles program providing free airline tickets for medical travel",
                "Hotels for Heroes lodging grants when Fisher House is at capacity",
                "Family kitchen facilities, laundry, and community living spaces"
            ),
            eligibility = "Immediate family members of veterans or active duty personnel receiving inpatient medical care.",
            intakeProcess = "Referrals are made through the social work department at the treating VA or military hospital.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),
        Charity(
            id = "nat-american-legion-tfa",
            name = "The American Legion Temporary Financial Assistance (TFA)",
            tagline = "Emergency Cash Grants for Minor Children of Veterans",
            description = "Provides direct cash grants to eligible active duty service members and veterans with minor children facing sudden financial distress to ensure children remain housed, fed, and warm.",
            targetZipCodes = listOf("ALL"),
            city = "Nationwide Post Network",
            state = "US",
            address = "Administered through local American Legion Posts nationwide",
            phone = "1-800-433-3318",
            tollFreePhone = "1-800-433-3318",
            email = "tfa@legion.org",
            website = "https://www.legion.org/financialassistance",
            hours = "Monday – Friday: 8:00 AM – 4:30 PM EST",
            categories = listOf(
                ResourceCategory.VETERANS,
                ResourceCategory.FOOD,
                ResourceCategory.UTILITIES,
                ResourceCategory.HOUSING
            ),
            services = listOf(
                "Non-repayable cash grants to cover rent, utilities, food, and clothing",
                "Medical bills and prescription coverage for minor children",
                "Local post assistance and family emergency support",
                "Veteran benefits guidance and claim assistance"
            ),
            eligibility = "Eligible veterans or active duty members with minor child (under 18) in the home experiencing verified financial hardship.",
            intakeProcess = "Contact your local American Legion Post or call 1-800-433-3318 for the department adjutant.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish"),
            isNationwideOrRegional = true
        ),

        // ================= CALIFORNIA (90001 - 96162) =================
        Charity(
            id = "ca-la-midnight-mission",
            name = "The Midnight Mission",
            tagline = "Food, Shelter & Essential Human Services in Downtown LA",
            description = "Historic human services provider in Skid Row offering three hot meals a day, emergency shelter, barber services, clothing distribution, and medical clinic access with zero barriers.",
            targetZipCodes = listOf("90001", "90011", "90013", "90014", "90015", "90021", "90028", "90044", "90007", "900", "901", "902"),
            city = "Los Angeles",
            state = "CA",
            address = "601 S San Pedro St, Los Angeles, CA 90014",
            phone = "(213) 624-9258",
            tollFreePhone = "(213) 624-9258",
            email = "help@midnightmission.org",
            website = "https://www.midnightmission.org",
            hours = "Meals: 6:30am, 12:30pm, 4:30pm daily | Walk-in Services 24/7",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING,
                ResourceCategory.CLOTHING,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "3 free hot meals served 365 days a year (no ID required)",
                "Emergency safe sleep beds and personal lockers",
                "Clean clothes, showers, hygiene kits, and haircut stations",
                "Onsite free primary health and dental screening"
            ),
            eligibility = "Anyone experiencing homelessness or low-income hardship. Zero paperwork required for meals.",
            intakeProcess = "Walk-in to the courtyard on San Pedro St for meals and clothing; see intake desk for bed sign-ups.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "ca-la-st-francis-center",
            name = "St. Francis Center Los Angeles",
            tagline = "Pantry Program & Homeless Services",
            description = "Community organization offering morning breakfast, emergency family pantry boxes, utility bill advocacy, and free mail services for unhoused neighbors.",
            targetZipCodes = listOf("90001", "90007", "90015", "90017", "90026", "90037", "90057", "900"),
            city = "Los Angeles",
            state = "CA",
            address = "1835 S Hope St, Los Angeles, CA 90015",
            phone = "(213) 747-5347",
            email = "info@stfranciscenterla.org",
            website = "https://www.stfranciscenterla.org",
            hours = "Tuesday – Saturday: 7:30 AM – 1:00 PM",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.UTILITIES,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Weekly family grocery boxes (meats, fresh produce, bread)",
                "Hot sit-down morning breakfast service",
                "Emergency utility grant referral and assistance",
                "Clean showers, clothing room, and mail holding"
            ),
            eligibility = "Low-income individuals and families residing in Central/South Los Angeles.",
            intakeProcess = "Bring any photo ID or mail showing residency for pantry signup. Walk-in breakfast needs no ID.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "ca-la-legal-aid",
            name = "Legal Aid Foundation of Los Angeles (LAFLA)",
            tagline = "Free Legal Assistance for Low-Income Angelenos",
            description = "Premier civil legal aid organization fighting evictions, securing government benefits, protecting workers from wage theft, and supporting domestic violence survivors.",
            targetZipCodes = listOf("90001", "90011", "90022", "90033", "90220", "900", "902", "906"),
            city = "Los Angeles",
            state = "CA",
            address = "1550 W 8th St, Los Angeles, CA 90017",
            phone = "(800) 399-4529",
            tollFreePhone = "1-800-399-4529",
            email = "intake@lafla.org",
            website = "https://lafla.org",
            hours = "Monday – Friday: 9:00 AM – 5:00 PM",
            categories = listOf(ResourceCategory.LEGAL, ResourceCategory.HOUSING),
            services = listOf(
                "Eviction defense and tenant representation in court",
                "Restoring wrongful CalWORKs, CalFresh, and SSI terminations",
                "Emergency restraining orders and domestic safety filings",
                "Expungement clinics to clear criminal records for employment"
            ),
            eligibility = "Income at or below 200% of federal poverty guidelines.",
            intakeProcess = "Call toll-free 1-800-399-4529 or apply online for intake triage.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Cantonese", "Mandarin", "Korean", "Vietnamese")
        ),
        Charity(
            id = "ca-sf-st-anthony",
            name = "St. Anthony Foundation San Francisco",
            tagline = "Tenderloin Dining Room, Medical Clinic & Hygiene Center",
            description = "San Francisco charity serving over 1,500 daily hot meals, free medical care at its community clinic, computer lab job training, and free clothing wardrobe.",
            targetZipCodes = listOf("94102", "94103", "94109", "94110", "94112", "941", "945", "946"),
            city = "San Francisco",
            state = "CA",
            address = "150 Golden Gate Ave, San Francisco, CA 94102",
            phone = "(415) 241-2600",
            email = "info@stanthonysf.org",
            website = "https://www.stanthonysf.org",
            hours = "Dining Room: 10:00 AM – 1:30 PM Daily | Clinic: 8:00 AM – 4:00 PM",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.CLOTHING,
                ResourceCategory.JOBS
            ),
            services = listOf(
                "Free hot lunch every single day without questions or fees",
                "Primary medical clinic with free prescriptions and pediatrics",
                "Free clothing boutique (suits for interviews, shoes, everyday wear)",
                "Technology lab with free computer access, Wi-Fi, and resume assistance"
            ),
            eligibility = "All low-income or unhoused residents of San Francisco.",
            intakeProcess = "Walk-in dining room on Golden Gate Ave. Clinic appointments available on-site.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Cantonese")
        ),

        // ================= TEXAS (75001 - 79999) =================
        Charity(
            id = "tx-houston-star-of-hope",
            name = "Star of Hope Mission",
            tagline = "Emergency Shelter & Transformation for Houston's Needy",
            description = "Community organization helping homeless and low-income men, women, and families through emergency shelter, life skills classes, hot meals, and employment readiness.",
            targetZipCodes = listOf("77001", "77002", "77004", "77020", "77033", "77060", "770", "772", "774"),
            city = "Houston",
            state = "TX",
            address = "2575 Reed Rd, Houston, TX 77051",
            phone = "(713) 748-0700",
            email = "sohcares@sohmission.org",
            website = "https://www.sohmission.org",
            hours = "Intake: Monday – Friday: 8:00 AM – 5:00 PM | Emergency 24/7",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.JOBS,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Emergency family and women shelter with clean private rooms",
                "3 nutritious daily meals and children's nutrition",
                "Job preparation, computer skills, and interview coaching",
                "Medical referrals and prescription co-pay vouchers"
            ),
            eligibility = "Low-income individuals, single women, and families facing housing crisis.",
            intakeProcess = "Call (713) 748-0700 or walk in to the Cornerstone Community campus.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "tx-dallas-crossroads",
            name = "Crossroads Community Services",
            tagline = "Nourishing Dallas Families with Dignity",
            description = "Innovative food distribution charity providing low-income families with nutritious groceries matching dietary needs, SNAP assistance, and community financial aid referrals.",
            targetZipCodes = listOf("75201", "75215", "75216", "75220", "75228", "752", "750", "751"),
            city = "Dallas",
            state = "TX",
            address = "4500 S Cockrell Hill Rd, Dallas, TX 75236",
            phone = "(214) 560-2511",
            email = "info@ccsdallas.org",
            website = "https://ccsdallas.org",
            hours = "Monday – Thursday: 8:30 AM – 2:00 PM",
            categories = listOf(ResourceCategory.FOOD, ResourceCategory.UTILITIES),
            services = listOf(
                "Client-choice grocery shopping (fresh meat, dairy, produce)",
                "Children weekend food packs and summer nutrition",
                "Utility relief and emergency bill triage assistance",
                "Nutrition education and diabetic health food support"
            ),
            eligibility = "Households residing in Dallas County earning under 185% of federal poverty line.",
            intakeProcess = "Schedule an appointment online or walk in during morning distribution hours.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "tx-austin-caritas",
            name = "Caritas of Austin",
            tagline = "Ending Homelessness & Poverty in Central Texas",
            description = "Provides permanent supportive housing, daily community kitchen lunches, workforce development, and emergency rent relief to prevent homelessness.",
            targetZipCodes = listOf("78701", "78702", "78723", "78741", "78753", "787", "786"),
            city = "Austin",
            state = "TX",
            address = "611 Neches St, Austin, TX 78701",
            phone = "(512) 479-4610",
            email = "caritas@caritasofaustin.org",
            website = "https://caritasofaustin.org",
            hours = "Community Lunch: Mon–Fri 11:00 AM – 12:30 PM | Offices: 8:00 AM – 5:00 PM",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING,
                ResourceCategory.JOBS,
                ResourceCategory.UTILITIES
            ),
            services = listOf(
                "Free hot lunch at the Downtown Community Kitchen",
                "Emergency rental subsidies and utility bill grants",
                "Job placement with living-wage employers",
                "Pantry items and fresh produce take-home boxes"
            ),
            eligibility = "Low-income Travis County residents and individuals experiencing homelessness.",
            intakeProcess = "Call (512) 479-4610 or walk into the downtown community center on Neches St.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= ILLINOIS (60001 - 62999) =================
        Charity(
            id = "il-chicago-greater-food-depository",
            name = "Greater Chicago Food Depository",
            tagline = "Chicago's Food Bank & Hunger Action Center",
            description = "Acts as the backbone for over 700 partner food pantries, soup kitchens, and shelters across Cook County, providing free groceries, job training, and benefits enrollment.",
            targetZipCodes = listOf("60601", "60608", "60623", "60629", "60637", "60649", "606", "607", "608"),
            city = "Chicago",
            state = "IL",
            address = "4100 W Ann Lurie Pl, Chicago, IL 60632",
            phone = "(773) 247-3663",
            email = "chicagofood@gcfd.org",
            website = "https://www.chicagofoodbank.org",
            hours = "Monday – Friday: 8:00 AM – 5:00 PM",
            categories = listOf(ResourceCategory.FOOD, ResourceCategory.JOBS),
            services = listOf(
                "Immediate referral to the closest neighborhood walk-in food pantry",
                "SNAP (Link card) food assistance enrollment specialists",
                "Free 14-week accredited culinary training program for job seekers",
                "Senior mobile grocery deliveries and youth cafe sites"
            ),
            eligibility = "All Cook County families and individuals in need of nutritious groceries.",
            intakeProcess = "Call (773) 247-3663 to find your immediate local pantry or get SNAP signup help.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Polish")
        ),
        Charity(
            id = "il-chicago-catholic-charities",
            name = "Catholic Charities of the Archdiocese of Chicago",
            tagline = "Comprehensive Emergency Assistance & Family Support",
            description = "One of the largest non-profit social service agencies in the Midwest, offering emergency food pantries, eviction prevention grants, senior care, and utility cut-off help.",
            targetZipCodes = listOf("60601", "60612", "60619", "60623", "60651", "606", "600", "604"),
            city = "Chicago",
            state = "IL",
            address = "721 N LaSalle St, Chicago, IL 60654",
            phone = "(312) 655-7700",
            tollFreePhone = "(312) 655-7700",
            email = "gethelp@catholiccharities.net",
            website = "https://www.catholiccharities.net",
            hours = "Monday – Friday: 8:30 AM – 4:30 PM",
            categories = listOf(
                ResourceCategory.UTILITIES,
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Emergency utility reconnection assistance (LIHEAP & ComEd Hardship)",
                "Rental assistance to settle eviction proceedings",
                "Walk-in food pantries with fresh produce, protein, and staples",
                "Maternity and infant diapers, formula, and crib distribution"
            ),
            eligibility = "Low-income Cook and Lake County residents. Open to all individuals regardless of religious faith.",
            intakeProcess = "Call Central Intake at (312) 655-7700 to begin application.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Polish", "Arabic")
        ),

        // ================= NEW YORK (10001 - 14925) =================
        Charity(
            id = "ny-nyc-bowery-mission",
            name = "The Bowery Mission",
            tagline = "Serving New Yorkers in Need Since 1879",
            description = "Serving high-need communities in Manhattan and the Bronx with daily hot meals, emergency shelter, clean showers, new clothing, and residential recovery programs.",
            targetZipCodes = listOf("10001", "10002", "10009", "10029", "10451", "10456", "11201", "11212", "100", "104", "112"),
            city = "New York",
            state = "NY",
            address = "227 Bowery, New York, NY 10002",
            phone = "(212) 674-3456",
            email = "info@bowery.org",
            website = "https://www.bowery.org",
            hours = "Daily Meals: 8:00 AM, 1:00 PM, 5:00 PM | Services: 24/7",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING,
                ResourceCategory.CLOTHING,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "3 hot daily meals (no ID, no paperwork required)",
                "Emergency overnight shelter for men and women",
                "Hot showers, clean underwear, socks, and seasonal clothing",
                "Doctor visits, optometry exams, and flu shots on select clinic days"
            ),
            eligibility = "Anyone in need of immediate food, shelter, or hygiene.",
            intakeProcess = "Walk in to 227 Bowery for meal service or shower sign-up.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "ny-nyc-food-bank",
            name = "Food Bank For New York City",
            tagline = "Major Hunger-Relief Organization across 5 Boroughs",
            description = "Distributes tens of millions of free meals annually through soup kitchens, community pantries, and public schools, plus free tax prep and SNAP support.",
            targetZipCodes = listOf("10001", "10027", "10029", "10451", "11212", "11354", "10301", "100", "104", "111", "112", "113", "114"),
            city = "New York",
            state = "NY",
            address = "252 W 116th St, New York, NY 10026",
            phone = "(212) 566-7855",
            email = "contactus@foodbanknyc.org",
            website = "https://www.foodbanknyc.org",
            hours = "Monday – Friday: 9:00 AM – 5:00 PM",
            categories = listOf(ResourceCategory.FOOD, ResourceCategory.JOBS, ResourceCategory.LEGAL),
            services = listOf(
                "Community kitchen serving hot dinners Mon-Fri in Harlem",
                "Food pantry distribution with fresh fruit and pantry staples",
                "Free IRS tax preparation yielding maximum Earned Income Tax Credits",
                "SNAP food stamps screening and recertification help"
            ),
            eligibility = "NYC residents with low or moderate income.",
            intakeProcess = "Call (212) 566-7855 or visit the West Harlem Community Kitchen directly.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Cantonese", "French")
        ),

        // ================= FLORIDA (32003 - 34997) =================
        Charity(
            id = "fl-miami-camillus-house",
            name = "Camillus House Miami",
            tagline = "Compassionate Healthcare, Shelter & Meals in South Florida",
            description = "Provides comprehensive humanitarian services to persons who are poor and homeless in Miami-Dade, including emergency housing, health clinics, and hot meals.",
            targetZipCodes = listOf("33101", "33125", "33127", "33130", "33136", "33142", "331", "330", "332"),
            city = "Miami",
            state = "FL",
            address = "1603 NW 7th Ave, Miami, FL 33136",
            phone = "(305) 374-1065",
            email = "info@camillus.org",
            website = "https://www.camillus.org",
            hours = "Day Center: Mon–Fri 7:00 AM – 3:00 PM | Emergency Shelter: 24/7",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Nutritious breakfast and lunch daily in the main dining hall",
                "Emergency short-term and transitional housing program",
                "Free medical clinic, dental extractions, and mental health therapy",
                "Shower facilities, fresh laundry service, and clean change of clothes"
            ),
            eligibility = "Individuals and families experiencing homelessness or extreme poverty in Miami-Dade.",
            intakeProcess = "Walk in to the Day Center entrance at NW 7th Ave for meals and day services.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Haitian Creole")
        ),
        Charity(
            id = "fl-orlando-coalition-homeless",
            name = "Coalition for the Homeless of Central Florida",
            tagline = "Transforming Lives with Shelter & Stability",
            description = "Largest provider of homeless services in Central Florida, offering family crisis shelter, women's shelter, hot meals, educational daycare, and housing case management.",
            targetZipCodes = listOf("32801", "32805", "32808", "32811", "32822", "328", "327"),
            city = "Orlando",
            state = "FL",
            address = "18 N Terry Ave, Orlando, FL 32801",
            phone = "(407) 426-1250",
            email = "help@cflhomeless.org",
            website = "https://www.centralfloridahomeless.org",
            hours = "Open 24/7 for emergency crisis housing",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.JOBS
            ),
            services = listOf(
                "Emergency residential shelter for families with children",
                "Nutritious daily meals prepared in the community kitchen",
                "Early childhood development program for sheltered infants/toddlers",
                "Rapid re-housing financial assistance and deposit grants"
            ),
            eligibility = "Homeless families with children, single women, and individuals.",
            intakeProcess = "Intake begins at 8:30 AM weekdays at the main intake center.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= GEORGIA (30002 - 31999) =================
        Charity(
            id = "ga-atlanta-mission",
            name = "Atlanta Mission (The Shepherd's Inn & My Sister's House)",
            tagline = "Emergency Shelter & Life-Transformation Programs",
            description = "Provides emergency shelter, meals, healthcare access, and vocational training to over 1,000 men, women, and children experiencing homelessness daily in Atlanta.",
            targetZipCodes = listOf("30301", "30303", "30310", "30314", "30315", "30318", "303", "300", "301"),
            city = "Atlanta",
            state = "GA",
            address = "165 Luckie St NW, Atlanta, GA 30303",
            phone = "(404) 588-4000",
            email = "contact@atlantamission.org",
            website = "https://atlantamission.org",
            hours = "Intake: Daily 8:00 AM – 7:00 PM | Meals: Daily",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.JOBS,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Safe emergency overnight shelter for men, women, and mothers",
                "Warm breakfast, lunch, and dinner services",
                "Career readiness training, computer literacy, and job search center",
                "Clothing closets with clean shoes, socks, and seasonal coats"
            ),
            eligibility = "Men, women, and children without stable shelter.",
            intakeProcess = "Call (404) 588-4000 or walk in to the Luckie St campus.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),
        Charity(
            id = "ga-atlanta-action-agency",
            name = "Fulton County Community Action Authority",
            tagline = "Empowering Low-Income Families to Reach Self-Sufficiency",
            description = "Designated Community Action Agency providing emergency utility bill assistance (LIHEAP), rental relief grants, job certification training, and family crisis aid.",
            targetZipCodes = listOf("30301", "30310", "30311", "30315", "30344", "303", "302"),
            city = "Atlanta",
            state = "GA",
            address = "1690 Pryor Rd SW, Atlanta, GA 30315",
            phone = "(404) 320-0166",
            email = "info@fcaa.org",
            website = "https://fultoncommunityaction.org",
            hours = "Monday – Friday: 8:30 AM – 5:00 PM",
            categories = listOf(
                ResourceCategory.UTILITIES,
                ResourceCategory.HOUSING,
                ResourceCategory.JOBS
            ),
            services = listOf(
                "Emergency energy crisis assistance (cooling & heating payments)",
                "Rent and mortgage mortgage arrears emergency funding",
                "Workforce development and vocational scholarship programs",
                "Emergency food pantry vouchers and grocery deliveries"
            ),
            eligibility = "Fulton County residents living at or below 200% federal poverty line.",
            intakeProcess = "Call (404) 320-0166 or schedule appointment online for document intake.",
            isWalkInFriendly = false,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= PENNSYLVANIA (15001 - 19640) =================
        Charity(
            id = "pa-phila-project-home",
            name = "Project HOME Philadelphia",
            tagline = "Housing, Medical Care & Education for Vulnerable Neighbors",
            description = "Nationally acclaimed non-profit providing supportive housing, street outreach, free healthcare through the Stephen Klein Wellness Center, and adult education.",
            targetZipCodes = listOf("19102", "19121", "19132", "19133", "19139", "19140", "191", "190"),
            city = "Philadelphia",
            state = "PA",
            address = "1515 Fairmount Ave, Philadelphia, PA 19130",
            phone = "(215) 232-7272",
            tollFreePhone = "(215) 232-1984 (Outreach)",
            email = "info@projecthome.org",
            website = "https://www.projecthome.org",
            hours = "Outreach hotline 24/7 | Hubs: Mon–Fri 8:30 AM – 5:00 PM",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.FOOD,
                ResourceCategory.JOBS
            ),
            services = listOf(
                "Permanent supportive and affordable family housing",
                "Full primary healthcare, dentistry, and pharmacy at Stephen Klein Wellness",
                "Employment services, computer lab training, and college prep",
                "24/7 homeless outreach team dispatch across Philadelphia"
            ),
            eligibility = "Low-income Philadelphians and individuals transitioning out of homelessness.",
            intakeProcess = "Call Outreach Hotline at (215) 232-1984 or walk in to the Hub of Hope.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= MICHIGAN (48001 - 49971) =================
        Charity(
            id = "mi-detroit-capuchin",
            name = "Capuchin Soup Kitchen & Services Center",
            tagline = "Nourishing Detroit with Hot Meals, Clothing & Family Aid",
            description = "Operating six sites across Detroit providing over 1,500 daily hot meals, emergency food pantry groceries, children's tutoring, and substance abuse counseling.",
            targetZipCodes = listOf("48201", "48202", "48205", "48207", "48211", "48219", "482", "480", "481"),
            city = "Detroit",
            state = "MI",
            address = "1820 Mount Elliott St, Detroit, MI 48207",
            phone = "(313) 579-2100",
            email = "info@cskdetroit.org",
            website = "https://www.cskdetroit.org",
            hours = "Meal Sites: Mon–Sat 8:30 AM – 4:00 PM | Services Center: Mon–Fri",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.CLOTHING,
                ResourceCategory.UTILITIES
            ),
            services = listOf(
                "Free hot meals served twice daily to anyone who walks in",
                "Emergency food pantry grocery distribution",
                "Free clothing distribution center (clothing, shoes, coats)",
                "Emergency appliance and utility relief vouchers"
            ),
            eligibility = "Anyone in need. No proof of income required for soup kitchen.",
            intakeProcess = "Walk into the Mount Elliott or Meldrum soup kitchens directly for meals.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Arabic")
        ),

        // ================= COLORADO (80001 - 81658) =================
        Charity(
            id = "co-denver-rescue-mission",
            name = "Denver Rescue Mission",
            tagline = "Changing Lives in the Rocky Mountain Region",
            description = "Oldest full-service charity in the Rocky Mountain region offering emergency shelter, hot meals, medical clinics, and transitional family housing.",
            targetZipCodes = listOf("80202", "80204", "80205", "80216", "80219", "802", "800", "801"),
            city = "Denver",
            state = "CO",
            address = "1130 Park Ave W, Denver, CO 80205",
            phone = "(303) 297-1815",
            email = "info@denverrescuemission.org",
            website = "https://denverrescuemission.org",
            hours = "Open 24/7 for emergency shelter | Meals served 3 times daily",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Up to 1,000 emergency beds per night with safe locker storage",
                "Breakfast, lunch, and dinner provided daily without cost",
                "Free medical and dental triage clinic on-site",
                "Winter weather gear (parkas, thermal socks, boots, blankets)"
            ),
            eligibility = "Individuals and families experiencing homelessness or extreme financial distress.",
            intakeProcess = "Walk in to the Lawrence Street Community Center for day services and bed reservation.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= WASHINGTON (98001 - 99403) =================
        Charity(
            id = "wa-seattle-desc",
            name = "Downtown Emergency Service Center (DESC)",
            tagline = "Integrated Health, Shelter & Crisis Solutions",
            description = "Non-profit organization in King County serving highly vulnerable adults with emergency shelter, permanent supportive housing, behavioral health, and crisis intervention.",
            targetZipCodes = listOf("98101", "98104", "98108", "98118", "98125", "981", "980", "982"),
            city = "Seattle",
            state = "WA",
            address = "515 3rd Ave, Seattle, WA 98104",
            phone = "(206) 464-1570",
            email = "info@desc.org",
            website = "https://www.desc.org",
            hours = "Crisis and Shelter Services open 24/7",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.CRISIS,
                ResourceCategory.FOOD
            ),
            services = listOf(
                "24/7 low-barrier emergency shelter with beds and storage",
                "Onsite integrated nursing, psychiatric care, and medication management",
                "Daily hot meals and clean drinking water stations",
                "Direct connection to permanent supportive apartments"
            ),
            eligibility = "Unhoused and low-income adults in Seattle and King County.",
            intakeProcess = "Walk into 515 3rd Ave or call 24/7 at (206) 464-1570.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Amharic", "Somali")
        ),

        // ================= ARIZONA (85001 - 86556) =================
        Charity(
            id = "az-phx-st-vincent-de-paul",
            name = "St. Vincent de Paul Society of Arizona",
            tagline = "Feeding, Clothing, Housing & Healing in the Southwest",
            description = "Comprehensive charity campus featuring five community dining rooms, a free dental & medical clinic for uninsured families, and emergency rent relief.",
            targetZipCodes = listOf("85001", "85003", "85009", "85033", "85040", "850", "852", "853"),
            city = "Phoenix",
            state = "AZ",
            address = "420 W Watkins Rd, Phoenix, AZ 85003",
            phone = "(602) 850-6900",
            email = "info@svdpaz.org",
            website = "https://www.stvincentdepaul.net",
            hours = "Dining Room: 11:00 AM – 1:00 PM Daily | Help Line: Mon–Fri",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HEALTHCARE,
                ResourceCategory.UTILITIES,
                ResourceCategory.CLOTHING
            ),
            services = listOf(
                "Free warm meals in 5 neighborhood dining halls across the valley",
                "Virginia G. Piper Free Medical & Dental Clinic for uninsured families",
                "Direct financial grants to avert eviction and power disconnection",
                "Free thrift store clothing and household furniture vouchers"
            ),
            eligibility = "Uninsured, low-income families and individuals in Maricopa County.",
            intakeProcess = "Call (602) 850-6900 for utility/rent assistance; walk into dining halls for meals.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        ),

        // ================= MASSACHUSETTS (01001 - 02791) =================
        Charity(
            id = "ma-boston-pine-street-inn",
            name = "Pine Street Inn",
            tagline = "Ending Homelessness with Hope, Dignity & Housing",
            description = "New England's largest homeless services organization, providing emergency shelter, street outreach vans, workforce training, and supportive permanent housing.",
            targetZipCodes = listOf("02101", "02111", "02118", "02119", "02124", "021", "022", "024"),
            city = "Boston",
            state = "MA",
            address = "444 Harrison Ave, Boston, MA 02118",
            phone = "(617) 892-9100",
            email = "info@pinestreetinn.org",
            website = "https://www.pinestreetinn.org",
            hours = "Emergency shelter & Mobile Outreach: 24/7",
            categories = listOf(
                ResourceCategory.HOUSING,
                ResourceCategory.FOOD,
                ResourceCategory.JOBS,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "Emergency shelter beds with lockers and hot showers",
                "Mobile outreach vans delivering sandwiches, warm soup, and blankets",
                "iCater food service job training and culinary certification",
                "Clinical behavioral health and Boston Healthcare for the Homeless clinic access"
            ),
            eligibility = "Unhoused adults and low-income individuals in Greater Boston.",
            intakeProcess = "Walk into 444 Harrison Ave or call (617) 892-9100.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Haitian Creole", "Cape Verdean Creole")
        ),

        // ================= OHIO (43001 - 45999) =================
        Charity(
            id = "oh-columbus-mid-ohio-food-collective",
            name = "Mid-Ohio Food Collective",
            tagline = "Ending Hunger Across 20 Central and Eastern Ohio Counties",
            description = "Major food collective connecting low-income Ohioans with fresh food markets, mobile markets, pharmacy pantries, and SNAP benefit sign-up.",
            targetZipCodes = listOf("43201", "43204", "43207", "43211", "43215", "432", "430", "431"),
            city = "Columbus",
            state = "OH",
            address = "3960 Brookham Dr, Grove City, OH 43123",
            phone = "(614) 277-3663",
            email = "info@mofc.org",
            website = "https://mofc.org",
            hours = "Monday – Friday: 8:00 AM – 4:30 PM",
            categories = listOf(ResourceCategory.FOOD, ResourceCategory.HEALTHCARE),
            services = listOf(
                "Free walk-in Mid-Ohio Market shopping with fresh produce and meats",
                "On-site healthcare pantry for medically-tailored groceries",
                "Emergency home delivery boxes for seniors and homebound residents",
                "Direct help applying for Ohio SNAP and Medicaid"
            ),
            eligibility = "Households at or below 200% federal poverty line in Central Ohio.",
            intakeProcess = "Call (614) 277-3663 to find the closest neighborhood market location.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish", "Somali")
        ),

        // ================= TENNESSEE (37010 - 38589) =================
        Charity(
            id = "tn-nashville-rescue-mission",
            name = "Nashville Rescue Mission",
            tagline = "Hope for the Hurting in Middle Tennessee",
            description = "Serving thousands of meals, shelter beds, clothing, and recovery guidance for men, women, and children seeking help in Nashville.",
            targetZipCodes = listOf("37201", "37203", "37207", "37208", "37210", "372", "370", "371"),
            city = "Nashville",
            state = "TN",
            address = "639 Lafayette St, Nashville, TN 37203",
            phone = "(615) 255-2475",
            email = "info@nashvillerescuemission.org",
            website = "https://nashvillerescuemission.org",
            hours = "Shelter open 24/7 | Breakfast 6:30am, Lunch 11:30am, Dinner 5:30pm",
            categories = listOf(
                ResourceCategory.FOOD,
                ResourceCategory.HOUSING,
                ResourceCategory.CLOTHING,
                ResourceCategory.HEALTHCARE
            ),
            services = listOf(
                "Free hot meals served 3 times daily to anyone in need",
                "Emergency overnight shelter for men, women, and mothers with children",
                "Free clothing distribution, shoes, and hygiene kits",
                "Medical clinic access and case management"
            ),
            eligibility = "Individuals and families in need of immediate shelter or food.",
            intakeProcess = "Walk into 639 Lafayette St for men's campus or 1716 Rosa L Parks Blvd for women and children.",
            isWalkInFriendly = true,
            isFreeService = true,
            languagesSupported = listOf("English", "Spanish")
        )
    )
}
