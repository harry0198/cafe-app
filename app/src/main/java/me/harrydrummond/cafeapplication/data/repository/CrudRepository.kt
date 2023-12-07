package me.harrydrummond.cafeapplication.data.repository

/**
 * A generic interface for performing CRUD (Create Read Update Delete) functions.
 */
interface CrudRepository<T> {

    /**
     * Save [T] to the database.
     *
     * @param type [T] to save.
     * @return Id was saved at. -1 if failed to save. -3 if conflict.
     */
    fun save(type: T): Int

    /**
     * Update [T] in the database.
     *
     * @param type [T] to update. Must have an ID.
     * @return If successfully updated or not.
     */
    fun update(type: T): Boolean

    /**
     * Get a [T] by their ID
     *
     * @param id Id of the [T] to fetch
     * @return [T] if exists or null.
     */
    fun getById(id: Int): T?

    /**
     * Deletes a [T] from the database.
     *
     * @param type [T] to delete
     * @return If delete was successful or not.
     */
    fun delete(type: T): Boolean
}