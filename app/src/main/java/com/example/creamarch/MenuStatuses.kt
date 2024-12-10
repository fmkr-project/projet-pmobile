package com.example.creamarch

// Data class to manage the status of the menu (e.g., whether the collection popup is open)
data class MenuStatus(
	var collectionPopupIsOpen: Boolean = false,  // Flag to check if the collection popup is open
	var collectionPopupSpecies: CreatureSpecies = Dex.species[1]!!  // The species currently displayed in the collection popup
)
{
	// Toggles the state of the collection popup (open/closed)
	fun toggleCollectionPopup() {
		collectionPopupIsOpen = !this.collectionPopupIsOpen
	}

	// Opens the collection popup
	fun openCollectionPopup()
	{
		collectionPopupIsOpen = true
	}
}
