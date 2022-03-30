package utils

object CategoryUtility {
    // creating a set of categories that the user needs to chose from
    @JvmStatic
    val categories = setOf("Home","College", "Shopping", "Bills", "Other")

    @JvmStatic
    fun isValidCategory(categoryToCheck: String): Boolean{
        for(category in categories){
            if(category.equals(categoryToCheck, ignoreCase = true)){
                return true
            }
        }
        return false
    }
}