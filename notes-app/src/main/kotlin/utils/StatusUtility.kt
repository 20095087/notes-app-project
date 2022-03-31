package utils

object StatusUtility {
    // creating a set of statuses that the user needs to chose from
    @JvmStatic
    val statuses = setOf("ToDo", "Doing", "Done")

    // validating if the users status that has been passed in is
    // equal to one from our set
    @JvmStatic
    fun isValidStatus(statusToCheck: String): Boolean{
        for(status in statuses){
            if(status.equals(statusToCheck, ignoreCase = true)){
                return true
            }
        }
        return false
    }
}