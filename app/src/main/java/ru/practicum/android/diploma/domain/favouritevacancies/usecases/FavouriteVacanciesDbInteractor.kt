package ru.practicum.android.diploma.domain.favouritevacancies.usecases

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entyties.FavouriteVacancy
import ru.practicum.android.diploma.domain.models.vacancies.Vacancy
import ru.practicum.android.diploma.domain.models.vacancydetails.VacancyDetails

interface FavouriteVacanciesDbInteractor {
    suspend fun insertVacancy(vacancy: VacancyDetails)
    suspend fun deleteVacancy(vacancy: VacancyDetails)
    fun getFavouriteVacancies(): Flow<List<Vacancy>>
    suspend fun getVacancyById(id: String): FavouriteVacancy?
}
