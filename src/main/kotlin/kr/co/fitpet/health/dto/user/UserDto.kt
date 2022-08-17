package kr.co.fitpet.health.dto.user

data class UserDto (

    var id: Int,

    var name: String? = null,

    var email: String? = null,

    var mobileNumber: String,

    var birthday: String,

    var userStatus: String,

    var mileageId: Int,

    var displayPetType: String,
)