package ru.practicum.android.diploma.data.db.converter

import ru.practicum.android.diploma.data.db.entyties.FavouriteVacancy
import ru.practicum.android.diploma.domain.models.vacancies.Vacancy
import ru.practicum.android.diploma.domain.models.vacancydetails.VacancyDetails

fun FavouriteVacancy.toDomain(): Vacancy {
    return Vacancy(
        nameVacancy = name,
        alternateUrl = alternateUrl,
        id = id,
        employerName = employmentForm?.name,
        logo = logoUrl,
        salary = salary,
        city = city,
        timestamp = timestamp
    )
}

fun VacancyDetails.toData(): FavouriteVacancy {
    return FavouriteVacancy(
        id = id,
        name = name,
        alternateUrl = alternateUrl,
        employer = employer,
        experience = experience,
        employmentForm = employmentForm,
        description = description,
        workFormat = workFormat,
        keySkills = keySkills,
        logoUrl = logoUrl,
        salary = salary,
        city = city,
        timestamp = System.currentTimeMillis()
    )
}

fun FavouriteVacancy.toDetails(): VacancyDetails {
    return VacancyDetails(
        id = id,
        name = name,
        salary = salary,
        employer = employer,
        experience = experience,
        employmentForm = employmentForm,
        description = description,
        workFormat = workFormat,
        alternateUrl = alternateUrl,
        keySkills = keySkills,
        city = city,
        logoUrl = logoUrl
    )
}
