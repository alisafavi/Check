package aliSafavi.check.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    foreignKeys = [
        ForeignKey(entity = Person::class,parentColumns = ["pId"],childColumns = ["personId"],onDelete = CASCADE),
        ForeignKey(entity = Bank::class,parentColumns = ["bId"],childColumns = ["bankId"],onDelete = CASCADE)
    ]
)
data class Check(
    var number: Long,
    var date: Long,
    var amount : Long,
    val isPaid : Boolean = false,
    var personId: Int?,
    var bankId: Int?,
    val reminderTime:Long,
    @PrimaryKey(autoGenerate = true)
    val cId: Long = 0
)

@Entity
data class Person (
    val name :String,
    val phoneNumber : Long? = null,
    @PrimaryKey(autoGenerate = true)
    val pId :Int =0
)

@Entity
data class Bank (
    var name : String,
    val accountNumber : Long,
    @PrimaryKey(autoGenerate = true)
    val bId: Int=0
){
    override fun toString(): String {
        return name
    }
}

data class FullCheck(
    @Embedded
    val check :Check,
    @Relation(
        parentColumn = "bankId",
        entityColumn = "bId"
    )
    val bank :Bank?,
    @Relation(
        parentColumn = "personId",
        entityColumn = "pId"
    )
    val person : Person?
)