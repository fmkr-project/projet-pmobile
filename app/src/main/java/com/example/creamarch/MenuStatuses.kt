package com.example.creamarch

data class MenuStatus(
	var collectionPopupIsOpen: Boolean = false,
	var collectionPopupSpecies: CreatureSpecies = Dex.species[1]!!
)
{
	fun toggleCollectionPopup() {
		collectionPopupIsOpen = !this.collectionPopupIsOpen
	}

	fun openCollectionPopup()
	{
		collectionPopupIsOpen = true
	}
}