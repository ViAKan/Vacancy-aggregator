package ru.practicum.android.diploma.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import ru.practicum.android.diploma.domain.models.salary.Salary
import ru.practicum.android.diploma.domain.models.vacancydetails.EmploymentForm

class TypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSalary(salary: Salary): String {
        val json = JSONObject()

        when (salary) {
            is Salary.NotSpecifies -> {
                json.put(TYPE, SalaryType.NOT_SPECIFIED.value)
            }

            is Salary.From -> {
                json.put(TYPE, SalaryType.FROM.value)
                json.put(AMOUNT, salary.amount)
                json.put(CURRENCY, salary.currency)
            }

            is Salary.Range -> {
                json.put(TYPE, SalaryType.RANGE.value)
                json.put(FROM, salary.from)
                json.put(TO, salary.to)
                json.put(CURRENCY, salary.currency)
            }

            is Salary.Fixed -> {
                json.put(TYPE, SalaryType.FIXED.value)
                json.put(AMOUNT, salary.amount)
                json.put(CURRENCY, salary.currency)
            }
        }
        return json.toString()
    }

    @TypeConverter
    fun toSalary(json: String): Salary {
        val obj = JSONObject(json)
        val type = SalaryType.from(obj.getString(TYPE))
        return when (type) {
            SalaryType.NOT_SPECIFIED -> Salary.NotSpecifies
            SalaryType.FROM -> Salary.From(
                obj.getInt(AMOUNT),
                obj.getString(CURRENCY)
            )

            SalaryType.RANGE -> Salary.Range(
                obj.getInt(FROM),
                obj.getInt(TO),
                obj.getString(CURRENCY)
            )

            SalaryType.FIXED -> Salary.Fixed(
                obj.getInt(AMOUNT),
                obj.getString(CURRENCY)
            )
        }
    }

    @TypeConverter
    fun fromEmploymentForm(employmentForm: EmploymentForm?): String? {
        return employmentForm?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toEmploymentForm(json: String?): EmploymentForm? {
        return json?.let {
            gson.fromJson(it, EmploymentForm::class.java)
        }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        return json?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, type)
        }
    }

    companion object {
        private const val TYPE = "type"
        private const val AMOUNT = "amount"
        private const val CURRENCY = "currency"
        private const val TO = "to"
        private const val FROM = "from"
    }
}

enum class SalaryType(val value: String) {
    NOT_SPECIFIED("NotSpecifies"),
    FROM("From"),
    RANGE("Range"),
    FIXED("Fixed");

    companion object {
        fun from(value: String): SalaryType =
            entries.find { it.value == value } ?: throw IllegalArgumentException("Unknown salary type: $value")
    }
}
