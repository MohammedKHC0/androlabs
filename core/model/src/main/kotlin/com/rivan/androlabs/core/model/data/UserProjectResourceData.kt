package com.rivan.androlabs.core.model.data

/**
 * Class summarizing user's project resource data.
 */
data class UserProjectResourceData(
    val favouriteProjectResources: Set<String>,
    val completedProjectResources: Set<String>
)